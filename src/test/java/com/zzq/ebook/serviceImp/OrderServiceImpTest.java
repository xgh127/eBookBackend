package com.zzq.ebook.serviceImp;

import com.zzq.ebook.dao.BookDao;
import com.zzq.ebook.dao.OrderItemDao;
import com.zzq.ebook.daoImp.BookDaoImp;
import com.zzq.ebook.daoImp.OrderItemImp;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.entity.OrderItem;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO：除了已经测试的2个函数还剩下10个，分为3组：3+4+3，分别由hhq，lsk，zzq完成，内容同Controller层的测试
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceImpTest {
    @InjectMocks
    OrderServiceImp orderService;
    @Mock
    OrderItemDao orderItemDao;
    @Mock
    BookDao bookDao;

    List<OrderItem> orderItems =new ArrayList<>(2);
    Book book = new Book();//桩程序的返回值
    OrderItem orderItem = new OrderItem();
    @BeforeEach
    void init(){
        for(int i = 0; i < 2 ; i++){//随便设的
            OrderItem orderItem = new OrderItem();
            orderItem.setItemID(i);
            orderItems.add(orderItem);
        }
        //初始化book，以便于后面作为桩程序的返回值
        book.setBookname("bookname");
        book.setInventory(100);
        //初始化orderItem，以便于后面作为桩程序的返回值
        orderItem.setItemID(1);
        orderItem.setBookID(1);
        orderItem.setBuynum(20);
    }

    @ParameterizedTest
    @CsvFileSource( resources = {"/serviceImp-test-data/addOneOrderItemToChart-Data.csv"})
    void addOneOrderItemToChart(String username, Integer bookID,Integer buyNum) {
        //桩程序，确保返回的book是我们想要的
        Mockito.when(bookDao.getOneBookByID(bookID)).thenReturn(book);
        if (buyNum > 100){
            //如果超过容量，返回null
            assertNull(orderService.addOneOrderItemToChart(username,bookID,buyNum));
        }
        else if (username == "user2"){
            assertNotNull(orderService.addOneOrderItemToChart(username,bookID,buyNum));
        }else {
            Mockito.when(orderItemDao.checkUserOrderItemByID(username,bookID)).thenReturn(orderItem);
        //如果购物车容量+购买数量大于100，返回null
            if (orderItem.getBuynum() + buyNum > 100){
                assertNull(orderService.addOneOrderItemToChart(username,bookID,buyNum));
            }
            else {
                assertNotNull(orderService.addOneOrderItemToChart(username,bookID,buyNum));
            }

        }

    }

    /**
     * @param username 购买用户的用户名
     * @param bookID 购买书的ID
     * @param refreshedNum 购物车刷新后的书本的数量
     */
    @ParameterizedTest
    @CsvFileSource( resources = {"/serviceImp-test-data/editOneOrderItemBUYNUMInChart-Data.csv"})
    void editOneOrderItemBUYNUMInChart(String username, Integer bookID,Integer refreshedNum) {
        // 模拟书籍查找的结果
        if (bookID == 1 && username == "user1"){
            Mockito.when(bookDao.getOneBookByID(bookID)).thenReturn(book);
        }
        // 模拟购物车查找的结果
        if (username == "user1" && bookID == 1){
            Mockito.when(orderItemDao.checkUserOrderItemByID(username,  bookID)).thenReturn(orderItem);
        }
        else{
            Mockito.when(orderItemDao.checkUserOrderItemByID(username,  bookID)).thenReturn(null);
        }

        // 模拟刷新购物车里面书本数量的函数
        int result = orderService.editOneOrderItemBUYNUMInChart(username,bookID,refreshedNum);
        if (bookID == 1 && username == "user1"){
            assertEquals(0, result);
        }
        if (username == "user2"){
            assertEquals(-1, result);
        }

        if (refreshedNum > 100 && username == "user1" && bookID == 1){
            assertEquals(-2, result);
        }

        if (refreshedNum > 100 && username == "user1" && bookID > 1){
            assertEquals(-1, result);
        }

    }
//
//    @Test
//    void orderMakeFromShopCart() {
//    }
//
//    @Test
//    void orderMakeFromDirectBuy() {
//    }
//
    @ParameterizedTest
    @CsvFileSource( resources = {"/serviceImp-test-data/findAllOrderItemInCart-Data.csv"})
    void findAllOrderItemInCart(String username,Integer expect) {
        Mockito.when(orderItemDao.queryOneUserShopCart(username)).thenReturn(orderItems);

        List<OrderItem> orderItem = orderService.findAllOrderItemInCart(username);
        assertEquals(expect,orderItem.size());
    }
//
//    @Test
//    void getOneOrder() {
//    }
//
//    @Test
//    void getAllOrder() {
//    }
//
//    @Test
//    void getAllOrderItem() {
//        assertEquals(1,1);
//    }
//
//    @Test
//    void getAllOrderItemWithBook() {
//    }
//
//    @Test
//    void getUserOrderItem() {
//    }
//
//    @Test
//    void getUserOrder() {
//    }
//
//    @Test
//    void getUserOrderItemWithBook() {
//    }
}