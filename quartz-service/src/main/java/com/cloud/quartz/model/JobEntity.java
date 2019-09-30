package com.cloud.quartz.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yijin
 */
@Data
public class JobEntity implements Serializable {
    private Integer id;
    private String name;          //job名称
    private String group;         //job组名
    private String cron;          //执行的cron
    private String className;     //任务类名(全限性)
    private String description;   //job描述信息
    private String status;        //job的执行状态,设置为OPEN/CLOSE且只有该值为OPEN才会执行该Job
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Override
    public String toString() {
        return "JobEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", cron='" + cron + '\'' +
                ", className='" + className + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public static class Builder {
        private Integer id;
        private String name = "";          //job名称
        private String group = "";         //job组名
        private String cron = "";          //执行的cron
        private String className = "";     //job类名
        private String description = "";   //job描述信息
        private String status = "";        //job的执行状态,只有该值为OPEN才会执行该Job
        public Builder withId(Integer i) {
            id = i;
            return this;
        }
        public Builder withName(String n) {
            name = n;
            return this;
        }
        public Builder withGroup(String g) {
            group = g;
            return this;
        }
        public Builder withCron(String c) {
            cron = c;
            return this;
        }
        public Builder withClassName(String p) {
            className = p;
            return this;
        }
        public Builder withDescription(String d) {
            description = d;
            return this;
        }
        public Builder withStatus(String s) {
            status = s;
            return this;
        }
    }
}