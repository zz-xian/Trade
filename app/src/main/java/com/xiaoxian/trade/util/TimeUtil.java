package com.xiaoxian.trade.util;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间相关工具类
 */

public class TimeUtil {
    /**
     * 服务端返回的时间经常会以.0结尾，去掉
     */
    public static String removeLastZero(String time) {
        if (TextUtils.isEmpty(time))
            return "";
        if (time.length() > 19)
            return time.substring(0, 19);
        else
            return time;
    }

    /**
     * yyyy-MM-dd HH:mm:ss格式时间，与当前相比，时间差转换为:几天几时几分几秒
     */
    public static String convert(String time) {
        try {
            long time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time).getTime();
            return convert((int) ((time1 - System.currentTimeMillis()) / 1000));
        } catch (ParseException e) {
            e.printStackTrace();
            return "未知";
        }
    }

    /**
     * yyyy-MM-dd HH:mm:ss格式时间，两个时间相比，时间差转换为：几天几时几分几秒
     *
     * @return
     */
    public static String convert(String startTime, String endTime) {
        try {
            long time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime).getTime();
            long time2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime).getTime();
            return convert((int) ((time2 - time1) / 1000));
        } catch (ParseException e) {
            e.printStackTrace();
            return "未知";
        }
    }

    /**
     * 时长秒，时间差转换为：几天几时几分几秒
     * 1天：86400s；1时：3600s；1分：60s
     * @param span 相差间隔，单位为秒
     */
    public static String convert(long span) {
        if (span < 0)
            return "时间超了";

        StringBuffer sb = new StringBuffer();
        if (span >= 86400) {
            int day = (int) (span / 86400);
            int hour = (int) ((span % 86400) / 3600);
            int minute = (int) ((span % 86400 % 3600) / 60);
            int second = (int) (span % 86400 % 3600 % 60);
            sb.append(day).append("天").append(hour).append("小时").append(minute).append("分钟").append(second).append("秒");
        } else if (span > 3600) {
            int hour = (int) (span / 3600);
            int minute = (int) ((span % 3600) / 60);
            int second = (int) (span % 3600 % 60);
            sb.append(hour).append("小时").append(minute).append("分钟").append(second).append("秒");
        } else if (span > 60) {
            int minute = (int) (span / 60);
            int second = (int) (span % 60);
            sb.append(minute).append("分钟").append(second).append("秒");
        } else {
            sb.append(span).append("秒");
        }
        return sb.toString();
    }

    /**
     * 时长秒，转换为：几分几秒
     */
    public static String convert1(long span) {
        if (span < 0)
            return String.valueOf(span);

        StringBuffer sb = new StringBuffer();
        if (span > 60) {
            int minute = (int) (span / 60);
            int second = (int) (span % 60);
            sb.append(minute).append("'").append(second).append("''");
        } else {
            sb.append(span).append("''");
        }
        return sb.toString();
    }

    /**
     * EEE MMM dd HH:mm:ss zzz yyyy格式时间，与当前相比，格式化为：xx分钟前，xx小时前和日期
     */
    public static String convertBeforeTimeZone(String time) {
        Log.v("Info", time);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy", Locale.ENGLISH);
        sdf.setLenient(false);//严格限制日期转换
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (Exception e) {
            return "";
        }
        return convertBefore(date.getTime());
    }

    /**
     * yyyy-MM-dd HH:mm:ss格式时间，与当前对比，格式化为：xx分钟前，xx小时前和日期
     */
    public static String convertBefore(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        try {
            long time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time).getTime();
            return convertBefore(time1);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将对比后时间，格式化为：xx分钟前，xx小时前和日期
     */
    public static String convertBefore(long time) {
        if (time < 0)
            return String.valueOf(time);

        int diffTime = (int) ((System.currentTimeMillis() - time) / 1000);
        if (diffTime < 86400 && diffTime > 0) {
            if (diffTime < 3600) {
                int minute = (diffTime / 60);
                if (minute == 0)
                    return "刚刚";
                else
                    return (diffTime / 60) + "分钟前";
            } else {
                return (diffTime / 3600) + "小时前";
            }
        } else {
            Calendar now = Calendar.getInstance();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    && c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
                return new SimpleDateFormat("HH:mm").format(c.getTime());
            }
            if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    && c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1) {
                return new SimpleDateFormat("昨天 HH:mm").format(c.getTime());
            }
            if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    && c.get(Calendar.DATE) == now.get(Calendar.DATE) - 2) {
                return new SimpleDateFormat("前天 HH:mm").format(c.getTime());
            } else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                return new SimpleDateFormat("M月d日 HH:mm").format(c.getTime());
            } else {
                return new SimpleDateFormat("yy年M月d日").format(c.getTime());
            }
        }
    }

    /**
     * 指定时间，在时间条件范围内，返回true，反之，返回false
     */
    public static boolean timeCompare(String sDate, String eDate, String checkTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            long sTime = sdf.parse(sDate).getTime();
            long eTime = sdf.parse(eDate).getTime();
            long check = sdf.parse(checkTime).getTime();
            if (check > sTime && check < eTime)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 当前时间，在时间条件范围内，返回true，反之，返回false
     */
    public static boolean timeCompare1(String sDate, String eDate) {
        try {
            long check = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            long sTime = sdf.parse(sdf1.format(check) + " " + sDate).getTime();
            long eTime = sdf.parse(sdf1.format(check) + " " + eDate).getTime();
            if (check > sTime && check < eTime)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断两个时间大小
     */
    public static boolean timeCompare2(String sDate, String eDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            long sTime = sdf.parse(sDate).getTime();
            long eTime = sdf.parse(eDate).getTime();
            if (sTime > eTime)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 传入时间添加秒
     *
     * @param time 时间
     * @param second 秒数，正数为添加秒，负数是减少秒
     * @return
     */
    public static String addSecond(String time, int second) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long resultTime = sdf.parse(time).getTime() + 1000 * second;
            return sdf.format(resultTime);
        } catch (Exception e) {
            return "";
        }
    }

    public static String parseTime(String time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Long.parseLong(time));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(c.getTime());
    }

    /**
     * 格式化取当前时间
     */
    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
    }
}
