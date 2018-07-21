package io.hulk.dubbo.springfox.demo.springboot.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zhaojigang
 * @date 2018/3/13
 */
@ApiModel("用户模型")
public class User {
    @ApiModelProperty("用户ID")
    private int    id;
    @ApiModelProperty("用户姓名")
    private String name;
    @ApiModelProperty("用户密码")
    private String password;

    public User() {
    }

    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
