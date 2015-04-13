package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.skill.*;

public abstract class AbstractCraftHarvestElement extends ImmutableFieldProvider
{
    public static final String TYPE_FIELD = "type";
    public static final String NAME_FIELD = "name";
    public static final String ICON_URL = "iconUrl";
    public static final String DURATION = "duration";
    public static final String SOURCE_NAME_FIELD = "sourceName";
    public static final String ACTION_DESCRIPTION = "actionDescription";
    public static final String LEVEL = "level";
    public static final String LEVEL_TEXT = "levelText";
    public static final String MULTIPLE = "multiple";
    public static final String CAN_USE_AS_INGREDIENT = "canUseAsIngredient";
    private final ResourceType m_resourceType;
    private final int m_itemId;
    private final int m_gfxId;
    private final int m_visualId;
    private int m_levelMin;
    private final int m_duration;
    private final boolean m_isMultiple;
    private final boolean m_canUseAsIngredient;
    
    protected AbstractCraftHarvestElement(final int itemId, final int visualId, final int levelMin, final int duration, final boolean multiple, final ResourceType type) {
        super();
        this.m_itemId = itemId;
        this.m_visualId = visualId;
        this.m_levelMin = levelMin;
        final AbstractReferenceItem item = ReferenceItemManager.getInstance().getReferenceItem(this.m_itemId);
        this.m_gfxId = ((item == null) ? 0 : item.getGfxId());
        this.m_duration = duration;
        this.m_isMultiple = multiple;
        this.m_resourceType = type;
        this.m_canUseAsIngredient = CraftManager.INSTANCE.hasCraftUsingAnyIngredient(this.m_itemId);
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("iconUrl")) {
            return WakfuConfiguration.getInstance().getIconUrl("itemsIconsPath", "defaultIconPath", this.m_gfxId);
        }
        if (fieldName.equals("sourceName")) {
            return this.getSourceName();
        }
        if (fieldName.equals("actionDescription")) {
            final ActionVisual actionVisual = ActionVisualManager.getInstance().get(this.m_visualId);
            final String action = WakfuTranslator.getInstance().getString("desc.mru." + actionVisual.getMruLabelKey());
            final String source = this.getSourceName();
            return WakfuTranslator.getInstance().getString("harvest.action", action, source);
        }
        if (fieldName.equals("duration")) {
            return String.format("%.1f s", this.m_duration / 1000.0f);
        }
        if (fieldName.equals("type")) {
            return this.getType().m_id;
        }
        if (fieldName.equals("multiple")) {
            return this.m_isMultiple;
        }
        if (fieldName.equals("level")) {
            return this.m_levelMin;
        }
        if (fieldName.equals("canUseAsIngredient")) {
            return this.m_canUseAsIngredient;
        }
        if (fieldName.equals("levelText")) {
            return WakfuTranslator.getInstance().getString("levelShort.custom", this.m_levelMin);
        }
        return null;
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(15, this.m_itemId, new Object[0]);
    }
    
    public int getLevelMin() {
        return this.m_levelMin;
    }
    
    public int getItemId() {
        return this.m_itemId;
    }
    
    public ResourceType getResourceType() {
        return this.m_resourceType;
    }
    
    public void setLevelMin(final int levelMin) {
        this.m_levelMin = levelMin;
    }
    
    public abstract String getSourceName();
    
    public abstract CraftHarvestElementType getType();
    
    public enum CraftHarvestElementType
    {
        RESOURCE((byte)0), 
        MONSTER((byte)1);
        
        private final byte m_id;
        
        private CraftHarvestElementType(final byte id) {
            this.m_id = id;
        }
        
        public static CraftHarvestElementType fromId(final byte id) {
            for (final CraftHarvestElementType type : values()) {
                if (id == type.m_id) {
                    return type;
                }
            }
            return null;
        }
    }
}
