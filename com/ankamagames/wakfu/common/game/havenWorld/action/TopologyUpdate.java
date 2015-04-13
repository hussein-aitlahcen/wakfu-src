package com.ankamagames.wakfu.common.game.havenWorld.action;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class TopologyUpdate extends HavenWorldAction
{
    private short m_patchX;
    private short m_patchY;
    private short m_patchId;
    private short m_oldPatchId;
    
    TopologyUpdate() {
        super();
    }
    
    public TopologyUpdate(final short patchX, final short patchY, final short patchId, final short oldPatchId) {
        super();
        this.m_patchX = patchX;
        this.m_patchY = patchY;
        this.m_patchId = patchId;
        this.m_oldPatchId = oldPatchId;
    }
    
    @Override
    public HavenWorldActionType getActionType() {
        return HavenWorldActionType.TOPOLOGY_UPDATE;
    }
    
    @Override
    protected void _serialize(final ByteArray bb) {
        super._serialize(bb);
        bb.putShort(this.m_patchX);
        bb.putShort(this.m_patchY);
        bb.putShort(this.m_patchId);
        bb.putShort(this.m_oldPatchId);
    }
    
    @Override
    protected void _unserialize(final ByteBuffer bb) {
        super._unserialize(bb);
        this.m_patchX = bb.getShort();
        this.m_patchY = bb.getShort();
        this.m_patchId = bb.getShort();
        this.m_oldPatchId = bb.getShort();
    }
    
    @Override
    public void check(final ActionChecker checker) {
        checker.checkUpdateTopology(this);
    }
    
    @Override
    public void compute(final ActionComputer computer) {
        computer.updateTopology(this);
    }
    
    public short getPatchX() {
        return this.m_patchX;
    }
    
    public short getPatchY() {
        return this.m_patchY;
    }
    
    public short getPatchId() {
        return this.m_patchId;
    }
    
    public short getOldPatchId() {
        return this.m_oldPatchId;
    }
    
    @Override
    public String toString() {
        return "TopologyUpdate{m_oldPatchId=" + this.m_oldPatchId + ", m_patchX=" + this.m_patchX + ", m_patchY=" + this.m_patchY + ", m_patchId=" + this.m_patchId + "} " + super.toString();
    }
}
