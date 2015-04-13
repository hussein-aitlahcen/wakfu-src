package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;

public class ChangeShortcutBarTypeCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (!args.isEmpty()) {
            final ShortcutBarManager manager2 = WakfuGameEntity.getInstance().getLocalPlayer().getShortcutBarManager();
            if (manager2.isDisplaySymbiot()) {
                return;
            }
            manager2.setCurrentBarType((manager2.getCurrentBarType() == ShortCutBarType.FIGHT) ? ShortCutBarType.WORLD : ShortCutBarType.FIGHT, true);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
