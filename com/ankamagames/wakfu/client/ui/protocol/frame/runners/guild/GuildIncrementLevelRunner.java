package com.ankamagames.wakfu.client.ui.protocol.frame.runners.guild;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.guild.bonus.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.guild.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.datas.guild.agt_like.*;

public class GuildIncrementLevelRunner implements MessageRunner
{
    @Override
    public boolean run(final Message message) {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity != null) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (!localPlayer.isInGuild()) {
                return false;
            }
            final GuildBonusDataAGT bonus = GuildHelper.getLevelBonusFor(localPlayer.getGuildHandler().getLevel() + 1);
            if (bonus == null) {
                return false;
            }
            networkEntity.sendMessage(new GuildAddBonusRequestMessage(bonus.get().getId()));
        }
        else {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
        }
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 18222;
    }
}
