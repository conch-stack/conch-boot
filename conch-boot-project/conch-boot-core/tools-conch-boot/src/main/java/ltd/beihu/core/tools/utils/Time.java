package ltd.beihu.core.tools.utils;

import ltd.beihu.core.tools.time.TimeConstant;
import sun.util.calendar.CalendarUtils;

import java.util.*;

/**
 * @Project: [angel]
 * @Package: [util]
 * @Description: [时间类, 擦除年月日]
 * @Author: [toming]
 * @CreateDate: [11/9/16 4:07 PM]
 * @Version: [v1.0]
 */
public class Time implements java.io.Serializable, Cloneable, Comparable<Time> {

    private static Integer offset;//时差

    static {
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = cal.getTimeZone();
        offset = timeZone.getRawOffset();
    }

    private long fastTime;

    public Time() {
        this(System.currentTimeMillis());
    }

    public Time(Date date) {
        this(date == null ? System.currentTimeMillis() : date.getTime());
    }

    public Time(long date) {
        date = date + offset;
        fastTime = date % TimeConstant.ONE_DAY;
    }

    /**
     * Compares two dates for equality.
     * The result is <code>true</code> if and only if the argument is
     * not <code>null</code> and could be cast to a <code>Time</code> object that
     * represents the same point in time, to the millisecond, as this object.
     * <p>
     * Thus, two <code>Date</code> objects are equal if and only if the
     * <code>getTime</code> method returns the same <code>long</code>
     * value for both.
     *
     * @param obj the object to compare with.
     * @return <code>true</code> if the objects are the same;
     * <code>false</code> otherwise.
     * @see Date#getTime()
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Time) {
            return getTime() == ((Time) obj).getTime();
        } else if (obj instanceof Date) {
            return getTime() == new Time((Date) obj).getTime();
        } else if (obj instanceof Long) {
            return getTime() == new Time((Long) obj).getTime();
        } else {
            return false;
        }
    }

    /**
     * Compare with another Time.
     *
     * @param another the <code>Time</code> to be compared.
     * @return the value <code>0</code> if the argument Time is equal to
     * this Time; a value less than <code>0</code> if this Date
     * is before the Time argument; and a value greater than
     * <code>0</code> if this Time is after the Time argument.
     * @throws NullPointerException if <code>another</code> is null.
     */
    @Override
    public int compareTo(Time another) {
        return (this.fastTime < another.fastTime ? -1 : (this.fastTime == another.fastTime ? 0 : 1));
    }

    /**
     * 计算两个时间之间的差
     *
     * @param another -另一个时间
     * @throws NullPointerException if the another is null
     */
    public long sub(Time another) {
        return this.fastTime > another.fastTime ?
                this.fastTime - another.fastTime :
                this.fastTime - another.fastTime + TimeConstant.ONE_DAY;
    }

    public long getTime() {
        return fastTime - offset;
    }

    /**
     * Converts this <code>Time</code> object to a <code>String</code>
     * of the form:
     * <blockquote><pre>
     *  hh:mm:ss SSS</pre></blockquote>
     * where:<ul>
     * <li><tt>hh</tt> is the hour of the day (<tt>00</tt> through
     * <tt>23</tt>), as two decimal digits.
     * <li><tt>mm</tt> is the minute within the hour (<tt>00</tt> through
     * <tt>59</tt>), as two decimal digits.
     * <li><tt>ss</tt> is the second within the minute (<tt>00</tt> through
     * <tt>61</tt>, as two decimal digits.
     * <li><tt>SSS</tt> is the milliseconds within the millisecond (<tt>000</tt> through
     * <tt>999</tt>, as three decimal digits.
     * </ul>
     *
     * @return a string representation of this date.
     * @see Date#toString()
     * @see Date#toLocaleString()
     * @see Date#toGMTString()
     */
    @Override
    public String toString() {
        // "HH:mm:ss SSS";
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(fastTime);
        StringBuilder sb = new StringBuilder(12);

        Integer[] mods = mod();
        CalendarUtils.sprintf0d(sb, mods[0], 2).append(':');   // HH
        CalendarUtils.sprintf0d(sb, mods[1], 2).append(':'); // mm
        CalendarUtils.sprintf0d(sb, mods[2], 2).append(' '); // ss
        CalendarUtils.sprintf0d(sb, mods[3], 3); // SSS
        return sb.toString();
    }

    private long[] units = new long[]{TimeConstant.ONE_HOUR, TimeConstant.ONE_MINUTE, TimeConstant.ONE_SECOND};

    private Integer[] mod() {
        List<Integer> modList = new ArrayList<>();
        long rest = fastTime;
        for (long unit : units) {
            int c = (int) (rest / unit);
            modList.add(c);
            rest = rest - unit * c;
        }
        modList.add((int) rest);
        Integer[] mods = new Integer[units.length];
        return modList.toArray(mods);
    }
}
