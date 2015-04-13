package com.ankamagames.wakfu.client.chat;

import com.ankamagames.baseImpl.graphics.chat.*;
import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.xulor2.property.*;

public class WakfuUser extends FieldedUser implements FieldProvider
{
    protected static final Logger m_logger;
    public static final short NONE = 0;
    public static final short FRIEND = 1;
    public static final short IGNORE = 2;
    public static final short PARTY = 4;
    public static final short GUILD = 8;
    private short m_type;
    private boolean m_notify;
    private short m_breedId;
    private byte m_sex;
    private String m_comentary;
    private boolean m_comentaryEdition;
    private long m_xp;
    public static final String NOTIFY_FIELD = "notify";
    public static final String TYPE_FIELD = "type";
    public static final String ILLUSTRATION_URL_FIELD = "illustrationUrl";
    public static final String COMMENTARY_FIELD = "commentary";
    public static final String COMMENTARY_EDITION_FIELD = "commentaryEdition";
    public static final String LEVEL_FIELD = "level";
    public static final String[] FIELDS;
    public static final String[] ALL_FIELDS;
    
    public WakfuUser(final String characterName, final String name, final boolean online, final long id, final boolean notify, final short breedId, final byte sex, final String comentary, final long xp) {
        super(characterName, name, online, id);
        this.m_type = 0;
        this.m_notify = false;
        this.m_breedId = 0;
        this.m_sex = 0;
        this.m_comentaryEdition = false;
        this.m_notify = notify;
        this.m_breedId = breedId;
        this.m_sex = sex;
        this.m_comentary = comentary;
        this.m_xp = xp;
    }
    
    public WakfuUser(final long id, final String name, final String characterName) {
        super(id, name, characterName);
        this.m_type = 0;
        this.m_notify = false;
        this.m_breedId = 0;
        this.m_sex = 0;
        this.m_comentaryEdition = false;
    }
    
    public void EnableType(final short type) {
        this.m_type |= type;
    }
    
    public void DisableType(final short type) {
        this.m_type &= (short)~type;
    }
    
    public boolean isType(final short type) {
        return (this.m_type & type) != 0x0;
    }
    
    public boolean hasNoType() {
        return this.m_type == 0;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("notify")) {
            return this.isNotify();
        }
        if (fieldName.equals("type")) {
            return this.m_type;
        }
        if (fieldName.equals("commentary")) {
            return (this.m_comentary != null && this.m_comentary.length() > 0) ? this.m_comentary : null;
        }
        if (fieldName.equals("commentaryEdition")) {
            return this.m_comentaryEdition;
        }
        if (fieldName.equals("level")) {
            return (this.m_xp == -1L) ? "-" : CharacterXpTable.getInstance().getLevelByXp(this.m_xp);
        }
        if (fieldName.equals("illustrationUrl")) {
            if (this.m_breedId == -1) {
                return null;
            }
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedContactListIllustrationPath"), this.m_breedId, this.m_sex);
            }
            catch (PropertyException e) {
                WakfuUser.m_logger.error((Object)e.getMessage(), (Throwable)e);
            }
        }
        return super.getFieldValue(fieldName);
    }
    
    public boolean isNotify() {
        return this.m_notify;
    }
    
    public void setNotify(final boolean notify) {
        this.m_notify = notify;
    }
    
    @Override
    public String[] getFields() {
        return WakfuUser.ALL_FIELDS;
    }
    
    public String getComentary() {
        return this.m_comentary;
    }
    
    public void setComentary(final String comentary) {
        this.m_comentary = comentary;
    }
    
    public boolean isComentaryEdition() {
        return this.m_comentaryEdition;
    }
    
    public void setComentaryEdition(final boolean comentaryEdition) {
        this.m_comentaryEdition = comentaryEdition;
    }
    
    public void setBreedId(final short breedId) {
        this.m_breedId = breedId;
    }
    
    public void setSex(final byte sex) {
        this.m_sex = sex;
    }
    
    public void setXp(final long xp) {
        this.m_xp = xp;
    }
    
    public void updateProperty() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, WakfuUser.ALL_FIELDS);
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuUser.class);
        FIELDS = new String[] { "notify", "type", "illustrationUrl", "commentary", "commentaryEdition", "level" };
        ALL_FIELDS = new String[WakfuUser.FIELDS.length + FieldedUser.FIELDS.length];
        System.arraycopy(WakfuUser.FIELDS, 0, WakfuUser.ALL_FIELDS, 0, WakfuUser.FIELDS.length);
        System.arraycopy(FieldedUser.FIELDS, 0, WakfuUser.ALL_FIELDS, WakfuUser.FIELDS.length, FieldedUser.FIELDS.length);
    }
}
