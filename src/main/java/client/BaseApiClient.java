package client;

import io.restassured.response.Response;

import java.util.Arrays;

public abstract class BaseApiClient {

    protected void assertStatus(Response response, int... expectedStatuses) {
        int actual = response.statusCode();

        for (int expected : expectedStatuses) {
            if (actual == expected) {
                return;
            }
        }

        throw new AssertionError(
                "Unexpected HTTP status code: " + actual +
                        ", expected one of: " + Arrays.toString(expectedStatuses)
        );
    }
}
