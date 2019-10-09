package ltd.beihu.core.tools.host;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * IpUtil
 */
public class IpUtil {

    private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);

    private IpUtil() {
    }

    /**
     * 此方法描述的是：获得服务器的IP地址
     */
    public static String getLocalIP() {
        String sIP = "";
        InetAddress ip = null;
        try {
            boolean bFindIP = false;
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                if (bFindIP) {
                    break;
                }
                NetworkInterface ni = netInterfaces
                        .nextElement();

                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = ips.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().matches(
                            "(\\d{1,3}\\.){3}\\d{1,3}")) {
                        bFindIP = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获得服务器的IP地址出错:{}", e.getMessage());
        }
        if (null != ip) {
            sIP = ip.getHostAddress();
        }
        return sIP;
    }

    /**
     * 此方法描述的是：获得服务器的IP地址(多网卡)
     */
    public static List<String> getLocalIPS() {
        InetAddress ip;
        List<String> ipList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces
                        .nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = ips.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().matches(
                            "(\\d{1,3}\\.){3}\\d{1,3}")) {
                        ipList.add(ip.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获得服务器的IP地址(多网卡)出错:{}", e.getMessage());
        }
        return ipList;
    }


    /**
     * 获得服务器的IP地址（指定网卡）
     *  TODO
     * @param ethname 网卡名称
     * @return IP地址
     */
    @Deprecated
    public static String getIp(String ethname) {
        String sIP = "";
        InetAddress ip = null;
        try {
            boolean bFindIP = false;
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                if (bFindIP) {
                    break;
                }
                NetworkInterface ni = netInterfaces
                        .nextElement();

                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = ips.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().matches(
                            "(\\d{1,3}\\.){3}\\d{1,3}")) {
                        bFindIP = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获得服务器的IP地址出错:{}", e.getMessage());
        }
        if (null != ip) {
            sIP = ip.getHostAddress();
        }
        return sIP;
    }

    /**
     * 此方法描述的是：获得服务器的MAC地址
     */
    public static String getMacId() {
        String macId = "";
        InetAddress ip = null;
        NetworkInterface ni = null;
        try {
            boolean bFindIP = false;
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                if (bFindIP) {
                    break;
                }
                ni = netInterfaces
                        .nextElement();
                // ----------特定情况，可以考虑用ni.getName判断
                // 遍历所有ip
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = ips.nextElement();
                    if (!ip.isLoopbackAddress() // 非127.0.0.1
                            && ip.getHostAddress().matches(
                            "(\\d{1,3}\\.){3}\\d{1,3}")
                            && ni.getHardwareAddress() != null) {
                        bFindIP = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获得服务器的MAC地址出错:{}", e.getMessage());
        }
        if (null != ip) {
            try {
                macId = getMacFromBytes(ni.getHardwareAddress());
            } catch (SocketException e) {
                logger.error("获得服务器的MAC地址出错:{}", e.getMessage());
            }
        }
        return macId;
    }

    /**
     * 此方法描述的是：获得服务器的MAC地址(多网卡)
     */
    public static List<String> getMacIds() {
        InetAddress ip = null;
        NetworkInterface ni = null;
        List<String> macList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                ni = netInterfaces
                        .nextElement();
                // ----------特定情况，可以考虑用ni.getName判断
                // 遍历所有ip
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = ips.nextElement();
                    if (!ip.isLoopbackAddress() // 非127.0.0.1
                            && ip.getHostAddress().matches(
                            "(\\d{1,3}\\.){3}\\d{1,3}")) {
                        if (ni.getHardwareAddress() != null) {
                            //为虚拟网卡
                            macList.add(getMacFromBytes(ni.getHardwareAddress()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获得服务器的MAC地址(多网卡)出错:{}", e.getMessage());
        }
        return macList;
    }

    private static String getMacFromBytes(byte[] bytes) {
        StringBuffer mac = new StringBuffer();
        byte currentByte;
        boolean first = false;
        for (byte b : bytes) {
            if (first) {
                mac.append("-");
            }
            currentByte = (byte) ((b & 240) >> 4);
            mac.append(Integer.toHexString(currentByte));
            currentByte = (byte) (b & 15);
            mac.append(Integer.toHexString(currentByte));
            first = true;
        }
        return mac.toString().toUpperCase();
    }

}