package com.ankamagames.wakfu.client.core.game.travel.provider;

import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.travel.character.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.travel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import org.jetbrains.annotations.*;

public class ZaapTravelProvider extends ClientTravelProvider
{
    @Override
    public void discover(final MapInteractiveElement travelMachine, final BasicCharacterInfo user) {
        final TravelHandler travelHandler = WakfuGameEntity.getInstance().getLocalPlayer().getTravelHandler();
        ((WakfuClientMapInteractiveElement)travelMachine).sendActionMessage(((ClientMapInteractiveElement)travelMachine).getDefaultAction());
        travelHandler.addDiscoveredZaap((int)travelMachine.getId());
    }
    
    @Override
    public void activate(final MapInteractiveElement travelMachine, final BasicCharacterInfo user) {
        UIZaapFrame.getInstance().initialize((TravelMachine)travelMachine);
    }
    
    @Override
    public TravelError checkTravel(final BasicCharacterInfo user, final MapInteractiveElement sourceMachine, final long destinationId) {
        final int cost = this.getZaapCost(sourceMachine, destinationId);
        if (user.getKamasCount() < cost) {
            return TravelError.NOT_ENOUGH_KAMA;
        }
        final ZaapInfo destinationInfo = TravelInfoManager.INSTANCE.getInfo(TravelType.ZAAP, destinationId);
        if (!destinationInfo.isDestinationValid(user)) {
            return TravelError.CRITERION_FAIL;
        }
        return TravelError.NO_ERROR;
    }
    
    private int getZaapCost(final MapInteractiveElement sourceMachine, final long destinationId) {
        if (SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.ZAAP_FREE)) {
            return 0;
        }
        return TravelInfoManager.INSTANCE.getZaapLink(sourceMachine.getId(), destinationId).getCost();
    }
    
    @Override
    protected void executeTravel(final BasicCharacterInfo user, final MapInteractiveElement sourceMachine, final long destinationId) {
        final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(800102);
        if (system != null) {
            system.setTarget(WakfuGameEntity.getInstance().getLocalPlayer().getMobile());
            IsoParticleSystemManager.getInstance().addParticleSystem(system);
        }
        UIZaapFrame.getInstance().hideDialog();
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                final TravelRequestMessage msg = new TravelRequestMessage(sourceMachine.getId(), destinationId);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
                WakfuGameEntity.getInstance().removeFrame(UIZaapFrame.getInstance());
            }
        }, 500L, 1);
    }
    
    @Override
    public boolean canUse(final LocalPlayerCharacter player, final TravelMachine machine) {
        return true;
    }
    
    @Override
    public TravelType getType() {
        return TravelType.ZAAP;
    }
    
    @Override
    public String getOverHeadInfo(final TravelMachine travelMachine) {
        return WakfuTranslator.getInstance().getString(36, (int)travelMachine.getId(), new Object[0]);
    }
    
    @Nullable
    @Override
    public String getTravelCostLabel(final TravelMachine machine) {
        return null;
    }
}
