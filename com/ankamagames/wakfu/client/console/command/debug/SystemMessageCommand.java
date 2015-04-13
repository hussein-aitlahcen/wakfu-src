package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.systemMessage.*;

public class SystemMessageCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        String text = null;
        int duration = Integer.MAX_VALUE;
        if (args.size() >= 4) {
            text = args.get(3).trim();
        }
        if (args.size() >= 3) {
            try {
                duration = Integer.parseInt(args.get(2));
            }
            catch (NumberFormatException ex) {}
        }
        WakfuSystemMessageManager.getInstance().showMessage(new BannerSystemMessageData(WakfuSystemMessageManager.SystemMessageType.CHALLENGE, "Message", 500, 2500, "AnimChallenge1"));
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
