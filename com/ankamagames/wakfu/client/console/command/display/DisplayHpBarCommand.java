package com.ankamagames.wakfu.client.console.command.display;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.xulor2.property.*;

public class DisplayHpBarCommand implements Command
{
    private static boolean m_invert;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
        final boolean b = !wakfuGamePreferences.getBooleanValue(WakfuKeyPreferenceStoreEnum.DISPLAY_HP_BAR_KEY);
        wakfuGamePreferences.setValue(WakfuKeyPreferenceStoreEnum.DISPLAY_HP_BAR_KEY, b);
        displayHpBar(b);
    }
    
    public static void invertDisplay(final boolean invert) {
        DisplayHpBarCommand.m_invert = invert;
        displayHpBar(WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DISPLAY_HP_BAR_KEY));
    }
    
    public static void displayHpBar(final boolean enable) {
        PropertiesProvider.getInstance().setPropertyValue("timeline.displayHPBar", enable ^ DisplayHpBarCommand.m_invert);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        DisplayHpBarCommand.m_invert = false;
    }
}
