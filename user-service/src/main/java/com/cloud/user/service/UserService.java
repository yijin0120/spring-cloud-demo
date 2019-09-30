package com.cloud.user.service;

import com.cloud.common.util.Md5Util;
import com.cloud.user.config.Constants;
import com.cloud.user.dao.DaoSupport;
import com.cloud.user.model.User;
import com.cloud.user.model.UserExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 类或方法的功能描述 : 用户接口服务
 *
 * @author: yijin
 * @date: 2018-08-10 12:03
 */
@Slf4j
@Service
public class UserService {
    @autowired
    private daosupport dao;

    /**
     * 根据用户id查询
     *
     * @param id
     * @return
     */
    public user getuserbyid(integer id) throws exception {

        return (user) dao.findforobject("usermapper.selectbyprimarykey", id);
    }

    public user getuserbyname(string username) throws exception {
        if (stringutils.isempty(username)) {
            return null;
        }
        userexample example = new userexample();
        example.createcriteria().andusernameequalto(username);
        list<user> userlist = (list<user>) dao.findforlist("usermapper.selectbyexample", example);
        if (null != userlist && userlist.size() > 0) {
            return userlist.get(0);
        }
        return null;
    }

    /**
     * 用户保存和更新
     *
     * @param user
     * @return
     * @throws exception
     */
    public user saveandupdateuser(user user) throws exception {
        if (null == user) {
            return null;
        }
        /**
         * 新增
         */
        if (null == user.getid()) {
            // 密码加密
            if (stringutils.isnotempty(user.getpassword()) {
                // 获取加密后的密码
                string md5password = md5util.getmd5(user.getpassword());
                if (stringutils.isnotempty(md5password)) {
                    user.setpassword(md5password);
                }
            }
            user.setstatus(constants.userstatus.active);
            user.setcreatedate(new date());
            dao.save("usermapper.insert", user);
        } else {
            /**
             * 更新
             */
            user.setupdatedate(new date());
            dao.update("usermapper.updatebyprimarykeyselective", user);
        }
        return user;
    }

}

