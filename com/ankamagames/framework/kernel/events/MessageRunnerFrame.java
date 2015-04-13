package com.ankamagames.framework.kernel.events;

import com.ankamagames.framework.kernel.core.common.message.*;
import gnu.trove.*;

public abstract class MessageRunnerFrame implements MessageFrame
{
    private final TIntObjectHashMap<MessageRunner> m_runners;
    
    protected MessageRunnerFrame(final MessageRunner... runners) {
        super();
        final int size = runners.length;
        this.m_runners = new TIntObjectHashMap<MessageRunner>(size);
        for (final MessageRunner runner : runners) {
            if (this.m_runners.put(runner.getProtocolId(), runner) != null) {
                throw new IllegalArgumentException("Il existe d\u00e9j\u00e0 un traitement de message pour le message " + runner.getProtocolId());
            }
        }
    }
    
    @Override
    public final boolean onMessage(final Message message) {
        final MessageRunner runner = this.m_runners.get(message.getId());
        return runner == null || runner.run(message);
    }
    
    protected final <T extends MessageRunner> void forEachRunner(final TObjectProcedure<T> proc) {
        this.m_runners.forEachValue((TObjectProcedure<MessageRunner>)proc);
    }
}
