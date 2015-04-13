package com.ankamagames.wakfu.client.core.emote;

import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public interface EmoteRunnableHandler
{
    public static final EmoteRunnableHandler EMOTE_MESSAGE_RUNNABLE_HANDLER = new EmoteRunnableHandler() {
        @Override
        public void runEmote(Emote emote, long targetId) {
            ActorPlayEmoteRequestMessage actorPlayEmoteRequestMessage;
            actorPlayEmoteRequestMessage = new ActorPlayEmoteRequestMessage();
            actorPlayEmoteRequestMessage.setEmoteId(emote.getId());
            actorPlayEmoteRequestMessage.setEmoteOccupation(emote.isInfiniteDuration());
            actorPlayEmoteRequestMessage.setTargetId(targetId);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(actorPlayEmoteRequestMessage);
        }
        
        @Override
        public boolean emoteMustBeLearned() {
            return true;
        }
    };
    
    void runEmote(Emote p0, long p1);
    
    boolean emoteMustBeLearned();
}
