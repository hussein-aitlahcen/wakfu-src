package com.ankamagames.wakfu.common.game.fighter.FighterCharacteristicProcedures;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public final class CharacBoostAnotherCharacProcedureCapped extends CharacBoostAnotherCharacProcedure
{
    private final int m_cap;
    private int m_totalValueAdded;
    
    public CharacBoostAnotherCharacProcedureCapped(final CharacteristicManager<FighterCharacteristic> manager, final FighterCharacteristicType characToBoost, final float ratio, final int updateType, final int cap) {
        super(manager, characToBoost, ratio, updateType);
        this.m_cap = cap;
    }
    
    @Override
    public void execute(final FighterCharacteristicEvent event, final int value) {
        final FighterCharacteristic tocharacteristic = this.m_manager.getCharacteristic(this.m_toCharacType);
        if (tocharacteristic == null) {
            return;
        }
        Label_0245: {
            switch (event) {
                case VALUE_ADDED: {
                    int valueToAdd = (int)Math.floor(this.m_ratio * value);
                    valueToAdd = Math.min(valueToAdd, this.m_cap - this.m_totalValueAdded);
                    if (valueToAdd <= 0) {
                        return;
                    }
                    switch (this.m_updateType) {
                        case 0: {
                            this.m_totalValueAdded += tocharacteristic.add(valueToAdd);
                            break;
                        }
                        case 1: {
                            tocharacteristic.setMax(tocharacteristic.unboundedmax() + valueToAdd);
                            break;
                        }
                    }
                    break;
                }
                case VALUE_SUBSTRACTED: {
                    int valueToSubstract = (int)Math.floor(this.m_ratio * value);
                    valueToSubstract = Math.min(valueToSubstract, this.m_totalValueAdded);
                    if (valueToSubstract <= 0) {
                        return;
                    }
                    switch (this.m_updateType) {
                        case 0: {
                            this.m_totalValueAdded -= tocharacteristic.substract(valueToSubstract);
                            break Label_0245;
                        }
                        case 1: {
                            tocharacteristic.setMax(tocharacteristic.unboundedmax() - valueToSubstract);
                            break Label_0245;
                        }
                    }
                    break;
                }
            }
        }
    }
}
