package com.ankamagames.baseImpl.graphics.ui.shortcuts;

import com.ankamagames.baseImpl.client.proxyclient.base.console.*;

public final class ShortcutManager extends AbstractShortcutManager
{
    private static final ShortcutManager m_instance;
    
    public static ShortcutManager getInstance() {
        return ShortcutManager.m_instance;
    }
    
    @Override
    public boolean executeShortcut(final Shortcut shortcut) {
        if (shortcut.getConsoleCommand() != null) {
            String input = shortcut.getConsoleCommand();
            if (shortcut.getParamString() != null) {
                input += shortcut.getParamString();
            }
            ConsoleManager.getInstance().parseInput(input, false);
            return true;
        }
        if (shortcut.getShortcutTrigger() != null) {
            shortcut.getShortcutTrigger().run();
            return true;
        }
        return false;
    }
    
    static {
        m_instance = new ShortcutManager();
    }
}
