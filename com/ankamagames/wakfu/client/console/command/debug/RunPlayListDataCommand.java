package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.wakfu.client.sound.*;

public class RunPlayListDataCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final short playListId = PrimitiveConverter.getShort(args.get(2));
        WakfuSoundManager.getInstance().playPlayList(playListId);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
