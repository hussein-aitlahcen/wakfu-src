package com.ankamagames.wakfu.client.network.protocol.frame.guild;

import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guild.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.common.game.guild.constant.*;
import com.ankamagames.framework.kernel.core.common.message.*;

class GuildErrorMessageRunner implements MessageRunner<GuildErrorMessage>
{
    @Override
    public boolean run(final GuildErrorMessage msg) {
        final GuildError error = msg.getError();
        if (error == null) {
            NetGuildFrame.m_logger.error((Object)"Message d'erreur de guilde inconnu !!!");
            return false;
        }
        String notifyError = null;
        switch (error) {
            case GUILD_CREATION_ERROR: {
                notifyError = WakfuTranslator.getInstance().getString("error.guild.creation");
                break;
            }
            case ALREADY_IN_GUILD: {
                notifyError = WakfuTranslator.getInstance().getString("error.guild.alreadyInGuild");
                WakfuGameEntity.getInstance().removeFrame(UIGuildCreatorFrame.getInstance());
                break;
            }
            case INVITED_NOT_FOUND: {
                notifyError = WakfuTranslator.getInstance().getString("group.error.unknown_user");
                break;
            }
            case BLAZON_ALREADY_EXIST: {
                notifyError = WakfuTranslator.getInstance().getString("error.guild.creation.existingBlazon");
                break;
            }
            case INVITED_ALREADY_REQUESTED: {
                notifyError = WakfuTranslator.getInstance().getString("group.error.invitationPending");
                break;
            }
            case INVITED_IN_GUILD: {
                notifyError = WakfuTranslator.getInstance().getString("guild.error.user.already.in.guild");
                break;
            }
            case NAME_ALREADY_EXIST: {
                notifyError = WakfuTranslator.getInstance().getString("error.guild.creation.existingName");
                break;
            }
            case GUILD_CREATION_NAME_INVALID: {
                notifyError = WakfuTranslator.getInstance().getString("error.guild.creation.invalidName");
                break;
            }
            case SOUL_AVATAR_CANT_BE_INVITED: {
                notifyError = WakfuTranslator.getInstance().getString("guild.error.user.is.soul.avatar");
                break;
            }
        }
        if (notifyError == null) {
            NetGuildFrame.m_logger.error((Object)("Message d'erreur de guilde non trait\u00e9 : " + error.name()));
            return false;
        }
        final ChatMessage notifyErrorMessage = new ChatMessage(notifyError);
        notifyErrorMessage.setPipeDestination(3);
        ChatManager.getInstance().pushMessage(notifyErrorMessage);
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 20059;
    }
}
