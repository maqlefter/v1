package iteration;


import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import java.util.List;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserDepositTest {
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
    public void userCanShowHisDepositTest(){
       Integer userId = given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header("authorization", "Basic YWRtaW46YWRtaW4=")
            .body("""
            {
            "username": "TestUser115",
            "password": "Test123!",
            "role": "USER"
            }
            """)
            .post("http://localhost:4111/api/v1/admin/users")
            .then()
            .assertThat().statusCode(HttpStatus.SC_CREATED)
           .extract()
           .path("id");
       String token= given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("""
                {
                "username" : "TestUser115",
                "password" : "Test123!"
                }
                """)
            .post("http://localhost:4111/api/v1/auth/login")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
           .extract()
           .header("authorization");

        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header("authorization", token)
            .body("{\n" +
                "  \"id\": " + userId + ",\n" +
                "  \"balance\": 100\n" +
                "}")
            .post("http://localhost:4111/api/v1/accounts/deposit")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK);
    }
}
