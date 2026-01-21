package client.authors;

import client.BaseApiClient;
import io.restassured.response.Response;
import model.Author;
import spec.Specs;

import java.util.List;

import static io.restassured.RestAssured.given;

public class AuthorsClient extends BaseApiClient {

    private static final String AUTHORS = "/Authors";

    public Response getAllRaw() {
        return given()
                .spec(Specs.requestSpec())
                .when()
                .get(AUTHORS)
                .then()
                .extract()
                .response();
    }

    public Response getByIdRaw(int id) {
        return given()
                .spec(Specs.requestSpec())
                .when()
                .get(AUTHORS + "/{id}", id)
                .then()
                .extract()
                .response();
    }

    public Response createRaw(Author author) {
        return given()
                .spec(Specs.requestSpec())
                .body(author) // SERIALIZATION: Author -> JSON
                .when()
                .post(AUTHORS)
                .then()
                .extract()
                .response();
    }

    public Response updateRaw(int id, Author author) {
        return given()
                .spec(Specs.requestSpec())
                .body(author) // SERIALIZATION
                .when()
                .put(AUTHORS + "/{id}", id)
                .then()
                .extract()
                .response();
    }

    public Response deleteRaw(int id) {
        return given()
                .spec(Specs.requestSpec())
                .when()
                .delete(AUTHORS + "/{id}", id)
                .then()
                .extract()
                .response();
    }

    public List<Author> getAll() {
        Response response = getAllRaw();
        assertStatus(response, 200);
        return response.jsonPath().getList("", Author.class);
    }

    public Author getById(int id) {
        Response response = getByIdRaw(id);
        assertStatus(response, 200);
        return response.as(Author.class);
    }

    public Author create(Author author) {
        Response response = createRaw(author);
        assertStatus(response, 200, 201);
        return response.as(Author.class);
    }

    public Author update(int id, Author author) {
        Response response = updateRaw(id, author);
        assertStatus(response, 200, 201);
        return response.as(Author.class);
    }

    public void delete(int id) {
        Response response = deleteRaw(id);
        assertStatus(response, 200, 204);
    }
}
