package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;

public class UseShortcutItemCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (args.get(2).length() >= 2) {
            final ShortcutBarManager shortcutBarManager = WakfuGameEntity.getInstance().getLocalPlayer().getShortcutBarManager();
            byte shortcutBarIndex = Byte.parseByte(args.get(2).charAt(1) + "");
            shortcutBarIndex = shortcutBarManager.getCurrentBar(shortcutBarIndex).getIndex();
            shortcutBarManager.useShortCut(shortcutBarIndex, Short.parseShort(args.get(2).charAt(0) + ""));
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
