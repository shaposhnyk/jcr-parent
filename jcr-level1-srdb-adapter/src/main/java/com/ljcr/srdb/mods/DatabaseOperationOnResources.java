package com.ljcr.srdb.mods;

import com.ljcr.srdb.Resource;
import com.ljcr.srdb.ResourceRelation;
import org.springframework.data.repository.CrudRepository;

public class DatabaseOperationOnResources implements DatabaseOperationVisitor {
    private final CrudRepository<Resource, Long> resRepo;
    private final CrudRepository<ResourceRelation, Long> relsRepo;

    public DatabaseOperationOnResources(CrudRepository<Resource, Long> resRepo, CrudRepository<ResourceRelation, Long> relsRepo) {
        this.resRepo = resRepo;
        this.relsRepo = relsRepo;
    }


    @Override
    public Object visit(UpdateOperation op) {
        if (op.isOnResource()) {
            Resource value = op.getValue();
            return resRepo.save(value);
        }
        ResourceRelation relation = op.getRelation();
        return relsRepo.save(relation);
    }

    @Override
    public Object visit(DeleteOperation op) {
        if (op.isOnResource()) {
            resRepo.deleteById(op.getValue().getId());
        } else {
            relsRepo.deleteById(op.getRelation().getId());
        }
        return null;
    }

    public Object visit(InsertOperation op) {
        if (op.isOnResource()) {
            return resRepo.save(op.getValue());
        }
        return relsRepo.save(op.getRelation());
    }
}
