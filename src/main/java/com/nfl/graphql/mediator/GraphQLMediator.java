package com.nfl.graphql.mediator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import graphql.schema.*;

import java.io.IOException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.nfl.graphql.mediator.glitr.type.GlitrScalars.GraphQLDateTime;
import static graphql.Scalars.GraphQLID;
import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;

@SuppressWarnings("WeakerAccess")
public class GraphQLMediator {

    private static final String NAME = "name";
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
        jsonMap = new HashMap<>(typesMap.size() * 7);
        typesMap.spliterator()
                .forEachRemaining(jsonNode ->
                        jsonMap.put(jsonNode.get(NAME).textValue(), jsonNode));
    }

    public GraphQLOutputType retrieveOutputDescription(String typeName) {
        if (!builtObjects.containsKey(typeName)) {
            build(typeName);
        }

        return builtObjects.get(typeName);
    }

    private void build(String typeName) {
        if (!jsonMap.containsKey(typeName)) {
            throw new UnsupportedOperationException("Asked to map an unknown type: " + typeName);
        }

        JsonNode typeNode = jsonMap.get(typeName);
        List<GraphQLFieldDefinition> fields = buildFields((ArrayNode) typeNode.get("fields"));

//        GraphQLObjectType
        int idx = 57;
        idx++;
    }

    private List<GraphQLFieldDefinition> buildFields(ArrayNode typeNode) {

        List<GraphQLFieldDefinition> schemaDefFields = new LinkedList<>();

        typeNode.spliterator().forEachRemaining(
                jsonNode -> {
                    String name = jsonNode.get(NAME).asText();
                    String desc = jsonNode.get("description").asText();
                    GraphQLOutputType type = deriveType(jsonNode.get("type"));
                    GraphQLFieldDefinition typeField = newFieldDefinition()
                            .type(type)
                            .name(name)
                            .description(desc)
                            .build();
                    schemaDefFields.add(typeField);
                }
        );

        return schemaDefFields;
    }

    private GraphQLOutputType deriveType(JsonNode type) {

        String kind = type.get("kind").asText();
        String typeName = type.get(NAME).asText();

        switch (kind) {
            case "NON_NULL":
                return new GraphQLNonNull(deriveType(type.get("ofType")));

            case "OBJECT":
                return retrieveOutputDescription(typeName);

            case "SCALAR":
                return scalarType(typeName);

            case "LIST":
                return new GraphQLList(deriveType(type.get("ofType")));

            default:
                throw new UnsupportedOperationException("Unknown kind:" + kind);
        }
    }

    private GraphQLOutputType scalarType(String typeName) {

        switch (typeName) {
            case "ID":
                return GraphQLID;

            case "String":
                return GraphQLString;

            case "DateTime":
                return GraphQLDateTime;

            case "Int":
                return GraphQLInt;

            default:
                throw new UnsupportedOperationException("Unknown type: " + typeName);
        }
    }
}
