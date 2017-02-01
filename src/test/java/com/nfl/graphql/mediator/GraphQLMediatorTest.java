package com.nfl.graphql.mediator;

import graphql.schema.GraphQLOutputType;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test
public class GraphQLMediatorTest extends BaseBeanTest {

    private String schemaJson;

    public GraphQLMediatorTest() {
        schemaJson = loadFromFile("schema.json");
    }

    public void imageConfirm() {
        assertFalse(schemaJson.isEmpty());
        GraphQLMediator graphQLMediator = new GraphQLMediator(schemaJson);
        assertNotNull(graphQLMediator);

        GraphQLOutputType imageObject = graphQLMediator.retrieveOutputDescription("Image");
        assertNotNull(imageObject);
    }

}
