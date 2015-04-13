package com.ankamagames.wakfu.common.configuration;

import java.io.*;
import org.apache.commons.lang3.*;
import gnu.trove.*;
import java.util.*;

final class ToStringConverter
{
    private static final String NULL = "null";
    
    public static String toString(final String key) {
        final SystemConfigurationType confParam = SystemConfigurationType.getByKey(key);
        if (confParam == null) {
            return "null";
        }
        final ValueType type = confParam.getType();
        switch (type) {
            case BOOLEAN: {
                return toStringBoolean(confParam);
            }
            case NUMBER: {
                return toStringNumber(confParam);
            }
            case STRING: {
                return toStringString(confParam);
            }
            case STRINGLIST: {
                return toStringStringList(confParam);
            }
            case NUMBERLIST: {
                return toStringNumberList(confParam);
            }
            default: {
                return "null";
            }
        }
    }
    
    private static String toStringNumberList(final SystemConfigurationType confParam) {
        final TIntArrayList value = SystemConfiguration.INSTANCE.getIntArrayList(confParam);
        return StringUtils.join(value, ",");
    }
    
    private static String toStringStringList(final SystemConfigurationType confParam) {
        final ArrayList<String> value = SystemConfiguration.INSTANCE.getStringArrayList(confParam);
        return StringUtils.join(value, ",");
    }
    
    private static String toStringString(final SystemConfigurationType confParam) {
        return SystemConfiguration.INSTANCE.getStringValue(confParam);
    }
    
    private static String toStringNumber(final SystemConfigurationType confParam) {
        final long longValue = SystemConfiguration.INSTANCE.getLongValue(confParam);
        return Long.toString(longValue);
    }
    
    private static String toStringBoolean(final SystemConfigurationType confParam) {
        final boolean booleanValue = SystemConfiguration.INSTANCE.getBooleanValue(confParam);
        return Boolean.toString(booleanValue);
    }
}
