create sequence sq_res_id start with 101 increment by 1;
create sequence sq_rel_id start with 1100 increment by 1;

create table res (
    id bigint default sq_res_id.nextval not null,
    type_id bigint not null,
    ref varchar(255) not null,
    ver bigint,
    primary key (id));

create table rel (
    rid bigint default sq_rel_id.nextval not null,
    child_id bigint,
    parent_id bigint,
    l varchar(5), vstr varchar(255), vdec decimal(15,5),
    primary key (rid));

-- it is not possible to fill this table w/o this record
insert into res (id, type_id, ref, ver) values(16, 16, 'TypeDef', 0);
-- it is base scalar types they should have predefined IDs, so it is advisable to insert them here
insert into res (id, type_id, ref, ver) values(0, 16, 'AnyType', 0);
insert into res (id, type_id, ref, ver) values(1, 16, 'String', 0);
insert into res (id, type_id, ref, ver) values(3, 16, 'Long', 0);
insert into res (id, type_id, ref, ver) values(7, 16, 'Name', 0);
insert into res (id, type_id, ref, ver) values(9, 16, 'Reference', 0);
insert into res (id, type_id, ref, ver) values(12, 16, 'Decimal', 0);
insert into res (id, type_id, ref, ver) values(18, 16, 'Array', 0);
insert into res (id, type_id, ref, ver) values(19, 16, 'Map', 0);
insert into res (id, type_id, ref, ver) values(65, 16, 'RepositoryRoot', 0);

alter table rel add constraint res_id_child_id foreign key (child_id) references res;
alter table rel add constraint res_id_parent_id foreign key (parent_id) references res;
alter table res add constraint res_type_id_parent_id foreign key (type_id) references res;