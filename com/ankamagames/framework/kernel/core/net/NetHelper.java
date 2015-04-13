package com.ankamagames.framework.kernel.core.net;

public class NetHelper
{
    public static final long INVALID_IP = -1L;
    
    public static long ip2long(final byte[] baddr) {
        if (baddr.length == 4) {
            long laddr = 0L;
            laddr |= (0xFFL & baddr[0]) << 24;
            laddr |= (0xFFL & baddr[1]) << 16;
            laddr |= (0xFFL & baddr[2]) << 8;
            laddr |= (0xFFL & baddr[3]);
            return laddr;
        }
        return -1L;
    }
}
