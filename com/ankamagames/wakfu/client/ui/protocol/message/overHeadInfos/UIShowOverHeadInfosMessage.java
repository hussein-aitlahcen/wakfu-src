package com.ankamagames.wakfu.client.ui.protocol.message.overHeadInfos;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.framework.graphics.image.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.component.*;

public class UIShowOverHeadInfosMessage extends UIMessage
{
    private final OverHeadTarget m_target;
    private final int m_offset;
    private final List<OverHeadInfo> m_overHeadInfos;
    private String m_breedIconUrl;
    private Color m_breedIconColor;
    private String m_title;
    private final int m_overheadDelayPreference;
    
    public UIShowOverHeadInfosMessage(final OverHeadTarget target, final int offset) {
        this(target, offset, -1);
    }
    
    public UIShowOverHeadInfosMessage(final OverHeadTarget target, final int offset, final int overheadDelayPreference) {
        super();
        this.m_overHeadInfos = new ArrayList<OverHeadInfo>();
        this.m_breedIconUrl = null;
        this.m_breedIconColor = Color.WHITE;
        this.m_target = target;
        this.m_offset = offset;
        this.m_overheadDelayPreference = overheadDelayPreference;
    }
    
    public OverHeadTarget getTarget() {
        return this.m_target;
    }
    
    public List<OverHeadInfo> getOverHeadInfos() {
        return this.m_overHeadInfos;
    }
    
    public int getOffset() {
        return this.m_offset;
    }
    
    public String getBreedIconUrl() {
        return this.m_breedIconUrl;
    }
    
    public void setBreedIconUrl(final String breedIconUrl) {
        this.m_breedIconUrl = breedIconUrl;
    }
    
    public Color getBreedIconColor() {
        return this.m_breedIconColor;
    }
    
    public void setBreedIconColor(final Color breedIconColor) {
        this.m_breedIconColor = breedIconColor;
    }
    
    public String getTitle() {
        return this.m_title;
    }
    
    public void setTitle(final String title) {
        this.m_title = title;
    }
    
    public void addInfo(final String text) {
        this.addInfo(text, null, null);
    }
    
    public void addInfo(final String text, final String iconURL) {
        this.addInfo(text, iconURL, null);
    }
    
    public void addInfo(final String text, final String iconURL, final ArrayList<String> overIconsUrl) {
        if (text != null && text.length() > 0) {
            this.m_overHeadInfos.add(new OverHeadInfo(text, iconURL, (ArrayList)overIconsUrl));
        }
    }
    
    public boolean hasOverheadDelayPreference() {
        return this.m_overheadDelayPreference >= 0;
    }
    
    public int getOverheadDelayPreference() {
        return this.m_overheadDelayPreference;
    }
    
    @Override
    public int getId() {
        return 16590;
    }
    
    public static class OverHeadInfo extends ImmutableFieldProvider
    {
        public static final String TEXT_FIELD = "text";
        public static final String ICON_URL_FIELD = "iconUrl";
        public static final String OVER_ICONS_URL_FIELD = "overIconsUrl";
        public final String[] FIELDS;
        private final ArrayList<String> m_overIconsURL;
        private final String m_iconURL;
        private final String m_text;
        
        private OverHeadInfo(final String text, final String iconURL, final ArrayList<String> overIconUrl) {
            super();
            this.FIELDS = new String[] { "text", "iconUrl", "overIconsUrl" };
            this.m_overIconsURL = overIconUrl;
            this.m_iconURL = iconURL;
            this.m_text = text;
        }
        
        public String getText() {
            return this.m_text;
        }
        
        @Override
        public String[] getFields() {
            return null;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("text")) {
                return this.m_text;
            }
            if (fieldName.equals("iconUrl")) {
                return this.m_iconURL;
            }
            if (fieldName.equals("overIconsUrl")) {
                return this.m_overIconsURL;
            }
            return null;
        }
    }
}
