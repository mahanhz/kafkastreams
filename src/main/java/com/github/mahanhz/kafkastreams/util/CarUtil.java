package com.github.mahanhz.kafkastreams.util;

import com.github.mahanhz.kafkastreams.stateful.Car;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.trim;

public final class CarUtil {

    private CarUtil() {
    }

    public static String key(final String year, final String carMake) {
        return StringUtils.lowerCase(trim(year) + "_" + trim(carMake));
    }

    public static String key(final Car car) {
        return key(car.getYear(), car.getMake());
    }
}
