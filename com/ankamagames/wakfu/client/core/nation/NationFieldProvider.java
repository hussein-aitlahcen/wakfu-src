package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;

public class NationFieldProvider extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String NATION_ID = "nationId";
    public static final String NAME_FIELD = "name";
    public static final String FLAG_ICON_URL_FIELD = "flagIconUrl";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String PASSPORT_STAMP_ICON_URL = "passportStampIconUrl";
    public static final String NATION_COLOR = "nationColor";
    public static final String IS_LOCAL_NATION = "isLocalNation";
    public static final String[] FIELDS;
    protected final int m_nationId;
    
    public NationFieldProvider(final int nationId) {
        super();
        this.m_nationId = nationId;
    }
    
    @Override
    public String[] getFields() {
        return NationFieldProvider.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("nationId")) {
            return this.m_nationId;
        }
        if (fieldName.equals("isLocalNation")) {
            return this.isLocalNation();
        }
        if (fieldName.equals("flagIconUrl")) {
            return WakfuConfiguration.getInstance().getFlagIconUrl((this.m_nationId != 0) ? this.m_nationId : -1);
        }
        if (fieldName.equals("description")) {
            return this.getDescription();
        }
        if (fieldName.equals("passportStampIconUrl")) {
            if (this.m_nationId != 0) {
                return WakfuConfiguration.getInstance().getIconUrl("passportStampIconsPath", "defaultIconPath", this.m_nationId);
            }
            return null;
        }
        else {
            if (fieldName.equals("nationColor")) {
                return (this.getColor() == null) ? Color.WHITE : this.getColor();
            }
            return null;
        }
    }
    
    public boolean isLocalNation() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return localPlayer != null && this.m_nationId == localPlayer.getCitizenComportment().getNationId();
    }
    
    private Color getColor() {
        final String nationColor = NationDisplayer.getInstance().getNationColor(this.m_nationId);
        return (nationColor == null || nationColor.length() == 0) ? null : Color.getRGBAFromHex(nationColor);
    }
    
    public String getDescription() {
        return WakfuTranslator.getInstance().getString(56, this.m_nationId, new Object[0]);
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(39, this.m_nationId, new Object[0]);
    }
    
    public int getNationId() {
        return this.m_nationId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NationFieldProvider.class);
        FIELDS = new String[] { "nationId", "isLocalNation", "name", "flagIconUrl", "description", "passportStampIconUrl", "nationColor" };
    }
}
