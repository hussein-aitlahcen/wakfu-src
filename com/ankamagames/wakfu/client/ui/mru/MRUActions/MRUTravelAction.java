package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.common.game.world.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.travel.*;

public final class MRUTravelAction extends MRUInteractifMachine
{
    @Override
    public MRUTravelAction getCopy() {
        return new MRUTravelAction();
    }
    
    @Override
    public boolean isEnabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final TravelMachine machine = (TravelMachine)this.m_source;
        return machine.canUse(localPlayer) && super.isEnabled();
    }
    
    @Override
    public String getLabel() {
        final String label = super.getLabel();
        final TravelMachine travelMachine = (TravelMachine)this.m_source;
        final String costLabel = travelMachine.getTravelCostLabel();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (costLabel == null || costLabel.isEmpty()) {
            return label;
        }
        final TextWidgetFormater sb = new TextWidgetFormater().append(label);
        final SubscribeWorldAccess worldSubscription = WorldInfoManager.getInstance().getInfo(travelMachine.getWorld()).m_subscriberWorld;
        if (worldSubscription != SubscribeWorldAccess.NON_ABO_FREE_ACCESS) {
            sb.newLine().append(WakfuTranslator.getInstance().getString("desc.cost", costLabel));
        }
        final TravelMachine machine = (TravelMachine)this.m_source;
        final TravelProvider travelProvider = TravelHelper.getProvider(machine.getTravelType());
        final TravelError travelError = travelProvider.checkTravel(localPlayer, machine, -1L);
        if (travelError == TravelError.NOT_SUBSCRIBER) {
            sb.newLine().addColor(MRUTravelAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed"));
        }
        return sb.finishAndToString();
    }
}
