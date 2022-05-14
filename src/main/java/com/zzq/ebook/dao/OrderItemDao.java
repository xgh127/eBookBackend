package com.zzq.ebook.dao;

import com.zzq.ebook.entity.OrderItem;

import java.util.List;

public interface OrderItemDao {
    OrderItem addOneOrderItem(OrderItem newOrder);

    List<OrderItem> queryOneUserShopCart(String username);


    OrderItem checkUserOrderItemByID(String username, int bookID);


    OrderItem saveOneOrderItem(OrderItem saveObj);
}
