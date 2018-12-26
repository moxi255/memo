package com.test.memo.dao;

import android.database.Cursor;

import org.litepal.crud.DataSupport;

public class dao {
    public int getCount(int memoTypeId) {
        int count=0;
        Cursor cursor = DataSupport.findBySQL("select * from memotype where id=?", String.valueOf(memoTypeId));

        if (cursor != null && cursor.moveToFirst()) {
            do {
                count=cursor.getInt(cursor.getColumnIndex("id"));

            } while (cursor.moveToNext());

        }
        return count;
    }

}
