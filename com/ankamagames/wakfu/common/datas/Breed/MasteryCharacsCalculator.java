package com.ankamagames.wakfu.common.datas.Breed;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import gnu.trove.*;
import java.util.*;

public class MasteryCharacsCalculator
{
    private static final double[] LEVEL_TO_MASTERY_RATIO;
    public static final int STASIS_MASTERY_RATIO = 2;
    public static final MasteryCharacsCalculator INSTANCE;
    public static final MasteryCharacsCalculator NULL_CALCULATOR;
    
    public void computeAndApply(final Elements element, final CharacteristicManager<FighterCharacteristic> characteristics, final Iterable<? extends AbstractSpellLevel> spellInventory) {
        if (element == null) {
            return;
        }
        final FighterCharacteristicType masteryCharacteristic = element.getMasteryCharacteristic();
        if (masteryCharacteristic == null) {
            return;
        }
        final int elementMastery = this.calculateElementMastery(element, spellInventory);
        this.applyElementMastery(characteristics, masteryCharacteristic, elementMastery);
    }
    
    private void applyElementMastery(final CharacteristicManager<FighterCharacteristic> characteristics, final FighterCharacteristicType masteryCharacteristic, final int elementMastery) {
        final FighterCharacteristic characteristic = characteristics.getCharacteristic(masteryCharacteristic);
        if (characteristic == null) {
            return;
        }
        characteristic.set(elementMastery);
    }
    
    private int calculateElementMastery(final Elements element, final Iterable<? extends AbstractSpellLevel> spellInventory) {
        final TShortArrayList levels = new TShortArrayList();
        final byte elementId = element.getId();
        for (final AbstractSpellLevel spellLevel : spellInventory) {
            if (spellLevel.getSpell().getElementId() == elementId && spellLevel.getLevel() > 0) {
                levels.add(spellLevel.getLevel());
            }
        }
        return this.calculateMastery(levels, element.getMasteryCharacteristic());
    }
    
    public int calculateMastery(final TShortArrayList levels, final FighterCharacteristicType masteryCharacteristic) {
        if (masteryCharacteristic == null) {
            return 0;
        }
        levels.sort();
        levels.reverse();
        double totalMastery = 0.0;
        for (int i = 0; i < levels.size() && i < MasteryCharacsCalculator.LEVEL_TO_MASTERY_RATIO.length; ++i) {
            totalMastery += levels.get(i) * MasteryCharacsCalculator.LEVEL_TO_MASTERY_RATIO[i];
        }
        if (masteryCharacteristic == FighterCharacteristicType.STASIS_MASTERY) {
            totalMastery /= 2.0;
        }
        return (int)Math.round(totalMastery);
    }
    
    static {
        LEVEL_TO_MASTERY_RATIO = new double[] { 0.3, 0.35, 0.05, 0.1, 0.2 };
        INSTANCE = new MasteryCharacsCalculator();
        NULL_CALCULATOR = new MasteryCharacsCalculator() {
            @Override
            public void computeAndApply(final Elements element, final CharacteristicManager<FighterCharacteristic> characteristics, final Iterable<? extends AbstractSpellLevel> spellInventory) {
            }
        };
    }
}
