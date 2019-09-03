package com.bill.demo.j8;

import com.google.common.collect.ImmutableMap;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.*;
import graphql.schema.idl.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bill.rules.odata.customization.util.JsonUtil.getJsonByMap;
import static com.jayway.jsonpath.internal.JsonFormatter.prettyPrint;
import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

public class GraphQLTest {

    @Test
    public void testSDLStyleSimpleTypeDefinition() {
        String schema = "type Query{hello: String}";
        String query = "{hello}";
        String result = getResult(
                schema,
                newRuntimeWiring()
                        .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
                        .build(),
                query, null
        );
        System.out.println(result);
        Assert.assertTrue(result.contains("\"hello\" : \"world\""));
    }

    static class UppercaseDirectiveOnField implements SchemaDirectiveWiring {
        @Override
        public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
            GraphQLFieldDefinition field = env.getElement();
            DataFetcher dataFetcher = DataFetcherFactories.wrapDataFetcher(field.getDataFetcher(),
                    (dataFetchingEnvironment, value) -> {
                        if (value == null) {
                            return null;
                        }
                        return value.toString().toUpperCase();
                    }
            );
            return field.transform(builder -> builder.dataFetcher(dataFetcher));
        }
    }

    @Test
    public void testDirectiveOnField() {
        String schema = "directive @uppercase on FIELD_DEFINITION " +
                "type Query{hello: String @uppercase } ";
        String query = "{hello}";
        String result = getResult(
                schema,
                newRuntimeWiring()
                        .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
                        .directive("uppercase", new UppercaseDirectiveOnField())
                        .build(),

                query, null
        );
        System.out.println(result);
        Assert.assertTrue(result.contains("\"hello\" : \"WORLD\""));
    }

    static class UppercaseDirectiveOnObject implements SchemaDirectiveWiring {
        @Override
        public GraphQLObjectType onObject(SchemaDirectiveWiringEnvironment<GraphQLObjectType> env) {
            GraphQLObjectType object = env.getElement();
            List<GraphQLFieldDefinition> graphQLFieldDefinitions = object.getFieldDefinitions();
            for(GraphQLFieldDefinition field: graphQLFieldDefinitions){
                DataFetcher dataFetcher = DataFetcherFactories.wrapDataFetcher(
                        field.getDataFetcher(),
                        (dataFetchingEnvironment, value) -> {
                            if (value == null) {
                                return null;
                            }
                            return value.toString().toUpperCase();
                        }
                );
                object = object.transform(
                        builder -> builder.field(
                                field.transform(
                                        fieldBuilder -> fieldBuilder.dataFetcher(dataFetcher)
                                )
                        )
                );
            }
            return object;
        }

    }

    @Test
    public void testDirectiveOnObject() {
        String schema = "directive @uppercase on OBJECT " +
                "type Query @uppercase {hello: String  anotherHello: String} ";
        String query = "{hello, anotherHello}";
        String result = getResult(
                schema,
                newRuntimeWiring()
                        .directive("uppercase", new UppercaseDirectiveOnObject())
                        .type("Query", builder -> builder
                                .dataFetcher("hello", new StaticDataFetcher("world"))
                                .dataFetcher("anotherHello", new StaticDataFetcher("world")))
                        .build(),

                query, null
        );
        System.out.println(result);
        Assert.assertTrue(result.contains("\"hello\" : \"WORLD\""));
        Assert.assertTrue(result.contains("\"anotherHello\" : \"WORLD\""));
    }

    static class GraphQLDataFetchers {

        private static List<Map<String, Object>> books = Arrays.asList(
                ImmutableMap.of("id", "book-1",
                        "name", "Harry Potter and the Philosopher's Stone",
                        "pageCount", "223",
                        "authorId", "author-1"),
                ImmutableMap.of("id", "book-2",
                        "name", "Moby Dick",
                        "pageCount", "635",
                        "authorId", "author-2"),
                ImmutableMap.of("id", "book-3",
                        "name", "Interview with the vampire",
                        "pageCount", "371",
                        "authorId", "author-3")
        );

        private static List<Map<String, Object>> authors = Arrays.asList(
                ImmutableMap.of("id", "author-1",
                        "firstName", "Joanne",
                        "lastName", "Rowling"),
                ImmutableMap.of("id", "author-2",
                        "firstName", "Herman",
                        "lastName", "Melville"),
                ImmutableMap.of("id", "author-3",
                        "firstName", "Anne",
                        "lastName", "Rice")
        );

        public DataFetcher getBookByIdDataFetcher() {
            return dataFetchingEnvironment -> {
                String bookId = dataFetchingEnvironment.getArgument("id");
                return books
                        .stream()
                        .filter(book -> book.get("id").equals(bookId))
                        .findFirst()
                        .orElse(null);
            };
        }

        public DataFetcher getAuthorDataFetcher() {
            return dataFetchingEnvironment -> {
                Map<String, Object> book = dataFetchingEnvironment.getSource();
                Object authorId = book.get("authorId");
                return authors
                        .stream()
                        .filter(author -> author.get("id").equals(authorId))
                        .findFirst()
                        .orElse(null);
            };
        }

        public DataFetcher getAllBooksDataFetcher() {
            return dataFetchingEnvironment -> books;
        }

    }



    @Test
    public void testFieldsArgumentAndAlias() {
        String schema =
                "type Query {\n" +
                        "    bookById(id: ID): Book \n" +
                        "    allBooks: [Book] \n" +
                        "}\n" +
                        "type Book {\n" +
                        "    id: ID\n" +
                        "    name: String\n" +
                        "    pageCount: Int\n" +
                        "    author: Author\n" +
                        "}\n" +
                        "type Author {\n" +
                        "    id: ID\n" +
                        "    firstName: String\n" +
                        "    lastName: String\n" +
                        "}";
        String query =
                "{" +
                        "   book: bookById(id: \"book-1\"){" + //field & argument & alias
                        "       id " +
                        "       name " +
                        "       pageCount " +
                        "       author{" +
                        "           id" +
                        "           firstName" +
                        "           lastName" +
                        "       }" +
                        "   }" +
                        "   allBooks{" + //array
                        "       id" +
                        "       name" +
                        "       author{" +
                        "           id" +
                        "       }" +
                        "   }" +
                        "}";
        String result = applyAssert(schema, query, null);
        Assert.assertTrue(result.contains("\"firstName\" : \"Joanne\","));
        Assert.assertTrue(result.contains("\"id\" : \"author-1\""));
        Assert.assertTrue(result.contains("\"id\" : \"author-2\""));
        Assert.assertTrue(result.contains("\"id\" : \"author-3\""));
    }


    @Test
    public void testComparison() {
        String schema =
                "type Query {\n" +
                        "    bookById(id: ID): Book \n" +
                        "    allBooks: [Book] \n" +
                        "}\n" +
                        "type Book {\n" +
                        "    id: ID\n" +
                        "    name: String\n" +
                        "    pageCount: Int\n" +
                        "    author: Author\n" +
                        "}\n" +
                        "type Author {\n" +
                        "    id: ID\n" +
                        "    firstName: String\n" +
                        "    lastName: String\n" +
                        "}";
        String query =
                "query booksWithComparisonFields{" +
                        "    allBooks{" +
                        "        ... on Book { XXXName: name }" + //inline fragment and alias
                        "        ... comparisonFields" +   //predefined fragment
                        "    }" +
                        "}" +
                        "fragment comparisonFields on Book {\n" +
                        "    name\n" +
                        "    id\n" +
                        "    pageCount\n" +
                        "    author {\n" +
                        "      id\n" +
                        "      firstName\n" +

                        "    }\n" +
                        "}";

        ;
        String result = applyAssert(schema, query, null);
        Assert.assertTrue(result.contains("\"firstName\" : \"Joanne\","));
        Assert.assertTrue(result.contains("\"XXXName\" : \"Harry Potter and the Philosopher's Stone\","));
        Assert.assertTrue(result.contains("\"XXXName\" : \"Moby Dick\","));
        Assert.assertTrue(result.contains("\"XXXName\" : \"Interview with the vampire\","));
    }

    @Test
    public void testVariable() {
        String schema =
                "type Query {\n" +
                        "    bookById(id: ID): Book \n" +
                        "    allBooks: [Book] \n" +
                        "}\n" +
                        "type Book {\n" +
                        "    id: ID\n" +
                        "    name: String\n" +
                        "    pageCount: Int\n" +
                        "    author: Author\n" +
                        "}\n" +
                        "type Author {\n" +
                        "    id: ID\n" +
                        "    firstName: String\n" +
                        "    lastName: String\n" +
                        "}";
        String query =
                "query booksWithVariabl($bookId: ID!, $withAuthor: Boolean!){" +
                        "    bookById(id: $bookId){" +
                        "       name " +
                        "       author  @include(if: $withAuthor){\n" +
                        "         id\n" +
                        "         firstName\n" +
                        "       }\n" +
                        "    } " +
                        "}";

        Map variables = new HashMap();
        variables.put("bookId", "book-1");//variable
        variables.put("withAuthor", true);//variable
        String result = applyAssert(schema, query, variables);
        Assert.assertTrue(result.contains("\"firstName\" : \"Joanne\","));
        Assert.assertTrue(result.contains("\"id\" : \"author-1\""));
        Assert.assertTrue(result.contains("\"name\" : \"Harry Potter and the Philosopher's Stone\""));
    }

    private String applyAssert(String schema, String query, Map vaiables) {
        GraphQLDataFetchers graphQLDataFetchers = new GraphQLDataFetchers();
        String result = getResult(
                schema,
                newRuntimeWiring()
                        .type(newTypeWiring("Query")
                                .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher())
                                .dataFetcher("allBooks", graphQLDataFetchers.getAllBooksDataFetcher()))

                        .type(newTypeWiring("Book")
                                .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                        .build(),
                query,
                vaiables
        );
        System.out.println(result);
        return result;
    }

    private String getResult(String schema, RuntimeWiring runtimeWiring, String query, Map vaiables) {
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema).build();
        ExecutionResult executionResult = graphQL.execute(
                graphql.ExecutionInput.newExecutionInput().query(query).variables(vaiables).context(new HashMap())
        );
        return prettyPrint(getJsonByMap(executionResult.getData()));
    }


}
