package cn.micro.trace.service.impl;

import cn.micro.trace.service.TraceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 默认跟踪实现
 */
public class DefaultTraceServiceImpl implements TraceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceService.class);

    @Override
    public void request(String url
            , String method
            , Map<String, List<String>> headers
            , Map<String, List<String>> parameters
            , byte[] reqBody
            , int rspStatus
            , byte[] rspBody
            , String serviceMethod
            , long cost) {
        LOGGER.info("{\"biz\":\"请求埋点\",\"status\":" + rspStatus + ",\"url\":\"" + url + "\",\"method\":\"" + method + "\",\"params\":\"" + parameters.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, kv -> String.join(",", kv.getValue()))).entrySet().stream().map(kv -> (kv.getKey() + "=" + kv.getValue())).collect(Collectors.joining("&")) + "\",\"payload\":" + (reqBody.length > 0 ? formatBytes(reqBody) : "\"\"") + ",\"resp\":" + (rspBody.length > 0 ? formatBytes(rspBody) : "\"\"") + ",\"api\":\"" + serviceMethod + "\",\"cost\":\"" + format(cost) + "\"}");
    }

    /**
     * 格式化执行时间，单位为 ms 和 s，保留三位小数
     *
     * @param nanos 纳秒
     * @return 格式化后的时间
     */
    public static String format(long nanos) {
        if (nanos < 1) {
            return "0ms";
        }
        double millis = (double) nanos / (1000 * 1000);
        // 不够 1 ms，最小单位为 ms
        if (millis > 1000) {
            return String.format("%.3fs", millis / 1000);
        } else {
            return String.format("%.3fms", millis);
        }
    }

    private static final String[] UNIT_NAMES = new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB"};

    /**
     * 格式化数据，数据是 {} 或者[] 首尾 格式，输出，否者返回可读大小
     * 数据大小大于20k,不输出原数据
     * 可读的文件大小<br>
     * 参考 http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
     *
     * @param bytes 数据
     * @return 大小 | 数据
     */
    public static String formatBytes(byte[] bytes) {
        long size = bytes.length;
        int lastIndex = (int) (size - 1);
        if (size == 1
                || (size < 20480 && bytes[0] == 123 && bytes[lastIndex] == 125)
                || (size < 20480 && bytes[0] == 91 && bytes[lastIndex] == 93)) {
            return new String(bytes);
        }
        int digitGroups = Math.min(UNIT_NAMES.length - 1, (int) (Math.log10(size) / Math.log10(1024)));
        return ("\"" + new DecimalFormat("#,##0.##")
                .format(size / Math.pow(1024, digitGroups)) + " " + UNIT_NAMES[digitGroups] + "\"");
    }
}
