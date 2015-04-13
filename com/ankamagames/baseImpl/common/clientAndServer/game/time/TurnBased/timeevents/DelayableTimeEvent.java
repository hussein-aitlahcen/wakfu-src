package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;

public abstract class DelayableTimeEvent implements TimeEvent
{
    private TimeEventObsolescenceListener<DelayableTimeEvent> m_obsolescenceListener;
    
    public abstract long getFighterToAttachToById();
    
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void sendTo(final TimeEventHandler eventHandler) {
        if (!this.isValid()) {
            return;
        }
        this.safelySendTo(eventHandler);
    }
    
    protected abstract void safelySendTo(final TimeEventHandler p0);
    
    protected abstract int specializedSerializedSize();
    
    protected abstract void specializedSerialize(final ByteBuffer p0);
    
    protected abstract void specializedRead(final TimelineUnmarshallingContext p0, final ByteBuffer p1);
    
    public int serializedSize() {
        return DelayableTimeEventType.byClass(this.getClass()).serializedSize(this);
    }
    
    public void serialize(final ByteBuffer buffer) {
        DelayableTimeEventType.byClass(this.getClass()).serializeDelayableTimeEvent(this, buffer);
    }
    
    public static DelayableTimeEvent deserialize(final TimelineUnmarshallingContext ctx, final ByteBuffer buffer) {
        final DelayableTimeEvent result = DelayableTimeEventType.createTimeEvent(buffer);
        result.specializedRead(ctx, buffer);
        return result;
    }
    
    public void clean() {
    }
    
    public void setObsolescenceListener(final TimeEventObsolescenceListener<DelayableTimeEvent> obsolescenceListener) {
        this.m_obsolescenceListener = obsolescenceListener;
    }
    
    public void notifyBeingObsolete() {
        if (this.m_obsolescenceListener != null) {
            this.m_obsolescenceListener.onBeingObsolete(this);
        }
    }
}
