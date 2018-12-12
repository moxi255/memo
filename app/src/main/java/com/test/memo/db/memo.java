package com.test.memo.db;

import java.util.Date;

public class memo {
    private Date data;
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
}
