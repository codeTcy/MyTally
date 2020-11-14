package com.tcy.mytally.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 负责管理数据库的类
 * 主要负责表中内容的操作，CRUD
 */
public class DBManger {

    public static SQLiteDatabase db;

    /*初始化数据库对象*/
    public static void initDB(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context);//得到帮助类对象
        db = helper.getWritableDatabase();//得到数据库对象
    }

    /*
     * 读取数据库里的数据，写入内存集合里面
     * kind 表示收入或者支出
     */
    public static List<TypeBean> getTypeList(int kind) {
        List<TypeBean> list = new ArrayList<>();
        //读取typetb表当中的数据
        String sql = "select * from typetb where kind =" + kind;
        Cursor cursor = db.rawQuery(sql, null);
        //循环读取游标内容，存储到对象当中
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            TypeBean bean = new TypeBean(id, typename, imageId, sImageId, kind1);
            list.add(bean);
        }

        return list;
    }

    /*
     * 向记账表当中插入一条元素
     */
    public static void insertItemToAccounttb(AccountBean accountBean) {
        ContentValues values = new ContentValues();
        //把id忽略，还有9个字段
        values.put("typename", accountBean.getTypename());
        values.put("sImageId", accountBean.getsImageId());
        values.put("beizhu", accountBean.getBeizhu());
        values.put("money", accountBean.getMoney());
        values.put("time", accountBean.getTime());
        values.put("year", accountBean.getYear());
        values.put("month", accountBean.getMonth());
        values.put("day", accountBean.getDay());
        values.put("kind", accountBean.getKind());

        db.insert("accounttb", null, values);
    }

    /*
     * 获取记账表中某一天的所有支出
     */
    public static List<AccountBean> getAccountListFromAccounttb(int year, int month, int day) {
        List<AccountBean> list = new ArrayList<>();

        //以下进行查询
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        //倒叙排列,最先获取的在最下面
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        //循环读取游标内容，存储到对象当中\
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            //year,month,day已经获取了,所以不用获取
            AccountBean bean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(bean);//加入到集合当中
        }

        return list;
    }
}
