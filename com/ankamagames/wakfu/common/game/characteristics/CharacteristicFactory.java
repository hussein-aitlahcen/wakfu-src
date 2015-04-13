package com.ankamagames.wakfu.common.game.characteristics;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public class CharacteristicFactory
{
    public static AbstractCharacteristic newCharacteristic(final CharacteristicType type) {
        switch (type.getCharacteristicType()) {
            case 0: {
                return new FighterCharacteristic((FighterCharacteristicType)type, type.getLowerBound(), type.getUpperBound());
            }
            case 1: {
                return new ItemCharacteristic((ItemCharacteristicType)type, type.getLowerBound(), type.getUpperBound());
            }
            case 2: {
                return new EffectAreaCharacteristic((EffectAreaCharacteristicType)type, type.getLowerBound(), type.getUpperBound());
            }
            default: {
                return null;
            }
        }
    }
}
