package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.pvp.*;

public final class SuicideCommand implements Command
{
    private static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (WakfuGameEntity.getInstance().getLocalPlayer().isOnFight()) {
            return;
        }
        if (WakfuGameEntity.getInstance().getLocalPlayer().getActor().getCurrentPath() != null) {
            WakfuGameEntity.getInstance().getLocalPlayer().getActor().stopMoving();
        }
        final ActorSuicideRequestMessage message = new ActorSuicideRequestMessage();
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
        if (WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getPvpState().isActive()) {
            PvpInteractionManager.INSTANCE.startInteraction(new PvpInteractionHandler() {
                @Override
                public void onFinish() {
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
                }
                
                @Override
                public void onCancel() {
                }
            });
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SuicideCommand.class);
    }
}
