package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.util;

import java.text.*;
import java.util.regex.*;

public class GameDateParseException extends ParseException
{
    private static final String DEFAULT_MSG = "Erreur au parse d'une date";
    private static final String VAR_PATTERN;
    private static final String WRONG_VAR_PATTERN;
    private final String m_format;
    private final String m_date;
    
    public GameDateParseException(final String format, final String date) {
        super("Erreur au parse d'une date", -1);
        this.m_format = format;
        this.m_date = date;
    }
    
    public String getFormat() {
        return this.m_format;
    }
    
    public String getDate() {
        return this.m_date;
    }
    
    @Override
    public String getMessage() {
        Matcher matcher = Pattern.compile(GameDateParseException.WRONG_VAR_PATTERN).matcher(this.m_format);
        if (matcher.find()) {
            return "Variable " + matcher.group() + " inconnue dans le FORMAT [ " + this.m_format + " ]";
        }
        matcher = Pattern.compile(GameDateParseException.VAR_PATTERN + GameDateParseException.VAR_PATTERN).matcher(this.m_format);
        if (matcher.find()) {
            return "Variables " + matcher.group() + " sans s\u00e9parateur dans le FORMAT [ " + this.m_format + " ]";
        }
        if (!Pattern.compile(GameDateChecker.getPattern(this.m_format)).matcher(this.m_date).matches()) {
            return "DATE [" + this.m_date + "] incompatible avec le FORMAT [ " + this.m_format + " ]";
        }
        return "Parse impossible pour le FORMAT [ " + this.m_format + " ] et la DATE [ " + this.m_date + " ]";
    }
    
    static {
        VAR_PATTERN = GameDateVarConstants.VAR.getChar() + "[" + GameDateVarConstants.VARS + "]";
        WRONG_VAR_PATTERN = GameDateVarConstants.VAR.getChar() + "[^" + GameDateVarConstants.VARS + "]";
    }
}
