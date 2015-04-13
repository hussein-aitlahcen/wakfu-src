package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.util;

import java.util.*;

public enum GameDateVarConstants
{
    VAR('%', ""), 
    SHORT_YEAR('y', "0*[0-9]{2}"), 
    LONG_YEAR('Y', "0*[0-9]{4}"), 
    MONTH('M', "0*[0-9]{1,2}"), 
    DAY('d', "0*[0-9]{1,2}"), 
    HOUR('h', "0*[0-9]{1,2}"), 
    MINUTE('m', "0*[0-9]{1,2}"), 
    SECOND('s', "0*[0-9]{1,2}");
    
    public static final HashMap<Character, GameDateVarConstants> CHARS;
    public static String VARS;
    private final char m_char;
    private final String m_pattern;
    
    private GameDateVarConstants(final char c, final String pattern) {
        this.m_char = c;
        this.m_pattern = pattern;
    }
    
    public char getChar() {
        return this.m_char;
    }
    
    public String getPattern() {
        return this.m_pattern;
    }
    
    static {
        CHARS = new HashMap<Character, GameDateVarConstants>();
        GameDateVarConstants.VARS = "";
        for (int i = 0; i < values().length; ++i) {
            final GameDateVarConstants constants = values()[i];
            final char c = constants.m_char;
            GameDateVarConstants.CHARS.put(c, constants);
            if (constants != GameDateVarConstants.VAR) {
                GameDateVarConstants.VARS += c;
            }
        }
    }
}
