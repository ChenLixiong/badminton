package com.ymq.badminton_rank.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenlixiong on 2016/11/27.
 */

public class CommonUtils {
    public static String refFormatNowDate(long currentTimeMillis) {
        Date nowTime = new Date(currentTimeMillis);
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String retStrFormatNowDate = sdFormatter.format(nowTime);
        return retStrFormatNowDate;
    }
}
