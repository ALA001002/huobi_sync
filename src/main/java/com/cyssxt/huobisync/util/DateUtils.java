package com.cyssxt.huobisync.util;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 *
 * @author bigo
 */
@Slf4j
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String YYYYMMDDHHMM = "yyyyMMddHHmm";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }
    public static final Long parseDateToMinuteStr(Calendar calendar)
    {
        String timer =  new SimpleDateFormat(YYYYMMDDHHMM).format(calendar.getTime());
        return Long.valueOf(timer);
    }

    public static void main(String[] args) {
        System.out.println(parseDateToMinuteStr(Calendar.getInstance()));
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 根据系统的时区，获取今天凌晨0点的时间
     * 十三位的毫秒级时间戳，如果精确到秒需要再除以1000
     * @return
     */
    public static Long getTodayZeroTimestamps(){
        long nowTime =System.currentTimeMillis();
        return nowTime - ((nowTime + TimeZone.getDefault().getRawOffset()) % (24 * 60 * 60 * 1000L));
    }

    /**
     * @Description 是否为昨天
     * @param inputJudgeDate 要判断是否在昨天24h内的时间
     * @return
     */
    public static boolean isYesterday(Date inputJudgeDate) {
        //获取昨天日期
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date day = cal.getTime();
        return isDestineDay(day, inputJudgeDate);
    }

    /**
     * @Description 是否为今天
     * @param inputJudgeDate 要判断是否在昨天24h内的时间
     * @return
     */
    public static boolean isToday(Date inputJudgeDate) {
        //获取今天日期
        Calendar cal=Calendar.getInstance();
        Date day = cal.getTime();
        return isDestineDay(day, inputJudgeDate);
    }

    /**
     * @Description 是否为指定的日期
     * @param inputJudgeDate 要判断是否在昨天24h内的时间
     * @return
     */
    public static boolean isDestineDay(Date destineDate, Date inputJudgeDate) {
        boolean flag = false;
        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = sp.format(destineDate);
        //定义每天的24h时间范围
        String beginTime = yesterday + " 00:00:00";
        String endTime = yesterday + " 23:59:59";
        Date paseBeginTime = null;
        Date paseEndTime = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            paseBeginTime = dateFormat.parse(beginTime);
            paseEndTime = dateFormat.parse(endTime);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        if(inputJudgeDate.after(paseBeginTime) && inputJudgeDate.before(paseEndTime)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 获取指定日期的开始时间
     *
     * @param certainDate
     *          指定日期
     * @param flex
     *          正负整数，正数表示指定日期的后几天，负数表示指定日期的前几天
     * @return
     */
    public static Date getStartTime(Date certainDate, int flex) {
        return DateUtils.truncate(DateUtils.addDays(certainDate, flex), Calendar.DATE);
    }

    /**
     * 获取指定日期的结束时间
     *
     * @param certainDate
     *          指定日期
     * @param flex
     *          正负整数，正数表示指定日期的后几天，负数表示指定日期的前几天
     * @return
     */
    public static Date getEndTime(Date certainDate, int flex) {
        return DateUtils.addMilliseconds(
                DateUtils.truncate(DateUtils.addDays(certainDate, flex + 1), Calendar.DATE), -1);
    }

    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }

    }

    public static Long parseDateToMinuteStr(Long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp*1000);
        return parseDateToMinuteStr(calendar);
    }
}
