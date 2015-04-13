package com.ankamagames.wakfu.common.game.fight;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

final class ItemAndSpellCastValidator
{
    private static Logger m_logger;
    private final AbstractFight m_linkedFight;
    private final CommonCastValidator m_commonCastValidator;
    private final ItemCastValidator m_itemCastValidator;
    
    ItemAndSpellCastValidator(final AbstractFight linkedFight) {
        super();
        this.m_linkedFight = linkedFight;
        this.m_commonCastValidator = new CommonCastValidator(linkedFight);
        this.m_itemCastValidator = new ItemCastValidator(linkedFight);
    }
    
    public CastValidity getItemAndSpellCastValidity(final BasicCharacterInfo fighter, final Item item, final AbstractSpellLevel spelllevel, final Point3 targetCell) {
        if (item == null || !item.isUsable()) {
            ItemAndSpellCastValidator.m_logger.error((Object)this.m_linkedFight.withFightId("utilisation d'un objet null, inutilisable ou cass\u00e9"));
            return CastValidity.INVALID_CONTAINER;
        }
        final AbstractReferenceItem refItem = item.getReferenceItem();
        if (spelllevel == null) {
            ItemAndSpellCastValidator.m_logger.error((Object)this.m_linkedFight.withFightId("cast d'un spell null"));
            return CastValidity.INVALID_CONTAINER;
        }
        final AbstractSpell spell = spelllevel.getSpell();
        if (spell.isPassive()) {
            return CastValidity.PASSIVE;
        }
        final SpellCost spellCost = spell.getSpellCost(spelllevel, fighter, targetCell, fighter.getEffectContext());
        if (refItem.getActionPoints() + spellCost.getCharacCost(FighterCharacteristicType.AP) > fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.AP).value()) {
            return CastValidity.NOT_ENOUGH_AP;
        }
        if (refItem.getWakfuPoints() + spellCost.getCharacCost(FighterCharacteristicType.WP) > fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.WP).value()) {
            return CastValidity.NOT_ENOUGH_FP;
        }
        if (refItem.getMovementPoints() + spellCost.getCharacCost(FighterCharacteristicType.MP) > fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.MP).value()) {
            return CastValidity.NOT_ENOUGH_MP;
        }
        if (targetCell == null) {
            final CastValidity validity = fighter.getSpellLevelCastHistory().canCastSpell(spelllevel, this.m_linkedFight.getTimeline().getCurrentTableturn());
            if (!validity.isValid()) {
                return validity;
            }
        }
        else {
            final BasicCharacterInfo target = this.m_linkedFight.getCharacterInfoAtPosition(targetCell.getX(), targetCell.getY());
            final FightMap fightMap = this.m_linkedFight.getFightMap();
            final CommonCastValidator commonCastValidator = this.m_commonCastValidator;
            if (!CommonCastValidator.checkCellAlignement(fighter, targetCell, spelllevel, target, this.m_linkedFight)) {
                return CastValidity.CELLS_NOT_ALIGNED;
            }
            final CastValidity validity2 = fighter.getSpellLevelCastHistory().canCastSpell(spelllevel, this.m_linkedFight.getTimeline().getCurrentTableturn(), target);
            if (!validity2.isValid()) {
                return validity2;
            }
        }
        final CastValidity validity = this.m_commonCastValidator.getCastValidity(fighter, spelllevel, targetCell, true, refItem.getUseRangeMin(), refItem.getUseRangeMax(), refItem.hasToTestLOS(), refItem.hasToTestFreeCell(), refItem.hasToTestNotBorderCell(), spell.isCanCastOnCasterCell(), spell.getCastCriterions());
        if (!validity.isValid()) {
            return validity;
        }
        return this.m_itemCastValidator.getItemCastValidity(fighter, item, targetCell, false);
    }
    
    static {
        ItemAndSpellCastValidator.m_logger = Logger.getLogger((Class)ItemAndSpellCastValidator.class);
    }
}
