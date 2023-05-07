package com.zzq.ebook.entity;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


/**
 * @Author : zzq
 * @Description :
 *  因为Book的这个类的覆盖用到的代码其实很少，所以我们在针对entity的测试中，额外增加了测试，保证覆盖率的完整性
 *  但是在实际的开发中，我们不需要这么做，因为我们的测试是针对业务逻辑的，而不是针对实体类的
 * */
@ExtendWith(MockitoExtension.class)
public class BookTest {
    @Test
    public void testGetBookId() {
        Book book = new Book();
        book.setID(1);
        Assertions.assertEquals(1, book.getID());
    }

    @Test
    public void testGetBookname() {
        Book book = new Book();
        book.setBookname("bookname");
        Assertions.assertEquals("bookname", book.getBookname());
    }

    @Test
    public void testGetAuthor() {
        Book book = new Book();
        book.setAuthor("author");
        Assertions.assertEquals("author", book.getAuthor());
    }

    @Test
    public void testGetPublisher() {
        Book book = new Book();
        book.setPublisher("publisher");
        Assertions.assertEquals("publisher", book.getPublisher());
    }

    @Test
    public void testGetPrice() {
        Book book = new Book();
        book.setPrice(1);
        Assertions.assertEquals(1, book.getPrice());
    }

    @Test
    public void testGetInventory() {
        Book book = new Book();
        book.setInventory(1);
        Assertions.assertEquals(1, book.getInventory());
    }

    @Test
    public void testGetSellnumber() {
        Book book = new Book();
        book.setSellnumber(1);
        Assertions.assertEquals(1, book.getSellnumber());
    }

    @Test
    public void testGetDeparture() {
        Book book = new Book();
        book.setDeparture("departure");
        Assertions.assertEquals("departure", book.getDeparture());
    }

    @Test
    public void testGetISBN() {
        Book book = new Book();
        book.setISBN("ISBN");
        Assertions.assertEquals("ISBN", book.getISBN());
    }

    @Test
    public void testGetDescription() {
        Book book = new Book();
        book.setDescription("description");
        Assertions.assertEquals("description", book.getDescription());
    }

    @Test
    public void testGetDisplaytitle() {
        Book book = new Book();
        book.setDisplaytitle("displaytitle");
        Assertions.assertEquals("displaytitle", book.getDisplaytitle());
    }

    @Test
    public void testGetImgtitle() {
        Book book = new Book();
        book.setImgtitle("imgtitle");
        Assertions.assertEquals("imgtitle", book.getImgtitle());
    }

    @Test
    public void testSetBookname() {
        Book book = new Book();
        book.setBookname("bookname");
        Assertions.assertEquals("bookname", book.getBookname());
    }

    @Test
    public void testSetAuthor() {
        Book book = new Book();
        book.setAuthor("author");
        Assertions.assertEquals("author", book.getAuthor());
    }

    @Test
    public void testSetPublisher() {
        Book book = new Book();
        book.setPublisher("publisher");
        Assertions.assertEquals("publisher", book.getPublisher());
    }

    @Test
    public void testSetPrice() {
        Book book = new Book();
        book.setPrice(1);
        Assertions.assertEquals(1, book.getPrice());
    }

    @Test
    public void testSetInventory() {
        Book book = new Book();
        book.setInventory(1);
        Assertions.assertEquals(1, book.getInventory());
    }

    @Test
    public void testSetSellnumber() {
        Book book = new Book();
        book.setSellnumber(1);
        Assertions.assertEquals(1, book.getSellnumber());
    }

    @Test
    public void testSetDeparture() {
        Book book = new Book();
        book.setDeparture("departure");
        Assertions.assertEquals("departure", book.getDeparture());
    }

    @Test
    public void testSetISBN() {
        Book book = new Book();
        book.setISBN("ISBN");
        Assertions.assertEquals("ISBN", book.getISBN());
    }


}
