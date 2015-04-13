package com.ankamagames.wakfu.client.console.command.xulor;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.xulor2.*;

public class ReloadThemeCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        manager.trace("Rechargement du fichier de th\u00e8me en cours...");
        Xulor.getInstance().getDocumentParser().reloadTheme();
        manager.trace("Termin\u00e9.");
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
