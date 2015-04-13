package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.common.game.world.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.account.*;

public final class MRUZaapOutTravelAction extends MRUInteractifMachine
{
    @Override
    public MRUZaapOutTravelAction getCopy() {
        return new MRUZaapOutTravelAction();
    }
    
    @Override
    public boolean isEnabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final TravelMachine machine = (TravelMachine)this.m_source;
        return machine.canUse(localPlayer) && super.isEnabled();
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.ZAAP.m_id;
    }
    
    @Override
    public String getLabel() {
        final String label = super.getLabel();
        final TravelMachine travelMachine = (TravelMachine)this.m_source;
        final String costLabel = travelMachine.getTravelCostLabel();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (costLabel == null || costLabel.length() == 0) {
            return label;
        }
        final TextWidgetFormater sb = new TextWidgetFormater().append(label);
        final WorldInfoManager.WorldInfo worldInfo = WorldInfoManager.getInstance().getInfo(travelMachine.getWorld());
        SubscribeWorldAccess worldSubscription;
        if (worldInfo == null) {
            worldSubscription = SubscribeWorldAccess.NON_ABO_FREE_ACCESS;
        }
        else {
            worldSubscription = worldInfo.m_subscriberWorld;
        }
        if (SubscribeWorldAccess.NON_ABO_FREE_ACCESS != worldSubscription) {
            sb.newLine().append(WakfuTranslator.getInstance().getString("desc.cost", costLabel));
        }
        if (!WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer)) {
            sb.newLine().addColor(MRUZaapOutTravelAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed"));
        }
        return sb.finishAndToString();
    }
}
