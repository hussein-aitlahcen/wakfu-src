package com.ankamagames.wakfu.common.game.havenWorld.action;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public final class InteractiveElementDelete extends HavenWorldAction
{
    private long m_elementUid;
    
    public InteractiveElementDelete() {
        super();
    }
    
    public InteractiveElementDelete(final long uid) {
        super();
        this.m_elementUid = uid;
    }
    
    @Override
    public HavenWorldActionType getActionType() {
        return HavenWorldActionType.INTERACTIVE_ELEMENT_DELETE;
    }
    
    @Override
    protected void _serialize(final ByteArray bb) {
        super._serialize(bb);
        bb.putLong(this.m_elementUid);
    }
    
    @Override
    protected void _unserialize(final ByteBuffer bb) {
        super._unserialize(bb);
        this.m_elementUid = bb.getLong();
    }
    
    @Override
    public void check(final ActionChecker checker) {
        checker.checkDeleteInteractiveElement(this);
    }
    
    @Override
    public void compute(final ActionComputer computer) {
        computer.deleteInteractiveElement(this);
    }
    
    public long getElementUid() {
        return this.m_elementUid;
    }
    
    @Override
    public String toString() {
        return "InteractiveElementDelete{m_elementUid=" + this.m_elementUid + "} " + super.toString();
    }
}
