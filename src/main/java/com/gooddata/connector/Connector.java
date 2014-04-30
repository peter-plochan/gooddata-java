/*
 * Copyright (C) 2007-2014, GoodData(R) Corporation. All rights reserved.
 */
package com.gooddata.connector;

/**
 */
public enum Connector {
    ZENDESK4;

    public String getName() {
        return name().toLowerCase();
    }
}
