package com.ankamagames.baseImpl.graphics.chat;

import com.ankamagames.baseImpl.client.proxyclient.base.chat.userGroup.*;
import com.ankamagames.framework.reflect.*;

public class FieldedUser extends User implements FieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String CHARACTER_NAME_FIELD = "characterName";
    public static final String DISPLAYED_NAME_FIELD = "displayedName";
    public static final String ONLINE_FIELD = "online";
    public static final String[] FIELDS;
    
    public FieldedUser(final String characterName, final String name, final boolean online, final long id) {
        super(characterName, name, online, id);
    }
    
    public FieldedUser(final long id, final String name, final String characterName) {
        super(id, name, characterName);
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("characterName")) {
            return this.getCharacterName();
        }
        if (fieldName.equals("displayedName")) {
            final String charaName = this.getCharacterName();
            if (charaName != null && !charaName.isEmpty()) {
                final StringBuilder sb = new StringBuilder();
                sb.append(charaName).append("\n(");
                sb.append(this.getName()).append(")");
                return sb.toString();
            }
            return this.getName();
        }
        else {
            if (fieldName.equals("online")) {
                return this.isOnline();
            }
            return null;
        }
    }
    
    @Override
    public void setOnline(final boolean online) {
        super.setOnline(online);
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(this, "online", "displayedName");
    }
    
    @Override
    public String[] getFields() {
        return FieldedUser.FIELDS;
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    static {
        FIELDS = new String[] { "name", "characterName", "displayedName", "online" };
    }
}
