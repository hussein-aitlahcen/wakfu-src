package com.ankamagames.framework.kernel.utils;

import org.apache.log4j.*;
import com.ankamagames.framework.java.util.*;
import java.util.regex.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.translator.*;

public class StringFormatter
{
    public static final Logger m_logger;
    public static final Pattern CONDITION_GLOBAL_PATTERN;
    public static final Pattern CONDITION_LOCAL_PATTERN;
    public static final Pattern REPLACE_BY_ARGS_PATTERN;
    public static final Pattern REPLACE_BY_CONTEXT_INFO_PATTERN;
    public static final Pattern REPLACE_BY_EXPRESSION_PATTERN;
    protected static Pattern REPLACE_BY_ICON_PATTERN;
    public static byte DEFAULT_GENDER;
    protected static byte m_gender;
    public static String DEFAULT_NAME;
    protected static String m_name;
    protected static String m_nationName;
    protected static String m_breedName;
    public static TextImageProvider m_textImageProvider;
    
    public static byte getGender() {
        return StringFormatter.m_gender;
    }
    
    public static String format(String string, final Object... args) {
        do {
            final StringBuffer formattedString = new StringBuffer();
            final Matcher matcher = StringFormatter.CONDITION_GLOBAL_PATTERN.matcher(string);
            while (matcher.find()) {
                boolean conditionResult = true;
                final String conditionGroup = matcher.group(1);
                final Matcher localMatcher = StringFormatter.CONDITION_LOCAL_PATTERN.matcher(conditionGroup);
                while (localMatcher.find()) {
                    final String group1 = localMatcher.group(1);
                    int conditionConst = 1;
                    if (group1.length() > 0) {
                        conditionConst = PrimitiveConverter.getInteger(group1);
                    }
                    final char conditionChar = localMatcher.group(2).charAt(0);
                    final String group2 = localMatcher.group(3);
                    int conditionArg = -1;
                    if (group2.length() > 0) {
                        conditionArg = PrimitiveConverter.getInteger(group2);
                    }
                    switch (conditionChar) {
                        case '<': {
                            if (args.length >= conditionArg) {
                                conditionResult &= isInferiorTo(args[conditionArg - 1], conditionConst);
                                continue;
                            }
                            continue;
                        }
                        case '>': {
                            if (args.length >= conditionArg) {
                                conditionResult &= isPlural(args[conditionArg - 1], conditionConst);
                                continue;
                            }
                            continue;
                        }
                        case '=': {
                            if (args.length >= conditionArg) {
                                conditionResult &= isEqualTo(args[conditionArg - 1], conditionConst);
                                continue;
                            }
                            continue;
                        }
                        case '~': {
                            conditionResult &= (args.length >= conditionArg && ((args[conditionArg - 1] != null && !isEqualTo(args[conditionArg - 1], 0)) || (args[conditionArg - 1] instanceof String && !isEqualTo((String)args[conditionArg - 1], ""))));
                            continue;
                        }
                        case '!': {
                            conditionResult &= (args.length < conditionArg || args[conditionArg - 1] == null || isEqualTo(args[conditionArg - 1], 0) || (args[conditionArg - 1] instanceof String && isEqualTo((String)args[conditionArg - 1], "")));
                            continue;
                        }
                        case '+': {
                            conditionResult &= (args.length >= conditionArg && args[conditionArg - 1] != null && isSuperiorTo(args[conditionArg - 1], 0, false));
                            continue;
                        }
                        case '-': {
                            conditionResult &= (args.length >= conditionArg && args[conditionArg - 1] != null && !isSuperiorTo(args[conditionArg - 1], 0, false));
                            continue;
                        }
                        case '*': {
                            conditionResult &= isSex(conditionConst);
                            continue;
                        }
                        default: {
                            StringFormatter.m_logger.error((Object)("Impossible de formatter l'expression : " + string));
                            continue;
                        }
                    }
                }
                if (conditionResult) {
                    matcher.appendReplacement(formattedString, matcher.group(3));
                }
                else {
                    matcher.appendReplacement(formattedString, matcher.group(4));
                }
            }
            matcher.appendTail(formattedString);
            string = formattedString.toString();
        } while (StringFormatter.CONDITION_GLOBAL_PATTERN.matcher(string).find());
        Matcher matcher2 = StringFormatter.REPLACE_BY_ARGS_PATTERN.matcher(string);
        StringBuffer formattedString2 = new StringBuffer();
        while (matcher2.find()) {
            final boolean absoluteValue = matcher2.group(1).charAt(0) == '£';
            final int argIndex = PrimitiveConverter.getInteger(matcher2.group(3)) - 1;
            int decimals;
            if (matcher2.groupCount() == 4) {
                decimals = PrimitiveConverter.getInteger(matcher2.group(4));
            }
            else {
                decimals = 0;
            }
            if (args.length > argIndex && args[argIndex] != null) {
                final boolean useThousandSeparator = matcher2.group(2) != null;
                final String argToString = formatArgument(args[argIndex], absoluteValue, decimals, useThousandSeparator);
                matcher2.appendReplacement(formattedString2, argToString);
            }
        }
        matcher2.appendTail(formattedString2);
        matcher2 = StringFormatter.REPLACE_BY_CONTEXT_INFO_PATTERN.matcher(formattedString2.toString());
        formattedString2 = new StringBuffer();
        while (matcher2.find()) {
            final String group3 = matcher2.group(1);
            final ContextInfo infoByName = ContextInfo.getInfoByName(group3);
            if (infoByName == null) {
                StringFormatter.m_logger.error((Object)("Tag inconnu : " + group3));
            }
            else {
                matcher2.appendReplacement(formattedString2, infoByName.getValue());
            }
        }
        matcher2.appendTail(formattedString2);
        final String s = formattedString2.toString();
        matcher2 = StringFormatter.REPLACE_BY_EXPRESSION_PATTERN.matcher(s);
        formattedString2 = new StringBuffer();
        while (matcher2.find()) {
            final String[] expressions = matcher2.group(1).split("\\|");
            final int end = matcher2.end();
            if (s.length() <= end) {
                continue;
            }
            boolean found = false;
            for (final String expression : expressions) {
                if (s.substring(end).startsWith(expression)) {
                    matcher2.appendReplacement(formattedString2, matcher2.group(3));
                    found = true;
                    break;
                }
            }
            if (found) {
                continue;
            }
            matcher2.appendReplacement(formattedString2, matcher2.group(4));
        }
        matcher2.appendTail(formattedString2);
        if (StringFormatter.m_textImageProvider != null) {
            matcher2 = StringFormatter.REPLACE_BY_ICON_PATTERN.matcher(formattedString2.toString());
            formattedString2 = new StringBuffer();
            while (matcher2.find()) {
                final byte iconId = PrimitiveConverter.getByte(matcher2.group(2));
                final String align = matcher2.group(4);
                try {
                    matcher2.appendReplacement(formattedString2, StringFormatter.m_textImageProvider.getIconUrl(iconId, align));
                }
                catch (Exception e) {
                    StringFormatter.m_logger.warn((Object)e.getMessage());
                }
            }
            matcher2.appendTail(formattedString2);
        }
        return formattedString2.toString();
    }
    
    protected static boolean isSex(final int conditionConst) {
        if (conditionConst > 127) {
            StringFormatter.m_logger.error((Object)("Constante trop grande pour le test du sex de l'interlocuteur : " + conditionConst));
            return false;
        }
        return (byte)conditionConst == StringFormatter.m_gender;
    }
    
    protected static boolean isPlural(final Object object, final int constant) {
        return isSuperiorTo(object, constant, true);
    }
    
    protected static boolean isInferiorTo(final Object object, final int value) {
        return isInferiorTo(object, value, true);
    }
    
    protected static boolean isInferiorTo(final Object object, final int value, final boolean ceilValue) {
        Number number = getAsNumber(object);
        if (number == null) {
            return false;
        }
        if (ceilValue) {
            number = Math.ceil(number.doubleValue());
        }
        return number.doubleValue() < value;
    }
    
    protected static boolean isSuperiorTo(final Object object, final int value, final boolean ceilValue) {
        Number number = getAsNumber(object);
        if (number == null) {
            return false;
        }
        if (ceilValue) {
            number = Math.ceil(number.doubleValue());
        }
        return number.doubleValue() > value;
    }
    
    protected static boolean isEqualTo(final Object object, final int value) {
        final Number number = getAsNumber(object);
        return number != null && number.doubleValue() == value;
    }
    
    protected static boolean isEqualTo(final String object, final String value) {
        return object.equals(value);
    }
    
    protected static String formatArgument(Object arg, final boolean useAbsolutevalue, final int decimalsCount, final boolean useThousandSeparator) {
        if (arg instanceof String) {
            if (!useAbsolutevalue && decimalsCount <= 0) {
                return arg.toString();
            }
            arg = PrimitiveConverter.getDouble(arg);
        }
        if (!(arg instanceof Number)) {
            return arg.toString();
        }
        double value = ((Number)arg).doubleValue();
        if (decimalsCount > 0 && arg instanceof RoundedNumber) {
            value = ((RoundedNumber)arg).getNotRoundedValue();
        }
        if (decimalsCount == 0) {
            value = MathHelper.fastRound(value);
        }
        else if (decimalsCount > 0) {
            value = MathHelper.round(value, decimalsCount);
        }
        if (useAbsolutevalue) {
            value = Math.abs(value);
        }
        return Translator.getInstance().formatNumber(value);
    }
    
    public static void setGender(final byte gender) {
        StringFormatter.m_gender = gender;
    }
    
    public static void setName(final String name) {
        StringFormatter.m_name = name;
    }
    
    public static void setNationName(final String nationName) {
        StringFormatter.m_nationName = nationName;
    }
    
    public static void setBreedName(final String breedName) {
        StringFormatter.m_breedName = breedName;
    }
    
    protected static Number getAsNumber(final Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return (Number)obj;
        }
        if (obj instanceof String) {
            return PrimitiveConverter.getDouble(obj, 0.0);
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)StringFormatter.class);
        CONDITION_GLOBAL_PATTERN = Pattern.compile("\\{((\\[[^\\[\\]\\{\\}?:]*\\])+)\\s*\\?([^\\{\\}]*):([^\\{\\}]*)\\}");
        CONDITION_LOCAL_PATTERN = Pattern.compile("\\[([^\\[\\]]*)([~\\*\\+\\-><!=])([^\\[\\]]*)\\]");
        REPLACE_BY_ARGS_PATTERN = Pattern.compile("\\[((,)*[\\£#])([0-9]+)(?:.([0-9]+))?\\]");
        REPLACE_BY_EXPRESSION_PATTERN = Pattern.compile("((\\|.*)+)\\?([^\\|]*):([^\\|]*)\\|");
        String regex = "\\[#(";
        for (final ContextInfo ci : ContextInfo.values()) {
            if (ci != ContextInfo.values()[0]) {
                regex += "|";
            }
            regex += ci.getInfoName();
        }
        regex += ")\\]";
        REPLACE_BY_CONTEXT_INFO_PATTERN = Pattern.compile(regex);
        StringFormatter.REPLACE_BY_ICON_PATTERN = Pattern.compile("\\[(#icon)\\s*([0-9]+)(\\s*:\\s*(west|east|center))*\\]");
        StringFormatter.DEFAULT_GENDER = 0;
        StringFormatter.m_gender = StringFormatter.DEFAULT_GENDER;
        StringFormatter.DEFAULT_NAME = "Unknown";
        StringFormatter.m_name = StringFormatter.DEFAULT_NAME;
        StringFormatter.m_nationName = StringFormatter.DEFAULT_NAME;
        StringFormatter.m_breedName = StringFormatter.DEFAULT_NAME;
        StringFormatter.m_textImageProvider = null;
    }
    
    private enum ContextInfo
    {
        NAME("name") {
            @Override
            public String getValue() {
                return StringFormatter.m_name;
            }
        }, 
        NATION_NAME("nationName") {
            @Override
            public String getValue() {
                return StringFormatter.m_nationName;
            }
        }, 
        BREED_NAME("breedName") {
            @Override
            public String getValue() {
                return StringFormatter.m_breedName;
            }
        };
        
        private String m_infoName;
        
        private ContextInfo(final String infoName) {
            this.m_infoName = infoName;
        }
        
        public String getInfoName() {
            return this.m_infoName;
        }
        
        public abstract String getValue();
        
        public static ContextInfo getInfoByName(final String infoName) {
            for (final ContextInfo ci : values()) {
                if (ci.getInfoName().equals(infoName)) {
                    return ci;
                }
            }
            return null;
        }
    }
}
