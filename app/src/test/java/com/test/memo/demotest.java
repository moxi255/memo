package com.test.memo;

import com.test.memo.db.Memo;
import com.test.memo.db.MemoType;

import org.junit.Test;
import org.litepal.crud.DataSupport;

import java.util.List;

public class demotest {
    @Test
    public void  findAll(){
        List<Memo> memos=DataSupport.findAll(Memo.class);
        for(Memo memo:memos){
            System.out.print(memo.toString());
        }
    }
}
