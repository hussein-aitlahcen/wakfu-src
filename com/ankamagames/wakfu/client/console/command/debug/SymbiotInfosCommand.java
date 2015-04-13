package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.specifics.*;
import com.ankamagames.wakfu.common.datas.specific.*;

public class SymbiotInfosCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final WakfuGameEntity gameEntity = WakfuGameEntity.getInstance();
        final LocalPlayerCharacter localPlayer = gameEntity.getLocalPlayer();
        final Symbiot symbiot = localPlayer.getSymbiot();
        if (symbiot != null) {
            for (byte i = 0; i < symbiot.size(); ++i) {
                final BasicInvocationCharacteristics crea = symbiot.getCreatureParametersFromIndex(i);
                ConsoleManager.getInstance().customTrace(crea.toString() + "\n", 11141375);
            }
        }
        else {
            ConsoleManager.getInstance().log("pas de symbiote");
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
