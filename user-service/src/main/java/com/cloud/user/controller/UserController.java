package com.cloud.user.controller;

import com.cloud.common.enums.ApiMsgEnum;
import com.cloud.common.vo.BaseReturnVO;
import com.cloud.user.service.UserService;
import com.cloud.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 类或方法的功能描述 : 用户接口
 *
 * @author: yijin
 * @date: 2018-06-21 11:46
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Value("${server.port}")
    String port;
    @Autowired
    UserService userService;

    /**
     * 获取用户服务的端口
     * @return
     */
    @GetMapping("/getPort")
    public String getPort() {
        log.info("user-service port：" + port);
        return "user-service port：" + port;
    }

    /**
     * 根据用户ID查询
     * @param id
     * @return
     */
    @GetMapping("/getUserById/{id}")
    public BaseReturnVO getUserById(@PathVariable("id") Integer id) {
        try {
            User user = userService.getUserById(id);
            return new BaseReturnVO(user);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据用户名查询
     * @param userName
     * @return
     */
    @PostMapping("/getUserByName")
    public BaseReturnVO getUserByName(String userName) {
        try {
            return new BaseReturnVO(userService.getUserByName(userName));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存用户
     * @param user
     * @return
     */
    @PostMapping("/saveUser")
    public BaseReturnVO saveUser(@RequestBody User user) {
        if (null == user) {
            return new BaseReturnVO(ApiMsgEnum.OK.getResCode(), "用户信息为空");
        }
        try {
            User userTmp = userService.saveAndUpdateUser(user);
            return new BaseReturnVO(userTmp);
        } catch (Exception e) {
            log.error("保存用户失败");
            return new BaseReturnVO(ApiMsgEnum.INTERNAL_SERVER_ERROR.getResCode(), "保存用户信息失败", e);
        }
    }

    /**
     * 测试AOP切面接口
     * @param exception
     * @return
     */
    @PostMapping("/list/{exception}")
    public List<User> testAspect(@PathVariable("exception") Boolean exception) {
        if (exception) {
            throw new Error("测试抛出异常");
        }
        try {
            User user = userService.getUserById(1);
            List<User> list = new ArrayList<>();
            list.add(user);
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}

