package com.ankamagames.wakfu.client.ui.protocol.frame.runners.guild;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.guild.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.xulor2.core.messagebox.*;

public class GuildRankSwitchRequestRunner implements MessageRunner
{
    @Override
    public int getProtocolId() {
        return 18205;
    }
    
    @Override
    public boolean run(final Message message) {
        final UIGuildChangeRankMessage msg = (UIGuildChangeRankMessage)message;
        final long characterId = msg.getCharacterId();
        final long rank = msg.getRankId();
        final GuildMember guildMember = WakfuGuildView.getInstance().getMember(characterId);
        final long bestRank = WakfuGuildView.getInstance().getBestRank();
        if (rank != bestRank && guildMember.getRank() != bestRank) {
            WakfuGuildView.getInstance().requestMemberRankChange(characterId, rank);
            return false;
        }
        String msgBoxMessage = null;
        final long localPlayerId = WakfuGameEntity.getInstance().getLocalPlayer().getId();
        if (characterId != localPlayerId) {
            msgBoxMessage = WakfuTranslator.getInstance().getString("guild.warning.giveMasterRank", guildMember.getName());
        }
        else {
            msgBoxMessage = WakfuTranslator.getInstance().getString("guild.warning.removeSelfMasterRank", guildMember.getName());
        }
        final MessageBoxControler controler = Xulor.getInstance().msgBox(msgBoxMessage, 6L, 102, 3);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 2) {
                    WakfuGuildView.getInstance().requestMemberRankChange(characterId, rank);
                }
                else {
                    PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGuildView.getInstance().getMemberView(characterId), "rank");
                }
            }
        });
        return false;
    }
}
