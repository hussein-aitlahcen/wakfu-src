package com.ankamagames.wakfu.common.game.effect.targeting;

import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

final class PercentHealthDescComparator implements Comparator<EffectUser>
{
    @Override
    public int compare(final EffectUser o1, final EffectUser o2) {
        if (!o1.hasCharacteristic(FighterCharacteristicType.HP)) {
            return 1;
        }
        if (!o2.hasCharacteristic(FighterCharacteristicType.HP)) {
            return -1;
        }
        final AbstractCharacteristic firstHp = o1.getCharacteristic(FighterCharacteristicType.HP);
        final Float firstRatio = firstHp.value() / firstHp.max() * 100.0f;
        final AbstractCharacteristic secondHp = o2.getCharacteristic(FighterCharacteristicType.HP);
        final Float secondRatio = secondHp.value() / secondHp.max() * 100.0f;
        return -firstRatio.compareTo(secondRatio);
    }
}
