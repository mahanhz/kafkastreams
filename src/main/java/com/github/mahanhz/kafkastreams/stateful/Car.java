package com.github.mahanhz.kafkastreams.stateful;

public class Car {

    public final String make;
    public final String year;

    public Car() {
        this.make = "";
        this.year = "";
    }

    public Car(final String make, final String year) {
        this.make = make;
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public String getYear() {
        return year;
    }
}
