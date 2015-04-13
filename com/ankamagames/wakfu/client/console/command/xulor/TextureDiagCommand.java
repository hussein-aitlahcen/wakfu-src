package com.ankamagames.wakfu.client.console.command.xulor;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.xulor2.*;

public class TextureDiagCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        manager.trace("Cr\u00e9ation des images en cours.");
        Xulor.getInstance().getDocumentParser().textureLoadDiag();
        manager.trace("Fin de la cr\u00e9ation des images");
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
