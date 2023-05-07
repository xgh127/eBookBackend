package com.zzq.ebook.controller;


import com.zzq.ebook.constant.constant;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.service.BookService;
import com.zzq.ebook.utils.message.Msg;
import com.zzq.ebook.utils.message.MsgCode;
import com.zzq.ebook.utils.message.MsgUtil;
import com.zzq.ebook.utils.session.SessionUtil;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


/**
 * @Author : zzq
 * @Description :测试BookController中的方法
 * */
@ExtendWith(MockitoExtension.class)
public class bookControllerTest {

    @InjectMocks
    private bookControl bc;

    @Mock
    private BookService bookService;

    String generateRandString(int length){
        // 生成一个随机字符串
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randNum = (int)(Math.random() * str.length());
            sb.append(str.charAt(randNum));
        }
        return sb.toString();
    }

    Book generateRandBook(int bookID){
        Book book = new Book();
        book.setID(bookID);
        book.setAuthor("author" + bookID + generateRandString(5));
        book.setBookname("bookname" + bookID + generateRandString(5));
        book.setDeparture("departure" + bookID + generateRandString(5));
        book.setDescription("description" + bookID + generateRandString(5));
        book.setDisplaytitle("displaytitle" + bookID + generateRandString(5));
        book.setImgtitle("imgtitle" + bookID + generateRandString(5));
        book.setInventory(bookID);
        book.setISBN("ISBN" + bookID + generateRandString(5));
        book.setPrice(bookID);
        book.setPublisher("publisher" + bookID + generateRandString(5));
        book.setSellnumber(bookID);

        return  book;
    }

    @Test
    public void testGetBook(){
        Book book = generateRandBook(1);
        Mockito.when(bookService.getBookByID(1)).thenReturn(book);

        for (int i = -1; i <= 1; i++) {
            Book getResult = bc.getBook(i);

            if( i <= 0){
                Assertions.assertEquals(null, getResult);

            }
            else{
                Assertions.assertEquals(book, getResult);
            }
        }
    }

    @Test
    public void testGetBookList(){
        // 创建一个空的bookList
        List<Book> bookList = new ArrayList<Book>();

        for (int i = 0; i < 10; i++) {
            Book book = generateRandBook(i);
            bookList.add(book);
        }
        Mockito.when(bookService.getRecommendBooks()).thenReturn(bookList);

        List<Book> getResult = bc.getMainPageBook();

        // 检查返回的bookList是否和预期相同
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(bookList.get(i), getResult.get(i));
        }
    }



    @Test
    public void testGetAllBook(){
        List<Book> bookList = new ArrayList<Book>();

        for (int i = 0; i <= 10; i++) {
            Book book = generateRandBook(i);
            bookList.add(book);
        }
        Mockito.when(bookService.getBookAll()).thenReturn(bookList);

        Map<String, String> params = new HashMap<>();
        List<Book> getResult = bc.getAllBook(params);


        // 检查返回的bookList是否和预期相同
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(bookList.get(i), getResult.get(i));
        }
    }


    /**
     * 测试getSearchedBooks 首页book推荐
     * @Author: zhang ziqian
     * */
    @Test
    public void testgetSearchedBooks(){
        // 用一个for循环来测试不同的查询参数条件下的返回结果
        for (int i = 0; i < 4; i++) {
            List<Book> bookList = new ArrayList<Book>();
            for (int j = 0; j < 10; j++) {
                Book book = generateRandBook(j);
                bookList.add(book);
            }

            Mockito.when(bookService.getSearchedBooks(i,"key")).thenReturn(bookList);

            List<Book> result = bc.searchBooks(i, "key");

            // 检查返回的bookList是否和预期相同
            for (int j = 0; j < 10; j++) {
                Assertions.assertEquals(bookList.get(j), result.get(j));
            }

        }
    }


    /**
     * 测试RequestSignature 用来给图片上传请求签名
     * @Author: zhang ziqian
     * */
    @Test
    public void testRequestSignature(){
        Map<String, String> params = new HashMap<>();
        params.put(constant.POLICYDATA, "data");
        // 检查未登录时的返回结果
        Msg result2 = bc.requestSignature(params);
        Assertions.assertNull(result2);

        // 检查登录后的返回结果
        JSONObject mockAuth = new JSONObject();
        mockAuth.put(constant.PRIVILEGE, 0);
        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
            mock.when(SessionUtil::getAuth).thenReturn(mockAuth);
            Mockito.when(bookService.getUploadSignature(
                    "data","[阿里云accessKey!]", "HmacSHA1"
                    )
            ).thenReturn(
                    MsgUtil.makeMsg(MsgCode.SUCCESS)
            );

            Msg result1 = bc.requestSignature(params);
            assertEquals("成功！", result1.getMsg());
        }

    }


    /**
     * 测试添加图书
     * @Author: zhang ziqian
     * */
    @Test
    public void testAddBook(){
        Map<String, String> params = new HashMap<>();
        JSONObject obj = new JSONObject();

        Msg result1 = bc.addBook(params);
        Assertions.assertNull(result1);

        // 检查登录后的返回结果
        JSONObject mockAuth = new JSONObject();
        mockAuth.put(constant.PRIVILEGE, 0);
        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
            mock.when(SessionUtil::getAuth).thenReturn(mockAuth);
            Book result = generateRandBook(1);
            Mockito.when(bookService.addOneBook(obj)).thenReturn(result);

            // 检查返回结果
            Msg result2 = bc.addBook(obj);
            Assertions.assertEquals(0,result2.getStatus());
        }

        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
            mock.when(SessionUtil::getAuth).thenReturn(mockAuth);
            Book result = generateRandBook(1);
            Mockito.when(bookService.addOneBook(obj)).thenReturn(null);
            // 检查返回结果
            Msg result3 = bc.addBook(obj);
            Assertions.assertEquals( -1,result3.getStatus());
        }


    }

    /**
     * 测试编辑图书
     * @Author: zhang ziqian
     * */
    @Test
    public void testEditBook(){
        Map<String, String> params = new HashMap<>();
        JSONObject obj = new JSONObject();

        Msg result1 = bc.editBook(params);
        Assertions.assertNull(result1);

        // 检查登录后的返回结果
        JSONObject mockAuth = new JSONObject();
        mockAuth.put(constant.PRIVILEGE, 0);
        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
            mock.when(SessionUtil::getAuth).thenReturn(mockAuth);
            Book result = generateRandBook(1);
            Mockito.when(bookService.editOneBook(obj)).thenReturn(result);

            // 检查返回结果
            Msg result2 = bc.editBook(obj);
            Assertions.assertEquals(0, result2.getStatus());
        }

        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
            mock.when(SessionUtil::getAuth).thenReturn(mockAuth);
            Book result = generateRandBook(1);
            Mockito.when(bookService.editOneBook(obj)).thenReturn(null);
            // 检查返回结果
            Msg result3 = bc.editBook(obj);
            Assertions.assertEquals(-1,result3.getStatus());
        }

    }


    /**
     * 测试删除图书
     * @Author: zhang ziqian
     * */
    @Test
    public void testDeleteBook(){
        Map<String, String> params = new HashMap<>();
        JSONObject obj = new JSONObject();

        Msg result1 = bc.deleteBook(params);
        Assertions.assertEquals(-1, result1.getStatus());

        // 检查登录后的返回结果
        JSONObject mockAuth = new JSONObject();
        mockAuth.put(constant.PRIVILEGE, 0);
        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
            // params.get(constant.BOOKID);
            obj.put(constant.BOOKID, "1");   // 传入一个bookid
            mock.when(SessionUtil::getAuth).thenReturn(mockAuth);
            Mockito.when(bookService.deleteOneBook(1)).thenReturn(0);
            // 检查返回结果
            Msg result2 = bc.deleteBook(obj);
            Assertions.assertEquals(0, result2.getStatus());
        }

        try (MockedStatic<SessionUtil> mock = Mockito.mockStatic(SessionUtil.class)) {
            // params.get(constant.BOOKID);
            obj.put(constant.BOOKID, "1");   // 传入一个bookid
            mock.when(SessionUtil::getAuth).thenReturn(mockAuth);
            Mockito.when(bookService.deleteOneBook(1)).thenReturn(1);
            // 检查返回结果
            Msg result3 = bc.deleteBook(obj);
            Assertions.assertEquals(-1, result3.getStatus());
        }


    }
}
