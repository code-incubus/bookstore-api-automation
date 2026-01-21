package util;

import model.Author;
import model.Book;

import java.time.OffsetDateTime;

public final class TestData {
    public static Book newBookPayload(int id) {
        return new Book(id, "Na Drini Cuprija " + id, "Opis za " + id, 100 + (id % 50), "podatak " + id, OffsetDateTime.now().toString());
    }

    public static Author newAuthorPayload(int id, int bookId) {
        return new Author(id, bookId, "Mitar" + id, "Miric" + id);
    }
}
