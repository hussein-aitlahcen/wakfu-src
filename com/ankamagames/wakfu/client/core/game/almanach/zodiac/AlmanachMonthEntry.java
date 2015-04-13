package com.ankamagames.wakfu.client.core.game.almanach.zodiac;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.net.soap.data.*;

public class AlmanachMonthEntry
{
    private final int m_id;
    private final int m_month;
    private final Color m_color;
    private final boolean m_background;
    private final String m_backgroundUrl;
    private final String m_protectorName;
    private final String m_protectorDesc;
    private final String m_protectorImageUrl;
    
    public AlmanachMonthEntry(final int id, final int month, final Color color, final boolean background, final String backgroundUrl, final String protectorName, final String protectorDesc, final String protectorImageUrl) {
        super();
        this.m_id = id;
        this.m_month = month;
        this.m_color = color;
        this.m_background = background;
        this.m_backgroundUrl = backgroundUrl;
        this.m_protectorName = protectorName;
        this.m_protectorDesc = protectorDesc;
        this.m_protectorImageUrl = protectorImageUrl;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public int getMonth() {
        return this.m_month;
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
    
    public String getProtectorName() {
        return this.m_protectorName;
    }
    
    public String getProtectorDesc() {
        return this.m_protectorDesc;
    }
    
    public String getProtectorImageUrl() {
        return this.m_protectorImageUrl;
    }
    
    public static AlmanachMonthEntry createFromSOAP(final MapData mapData) {
        final int id = mapData.getIntValue("id");
        final int month = Integer.parseInt(mapData.getStringValue("month"));
        final Data colorData = mapData.getValue("color");
        final Color color = null;
        while (true) {
            if (colorData == null || colorData.getDataType() != DataType.NIL) {
                final boolean bg = mapData.getBooleanValue("background");
                final String bgUrl = mapData.getStringValue("backgroundurl");
                final String name = StringUtils.correctWordChars(mapData.getStringValue("protectorname"));
                final String description = StringUtils.correctWordChars(mapData.getStringValue("protectordesc"));
                final Data imageUrlData = mapData.getValue("protectorimageurl");
                final String imageUrl = (imageUrlData.getDataType() == DataType.BOOLEAN) ? null : mapData.getStringValue("protectorimageurl");
                return new AlmanachMonthEntry(id, month, color, bg, bgUrl, name, description, imageUrl);
            }
            continue;
        }
    }
}
