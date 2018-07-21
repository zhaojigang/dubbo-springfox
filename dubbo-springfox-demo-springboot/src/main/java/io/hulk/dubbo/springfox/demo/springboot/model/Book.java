package io.hulk.dubbo.springfox.demo.springboot.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zhaojigang
 * @date 2018/3/15
 */
@ApiModel("书籍")
public class Book {
    @ApiModelProperty("ID")
    private Long   id;
    @ApiModelProperty("书标题")
    private String title;
    @ApiModelProperty("书的内容")
    private String content;

    public Book() {
    }

    public Book(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
