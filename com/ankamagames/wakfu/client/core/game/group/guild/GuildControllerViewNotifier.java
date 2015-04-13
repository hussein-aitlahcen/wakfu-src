package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

public class GuildControllerViewNotifier extends GuildController
{
    public GuildControllerViewNotifier(final Guild guild) {
        super(guild);
    }
    
    @Override
    public void changeMemberGuildPoints(final long memberId, final int points) throws GuildException {
        final GuildMember member = this.getGuild().getMember(memberId);
        final int oldGuildPoints = member.getGuildPoints();
        super.changeMemberGuildPoints(memberId, points);
        final int newGuildPoints = member.getGuildPoints();
        if (oldGuildPoints == newGuildPoints) {
            return;
        }
        if (member.getId() != WakfuGameEntity.getInstance().getLocalPlayer().getId()) {
            return;
        }
        final String mess = WakfuTranslator.getInstance().getString("guild.chatPointsGain", newGuildPoints - oldGuildPoints);
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("chat.pipeName.guild"), mess);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
}
