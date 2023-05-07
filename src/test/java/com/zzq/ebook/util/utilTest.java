package com.zzq.ebook.util;

import com.zzq.ebook.utils.message.Msg;
import com.zzq.ebook.utils.message.MsgCode;
import com.zzq.ebook.utils.message.MsgUtil;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


/**
 * @Author : zzq
 * @Description :测试工具类的Msg相关的
 * */
@ExtendWith(MockitoExtension.class)
public class utilTest {
    // 主要测试utils中的工具类
    @Test
    public void testMsgCode() {
        MsgCode msgCode = MsgCode.SUCCESS;
        Assertions.assertEquals(msgCode.getStatus(), 0);
        Assertions.assertEquals(msgCode.getMsg(), "成功！");

        MsgCode msgCode1 = MsgCode.ERROR;
        Assertions.assertEquals(msgCode1.getStatus(), -1);
        Assertions.assertEquals(msgCode1.getMsg(), "错误！");

    }

    /**
     * @Author : zzq
     * @Description : 测试MsgUtil中的makeMsg方法
     * 主要测试不带数据的Msg返回，以及不带额外信息的Msg返回
     * @Date : 2023/5/7 23:31
     * */
    @Test
    public void testMsg_common(){
        Msg msg = MsgUtil.makeMsg(MsgCode.SUCCESS);
        Assertions.assertEquals(msg.getStatus(), 0);
        Assertions.assertEquals(msg.getMsg(), "成功！");

        Msg msg1 = MsgUtil.makeMsg(MsgCode.ERROR, "寄了");
        Assertions.assertEquals(msg1.getStatus(), -1);
        Assertions.assertEquals(msg1.getMsg(), "寄了");

        Msg msg2 = MsgUtil.makeMsg(MsgCode.SUCCESS, "寄了", null);
        Assertions.assertEquals(msg2.getStatus(), 0);
        Assertions.assertEquals(msg2.getMsg(), "寄了");
    }

    /**
     * @Author : zzq
     * @Description : 测试MsgUtil中的makeMsg方法
     * 主要测试带有数据的Msg返回，以及带有额外信息的Msg返回
     * */
    @Test
    public void testMsg_data(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "zzq");
        jsonObject.put("age", 20);
        Msg msg3 = MsgUtil.makeMsg(MsgCode.SUCCESS, jsonObject);
        Assertions.assertEquals(msg3.getStatus(), 0);
        Assertions.assertEquals(msg3.getMsg(), "成功！");
        Assertions.assertEquals(msg3.getData(), jsonObject);

        Msg msg4 = MsgUtil.makeMsg(500, "寄了", jsonObject);
        Assertions.assertEquals(msg4.getStatus(), 500);
        Assertions.assertEquals(msg4.getMsg(), "寄了");
        Assertions.assertEquals(msg4.getData(), jsonObject);
        msg4.setStatus(200);
        msg4.setMsg("寄了寄了");
        msg4.setData(null);
        Assertions.assertEquals(msg4.getStatus(), 200);
        Assertions.assertEquals(msg4.getMsg(), "寄了寄了");
        Assertions.assertEquals(msg4.getData(), null);

        Msg msg5 = MsgUtil.makeMsg(500, "成天就知道寄");
        Assertions.assertEquals(msg5.getStatus(), 500);
        Assertions.assertEquals(msg5.getMsg(), "成天就知道寄");
    }





}
