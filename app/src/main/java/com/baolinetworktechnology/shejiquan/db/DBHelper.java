package com.baolinetworktechnology.shejiquan.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHelper extends SQLiteOpenHelper
{
	//数据库名称
	public static final String DB_NAME="coll.db";
	//环信库名称
	public static final String ea_NAME="ease.db";
	//表名称
	private static final String TBL_NAME="CollTbl";
	//环信名称
	private static final String EASE_NAME="easeTbl";
	//创建表SQL语句
	//isConcern  0是true ,1是false.
	private static final String CREATE_TBL=" create table "
			+" CollTbl(_id integer primary key autoincrement,friendGUID text,fromID text,logo text,name text," +
			"markStatus integer,markName text,nickName text,isConcern text,remarkName text,initial text,guid text,mobile text,label text)";
	//创建环信头像表SQL语句
	private static final String EASE_TBL=" create table "
			+" easeTbl(_id integer primary key autoincrement,Nick text,Avatar text,LoginName text)";
	//SQLiteDatabase实例
	private SQLiteDatabase db;
	
	/*
	 * 构造方法
	 */
	public DBHelper(Context c,String str){
		super(c,str,null,2);
	};
	/*
	 * 创建表
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	public void onCreate(SQLiteDatabase db)
	{
		this.db=db;
		db.execSQL(CREATE_TBL);
		db.execSQL(EASE_TBL);
	}
	/*
    * 更新表中的数据
    */
	public void update(ContentValues values,String friendGUID)
	{
		//获得SQLiteDatabase实例
		SQLiteDatabase db=getWritableDatabase();
		//更新
		db.update("friendGUID", values, "friendGUID ="+friendGUID, null);
		//关闭
		db.close();
	}
	/*
	 * 插入方法
	 */
	public void insert(ContentValues values)
	{
		//获得SQLiteDatabase实例
		SQLiteDatabase db=getWritableDatabase();
		//插入
		db.insert(TBL_NAME, null, values);
		//关闭
		db.close();
	}
	/*
	 * 查询方法
	 */
	public Cursor query(String userguid)
	{
		//获得SQLiteDatabase实例
		SQLiteDatabase db=getWritableDatabase();
		//查询获得Cursor
		Cursor c=db.query(TBL_NAME, null, "guid=?", new String[]{userguid}, null, null, null);
		return c;
	}
	/*
 * 插入方法
 */
	public void esaeinsert(ContentValues values)
	{
		//获得SQLiteDatabase实例
		SQLiteDatabase db=getWritableDatabase();
		//插入
		db.insert(EASE_NAME, null, values);
		//关闭
		db.close();
	}
	/*
	 * 查询方法
	 */
	public Cursor esaequery()
	{
		//获得SQLiteDatabase实例
		SQLiteDatabase db=getWritableDatabase();
		//查询获得Cursor
		Cursor c=db.query(EASE_NAME, null, null, null, null, null, null);
		return c;
	}
	/*
	 * 删除方法
	 */
	public void del(String friendGUID)
	{
		if(db==null)
		{
			//获得SQLiteDatabase实例
			db=getWritableDatabase();			
		}
		//执行删除
		db.delete(TBL_NAME, "friendGUID=?", new String[]{friendGUID});
	}
	/*
 * 删除方法
 */
	public void delete(String guid)
	{
		if(db==null)
		{
			//获得SQLiteDatabase实例
			db=getWritableDatabase();
		}
		//执行删除
		db.delete(TBL_NAME, "guid=?", new String[]{guid});
	}
	/*
* 删除方法
*/
	public void easedelete(String LoginName)
	{
		if(db==null)
		{
			//获得SQLiteDatabase实例
			db=getWritableDatabase();
		}
		//执行删除
		db.delete(EASE_NAME, "LoginName=?", new String[]{LoginName});
	}
	/*
	 * 关闭数据库
	 */
	public void colse()
	{
		if(db!=null)
		{
			db.close();
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
