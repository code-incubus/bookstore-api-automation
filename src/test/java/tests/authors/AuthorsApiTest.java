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
            Author author = authors.getById(1);
            assertThat(author.id()).isEqualTo(1);
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
            int authorId = 8888;
            int bookId = 1;
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
            int authorId = 8888;
            int bookId = 1;

            Author base = TestData.newAuthorPayload(authorId, bookId);
            Author update = new Author(base.id(), base.idBook(), base.firstName() + "_UPDATED", base.lastName() + "_UPDATED");
            Author updated = authors.update(authorId, update);
            assertThat(updated.id()).isEqualTo(authorId);
            assertThat(updated.firstName()).contains("UPDATED");
            assertThat(updated.lastName()).contains("FAILED"); // expected to fail since it's dummy source
        }

        @Test
        @Story("DELETE /Authors/{id}")
        void shouldDeleteAuthor() {
            authors.delete(8888);
        }
    }

    @Nested
    @Tag("negative")
    class NegativeAndEdge {

        @Test
        @Story("GET /Authors/{id} - non existing")
        void shouldHandleMissingAuthor() {
            Response res = authors.getByIdRaw(999999);
            assertThat(res.statusCode()).isIn(400, 404);
        }

        @Test
        @Story("POST /Authors - invalid payload")
        void shouldHandleInvalidCreatePayload() {
            Author invalid = new Author(0, 0, "", "");
            Response res = authors.createRaw(invalid);
            assertThat(res.statusCode()).isIn(400); // expected to fail since it's dummy source
        }
    }
}
