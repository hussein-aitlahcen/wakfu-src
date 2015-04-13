package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

public class CommandWhereIAm implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final WakfuGameEntity gameEntity = WakfuGameEntity.getInstance();
        final LocalPlayerCharacter localPlayer = gameEntity.getLocalPlayer();
        final CharacterActor actor = localPlayer.getActor();
        manager.trace(localPlayer.getControllerName() + "[" + Integer.toString(actor.getWorldCellX()) + "," + Integer.toString(actor.getWorldCellY()) + "," + Double.toString(actor.getAltitude()) + "]" + " @" + Short.toString(localPlayer.getInstanceId()));
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
