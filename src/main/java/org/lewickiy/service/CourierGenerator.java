package org.lewickiy.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.lewickiy.entity.Courier;

public class CourierGenerator {
    public static Courier random() {
        return new Courier("Autotest" + RandomStringUtils.randomAlphanumeric(5, 10)
                , "simplePassword", "Autotest"
        );
    }
}
