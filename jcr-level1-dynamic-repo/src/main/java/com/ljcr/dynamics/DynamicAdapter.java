package com.ljcr.dynamics;

import com.ljcr.api.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class DynamicAdapter {
    private static final Logger logger = LoggerFactory.getLogger(DynamicAdapter.class);

    public static Repository createWs(Repository staticRepo) throws IOException {
        WrappingVisitor visitor = new WrappingVisitor(staticRepo);
        return new DynamicRepositoryWrapper(visitor);
    }
}


