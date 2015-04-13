package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.emote.*;

public class SitCommand implements Command
{
    protected static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (!this.isRunnable(pattern)) {
            return;
        }
        run();
    }
    
    public static void run() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractOccupation occupation = localPlayer.getCurrentOccupation();
        if (occupation != null && occupation.getOccupationTypeId() != 1) {
            return;
        }
        final CharacterActor actor = WakfuGameEntity.getInstance().getLocalPlayer().getActor();
        final Direction8 direction = actor.getDirection();
        Direction8 newDirection = null;
        switch (direction) {
            case SOUTH:
            case EAST: {
                newDirection = Direction8.SOUTH_EAST;
                break;
            }
            case NORTH:
            case WEST: {
                newDirection = Direction8.NORTH_WEST;
                break;
            }
        }
        if (newDirection != null) {
            final ActorDirectionChangeRequestMessage directionMsg = new ActorDirectionChangeRequestMessage(newDirection);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(directionMsg);
        }
        final ActorRestRequestMessage message = new ActorRestRequestMessage();
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
    }
    
    private boolean isRunnable(final CommandPattern pattern) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final Emote emote = ReferenceEmoteManager.INSTANCE.getEmote(pattern);
        if (emote == null || !player.getEmoteHandler().knowEmote(emote.getId())) {
            return false;
        }
        final CharacterActor actor = player.getActor();
        return actor.getCurrentPath() == null;
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SitCommand.class);
    }
}
