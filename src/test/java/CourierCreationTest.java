import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lewickiy.entity.Courier;
import org.lewickiy.entity.Credentials;
import org.lewickiy.service.CourierAssertions;
import org.lewickiy.service.CourierGenerator;
import org.lewickiy.service.CourierLifecycle;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreationTest {
    Courier courier;
    Courier duplicateCourier;
    Credentials credentials;
    CourierLifecycle courierLifecycle;
    CourierAssertions courierAssertions;
    Integer courierId;

    @Before
    @Step("Creating a courier data set for registration and authorization")
    public void setUp() {
        courier = CourierGenerator.random();
        credentials = Credentials.from(courier);
        courierLifecycle = new CourierLifecycle();
        courierAssertions = new CourierAssertions();
    }

    @After
    @Step("Removing a courier by id if the value is not empty")
    public void deleteCourier() {
        //TODO login take courier id, then delete courier by id
        //courierLifecycle.registeredCourierLogin(courier);
        if (courierId != null) {
            System.out.println(courierId + "!!!!!!!!!!!");
            courierLifecycle.deleteCourierById(courierId).log().all().assertThat().statusCode(HttpURLConnection.HTTP_OK);
        }
    }

    @Test
    @Step
    @DisplayName("Registration of a new courier")
    @Description("We check that the new courier is registered and returns a response code 201 - created and \"ok\": true")
    public void createNewCourier() {
        duplicateCourier = courier;
        courierLifecycle.createCourier(courier)
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @Step
    @DisplayName("Registering a new courier with empty field values")
    @Description("")
    public void createNullCourier() {
        courierAssertions.notEnoughDataCreatingCourier(new Courier());
    }

    @Test
    @Step
    @DisplayName("Registering a new courier with empty login field")
    @Description("")
    public void createNullLoginFieldCourier() {
        courier.setLogin(null);
        courierAssertions.notEnoughDataCreatingCourier(courier);
    }

    @Test
    @Step
    @DisplayName("Registering a new courier with empty password field")
    @Description("")
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
