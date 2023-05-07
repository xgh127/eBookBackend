package com.zzq.ebook.entity;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


/**
 * @Author : zzq
 * @Description :
 *  因为User的这个类的覆盖用到的代码其实很少，所以我们在针对entity的测试中，额外增加了测试，保证覆盖率的完整性
 *  但是在实际的开发中，我们不需要这么做，因为我们的测试是针对业务逻辑的，而不是针对实体类的
 * */
@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Test
    public void testGetUsername() {
        User user = new User();
        user.setUsername("username");
        Assertions.assertEquals("username", user.getUsername());
    }

    @Test
    public void testGetPassword() {
        User user = new User();
        user.setPassword("password");
        Assertions.assertEquals("password", user.getPassword());
    }

    @Test
    public void testGetEmail() {
        User user = new User();
        user.setEmail("email");
        Assertions.assertEquals("email", user.getEmail());
    }

    @Test
    public void testGetPhone() {
        User user = new User();
        user.setTelephone("phone");
        Assertions.assertEquals("phone", user.getTelephone());
    }


    @Test
    public void testGetAddress() {
        User user = new User();
        user.setUseraddress("address");
        Assertions.assertEquals("address", user.getUseraddress());

    }

    @Test
    public void testGetPrivilege() {
        User user = new User();
        user.setPrivilege(1);
        Assertions.assertEquals(1, user.getPrivilege());
    }

    @Test
    public void testGetForbidlogin() {
        User user = new User();
        user.setForbidlogin(1);
        Assertions.assertEquals(1, user.getForbidlogin());
    }

    @Test
    public void testGetName() {
        User user = new User();
        user.setName("name");
        Assertions.assertEquals("name", user.getName());
    }

    @Test
    public void testSetUsername() {
        User user = new User();
        user.setUsername("username");
        Assertions.assertEquals("username", user.getUsername());
    }

    @Test
    public void testSetPassword() {
        User user = new User();
        user.setPassword("password");
        Assertions.assertEquals("password", user.getPassword());
    }

    @Test
    public void testSetEmail() {
        User user = new User();
        user.setEmail("email");
        Assertions.assertEquals("email", user.getEmail());
    }

    @Test
    public void testSetPhone() {
        User user = new User();
        user.setTelephone("phone");
        Assertions.assertEquals("phone", user.getTelephone());
    }


    @Test
    public void testGetChildOrderItem() {
        User user = new User();
        user.setChildOrderItem(null);
        Assertions.assertEquals(null, user.getChildOrderItem());
    }




}
