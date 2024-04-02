package cn.micro.unique.util;


import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <a href="https://help.aliyun.com/document_detail/151840.html">...</a>
 */
public final class TraceIdUtil {

    private static String IP_16 = "ffffffff";

    static {
        try {
            String ipAddress = INetUtil.getHostIp();
            if (ipAddress != null) {
                IP_16 = getIP_16(ipAddress);
            }
        } catch (Throwable e) {
            /*
             * empty catch block
             */
        }
    }

    private static final AtomicInteger atomicInteger = new AtomicInteger(999);

    private static int getNextId() {
        for (; ; ) {
            int current = atomicInteger.get();
            int next = (current > 8999) ? 1000 : current + 1;
            if (atomicInteger.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    private static String getIP_16(String ip) {
        String[] ips = ip.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (String column : ips) {
            String hex = Integer.toHexString(Integer.parseInt(column));
            if (hex.length() == 1) {
                sb.append('0').append(hex);
            } else {
                sb.append(hex);
            }

        }
        return sb.toString();
    }

    public static String genTraceId() {
        return IP_16 + System.currentTimeMillis() + getNextId() + getPId();
    }

    private static volatile int pId = -1;

    /**
     * 获得当前进程的PID
     * <p>
     * 当失败时返回-1
     *
     * @return pid
     */
    public static int getPId() {
        if (pId > 0) {
            return pId;
        }
        // something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
        final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        final int index = jvmName.indexOf('@');
        if (index > 0) {
            pId = Integer.parseInt(jvmName.substring(0, index));
            return pId;
        }
        return pId;
    }

}
