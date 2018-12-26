package com.test.memo.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class MemoType extends DataSupport implements Serializable {
    private int id;
    private String name;
    private int iconId;
    @Column(defaultValue = "0")
    private int count;

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



    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count =count;
    }
    public  String toString(){
        return id+","+name+","+count;
    }
}
