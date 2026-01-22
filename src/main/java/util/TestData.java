package util;

import model.Author;
import model.Book;

import java.time.OffsetDateTime;
import java.util.concurrent.ThreadLocalRandom;

public final class TestData {

    private TestData() {
    }

    /**
     * Generates a unique positive ID.
     */
    public static int uniqueId() {
        return (int) (System.currentTimeMillis() % 1_000_000)
                + ThreadLocalRandom.current().nextInt(1000);
    }

    /**
     * IDs that are known to exist on the demo API (assumption for DEMO api).
     */
    public static int existingBookId() {
        return 1;
    }

    public static int existingAuthorId() {
        return 1;
    }

    /**
     * Generates an ID that is very unlikely to exist.
     */
    public static int nonExistingId() {
        return ThreadLocalRandom.current().nextInt(900_000, 999_999);
    }

    public static Book newBookPayload(int id) {
        return new Book(
                id,
                "Na Drini Cuprija " + id,
                "Opis za " + id,
                100 + (id % 50),
                "podatak " + id,
                OffsetDateTime.now().toString()
        );
    }

    public static Author newAuthorPayload(int id, int bookId) {
        return new Author(
                id,
                bookId,
                "Ivo " + id,
                "Andric " + id
        );
    }
}