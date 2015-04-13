package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.wakfu.common.game.fighter.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public final class ElementsUtils
{
    public static List<Elements> getElementsSortedByIncreasingDamageValue(final CharacteristicManager<FighterCharacteristic> characteristicManager) {
        if (characteristicManager == null) {
            return Collections.emptyList();
        }
        final List<Elements> res = Arrays.asList(Elements.values());
        Collections.sort(res, new IncreasingDamageComparator(characteristicManager));
        return res;
    }
    
    private static class IncreasingDamageComparator implements Comparator<Elements>
    {
        private final CharacteristicManager<FighterCharacteristic> m_characteristicManager;
        
        IncreasingDamageComparator(final CharacteristicManager<FighterCharacteristic> characteristicManager) {
            super();
            this.m_characteristicManager = characteristicManager;
        }
        
        @Override
        public int compare(final Elements o1, final Elements o2) {
            if (!this.m_characteristicManager.contains(o1.getDamageBonusCharacteristic())) {
                return -1;
            }
            if (!this.m_characteristicManager.contains(o2.getDamageBonusCharacteristic())) {
                return 1;
            }
            final int firstValue = this.m_characteristicManager.getCharacteristicValue(o1.getDamageBonusCharacteristic());
            final int secondValue = this.m_characteristicManager.getCharacteristicValue(o2.getDamageBonusCharacteristic());
            return Integer.valueOf(firstValue).compareTo(Integer.valueOf(secondValue));
        }
    }
}
