package org.example.orderbook;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Order {
    private final long id;
    //FIXME: shouldn't the order also have the symbol of the instrument?
    private final double price;       //TODO: suggest BigDecimal
    private final char side;          //B == Bid/Buy, O == Offer/Ask/Sell
    private long size;

    public Order(long id, double price, char side, long size) {
        this.id = id;
        this.price = price;
        this.side = side;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public char getSide() {
        return side;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return new EqualsBuilder()
                .append(id, order.id)
                .append(price, order.price)
                .append(side, order.side)
                .append(size, order.size)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(price)
                .append(side)
                .append(size)
                .toHashCode();
    }
}
