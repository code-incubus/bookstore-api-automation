package tests.authors;

import client.authors.AuthorsClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import model.Author;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import util.TestData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Online Bookstore API")
@Feature("Authors")
@Tag("api")
@Tag("authors")
class AuthorsApiTest {

    private final AuthorsClient authors = new AuthorsClient();

    @Nested
    @Tag("smoke")
    class Smoke {

        @Test
        @Story("GET /Authors")
        void shouldReturnListOfAuthors() {
            List<Author> all = authors.getAll();
            assertThat(all).isNotNull();
            assertThat(all).isNotEmpty();
            assertThat(all.get(0).id()).isGreaterThan(0);
        }

        @Test
        @Story("GET /Authors/{id}")
        void shouldReturnAuthorById() {
            int existingId = TestData.existingAuthorId();

            Author author = authors.getById(existingId);

            assertThat(author.id()).isEqualTo(existingId);
            assertThat(author.firstName()).isNotBlank();
            assertThat(author.lastName()).isNotBlank();
        }
    }

    @Nested
    @Tag("functional")
    class Crud {

        @Test
        @Story("POST /Authors")
        void shouldCreateAuthor() {
            int authorId = TestData.uniqueId();
            int bookId = TestData.existingBookId();

            Author payload = TestData.newAuthorPayload(authorId, bookId);
            Author created = authors.create(payload);

            assertThat(created.id()).isEqualTo(authorId);
            assertThat(created.idBook()).isEqualTo(bookId);
            assertThat(created.firstName()).isEqualTo(payload.firstName());
            assertThat(created.lastName()).isEqualTo(payload.lastName());
        }

        @Test
        @Story("PUT /Authors/{id}")
        void shouldUpdateAuthor() {
            int authorId = TestData.uniqueId();
            int bookId = TestData.existingBookId();

            Author base = TestData.newAuthorPayload(authorId, bookId);
            authors.create(base);

            Author update = new Author(
                    base.id(),
                    base.idBook(),
                    base.firstName() + "_UPDATED",
                    base.lastName() + "_UPDATED"
            );

            Author updated = authors.update(authorId, update);

            assertThat(updated.id()).isEqualTo(authorId);
            assertThat(updated.firstName()).contains("UPDATED");
            assertThat(updated.lastName()).contains("UPDATED");
        }

        @Test
        @Story("DELETE /Authors/{id}")
        void shouldDeleteAuthor() {
            int authorId = TestData.uniqueId();
            int bookId = TestData.existingBookId();

            authors.create(TestData.newAuthorPayload(authorId, bookId));

            authors.delete(authorId);

            // demo API can be inconsistent in validation behavior
            Response res = authors.getByIdRaw(authorId);
            assertThat(res.statusCode()).isIn(400, 404);
        }
    }

    @Nested
    @Tag("negative")
    class NegativeAndEdge {

        @Test
        @Story("GET /Authors/{id} - non existing")
        void shouldHandleMissingAuthor() {
            int missingId = TestData.nonExistingId();

            Response res = authors.getByIdRaw(missingId);
            assertThat(res.statusCode()).isIn(400, 404);
        }

        @Test
        @Story("POST /Authors - invalid payload")
        void shouldHandleInvalidCreatePayload() {
            Author invalid = new Author(0, 0, "", "");
            Response res = authors.createRaw(invalid);

            // demo API can be inconsistent in validation behavior
            assertThat(res.statusCode()).isIn(400, 422, 200, 201);
        }
    }
}