package com.ankamagames.wakfu.common.game.havenWorld.action;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class BuildingMove extends HavenWorldAction
{
    private long m_buildingUid;
    private short m_x;
    private short m_y;
    
    BuildingMove() {
        super();
    }
    
    public BuildingMove(final long buildingUid, final short x, final short y) {
        super();
        this.m_buildingUid = buildingUid;
        this.m_x = x;
        this.m_y = y;
    }
    
    @Override
    public HavenWorldActionType getActionType() {
        return HavenWorldActionType.BUILDING_MOVE;
    }
    
    @Override
    protected void _serialize(final ByteArray bb) {
        super._serialize(bb);
        bb.putLong(this.m_buildingUid);
        bb.putShort(this.m_x);
        bb.putShort(this.m_y);
    }
    
    @Override
    protected void _unserialize(final ByteBuffer bb) {
        super._unserialize(bb);
        this.m_buildingUid = bb.getLong();
        this.m_x = bb.getShort();
        this.m_y = bb.getShort();
    }
    
    @Override
    public void check(final ActionChecker checker) {
        checker.checkMoveBuilding(this);
    }
    
    @Override
    public void compute(final ActionComputer computer) {
        computer.moveBuilding(this);
    }
    
    public long getBuildingUid() {
        return this.m_buildingUid;
    }
    
    public short getX() {
        return this.m_x;
    }
    
    public short getY() {
        return this.m_y;
    }
    
    @Override
    public String toString() {
        return "BuildingMove{m_buildingUid=" + this.m_buildingUid + ", m_x=" + this.m_x + ", m_y=" + this.m_y + "} " + super.toString();
    }
}
