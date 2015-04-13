package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.util;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.util.regex.*;

public class GameDateChecker
{
    public static boolean checkForParse(final String format, final String date) {
        return CalendarConstant.FORMAT_PATTERN.matcher(format).matches() && Pattern.compile(getPattern(format)).matcher(date).matches();
    }
    
    static String getPattern(final String format) {
        String datePattern = format;
        for (final GameDateVarConstants var : GameDateVarConstants.values()) {
            datePattern = datePattern.replaceAll(String.valueOf(GameDateVarConstants.VAR.getChar()) + var.getChar(), var.getPattern());
        }
        return datePattern;
    }
}
