package com.ankamagames.wakfu.common.game.havenWorld.action;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public final class InteractiveElementCreate extends HavenWorldAction
{
    private long m_elementUid;
    private long m_interactiveElementId;
    private int m_templateId;
    private final Point3 m_position;
    private long m_ownerId;
    private byte[] m_data;
    private int m_version;
    
    InteractiveElementCreate() {
        super();
        this.m_position = new Point3();
    }
    
    public InteractiveElementCreate(final long elementUid, final long interactiveElementId, final int templateId, final Point3 position, final long ownerId, final byte[] data, final int version) {
        super();
        this.m_position = new Point3();
        this.m_elementUid = elementUid;
        this.m_interactiveElementId = interactiveElementId;
        this.m_templateId = templateId;
        this.m_position.set(position);
        this.m_ownerId = ownerId;
        this.m_data = data;
        this.m_version = version;
    }
    
    @Override
    public HavenWorldActionType getActionType() {
        return HavenWorldActionType.INTERACTIVE_ELEMENT_CREATE;
    }
    
    @Override
    protected void _serialize(final ByteArray bb) {
        super._serialize(bb);
        bb.putLong(this.m_elementUid);
        bb.putLong(this.m_interactiveElementId);
        bb.putInt(this.m_templateId);
        bb.putInt(this.m_position.getX());
        bb.putInt(this.m_position.getY());
        bb.putShort(this.m_position.getZ());
        bb.putLong(this.m_ownerId);
        bb.putInt(this.m_data.length);
        bb.put(this.m_data);
        bb.putInt(this.m_version);
    }
    
    @Override
    protected void _unserialize(final ByteBuffer bb) {
        super._unserialize(bb);
        this.m_elementUid = bb.getLong();
        this.m_interactiveElementId = bb.getLong();
        this.m_templateId = bb.getInt();
        this.m_position.set(bb.getInt(), bb.getInt(), bb.getShort());
        this.m_ownerId = bb.getLong();
        bb.get(this.m_data = new byte[bb.getInt()]);
        this.m_version = bb.getInt();
    }
    
    @Override
    public void check(final ActionChecker checker) {
        checker.checkCreateInteractiveElement(this);
    }
    
    @Override
    public void compute(final ActionComputer computer) {
        computer.createInteractiveElement(this);
    }
    
    public long getBuildingUid() {
        return -1L;
    }
    
    public long getElementUid() {
        return this.m_elementUid;
    }
    
    public void setElementUid(final long elementUid) {
        this.m_elementUid = elementUid;
    }
    
    public long getOwnerId() {
        return this.m_ownerId;
    }
    
    public byte[] getData() {
        return this.m_data;
    }
    
    public int getVersion() {
        return this.m_version;
    }
    
    public int getTemplateId() {
        return this.m_templateId;
    }
    
    public Point3 getPosition() {
        return this.m_position;
    }
    
    public long getInteractiveElementId() {
        return this.m_interactiveElementId;
    }
    
    @Override
    public String toString() {
        return "InteractiveElementCreate{m_data=" + this.m_data + ", m_elementUid=" + this.m_elementUid + ", m_interactiveElementId=" + this.m_interactiveElementId + ", m_templateId=" + this.m_templateId + ", m_position=" + this.m_position + ", m_ownerId=" + this.m_ownerId + "} " + super.toString();
    }
}
