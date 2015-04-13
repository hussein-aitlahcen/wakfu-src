package com.ankamagames.wakfu.common.game.nation.data;

import com.ankamagames.framework.kernel.core.common.listener.*;
import java.nio.*;

public abstract class NationPart
{
    public static final NationPart EMPTY_PART;
    private final ListenerHandler<NationPartListener> m_listeners;
    
    public NationPart() {
        super();
        this.m_listeners = new ListenerHandler<NationPartListener>(new Notifier());
    }
    
    public final boolean addDataChangedListener(final NationPartListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.addListener(listener);
    }
    
    public final void removeDataChangedListener(final NationPartListener listener) {
        this.m_listeners.removeListener(listener);
    }
    
    public void fireDataChanged() {
        this.m_listeners.notifyListeners();
    }
    
    public abstract void unSerialize(final ByteBuffer p0, final int p1);
    
    public abstract void serialize(final ByteBuffer p0);
    
    public abstract int serializedSize();
    
    static {
        EMPTY_PART = new EmptyPart();
    }
    
    private static class Notifier implements ListenerNotifier<NationPartListener>
    {
        @Override
        public void notify(final NationPartListener listener) {
            listener.onDataChanged();
        }
    }
    
    private static class EmptyPart extends NationPart
    {
        @Override
        public void serialize(final ByteBuffer buffer) {
        }
        
        @Override
        public void unSerialize(final ByteBuffer buffer, final int version) {
        }
        
        @Override
        public int serializedSize() {
            return 0;
        }
    }
}
