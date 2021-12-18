package com.jfeat.am.dbss.services.model;


import java.sql.Date;
import java.time.LocalDate;

/**
 * Created by Silent-Y on 2017/11/2.
 */
public class Message {
    private Integer id;
    private Date timestamp;
    private String type;
    private String author;
    private String sql;

    public Message(){
        this.timestamp = Date.valueOf(LocalDate.now());
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
