package stbvideocall.jhonelee.xyt.com.aini_app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * Created by bakumon on 2016/12/8 17:18.
 */
public class DateUtil {
    public static String dateFormat(Date date) {
        if (date == null) {
            return "unknown";
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        return outputFormat.format(date);

    }
}

