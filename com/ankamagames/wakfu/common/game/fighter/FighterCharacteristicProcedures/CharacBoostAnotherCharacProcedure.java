package com.ankamagames.wakfu.common.game.fighter.FighterCharacteristicProcedures;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public class CharacBoostAnotherCharacProcedure implements FighterCharacteristicProcedure
{
    public static final int UPDATE_VALUE = 0;
    public static final int UPDATE_MAX = 1;
    protected final CharacteristicManager<FighterCharacteristic> m_manager;
    protected final FighterCharacteristicType m_toCharacType;
    protected final int m_updateType;
    protected final float m_ratio;
    
    public CharacBoostAnotherCharacProcedure(final CharacteristicManager<FighterCharacteristic> manager, final FighterCharacteristicType characToBoost, final float ratio, final int updateType) {
        super();
        this.m_manager = manager;
        this.m_ratio = ratio;
        this.m_toCharacType = characToBoost;
        this.m_updateType = updateType;
    }
    
    @Override
    public void execute(final FighterCharacteristicEvent event, final int value) {
        final FighterCharacteristic tocharacteristic = this.m_manager.getCharacteristic(this.m_toCharacType);
        Label_0193: {
            switch (event) {
                case VALUE_ADDED: {
                    final int valueToAdd = (int)Math.floor(this.m_ratio * value);
                    if (tocharacteristic != null) {
                        switch (this.m_updateType) {
                            case 0: {
                                tocharacteristic.add(valueToAdd);
                                break;
                            }
                            case 1: {
                                tocharacteristic.setMax(tocharacteristic.unboundedmax() + valueToAdd);
                                break;
                            }
                        }
                    }
                    break;
                }
                case VALUE_SUBSTRACTED: {
                    if (tocharacteristic == null) {
                        break;
                    }
                    final int valueToSubstract = (int)Math.floor(this.m_ratio * value);
                    switch (this.m_updateType) {
                        case 0: {
                            tocharacteristic.substract(valueToSubstract);
                            break Label_0193;
                        }
                        case 1: {
                            tocharacteristic.setMax(tocharacteristic.unboundedmax() - valueToSubstract);
                            break Label_0193;
                        }
                    }
                    break;
                }
            }
        }
    }
}
