package com.ankamagames.wakfu.common.constants;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;
import java.text.*;

public class Version extends com.ankamagames.baseImpl.common.clientAndServer.utils.Version
{
    public static final byte MAJOR = 1;
    public static final short MINOR = 39;
    public static final byte REVISION = 4;
    public static final int MIN_REQUIRED_BUILD = -1;
    public static final String READABLE_VERSION;
    public static final String READABLE_DATED_VERSION;
    public static final String BUILD_VERSION;
    public static final Date BUILD_DATE;
    public static final byte[] INTERNAL_VERSION;
    public static final int SERIALIZATION_VERSION;
    protected static final Logger m_logger;
    private static final Version m_version;
    private static final String TEAMCITY_BUILD_DATE = "20141222";
    private static final String TEAMCITY_BUILD_VERSION = "0";
    
    public static void display() {
        Version.m_logger.info((Object)Version.READABLE_DATED_VERSION);
    }
    
    public static String format(final byte[] datas) {
        if (datas == null || datas.length < 4) {
            return "<unknown>";
        }
        final StringBuilder sb = new StringBuilder();
        final ByteBuffer bb = ByteBuffer.wrap(datas);
        final int major = bb.get() & 0xFF;
        final int minor = bb.getShort() & 0xFFFF;
        final int revision = bb.get() & 0xFF;
        sb.append(major).append('.');
        if (minor < 10) {
            sb.append('0');
        }
        sb.append(minor).append('.');
        if (revision < 10) {
            sb.append('0');
        }
        sb.append(revision);
        final int buildLength = bb.get() & 0xFF;
        final byte[] encodedBuildString = new byte[buildLength];
        bb.get(encodedBuildString);
        final String buildString = StringUtils.fromUTF8(encodedBuildString);
        int buildVersion = -1;
        try {
            buildVersion = Integer.parseInt(buildString);
        }
        catch (NumberFormatException e) {
            Version.m_logger.error((Object)("Num\u00e9ro de build au format incorrect: " + buildString));
        }
        if (buildVersion >= 0) {
            sb.append(" build ").append(buildString);
        }
        return sb.toString();
    }
    
    public static void main(final String[] args) {
        System.out.println(Version.SERIALIZATION_VERSION);
    }
    
    @Override
    protected boolean implCheckVersion(final byte[] datas) {
        if (datas == null) {
            Version.m_logger.error((Object)"[implCheckVersion] check null");
            return false;
        }
        if (datas.length < 5) {
            Version.m_logger.error((Object)("[implCheckVersion] wrong data length: " + datas.length));
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(datas);
        return bb.get() == 1 && bb.getShort() == 39 && bb.get() == 4;
    }
    
    @Override
    protected byte[] implGetNeededVersion() {
        return Version.INTERNAL_VERSION;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Version.class);
        m_version = new Version();
        int version = -1;
        if ("0".length() > 0) {
            try {
                version = Integer.parseInt("0");
            }
            catch (NumberFormatException e) {
                Version.m_logger.error((Object)"TEAMCITY_BUILD_VERSION invalide : 0");
            }
        }
        BUILD_VERSION = Integer.toString(version);
        Date date = new Date();
        if ("20141222".length() > 0) {
            try {
                date = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse("20141222");
            }
            catch (ParseException e2) {
                Version.m_logger.error((Object)"TEAMCITY_BUILD_DATE invalide : 20141222");
            }
        }
        BUILD_DATE = date;
        final byte[] encodedRequiredBuild = StringUtils.toUTF8(Integer.toString(-1));
        INTERNAL_VERSION = new byte[5 + encodedRequiredBuild.length];
        final ByteBuffer bb = ByteBuffer.wrap(Version.INTERNAL_VERSION);
        bb.put((byte)1);
        bb.putShort((short)39);
        bb.put((byte)4);
        bb.put((byte)encodedRequiredBuild.length);
        bb.put(encodedRequiredBuild);
        SERIALIZATION_VERSION = 10039004;
        String readableVersion = String.format("%d.%02d.%d", 1, 39, 4);
        if (version != -1) {
            readableVersion += String.format(" (build %s)", Version.BUILD_VERSION);
        }
        READABLE_VERSION = readableVersion;
        READABLE_DATED_VERSION = String.format("%d.%02d (build %s [%4$tY-%4$tm-%4$td @ %4$tHH%4$tMmin%4$tS])", 1, 39, Version.BUILD_VERSION, Version.BUILD_DATE);
    }
}
