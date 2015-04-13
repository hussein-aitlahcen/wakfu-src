package com.ankamagames.wakfu.common.game.travel;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public abstract class TravelProvider
{
    public void travel(final BasicCharacterInfo user, final MapInteractiveElement sourceMachine, final long destinationId) {
        final TravelError result = this.checkTravel(user, sourceMachine, destinationId);
        if (result != TravelError.NO_ERROR) {
            this.onError(result);
            return;
        }
        this.executeTravel(user, sourceMachine, destinationId);
    }
    
    public abstract void discover(final MapInteractiveElement p0, final BasicCharacterInfo p1);
    
    public abstract void activate(final MapInteractiveElement p0, final BasicCharacterInfo p1);
    
    public abstract TravelError checkTravel(final BasicCharacterInfo p0, final MapInteractiveElement p1, final long p2);
    
    protected abstract void executeTravel(final BasicCharacterInfo p0, final MapInteractiveElement p1, final long p2);
    
    protected abstract void onError(final TravelError p0);
    
    public abstract TravelType getType();
    
    @Override
    public String toString() {
        return "TravelProvider{" + this.getClass().getSimpleName() + ", TravelType:" + this.getType() + '}';
    }
}
