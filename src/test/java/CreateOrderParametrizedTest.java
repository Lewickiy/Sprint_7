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
public class CreateOrderTest {
    static OrderLifecycle orderLifecycle;
    Order order;
    Integer orderId;

    @Parameterized.Parameter
    public Color[] colors;

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {new Color[] {GREY, BLACK}},
                {new Color[] {GREY}},
                {new Color[] {}}
        };
    }
            /*
        Проверь, что когда создаёшь заказ:
        можно указать один из цветов — BLACK или GREY;
        можно указать оба цвета;
        можно совсем не указывать цвет;
        тело ответа содержит track.
        Чтобы протестировать создание заказа, нужно использовать параметризацию.
            */

    @BeforeClass
    @Step("")
    public static void setUpBeforeClass() {
        orderLifecycle = new OrderLifecycle();
    }

    @Before
    @Step("Creating a courier data set for registration and authorization")
    public void setUpBeforeTest() {
        order = OrderGenerator.random();
    }

    @After
    @Step()
    public void deleteOrder() {
        orderLifecycle.cancelOrder(orderId).assertThat().statusCode(HttpURLConnection.HTTP_OK);
    }

    @Test
    @DisplayName("Creating an order returns the track number")
    @Description("")
    public void orderCreationReturnsTrackNumber() {
        order.setColor(colors);
        orderId = orderLifecycle.createOrder(order)
                .log().all()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .body("track", notNullValue()).extract().path("track");
    }
}
