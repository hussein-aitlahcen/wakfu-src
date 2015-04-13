package com.ankamagames.wakfu.common.game.havenWorld.action;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class BuildingCreate extends HavenWorldAction
{
    private long m_buildingUid;
    private long m_creationDate;
    private short m_buildingRefId;
    private short m_x;
    private short m_y;
    
    BuildingCreate() {
        super();
    }
    
    public BuildingCreate(final short buildingRefId, final short x, final short y) {
        super();
        this.m_buildingRefId = buildingRefId;
        this.m_x = x;
        this.m_y = y;
    }
    
    @Override
    public HavenWorldActionType getActionType() {
        return HavenWorldActionType.BUILDING_CREATE;
    }
    
    @Override
    protected void _serialize(final ByteArray bb) {
        super._serialize(bb);
        bb.putLong(this.m_buildingUid);
        bb.putLong(this.m_creationDate);
        bb.putShort(this.m_buildingRefId);
        bb.putShort(this.m_x);
        bb.putShort(this.m_y);
    }
    
    @Override
    protected void _unserialize(final ByteBuffer bb) {
        super._unserialize(bb);
        this.m_buildingUid = bb.getLong();
        this.m_creationDate = bb.getLong();
        this.m_buildingRefId = bb.getShort();
        this.m_x = bb.getShort();
        this.m_y = bb.getShort();
    }
    
    @Override
    public void check(final ActionChecker checker) {
        checker.checkCreateBuilding(this);
    }
    
    @Override
    public void compute(final ActionComputer computer) {
        computer.createBuilding(this);
    }
    
    public long getBuildingUid() {
        return this.m_buildingUid;
    }
    
    public void setBuildingUid(final long buildingUid) {
        this.m_buildingUid = buildingUid;
    }
    
    public long getCreationDate() {
        return this.m_creationDate;
    }
    
    public void setCreationDate(final long creationDate) {
        this.m_creationDate = creationDate;
    }
    
    public short getBuildingRefId() {
        return this.m_buildingRefId;
    }
    
    public short getX() {
        return this.m_x;
    }
    
    public short getY() {
        return this.m_y;
    }
    
    @Override
    public String toString() {
        return "BuildingCreate{m_buildingRefId=" + this.m_buildingRefId + ", m_buildingUid=" + this.m_buildingUid + ", m_creationDate=" + this.m_creationDate + ", m_x=" + this.m_x + ", m_y=" + this.m_y + "} " + super.toString();
    }
}
