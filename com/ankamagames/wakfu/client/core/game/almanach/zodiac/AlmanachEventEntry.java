package com.ankamagames.wakfu.client.core.game.almanach.zodiac;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.net.soap.data.*;

public class AlmanachEventEntry
{
    private final int m_id;
    private final String m_type;
    private final String m_mobileInfo;
    private final GameDate m_date;
    private final boolean m_recurring;
    private final Color m_color;
    private final boolean m_background;
    private final String m_backgroundUrl;
    private final String m_name;
    private final String m_bossName;
    private final String m_bossText;
    private final String m_bossImageUrl;
    private final String m_ephemeris;
    private final String m_rubrikabrax;
    private final boolean m_showFest;
    private final String m_festText;
    private final int m_weight;
    
    public AlmanachEventEntry(final int id, final String type, final String mobileInfo, final GameDate date, final boolean recurring, final Color color, final boolean background, final String backgroundUrl, final String name, final String bossName, final String bossText, final String bossImageUrl, final String ephemeris, final String rubrikabrax, final boolean showFest, final String festText, final int weight) {
        super();
        this.m_id = id;
        this.m_type = type;
        this.m_mobileInfo = mobileInfo;
        this.m_date = date;
        this.m_recurring = recurring;
        this.m_color = color;
        this.m_background = background;
        this.m_backgroundUrl = backgroundUrl;
        this.m_name = name;
        this.m_bossName = bossName;
        this.m_bossText = bossText;
        this.m_bossImageUrl = bossImageUrl;
        this.m_ephemeris = ephemeris;
        this.m_rubrikabrax = rubrikabrax;
        this.m_showFest = showFest;
        this.m_festText = festText;
        this.m_weight = weight;
    }
    
    public static AlmanachEventEntry createFromSOAP(final MapData mapData) {
        final int id = mapData.getIntValue("id");
        final String type = mapData.getStringValue("type");
        final String mobileInfo = mapData.getStringValue("mobileinfo");
        final GameDate date = AlmanachSOAPHelper.convertToGameDate(PrimitiveConverter.getLong(mapData.getStringValue("date")) * 1000L);
        final boolean recurring = mapData.getBooleanValue("recurring");
        final Data colorData = mapData.getValue("color");
        final Color color = null;
        while (true) {
            if (colorData == null || colorData.getDataType() != DataType.NIL) {
                final boolean bg = mapData.getBooleanValue("background");
                final String bgUrl = mapData.getStringValue("backgroundurl");
                final String name = StringUtils.correctWordChars(mapData.getStringValue("name"));
                final String bossName = StringUtils.correctWordChars(mapData.getStringValue("bossname"));
                final String bossText = StringUtils.correctWordChars(mapData.getStringValue("bosstext"));
                final Data bossImageUrlData = mapData.getValue("bossimageurl");
                final String bossImageUrl = (bossImageUrlData.getDataType() == DataType.BOOLEAN) ? null : mapData.getStringValue("bossimageurl");
                final String ephemeris = StringUtils.correctWordChars(mapData.getStringValue("ephemeris"));
                final String rubrikabrax = StringUtils.correctWordChars(mapData.getStringValue("rubrikabrax"));
                final boolean showfest = PrimitiveConverter.getInteger(mapData.getStringValue("showfest")) != 0;
                final String festtext = StringUtils.correctWordChars(mapData.getStringValue("festtext"));
                final int weight = mapData.getIntValue("weight");
                return new AlmanachEventEntry(id, type, mobileInfo, date, recurring, color, bg, bgUrl, name, bossName, bossText, bossImageUrl, ephemeris, rubrikabrax, showfest, festtext, weight);
            }
            continue;
        }
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public String getType() {
        return this.m_type;
    }
    
    public String getMobileInfo() {
        return this.m_mobileInfo;
    }
    
    public GameDate getDate() {
        return this.m_date;
    }
    
    public boolean isRecurring() {
        return this.m_recurring;
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
    
    public String getBossText() {
        return this.m_bossText;
    }
    
    public String getEphemeris() {
        return this.m_ephemeris;
    }
    
    public String getRubrikabrax() {
        return this.m_rubrikabrax;
    }
    
    public boolean isShowFest() {
        return this.m_showFest;
    }
    
    public String getFestText() {
        return this.m_festText;
    }
    
    public int getWeight() {
        return this.m_weight;
    }
    
    public String getBossName() {
        return this.m_bossName;
    }
    
    public String getBossImageUrl() {
        return this.m_bossImageUrl;
    }
}
