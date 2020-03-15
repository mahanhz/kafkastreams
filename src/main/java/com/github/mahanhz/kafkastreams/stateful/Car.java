package com.github.mahanhz.kafkastreams.stateful;

public class Car {

    private final String id;
    public final String make;
    public final String year;

    public Car() {
        this.id = "";
        this.make = "";
        this.year = "";
    }

    public Car(final String id, final String make, final String year) {
        this.id = id;
        this.make = make;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public String getMake() {
        return make;
    }

    public String getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", make='" + make + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
