package com.nabob.conch.tools.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NetAddressUtil {
    private static NetAddress netAddress = getNetAddress();

    private NetAddressUtil() {
    }

    /**
     * 获取实时的ip
     *
     * @return
     */
    public static NetAddress getNetAddress() {
        netAddress = new NetAddress();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface i = interfaces.nextElement();
                if (i != null) {
                    Enumeration<InetAddress> addresses = i.getInetAddresses();
                    System.out.println(i.getDisplayName());
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        String hostAddr = address.getHostAddress();

                        // local loopback
                        if (hostAddr.indexOf("127.") == 0) {
                            netAddress.loopbackIp = address.getHostAddress();
                            netAddress.loopbackHost = address.getHostName();
                        }

                        // internal ip addresses (behind this router)
                        if (hostAddr.indexOf("192.168") == 0 ||
                                hostAddr.indexOf("10.") == 0 ||
                                hostAddr.indexOf("172.16") == 0) {
                            netAddress.host = address.getHostName();
                            netAddress.ip = address.getHostAddress();
                        }

                        System.out.println("\t\t-" + address.getHostName() + ":" + address.getHostAddress() + " - " + address.getAddress());
                    }
                }
            }
        } catch (SocketException e) {

        }
        try {
            InetAddress loopbackIpAddress = InetAddress.getLocalHost();
            netAddress.loopbackIp = loopbackIpAddress.getHostName();
            System.out.println("LOCALHOST: " + netAddress.loopbackHost);
        } catch (UnknownHostException e) {
            System.err.println("ERR: " + e.toString());
        }
        return netAddress;
    }

    public static String getLoopbackHost() {
        return netAddress.loopbackHost;
    }

    public static String getHost() {
        return netAddress.host;
    }

    public static String getIp() {
        return netAddress.ip;
    }

    public static String getLoopbackIp() {
        return netAddress.loopbackIp;
    }

    public static String getNetAddressString() {
        return netAddress.toString();
    }

    public static class NetAddress {
        private String loopbackHost = "";
        private String host = "";
        private String loopbackIp = "";
        private String ip = "";

        public String getLoopbackHost() {
            return loopbackHost;
        }

        public String getHost() {
            return host;
        }

        public String getLoopbackIp() {
            return loopbackIp;
        }

        public String getIp() {
            return ip;
        }

        @Override
        public String toString() {
            return "{" +
                    "loopbackHost='" + loopbackHost + '\'' +
                    ", host='" + host + '\'' +
                    ", loopbackIp='" + loopbackIp + '\'' +
                    ", ip='" + ip + '\'' +
                    '}';
        }
    }

}