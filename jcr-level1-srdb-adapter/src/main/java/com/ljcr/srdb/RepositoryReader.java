package com.ljcr.srdb;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableNodeScalar;
import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.ItemNotFoundException;
import com.ljcr.srdb.mods.visitors.*;
import com.ljcr.srdb.readers.*;
import com.ljcr.srdb.readers.ImmutableRelationsObject;
import com.ljcr.srdb.readers.ImmutableResourceNoRelObject;
import com.ljcr.utils.ImmutableResourceWithComplement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.ljcr.srdb.TypeSchema.metaSchemaOf;
import static java.util.stream.Collectors.*;

public class RepositoryReader {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    static final TypeDefinition META_FIELD = new FieldSchema();
    static final TypeSchema META_SCHEMA = metaSchemaOf(META_FIELD);

    private final ResourceRepository res;

    private final RelationRepository rels;

    public RepositoryReader(ResourceRepository res, RelationRepository rels) {
        this.res = res;
        this.rels = rels;
    }

    /**
     * Reads whole relational repository in memory and returns its root node
     *
     * @return root node of the repository
     */
    public ImmutableNode readSchema() {
        Iterable<Resource> types = res.allOf(META_SCHEMA);
        Iterable<Resource> fields = res.childrenOf(META_SCHEMA);
        Iterable<ResourceRelation> relations = rels.allRelationsOfType(META_SCHEMA);

        logger.info("Types");
        types.forEach(r -> logger.info("Resource: {}", r));

        fields.forEach(fr -> logger.info("Field: {}", fr));

        Map<Long, Resource> typeById = StreamSupport.stream(types.spliterator(), false)
                .collect(Collectors.toMap(rel -> rel.getId(), Function.identity()));

        Map<Long, Resource> fieldById = StreamSupport.stream(fields.spliterator(), false)
                .collect(Collectors.toMap(rel -> rel.getId(), Function.identity()));

        Map<Long, List<ResourceRelation>> relationsByParentId = StreamSupport.stream(relations.spliterator(), false)
                .collect(Collectors.groupingBy(rr -> rr.getParent().getId(), LinkedHashMap::new, toList()));

        Map<String, TypeDefinition> typesByRef = new HashMap<>();
        StandardTypes.getPrimitiveTypes().forEach(st -> {
            typesByRef.put(st.getIdentifier(), st);
            typeById.put(st.getNumericCode() * 1L, new Resource(st.getNumericCode() * 1L, StandardTypes.TYPEDEF.getNumericCode() * 1L, st.getIdentifier()));
        });
        StandardTypes.getSpecialTypes().forEach(st -> {
            typesByRef.put(st.getIdentifier(), st);
            typeById.put(st.getNumericCode() * 1L, new Resource(st.getNumericCode() * 1L, StandardTypes.TYPEDEF.getNumericCode() * 1L, st.getIdentifier()));
        });

        relationsByParentId.entrySet().forEach(te -> {
            Resource typeRes = typeById.get(te.getKey());
            List<PropertyDefinition> fieldTypes = te.getValue().stream()
                    .map(rr -> {
                        Long fid = rr.getChild().getId();
                        Resource fieldRes = fieldById.get(fid);
                        String name = fieldRes.getReference().substring(typeRes.getReference().length() + 1);
                        Resource fieldTypeRes = typeById.get(fieldRes.getTypeId());
                        TypeDefinition type = typesByRef.get(fieldTypeRes.getReference());
                        return StandardTypes.propertyOf(name, type);
                    })
                    .collect(toList());

            TypeSchema type = new TypeSchema(typeRes.getReference(), fieldTypes.stream().collect(toMap(p -> p.getIdentifier(), Function.identity())));
            typesByRef.put(typeRes.getReference(), type);

            logger.info("Found: {} -> {}", typeRes, fieldTypes);
        });


        return null;
    }

    public ImmutableNode findResource(String reference, TypeDefinition type) {
        Resource objResource = res.findObject(reference, type)
                .orElseThrow(() -> new ItemNotFoundException("ref=" + reference + ",type=" + type));

        Iterable<ResourceRelation> relations = rels.allRelationsOfRes(objResource);
        Map<String, List<ResourceRelation>> relationMap = StreamSupport.stream(relations.spliterator(), false)
                .collect(groupingBy((ResourceRelation r) -> fieldNameOf(r.getChild()),
                        //.getReference(),
                        toList()));

        TypeDefinition actualType = actualTypeOf(reference, type);

        if (relationMap.isEmpty()) {
            return new ImmutableResourceNoRelObject(actualType, objResource);
        }

        StandardTypeVisitor<ImmutableNodeScalar> simpleTypesFactory = eagerRelationFactory();
        PropertyFactory simplePropertiesFactory = p -> getValueFromMap(simpleTypesFactory, p, relationMap);

        ImmutableRelationsObject imRelations = new ImmutableRelationsObject(actualType, simplePropertiesFactory);
        ImmutableResourceNoRelObject imReferencable = new ImmutableResourceNoRelObject(referencableType(), objResource);
        return new ImmutableResourceWithComplement(imReferencable, imRelations);
    }

    private TypeDefinition referencableType() {
        return null;
    }

    private String fieldNameOf(Resource child) {
        String reference = child.getReference();
        // is this false?
        int idx = reference.indexOf(".");
        return idx > 0 ? reference.substring(idx + 1) : reference;
    }

    private ImmutableNodeScalar getValueFromMap(StandardTypeVisitor<ImmutableNodeScalar> factory, PropertyDefinition p, Map<String, List<ResourceRelation>> relationMap) {
        try {
            final String identifier = p.getIdentifier();
            final List<ResourceRelation> context = relationMap.get(identifier);
            return p.getType()
                    .accept(factory, context);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to fetch " + p + " from map: " + relationMap, e);
        } catch (RuntimeException e) {
            throw new IllegalStateException("Failed to fetch " + p + " from map: " + relationMap, e);
        }
    }

    private TypeDefinition actualTypeOf(String reference, TypeDefinition type) {
        if (type.equals(StandardTypes.TYPEDEF)) {
            return META_SCHEMA;
        }
        return type;
    }

    public ImmutableNode findResourceLazy(String reference, TypeDefinition type) {
        Resource objResource = res.findObject(reference, type)
                .orElseThrow(() -> new ItemNotFoundException("ref=" + reference + ",type=" + type));


        StandardTypeVisitor<ImmutableNodeScalar> simpleTypesFactory = lazyRelationFactory();
        PropertyFactory simplePropertiesFactory = p -> {
            Iterable<ResourceRelation> relations = rels.relationsBetween(objResource, p);
            return p.getType()
                    .accept(simpleTypesFactory, relations);
        };

        return new ImmutableRelationsObject(type, simplePropertiesFactory);
    }

    /**
     * Eager relation factory supposes that all relations are already in memory,
     * i.e. source object is of type {code}Collection<ResourceRelation>{code}
     *
     * @return
     */
    private StandardTypeVisitor<ImmutableNodeScalar> eagerRelationFactory() {
        StandardTypeVisitor<Boolean> nullFilter = new ValuePredicate(obj -> obj == null);
        StandardTypeVisitor<Boolean> typeFilter = new ValuePredicate(obj -> obj instanceof ResourceRelation || obj instanceof Collection<?>);

        StandardTypeVisitor<Object> relationToPrimitive = new RelationToValueVisitor();
        StandardTypeVisitor<Object> relationToObject = new DelegatingVisitor<Object>(relationToPrimitive) {
            @Override
            public Object visit(StandardTypes.TypeDefinitionType type, Object context) {
                TypeDefinition fieldDef = actualTypeOf(type.getIdentifier(), type);
                Resource objResource = ((ResourceRelation) context).getChild();
                return new ImmutableResourceNoRelObject(fieldDef, objResource);
            }

            @Override
            public List<Object> visit(StandardTypes.ArrayType type, Object context) {
                TypeDefinition valueType = type.getValueType();
                Collection<ResourceRelation> res = (Collection<ResourceRelation>) context;
                if (res == null || res.isEmpty()) {
                    return Collections.emptyList();
                }
                return res.stream()
                        .map(rel -> valueType.accept(this, rel))
                        .collect(toList());
            }
        };

        StandardTypeVisitor<ImmutableNodeScalar> objectToValue = new SimpleValueNodeFactory();
        StandardTypeVisitor<ImmutableNodeScalar> relationsToValue = new ChainingVisitor<>(relationToObject, objectToValue);
        StandardTypeVisitor<ImmutableNodeScalar> ifMatchConvert = new ConditionalVisitorOrThrow<>(typeFilter, relationsToValue);

        StandardTypeVisitor<ImmutableNodeScalar> nullFactory = new NullValueFactory<>();
        StandardTypeVisitor<ImmutableNodeScalar> nullOrValue = new ConditionalVisitorOrElse<>(nullFilter, nullFactory, ifMatchConvert);

        return nullOrValue;
    }

    /**
     * Lazy relation factory supposes that relations were queried from the storage.
     * And are of type {code}Iterable<ResourceRelation>{code}
     *
     * @return
     */
    private StandardTypeVisitor<ImmutableNodeScalar> lazyRelationFactory() {
        StandardTypeVisitor<Boolean> typeFilter = new ValuePredicate(obj -> obj instanceof Iterable<?>);
        StandardTypeVisitor<Object> relationToObject = new RelationToValueVisitor();
        StandardTypeVisitor<ImmutableNodeScalar> objectToValue = new SimpleValueNodeFactory();
        StandardTypeVisitor<ImmutableNodeScalar> relationsToValue = new ChainingVisitor<>(relationToObject, objectToValue);
        StandardTypeVisitor<ImmutableNodeScalar> compositeVisitor = new ConditionalVisitorOrThrow<>(typeFilter, relationsToValue);
        return compositeVisitor;
    }
}
