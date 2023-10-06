import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lewickiy.entity.Courier;
import org.lewickiy.entity.Credentials;
import org.lewickiy.service.CourierAssertions;
import org.lewickiy.service.CourierGenerator;
import org.lewickiy.service.CourierLifecycle;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreationTest {
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
            courierId = courierLifecycle.registeredCourierLogin(credentials).extract().path("id");
        }
        if (courierId != null) {
            courierLifecycle.deleteCourierById(courierId)
                    .log().all()
                    .assertThat()
                    .statusCode(HttpURLConnection.HTTP_OK);
        }
    }

    @Test
    @Step
    @DisplayName("Registration of a new courier")
    @Description("We check that the new courier is registered and returns a response code 201 - created and \"ok\": true")
    public void createNewCourier() {
        courierLifecycle.createCourier(courier)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @Step
    @DisplayName("Registering a new courier with empty field values")
    public void createNullCourier() {
        courierAssertions.notEnoughDataCreatingCourier(new Courier());
    }

    @Test
    @Step
    @DisplayName("Registering a new courier with empty login field")
    public void createNullLoginFieldCourier() {
        courier.setLogin(null);
        courierAssertions.notEnoughDataCreatingCourier(courier);
    }

    @Test
    @Step
    @DisplayName("Registering a new courier with empty password field")
    public void createNullPasswordFieldCourier() {
        courier.setPassword(null);
        courierAssertions.notEnoughDataCreatingCourier(courier);
    }

    @Test
    @Step
    @DisplayName("Checking an attempt to register a duplicate courier")
    @Description("This test is considered passed if, when trying to register a duplicate courier, the following are returned: status code 409 and a message \"Этот логин уже используется. Попробуйте другой.\"")
    public void createDuplicateCourier() {
        courierLifecycle.createCourier(courier)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED);
        courierLifecycle.createCourier(courier)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой.")
        );
    }
}
