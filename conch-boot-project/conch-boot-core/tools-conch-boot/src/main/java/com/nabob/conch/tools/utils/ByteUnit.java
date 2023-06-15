package com.nabob.conch.tools.utils;


public enum ByteUnit {

    /**
     * Byte unit representing one Byte
     */
    BYTE {
        public long toBytes(long d) {
            return d;
        }

        public long toKB(long d) {
            return d / (C1 / C0);
        }

        public long toMB(long d) {
            return d / (C2 / C0);
        }

        public long toGB(long d) {
            return d / (C3 / C0);
        }

        public long toTB(long d) {
            return d / (C4 / C0);
        }

        public long toPB(long d) {
            return d / (C5 / C0);
        }

        public long toEB(long d) {
            return d / (C6 / C0);
        }

        public long convert(long d, ByteUnit u) {
            return u.toBytes(d);
        }

        int excessNanos(long d, long m) {
            return (int) (d - (m * C2));
        }
    },

    /**
     * Byte unit representing one thousand Bytes
     */
    KB {
        public long toBytes(long d) {
            return x(d, C1 / C0, MAX / (C1 / C0));
        }

        public long toKB(long d) {
            return d;
        }

        public long toMB(long d) {
            return d / (C2 / C1);
        }

        public long toGB(long d) {
            return d / (C3 / C1);
        }

        public long toTB(long d) {
            return d / (C4 / C1);
        }

        public long toPB(long d) {
            return d / (C5 / C1);
        }

        public long toEB(long d) {
            return d / (C6 / C1);
        }

        public long convert(long d, ByteUnit u) {
            return u.toKB(d);
        }

        int excessNanos(long d, long m) {
            return (int) ((d * C1) - (m * C2));
        }
    },

    /**
     * Byte unit representing one thousandth of a second
     */
    MB {
        public long toBytes(long d) {
            return x(d, C2 / C0, MAX / (C2 / C0));
        }

        public long toKB(long d) {
            return x(d, C2 / C1, MAX / (C2 / C1));
        }

        public long toMB(long d) {
            return d;
        }

        public long toGB(long d) {
            return d / (C3 / C2);
        }

        public long toTB(long d) {
            return d / (C4 / C2);
        }

        public long toPB(long d) {
            return d / (C5 / C2);
        }

        public long toEB(long d) {
            return d / (C6 / C2);
        }

        public long convert(long d, ByteUnit u) {
            return u.toMB(d);
        }

        int excessNanos(long d, long m) {
            return 0;
        }
    },

    /**
     * Byte unit representing one second
     */
    GB {
        public long toBytes(long d) {
            return x(d, C3 / C0, MAX / (C3 / C0));
        }

        public long toKB(long d) {
            return x(d, C3 / C1, MAX / (C3 / C1));
        }

        public long toMB(long d) {
            return x(d, C3 / C2, MAX / (C3 / C2));
        }

        public long toGB(long d) {
            return d;
        }

        public long toTB(long d) {
            return d / (C4 / C3);
        }

        public long toPB(long d) {
            return d / (C5 / C3);
        }

        public long toEB(long d) {
            return d / (C6 / C3);
        }

        public long convert(long d, ByteUnit u) {
            return u.toGB(d);
        }

        int excessNanos(long d, long m) {
            return 0;
        }
    },

    /**
     * Byte unit representing sixty seconds
     */
    TB {
        public long toBytes(long d) {
            return x(d, C4 / C0, MAX / (C4 / C0));
        }

        public long toKB(long d) {
            return x(d, C4 / C1, MAX / (C4 / C1));
        }

        public long toMB(long d) {
            return x(d, C4 / C2, MAX / (C4 / C2));
        }

        public long toGB(long d) {
            return x(d, C4 / C3, MAX / (C4 / C3));
        }

        public long toTB(long d) {
            return d;
        }

        public long toPB(long d) {
            return d / (C5 / C4);
        }

        public long toEB(long d) {
            return d / (C6 / C4);
        }

        public long convert(long d, ByteUnit u) {
            return u.toTB(d);
        }

        int excessNanos(long d, long m) {
            return 0;
        }
    },

    /**
     * Byte unit representing sixty minutes
     */
    PB {
        public long toBytes(long d) {
            return x(d, C5 / C0, MAX / (C5 / C0));
        }

        public long toKB(long d) {
            return x(d, C5 / C1, MAX / (C5 / C1));
        }

        public long toMB(long d) {
            return x(d, C5 / C2, MAX / (C5 / C2));
        }

        public long toGB(long d) {
            return x(d, C5 / C3, MAX / (C5 / C3));
        }

        public long toTB(long d) {
            return x(d, C5 / C4, MAX / (C5 / C4));
        }

        public long toPB(long d) {
            return d;
        }

        public long toEB(long d) {
            return d / (C6 / C5);
        }

        public long convert(long d, ByteUnit u) {
            return u.toPB(d);
        }

        int excessNanos(long d, long m) {
            return 0;
        }
    },

    /**
     * Byte unit representing twenty four hours
     */
    EB {
        public long toBytes(long d) {
            return x(d, C6 / C0, MAX / (C6 / C0));
        }

        public long toKB(long d) {
            return x(d, C6 / C1, MAX / (C6 / C1));
        }

        public long toMB(long d) {
            return x(d, C6 / C2, MAX / (C6 / C2));
        }

        public long toGB(long d) {
            return x(d, C6 / C3, MAX / (C6 / C3));
        }

        public long toTB(long d) {
            return x(d, C6 / C4, MAX / (C6 / C4));
        }

        public long toPB(long d) {
            return x(d, C6 / C5, MAX / (C6 / C5));
        }

        public long toEB(long d) {
            return d;
        }

        public long convert(long d, ByteUnit u) {
            return u.toEB(d);
        }

        int excessNanos(long d, long m) {
            return 0;
        }
    };

    // Handy constants for conversion methods
    static final long C0 = 1L;
    static final long C1 = C0 * 1024L;
    static final long C2 = C1 * 1024L;
    static final long C3 = C2 * 1024L;
    static final long C4 = C3 * 1024L;
    static final long C5 = C4 * 1024L;
    static final long C6 = C5 * 1024L;

    static final long MAX = Long.MAX_VALUE;

    /**
     * Scale d by m, checking for overflow.
     * This has a short name to make above code more readable.
     */
    static long x(long d, long m, long over) {
        if (d > over) return Long.MAX_VALUE;
        if (d < -over) return Long.MIN_VALUE;
        return d * m;
    }

    // To maintain full signature compatibility with 1.5, and to improve the
    // clarity of the generated javadoc (see 6287639: Abstract methods in
    // enum classes should not be listed as abstract), method convert
    // etc. are not declared abstract but otherwise act as abstract methods.

    /**
     * Converts the given time bytes in the given unit to this unit.
     * Conversions from finer to coarser granularities truncate, so
     * lose precision. For example, converting {@code 999} milliseconds
     * to seconds results in {@code 0}. Conversions from coarser to
     * finer granularities with arguments that would numerically
     * overflow saturate to {@code Long.MIN_VALUE} if negative or
     * {@code Long.MAX_VALUE} if positive.
     * <p>
     * <p>For example, to convert 10 minutes to milliseconds, use:
     * {@code ByteUnit.MB.convert(10L, ByteUnit.TB)}
     *
     * @param sourceDuration the time bytes in the given {@code sourceUnit}
     * @param sourceUnit     the unit of the {@code sourceDuration} argument
     * @return the converted bytes in this unit,
     * or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     */
    public long convert(long sourceDuration, ByteUnit sourceUnit) {
        throw new AbstractMethodError();
    }

    /**
     * Equivalent to
     * {@link #convert(long, ByteUnit) NANOSECONDS.convert(bytes, this)}.
     *
     * @param bytes the bytes
     * @return the converted bytes,
     * or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     */
    public long toBytes(long bytes) {
        throw new AbstractMethodError();
    }

    /**
     * Equivalent to
     * {@link #convert(long, ByteUnit) KB.convert(bytes, this)}.
     *
     * @param bytes the bytes
     * @return the converted bytes,
     * or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     */
    public long toKB(long bytes) {
        throw new AbstractMethodError();
    }

    /**
     * Equivalent to
     * {@link #convert(long, ByteUnit) MB.convert(bytes, this)}.
     *
     * @param bytes the bytes
     * @return the converted bytes,
     * or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     */
    public long toMB(long bytes) {
        throw new AbstractMethodError();
    }

    /**
     * Equivalent to
     * {@link #convert(long, ByteUnit) GB.convert(bytes, this)}.
     *
     * @param bytes the bytes
     * @return the converted bytes,
     * or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     */
    public long toGB(long bytes) {
        throw new AbstractMethodError();
    }

    /**
     * Equivalent to
     * {@link #convert(long, ByteUnit) TB.convert(bytes, this)}.
     *
     * @param bytes the bytes
     * @return the converted bytes,
     * or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     * @since 1.6
     */
    public long toTB(long bytes) {
        throw new AbstractMethodError();
    }

    /**
     * Equivalent to
     * {@link #convert(long, ByteUnit) PB.convert(bytes, this)}.
     *
     * @param bytes the bytes
     * @return the converted bytes,
     * or {@code Long.MIN_VALUE} if conversion would negatively
     * overflow, or {@code Long.MAX_VALUE} if it would positively overflow.
     * @since 1.6
     */
    public long toPB(long bytes) {
        throw new AbstractMethodError();
    }

    /**
     * Equivalent to
     * {@link #convert(long, ByteUnit) EB.convert(bytes, this)}.
     *
     * @param bytes the bytes
     * @return the converted bytes
     * @since 1.6
     */
    public long toEB(long bytes) {
        throw new AbstractMethodError();
    }

}
