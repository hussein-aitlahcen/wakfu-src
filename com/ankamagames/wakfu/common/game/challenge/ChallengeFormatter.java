package com.ankamagames.wakfu.common.game.challenge;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import com.ankamagames.framework.kernel.utils.*;

public final class ChallengeFormatter
{
    private static final String LINE_SEPARATOR = "~";
    private static final String SEPARATOR = "|";
    private static final String NAME_PARAMETER_TAG = "name";
    private static final String VALUE_PARAMETER_TAG = "value";
    
    public static Object extractValue(final String baseValue, final Type type, final ArrayList<ObjectPair<String, String>> vars) {
        if (baseValue == null) {
            return null;
        }
        String value = baseValue;
        if (vars != null) {
            for (final ObjectPair<String, String> var : vars) {
                value = value.replaceAll("(?i)(\\$\\$" + var.getFirst() + ")", var.getSecond());
            }
        }
        switch (type) {
            case SHORT: {
                return Short.parseShort(value);
            }
            case INTEGER: {
                return Integer.parseInt(value);
            }
            case LONG: {
                return Long.parseLong(value);
            }
            case STRING: {
                return value;
            }
            default: {
                throw new IllegalArgumentException("Pas de type connu ? WTF ?");
            }
        }
    }
    
    public static Object[] extractArrayValues(final String[] baseValues, final Type type, final ArrayList<ObjectPair<String, String>> vars) {
        if (baseValues == null) {
            return null;
        }
        Object[] values = null;
        switch (type) {
            case SHORT: {
                values = new Short[baseValues.length];
                break;
            }
            case INTEGER: {
                values = new Integer[baseValues.length];
                break;
            }
            case LONG: {
                values = new Long[baseValues.length];
                break;
            }
            case STRING: {
                values = new String[baseValues.length];
                break;
            }
            default: {
                throw new IllegalArgumentException("Pas de type connu ? WTF ?");
            }
        }
        for (int i = 0; i < baseValues.length; ++i) {
            values[i] = extractValue(baseValues[i], type, vars);
        }
        return values;
    }
    
    public static ArrayList<ObjectPair<String, String>> extractInstanceParam(final String representation) {
        final ArrayList<ObjectPair<String, String>> params = new ArrayList<ObjectPair<String, String>>();
        final String[] arr$;
        final String[] lines = arr$ = StringUtils.split(representation, "~");
        for (String line : arr$) {
            if (line.length() != 0) {
                if (line.endsWith("|")) {
                    line += " ";
                }
                final String[] values = StringUtils.split(line, "\\|");
                final ObjectPair<String, String> param = new ObjectPair<String, String>();
                for (int i = 0; i < values.length; i += 2) {
                    if (values[i].equalsIgnoreCase("name")) {
                        param.setFirst((values[i + 1] == null) ? "" : values[i + 1]);
                    }
                    else if (values[i].equalsIgnoreCase("value")) {
                        param.setSecond((values[i + 1] == null) ? "" : values[i + 1]);
                    }
                }
                params.add(param);
            }
        }
        return params;
    }
    
    public enum Type
    {
        INTEGER, 
        LONG, 
        SHORT, 
        STRING, 
        STRING_ARRAY;
    }
}
