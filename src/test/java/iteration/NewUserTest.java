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

public class NewUserTest {
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
  public void adminCanCreateUserTest() {
    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .header("authorization", "Basic YWRtaW46YWRtaW4=")
        .body("""
            {
            "username": "Dima30",
            "password": "11Dickmm!",
            "role": "USER"
            }
            """)
        .post("http://localhost:4111/api/v1/admin/users")
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_CREATED)
        .body("username", Matchers.equalTo("Dima30"))
        .body("password", Matchers.not(Matchers.equalTo("11Dickmm!")))
        .body("role", Matchers.equalTo("USER"))
        .body("password", Matchers.startsWith("$2a$"));
  }
  @Test
  public void adminCantCreateUserTest() {
    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .header("authorization", "Basic YWRtaW46YWRtaW4=")
        .body("""
            {
            "username": "DIMA10%IQ!!",
            "password": "DIMALOVEDICKS10SMBLYADINKI%%**&&&$!@$!@$!@$",
            "role": "USER"
            }
            """)
        .post("http://localhost:4111/api/v1/admin/users")
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body("password", Matchers.equalTo("Password must contain at least one digit, one lower case, one upper case, one special character, no spaces, and be at least 8 characters long"))
        .body("username",Matchers.equalTo("Username must contain only letters, digits, dashes, underscores, and dots"));
  }
  @Test
  public void adminCanCheckList(){
    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .header("authorization", "Basic YWRtaW46YWRtaW4=")
        .body("""
            {
            "username": "TestUser",
            "password": "Test123!",
            "role": "USER"
            }
            """)
        .post("http://localhost:4111/api/v1/admin/users")
            .then()
                .assertThat().statusCode(HttpStatus.SC_CREATED);

    given()
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .header("authorization","Basic YWRtaW46YWRtaW4=")
        .get("http://localhost:4111/api/v1/admin/users")
        .then()
        .assertThat()
        .statusCode(HttpStatus.SC_OK)
        .body("username", Matchers.not(Matchers.equalTo("TestUser")));
  }
}
