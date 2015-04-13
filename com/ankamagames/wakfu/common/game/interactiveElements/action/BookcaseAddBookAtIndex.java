package com.ankamagames.wakfu.common.game.interactiveElements.action;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public final class BookcaseAddBookAtIndex implements InteractiveElementParametrizedAction
{
    public long m_bookUid;
    public byte m_bookcaseIndex;
    
    BookcaseAddBookAtIndex() {
        super();
    }
    
    public BookcaseAddBookAtIndex(final long bookUid, final byte bookcaseIndex) {
        super();
        this.m_bookUid = bookUid;
        this.m_bookcaseIndex = bookcaseIndex;
    }
    
    @Override
    public byte[] serialize() {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_bookUid);
        ba.put(this.m_bookcaseIndex);
        return ba.toArray();
    }
    
    @Override
    public void unserialize(final ByteBuffer bb) {
        this.m_bookUid = bb.getLong();
        this.m_bookcaseIndex = bb.get();
    }
    
    public long getBookUid() {
        return this.m_bookUid;
    }
    
    public byte getBookcaseIndex() {
        return this.m_bookcaseIndex;
    }
    
    @Override
    public short getId() {
        return 1;
    }
}
