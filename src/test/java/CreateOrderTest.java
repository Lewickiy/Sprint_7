import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lewickiy.entity.Order;
import org.lewickiy.service.OrderGenerator;
import org.lewickiy.service.OrderLifecycle;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {
    static OrderLifecycle orderLifecycle;
    Order order;
    Integer orderId;

    @BeforeClass
    @Step("Before running all tests, an instance of the OrderLifecycle class is created")
    public static void setUpBeforeClass() {
        orderLifecycle = new OrderLifecycle();
    }

    @Before
    @Step("Before the test, an instance of the Order class is created")
    public void setUpBeforeTest() {
        order = OrderGenerator.random();
    }

    @After
    @Step("After the test the order is canceled")
    public void deleteOrder() {
        orderLifecycle.cancelOrder(orderId).assertThat().statusCode(HttpURLConnection.HTTP_OK);
    }

    @Test
    @DisplayName("Creating an order returns the track number")
    public void orderCreationReturnsTrackNumber() {
        orderId = orderLifecycle.createOrder(order)
                .log().all()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .body("track", notNullValue()).extract().path("track");
    }
}
