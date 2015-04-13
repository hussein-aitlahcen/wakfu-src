package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.util;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class GameDateFormatter
{
    private static final Logger m_logger;
    
    public static GameDate parse(@NotNull final String format, @NotNull final String date) throws GameDateParseException {
        if (!GameDateChecker.checkForParse(format, date)) {
            throw new GameDateParseException(format, date);
        }
        int y = 0;
        int mo = 1;
        int d = 1;
        int h = 0;
        int m = 0;
        int s = 0;
        final char[] chars = format.toCharArray();
        int Foffset = 0;
        int Doffset = 0;
        while (Foffset < chars.length) {
            char c = chars[Foffset++];
            final int startOffset = Doffset++;
            if (c == GameDateVarConstants.VAR.getChar()) {
                c = chars[Foffset++];
                String v;
                if (Foffset < chars.length) {
                    final int separatorOffset = date.indexOf(chars[Foffset], Doffset);
                    v = date.substring(startOffset, separatorOffset);
                }
                else {
                    v = date.substring(startOffset);
                }
                Doffset += v.length();
                final GameDateVarConstants var = GameDateVarConstants.CHARS.get(c);
                switch (var) {
                    case SHORT_YEAR: {
                        final String yearString = Calendar.getInstance().get(1) / 100 + v;
                        y = Integer.parseInt(yearString);
                        break;
                    }
                    case LONG_YEAR: {
                        y = Integer.parseInt(v);
                        break;
                    }
                    case MONTH: {
                        mo = Integer.parseInt(v);
                        break;
                    }
                    case DAY: {
                        d = Integer.parseInt(v);
                        break;
                    }
                    case HOUR: {
                        h = Integer.parseInt(v);
                        break;
                    }
                    case MINUTE: {
                        m = Integer.parseInt(v);
                        break;
                    }
                    case SECOND: {
                        s = Integer.parseInt(v);
                        break;
                    }
                }
                ++Foffset;
            }
        }
        return new GameDate(s, m, h, d, mo, y);
    }
    
    public static String format(@NotNull final String format, @NotNull final GameDateConst date) {
        final StringBuilder sb = new StringBuilder();
        final char[] chars = format.toCharArray();
        int offset = 0;
        int startCopy = 0;
        while (offset < chars.length) {
            if (chars[offset] == '%') {
                if (offset != startCopy) {
                    sb.append(chars, startCopy, offset - startCopy);
                }
                if (++offset == chars.length) {
                    startCopy = chars.length;
                    break;
                }
                final char nextChar = chars[offset];
                final GameDateVarConstants var = GameDateVarConstants.CHARS.get(nextChar);
                switch (var) {
                    case SHORT_YEAR: {
                        sb.append(date.getYear() % 100);
                        break;
                    }
                    case LONG_YEAR: {
                        sb.append(date.getYear());
                        break;
                    }
                    case MONTH: {
                        final int month = date.getMonth();
                        if (month < 10) {
                            sb.append('0');
                        }
                        sb.append(month);
                        break;
                    }
                    case DAY: {
                        final int day = date.getDay();
                        if (day < 10) {
                            sb.append('0');
                        }
                        sb.append(day);
                        break;
                    }
                    case HOUR: {
                        final int hour = date.getHours();
                        if (hour < 10) {
                            sb.append('0');
                        }
                        sb.append(hour);
                        break;
                    }
                    case MINUTE: {
                        final int minute = date.getMinutes();
                        if (minute < 10) {
                            sb.append('0');
                        }
                        sb.append(minute);
                        break;
                    }
                    case SECOND: {
                        final int second = date.getSeconds();
                        if (second < 10) {
                            sb.append('0');
                        }
                        sb.append(second);
                        break;
                    }
                    case VAR: {
                        sb.append(GameDateVarConstants.VAR.getChar());
                        break;
                    }
                    default: {
                        GameDateFormatter.m_logger.warn((Object)("Caract\u00e8re de formattage inconnu '" + nextChar + "' dans la chaine " + format));
                        break;
                    }
                }
                startCopy = offset + 1;
            }
            ++offset;
        }
        sb.append(chars, startCopy, chars.length - startCopy);
        return sb.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)GameDateFormatter.class);
    }
}
