package specifications;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

public class ResponseSpec {
  private ResponseSpec() {
  }

  private static ResponseSpecBuilder defaultResponseBuilder() {
    return new ResponseSpecBuilder();
  }

  public static ResponseSpecification entityWasCreated() {
    return defaultResponseBuilder()
        .expectStatusCode(HttpStatus.SC_CREATED)
        .build();
  }

  public static ResponseSpecification requestWasOk() {
    return defaultResponseBuilder()
        .expectStatusCode(HttpStatus.SC_OK)
        .build();
  }

  public static ResponseSpecification entityWasBadRequest() {
    return defaultResponseBuilder()
        .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
        .build();
  }
}
