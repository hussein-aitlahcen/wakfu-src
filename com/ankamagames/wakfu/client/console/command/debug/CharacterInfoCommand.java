package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public class CharacterInfoCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final WakfuGameEntity gameEntity = WakfuGameEntity.getInstance();
        final LocalPlayerCharacter localPlayer = gameEntity.getLocalPlayer();
        for (final FighterCharacteristicType c : FighterCharacteristicType.values()) {
            final FighterCharacteristic charac = localPlayer.getCharacteristic((CharacteristicType)c);
            final int value = localPlayer.getCharacteristicValue(c);
            ConsoleManager.getInstance().customTrace(c.name() + " \t" + value + "/" + charac.max(), 11141375);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
