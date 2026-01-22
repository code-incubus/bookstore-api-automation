package tests.books;

import client.books.BooksClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import model.Book;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import util.TestData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Online Bookstore API")
@Feature("Books")
@Tag("api")
@Tag("books")
class BooksApiTest {

    private final BooksClient books = new BooksClient();

    @Nested
    @Tag("smoke")
    class Smoke {

        @Test
        @Story("GET /Books")
        void shouldReturnListOfBooks() {
            List<Book> all = books.getAll();
            assertThat(all).isNotNull();
            assertThat(all).isNotEmpty();
            assertThat(all.get(0).id()).isGreaterThan(0);
        }

        @Test
        @Story("GET /Books/{id}")
        void shouldReturnBookById() {
            int existingId = TestData.existingBookId();

            Book book = books.getById(existingId);

            assertThat(book.id()).isEqualTo(existingId);
            assertThat(book.title()).isNotBlank();
        }
    }

    @Nested
    @Tag("functional")
    class Crud {

        @Test
        @Story("POST /Books")
        void shouldCreateBook() {
            int id = TestData.uniqueId();

            Book payload = TestData.newBookPayload(id);
            Book created = books.create(payload);

            assertThat(created.id()).isEqualTo(id);
            assertThat(created.title()).isEqualTo(payload.title());
            assertThat(created.pageCount()).isEqualTo(payload.pageCount());
        }

        @Test
        @Story("PUT /Books/{id}")
        void shouldUpdateBook() {
            int id = TestData.uniqueId();

            Book payload = TestData.newBookPayload(id);
            books.create(payload);

            Book updatePayload = new Book(
                    payload.id(),
                    payload.title() + " - UPDATED",
                    payload.description(),
                    payload.pageCount() + 1,
                    payload.excerpt(),
                    payload.publishDate()
            );

            Book updated = books.update(id, updatePayload);

            assertThat(updated.id()).isEqualTo(id);
            assertThat(updated.title()).contains("UPDATED");
            assertThat(updated.pageCount()).isEqualTo(updatePayload.pageCount());
        }

        @Test
        @Story("DELETE /Books/{id}")
        void shouldDeleteBook() {
            int id = TestData.uniqueId();

            // ensure book exists
            books.create(TestData.newBookPayload(id));

            books.delete(id);

            // optional validation (demo API often returns 400/404 after delete)
            Response res = books.getByIdRaw(id);
            assertThat(res.statusCode()).isIn(400, 404);
        }
    }

    @Nested
    @Tag("negative")
    class NegativeAndEdge {

        @Test
        @Story("GET /Books/{id} - non existing")
        void shouldHandleMissingBook() {
            int missingId = TestData.nonExistingId();

            Response res = books.getByIdRaw(missingId);
            assertThat(res.statusCode()).isIn(400, 404);
        }

        @Test
        @Story("POST /Books - invalid payload")
        void shouldHandleInvalidCreatePayload() {
            Book invalid = new Book(0, "", "", 0, "", "");
            Response res = books.createRaw(invalid);

            // demo API can be inconsistent in validation behavior
            assertThat(res.statusCode()).isIn(400, 422, 200, 201);
        }

        @Test
        @Story("PUT /Books/{id} - invalid id")
        void shouldHandleInvalidUpdateId() {
            int invalidId = -1;

            Book payload = TestData.newBookPayload(TestData.uniqueId());
            Response res = books.updateRaw(invalidId, payload);

            assertThat(res.statusCode()).isIn(200, 201, 400, 404);
        }

        @Test
        @Story("DELETE /Books/{id} - non existing")
        void shouldHandleDeleteNonExisting() {
            int missingId = TestData.nonExistingId();

            Response res = books.deleteRaw(missingId);
            assertThat(res.statusCode()).isIn(200, 204, 404);
        }
    }
}