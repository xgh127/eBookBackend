package com.zzq.ebook.dao;


import com.zzq.ebook.daoImp.BookDaoImp;
import com.zzq.ebook.entity.Book;
import com.zzq.ebook.repository.BookRepository;
import com.zzq.ebook.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class bookDaoTest {
    @InjectMocks
    private BookDaoImp bookDao;


    @Mock
    private BookRepository bookRepository;

    String generateRandString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb=new StringBuilder(length);
        for(int i=0;i<length;i++)
            sb.append(str.charAt((int)(Math.random()*62)));
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
    public void testGetOneBookByID() {
        Book randBook = generateRandBook(1);
        Mockito.when(bookRepository.getOne(anyInt())).thenReturn(randBook);
        // 测试 bookDao.getOneBookByID(id); 是否能够返回一个 Book 对象
        Book book = bookDao.getOneBookByID(1);
        Assertions.assertEquals(randBook, book);
    }

    @Test
    public void testGetAllBooks(){
        List<Book> allData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book randBook = generateRandBook(i);
            allData.add(randBook);
        }

        when(bookRepository.findAll()).thenReturn(allData);

        // 测试 bookDao.getAllBooks(); 是否能够返回一个 List<Book> 对象
        List<Book> books = bookDao.getAllBooks();
    }


    @Test
    public void testgetTopSellBooks(){
        List<Book> allData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book randBook = generateRandBook(i);
            allData.add(randBook);
        }

         allData.sort((a, b) -> b.getSellnumber() - a.getSellnumber());

        // mock bookRepository.findAllByOrderBySellnumberDesc(PageRequest.of(0, limit))
        when(bookRepository.findAllByOrderBySellnumberDesc(any())).thenReturn(allData);

        // 测试 bookDao.getTopSellBooks(); 是否能够返回一个 List<Book> 对象
        List<Book> books = bookDao.getTopSellBooks(10);

        // 测试返回的 List<Book> 是否是按照 sellnumber 降序排列的
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(books.get(i).getSellnumber(), 9 - i);
        }

    }

    @Test
    public void testfindBooksByAuthorLike(){
        List<Book> allData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book randBook = generateRandBook(i);
            allData.add(randBook);
        }

        // mock bookRepository.findAllByAuthorLike("%" + author + "%")
        when(bookRepository.findBooksByAuthorLike(any())).thenReturn(allData);

        // 测试 bookDao.findBooksByAuthorLike(author); 是否能够返回一个 List<Book> 对象
        List<Book> books = bookDao.findBooksByAuthorLike("author");

        // 测试返回的 List<Book> 是否是按照 author 模糊查询的
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(books.get(i).getAuthor().contains("author"), true);
        }
    }

    @Test
    public void testfindBooksByPublisherLike(){
        List<Book> allData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book randBook = generateRandBook(i);
            allData.add(randBook);
        }

        // mock bookRepository.findAllByPublisherLike("%" + publisher + "%")
        when(bookRepository.findBooksByPublisherLike(any())).thenReturn(allData);

        // 测试 bookDao.findBooksByPublisherLike(publisher); 是否能够返回一个 List<Book> 对象
        List<Book> books = bookDao.findBooksByPublisherLike("publisher");

        // 测试返回的 List<Book> 是否是按照 publisher 模糊查询的
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(books.get(i).getPublisher().contains("publisher"), true);
        }
    }


    // findBooksByDisplaytitleLike
    @Test
    public void testfindBooksByDisplaytitleLike(){
        List<Book> allData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book randBook = generateRandBook(i);
            allData.add(randBook);
        }

        // mock bookRepository.findAllByDisplaytitleLike("%" + displaytitle + "%")
        when(bookRepository.findBooksByDisplaytitleIsLike(any())).thenReturn(allData);

        // 测试 bookDao.findBooksByDisplaytitleLike(displaytitle); 是否能够返回一个 List<Book> 对象
        List<Book> books = bookDao.findBooksByDisplaytitleLike("displaytitle");

        // 测试返回的 List<Book> 是否是按照 displaytitle 模糊查询的
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(books.get(i).getDisplaytitle().contains("displaytitle"), true);
        }
    }


    // findBooksGlobal
    @Test
    public void testfindBooksGlobal(){
        List<Book> allData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book randBook = generateRandBook(i);
            allData.add(randBook);
        }
        // findBooksByAuthorLikeOrPublisherLikeOrDisplaytitleLike mock

        when(bookRepository.findBooksByAuthorLikeOrPublisherLikeOrDisplaytitleLike(any(), any(), any())).thenReturn(allData);

        // 测试 bookDao.findBooksGlobal(keyword); 是否能够返回一个 List<Book> 对象
        List<Book> books = bookDao.findBooksGlobal("keyword");
    }

    @Test
    public void testdeleteOneBookByID(){
        // mock bookRepository.deleteById(id)
        doNothing().when(bookRepository).deleteById(anyInt());

        // 测试 bookDao.deleteOneBookByID(id); 是否能够成功删除
        bookDao.deleteOneBookByID(1);
    }


    // saveOneBook
    @Test
    public void testsaveOneBook(){
        // bookRepository.save(newoneBook); mock
        Book newBook = generateRandBook(1);
        when(bookRepository.save(any())).thenReturn(newBook);
        Book result = bookDao.saveOneBook(newBook);
        Assertions.assertEquals(result, newBook);
    }

    // saveAllBooks
    @Test
    public void testsaveAllBooks(){
        // mock saveAllBooks
        List<Book> allData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book randBook = generateRandBook(i);
            allData.add(randBook);
        }
        when(bookRepository.saveAll(any())).thenReturn(allData);

        // 测试 bookDao.saveAllBooks(allData); 是否能够成功
        List<Book> result = bookDao.saveAllBooks(allData);
        Assertions.assertEquals(result, allData);

    }


}
