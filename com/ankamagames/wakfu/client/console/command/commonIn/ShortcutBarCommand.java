package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;

public class ShortcutBarCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (!args.isEmpty()) {
            final byte barNum = Byte.parseByte(args.get(2));
            final ShortcutBarManager shortcutManager = WakfuGameEntity.getInstance().getLocalPlayer().getShortcutBarManager();
            shortcutManager.setSelectedShortcutBarNumber((byte)(shortcutManager.getCurrentBarType().getFirstIndex() + barNum), true);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
