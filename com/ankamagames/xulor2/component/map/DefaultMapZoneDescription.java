package com.ankamagames.xulor2.component.map;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.graphics.image.*;

public class DefaultMapZoneDescription implements PartitionListMapZoneDescription
{
    private final PartitionList m_list;
    private final int m_id;
    private final Color m_color;
    private final String m_textDescription;
    private final String m_iconUrl;
    private final byte m_maskIndex;
    private final String m_anim1;
    private final String m_anim2;
    private final long m_highlightSoundId;
    private final boolean m_isInteractive;
    
    public DefaultMapZoneDescription(final PartitionList list, final int id, final Color color, final String textDescription, final String iconUrl, final byte maskIndex, final String anim1, final String anim2, final long highlightSoundId, final boolean interactive) {
        super();
        this.m_list = list;
        this.m_id = id;
        this.m_color = color;
        this.m_textDescription = textDescription;
        this.m_iconUrl = iconUrl;
        this.m_maskIndex = maskIndex;
        this.m_anim1 = anim1;
        this.m_anim2 = anim2;
        this.m_highlightSoundId = highlightSoundId;
        this.m_isInteractive = interactive;
    }
    
    @Override
    public boolean isInteractive() {
        return this.m_isInteractive;
    }
    
    @Override
    public PartitionList getPartitionList() {
        return this.m_list;
    }
    
    @Override
    public Color getZoneColor() {
        return this.m_color;
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public byte getMaskIndex() {
        return this.m_maskIndex;
    }
    
    @Override
    public String getTextDescription() {
        return this.m_textDescription;
    }
    
    @Override
    public int getBorderWidth() {
        return 1;
    }
    
    @Override
    public String getIconUrl() {
        return this.m_iconUrl;
    }
    
    @Override
    public String getAnim1() {
        return this.m_anim1;
    }
    
    @Override
    public String getAnim2() {
        return this.m_anim2;
    }
    
    @Override
    public long getHighlightSoundId() {
        return this.m_highlightSoundId;
    }
    
    public byte getScrollDecorator() {
        return 0;
    }
    
    @Override
    public boolean canZoomIn() {
        return false;
    }
}
