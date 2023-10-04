import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.lewickiy.entity.Color;
import org.lewickiy.entity.Order;
import org.lewickiy.service.OrderGenerator;
import org.lewickiy.service.OrderLifecycle;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.lewickiy.entity.Color.BLACK;
import static org.lewickiy.entity.Color.GREY;

@RunWith(Parameterized.class)
public class CreateOrderParametrizedTest {
    static OrderLifecycle orderLifecycle;
    Order order;
    Integer orderId;

    @Parameterized.Parameter
    public Color[] colors;

    @Parameterized.Parameters(name = "An array of colors {0} is added to the order")
    public static Object[][] getTestData() {
        return new Object[][] {
                {new Color[] {GREY, BLACK}},
                {new Color[] {GREY, null}},
                {new Color[] {null, null}}
        };
    }

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
    public void cancelOrder() {
        orderLifecycle.cancelOrder(orderId).assertThat().statusCode(HttpURLConnection.HTTP_OK);
    }

    @Test
    @DisplayName("Creating an order with a combination of parameters")
    @Description("It is possible to create an Order with one or two color options, or without color options")
    public void orderCreateCombinationColorsParameters() {
        order.setColor(colors);
        orderId = orderLifecycle.createOrder(order)
                .log().all()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .body("track", notNullValue())
                .extract().path("track");
    }
}
