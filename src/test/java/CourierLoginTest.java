import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.lewickiy.entity.Courier;
import org.lewickiy.entity.Credentials;
import org.lewickiy.service.CourierAssertions;
import org.lewickiy.service.CourierGenerator;
import org.lewickiy.service.CourierLifecycle;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {
    static CourierLifecycle courierLifecycle;
    static CourierAssertions courierAssertions;
    Courier courier;
    Credentials credentials;
    Integer courierId;

    @BeforeClass
    public static void setUpBeforeClass() {
        courierLifecycle = new CourierLifecycle();
        courierAssertions = new CourierAssertions();
    }

    @Before
    @Step("Creating a courier data set for registration and authorization")
    public void setUpBeforeTest() {
        courier = CourierGenerator.random();
        credentials = Credentials.from(courier);
    }

    @After
    @Step("Removing a courier by id if the value is not empty")
    public void deleteCourier() {
        if (courier.getLogin() != null && courier.getPassword() != null) {
            courierId = courierLifecycle.registeredCourierLogin(credentials)
                    .extract().path("id");
        }
        if (courierId != null) {
            courierLifecycle.deleteCourierById(courierId)
                    .log().all()
                    .assertThat()
                    .statusCode(HttpURLConnection.HTTP_OK);
        }
    }

    @Test
    @DisplayName("Checking the Authorization of the Registered Courier")
    @Description("The code 200ok and a non-empty id value should be returned")
    @Category(Courier.class)
    public void loginCourier() {
        courierLifecycle.createCourier(courier).log().all();
        courierLifecycle.registeredCourierLogin(credentials)
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Checking the authorization of a registered courier without a login field value")
    @Description("In response to an incorrect request, a status code of 400 and the message should be returned: \"Недостаточно данных для входа\"")
    public void loginCourierWithoutLogin() {
        courierLifecycle.createCourier(courier).log().all();
        credentials.setLogin("");
        courierLifecycle.registeredCourierLogin(credentials).log().all()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Checking the authorization of a registered courier without a password field value")
    @Description("In response to an incorrect request, a status code of 400 and the message should be returned: \"Недостаточно данных для входа\"")
    public void loginCourierWithoutPassword() {
        courierLifecycle.createCourier(courier).log().all();
        credentials.setPassword("");
        courierLifecycle.registeredCourierLogin(credentials).log().all()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Checking the authorization of a registered courier without a password and login field values")
    @Description("In response to an incorrect request, a status code of 400 and the message should be returned: \"Недостаточно данных для входа\"")
    public void loginCourierWithoutPasswordAndLogin() {
        courierLifecycle.createCourier(courier).log().all();
        credentials.setPassword("");
        credentials.setLogin("");
        courierLifecycle.registeredCourierLogin(credentials).log().all()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Attempted authorization with an incorrect login")
    public void loginCourierWithWrongLogin() {
        courierLifecycle.createCourier(courier).log().all();
        credentials.setLogin("wrong_login");
        courierLifecycle.registeredCourierLogin(credentials).log().all()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Attempted authorization with an incorrect password")
    public void loginCourierWithWrongPassword() {
        courierLifecycle.createCourier(courier).log().all();
        credentials.setPassword("wrong_password");
        courierLifecycle.registeredCourierLogin(credentials).log().all()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Attempt to authorize an unregistered courier")
    public void unregisteredCourierLogin() {
        courierLifecycle.createCourier(courier).log().all();
        credentials.setLogin("unregistered_courier_login");
        credentials.setPassword("unregistered_courier_password");
        courierLifecycle.registeredCourierLogin(credentials).log().all()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
