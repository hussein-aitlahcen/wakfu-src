package com.ankamagames.wakfu.client.core.game.tutorial;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;

public class TutorialView extends ImmutableFieldProvider
{
    private static final String TUTORIAL_TYPE_ICON_STYLE = "tutorialType";
    public static final String IMAGE_URL_FIELD = "imageUrl";
    public static final String TITLE_FIELD = "title";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String THEME_STYLE = "style";
    private final String m_iconUrl;
    private final String m_title;
    private final String m_desc;
    private final int m_type;
    private int m_eventActionId;
    
    public TutorialView(final String iconUrl, final String title, final String desc, final int type, final int eventActionId) {
        this(iconUrl, title, desc, type);
        this.m_eventActionId = eventActionId;
    }
    
    public TutorialView(final String iconUrl, final String title, final String desc, final int type) {
        super();
        this.m_eventActionId = -1;
        this.m_iconUrl = iconUrl;
        this.m_title = title;
        this.m_desc = desc;
        this.m_type = type;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("imageUrl")) {
            if (this.m_iconUrl == null) {
                return null;
            }
            return WakfuConfiguration.getInstance().getTutorialIconUrl(this.m_iconUrl);
        }
        else {
            if (fieldName.equals("title")) {
                return this.m_title;
            }
            if (fieldName.equals("description")) {
                return this.m_desc;
            }
            if (!fieldName.equals("style")) {
                return null;
            }
            switch (this.m_type) {
                case 1: {
                    return "tutorialType" + this.m_type;
                }
                default: {
                    return null;
                }
            }
        }
    }
    
    public String getTitle() {
        return this.m_title;
    }
    
    public int getEventActionId() {
        return this.m_eventActionId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TutorialView)) {
            return false;
        }
        final TutorialView that = (TutorialView)o;
        return this.m_eventActionId == that.m_eventActionId && this.m_type == that.m_type && this.m_desc.equals(that.m_desc) && (this.m_iconUrl == null || this.m_iconUrl.equals(that.m_iconUrl)) && this.m_title.equals(that.m_title);
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
}
