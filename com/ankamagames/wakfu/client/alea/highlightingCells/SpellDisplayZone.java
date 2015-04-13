package com.ankamagames.wakfu.client.alea.highlightingCells;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.wakfu.common.game.spell.*;

public class SpellDisplayZone extends RangeAndEffectDisplayer implements CustomTextureHighlightingProvider
{
    private static final Logger m_logger;
    private static final String ZONE_EFFECT_NAME = "SpellZoneEffect";
    private static final String RANGE_NAME = "SpellRange";
    private static final String RANGE_WITH_CONSTRAINT_NAME = "SpellRangeWithConstraint";
    private static final String RANGE_WITHOUT_LOS_NAME = "SpellRangeWithoutLOS";
    private static final String EMPTY_CELLS_NEEDED_NAME = "SpellEmptyCellsNeeded";
    private SpellLevel m_selectedSpell;
    private static final String TEXTURE_NO_EFFECT = "forbidden.tga";
    private static final String TEXTURE_NOT_IN_LOS = "notinLOS.tga";
    private static final SpellDisplayZone m_instance;
    
    public static SpellDisplayZone getInstance() {
        return SpellDisplayZone.m_instance;
    }
    
    private SpellDisplayZone() {
        super("SpellRange", WakfuClientConstants.RANGE_COLOR, "SpellZoneEffect", WakfuClientConstants.ZONE_EFFECT_COLOR, "SpellRangeWithConstraint", WakfuClientConstants.RANGE_COLOR_WITH_CONSTRAINTS, "SpellRangeWithoutLOS", WakfuClientConstants.RANGE_COLOR_WITHOUT_LOS, "SpellEmptyCellsNeeded", WakfuClientConstants.EMPTY_CELL_NEEDED_COLOR);
    }
    
    public void selectSpellRange(final SpellLevel selectedSpell, final CharacterInfo fighter) {
        this.m_selectedSpell = selectedSpell;
        this.selectRange(fighter);
        this.m_selectedSpell = null;
    }
    
    @Override
    public void selectZoneEffect(final EffectContainer<WakfuEffect> container, final CharacterInfo fighter, final Point3 target) {
        if (container instanceof SpellLevel && ((SpellLevel)container).getSpell().isCastOnRandomCell()) {
            this.clearAndSelectTargetCell(target);
            return;
        }
        super.selectZoneEffect(container, fighter, target);
    }
    
    @Override
    protected RangeValidity checkValidity(final Point3 target, final CharacterInfo caster) {
        switch (caster.getCurrentFight().getSpellCastValidity(caster, this.m_selectedSpell, target, true)) {
            case OK:
            case OK_BUT_NO_EFFECT_ON_TARGET: {
                return RangeValidity.OK;
            }
            case INVALID_LINE_OF_SIGHT: {
                return RangeValidity.INVALID_LOS;
            }
            case CAST_CRITERIONS_NOT_VALID: {
                if (this.m_selectedSpell.getSpell() != null && this.m_selectedSpell.getSpell().hasProperty(SpellPropertyType.DO_NOT_DISPLAY_INVALID_CRITERION_CELLS)) {
                    return RangeValidity.INVALID;
                }
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
            if (this.m_rangeWithConstraint != null) {
                this.m_rangeWithConstraint.setTexture(textureFilename, HighLightTextureApplication.ISO);
            }
            textureFilename = WakfuConfiguration.getInstance().getString("highLightGfxPath") + "notinLOS.tga";
            if (this.m_rangeWithoutLOS != null) {
                this.m_rangeWithoutLOS.setTexture(textureFilename, HighLightTextureApplication.ISO);
            }
        }
        catch (Exception e) {
            SpellDisplayZone.m_logger.error((Object)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SpellDisplayZone.class);
        m_instance = new SpellDisplayZone();
    }
}
