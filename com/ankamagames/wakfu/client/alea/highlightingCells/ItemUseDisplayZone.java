package com.ankamagames.wakfu.client.alea.highlightingCells;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.wakfu.common.game.spell.*;

public class ItemUseDisplayZone extends RangeAndEffectDisplayer implements CustomTextureHighlightingProvider
{
    private static final Logger m_logger;
    private static final String ZONE_EFFECT_NAME = "ItemZoneEffect";
    private static final String RANGE_NAME = "ItemRange";
    private static final String RANGE_WITH_CONSTRAINT_NAME = "ItemRangeWithConstraint";
    private static final String RANGE_WITHOUT_LOS_NAME = "ItemRangeWithoutLOS";
    private static final String EMPTY_CELLS_NEEDED_NAME = "ItemEmptyCellsNeeded";
    private Item m_selectedItem;
    private static final String TEXTURE_NO_EFFECT = "forbidden.tga";
    private static final String TEXTURE_NOT_IN_LOS = "notinLOS.tga";
    private static final ItemUseDisplayZone m_instance;
    
    public static ItemUseDisplayZone getInstance() {
        return ItemUseDisplayZone.m_instance;
    }
    
    private ItemUseDisplayZone() {
        super("ItemRange", WakfuClientConstants.RANGE_COLOR, "ItemZoneEffect", WakfuClientConstants.ZONE_EFFECT_COLOR, "ItemRangeWithConstraint", WakfuClientConstants.RANGE_COLOR_WITH_CONSTRAINTS, "ItemRangeWithoutLOS", WakfuClientConstants.RANGE_COLOR_WITHOUT_LOS, "ItemEmptyCellsNeeded", WakfuClientConstants.EMPTY_CELL_NEEDED_COLOR);
    }
    
    public void selectItemRange(final Item item, final CharacterInfo fighter) {
        this.m_selectedItem = item;
        this.selectRange(fighter);
        this.m_selectedItem = null;
    }
    
    @Override
    protected RangeValidity checkValidity(final Point3 target, final CharacterInfo caster) {
        switch (caster.getCurrentFight().getItemCastValidity(caster, this.m_selectedItem, target, true)) {
            case OK: {
                return RangeValidity.OK;
            }
            case OK_BUT_NO_EFFECT_ON_TARGET: {
                return RangeValidity.OK_WITH_CONSTRAINTS;
            }
            case INVALID_LINE_OF_SIGHT: {
                return RangeValidity.INVALID_LOS;
            }
            case CAST_CRITERIONS_NOT_VALID: {
                return RangeValidity.INVALID_CRITERION;
            }
            default: {
                return RangeValidity.INVALID;
            }
        }
    }
    
    @Override
    public void update() {
        try {
            String textureFilename = WakfuConfiguration.getInstance().getString("highLightGfxPath") + "forbidden.tga";
            this.m_rangeWithConstraint.setTexture(textureFilename, HighLightTextureApplication.ORTHO_COVER_CELL);
            textureFilename = WakfuConfiguration.getInstance().getString("highLightGfxPath") + "notinLOS.tga";
            this.m_rangeWithoutLOS.setTexture(textureFilename, HighLightTextureApplication.ISO);
        }
        catch (Exception e) {
            ItemUseDisplayZone.m_logger.error((Object)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemUseDisplayZone.class);
        m_instance = new ItemUseDisplayZone();
    }
}
