package com.ankamagames.wakfu.common.game.interactiveElements.action;

import java.nio.*;

public final class RemoveBookFromIndex implements InteractiveElementParametrizedAction
{
    private byte m_index;
    
    RemoveBookFromIndex() {
        super();
    }
    
    public RemoveBookFromIndex(final byte index) {
        super();
        this.m_index = index;
    }
    
    public byte getIndex() {
        return this.m_index;
    }
    
    @Override
    public byte[] serialize() {
        return new byte[] { this.m_index };
    }
    
    @Override
    public void unserialize(final ByteBuffer bb) {
        this.m_index = bb.get();
    }
    
    @Override
    public short getId() {
        return 3;
    }
}
