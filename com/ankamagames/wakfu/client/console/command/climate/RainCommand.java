package com.ankamagames.wakfu.client.console.command.climate;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.climate.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class RainCommand implements Command
{
    protected static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        try {
            WakfuClientInstance.getInstance();
            final LocalPlayerCharacter player = WakfuClientInstance.getGameEntity().getLocalPlayer();
            final int px = player.getWorldCellX();
            final int py = player.getWorldCellY();
            final short instanceId = player.getInstanceId();
            final int argIndex = 2;
            final float value = (float)Double.parseDouble(args.get(argIndex));
            final ChangeRainConstantsMessage message = new ChangeRainConstantsMessage();
            message.setLocation(px, py);
            message.setInstanceId(instanceId);
            message.setPrecipitations(value);
            WakfuClientInstance.getInstance();
            WakfuClientInstance.getGameEntity().getNetworkEntity().sendMessage(message);
        }
        catch (Exception e) {
            manager.err("Probl\u00e8me lors de l'execution de la commande : " + e.toString());
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)RainCommand.class);
    }
}
