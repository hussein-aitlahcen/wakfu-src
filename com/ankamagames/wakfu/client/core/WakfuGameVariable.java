package com.ankamagames.wakfu.client.core;

import org.apache.log4j.*;
import java.util.regex.*;

public class WakfuGameVariable
{
    private static final Logger m_logger;
    private static final boolean DEBUG = true;
    private static final Pattern VARIABLE_PATTERN;
    private static final WakfuGameVariable m_instance;
    
    public static WakfuGameVariable getInstance() {
        return WakfuGameVariable.m_instance;
    }
    
    private static String getVariableValue(final String varKey) {
        assert varKey != null;
        try {
            return "<b>" + Var.valueOf(varKey).getValue() + "</b>";
        }
        catch (Exception e) {
            WakfuGameVariable.m_logger.error((Object)("la variable est inconnu " + varKey), (Throwable)e);
            return varKey;
        }
    }
    
    public static String replace(final String text) {
        final Matcher matcher = WakfuGameVariable.VARIABLE_PATTERN.matcher(text);
        final StringBuilder sb = new StringBuilder(text.length());
        while (matcher.find()) {
            final String varName = matcher.group(2);
            if (varName == null) {
                sb.append(matcher.group(3));
            }
            else {
                sb.append(getVariableValue(varName));
            }
        }
        return sb.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuGameVariable.class);
        VARIABLE_PATTERN = Pattern.compile("(\\\\\\$([a-zA-Z_]+)\\$)|([^(\\\\\\$([a-zA-Z_]+)\\$)]*)");
        m_instance = new WakfuGameVariable();
    }
    
    private enum Var
    {
        PLAYER_NAME {
            @Override
            String getValue() {
                return WakfuGameEntity.getInstance().getLocalPlayer().getName();
            }
        }, 
        PLAYER_BREED {
            @Override
            String getValue() {
                return WakfuGameEntity.getInstance().getLocalPlayer().getBreedInfo().getName();
            }
        };
        
        abstract String getValue();
    }
}
