/*
 * Copyright 2013 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.nezha.channel;

import io.nezha.internal.EmptyArrays;
import io.nezha.internal.PlatformDependent;
import io.nezha.internal.SystemPropertyUtil;
import io.nezha.internal.logging.InternalLogger;
import io.nezha.internal.logging.InternalLoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The default {@link ChannelId} implementation.
 */
public final class DefaultChannelId implements ChannelId {

    private static final long serialVersionUID = 3884076183504074063L;

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultChannelId.class);
    private static final byte[] MACHINE_ID = "hhh".getBytes();
    private static final int PROCESS_ID_LEN = 4;
    private static final int PROCESS_ID = 0;
    private static final int SEQUENCE_LEN = 4;
    private static final int TIMESTAMP_LEN = 8;
    private static final int RANDOM_LEN = 4;

    private static final AtomicInteger nextSequence = new AtomicInteger();

    /**
     * Returns a new {@link DefaultChannelId} instance.
     */
    public static DefaultChannelId newInstance() {
        return new DefaultChannelId();
    }

    static {
        
    }

    private static int defaultProcessId() {
        ClassLoader loader = null;
        String value;
        try {
            loader = PlatformDependent.getClassLoader(DefaultChannelId.class);
            // Invoke java.lang.management.ManagementFactory.getRuntimeMXBean().getName()
            Class<?> mgmtFactoryType = Class.forName("java.lang.management.ManagementFactory", true, loader);
            Class<?> runtimeMxBeanType = Class.forName("java.lang.management.RuntimeMXBean", true, loader);

            Method getRuntimeMXBean = mgmtFactoryType.getMethod("getRuntimeMXBean", EmptyArrays.EMPTY_CLASSES);
            Object bean = getRuntimeMXBean.invoke(null, EmptyArrays.EMPTY_OBJECTS);
            Method getName = runtimeMxBeanType.getMethod("getName", EmptyArrays.EMPTY_CLASSES);
            value = (String) getName.invoke(bean, EmptyArrays.EMPTY_OBJECTS);
        } catch (Throwable t) {
            logger.debug("Could not invoke ManagementFactory.getRuntimeMXBean().getName(); Android?", t);
            try {
                // Invoke android.os.Process.myPid()
                Class<?> processType = Class.forName("android.os.Process", true, loader);
                Method myPid = processType.getMethod("myPid", EmptyArrays.EMPTY_CLASSES);
                value = myPid.invoke(null, EmptyArrays.EMPTY_OBJECTS).toString();
            } catch (Throwable t2) {
                logger.debug("Could not invoke Process.myPid(); not Android?", t2);
                value = "";
            }
        }

        int atIndex = value.indexOf('@');
        if (atIndex >= 0) {
            value = value.substring(0, atIndex);
        }

        int pid;
        try {
            pid = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            // value did not contain an integer.
            pid = -1;
        }

        if (pid < 0) {
            pid = PlatformDependent.threadLocalRandom().nextInt();
            logger.warn("Failed to find the current process ID from '{}'; using a random value: {}",  value, pid);
        }

        return pid;
    }

    private final byte[] data;
    private final int hashCode;

    private transient String shortValue;
    private transient String longValue;

    private DefaultChannelId() {
        data = new byte[MACHINE_ID.length + PROCESS_ID_LEN + SEQUENCE_LEN + TIMESTAMP_LEN + RANDOM_LEN];
        int i = 0;

        // machineId
        System.arraycopy(MACHINE_ID, 0, data, i, MACHINE_ID.length);
        i += MACHINE_ID.length;

        // processId
        i = writeInt(i, PROCESS_ID);

        // sequence
        i = writeInt(i, nextSequence.getAndIncrement());

        // timestamp (kind of)
        i = writeLong(i, Long.reverse(System.nanoTime()) ^ System.currentTimeMillis());

        // random
        int random = PlatformDependent.threadLocalRandom().nextInt();
        i = writeInt(i, random);
        assert i == data.length;

        hashCode = Arrays.hashCode(data);
    }

    private int writeInt(int i, int value) {
        data[i ++] = (byte) (value >>> 24);
        data[i ++] = (byte) (value >>> 16);
        data[i ++] = (byte) (value >>> 8);
        data[i ++] = (byte) value;
        return i;
    }

    private int writeLong(int i, long value) {
        data[i ++] = (byte) (value >>> 56);
        data[i ++] = (byte) (value >>> 48);
        data[i ++] = (byte) (value >>> 40);
        data[i ++] = (byte) (value >>> 32);
        data[i ++] = (byte) (value >>> 24);
        data[i ++] = (byte) (value >>> 16);
        data[i ++] = (byte) (value >>> 8);
        data[i ++] = (byte) value;
        return i;
    }

    @Override
    public String asShortText() {
        String shortValue = this.shortValue;
        
        return shortValue;
    }

    @Override
    public String asLongText() {
        String longValue = this.longValue;
        if (longValue == null) {
            this.longValue = longValue = newLongValue();
        }
        return longValue;
    }

    private String newLongValue() {
        return "";
    }


    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public int compareTo(final ChannelId o) {
        if (this == o) {
            // short circuit
            return 0;
        }
        if (o instanceof DefaultChannelId) {
            // lexicographic comparison
            final byte[] otherData = ((DefaultChannelId) o).data;
            int len1 = data.length;
            int len2 = otherData.length;
            int len = Math.min(len1, len2);

            for (int k = 0; k < len; k++) {
                byte x = data[k];
                byte y = otherData[k];
                if (x != y) {
                    // treat these as unsigned bytes for comparison
                    return (x & 0xff) - (y & 0xff);
                }
            }
            return len1 - len2;
        }

        return asLongText().compareTo(o.asLongText());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DefaultChannelId)) {
            return false;
        }
        DefaultChannelId other = (DefaultChannelId) obj;
        return hashCode == other.hashCode && Arrays.equals(data, other.data);
    }

    @Override
    public String toString() {
        return asShortText();
    }
}
