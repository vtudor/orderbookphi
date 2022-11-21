package org.example.orderbook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderBookTest {

    private OrderBook orderBook;

    @BeforeEach
    void setUp() {

        orderBook = new OrderBook("VOD.L");

        Order order1 = new Order(1, 100, 'B', 10);
        Order order2 = new Order(2, 102, 'B', 20);
        Order order3 = new Order(3, 103, 'B', 30);
        Order order4 = new Order(4, 100, 'B', 40);
        Order order5 = new Order(5, 100, 'B', 50);
        Order order6 = new Order(6, 105.5, 'O', 55);
        Order order7 = new Order(7, 104.5, 'O', 45);
        Order order8 = new Order(8, 103.5, 'O', 35);
        Order order9 = new Order(9, 102.5, 'O', 45);

        orderBook.add(order1);
        orderBook.add(order2);
        orderBook.add(order3);
        orderBook.add(order4);
        orderBook.add(order5);
        orderBook.add(order6);
        orderBook.add(order7);
        orderBook.add(order8);
        orderBook.add(order9);
    }

    @Test
    @DisplayName("Should throw an exception when the order does not exist")
    void removeWhenOrderDoesNotExistThenThrowException() {
        Order orderToRemove = new Order(10, 100, 'B', 10);

        assertThrows(RuntimeException.class, () -> orderBook.remove(orderToRemove));
    }


    @Test
    @DisplayName("Should throw an exception when the level exceeds orderbook depth")
    void getTotalSizeForSideAndLeveShouldThrowExceptionWhenTheLevelExceedsOrderbookDepth() {
        assertThrows(RuntimeException.class, () -> orderBook.getTotalSizeForSideAndLeve('B', 100));
    }

    @Test
    @DisplayName("Should return the total size for the given side and level")
    void getTotalSizeForSideAndLeveShouldReturnTheTotalSizeForTheGivenSideAndLevel() {

        assertEquals(30, orderBook.getTotalSizeForSideAndLeve('B', 1));
        assertEquals(20, orderBook.getTotalSizeForSideAndLeve('B', 2));
        assertEquals(100, orderBook.getTotalSizeForSideAndLeve('B', 3));
    }

    @Test
    @DisplayName(
            "Should add the order to the map of orders for that price when the price is in the map")
    void addWhenPriceIsInTheMap() {
        Order order = new Order(1, 100.0, 'B', 100);
        orderBook.add(order);

        orderBook.add(order);

        assertEquals(1, orderBook.getOrderBySideOrdered('B').size());
    }

    @Test
    @DisplayName("Should add the order to the order book when the price is not in the map")
    void addWhenPriceIsNotInTheMap() {
        Order order = new Order(1, 99.0, 'B', 99);

        orderBook.add(order);

        assertEquals(1, orderBook.getOrderBySideOrdered('B').size());
        assertEquals(1, orderBook.getOrderBySideOrdered('B').get(1).size());
    }
}