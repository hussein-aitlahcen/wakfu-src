package com.ankamagames.wakfu.common.game.interactiveElements.action;

import java.nio.*;

public final class SwapBooksFromIndex implements InteractiveElementParametrizedAction
{
    private byte m_index;
    private byte m_index2;
    
    SwapBooksFromIndex() {
        super();
    }
    
    public SwapBooksFromIndex(final byte index, final byte index2) {
        super();
        this.m_index = index;
        this.m_index2 = index2;
    }
    
    public byte getIndex() {
        return this.m_index;
    }
    
    public byte getIndex2() {
        return this.m_index2;
    }
    
    @Override
    public byte[] serialize() {
        return new byte[] { this.m_index, this.m_index2 };
    }
    
    @Override
    public void unserialize(final ByteBuffer bb) {
        this.m_index = bb.get();
        this.m_index2 = bb.get();
    }
    
    @Override
    public short getId() {
        return 4;
    }
}
