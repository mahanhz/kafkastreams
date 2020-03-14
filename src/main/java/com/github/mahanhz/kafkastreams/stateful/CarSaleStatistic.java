package com.github.mahanhz.kafkastreams.stateful;

public class CarSaleStatistic {

    private final String make;
    private final String year;
    private long quantity;

    private CarSaleStatistic() {
        this.make = "";
        this.year = "";
        this.quantity = 0L;
    }

    private CarSaleStatistic(final String make, final String year, final long quantity) {
        this.make = make;
        this.year = year;
        this.quantity = quantity;
    }

    public static CarSaleStatistic init() {
        return new CarSaleStatistic();
    }

    public static CarSaleStatistic of(final String make, final String year, final long quantity) {
        return new CarSaleStatistic(make, year, quantity);
    }

    public String getMake() {
        return make;
    }

    public String getYear() {
        return year;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
