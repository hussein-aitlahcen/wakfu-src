package com.ankamagames.wakfu.client.chat.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;

public class PartyInvitationCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final String userName = args.get(2).replaceAll("\"", "");
        WakfuGameEntity.getInstance().getLocalPlayer().getPartyComportment().inviteSomeone(userName);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
