package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMIHideAllEquipmentsAction extends HMIAction
{
    @Override
    protected boolean initialize(final String parameters) {
        return true;
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.HIDE_ALL_EQUIPMENTS;
    }
}
