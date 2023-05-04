package com.zzq.ebook.serviceImp;

import com.zzq.ebook.dao.BookDao;
import com.zzq.ebook.dao.OrderItemDao;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.entity.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private List<OrderItem> orderItems =new ArrayList<>(2);
    private Book book = new Book();//桩程序的返回值
    private OrderItem orderItem = new OrderItem();
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
    @CsvFileSource(resources = {"/serviceImp-test-data/addOneOrderItemToChart-Data.csv"})
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

    @Test
    void editOneOrderItemBUYNUMInChart() {
    }

    @Test
    void orderMakeFromShopCart() {
    }

    @Test
    void orderMakeFromDirectBuy() {
    }

    @ParameterizedTest
    @CsvFileSource( resources = {"/serviceImp-test-data/findAllOrderItemInCart-Data.csv"})
    void findAllOrderItemInCart(String username,Integer expect) {
        Mockito.when(orderItemDao.queryOneUserShopCart(username)).thenReturn(orderItems);

        List<OrderItem> orderItem = orderService.findAllOrderItemInCart(username);
        assertEquals(expect,orderItem.size());
    }

    @Test
    void getOneOrder() {
    }

    @Test
    void getAllOrder() {
    }

    @Test
    void getAllOrderItem() {
    }

    @Test
    void getAllOrderItemWithBook() {
    }

    @Test
    void getUserOrderItem() {
    }

    @Test
    void getUserOrder() {
    }

    @Test
    void getUserOrderItemWithBook() {
    }
}