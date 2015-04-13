package com.ankamagames.wakfu.client.core.game.travel.provider;

import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.travel.character.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.travel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.common.game.world.*;

public class DragoTravelProvider extends ClientTravelProvider
{
    @Override
    public void discover(final MapInteractiveElement travelMachine, final BasicCharacterInfo user) {
        final TravelHandler travelHandler = WakfuGameEntity.getInstance().getLocalPlayer().getTravelHandler();
        ((WakfuClientMapInteractiveElement)travelMachine).sendActionMessage(((ClientMapInteractiveElement)travelMachine).getDefaultAction());
        travelHandler.addDiscoveredDrago((int)travelMachine.getId());
    }
    
    @Override
    public void activate(final MapInteractiveElement travelMachine, final BasicCharacterInfo user) {
        UIDragoMapFrame.getInstance().initialize((TravelMachine)travelMachine);
    }
    
    @Override
    public TravelError checkTravel(final BasicCharacterInfo user, final MapInteractiveElement sourceMachine, final long destinationId) {
        final int cost = this.getCost(sourceMachine);
        if (cost > 0 && !WakfuAccountPermissionContext.SUBSCRIBER.hasPermission((WakfuAccountInformationHolder)user)) {
            return TravelError.NOT_SUBSCRIBER;
        }
        if (user.getKamasCount() < cost) {
            return TravelError.NOT_ENOUGH_KAMA;
        }
        final DragoInfo info = TravelInfoManager.INSTANCE.getInfo(this.getType(), sourceMachine.getId());
        if (!info.isDestinationValid(user) || (user.getCitizenComportment().getPvpState().isActive() && !WakfuGameEntity.getInstance().getLocalPlayer().isOnFriendNation())) {
            return TravelError.CRITERION_FAIL;
        }
        return TravelError.NO_ERROR;
    }
    
    @Override
    protected void executeTravel(final BasicCharacterInfo user, final MapInteractiveElement sourceMachine, final long destinationId) {
        final TravelRequestMessage msg = new TravelRequestMessage(sourceMachine.getId(), destinationId);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    @Override
    public boolean canUse(final LocalPlayerCharacter player, final TravelMachine machine) {
        return (player.isOnFriendNation() || !player.getCitizenComportment().getPvpState().isActive()) && this.checkTravel(player, machine, -1L) == TravelError.NO_ERROR;
    }
    
    @Override
    public TravelType getType() {
        return TravelType.DRAGO;
    }
    
    @Override
    public String getOverHeadInfo(final TravelMachine travelMachine) {
        return WakfuTranslator.getInstance().getString(82, (int)travelMachine.getId(), new Object[0]);
    }
    
    @Nullable
    @Override
    public String getTravelCostLabel(final TravelMachine machine) {
        final LocalPlayerCharacter user = WakfuGameEntity.getInstance().getLocalPlayer();
        final TextWidgetFormater sb = new TextWidgetFormater();
        final TravelError travelError = this.checkTravel(user, machine, -1L);
        sb.addColor(((travelError == TravelError.NO_ERROR) ? Color.GREEN : Color.RED).getRGBtoHex());
        sb.append(this.getCost(machine)).append("§");
        if (travelError == TravelError.CRITERION_FAIL) {
            sb.newLine().append(WakfuTranslator.getInstance().getString("chat.travel.invalidCriterion"));
        }
        return sb.finishAndToString();
    }
    
    private int getCost(final MapInteractiveElement sourceMachine) {
        final SubscribeWorldAccess worldSubscription = WorldInfoManager.getInstance().getInfo(sourceMachine.getWorld()).m_subscriberWorld;
        return (worldSubscription == SubscribeWorldAccess.NON_ABO_FREE_ACCESS && false) ? 1 : 0;
    }
}
