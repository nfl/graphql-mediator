package com.nfl.graphql.mediator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import graphql.schema.*;

import java.io.IOException;

import java.util.*;
import java.util.function.Consumer;

import static com.nfl.graphql.mediator.glitr.type.GlitrScalars.GraphQLDate;
import static com.nfl.graphql.mediator.glitr.type.GlitrScalars.GraphQLDateTime;
import static graphql.Scalars.*;
import static graphql.schema.GraphQLEnumType.newEnum;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

@SuppressWarnings("WeakerAccess")
public class GraphQLMediator {

    private static final String NAME = "name";
    private static final String OBJECT_KIND = "OBJECT";
    private static final String ENUM_KIND = "ENUM";
    private final Map<String, JsonNode> jsonMap;

    private Map<String, GraphQLOutputType> builtObjects = new HashMap<>(89);

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
        return retrieveOutputDescription(typeName, emptySet());
    }

    /*
     * The purpose of undefinedTypes is to detect loops in object construction.  If two passes trying to resolve
     * an undefined type occur, then stop trying.
     */
    private GraphQLOutputType retrieveOutputDescription(String typeName, Set<String> undefinedTypes) {

        if (!builtObjects.containsKey(typeName) && !undefinedTypes.contains(typeName)) {
            Set<String> moreUndefined = new HashSet<>(undefinedTypes);
            moreUndefined.add(typeName);
            GraphQLOutputType newlyBuilt = build(typeName, moreUndefined);
            builtObjects.put(typeName, newlyBuilt);
        }

        return builtObjects.get(typeName);
    }

    private GraphQLOutputType build(String typeName, Set<String> undefinedTypes) {
        if (!jsonMap.containsKey(typeName)) {
            throw new UnsupportedOperationException("Asked to map an unknown type: " + typeName);
        }

        JsonNode typeNode = jsonMap.get(typeName);
        switch (typeNode.get("kind").asText()) {
            case OBJECT_KIND:
                List<GraphQLFieldDefinition> fields = buildFields((ArrayNode) typeNode.get("fields"), undefinedTypes);
                return new GraphQLObjectType(typeNode.get(NAME).asText(), typeNode.get("description").asText(), fields, emptyList());

            case ENUM_KIND:
                GraphQLEnumType.Builder builder = newEnum().name(NAME);
                deriveEnumValues((ArrayNode) typeNode.get("enumValues"), builder);
                return builder.build();

            default:
                throw new UnsupportedOperationException();


        }
    }

    private void deriveEnumValues(ArrayNode enumValues, GraphQLEnumType.Builder builder) {
        enumValues.spliterator().forEachRemaining(valueNode -> builder.value(valueNode.get(NAME).asText()));
    }

    private List<GraphQLFieldDefinition> buildFields(ArrayNode typeNode, Set<String> undefinedTypes) {

        List<GraphQLFieldDefinition> schemaDefFields = new LinkedList<>();
        typeNode.spliterator().forEachRemaining(buildFieldDefinition(undefinedTypes, schemaDefFields));

        return schemaDefFields;
    }

    private Consumer<JsonNode> buildFieldDefinition(Set<String> undefinedTypes, List<GraphQLFieldDefinition> schemaDefFields) {
        return jsonNode -> {
            String name = jsonNode.get(NAME).asText();
            String desc = jsonNode.get("description").asText();
            GraphQLOutputType type = deriveType(jsonNode.get("type"), undefinedTypes);
            if (type != null) {
                GraphQLFieldDefinition typeField = newFieldDefinition()
                        .type(type)
                        .name(name)
                        .description(desc)
                        .build();
                schemaDefFields.add(typeField);
            }
        };
    }

    private GraphQLOutputType deriveType(JsonNode type, Set<String> undefinedTypes) {

        String kind = type.get("kind").asText();
        String typeName = type.get(NAME).asText();

        switch (kind) {
            case "NON_NULL":
                GraphQLOutputType derived = deriveType(type.get("ofType"), undefinedTypes);
                if (derived == null) {
                    return null;
                }
                return new GraphQLNonNull(derived);

            case ENUM_KIND:
            case OBJECT_KIND:
                return retrieveOutputDescription(typeName, undefinedTypes);

            case "SCALAR":
                return scalarType(typeName);

            case "LIST":
                GraphQLOutputType derivedForList = deriveType(type.get("ofType"), undefinedTypes);
                if (derivedForList == null) {
                    return null;
                }
                return new GraphQLList(derivedForList);

            case "INTERFACE":
                return null;        // TODO - figure out if anything is really needed here.

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

            case "Date":
                return GraphQLDate;

            case "Int":
                return GraphQLInt;

            case "Long":
                return GraphQLLong;

            case "Boolean":
                return GraphQLBoolean;

            case "Float":
                return GraphQLFloat;

            case "BigInteger":
                return GraphQLBigInteger;

            default:
                throw new UnsupportedOperationException("Unknown type: " + typeName);
        }
    }
}
