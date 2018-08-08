package com.ljcr.srdb;

import com.ljcr.api.*;


public class RdbAdapter {

    public static Repository createWs(final String connectionString) {
        return new Repository() {
            public String getName() {
                return connectionString;
            }

            public ImmutableNode getRootNode() {
                return null;
            }
        };
    }
}


