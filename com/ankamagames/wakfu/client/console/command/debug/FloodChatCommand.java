package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import java.lang.reflect.*;

public class FloodChatCommand implements Command
{
    private long m_quantity;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (args.size() != 3) {
            return;
        }
        final ArrayList<String> fields = new ArrayList<String>();
        for (final Field f : WakfuTranslator.class.getDeclaredFields()) {
            try {
                if (f.getType() == String.class) {
                    fields.add(WakfuTranslator.getInstance().getString((String)f.get(WakfuTranslator.getInstance())));
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        this.m_quantity = Long.parseLong(args.get(2));
        for (int i = 0; i < this.m_quantity; ++i) {
            ProcessScheduler.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    final String message = fields.get(MathHelper.random(fields.size()));
                    final ChatMessage message2 = new ChatMessage(message);
                    message2.setPipeDestination(4);
                    ChatManager.getInstance().pushMessage(message2);
                }
            }, i, 1);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
