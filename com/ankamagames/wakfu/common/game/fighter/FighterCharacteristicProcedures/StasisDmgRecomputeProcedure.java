package com.ankamagames.wakfu.common.game.fighter.FighterCharacteristicProcedures;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;

public final class StasisDmgRecomputeProcedure implements FighterCharacteristicProcedure
{
    private final CharacteristicManager<FighterCharacteristic> m_manager;
    
    public StasisDmgRecomputeProcedure(final CharacteristicManager<FighterCharacteristic> manager) {
        super();
        this.m_manager = manager;
    }
    
    @Override
    public void execute(final FighterCharacteristicEvent event, final int value) {
        final FighterCharacteristic dmgStasis = this.m_manager.getCharacteristic(FighterCharacteristicType.DMG_STASIS_PERCENT);
        if (dmgStasis == null) {
            return;
        }
        final List<Elements> bestThreeElements = this.getBestThreeElements();
        if (bestThreeElements.size() != 3) {
            return;
        }
        final int stasisDamage = this.computeDamageValue(bestThreeElements);
        dmgStasis.set(stasisDamage);
    }
    
    private int computeDamageValue(final Collection<Elements> bestThreeElements) {
        int damageSum = 0;
        for (final Elements bestThreeElement : bestThreeElements) {
            damageSum += this.m_manager.getCharacteristicValue(bestThreeElement.getDamageBonusCharacteristic());
        }
        final int stasisMastery = this.m_manager.getCharacteristicValue(FighterCharacteristicType.STASIS_MASTERY);
        return damageSum / bestThreeElements.size() + stasisMastery;
    }
    
    private List<Elements> getBestThreeElements() {
        final List<Elements> sortedElements = ElementsUtils.getElementsSortedByIncreasingDamageValue(this.m_manager);
        final List<Elements> modifiableSortedElements = new ArrayList<Elements>(sortedElements);
        Collections.reverse(modifiableSortedElements);
        modifiableSortedElements.retainAll(Arrays.asList(Elements.AIR, Elements.WATER, Elements.FIRE, Elements.EARTH));
        return modifiableSortedElements.subList(0, 3);
    }
    
    @Override
    public String toString() {
        return "StasisDmgRecomputeProcedure{m_manager=" + this.m_manager + '}';
    }
}
