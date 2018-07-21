package io.hulk.dubbo.springfox.demo.springboot.api;

import io.hulk.dubbo.springfox.demo.springboot.model.Book;
import io.hulk.dubbo.springfox.demo.springboot.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;
import java.util.Map;

/**
 * @author zhaojigang
 * @date 2018/5/16
 */
@Api("Book相关的Dubbo服务")
public interface BookService {

    @ApiOperation("测试对象+基本属性")
    Book testCommonField(@ApiParam(value = "书籍", required = true) Book book,
                         @ApiParam(value = "用户") User user,
                         @ApiParam("书题") String title,
                         @ApiParam(value = "数量", required = true, defaultValue = "1") int count);

    @ApiOperation("测试一层泛型")
    String testGenericField(@ApiParam List<Book> books);

    @ApiOperation("测试嵌套泛型")
    String testNestingGenericField(@ApiParam List<List<Book>> books);

    @ApiOperation("测试Map")
    Map<String, Book> testMapFieldAndResp(@ApiParam Map<String, Book> title2Book);

    @ApiOperation(value = "测试方法重载，使用nickName进行区分访问uri", nickname = "byBookAndTitle")
    Book testCommonField(@ApiParam Book book, @ApiParam("书题") String title);

    @ApiOperation("测试参数为空+返回为空")
    void testVoidReturn();

    @ApiOperation(value = "测试Long")
    Long testLongField(@ApiParam("id") Long id);

    @ApiOperation(value = "测试接口抛异常")
    Book testObjectField(Book book1, Book book2) throws ArrayIndexOutOfBoundsException, NumberFormatException;
}
