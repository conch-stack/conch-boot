package com.nabob.conch.tools.host;

import java.util.List;

public class HostInfo {
    private String hostname;
    private List<String> ipv4;
    private List<String> ipv6;
    private String mac;
    private String javaVersion;
    private String osName;
    private String osArch;
    private String osVersion;

    public HostInfo() {
    }

    public HostInfo(String hostname, List<String> ipv4, List<String> ipv6, String mac, String javaVersion, String osName, String osArch, String osVersion) {
        this.hostname = hostname;
        this.ipv4 = ipv4;
        this.ipv6 = ipv6;
        this.mac = mac;
        this.javaVersion = javaVersion;
        this.osName = osName;
        this.osArch = osArch;
        this.osVersion = osVersion;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<String> getIpv4() {
        return ipv4;
    }

    public void setIpv4(List<String> ipv4) {
        this.ipv4 = ipv4;
    }

    public List<String> getIpv6() {
        return ipv6;
    }

    public void setIpv6(List<String> ipv6) {
        this.ipv6 = ipv6;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsArch() {
        return osArch;
    }

    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
}

