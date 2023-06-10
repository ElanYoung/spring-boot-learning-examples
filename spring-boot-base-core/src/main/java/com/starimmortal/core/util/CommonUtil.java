package com.starimmortal.core.util;

import com.starimmortal.core.constant.PatternConstant;
import com.starimmortal.core.constant.StringConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Random;

/**
 * 通用工具类
 *
 * @author william@StarImmortal
 * @date 2021/02/08
 */
@Component
@Slf4j
public class CommonUtil {
    /**
     * 请求头
     */
    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    /**
     * 金额单位元转化为分
     * stripTrailingZeros：移除所有尾部零
     *
     * @param p 金额（单位：元）
     * @return 金额（单位：分）
     */
    public static BigDecimal yuanToFenPlain(BigDecimal p) {
        p = p.multiply(new BigDecimal("100"));
        return p.stripTrailingZeros();
    }

    /**
     * 获取本机ip地址
     *
     * @return ip地址
     */
    public static String getRemoteRealIp(HttpServletRequest request) {
        if (request == null) {
            if (null == RequestContextHolder.getRequestAttributes()) {
                return null;
            }
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }

        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                log.debug("ipList.split(\",\")[0]::{}", ipList.split(",")[0]);
                return ipList.split(",")[0];
            }
        }

        log.debug("request.getRemoteAddress()::{}", request.getRemoteAddr());
        return request.getRemoteAddr();
    }

    /**
     * 十位时间戳
     *
     * @return timestamp
     */
    public static String tenTimestamp() {
        long timestamp = Calendar.getInstance().getTimeInMillis();
        return Long.toString(timestamp).substring(0, Long.toString(timestamp).length() - 3);
    }

    /**
     * 常规自动日期格式识别
     *
     * @param str 时间字符串
     * @return Date
     */
    public static String getDateFormat(String str) {
        boolean year = PatternConstant.DATE_PATTERN.matcher(str.substring(0, 4)).matches();
        StringBuilder sb = new StringBuilder();
        int index = 0;
        if (!year) {
            if (str.contains("月") || str.contains(StringConstant.LINE_SEPARATOR) || str.contains(StringConstant.SEPARATOR)) {
                if (Character.isDigit(str.charAt(0))) {
                    index = 1;
                }
            } else {
                index = 3;
            }
        }
        for (int i = 0; i < str.length(); i++) {
            char chr = str.charAt(i);
            if (Character.isDigit(chr)) {
                if (index == 0) {
                    sb.append("y");
                }
                if (index == 1) {
                    sb.append("M");
                }
                if (index == 2) {
                    sb.append("d");
                }
                if (index == 3) {
                    sb.append("H");
                }
                if (index == 4) {
                    sb.append("m");
                }
                if (index == 5) {
                    sb.append("s");
                }
                if (index == 6) {
                    sb.append("S");
                }
            } else {
                if (i > 0) {
                    char lastChar = str.charAt(i - 1);
                    if (Character.isDigit(lastChar)) {
                        index++;
                    }
                }
                sb.append(chr);
            }
        }
        return sb.toString();
    }

    /**
     * 生成指定位数的随机数字字符串
     *
     * @param length 字符串长度
     * @return 随机数字字符串
     */
    public static String randomNumbers(int length) {
        Random random = new Random();
        StringBuilder randomNumbers = new StringBuilder();
        for (int i = 0; i < length; i++) {
            randomNumbers.append(random.nextInt(10));
        }
        return randomNumbers.toString();
    }
}
