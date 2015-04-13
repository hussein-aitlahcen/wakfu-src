package com.ankamagames.wakfu.common.game.fight;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.spell.*;

public final class SpellCastValidation
{
    public static boolean checkMp(final BasicCharacterInfo fighter, final AbstractSpellLevel spelllevel, final AbstractSpell spell) {
        final byte mpCost = spell.getMovementPoints(spelllevel, fighter, null, fighter.getEffectContext());
        int mp = fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.MP).value();
        if (mpCost > 0) {
            if (fighter.isActiveProperty(FightPropertyType.CRIPPLED_3)) {
                mp = -1;
            }
            else if (fighter.isActiveProperty(FightPropertyType.CRIPPLED_2)) {
                mp -= 2;
            }
            else if (fighter.isActiveProperty(FightPropertyType.CRIPPLED_1)) {
                --mp;
            }
        }
        return mpCost <= mp;
    }
    
    public static boolean checkWp(final BasicCharacterInfo fighter, final AbstractSpellLevel spelllevel, final AbstractSpell spell) {
        final byte wpCost = spell.getWakfuPoints(spelllevel, fighter, null, fighter.getEffectContext());
        int wp = fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.WP).value();
        if (wpCost > 0) {
            if (fighter.isActiveProperty(FightPropertyType.STASIS_3)) {
                wp = -1;
            }
            else if (fighter.isActiveProperty(FightPropertyType.STASIS_2)) {
                wp -= 2;
            }
            else if (fighter.isActiveProperty(FightPropertyType.STASIS_1)) {
                --wp;
            }
        }
        return wpCost <= wp;
    }
    
    public static boolean checkAp(final BasicCharacterInfo fighter, final AbstractSpellLevel spelllevel, final AbstractSpell spell) {
        final byte apCost = spell.getActionPoints(spelllevel, fighter, null, fighter.getEffectContext());
        int ap = fighter.getCharacteristic((CharacteristicType)FighterCharacteristicType.AP).value();
        if (apCost > 0) {
            if (fighter.isActiveProperty(FightPropertyType.AP_AS_MP)) {
                return false;
            }
            if (fighter.isActiveProperty(FightPropertyType.GROGGY_3)) {
                ap = -1;
            }
            else if (fighter.isActiveProperty(FightPropertyType.GROGGY_2)) {
                ap -= 2;
            }
            else if (fighter.isActiveProperty(FightPropertyType.GROGGY_1)) {
                --ap;
            }
        }
        return apCost <= ap;
    }
    
    public static boolean checkCharac(final BasicCharacterInfo fighter, final FighterCharacteristicType charac, final AbstractSpellLevel spelllevel, final AbstractSpell spell) {
        final SpellCost spellCost = spell.getSpellCost(spelllevel, fighter, null, fighter.getEffectContext());
        final byte characCost = spellCost.getCharacCost(charac);
        if (characCost == 0) {
            return true;
        }
        final int value = fighter.getCharacteristic((CharacteristicType)charac).value();
        return characCost <= value;
    }
    
    public static boolean checkPassivity(final AbstractSpell spell) {
        return spell.isPassive();
    }
    
    public static boolean checkCarryCast(final BasicCharacterInfo fighter, final AbstractSpell spell) {
        return !fighter.isCarrying() || spell.canBeCastedWhenCarrying();
    }
}
