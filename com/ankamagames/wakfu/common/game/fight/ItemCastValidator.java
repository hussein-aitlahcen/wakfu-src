package com.ankamagames.wakfu.common.game.fight;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

final class ItemCastValidator
{
    private static Logger m_logger;
    private final AbstractFight m_linkedFight;
    private final CommonCastValidator m_commonCastValidator;
    
    ItemCastValidator(final AbstractFight linkedFight) {
        super();
        this.m_linkedFight = linkedFight;
        this.m_commonCastValidator = new CommonCastValidator(linkedFight);
    }
    
    public CastValidity getItemCastValidity(final BasicCharacterInfo fighter, final Item item, final Point3 targetCell, final boolean withUseCost) {
        if (item == null || !item.isUsable()) {
            ItemCastValidator.m_logger.error((Object)this.m_linkedFight.withFightId("cast d'un item null ou non utilisable ou cass\u00e9"));
            return CastValidity.INVALID_CONTAINER;
        }
        if (item.isExpiredRent()) {
            return CastValidity.CANNOT_USE_ITEM;
        }
        if (fighter.isCarrying()) {
            return CastValidity.CANNOT_USE_ITEM_WHEN_CARRYING;
        }
        if (fighter.isActiveProperty(FightPropertyType.CANNOT_USE_ITEM_CAST)) {
            return CastValidity.CANNOT_USE_ITEM;
        }
        final AbstractReferenceItem refItem = item.getReferenceItem();
        if (withUseCost) {
            final byte apCost = refItem.getActionPoints();
            if (apCost > 0 && fighter.isActiveProperty(FightPropertyType.AP_AS_MP)) {
                return CastValidity.NOT_ENOUGH_AP;
            }
            if (apCost > fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.AP).value()) {
                return CastValidity.NOT_ENOUGH_AP;
            }
            if (refItem.getWakfuPoints() > fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.WP).value()) {
                return CastValidity.NOT_ENOUGH_FP;
            }
            if (refItem.getMovementPoints() > fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.MP).value()) {
                return CastValidity.NOT_ENOUGH_MP;
            }
        }
        if (!EquipmentInventoryChecker.getInstance().checkCriterion(item, (EffectUser)fighter, (EffectContext)this.m_linkedFight.getContext())) {
            return CastValidity.CAST_CRITERIONS_NOT_VALID;
        }
        if (refItem.isUseOnlyInLine() && targetCell != null && targetCell.getX() != fighter.getPosition().getX() && targetCell.getY() != fighter.getPosition().getY()) {
            return CastValidity.CELLS_NOT_ALIGNED;
        }
        if (refItem.getId() != 2145 && refItem.isEquipment() && !fighter.getEquipmentInventory().containsReferenceId(refItem.getId())) {
            return CastValidity.CANNOT_USE_ITEM;
        }
        final SimpleCriterion criterion = refItem.getCriterion(ActionsOnItem.USE_IN_FIGHT);
        return this.m_commonCastValidator.getCastValidity(fighter, item, targetCell, false, refItem.getUseRangeMin(), refItem.getUseRangeMax(), refItem.hasToTestLOS(), refItem.hasToTestFreeCell(), refItem.hasToTestNotBorderCell(), false, criterion);
    }
    
    static {
        ItemCastValidator.m_logger = Logger.getLogger((Class)SpellCastValidator.class);
    }
}
