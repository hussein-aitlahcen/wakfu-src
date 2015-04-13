package com.ankamagames.wakfu.client.core.game.travel.provider;

import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.travel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.travel.character.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.client.core.game.travel.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;

public class BoatTravelProvider extends ClientTravelProvider
{
    @Override
    public void discover(final MapInteractiveElement travelMachine, final BasicCharacterInfo user) {
    }
    
    @Override
    public void activate(final MapInteractiveElement travelMachine, final BasicCharacterInfo user) {
        UIBoatFrame.getInstance().initialize((TravelMachine)travelMachine);
    }
    
    @Override
    public TravelError checkTravel(final BasicCharacterInfo user, final MapInteractiveElement sourceMachine, final long destinationId) {
        final BoatLink link = TravelInfoManager.INSTANCE.getBoatLink(sourceMachine.getId(), destinationId);
        if (!link.isCriterionValid(user, sourceMachine)) {
            return TravelError.CRITERION_FAIL;
        }
        if (!link.isCriterionDisplayValid(user, sourceMachine)) {
            return TravelError.CRITERION_FAIL;
        }
        final int cost = link.getCost();
        if (cost > 0 && !((PlayerCharacter)user).hasSubscriptionRight(SubscriptionRight.USE_BOAT)) {
            return TravelError.NOT_SUBSCRIBER;
        }
        if (TravelHelper.needsToPayForBoat(((LocalPlayerCharacter)user).getTravelHandler(), link) && user.getKamasCount() < cost) {
            return TravelError.NOT_ENOUGH_KAMA;
        }
        return TravelError.NO_ERROR;
    }
    
    @Override
    protected void executeTravel(final BasicCharacterInfo user, final MapInteractiveElement sourceMachine, final long destinationId) {
        final TravelRequestMessage msg = new TravelRequestMessage(sourceMachine.getId(), destinationId);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
        final BoatLink link = TravelInfoManager.INSTANCE.getBoatLink(sourceMachine.getId(), destinationId);
        final TravelHandler travelHandler = WakfuGameEntity.getInstance().getLocalPlayer().getTravelHandler();
        travelHandler.addDiscoveredBoat((int)link.getId());
    }
    
    @Override
    public boolean canUse(final LocalPlayerCharacter player, final TravelMachine machine) {
        return true;
    }
    
    @Override
    public TravelType getType() {
        return TravelType.BOAT;
    }
    
    @Override
    public String getOverHeadInfo(final TravelMachine travelMachine) {
        final BoatTicketOfficeFieldProvider boatTicketOfficeFieldProvider = new BoatTicketOfficeFieldProvider();
        boatTicketOfficeFieldProvider.initialise(travelMachine.getId(), travelMachine);
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(WakfuTranslator.getInstance().getString(83, (int)travelMachine.getId(), new Object[0])).newLine().newLine();
        sb.append(WakfuTranslator.getInstance().getString("destinations")).newLine();
        sb.append(boatTicketOfficeFieldProvider.getDescription());
        return sb.finishAndToString();
    }
    
    @Nullable
    @Override
    public String getTravelCostLabel(final TravelMachine machine) {
        return null;
    }
}
