package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;

public class CharacterBookCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final UICharacterSheetFrame bookFrame = UICharacterSheetFrame.getInstance();
        if (!WakfuGameEntity.getInstance().hasFrame(bookFrame)) {
            WakfuGameEntity.getInstance().pushFrame(bookFrame);
        }
        else {
            WakfuGameEntity.getInstance().removeFrame(bookFrame);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
