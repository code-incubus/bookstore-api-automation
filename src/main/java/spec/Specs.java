package spec;

import config.AppConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public final class Specs {
    private Specs() {
    }

    public static RequestSpecification requestSpec() {
        RequestSpecBuilder b = new RequestSpecBuilder()
                .setBaseUri(AppConfig.baseUrl())
                .setContentType(JSON)
                .setAccept(JSON)
                .addFilter(new AllureRestAssured());

        if (AppConfig.httpLoggingEnabled()) {
            b.log(LogDetail.ALL);
        }
        return b.build();
    }
}
