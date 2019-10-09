package ltd.beihu.core.boot.autoconfigure.antiscrapy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "beihu.anti.security.strategy")
@Validated
public class AntiSecurityProperties {

    private boolean enable = false;

    @NotBlank
    private String namespace;

    /**
     * strategy for hour
     */
    private Hour hour;

    /**
     * strategy for day
     */
    private Day day;

    public static class Day {

        /**
         * ip strategy
         */
        private int ip = 10;

        /**
         * ip and ua strategy
         */
        private int ipAndUa = 5;

        /**
         * phone strategy
         */
        private int phone = 10;

        public int getIp() {
            return ip;
        }
        public void setIp(int ip) {
            this.ip = ip;
        }
        public int getIpAndUa() {
            return ipAndUa;
        }
        public void setIpAndUa(int ipAndUa) {
            this.ipAndUa = ipAndUa;
        }
        public int getPhone() {
            return phone;
        }
        public void setPhone(int phone) {
            this.phone = phone;
        }
    }

    public static class Hour {

        /**
         * ip strategy
         */
        private int ip = 3;

        /**
         * ip and ua strategy
         */
        private int ipAndUa = 2;

        /**
         * phone strategy
         */
        private int phone = 3;

        public int getIp() {
            return ip;
        }
        public void setIp(int ip) {
            this.ip = ip;
        }
        public int getIpAndUa() {
            return ipAndUa;
        }
        public void setIpAndUa(int ipAndUa) {
            this.ipAndUa = ipAndUa;
        }
        public int getPhone() {
            return phone;
        }
        public void setPhone(int phone) {
            this.phone = phone;
        }
    }

    public String getNamespace() {
        return namespace;
    }
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    public boolean isEnable() {
        return enable;
    }
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    public Hour getHour() {
        return hour;
    }
    public void setHour(Hour hour) {
        this.hour = hour;
    }
    public Day getDay() {
        return day;
    }
    public void setDay(Day day) {
        this.day = day;
    }
}
