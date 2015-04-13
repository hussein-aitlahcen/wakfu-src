package com.ankamagames.wakfu.client.core.emote;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.text.*;

public abstract class EmoteSmileyFieldProvider extends ImmutableFieldProvider
{
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String KNOWN = "isKnown";
    public static final String[] FIELDS;
    protected final int m_id;
    protected final String m_commandText;
    private boolean m_known;
    
    protected EmoteSmileyFieldProvider(final int id, final String commandText) {
        super();
        this.m_id = id;
        this.m_commandText = commandText;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public String getName() {
        return null;
    }
    
    public String getCommandText() {
        return this.m_commandText;
    }
    
    public String getDescription() {
        final TextWidgetFormater textWidgetFormater = new TextWidgetFormater();
        return textWidgetFormater.b().append(this.getName())._b().append(" (" + this.getCommandText() + ")").finishAndToString();
    }
    
    public abstract String getIconUrl();
    
    public void setKnown(final boolean known) {
        this.m_known = known;
    }
    
    public boolean isKnown() {
        return this.m_known;
    }
    
    @Override
    public String[] getFields() {
        return EmoteSmileyFieldProvider.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("iconUrl")) {
            return this.getIconUrl();
        }
        if (fieldName.equals("description")) {
            return this.getDescription();
        }
        if (fieldName.equals("isKnown")) {
            return this.m_known;
        }
        return null;
    }
    
    static {
        FIELDS = new String[] { "iconUrl", "description", "isKnown" };
    }
}
