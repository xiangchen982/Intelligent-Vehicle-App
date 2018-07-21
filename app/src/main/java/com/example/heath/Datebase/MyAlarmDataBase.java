package com.example.heath.Datebase;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.heath.Model.AlarmModel;


import java.util.ArrayList;
import java.util.List;

public class MyAlarmDataBase {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = " AlarmDatabase";
    private SQLiteDatabase db;
    private MyAlarmOpenHelper myAlarmOpenHelper;
    private static final String TABLE_ALARM = " AlarmTable";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TIME = "time";
    private static final String KEY_REPEAT_TYPE = "repeat_type";
    private static final String KEY_REPEAT_CODE = "repeat_code";
    private static final String KEY_WAKE_TYPE = "normal";
    private static final String KEY_ACTIVE = "active";
    private static final String KEY_RING = "ring";


    public MyAlarmDataBase(Context context){
        MyAlarmOpenHelper openHelper = new MyAlarmOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.myAlarmOpenHelper = openHelper;
        db=openHelper.getWritableDatabase();
    }

    public int addAlarm(AlarmModel alarmModel){
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE , alarmModel.getTitle());
        values.put(KEY_TIME, alarmModel.getTime());
        values.put(KEY_REPEAT_TYPE, alarmModel.getRepeatType());
        values.put(KEY_REPEAT_CODE,alarmModel.getRepeatCode());
        values.put(KEY_WAKE_TYPE,alarmModel.getWakeType());
        values.put(KEY_RING,alarmModel.getRing());
        values.put(KEY_ACTIVE, alarmModel.getActive());

        if (!db.isOpen()){
            db = myAlarmOpenHelper.getWritableDatabase();
        }

        long ID = db.insert(TABLE_ALARM,null,values);
        db.close();
        return (int)ID;
    }


    //获取单个闹钟
    public AlarmModel getAlarm(int id){
        if (!db.isOpen()){
            db = myAlarmOpenHelper.getWritableDatabase();
        }
        Cursor cursor = db.query(TABLE_ALARM,new String[]
                {
                        KEY_ID,
                        KEY_TITLE,
                        KEY_TIME,
                        KEY_WAKE_TYPE,
                        KEY_REPEAT_TYPE,
                        KEY_REPEAT_CODE,
                        KEY_RING,
                        KEY_ACTIVE
                },KEY_ID + "=?",new String[]{String.valueOf(id)},null,null,null,null);


        AlarmModel alarmModel = null;
        if (cursor != null && cursor.moveToFirst()) {

            alarmModel = new AlarmModel(Integer.parseInt(cursor.getString(0))
                    ,cursor.getString(1),cursor.getString(2),cursor.getString(3),
                    cursor.getString(4),cursor.getString(5),cursor.getString(6),
                    cursor.getString(7));

            cursor.close();
        }

        return alarmModel;
    }

    //获取所有Alarm
    public List<AlarmModel> getAllAlarms(){
        List<AlarmModel> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_ALARM;

        if (!db.isOpen()){
            db = myAlarmOpenHelper.getWritableDatabase();
        }

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                AlarmModel alarmModel = new AlarmModel();
                alarmModel.setID(Integer.parseInt(cursor.getString(0)));
                alarmModel.setTitle(cursor.getString(1));
                alarmModel.setTime(cursor.getString(2));
                alarmModel.setWakeType(cursor.getString(3));
                alarmModel.setRepeatType(cursor.getString(4));
                alarmModel.setRepeatCode(cursor.getString(5));
                alarmModel.setRing(cursor.getString(6));
                alarmModel.setActive(cursor.getString(7));

                list.add(alarmModel);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return list;
    }


    public int updateAlarm(AlarmModel alarmModel){
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE , alarmModel.getTitle());
        values.put(KEY_TIME , alarmModel.getTime());
        values.put(KEY_WAKE_TYPE , alarmModel.getWakeType());
        values.put(KEY_REPEAT_TYPE, alarmModel.getRepeatType());
        values.put(KEY_REPEAT_CODE, alarmModel.getRepeatCode());
        values.put(KEY_RING,alarmModel.getRing());
        values.put(KEY_ACTIVE, alarmModel.getActive());

        if (!db.isOpen()){
            db = myAlarmOpenHelper.getWritableDatabase();
        }

        return db.update(TABLE_ALARM, values, KEY_ID + "=?",
                new String[]{String.valueOf(alarmModel.getID())});
    }

    public void deleteAlarm(AlarmModel alarmModel){
        if (!db.isOpen()){
            db = myAlarmOpenHelper.getWritableDatabase();
        }
        db.delete(TABLE_ALARM, KEY_ID + "=?",
                new String[]{String.valueOf(alarmModel.getID())});
        db.close();
    }



}
