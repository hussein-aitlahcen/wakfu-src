package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.wakfu.client.sound.*;

public class PlayMusicCommand implements Command
{
    protected static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final long id = PrimitiveConverter.getLong(args.get(2));
        int gain = 100;
        if (args.size() > 3) {
            gain = PrimitiveConverter.getInteger(args.get(3));
            if (gain == 0) {
                gain = 100;
            }
        }
        WakfuSoundManager.getInstance().playMusic(id, gain / 100.0f);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayMusicCommand.class);
    }
}
