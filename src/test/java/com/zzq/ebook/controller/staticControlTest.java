package com.zzq.ebook.controller;


import com.zzq.ebook.constant.constant;
import com.zzq.ebook.service.BookService;
import com.zzq.ebook.service.StatisticService;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class staticControlTest {
    @InjectMocks
    private statisticControl sc;

    @Mock
    private StatisticService statisticService;


    @Test
    public void testuserConsumeData() throws ParseException {
        // JSONArray
        Map<String, String> params = new HashMap<>();
        // 直接调用
        JSONArray result1 = sc.userConsumeData(params);
        // result1应该是NUll
        assert(result1 == null);

        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)){
            JSONObject auth = new JSONObject();
            auth.put(constant.USERNAME, "sessionJsonUsername");
            auth.put(constant.PRIVILEGE, 1);
            mock.when(SessionUtil::getAuth).thenReturn(auth);
            // 调用
            JSONArray result2 = sc.userConsumeData(params);
            assert(result2 == null);

            JSONArray data = new JSONArray();
            // change params
            auth.put(constant.PRIVILEGE, 0);
            when(statisticService.userConsumeStatistic(any(),any())).thenReturn(data);

            // 调用
            JSONArray result3 = sc.userConsumeData(params);
            assert(result3 != null);

            params.put("startDate", "2020-01-01 00:00:00");
            params.put("endDate", "2025-01-01 00:00:00");

            JSONArray result4 = sc.userConsumeData(params);
            assert(result4 != null);
        }
    }



    @Test
    public void testbookSellnum() throws ParseException {
        // JSONArray
        Map<String, String> params = new HashMap<>();
        // 直接调用
        JSONArray result1 = sc.bookSellnum(params);
        // result1应该是NUll
        assert(result1 == null);

        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)){
            JSONObject auth = new JSONObject();
            auth.put(constant.USERNAME, "sessionJsonUsername");
            auth.put(constant.PRIVILEGE, 1);
            mock.when(SessionUtil::getAuth).thenReturn(auth);
            // 调用
            JSONArray result2 = sc.bookSellnum(params);
            assert(result2 == null);


            JSONArray data = new JSONArray();
            // change params
            auth.put(constant.PRIVILEGE, 0);
            when(statisticService.bookSellnumStatistic(any(),any())).thenReturn(data);

            // 调用
            JSONArray result3 = sc.bookSellnum(params);
            assert(result3 != null);

            params.put("startDate", "2020-01-01 00:00:00");
            params.put("endDate", "2025-01-01 00:00:00");

            JSONArray result4 = sc.bookSellnum(params);

            assert(result4 != null);
        }
    }


    @Test
    void testuserBookTotalPay() throws ParseException {
        // JSONArray
        Map<String, String> params = new HashMap<>();
        // 直接调用
        JSONArray result1 = sc.userBookTotalPay(params);
        // result1应该是NUll
        assert(result1 == null);


        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)){
            JSONObject auth = new JSONObject();
            auth.put(constant.USERNAME, "sessionJsonUsername");
            auth.put(constant.PRIVILEGE, 1);
            mock.when(SessionUtil::getAuth).thenReturn(auth);
            // 调用
            JSONArray result2 = sc.userBookTotalPay(params);
            assert(result2 == null);

            JSONArray data = new JSONArray();
            // change params
            auth.put(constant.PRIVILEGE, 0);
            when(statisticService.userSelfStatistic_BookTotalPay(any(),any(),any())).thenReturn(data);

            // 调用
            JSONArray result3 = sc.userBookTotalPay(params);
            assert(result3 != null);

            params.put("startDate", "2020-01-01 00:00:00");
            params.put("endDate", "2025-01-01 00:00:00");

            JSONArray result4 = sc.userBookTotalPay(params);
            assert(result4 != null);

        }
    }


    // userBookAllBuyNum
    @Test
    void testuserBookAllBuyNum() throws ParseException {
        // JSONArray
        Map<String, String> params = new HashMap<>();
        // 直接调用
        JSONArray result1 = sc.userBookAllBuyNum(params);
        // result1应该是NUll
        assert(result1 == null);


        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)){
            JSONObject auth = new JSONObject();
            auth.put(constant.USERNAME, "sessionJsonUsername");
            auth.put(constant.PRIVILEGE, 1);
            mock.when(SessionUtil::getAuth).thenReturn(auth);
            // 调用
            JSONArray result2 = sc.userBookAllBuyNum(params);
            assert(result2 == null);

            JSONArray data = new JSONArray();
            // change params
            auth.put(constant.PRIVILEGE, 0);
            when(statisticService.userSelfStatistic_BookAllBuyNum(any(),any(),any())).thenReturn(data);

            // 调用
            JSONArray result3 = sc.userBookAllBuyNum(params);
            assert(result3 != null);

            params.put("startDate", "2020-01-01 00:00:00");
            params.put("endDate", "2025-01-01 00:00:00");

            JSONArray result4 = sc.userBookAllBuyNum(params);
            assert(result4 != null);

        }

    }

    // userbookWithBuyNum
    @Test
    void testuserbookWithBuyNum() throws ParseException {
        // JSONArray
        Map<String, String> params = new HashMap<>();
        // 直接调用
        JSONArray result1 = sc.userbookWithBuyNum(params);
        // result1应该是NUll
        assert(result1 == null);

        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)){
            JSONObject auth = new JSONObject();
            auth.put(constant.USERNAME, "sessionJsonUsername");
            auth.put(constant.PRIVILEGE, 1);
            mock.when(SessionUtil::getAuth).thenReturn(auth);
            // 调用
            JSONArray result2 = sc.userbookWithBuyNum(params);
            assert(result2 == null);

            JSONArray data = new JSONArray();
            // change params
            auth.put(constant.PRIVILEGE, 0);
            when(statisticService.userSelfStatistic_BookWithBuyNum(any(),any(),any())).thenReturn(data);

            // 调用
            JSONArray result3 = sc.userbookWithBuyNum(params);
            assert(result3 != null);

            params.put("startDate", "2020-01-01 00:00:00");
            params.put("endDate", "2025-01-01 00:00:00");

            JSONArray result4 = sc.userbookWithBuyNum(params);
            assert(result4 != null);
        }
    }

}
