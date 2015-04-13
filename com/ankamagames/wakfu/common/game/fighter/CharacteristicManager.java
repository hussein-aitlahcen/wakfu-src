package com.ankamagames.wakfu.common.game.fighter;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import org.jetbrains.annotations.*;

public interface CharacteristicManager<Characteristic extends AbstractCharacteristic> extends RawConvertible<RawCharacteristics>
{
    void makeDefault();
    
    @Nullable
    Characteristic getCharacteristic(CharacteristicType p0);
    
    int getCharacteristicValue(CharacteristicType p0);
    
    int getCharacteristicMaxValue(CharacteristicType p0);
    
    boolean contains(CharacteristicType p0);
    
    boolean toRaw(RawCharacteristics p0, CharacteristicType... p1);
    
    void copy(FighterCharacteristicManager p0);
}
