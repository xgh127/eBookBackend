package com.zzq.ebook.controller;


import com.zzq.ebook.constant.constant;
import com.zzq.ebook.service.OrderService;
import com.zzq.ebook.service.UserService;
import com.zzq.ebook.utils.message.Msg;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class userControlTest {

    @InjectMocks
    private userControl uc;
    @Mock
    UserService userService;

    @Test
    public void testCheckUserExit(){
        Map<String, String> params = new HashMap<>();
        params.put("username", "zzq");

        // Mock userService.checkUserIfExist(username)
         when(userService.checkUserIfExist("zzq")).thenReturn(false);

         // 测试
        Msg result = uc.checkUserExit(params);
        Assertions.assertEquals(0, result.getStatus());


        when(userService.checkUserIfExist("exituser")).thenReturn(true);
        params.put("username", "exituser");
        result = uc.checkUserExit(params);
        Assertions.assertEquals(-1, result.getStatus());
    }

    @Test
    public void testRegister() {
        Map<String, String> params = new HashMap<>();
        params.put(constant.EMAIL, "123@123.com");
        params.put(constant.LOCATION, "123");
        params.put(constant.PASSWORD, "");
        params.put(constant.PHONE, "123");
        params.put(constant.USERNAME, "123");
        params.put(constant.CONFIRM, "123");
        params.put(constant.AGREEMENT, "true");

        for (int i = -4; i <=0; i++) {
            // mock userService.registerUser(email,location,password,phone,username,confirm,agreement);
            when(userService.registerUser(
                    params.get(constant.EMAIL),
                    params.get(constant.LOCATION),
                    params.get(constant.PASSWORD),
                    params.get(constant.PHONE),
                    params.get(constant.USERNAME),
                    params.get(constant.CONFIRM),
                    params.get(constant.AGREEMENT)
            )).thenReturn(i);

            // 测试
            Msg result = uc.register(params);
//            Assertions.assertEquals(i, result.getStatus());

            if (i == 0) {
                Assertions.assertEquals(0, result.getStatus());
            } else if (i == -4){
                Assertions.assertNull(result);
            } else{
                Assertions.assertEquals(-1, result.getStatus());
            }

        }
    }


    @Test
    public void testqueryAllUserInfo(){

    }




}
