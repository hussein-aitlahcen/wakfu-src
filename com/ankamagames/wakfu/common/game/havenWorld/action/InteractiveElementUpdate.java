package com.ankamagames.wakfu.common.game.havenWorld.action;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class InteractiveElementUpdate extends HavenWorldAction
{
    private long m_elementUid;
    private final Point3 m_position;
    private byte[] m_data;
    private int m_version;
    
    InteractiveElementUpdate() {
        super();
        this.m_position = new Point3();
    }
    
    public InteractiveElementUpdate(final long elementUid, final Point3 position, final byte[] data, final int version) {
        super();
        this.m_position = new Point3();
        this.m_elementUid = elementUid;
        this.m_position.set(position);
        this.m_data = data;
        this.m_version = version;
    }
    
    @Override
    public HavenWorldActionType getActionType() {
        return HavenWorldActionType.INTERACTIVE_ELEMENT_UPDATE;
    }
    
    @Override
    protected void _serialize(final ByteArray bb) {
        super._serialize(bb);
        bb.putLong(this.m_elementUid);
        bb.putInt(this.m_position.getX());
        bb.putInt(this.m_position.getY());
        bb.putShort(this.m_position.getZ());
        bb.putInt(this.m_data.length);
        bb.put(this.m_data);
        bb.putInt(this.m_version);
    }
    
    @Override
    protected void _unserialize(final ByteBuffer bb) {
        super._unserialize(bb);
        this.m_elementUid = bb.getLong();
        this.m_position.set(bb.getInt(), bb.getInt(), bb.getShort());
        bb.get(this.m_data = new byte[bb.getInt()]);
        this.m_version = bb.getInt();
    }
    
    @Override
    public void check(final ActionChecker checker) {
        checker.checkUpdateInteractiveElement(this);
    }
    
    @Override
    public void compute(final ActionComputer computer) {
        computer.updateInteractiveElement(this);
    }
    
    public long getElementUid() {
        return this.m_elementUid;
    }
    
    public byte[] getData() {
        return this.m_data;
    }
    
    public int getVersion() {
        return this.m_version;
    }
    
    public Point3 getPosition() {
        return this.m_position;
    }
    
    @Override
    public String toString() {
        return "InteractiveElementUpdate{m_data=" + this.m_data + ", m_elementUid=" + this.m_elementUid + ", m_position=" + this.m_position + "} " + super.toString();
    }
}
