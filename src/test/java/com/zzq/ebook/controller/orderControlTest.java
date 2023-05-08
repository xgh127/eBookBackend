package com.zzq.ebook.controller;

import com.zzq.ebook.constant.constant;
import com.zzq.ebook.entity.Order;
import com.zzq.ebook.entity.OrderItem;
import com.zzq.ebook.service.OrderService;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * 测试原则说明：
 * 测试应该是独立的，不依赖于外部因素，因此在测试中最好使用模拟数据来进行测试，以确保测试执行的一致性和可重复性
 * by Xu
 * TODO:分锅，除了已经完成测试的addToChart，queryChart函数，从上至下有7个函数，分为3组，2+2+3，分别由3个人完成，
 * TODO：按照姓氏首字母排序，为hhq，lsk，zzq分别负责2+2+3的函数测试
 * TODO：任务内容包括:设计测试用例并保存在一个csv文件中，然后编写测试代码，画对应函数的DDPath图，然后在共享文档里填上相应的测试用例
 * TODO:UPDATE,画对应函数的数据流图并填写测试用例（用例大概率可以复用DD路径的，主要是填写和画图），分工照旧
 */
@ExtendWith(MockitoExtension.class)
public class orderControlTest {

    @InjectMocks
    private orderControl oc;
    @Mock
    OrderService orderService;

    private List<OrderItem> items = new ArrayList<>(2); //作为一个桩程序的返回值而定义的变量

    @BeforeEach
    void init() {
        for (int i = 0; i < 2; ++i) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemID(i);
            items.add(orderItem);
        }
    }

    /**
     * 测试addToChart函数
     *
     * @param sessionJsonUsername
     * @param username
     * @param bookID
     * @param buyNum
     * @param expect              期望返回的Msg的status
     *                            by Xu
     */
    @ParameterizedTest
    @CsvFileSource(resources = {"/controller-test-data/addToChart-Data.csv"})
    void addToChart(String sessionJsonUsername, String username, Integer bookID, Integer buyNum, Integer expect) {
        Map<String, String> param = new HashMap<>();
        param.put(constant.USERNAME, username);
        param.put(constant.BOOKID, bookID.toString());
        param.put(constant.SINGLE_ITEM_BUYNUM, buyNum.toString());
        JSONObject auth = new JSONObject();
        auth.put(constant.USERNAME, sessionJsonUsername);
        if (!sessionJsonUsername.equals(username)) {
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                mock.when(SessionUtil::getAuth).thenReturn(auth);
                assertEquals(expect, oc.addToChart(param).getStatus());
            }
        } else {
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                mock.when(SessionUtil::getAuth).thenReturn(auth);
                if (buyNum < 2000) {
                    when(orderService.addOneOrderItemToChart(username, bookID, buyNum)).thenReturn(items.get(0));
                    assertEquals(expect, oc.addToChart(param).getStatus());
                } else {
                    when(orderService.addOneOrderItemToChart(username, bookID, buyNum)).thenReturn(null);
                    int status = oc.addToChart(param).getStatus();
                    System.out.println(status);
                    assertEquals(expect, status);
                }
            }
        }

    }

    /**
     * 测试购物车修改功能，expect是期望返回的Msg的状态码
     *
     * @param sessionJsonUsername session的用户名，检查是否是用户自己请求自己的数据
     * @param username            用户名，发起请求的用户的用户名
     * @param bookID              修改购物车中的书籍编号，不能为不存在的书籍
     * @param buyNum              修改后购物车中书籍的数量，不能过大
     * @param expect              期望返回的Msg的状态码，假设orderService能够正常修改user5的购物车信息
     *                            by Li
     */
    @ParameterizedTest
    @CsvFileSource(resources = {"/controller-test-data/refreshShopCartItem-Data.csv"})
    void refreshShopCartItem(String sessionJsonUsername, String username, Integer bookID, Integer buyNum, Integer expect) {
        Map<String, String> param = new HashMap<>();
        param.put(constant.USERNAME, username);
        param.put(constant.BOOKID, bookID.toString());
        param.put(constant.REFRESHED_BUY_NUM, buyNum.toString());
        JSONObject authJson = null;
        if (!sessionJsonUsername.equals("null")) {
            authJson = new JSONObject();
            authJson.put(constant.USERNAME, sessionJsonUsername);
        }
        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
            // getAuth是一个静态方法
            mock.when(SessionUtil::getAuth).thenReturn(authJson);
            if (sessionJsonUsername.equals("null")) {
                assertThrows(Error.class, () -> oc.refreshShopCartItem(param));
                return;
            } else if (sessionJsonUsername.equals(username)) {
                if (buyNum > 10) {
                    when(orderService.editOneOrderItemBUYNUMInChart(username, bookID, buyNum)).thenReturn(-2);
                } else if (bookID < 0) {
                    when(orderService.editOneOrderItemBUYNUMInChart(username, bookID, buyNum)).thenReturn(-1);
                } else {
                    when(orderService.editOneOrderItemBUYNUMInChart(username, bookID, buyNum)).thenReturn(0);
                } //桩程序
            }
            assertEquals(oc.refreshShopCartItem(param).getStatus(), expect);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }


    }

    /**
     * 测试购物车查询功能，expect是返回的OrderItem的数量
     *
     * @param sessionJsonUsername session的用户名，检查是否是用户自己请求自己的数据
     * @param username            用户名，发起请求的用户的用户名
     * @param expect              获取的List<OrderItem>的大小，假设orderService能够正常返回user5的购物车orderItem
     *                            by Xu
     */
    @ParameterizedTest
    @CsvFileSource(resources = {"/controller-test-data/queryChart-Data.csv"})
    void queryChart(String sessionJsonUsername, String username, int expect) {
        Map<String, String> param = new HashMap<>();
        int actualSize = 0;
        param.put("username", username);
        // 模拟SessionUtil.getAuth()的返回值，确保能控制SessionUtil返回给定的值
        if (sessionJsonUsername.equals("null")) {
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                //mock模拟getAuth的返回值
                mock.when(SessionUtil::getAuth).thenReturn(null);
                if (oc.queryChart(param) == null) actualSize = 0;
                assertEquals(expect, actualSize);
            }
        } else {
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                JSONObject authJson = new JSONObject();
                authJson.put("username", sessionJsonUsername);
                mock.when(SessionUtil::getAuth).thenReturn(authJson);
                if (oc.queryChart(param) == null) {//如果session内的用户名和请求用户名不一致，会返回null
                    assertEquals(expect, actualSize);
                } else {//如果一致，会返回用户的items，然后检查获取到的ItemList内orderItem的数量是否与预期一致
                    //注意：这里的Mockito是模拟orderService的返回，保证其能返回指定的值，以便测试controller层的函数是否运行正常
                    when(orderService.findAllOrderItemInCart(eq("user5"))).thenReturn(items);
                    List<OrderItem> itemList = oc.queryChart(param);
                    assertEquals(expect, itemList.size());
                }
            }
        }
    }

    int[] generateOrderBookInfos(int itemNum) {
        int[] bookIDGroup = new int[itemNum];
        for(int i=1; i<=itemNum; i++){
            bookIDGroup[i-1] = (int) (Math.random() * 100);
        }
        return bookIDGroup;
    }

    /**
     * 测试下单功能，expect是期望返回的Msg的状态码
     *
     * @param itemNum        订单项目数量
     * @param orderFrom      订单来源，购物车购买或直接购买
     * @param username       用户名，发起请求的用户的用户名
     * @param receivename    收件人姓名
     * @param postcode       收件人邮件编码
     * @param phonenumber    收件人手机号
     * @param receiveaddress 收件地址
     * @param orderSuccess   修改后购物车中书籍的数量，不能过大
     * @param expect         修改后购物车中书籍的数量，不能过大
     *                       by Li
     */
    @ParameterizedTest
    @CsvFileSource(resources = {"/controller-test-data/orderMakeFromShopCart-Data.csv"})
    void orderMakeFromShopCart(
            Integer itemNum,
            String orderFrom,
            String username,
            String receivename,
            String postcode,
            String phonenumber,
            String receiveaddress,
            boolean orderSuccess,
            int expect
    ) throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("orderFrom", orderFrom);
        param.put(constant.USERNAME, username);
        param.put("receivename", receivename);
        param.put("postcode", postcode);
        param.put("phonenumber", phonenumber);
        param.put("receiveaddress", receiveaddress);
        if (itemNum == 0) {
            assertNull(oc.orderMakeFromShopCart(param));
            return;
        }
        int[] bookIDGroup = generateOrderBookInfos(itemNum);
        int[] bookNumGroup = generateOrderBookInfos(itemNum);
        for (int i = 1; i <= itemNum; ++i) {
            param.put("bookIDGroup" + i, Integer.toString(bookIDGroup[i - 1]));
            param.put("bookNumGroup" + i, Integer.toString(bookNumGroup[i - 1]));
        }
        if (orderFrom.equals("ShopCart")) {
            when(orderService.orderMakeFromShopCart(
                    bookIDGroup,
                    bookNumGroup,
                    username,
                    receivename,
                    postcode,
                    phonenumber,
                    receiveaddress,
                    itemNum
            )).thenReturn(orderSuccess ? 0 : -1);
        }
        else if (orderFrom.equals("DirectBuy")) {
            when(orderService.orderMakeFromDirectBuy(
                    bookIDGroup,
                    bookNumGroup,
                    username,
                    receivename,
                    postcode,
                    phonenumber,
                    receiveaddress,
                    itemNum
            )).thenReturn(orderSuccess ? 0 : -1);
        }
        assertEquals(oc.orderMakeFromShopCart(param).getStatus(), expect);
    }

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

    /**
     * 测试购物车查询功能，expect是返回的OrderItem的数量
     *
     * @param sessionJsonUsername session的用户名，检查是否是用户自己请求自己的数据
     * @param username            用户名，发起请求的用户的用户名
     * @param expect              获取的List<OrderItem>的大小，假设orderService能够正常返回user5的购物车orderItem
     *                            by Zhang
     */
    @ParameterizedTest
    @CsvFileSource(resources = {"/controller-test-data/getAllOrderItem.csv"})
    void getAllOrderItem(String sessionJsonUsername, String username, int expect) {
        // 准备测试数据
        JSONArray testOrderItemList = new JSONArray();
        for (int i = 0; i < 10; ++i) {
            testOrderItemList.add(generateRandomOrderItem());
        }
        // 模拟SessionUtil.getAuth()的返回值，确保能控制SessionUtil返回给定的值
        Map<String, String> param = new HashMap<>();
        int actualSize = 0;
        param.put("username", username);

        if (sessionJsonUsername.equals("null")) {
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                //mock模拟getAuth的返回值
                mock.when(SessionUtil::getAuth).thenReturn(null);
                assertEquals(expect, actualSize);
            }
        } else {
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                JSONObject authJson = new JSONObject();
                authJson.put("username", sessionJsonUsername);
                mock.when(SessionUtil::getAuth).thenReturn(authJson);
                if (oc.queryChart(param) == null) {//如果session内的用户名和请求用户名不一致，会返回null
                    assertEquals(expect, actualSize);
                } else {
                    //如果一致，会返回用户的items，然后检查获取到的ItemList内orderItem的数量是否与预期一致
                    if (sessionJsonUsername == username) {
                        when(orderService.getAllOrderItemWithBook()).thenReturn(testOrderItemList);
                        JSONArray itemList = oc.getAllOrderItem();
                        assertEquals(expect, itemList.size());
                    } else {
                        assertNull(oc.getAllOrderItem());
                    }
                }

            }
        }
    }


    @Test
    void getAllOrderItem_login(){
        // 准备测试数据
        JSONArray testOrderItemList = new JSONArray();
        for (int i = 0; i < 10; ++i) {
            testOrderItemList.add(generateRandomOrderItem());
        }
        // 模拟SessionUtil.getAuth()的返回值，确保能控制SessionUtil返回给定的值
        Map<String, String> param = new HashMap<>();
        int actualSize = 0;
        param.put("username", "user1");
        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
            JSONObject authJson = new JSONObject();
            authJson.put("username", "user1");
            authJson.put(constant.PRIVILEGE, 0);
            mock.when(SessionUtil::getAuth).thenReturn(authJson);
            when(orderService.getAllOrderItemWithBook()).thenReturn(testOrderItemList);
            JSONArray itemList = oc.getAllOrderItem();
            assertEquals(10, itemList.size());
        }

        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
            JSONObject authJson = new JSONObject();
            authJson.put("username", "user1");
            authJson.put(constant.PRIVILEGE, 1);
            mock.when(SessionUtil::getAuth).thenReturn(authJson);
            JSONArray itemList = oc.getAllOrderItem();
            Assertions.assertNull(itemList);
        }
    }

    /**
     * 测试没有用户登录时，获取用户的订单
     */
    @ParameterizedTest
    @CsvFileSource(resources = {"/controller-test-data/getAllOrderItem.csv"})
    void getAllOrder_nologin(String sessionJsonUsername, String username, int expect) {
        // 准备测试数据
        JSONArray testOrderList = new JSONArray();
        for (int i = 0; i < 10; ++i) {
            testOrderList.add(generateRandomOrder());
        }

        // 模拟SessionUtil.getAuth()的返回值，确保能控制SessionUtil返回给定的值
        Map<String, String> param = new HashMap<>();
        int actualSize = 0;
        param.put("username", username);

        if (sessionJsonUsername.equals("null")) {
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                //mock模拟getAuth的返回值
                mock.when(SessionUtil::getAuth).thenReturn(null);
                assertEquals(expect, actualSize);
            }
        } else {
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                JSONObject authJson = new JSONObject();
                authJson.put("username", sessionJsonUsername);
                mock.when(SessionUtil::getAuth).thenReturn(authJson);
                if (oc.queryChart(param) == null) {//如果session内的用户名和请求用户名不一致，会返回null
                    assertEquals(expect, actualSize);
                } else {//如果一致，会返回用户的items，然后检查获取到的ItemList内orderItem的数量是否与预期一致
                    if (sessionJsonUsername == username) {
                        when(orderService.getAllOrder()).thenReturn(testOrderList);
                        JSONArray itemList = oc.getAllOrder();
                        assertEquals(expect, itemList.size());
                    } else {
                        assertNull(oc.getAllOrder());
                    }
                }

            }
        }
    }

    /**
     * 测试有用户登录时，获取用户的订单
     */
    @Test
    void getAllOrder_login(){
        // 准备测试数据
        JSONArray testOrderList = new JSONArray();
        for (int i = 0; i < 10; ++i) {
            testOrderList.add(generateRandomOrder());
        }
        // 模拟SessionUtil.getAuth()的返回值，确保能控制SessionUtil返回给定的值
        Map<String, String> param = new HashMap<>();
        param.put("username", "user1");
        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
            JSONObject authJson = new JSONObject();
            authJson.put("username", "user1");
            authJson.put(constant.PRIVILEGE, 0);
            mock.when(SessionUtil::getAuth).thenReturn(authJson);
            when(orderService.getAllOrder()).thenReturn(testOrderList);
            JSONArray itemList = oc.getAllOrder();
            assertEquals(10, itemList.size());
        }

        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
            JSONObject authJson = new JSONObject();
            authJson.put("username", "user2");
            authJson.put(constant.PRIVILEGE, 3);
            mock.when(SessionUtil::getAuth).thenReturn(authJson);
            JSONArray result = oc.getAllOrder();
            assertNull(result);
        }

    }


    /**
     * 测试没有用户登录时，获取用户的订单
     */
    @ParameterizedTest
    @CsvFileSource(resources = {"/controller-test-data/getAllOrderItem.csv"})
    void getUserOrderItem(String sessionJsonUsername, String username, int expect) {
        // 准备测试数据
        JSONArray testOrderItemList = new JSONArray();
        for (int i = 0; i < 10; ++i) {
            testOrderItemList.add(generateRandomOrderItem());
        }
        // 模拟SessionUtil.getAuth()的返回值，确保能控制SessionUtil返回给定的值
        Map<String, String> param = new HashMap<>();
        int actualSize = 0;
        param.put("username", username);

        if (sessionJsonUsername.equals("null")) {
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                //mock模拟getAuth的返回值
                mock.when(SessionUtil::getAuth).thenReturn(null);
                assertEquals(expect, actualSize);
            }
        } else {
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                JSONObject authJson = new JSONObject();
                authJson.put("username", sessionJsonUsername);
                mock.when(SessionUtil::getAuth).thenReturn(authJson);
                if (oc.queryChart(param) == null) {//如果session内的用户名和请求用户名不一致，会返回null
                    assertEquals(expect, actualSize);
                } else {//如果一致，会返回用户的items，然后检查获取到的ItemList内orderItem的数量是否与预期一致
                    if (sessionJsonUsername == username) {
                        when(orderService.getAllOrderItemWithBook()).thenReturn(testOrderItemList);
                        JSONArray itemList = oc.getUserOrderItem();
                        assertEquals(expect, itemList.size());
                    } else {
                        assertNull(oc.getUserOrderItem());
                    }
                }

            }
        }
    }

    /**
     * 测试有用户登录时，获取用户的订单
     */
    @Test
    void getUserOrderItem_login(){
        // 准备测试数据
        JSONArray testOrderList = new JSONArray();
//        List<OrderItem> testOrderList = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            testOrderList.add(generateRandomOrderItem());
        }

        // 模拟SessionUtil.getAuth()的返回值，确保能控制SessionUtil返回给定的值
        Map<String, String> param = new HashMap<>();
        param.put("username", "user1");
        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
            JSONObject authJson = new JSONObject();
            authJson.put("username", "user1");
            authJson.put(constant.PRIVILEGE, 0);
            mock.when(SessionUtil::getAuth).thenReturn(authJson);
            when(orderService.getUserOrderItemWithBook("user1")).thenReturn(testOrderList);
            JSONArray itemList = oc.getUserOrderItem();
            assertEquals(10, itemList.size());
        }
    }


    @ParameterizedTest
    @CsvFileSource(resources = {"/controller-test-data/getAllOrderItem.csv"})
    void getUserOrder(String sessionJsonUsername, String username, int expect) {
        // 准备测试数据
        JSONArray testOrderList = new JSONArray();
        for (int i = 0; i < 10; ++i) {
            testOrderList.add(generateRandomOrder());
        }

        // 模拟SessionUtil.getAuth()的返回值，确保能控制SessionUtil返回给定的值
        Map<String, String> param = new HashMap<>();
        int actualSize = 0;
        param.put("username", username);

        if (sessionJsonUsername.equals("null")) {
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                //mock模拟getAuth的返回值
                mock.when(SessionUtil::getAuth).thenReturn(null);
                assertEquals(expect, actualSize);
            }
        } else {
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                JSONObject authJson = new JSONObject();
                authJson.put("username", sessionJsonUsername);
                mock.when(SessionUtil::getAuth).thenReturn(authJson);
                if (oc.queryChart(param) == null) {//如果session内的用户名和请求用户名不一致，会返回null
                    assertEquals(expect, actualSize);
                } else {//如果一致，会返回用户的items，然后检查获取到的ItemList内orderItem的数量是否与预期一致
                    if (sessionJsonUsername == username) {
                        when(orderService.getUserOrder(username)).thenReturn(testOrderList);
                        JSONArray itemList = oc.getUserOrder();
                        assertEquals(expect, itemList.size());
                    } else {
                        assertNull(oc.getUserOrder());
                    }
                }

            }
        }
    }

    @Test
    void testFunction() {
    }
}