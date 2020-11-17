package com.tcy.mytally.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tcy.mytally.util.FloatUtils;

import java.math.BigDecimal;
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

    /*
     * 获取记账表中某一个月的所有支出
     */
    public static List<AccountBean> getAccountListOneMonthFromAccounttb(int year, int month) {
        List<AccountBean> list = new ArrayList<>();

        //以下进行查询
        String sql = "select * from accounttb where year=? and month=? order by id desc";
        //倒叙排列,最先获取的在最下面
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        //循环读取游标内容，存储到对象当中
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            //year,month,day已经获取了,所以不用获取
            AccountBean bean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(bean);//加入到集合当中
        }

        return list;
    }


    /*
     * 获取某一天的支出或者收入的总金额,传入kind ：支出----0  收入----1
     */
    public static float getSumMoneyOneDay(int year, int month, int day, int kind) {
        float total = 0.0f;

        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        //遍历
        if (cursor.moveToFirst()) {
            float sum = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = sum;
        }
        return total;
    }

    /*
     * 获取某一个月的支出或者收入的总金额,传入kind ：支出----0  收入----1
     */
    public static float getSumMoneyOneMonth(int year, int month, int kind) {
        float total = 0.0f;

        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        //遍历
        if (cursor.moveToFirst()) {
            float sum = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = sum;
        }
        return total;
    }

    /*
     * 统计某月份支出或者收入情况有多少条  支出----0  收入----1
     * */
    public static int getCountItemOneMonth(int year, int month, int kind) {
        int total = 0;

        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        //遍历
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndex("count(money)"));
            total = count;
        }

        return total;
    }


    /*
     * 获取某一年的支出或者收入的总金额,传入kind ：支出----0  收入----1
     */
    public static float getSumMoneyOneYear(int year, int kind) {
        float total = 0.0f;

        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        //遍历
        if (cursor.moveToFirst()) {
            float sum = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = sum;
        }
        return total;
    }


    /*
     * 根据传入的id,删除accounttb表中的一条数据
     * */
    public static int deleteItemFromAccounttbById(int id) {
        int i = db.delete("accounttb", "id=?", new String[]{id + ""});//得到删除的是哪一条数据
        return i;
    }


    /*
     * 根据备注搜索收入或者支出的情况列表
     * */
    public static List<AccountBean> getAccountListByRemarkFromAccounttb(String beizhu) {
        List<AccountBean> list = new ArrayList<>();

        //以下进行查询
        //模糊查询
        String sql = "select * from accounttb where beizhu like '%" + beizhu + "%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu1 = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));

            //
            AccountBean bean = new AccountBean(id, typename, sImageId, beizhu1, money, time, year, month, day, kind);
            list.add(bean);
        }
        return list;
    }

    /*
     * 查询记账的表中有几个年份信息
     * */
    public static List<Integer> getYearListFromAccounttb() {
        List<Integer> yearList = new ArrayList<>();

        //
        String sql = "select distinct(year) from accounttb order by year asc";//升序排
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            yearList.add(year);
        }

        return yearList;
    }

    /*
     * 删除accounttb表格当中的所有数据
     * */
    public static void deleteAllAccounttb() {
        String sql = "delete from accounttb";
        db.execSQL(sql);
    }

    /*
     * 查询指定年份和月份的收入或支出每一种类型的总钱数
     * */
    public static List<ChartItemBean> getChartListFromAccounttb(int year, int month, int kind) {
        List<ChartItemBean> list = new ArrayList<>();

        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);//求出支出或者收入总钱数
        String sql = "select typename,sImageId,sum(money)as total from accounttb where year=? and month=? and kind=? group by typename order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));
            //计算所占百分比  total/sumMonth
            float ratio = FloatUtils.div(total, sumMoneyOneMonth);
            ChartItemBean bean = new ChartItemBean(sImageId, typename, ratio, total);
            list.add(bean);
        }

        return list;
    }


}
