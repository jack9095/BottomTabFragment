package com.example.schedule.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Administrator on 2018/5/8.
 */

public class ScheduleUtil {

    // 系统calendar content provider相关的uri
    public static String CALANDER_URL = "content://com.android.calendar/calendars";
    public static String CALANDER_EVENT_URL = "content://com.android.calendar/events";
    public static String CALANDER_REMIDER_URL = "content://com.android.calendar/reminders";

    private static String CALENDARS_NAME = "test";
    private static String CALENDARS_ACCOUNT_NAME = "test@gmail.com";
    private static String CALENDARS_ACCOUNT_TYPE = "com.android.exchange";
    private static String CALENDARS_DISPLAY_NAME = "测试账户";

    // 结束时间 ms
    private static long END_TIME = 60000;

    /**
     * 检查是否有现有存在的账户。存在则返回账户id，否则返回-1
     * @param context  上下文
     * @return 存在则返回账户id，否则返回-1
     */
    public static int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALANDER_URL), null, null, null, null);
        try {
            if (userCursor == null)//查询返回空值
                return -1;
            int count = userCursor.getCount();
            if (count > 0) {//存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    /**
     * 添加账户。账户创建成功则返回账户id，否则返回-1
     * @param context   上下文
     * @return  账户创建成功则返回账户id，否则返回-1
     */
    public static long addCalendarAccount(Context context) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);

        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALANDER_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    /**
     * 获取账户。如果账户不存在则先创建账户，账户存在获取账户id；获取账户成功返回账户id，否则返回-1
     * @param context  上下文
     * @return
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     */
    public static int checkAndAddCalendarAccount(Context context){
        int oldId = checkCalendarAccount(context);
        if( oldId >= 0 ){
            return oldId;
        }else{
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }

    /**
     * 添加日历事件、日程
     * @param context  上下文
     * @param title     标题
     * @param description  描述
     * @param beginTime    设置开始时间
     */
    public static void addCalendarEvent(Context context,String title, String description, long beginTime){
        // 获取日历账户的id
        int calId = checkAndAddCalendarAccount(context);
        if (calId < 0) {
            // 获取账户id失败直接返回，添加日历事件失败
            Log.e("ScheduleUtil","获取账户id失败");
            return;
        }

        ContentValues event = new ContentValues();
//        event.put("title", title);
        event.put(CalendarContract.Events.TITLE, title);
//        event.put("description", description);
        event.put(CalendarContract.Events.DESCRIPTION, description);
        // 插入账户的id
//        event.put("calendar_id", calId);
        event.put(CalendarContract.Events.CALENDAR_ID, calId);
        event.put(CalendarContract.Events.EVENT_LOCATION, "计算机学院");
        Calendar mCalendar = Calendar.getInstance();

        mCalendar.setTimeInMillis(beginTime);//设置开始时间
//        mCalendar.setTimeInMillis(System.currentTimeMillis());//设置开始时间
        long start = mCalendar.getTime().getTime();
        mCalendar.setTimeInMillis(start + END_TIME);//设置终止时间
        long end = mCalendar.getTime().getTime();

        event.put(CalendarContract.Events.DTSTART, start);  //设置开始时间
        event.put(CalendarContract.Events.DTEND, end);     //设置终止时间
        event.put(CalendarContract.Events.HAS_ALARM, 1);//设置有闹钟提醒   如果需要有提醒,必须要有这一行
        TimeZone tz = TimeZone.getDefault();//获取默认时区
        event.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());  //这个是时区，必须有，
//        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");  //这个是时区，必须有，
        //添加事件
        Uri newEvent = context.getContentResolver().insert(Uri.parse(CALANDER_EVENT_URL), event);
        if (newEvent == null) {
            // 添加日历事件失败直接返回
            Log.e("ScheduleUtil","添加日历事件失败");
            return;
        }
        //事件提醒的设定
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        // 提前10分钟有提醒
//        values.put(CalendarContract.Reminders.MINUTES, 10);
        values.put(CalendarContract.Reminders.MINUTES, 0);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(Uri.parse(CALANDER_REMIDER_URL), values);
        if(uri == null) {
            // 添加闹钟提醒失败直接返回
            Log.e("ScheduleUtil","添加日程失败");
            return;
        }else{
            Log.e("ScheduleUtil","添加日程成功");
        }
    }

    /**
     * 删除日历事件、日程  (根据设置的title来查找并删除)
     * @param context  上下文
     * @param title  日程标题
     */
    public static void deleteCalendarEvent(Context context,String title){
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALANDER_EVENT_URL), null, null, null, null);
        try {
            if (eventCursor == null)//查询返回空值
                return;
            if (eventCursor.getCount() > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
                    if (!TextUtils.isEmpty(title) && title.equals(eventTitle)) {
                        int id = eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID));//取得id
                        Uri deleteUri = ContentUris.withAppendedId(Uri.parse(CALANDER_EVENT_URL), id);
                        int rows = context.getContentResolver().delete(deleteUri, null, null);
                        if (rows == -1) {
                            //事件删除失败
                            return;
                        }
                    }
                }
            }
        } finally {
            if (eventCursor != null) {
                eventCursor.close();
            }
        }
    }
}
