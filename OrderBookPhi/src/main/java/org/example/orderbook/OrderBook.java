package org.example.orderbook;

import java.util.*;

import static java.util.stream.Collectors.toCollection;

public class OrderBook {

    private final String symbol;

    private final SortedMap<Double, Map<Long, Order>> bidMap = new TreeMap<>(Comparator.reverseOrder());

    private final SortedMap<Double, Map<Long, Order>> offerMap = new TreeMap<>();


    public OrderBook(String symbol) {
        this.symbol = symbol;
    }


    public synchronized void add(Order orderNew) {

        //TODO: check if order is invalid or if symbol != Order.symbol

        long orderId = orderNew.getId();
        double orderPrice = orderNew.getPrice();
        SortedMap<Double, Map<Long, Order>> orderSideMap = getSideOrdersMap(orderNew);

        if (orderSideMap.containsKey(orderPrice)) {
            Map<Long, Order> ordersForPriceMap = orderSideMap.get(orderPrice);
            ordersForPriceMap.put(orderId, orderNew);
        } else {
            Map<Long, Order> ordersForPriceMap = new LinkedHashMap<>();
            ordersForPriceMap.put(orderId, orderNew);
            orderSideMap.put(orderPrice, ordersForPriceMap);
        }


    }

    public void remove(Order orderToRemove) {
        long orderId = orderToRemove.getId();
        double orderPrice = orderToRemove.getPrice();

        SortedMap<Double, Map<Long, Order>> orderSideMap = getSideOrdersMap(orderToRemove);
        if (orderSideMap.containsKey(orderPrice)) {
            Order orderExisting = orderSideMap.get(orderPrice).get(orderId);
            if (orderExisting.equals(orderToRemove)) {
                orderSideMap.get(orderPrice).remove(orderId);
            }
        }

    }


    public synchronized void modify(Order orderToModify) {
        long orderId = orderToModify.getId();
        double orderPrice = orderToModify.getPrice();

        //assumption 1: only the size is modified, not the price or side
        //assumption 2: the order in the orderbook is preserved (order is not moved to end

        SortedMap<Double, Map<Long, Order>> orderSideMap = getSideOrdersMap(orderToModify);


        if (!orderSideMap.containsKey(orderPrice) || !orderSideMap.get(orderPrice).containsKey(orderId)) {
            throw new RuntimeException("Order does not exist");     //FIXME: create proper Exception
        }

        orderSideMap.get(orderPrice).get(orderId).setSize(orderToModify.getSize());

    }

    public Double getPriceForSideAndLeve(char side, int level) {
        SortedMap<Double, Map<Long, Order>> orderSideMap = getSideOrdersMap(side);

        if (level > orderSideMap.size()) {
            throw new RuntimeException("Level exceeds orderbook depth");     //FIXME: create proper Exception
        }
        Double price = null;

        int i = 1;
        for (Double k : orderSideMap.keySet()) {
            if (i == level) {
                price = k;
                break;
            } else {
                i++;
            }
        }

        return price;
    }

    public int getTotalSizeForSideAndLeve(char side, int level) {
        SortedMap<Double, Map<Long, Order>> orderSideMap = getSideOrdersMap(side);

        if (level > orderSideMap.size()) {
            throw new RuntimeException("Level exceeds orderbook depth");     //FIXME: create proper Exception
        }

        Map<Long, Order> orderMapForLevel = null;
        int i = 1;
        for (Double k : orderSideMap.keySet()) {
            if (i == level) {
                //price = k;
                orderMapForLevel = orderSideMap.get(k);
                break;
            } else {
                i++;
            }
        }

        int size = 0;
        for (Order o : orderMapForLevel.values()) {
            size += o.getSize();
        }

        return size;
    }


    public Map<Integer, ArrayList<Order>> getOrderBySideOrdered(char side) {
        Map<Integer, ArrayList<Order>> orderedOrdersMap = new LinkedHashMap<>();

        SortedMap<Double, Map<Long, Order>> orderSideMap = getSideOrdersMap(side);

        int level = 1;
        for (Double k : orderSideMap.keySet()) {
            orderedOrdersMap.put(level, orderSideMap.get(k).values().stream().collect(toCollection(ArrayList::new)));
            level++;
        }

        return orderedOrdersMap;
    }



    private SortedMap<Double, Map<Long, Order>> getSideOrdersMap(Order order) {
        char side = order.getSide();

        return getSideOrdersMap(side);
    }

    private SortedMap<Double, Map<Long, Order>> getSideOrdersMap(char side) {
        SortedMap<Double, Map<Long, Order>> orderSideMap;
        switch (side) {
            case 'B':
                orderSideMap = bidMap;
                break;
            case 'O':
                orderSideMap = offerMap;
                break;
            default:
                throw new RuntimeException("Invalid side");     //FIXME: create proper Exception
        }

        return orderSideMap;
    }


}
