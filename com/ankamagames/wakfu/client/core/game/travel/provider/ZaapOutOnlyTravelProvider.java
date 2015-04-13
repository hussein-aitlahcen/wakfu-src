package com.ankamagames.wakfu.client.core.game.travel.provider;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import com.ankamagames.wakfu.common.game.travel.*;

public final class ZaapOutOnlyTravelProvider extends ZaapTravelProvider
{
    @Override
    public String getOverHeadInfo(final TravelMachine travelMachine) {
        return WakfuTranslator.getInstance().getString("haven.world.zaap.name");
    }
    
    @Override
    public String getTravelCostLabel(final TravelMachine machine) {
        return "travelCostLabel";
    }
    
    @Override
    public boolean canUse(final LocalPlayerCharacter player, final TravelMachine machine) {
        return true;
    }
    
    @Override
    public void discover(final MapInteractiveElement travelMachine, final BasicCharacterInfo user) {
    }
    
    @Override
    public void activate(final MapInteractiveElement travelMachine, final BasicCharacterInfo user) {
        UIZaapFrame.getInstance().initialize((TravelMachine)travelMachine);
    }
    
    @Override
    public TravelError checkTravel(final BasicCharacterInfo user, final MapInteractiveElement sourceMachine, final long destinationId) {
        if (!(sourceMachine instanceof HavenWorldTravelMachine)) {
            return TravelError.NOT_ZAAP;
        }
        if (!(user instanceof GuildUser)) {
            return TravelError.NOT_ZAAP;
        }
        final int cost = ((HavenWorldTravelMachine)sourceMachine).getTravelCost(((GuildUser)user).getGuildHandler().getGuildId());
        if (user.getKamasCount() < cost) {
            return TravelError.NOT_ENOUGH_KAMA;
        }
        final ZaapInfo destinationInfo = TravelInfoManager.INSTANCE.getInfo(TravelType.ZAAP, destinationId);
        if (!destinationInfo.isDestinationValid(user)) {
            return TravelError.CRITERION_FAIL;
        }
        return TravelError.NO_ERROR;
    }
    
    @Override
    public TravelType getType() {
        return TravelType.ZAAP_OUT_ONLY;
    }
}
