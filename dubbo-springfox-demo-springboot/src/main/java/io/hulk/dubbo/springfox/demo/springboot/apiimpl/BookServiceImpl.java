package io.hulk.dubbo.springfox.demo.springboot.apiimpl;

import io.hulk.dubbo.springfox.demo.springboot.api.BookService;
import io.hulk.dubbo.springfox.demo.springboot.model.Book;
import io.hulk.dubbo.springfox.demo.springboot.model.User;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaojigang
 * @date 2018/5/16
 */
public class BookServiceImpl implements BookService {
    @Override
    public Book testCommonField(Book book, User user, String title, int count) {
        if (book != null && user != null) {
            return new Book(book.getTitle(), user.getName());
        }
        return new Book(title + "-" + count, "book为null");
    }

    @Override
    public String testGenericField(List<Book> books) {
        if (!CollectionUtils.isEmpty(books)) {
            return books.get(0).getTitle();
        }
        return "书籍列表为空";
    }

    @Override
    public String testNestingGenericField(List<List<Book>> books) {
        if (!CollectionUtils.isEmpty(books)) {
            List<Book> nestingBooks = books.get(0);
            if (!CollectionUtils.isEmpty(nestingBooks)) {
                return nestingBooks.get(0).getTitle();
            }
        }
        return "书籍列表为空或其内嵌列表为空";
    }

    @Override
    public Map<String, Book> testMapFieldAndResp(Map<String, Book> title2Book) {
        Map<String, Book> body2Book = new HashMap<>();
        if (title2Book != null && title2Book.size() > 0) {
            for (String title : title2Book.keySet()) {
                body2Book.put(title2Book.get(title).getContent(), title2Book.get(title));
            }
        }
        return body2Book;
    }

    @Override
    public Book testCommonField(Book book, String title) {
        return new Book(title, book.getContent());
    }

    @Override
    public void testVoidReturn() {
        System.out.println("testVoidReturn,haha");
    }

    @Override
    public Long testLongField(Long id) {
        return id;
    }

    @Override
    public Book testObjectField(Book book1, Book book2) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        Book book = new Book();
        book.setId(99L);
        book.setTitle(book1.getTitle());
        book.setContent(book2.getContent());
        throw new RuntimeException("haha,error!!!");
    }
}