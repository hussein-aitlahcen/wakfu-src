package com.ankamagames.wakfu.client.core.krosmoz;

import com.ankamagames.framework.kernel.core.common.message.*;

public abstract class KrozmozGameMessageRunner<T extends Message> implements MessageRunner<T>
{
    protected KrosmozGameFrame m_frame;
    
    public KrosmozGameFrame getFrame() {
        return this.m_frame;
    }
    
    public void setFrame(final KrosmozGameFrame frame) {
        this.m_frame = frame;
    }
}
