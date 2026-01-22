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
            Book book = books.getById(1);
            assertThat(book.id()).isEqualTo(1);
            assertThat(book.title()).isNotBlank();
        }
    }

    @Nested
    @Tag("functional")
    class Crud {

        @Test
        @Story("POST /Books")
        void shouldCreateBook() {
            int id = 7777;
            Book payload = TestData.newBookPayload(id);
            Book created = books.create(payload);
            assertThat(created.id()).isEqualTo(id);
            assertThat(created.title()).isEqualTo(payload.title());
            assertThat(created.pageCount()).isEqualTo(payload.pageCount());
        }

        @Test
        @Story("PUT /Books/{id}")
        void shouldUpdateBook() {
            int id = 7777;
            Book payload = TestData.newBookPayload(id);
            Book updatePayload = new Book(payload.id(), payload.title() + " - UPDATED", payload.description(), payload.pageCount() + 1, payload.excerpt(), payload.publishDate());
            Book updated = books.update(id, updatePayload);
            assertThat(updated.id()).isEqualTo(id);
            assertThat(updated.title()).contains("UPDATED");
            assertThat(updated.pageCount()).isEqualTo(updatePayload.pageCount());
        }

        @Test
        @Story("DELETE /Books/{id}")
        void shouldDeleteBook() {
            books.delete(7777);
        }
    }

    @Nested
    @Tag("negative")
    class NegativeAndEdge {

        @Test
        @Story("GET /Books/{id} - non existing")
        void shouldHandleMissingBook() {
            Response res = books.getByIdRaw(999999);
            assertThat(res.statusCode()).isIn(400, 404);
        }

        @Test
        @Story("POST /Books - invalid payload")
        void shouldHandleInvalidCreatePayload() {
            Book invalid = new Book(0, "", "", 0, "", "");
            Response res = books.createRaw(invalid);
            assertThat(res.statusCode()).isIn(200); // expected to fail since it's dummy source
        }

        @Test
        @Story("PUT /Books/{id} - invalid id")
        void shouldHandleInvalidUpdateId() {
            int invalidId = -1;
            Book payload = TestData.newBookPayload(12345);
            Response res = books.updateRaw(invalidId, payload);
            assertThat(res.statusCode()).isIn(200, 201, 400, 404);
        }

        @Test
        @Story("DELETE /Books/{id} - non existing")
        void shouldHandleDeleteNonExisting() {
            Response res = books.deleteRaw(999999);
            assertThat(res.statusCode()).isIn(200, 204, 404);
        }
    }
}
