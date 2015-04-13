package com.ankamagames.wakfu.client.console.command.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;

public class ShowAggroCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("Aggro List \n");
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer != null && localPlayer.getCurrentFight() != null) {
            for (final CharacterInfo characterInfo : localPlayer.getCurrentFight().getFighters()) {
                buffer.append(characterInfo.aggroToString());
            }
        }
        else {
            buffer.append("pas de combat en cours \n");
        }
        ConsoleManager.getInstance().trace(buffer.toString());
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
