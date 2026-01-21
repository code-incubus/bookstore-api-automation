package client.books;

import client.BaseApiClient;
import io.restassured.response.Response;
import model.Book;
import spec.Specs;

import java.util.List;

import static io.restassured.RestAssured.given;

public class BooksClient extends BaseApiClient {

    private static final String BOOKS = "/Books";

    public Response getAllRaw() {
        return given()
                .spec(Specs.requestSpec())
                .when()
                .get(BOOKS)
                .then()
                .extract()
                .response();
    }

    public Response getByIdRaw(int id) {
        return given()
                .spec(Specs.requestSpec())
                .when()
                .get(BOOKS + "/{id}", id)
                .then()
                .extract()
                .response();
    }

    public Response createRaw(Book book) {
        return given()
                .spec(Specs.requestSpec())
                .body(book) // SERIALIZATION: Book -> JSON
                .when()
                .post(BOOKS)
                .then()
                .extract()
                .response();
    }

    public Response updateRaw(int id, Book book) {
        return given()
                .spec(Specs.requestSpec())
                .body(book) // SERIALIZATION
                .when()
                .put(BOOKS + "/{id}", id)
                .then()
                .extract()
                .response();
    }

    public Response deleteRaw(int id) {
        return given()
                .spec(Specs.requestSpec())
                .when()
                .delete(BOOKS + "/{id}", id)
                .then()
                .extract()
                .response();
    }

    public List<Book> getAll() {
        Response response = getAllRaw();
        assertStatus(response, 200);
        return response.jsonPath().getList("", Book.class);
    }

    public Book getById(int id) {
        Response response = getByIdRaw(id);
        assertStatus(response, 200);
        return response.as(Book.class);
    }

    public Book create(Book book) {
        Response response = createRaw(book);
        assertStatus(response, 200, 201);
        return response.as(Book.class);
    }
    public Book update(int id, Book book) {
        Response response = updateRaw(id, book);
        assertStatus(response, 200, 201);

        return response.as(Book.class);
    }
    public void delete(int id) {
        Response response = deleteRaw(id);
        assertStatus(response, 200, 204);
    }
}
