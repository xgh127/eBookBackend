package com.zzq.ebook.dao;

import com.zzq.ebook.daoImp.OrderDaoImp;
import com.zzq.ebook.daoImp.OrderItemImp;
import com.zzq.ebook.entity.OrderItem;
import com.zzq.ebook.repository.OrderItemRepository;
import com.zzq.ebook.repository.OrderRepository;
import net.sf.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class orderItemTest {

    @InjectMocks
    private OrderItemImp orderItemImp;

    @Mock
    private OrderItemRepository orderItemRepository;


    @Test
    public void testaddOneOrderItem() {
        OrderItem newOrderItem = new OrderItem();
        // mock orderItemRepository.save(newOrderItem);
        when(orderItemRepository.save(newOrderItem)).thenReturn(newOrderItem);
        OrderItem result = orderItemImp.addOneOrderItem(newOrderItem);
        assert(result == newOrderItem);
    }


    @Test
    public void testqueryOneUserShopCart(){
        List<OrderItem> randOrderItemList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            OrderItem randOrderItem = new OrderItem();
            randOrderItem.setOrderID(i);
            randOrderItemList.add(randOrderItem);
        }

        // mock orderItemRepository.findAllByUserID(userID);
        when(orderItemRepository.findUserShopCartItem("zzq")).thenReturn(randOrderItemList);

        List<OrderItem> result = orderItemImp.queryOneUserShopCart("zzq");

        assert(result == randOrderItemList);
    }


    @Test
    public void testcheckUserOrderItemByID(){
        OrderItem randOrderItem = new OrderItem();
        randOrderItem.setOrderID(1);
//        randOrderItem.setUserID("zzq");

        // orderItemRepository.findUserShopCartItemOfBook(username,bookID); mock
        when(orderItemRepository.findUserShopCartItemOfBook("zzq",1)).thenReturn(randOrderItem);

        OrderItem result = orderItemImp.checkUserOrderItemByID("zzq",1);
        assert(result == randOrderItem);
    }


    @Test
    public void testsaveOneOrderItem(){
        OrderItem randOrderItem = new OrderItem();
        randOrderItem.setOrderID(1);

        // orderItemRepository.save(randOrderItem); mock
        when(orderItemRepository.save(randOrderItem)).thenReturn(randOrderItem);

        OrderItem result = orderItemImp.saveOneOrderItem(randOrderItem);
        assert(result == randOrderItem);
    }

    @Test
    public void testsaveAllOrderItems(){
        List<OrderItem> randOrderItemList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            OrderItem randOrderItem = new OrderItem();
            randOrderItem.setOrderID(i);
            randOrderItemList.add(randOrderItem);
        }

        // orderItemRepository.saveAll(randOrderItemList); mock
        when(orderItemRepository.saveAll(randOrderItemList)).thenReturn(randOrderItemList);

        List<OrderItem> result = orderItemImp.saveAllOrderItems(randOrderItemList);
        assert(result == randOrderItemList);
    }

    @Test
    public void testgetAllOrderItem(){
        List<OrderItem> randOrderItemList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            OrderItem randOrderItem = new OrderItem();
            randOrderItem.setOrderID(i);
            randOrderItemList.add(randOrderItem);
        }

        // orderItemRepository.findAll(); mock
        when(orderItemRepository.findAll()).thenReturn(randOrderItemList);

        List<OrderItem> result = orderItemImp.getAllOrderItem();
        assert(result == randOrderItemList);
    }

    @Test
    public void testgetUserOrderItem(){
        List<OrderItem> randOrderItemList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            OrderItem randOrderItem = new OrderItem();
            randOrderItem.setOrderID(i);
            randOrderItemList.add(randOrderItem);
        }

        // orderItemRepository.findAllByUserID(userID); mock
        when(orderItemRepository.findOrderItemsByBelonguser("zzq")).thenReturn(randOrderItemList);


        List<OrderItem> result = orderItemImp.getUserOrderItem("zzq");
        assert(result == randOrderItemList);

    }


    //getOrderItemByBookID
    @Test
    public void testgetOrderItemByBookID(){
        OrderItem randOrderItem = new OrderItem();

        // orderItemRepository.findAllByUserID(userID); mock
        when(orderItemRepository.findOrderItemByBookID(1)).thenReturn(randOrderItem);

        OrderItem result = orderItemImp.getOrderItemByBookID(1);
        assert(result == randOrderItem);

    }


    @Test
    public void testuserConsumeStatistic(){
        // mock orderItemRepository.userConsumeStatistic(starttime,endtime);
        JSONArray jsonArray = new JSONArray();

        Date date1 = new Date();
        Date date2 = new Date();


        when(orderItemRepository.userConsumeStatistic(date1, date2)).thenReturn(jsonArray);

        JSONArray result = orderItemImp.userConsumeStatistic(date1, date2);
        assert(result == jsonArray);
    }

    @Test
    public void testuserbookSellnumStatistic(){
        JSONArray jsonArray = new JSONArray();

        Date date1 = new Date();
        Date date2 = new Date();

        when(orderItemRepository.bookSellnumStatistic(date1, date2)).thenReturn(jsonArray);

        JSONArray result = orderItemImp.bookSellnumStatistic(date1, date2);
        assert(result == jsonArray);
    }

    // userSelfStatistic_BookWithBuyNum test
    @Test
    public void testuserSelfStatistic_BookWithBuyNum(){
        JSONArray jsonArray = new JSONArray();

        Date date1 = new Date();
        Date date2 = new Date();

        when(orderItemRepository.userSelfStatistic_BookWithBuyNum( date1, date2, "zzq")).thenReturn(jsonArray);

        JSONArray result = orderItemImp.userSelfStatistic_BookWithBuyNum(date1, date2, "zzq");
        assert(result == jsonArray);
    }

    // userSelfStatistic_BookAllBuyNum
    @Test
    public void testuserSelfStatistic_BookAllBuyNum(){
        JSONArray jsonArray = new JSONArray();

        Date date1 = new Date();
        Date date2 = new Date();

        when(orderItemRepository.userSelfStatistic_BookAllBuyNum( date1, date2, "zzq")).thenReturn(jsonArray);

        JSONArray result = orderItemImp.userSelfStatistic_BookAllBuyNum(date1, date2, "zzq");
        assert(result == jsonArray);
    }

    // userSelfStatistic_BookTotalPay
    @Test
    public void testuserSelfStatistic_BookTotalPay(){
        JSONArray jsonArray = new JSONArray();

        Date date1 = new Date();
        Date date2 = new Date();

        when(orderItemRepository.userSelfStatistic_BookTotalPay( date1, date2, "zzq")).thenReturn(jsonArray);

        JSONArray result = orderItemImp.userSelfStatistic_BookTotalPay(date1, date2, "zzq");
        assert(result == jsonArray);
    }

}
