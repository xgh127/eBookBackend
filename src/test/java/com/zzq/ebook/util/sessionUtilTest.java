package com.zzq.ebook.util;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @Author : zzq
 * @Description :
 * */
@ExtendWith(MockitoExtension.class)
public class sessionUtilTest {

    @Test
    public void testSetSession(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "zzq");
        SessionUtil.setSession(jsonObject);
        JSONObject jsonObjectGet = SessionUtil.getAuth();
        if (jsonObjectGet != null) {
            Assertions.assertEquals(jsonObjectGet.get("username"), "zzq");
        }
    }

    /**
     * @Author : zzq
     * @Description : 测试removeSession方法
     * @param : []
     * @reminder 特别注意的是在测试removeSession方法时，需要在每次测试前都要初始化一下RequestContextHolder.setRequestAttributes(requestAttributes);
     * 因为要模拟HTTP请求，所以需要模拟HttpServletRequest对象和HttpSession对象，这两个对象都是在RequestContextHolder.getRequestAttributes()方法中获取的。
     * */
    @Test
    public void testRemoveSession(){
        // 创建虚拟的ServletRequestAttributes对象
        ServletRequestAttributes requestAttributes = mock(ServletRequestAttributes.class);
        // 设置RequestContextHolder的RequestAttributes属性
        RequestContextHolder.setRequestAttributes(requestAttributes);

        // 模拟HttpServletRequest和HttpSession对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        // 当ServletRequestAttributes的getRequest()方法被调用时，返回模拟的HttpServletRequest对象
        when(requestAttributes.getRequest()).thenReturn(request);
        // 当HttpServletRequest的getSession()方法被调用时，返回模拟的HttpSession对象
        when(request.getSession()).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "zzq");
        SessionUtil.setSession(jsonObject);
        SessionUtil.removeSession();
        JSONObject jsonObjectGet = SessionUtil.getAuth();
        // json
        Assertions.assertEquals(jsonObjectGet.isEmpty(), true);
    }


    /**
     * @Author : zzq
     * @Description : 测试getAuth方法
     * @param : []
     *
     * @reminder 特别注意的是在测试getAuth方法时，需要在每次测试前都要初始化一下RequestContextHolder.setRequestAttributes(requestAttributes);*/
    @Test
    public void testGetAuth(){
        // 创建虚拟的ServletRequestAttributes对象
        ServletRequestAttributes requestAttributes = mock(ServletRequestAttributes.class);
        // 设置RequestContextHolder的RequestAttributes属性
        RequestContextHolder.setRequestAttributes(requestAttributes);

        // 模拟HttpServletRequest和HttpSession对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        // 当ServletRequestAttributes的getRequest()方法被调用时，返回模拟的HttpServletRequest对象
        when(requestAttributes.getRequest()).thenReturn(request);
        // 当HttpServletRequest的getSession(false)方法被调用时，返回模拟的HttpSession对象
        when(request.getSession()).thenReturn(session);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "zzq");
        SessionUtil.setSession(jsonObject);
        JSONObject jsonObjectGet = SessionUtil.getAuth();
        if (jsonObjectGet != null) {
            Assertions.assertEquals(jsonObjectGet.get("username"), "zzq");
        }

    }


}
