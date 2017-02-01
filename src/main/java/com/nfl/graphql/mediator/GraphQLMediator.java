package com.nfl.graphql.mediator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import graphql.schema.GraphQLObjectType;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class GraphQLMediator {

    private final Map<String, JsonNode> jsonMap;

    private Map<String, GraphQLObjectType> builtObjects = new HashMap<>(89);

    public GraphQLMediator(String rawJson) {
        JsonNode root;
        ObjectMapper mapper = new ObjectMapper();
        try {
            root = mapper.readTree(rawJson);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        ArrayNode typesMap = (ArrayNode) root.get("data").get("__schema").get("types");
        jsonMap = new HashMap<>(typesMap.size()*7);
        typesMap.spliterator()
                .forEachRemaining(jsonNode ->
                        jsonMap.put(jsonNode.get("name").textValue(), jsonNode));
    }

    public GraphQLObjectType retrieveOutputDescription(String typeName) {
        if (!builtObjects.containsKey(typeName)) {
            build(typeName);
        }

        return builtObjects.get(typeName);
    }

    private void build(String typeName) {
        int idx = 57;
        idx++;
    }
}
