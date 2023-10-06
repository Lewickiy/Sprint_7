package org.lewickiy.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.lewickiy.entity.Color;
import org.lewickiy.entity.Order;

public class OrderGenerator {

    public static Order random() {
        Order order = new Order();
        order.setFirstName("FirstnameTest" + RandomStringUtils.randomAlphanumeric(5, 10));
        order.setLastName("LastnameTest" + RandomStringUtils.randomAlphanumeric(5, 10));
        order.setAddress("AddressTest" + RandomStringUtils.randomAlphanumeric(5, 10));
        order.setMetroStation("MetroStationName" + RandomStringUtils.randomAlphanumeric(3, 6));
        order.setPhone(randomPhone());
        order.setRentTime(getRentTime());
        order.setDeliveryDate(getFixDate());
        order.setComment("CommentTest" + RandomStringUtils.randomAlphanumeric(5, 10));
        order.setColor(getColorRandom()); //Здесь под вопросом генерация массива. Должен ли быть массив с 2 ячейками или для одного значения нужен массив с одной ячейкой. Если что, можно быстро переделать
        return order;
    }


    private static Color[] getColorRandom() {
        Color[] colors;
        switch ((int) Math.floor(Math.random() *(4 - 1 + 1) + 1)) {
            case (1):
                colors = new Color[]{Color.BLACK};
                break;
            case (2):
                colors = new Color[]{Color.GREY};
                break;
            case (3):
                colors = new Color[2];
                break;
            default:
                colors = new Color[] {Color.BLACK, Color.GREY};
        }
        return colors;
    }

    private static String getFixDate() {
        return "2024-02-02";
    }

    private static String randomPhone() {
        int start = 79010;
        int end = 79999;
        int startTwo = 100000;
        int endTwo = 999999;
        end -= start;
        endTwo -= startTwo;

        String startPhone = String.valueOf( (int) (Math.random() * ++end) + start);
        String endPhone = String.valueOf( (int) (Math.random() * ++endTwo) + startTwo);
        return startPhone + endPhone;
    }

    private static Integer getRentTime() {
        return (int) (Math.random() * 10);
    }
}
