package com.ankamagames.wakfu.client.core.krosmoz;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.kernel.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.ui.protocol.message.krozmoz.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public abstract class KrosmozGameFrame extends MessageRunnerFrame implements KrosmozGameCommandInterface
{
    public static int GAME_ID;
    private SWFWrapper m_swfWrapper;
    
    protected KrosmozGameFrame(final MessageRunner... runners) {
        super(getMessageRunners(runners));
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        this.forEachRunner((TObjectProcedure<MessageRunner>)new TObjectProcedure<KrozmozGameMessageRunner>() {
            @Override
            public boolean execute(final KrozmozGameMessageRunner runner) {
                runner.setFrame(KrosmozGameFrame.this);
                return true;
            }
        });
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void setSwfWrapper(final SWFWrapper swfWrapper) {
        this.m_swfWrapper = swfWrapper;
    }
    
    public SWFWrapper getSwfWrapper() {
        return this.m_swfWrapper;
    }
    
    private static MessageRunner[] getMessageRunners(final MessageRunner... runners) {
        final MessageRunner[] innerRunners = { new KrosmozCommandReceivedMessageRunner() };
        final MessageRunner[] allRunners = new MessageRunner[innerRunners.length + runners.length];
        System.arraycopy(innerRunners, 0, allRunners, 0, innerRunners.length);
        System.arraycopy(runners, 0, allRunners, innerRunners.length, runners.length);
        return allRunners;
    }
    
    static {
        KrosmozGameFrame.GAME_ID = 3;
    }
    
    public static class KrosmozCommandReceivedMessageRunner extends KrozmozGameMessageRunner<UIKrosmozCommandReceivedMessage>
    {
        @Override
        public boolean run(final UIKrosmozCommandReceivedMessage msg) {
            this.m_frame.onCommandReceived(msg.getCommand(), msg.getParams());
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 17350;
        }
    }
}
