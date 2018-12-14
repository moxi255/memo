package com.test.memo.db;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class Memo extends DataSupport{
    private int id;
    private Date data;
    private String title;
    private String content;
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
