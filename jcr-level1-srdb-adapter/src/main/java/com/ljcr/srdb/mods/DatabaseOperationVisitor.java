package com.ljcr.srdb.mods;

public interface DatabaseOperationVisitor {
    Object visit(UpdateOperation op);

    Object visit(DeleteOperation op);

    Object visit(InsertOperation op);
}
