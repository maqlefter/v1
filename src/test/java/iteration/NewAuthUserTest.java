package iteration;


import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import java.util.List;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class NewAuthUserTest {
  @BeforeAll
  public static void setupRestAssured() {  // Добавлено static
    RestAssured.filters(
        List.of(
            new RequestLoggingFilter(),
            new ResponseLoggingFilter()
        )
    );
  }

  @Test
  public void adminCanGenerateAuthTokenTest() {
    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
            {
              "username" : "admin",
              "password" : "admin"
            }
            """)
        .post("http://localhost:4111/api/v1/auth/login")
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .header("authorization", "Basic YWRtaW46YWRtaW4=");
  }

  @Test
  public void userCanGenerateAuthTokenTest() {
    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .header("authorization", "Basic YWRtaW46YWRtaW4=")
        .body("""
            {
            "username": "Nikita20",
            "password": "Nikita20!",
            "role": "USER"
            }
            """)
        .post("http://localhost:4111/api/v1/admin/users")
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_CREATED);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .body("""
            {
              "username" : "Nikita20",
              "password" : "Nikita20!"
            }
            """)
        .post("http://localhost:4111/api/v1/auth/login")
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .header("authorization", Matchers.notNullValue());
  }
}

