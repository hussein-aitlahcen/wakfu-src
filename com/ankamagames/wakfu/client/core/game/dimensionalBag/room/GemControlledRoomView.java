package com.ankamagames.wakfu.client.core.game.dimensionalBag.room;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.xulor2.property.*;

public class GemControlledRoomView implements FieldProvider
{
    private static final Logger m_logger;
    public static final String IS_EXTENDED_FIELD = "isExtended";
    public static final String HAS_A_PRIMARY_GEM_FIELD = "hasAPrimaryGem";
    public static final String PRIMARY_GEM_FIELD = "primaryGem";
    public static final String PRIMARY_GEM_ICON_URL_FIELD = "primaryGemIconUrl";
    public static final String PRIMARY_GEM_IS_LOCKED_FIELD = "primaryGemIsLocked";
    public static final String HAS_A_SECONDARY_GEM_FIELD = "hasASecondaryGem";
    public static final String SECONDARY_GEM_ICON_URL_FIELD = "secondaryGemIconUrl";
    public static final String SECONDARY_GEM_FIELD = "secondaryGem";
    private GemControlledRoom m_room;
    
    public GemControlledRoomView(final GemControlledRoom room) {
        super();
        this.m_room = room;
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isExtended")) {
            final Item primaryGem = this.m_room.getGem(true);
            if (primaryGem == null) {
                return false;
            }
            final Item secondaryGem = this.m_room.getGem(false);
            if (secondaryGem == null) {
                return false;
            }
            return primaryGem.getReferenceId() == secondaryGem.getReferenceId();
        }
        else {
            if (fieldName.equals("hasAPrimaryGem")) {
                return this.m_room.getGem(true) != null;
            }
            if (fieldName.equals("hasASecondaryGem")) {
                return this.m_room.getGem(false) != null;
            }
            if (fieldName.equals("primaryGemIconUrl")) {
                final Item gem = this.m_room.getGem(true);
                if (gem == null) {
                    return null;
                }
                final int iconId = this.getGemIconIdFromRefId(gem);
                if (iconId == -1) {
                    return null;
                }
                try {
                    return String.format(WakfuConfiguration.getInstance().getString("dimensionalBagPrimaryGemPath"), iconId);
                }
                catch (PropertyException e) {
                    GemControlledRoomView.m_logger.warn((Object)"Impossible de trouver la propri\u00e9t\u00e9 dimensionalBagPrimaryGemPath");
                    return null;
                }
            }
            if (fieldName.equals("secondaryGemIconUrl")) {
                final Item gem = this.m_room.getGem(false);
                if (gem == null) {
                    return null;
                }
                final int iconId = this.getGemIconIdFromRefId(gem);
                if (iconId == -1) {
                    return null;
                }
                try {
                    return String.format(WakfuConfiguration.getInstance().getString("dimensionalBagSecondaryGemPath"), iconId);
                }
                catch (PropertyException e) {
                    GemControlledRoomView.m_logger.warn((Object)"Impossible de trouver la propri\u00e9t\u00e9 dimensionalBagSecondaryGemPath");
                    return null;
                }
            }
            if (fieldName.equals("primaryGem")) {
                return new GemItem(this.m_room.getGem(true), this.m_room.getLayoutPosition());
            }
            if (fieldName.equals("secondaryGem")) {
                return new GemItem(this.m_room.getGem(false), this.m_room.getLayoutPosition());
            }
            if (fieldName.equals("primaryGemIsLocked")) {
                return this.m_room.isPrimaryGemLocked();
            }
            return null;
        }
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    private int getGemIconIdFromRefId(final Item item) {
        final int itemReferenceId = item.getReferenceId();
        final GemType gemType = GemType.getFromItemReferenceId(itemReferenceId);
        switch (gemType) {
            case GEM_ID_MERCHANT: {
                return 0;
            }
            case GEM_ID_DECORATION: {
                return 1;
            }
            case GEM_ID_CRAFT: {
                return 2;
            }
            case GEM_ID_GUILD: {
                return 3;
            }
            case GEM_ID_RESOURCES: {
                return 4;
            }
            case GEM_ID_FARMING: {
                return 5;
            }
            case GEM_ID_DUNGEON: {
                return 6;
            }
            case GEM_ID_LIVING_ROOM: {
                return 3;
            }
            default: {
                return -1;
            }
        }
    }
    
    public void updateUI() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isExtended", "hasAPrimaryGem", "primaryGemIconUrl", "hasASecondaryGem", "secondaryGemIconUrl", "primaryGem", "secondaryGem");
    }
    
    static {
        m_logger = Logger.getLogger((Class)GemControlledRoomView.class);
    }
}
