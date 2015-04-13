package com.ankamagames.wakfu.client.console.command.display;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class ShowHideNameOverheadsCommand implements Command
{
    private static boolean m_hide;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (ShowHideNameOverheadsCommand.m_hide) {
            UIOverHeadInfosFrame.getInstance().hideNameOverheads();
        }
        else {
            UIOverHeadInfosFrame.getInstance().displayNameOverheads();
        }
        ShowHideNameOverheadsCommand.m_hide = !ShowHideNameOverheadsCommand.m_hide;
    }
    
    public static boolean isActive() {
        return ShowHideNameOverheadsCommand.m_hide;
    }
    
    public static void setHide(final boolean hide) {
        ShowHideNameOverheadsCommand.m_hide = hide;
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        ShowHideNameOverheadsCommand.m_hide = false;
    }
}
