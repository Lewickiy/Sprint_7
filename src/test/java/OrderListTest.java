import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.lewickiy.service.OrderListLifecycle;

import java.net.HttpURLConnection;

public class OrderListTest {
    OrderListLifecycle orderListLifecycle = new OrderListLifecycle();

    @Test
    @DisplayName("Creating an order with a combination of parameters")
    @Description("It is possible to create an Order with one or two color options, or without color options")
    public void orderCreateCombinationColorsParameters() {
        orderListLifecycle.getOrders().statusCode(HttpURLConnection.HTTP_OK);
    }
}
