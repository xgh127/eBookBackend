package com.zzq.ebook.dao;


import com.zzq.ebook.daoImp.OrderItemImp;
import com.zzq.ebook.daoImp.UserDaoImp;
import com.zzq.ebook.entity.User;
import com.zzq.ebook.repository.OrderItemRepository;
import com.zzq.ebook.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


/**
 * @Author : zzq
 * @Description :测试UserDaoImp的相关的方法
 * */
@ExtendWith(MockitoExtension.class)
public class userDaoTest {
    @InjectMocks
    private UserDaoImp userDaoImp;

    @Mock
    private UserRepository userRepository;


    @Test
    public void testcheckUser(){
        User randUser = new User();
        // mock userRepository.checkUser(username,password);
        when(userRepository.checkUser("zzq","123")).thenReturn(randUser);

        User result = userDaoImp.checkUser("zzq","123");

        assert(result == randUser);

    }


    @Test
    public void testgetUserByusername(){
        User randUser = new User();
        // mock userRepository.getOne(username);
        when(userRepository.getOne("zzq")).thenReturn(randUser);

        User result = userDaoImp.getUserByusername("zzq");

        assert(result == randUser);

    }


    @Test
    public void getAllUser(){
        List<User > randUserList = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            User randUser = new User();
            randUser.setUsername("123");
            randUserList.add(randUser);
        }

        // mock userRepository.findAll();
        when(userRepository.findAll()).thenReturn(randUserList);

        List<User> result = userDaoImp.getAllUser();

        assert(result == randUserList);

    }


    @Test
    public void saveOneUser(){
        User randUser = new User();
        // mock userRepository.save(oneUser);
        when(userRepository.save(randUser)).thenReturn(randUser);

        User result = userDaoImp.saveOneUser(randUser);

        assert(result == randUser);
    }
}
