package com.ankamagames.wakfu.client.console.command.display;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;

public class SetFightLODLevelCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final WakfuGamePreferences gamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
        final int lodLevel = gamePreferences.getIntValue(KeyPreferenceStoreEnum.FIGHT_LOD_LEVEL_KEY);
        final int level = (lodLevel + 1) % 3;
        gamePreferences.setValue(KeyPreferenceStoreEnum.FIGHT_LOD_LEVEL_KEY, level);
        setFightLODLevel((byte)level);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    public static void setFightLODLevel(final byte level) {
        FightVisibilityManager.getInstance().setFightLODLevel(level);
    }
}
