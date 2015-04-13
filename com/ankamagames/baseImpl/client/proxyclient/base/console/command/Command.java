package com.ankamagames.baseImpl.client.proxyclient.base.console.command;

import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;

public interface Command
{
    void execute(ConsoleManager p0, CommandPattern p1, ArrayList<String> p2);
    
    boolean isPassThrough();
}
