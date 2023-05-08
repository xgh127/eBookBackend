package com.zzq.ebook.dao;


import com.zzq.ebook.daoImp.BookDaoImp;
import com.zzq.ebook.daoImp.OrderDaoImp;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.entity.Order;
import com.zzq.ebook.repository.BookRepository;
import com.zzq.ebook.repository.OrderRepository;
import com.zzq.ebook.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class orderDaoTest {
    @InjectMocks
    private OrderDaoImp orderDaoImp;


    @Mock
    private OrderRepository orderRepository;

    @Test
    public void testsaveOneOrder() {
        Order randOrder = new Order();
        when(orderRepository.save(any(Order.class))).thenReturn(randOrder);
        Order result = orderDaoImp.saveOneOrder(randOrder);
        Assertions.assertEquals(randOrder, result);
    }

    @Test
    public void testgetOrderByID(){
        // mock orderRepository.getOne(ID);
        Order randOrder = new Order();
        randOrder.setOrderID(1);
        when(orderRepository.getOne(anyInt())).thenReturn(randOrder);
        Order result = orderDaoImp.getOrderByID(1);
        Assertions.assertEquals(randOrder, result);
    }


    @Test
    public void testgetAllOrder(){
        // mock orderRepository.findAll();
        List<Order> randOrderList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Order randOrder = new Order();
            randOrder.setOrderID(i);
            randOrderList.add(randOrder);
        }
        when(orderRepository.findAll()).thenReturn(randOrderList);
        List<Order> result = orderDaoImp.getAllOrder();
        Assertions.assertEquals(randOrderList, result);

    }



    @Test
    public void testgetUserOrder(){
        // mock orderRepository.findOrdersByBelonguser(username);
        List<Order> randOrderList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Order randOrder = new Order();
            randOrder.setOrderID(i);
            randOrderList.add(randOrder);
        }
        when(orderRepository.findOrdersByBelonguser(any(String.class))).thenReturn(randOrderList);
        List<Order> result = orderDaoImp.getUserOrder("username");
        Assertions.assertEquals(randOrderList, result);

    }

}
