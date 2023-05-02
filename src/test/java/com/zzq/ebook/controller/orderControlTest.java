package com.zzq.ebook.controller;

import com.zzq.ebook.entity.OrderItem;
import com.zzq.ebook.service.OrderService;
import com.zzq.ebook.utils.message.MsgUtil;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//引入MsgCode
import com.zzq.ebook.utils.message.MsgCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import com.zzq.ebook.constant.constant;
/**
 * 测试原则说明：
 * 测试应该是独立的，不依赖于外部因素，因此在测试中最好使用模拟数据来进行测试，以确保测试执行的一致性和可重复性
 * by Xu
 * TODO:分锅，除了已经完成测试的addToChart，queryChart函数，从上至下有7个函数，分为3组，2+2+3，分别由3个人完成，
 * TODO：按照姓氏首字母排序，为hhq，lsk，zzq分别负责2+2+3的函数测试
 * TODO：任务内容包括:设计测试用例并保存在一个csv文件中，然后编写测试代码，画对应函数的DDPath图，然后在共享文档里填上相应的测试用例
 */
@ExtendWith(MockitoExtension.class)
public class orderControlTest {

    @InjectMocks
    private orderControl oc;
    @Mock
    OrderService orderService;

    private List<OrderItem> items = new ArrayList<>(2); //作为一个桩程序的返回值而定义的变量
    @BeforeEach
    void  init(){
        for(int  i = 0; i < 2; ++i) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemID(i);
            items.add(orderItem);
        }

    }

    /**
     * 测试addToChart函数
     * @param sessionJsonUsername
     * @param username
     * @param bookID
     * @param buyNum
     * @param expect 期望返回的Msg的status
     */
    @ParameterizedTest
    @CsvFileSource(resources = {"/controller-test-data/addToChart-Data.csv"})
    void addToChart(String sessionJsonUsername, String username,Integer bookID,Integer buyNum,Integer expect) {
        Map<String,String> param = new HashMap<>();
        param.put(constant.USERNAME,username);
        param.put(constant.BOOKID,bookID.toString());
        param.put(constant.SINGLE_ITEM_BUYNUM,buyNum.toString());
        JSONObject auth = new JSONObject();
        auth.put(constant.USERNAME,sessionJsonUsername);
        if(!sessionJsonUsername.equals(username)){
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                mock.when(SessionUtil::getAuth).thenReturn(auth);
                assertEquals(expect, oc.addToChart(param).getStatus());
            }
        }else{
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                mock.when(SessionUtil::getAuth).thenReturn(auth);
                if (buyNum < 2000){
                    Mockito.when(orderService.addOneOrderItemToChart(username,bookID,buyNum)).thenReturn(items.get(0));
                    assertEquals(expect, oc.addToChart(param).getStatus());
                }else{
                    Mockito.when(orderService.addOneOrderItemToChart(username,bookID,buyNum)).thenReturn(null);
                    int status =  oc.addToChart(param).getStatus();
                    System.out.println(status);
                    assertEquals(expect, status);
                }
            }
        }

    }

    @Test
    void refreshShopCartItem() {
    }

    /**
     * 测试购物车查询功能，expect是返回的OrderItem的数量
     * @param sessionJsonUsername session的用户名，检查是否是用户自己请求自己的数据
     * @param username  用户名，发起请求的用户的用户名
     * @param expect   获取的List<OrderItem>的大小，假设orderService能够正常返回user5的购物车orderItem
     * by Xu
     */
    @ParameterizedTest
    @CsvFileSource(resources = {"/controller-test-data/queryChart-Data.csv"})
    void queryChart(String sessionJsonUsername, String username,int expect) {
        Map<String,String> param = new HashMap<>();
        int actualSize = 0;
        param.put("username",username);
        // 模拟SessionUtil.getAuth()的返回值，确保能控制SessionUtil返回给定的值
        if (sessionJsonUsername.equals("null")) {
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                //mock模拟getAuth的返回值
                mock.when(SessionUtil::getAuth).thenReturn(null);
                if (oc.queryChart(param) == null)  actualSize = 0;
                assertEquals(expect, actualSize);
            }
        } else {
            try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
                JSONObject authJson = new JSONObject();
                authJson.put("username", sessionJsonUsername);
                mock.when(SessionUtil::getAuth).thenReturn(authJson);
                if (oc.queryChart(param) == null){//如果session内的用户名和请求用户名不一致，会返回null
                    assertEquals(expect,actualSize);
                }else{//如果一致，会返回用户的items，然后检查获取到的ItemList内orderItem的数量是否与预期一致
                    //注意：这里的Mockito是模拟orderService的返回，保证其能返回指定的值，以便测试controller层的函数是否运行正常
                    Mockito.when(orderService.findAllOrderItemInCart(eq("user5"))).thenReturn(items);
                    List<OrderItem> itemList =oc.queryChart(param);
                    assertEquals(expect, itemList.size());
                }
            }
        }
    }

    @Test
    void orderMakeFromShopCart() {
    }

    @Test
    void getAllOrderItem() {
    }

    @Test
    void getAllOrder() {
    }

    @Test
    void getUserOrderItem() {
    }

    @Test
    void getUserOrder() {
    }

    @Test
    void testFunction() {
    }
}