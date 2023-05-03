package com.zzq.ebook.serviceImp;

import com.zzq.ebook.dao.BookDao;
import com.zzq.ebook.dao.OrderDao;
import com.zzq.ebook.dao.OrderItemDao;
import com.zzq.ebook.daoImp.BookDaoImp;
import com.zzq.ebook.daoImp.OrderItemImp;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.entity.Order;
import com.zzq.ebook.entity.OrderItem;
import net.sf.json.JSONArray;
import org.aspectj.weaver.ast.Or;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    OrderDao orderDao;

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

    /**
     * 这个函数没有参数，因为函数的参数有一些特殊，所以不从csv文件读取
     */
    @Test
    void orderMakeFromShopCart() {


    }

    @Test
    void orderMakeFromDirectBuy() {
        int []bookIDGroup = {1};
        int []bookNumGroup = {1};
        String username = "user1";
        String receivename = "test";
        String postcode = "123456";
        String phonenumber = "12345678910";
        String receiveaddress = "test";
        int site = 1;

        // 创建模拟的订单数据，因为读取csv反而不方便
        Order newOrder = new Order();
        newOrder.setOrderID(1);
        newOrder.setBelonguser(username);
        newOrder.setContactphone(phonenumber);
        newOrder.setDestination(receiveaddress);
        newOrder.setReceivername(receivename);
        newOrder.setPostalcode(postcode);
        newOrder.setCreate_time(new Timestamp(System.currentTimeMillis()));
        // 设置总价
        newOrder.setTotalprice(50);

        OrderItem oneitem = new OrderItem();
        oneitem.setStatus(2);
        oneitem.setBelonguser(username);
        oneitem.setOrderID(1);
        oneitem.setBookID(bookIDGroup[0]);
        oneitem.setBuynum(bookNumGroup[0]);
        oneitem.setPayprice(100);
        oneitem.setCreate_Itemtime(new Timestamp(System.currentTimeMillis()));

        // 模拟书籍查找的结果
        Mockito.when(bookDao.getOneBookByID(bookIDGroup[0])).thenReturn(book);
        Mockito.when(orderDao.saveOneOrder(Mockito.any())).thenReturn(newOrder);
        Mockito.when(orderItemDao.saveOneOrderItem(Mockito.any())).thenReturn(oneitem);
        Mockito.when(bookDao.saveOneBook(Mockito.any())).thenReturn(book);

        int res = 1;
        try {
            res = orderService.orderMakeFromDirectBuy(bookIDGroup,bookNumGroup,username,receivename,postcode,phonenumber,receiveaddress,site);
        }catch (Exception e){
            System.out.println(e);
        }
        assertEquals(0, res);
    }


    @ParameterizedTest
    @CsvFileSource( resources = {"/serviceImp-test-data/findAllOrderItemInCart-Data.csv"})
    void findAllOrderItemInCart(String username,Integer expect) {
        Mockito.when(orderItemDao.queryOneUserShopCart(username)).thenReturn(orderItems);

        List<OrderItem> orderItem = orderService.findAllOrderItemInCart(username);
        assertEquals(expect,orderItem.size());
    }
//
    @Test
    void getOneOrder() {
        // 这一步骤也不需要额外的操作，因为orderDao的函数已经被mock了
        // 我们只需要判断模拟的数据和返回的数据是否一致即可
        Order testOrder = new Order();
        testOrder.setOrderID(1);
        testOrder.setBelonguser("user1");
        testOrder.setContactphone("12345678910");
        testOrder.setDestination("test");
        testOrder.setReceivername("test");
        Mockito.when(orderDao.getOrderByID(1)).thenReturn(testOrder);
        Order order1 = orderService.getOneOrder(1);

        assertEquals(testOrder.getOrderID(), order1.getOrderID());
        assertEquals(testOrder.getBelonguser(), order1.getBelonguser());
        assertEquals(testOrder.getContactphone(), order1.getContactphone());
        assertEquals(testOrder.getDestination(), order1.getDestination());
        assertEquals(testOrder.getReceivername(), order1.getReceivername());
    }
//
    OrderItem generateRandomOrderItem(){
        OrderItem testOrderItem = new OrderItem();
        testOrderItem.setBuynum((int)(Math.random() * 100));
        testOrderItem.setBookID(1);
        testOrderItem.setPayprice((int)(Math.random() * 100));
        testOrderItem.setOrderID((int)(Math.random() * 100));
        testOrderItem.setBelonguser("user1");
        testOrderItem.setCreate_Itemtime(new Timestamp(System.currentTimeMillis()));
        testOrderItem.setStatus(1);
        return testOrderItem;
    }

    Order generateRandomOrder(){
        Order testOrder = new Order();
        testOrder.setOrderID((int)(Math.random() * 100));
        testOrder.setBelonguser("user1");
        testOrder.setContactphone("12345678910");
        testOrder.setDestination("test");
        testOrder.setReceivername("test");
        testOrder.setPostalcode("123456");
        testOrder.setCreate_time(new Timestamp(System.currentTimeMillis()));
        testOrder.setTotalprice((int)(Math.random() * 100));
        return testOrder;
    }

    @Test
    void getAllOrder() {
        // 模拟两个订单的数据
        Order testOrder1 = generateRandomOrder();
        Order testOrder2 = generateRandomOrder();
        OrderItem testOrderItem1 = generateRandomOrderItem();
        OrderItem testOrderItem2 = generateRandomOrderItem();
        OrderItem testOrderItem3 = generateRandomOrderItem();
        OrderItem testOrderItem4 = generateRandomOrderItem();

        // 创建绑定关系
        testOrderItem1.setOrderID(testOrder1.getOrderID());
        testOrderItem2.setOrderID(testOrder1.getOrderID());
        testOrderItem3.setOrderID(testOrder2.getOrderID());
        testOrderItem4.setOrderID(testOrder2.getOrderID());

        // 创建绑定关系
        List<OrderItem> testOrderItemList1 = new ArrayList<>();
        testOrderItemList1.add(testOrderItem1);
        testOrderItemList1.add(testOrderItem2);
        List<OrderItem> testOrderItemList2 = new ArrayList<>();
        testOrderItemList2.add(testOrderItem3);
        testOrderItemList2.add(testOrderItem4);

        // 绑定父子关系
        testOrder1.setChileItem(testOrderItemList1);
        testOrder2.setChileItem(testOrderItemList2);


        // 模拟数据
        List<Order> testOrderList = new ArrayList<>();
        testOrderList.add(testOrder1);
        testOrderList.add(testOrder2);

        // 模拟数据
        Mockito.when(orderDao.getAllOrder()).thenReturn(testOrderList);
        Mockito.when(bookDao.getOneBookByID(Mockito.anyInt())).thenReturn(book);

        JSONArray result = orderService.getAllOrder();
        System.out.println(result);

        // 判断返回的数据是否正确
        assertEquals(testOrderList.size(), result.size());
        assertEquals(String.valueOf(testOrder1.getOrderID()), result.getJSONObject(0).getString("orderID"));
        assertEquals(String.valueOf(testOrder2.getOrderID()), result.getJSONObject(1).getString("orderID"));
        assertEquals(String.valueOf(testOrder1.getBelonguser()), result.getJSONObject(0).getString("belonguser"));
        assertEquals(String.valueOf(testOrder2.getBelonguser()), result.getJSONObject(1).getString("belonguser"));
        assertEquals(String.valueOf(testOrder1.getReceivername()), result.getJSONObject(0).getString("receivername"));
        assertEquals(String.valueOf(testOrder2.getReceivername()), result.getJSONObject(1).getString("receivername"));
        assertEquals(String.valueOf(testOrder1.getContactphone()), result.getJSONObject(0).getString("contactphone"));
        assertEquals(String.valueOf(testOrder2.getContactphone()), result.getJSONObject(1).getString("contactphone"));
        assertEquals(String.valueOf(testOrder1.getDestination()), result.getJSONObject(0).getString("destination"));
        assertEquals(String.valueOf(testOrder2.getDestination()), result.getJSONObject(1).getString("destination"));
        assertEquals(String.valueOf(testOrder1.getPostalcode()), result.getJSONObject(0).getString("postalcode"));
        assertEquals(String.valueOf(testOrder2.getPostalcode()), result.getJSONObject(1).getString("postalcode"));
//        assertEquals(String.valueOf(testOrder1.getChileItem().get(0).getBookID()), result.getJSONObject(0).getString("chileItem"));
        // 把order的childItem转换成jsonArray
        JSONArray childJSONArray1 = new JSONArray().fromObject(testOrder1.getChileItem());
        JSONArray childJSONArray2 = new JSONArray().fromObject(testOrder2.getChileItem());

        assertEquals(childJSONArray1.toString(), result.getJSONObject(0).getString("chileItem"));
        assertEquals(childJSONArray2.toString(), result.getJSONObject(1).getString("chileItem"));
    }
//
    @Test
    void getAllOrderItem() {
        // 创建一个随机的订单项
        OrderItem testOrderItem1 = generateRandomOrderItem();
        OrderItem testOrderItem2 = generateRandomOrderItem();

        // 模拟数据
        List<OrderItem> testOrderItemList = new ArrayList<>();
        testOrderItemList.add(testOrderItem1);
        testOrderItemList.add(testOrderItem2);

        // 模拟数据
        Mockito.when(orderItemDao.getAllOrderItem()).thenReturn(testOrderItemList);

        List<OrderItem> result = orderService.getAllOrderItem();

        // 判断返回的数据是否正确
        assertEquals(testOrderItemList.size(), result.size());
        assertEquals(String.valueOf(testOrderItem1.getOrderID()), String.valueOf(result.get(0).getOrderID()));
        assertEquals(String.valueOf(testOrderItem2.getOrderID()), String.valueOf(result.get(1).getOrderID()));
        assertEquals(String.valueOf(testOrderItem1.getBookID()), String.valueOf(result.get(0).getBookID()));
        assertEquals(String.valueOf(testOrderItem2.getBookID()), String.valueOf(result.get(1).getBookID()));
        assertEquals(String.valueOf(testOrderItem1.getBelonguser()), String.valueOf(result.get(0).getBelonguser()));
        assertEquals(String.valueOf(testOrderItem2.getBelonguser()), String.valueOf(result.get(1).getBelonguser()));
        assertEquals(String.valueOf(testOrderItem1.getBookID()), String.valueOf(result.get(0).getBookID()));
        assertEquals(String.valueOf(testOrderItem2.getBookID()), String.valueOf(result.get(1).getBookID()));
    }


    @Test
    void getUserOrderItem() {
        // 创建一个随机的订单项
        OrderItem testOrderItem1 = generateRandomOrderItem();
        OrderItem testOrderItem2 = generateRandomOrderItem();

        // 模拟数据
        List<OrderItem> testOrderItemList = new ArrayList<>();
        testOrderItemList.add(testOrderItem1);
        testOrderItemList.add(testOrderItem2);

        // 模拟数据
        Mockito.when(orderItemDao.getUserOrderItem(Mockito.anyString())).thenReturn(testOrderItemList);

        // 调用方法
        List<OrderItem> result = orderService.getUserOrderItem("testUser");

        // 判断返回的数据是否正确
        assertEquals(testOrderItemList.size(), result.size());
        assertEquals(String.valueOf(testOrderItem1.getOrderID()), String.valueOf(result.get(0).getOrderID()));
        assertEquals(String.valueOf(testOrderItem2.getOrderID()), String.valueOf(result.get(1).getOrderID()));
        assertEquals(String.valueOf(testOrderItem1.getBookID()), String.valueOf(result.get(0).getBookID()));
        assertEquals(String.valueOf(testOrderItem2.getBookID()), String.valueOf(result.get(1).getBookID()));
        assertEquals(String.valueOf(testOrderItem1.getBelonguser()), String.valueOf(result.get(0).getBelonguser()));
        assertEquals(String.valueOf(testOrderItem2.getBelonguser()), String.valueOf(result.get(1).getBelonguser()));
        assertEquals(String.valueOf(testOrderItem1.getBookID()), String.valueOf(result.get(0).getBookID()));
        assertEquals(String.valueOf(testOrderItem2.getBookID()), String.valueOf(result.get(1).getBookID()));
    }
//
//        @Test
//        void getUserOrder() {
//
//        }
//
//    @Test
//    void getUserOrderItemWithBook() {
//    }
    //    @Test
//    void getAllOrderItemWithBook() {
//
//    }
//
}