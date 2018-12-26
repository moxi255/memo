package com.test.memo.db;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

public class Memo extends DataSupport implements Serializable {
    private int id;
    private Date data;
    private String title;
    private String content;
    private int memotype_id;

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
    private Memo memo;

    public Memo getMemo() {
        return memo;
    }

    public void setMemo(Memo memo) {
        this.memo = memo;
    }

    public String toString(){
        String re=id+","+data+","+title+","+content+","+memotype_id;
        return re;
    }

    public int getMemotype_id() {
        return memotype_id;
    }

    public void setMemotype_id(int memotype_id) {
        this.memotype_id = memotype_id;
    }
}
