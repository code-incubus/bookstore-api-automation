package tests.contract;

import client.books.BooksClient;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Tag("contract")
@Tag("api")
class BooksContractTest {

    private final BooksClient books = new BooksClient();

    @Test
    void getAllBooks_shouldMatchSchema() {
        books.getAllRaw()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body(matchesJsonSchemaInClasspath("schemas/books/books-list.schema.json"));
    }

    @Test
    void getBookById_shouldMatchSchema() {
        books.getByIdRaw(1)
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body(matchesJsonSchemaInClasspath("schemas/books/book.schema.json"));
    }
}