package com.ankamagames.wakfu.common.game.havenWorld.action;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class TopologyCreate extends HavenWorldAction
{
    private short m_partitionX;
    private short m_partitionY;
    
    TopologyCreate() {
        super();
    }
    
    public TopologyCreate(final short partitionX, final short partitionY) {
        super();
        this.m_partitionX = partitionX;
        this.m_partitionY = partitionY;
    }
    
    @Override
    public HavenWorldActionType getActionType() {
        return HavenWorldActionType.TOPOLOGY_CREATE;
    }
    
    @Override
    protected void _serialize(final ByteArray bb) {
        super._serialize(bb);
        bb.putShort(this.m_partitionX);
        bb.putShort(this.m_partitionY);
    }
    
    @Override
    protected void _unserialize(final ByteBuffer bb) {
        super._unserialize(bb);
        this.m_partitionX = bb.getShort();
        this.m_partitionY = bb.getShort();
    }
    
    @Override
    public void check(final ActionChecker checker) {
        checker.checkCreateTopology(this);
    }
    
    @Override
    public void compute(final ActionComputer computer) {
        computer.createTopology(this);
    }
    
    public short getPartitionX() {
        return this.m_partitionX;
    }
    
    public short getPartitionY() {
        return this.m_partitionY;
    }
    
    @Override
    public String toString() {
        return "TopologyCreate{m_partitionX=" + this.m_partitionX + ", m_partitionY=" + this.m_partitionY + "} " + super.toString();
    }
}
