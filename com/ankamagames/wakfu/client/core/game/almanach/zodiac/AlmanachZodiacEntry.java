package com.ankamagames.wakfu.client.core.game.almanach.zodiac;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.net.soap.data.*;

public class AlmanachZodiacEntry
{
    private final int m_id;
    private final GameDate m_begin;
    private final GameDate m_end;
    private final Color m_color;
    private final boolean m_background;
    private final String m_backgroundUrl;
    private final String m_name;
    private final String m_description;
    private final String m_imageUrl;
    
    private AlmanachZodiacEntry(final int id, final GameDate begin, final GameDate end, final Color color, final boolean background, final String backgroundUrl, final String name, final String description, final String imageUrl) {
        super();
        this.m_id = id;
        this.m_begin = begin;
        this.m_end = end;
        this.m_color = color;
        this.m_background = background;
        this.m_backgroundUrl = backgroundUrl;
        this.m_name = name;
        this.m_description = description;
        this.m_imageUrl = imageUrl;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public GameDate getBegin() {
        return this.m_begin;
    }
    
    public GameDate getEnd() {
        return this.m_end;
    }
    
    public Color getColor() {
        return this.m_color;
    }
    
    public boolean isBackground() {
        return this.m_background;
    }
    
    public String getBackgroundUrl() {
        return this.m_backgroundUrl;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public String getImageUrl() {
        return this.m_imageUrl;
    }
    
    public boolean isDateValid(final GameDateConst date) {
        final boolean validInFirstMonth = this.m_begin.getMonth() == date.getMonth() && this.m_begin.getDay() <= date.getDay();
        final boolean validInSecondMonth = this.m_end.getMonth() == date.getMonth() && this.m_end.getDay() >= date.getDay();
        return validInFirstMonth || validInSecondMonth;
    }
    
    public static AlmanachZodiacEntry createFromSOAP(final MapData zodiacMap) {
        final int id = zodiacMap.getIntValue("id");
        final GameDate begin = AlmanachSOAPHelper.convertToGameDate(PrimitiveConverter.getLong(zodiacMap.getStringValue("begin")) * 1000L);
        final GameDate end = AlmanachSOAPHelper.convertToGameDate(PrimitiveConverter.getLong(zodiacMap.getStringValue("end")) * 1000L);
        final Data colorData = zodiacMap.getValue("color");
        final Color color = null;
        while (true) {
            if (colorData == null || colorData.getDataType() != DataType.NIL) {
                final boolean bg = zodiacMap.getBooleanValue("background");
                final String bgUrl = zodiacMap.getStringValue("backgroundurl");
                final String name = StringUtils.correctWordChars(zodiacMap.getStringValue("name"));
                final String description = StringUtils.correctWordChars(zodiacMap.getStringValue("description"));
                final Data imageUrlData = zodiacMap.getValue("imageurl");
                final String imageUrl = (imageUrlData.getDataType() == DataType.BOOLEAN) ? null : zodiacMap.getStringValue("imageurl");
                return new AlmanachZodiacEntry(id, begin, end, color, bg, bgUrl, name, description, imageUrl);
            }
            continue;
        }
    }
}
