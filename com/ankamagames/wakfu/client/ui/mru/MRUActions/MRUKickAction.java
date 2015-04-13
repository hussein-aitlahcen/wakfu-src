package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUKickAction extends AbstractMRUAction
{
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUKickAction();
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof PlayerCharacter)) {
            return false;
        }
        final DimensionalBagView dimensionalBag = WakfuGameEntity.getInstance().getLocalPlayer().getVisitingDimentionalBag();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return dimensionalBag != null && dimensionalBag.getOwnerId() == WakfuGameEntity.getInstance().getLocalPlayer().getId() && !localPlayer.isWaitingForResult() && !localPlayer.isOnFight();
    }
    
    @Override
    public void run() {
        final PlayerCharacter player = (PlayerCharacter)this.m_source;
        final PSKickUserRequestMessage msg = new PSKickUserRequestMessage();
        msg.setCharacterId(player.getId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.CHARACTER_KICK_ACTION;
    }
    
    @Override
    public String getTranslatorKey() {
        return "kick";
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.KICK.m_id;
    }
}
