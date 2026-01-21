package tests.contract;

import client.authors.AuthorsClient;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Tag("contract")
@Tag("api")
class AuthorsContractTest {

    private final AuthorsClient authors = new AuthorsClient();

    @Test
    void getAllAuthors_shouldMatchSchema() {
        authors.getAllRaw()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body(matchesJsonSchemaInClasspath("schemas/authors/authors-list.schema.json"));
    }

    @Test
    void getAuthorById_shouldMatchSchema() {
        authors.getByIdRaw(1)
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body(matchesJsonSchemaInClasspath("schemas/authors/author.schema.json"));
    }
}
