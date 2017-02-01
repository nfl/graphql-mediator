package com.nfl.graphql.mediator;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test
public class GraphQLMediatorTest extends BaseBeanTest {

    private String schemaJson;

    public GraphQLMediatorTest() {
        schemaJson = loadFromFile("schema.json");
    }

    public void tryMe() {
        assertFalse(schemaJson.isEmpty());
        assertTrue(schemaJson.length() > 100);
    }

}
