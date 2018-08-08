create sequence hibernate_sequence start with 1 increment by 1
create table rel (rid bigint not null, vdec decimal(5,0), l varchar(5), vstr varchar(255), child_id bigint, parent_id bigint, primary key (rid))
create table res (id bigint not null, ref varchar(255) not null, type_id bigint not null, ver bigint, primary key (id))
alter table rel add constraint FKfgk74fj4f6ppvxt8rs2c9n20f foreign key (child_id) references res
alter table rel add constraint FKi1yh3qxpvqcy7f1vufda5qm0j foreign key (parent_id) references res
