package com.ankamagames.wakfu.client.console.command.debug.anim;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class AnmDebuggerCommand implements Command
{
    private static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        loadAnmDebugger();
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    public static void loadAnmDebugger() {
        PropertiesProvider.getInstance().setPropertyValue("debug.anm", AnmDebuggerFieldProvider.getInstance());
        Xulor.getInstance().load("debugAnmDialog", Dialogs.getDialogPath("debugAnmDialog"), 1L, (short)10000);
        Xulor.getInstance().putActionClass("wakfu.debugAnm", DebugAnmActions.class);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnmDebuggerCommand.class);
    }
}
