package com.ankamagames.wakfu.common.game.havenWorld.action;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class BuildingEvolve extends HavenWorldAction
{
    private long m_buildingUid;
    private short m_nextBuildingRefId;
    private long m_creationDate;
    
    BuildingEvolve() {
        super();
    }
    
    public BuildingEvolve(final long buildingUid) {
        super();
        this.m_buildingUid = buildingUid;
    }
    
    @Override
    public HavenWorldActionType getActionType() {
        return HavenWorldActionType.BUILDING_EVOLUTION;
    }
    
    @Override
    protected void _serialize(final ByteArray bb) {
        super._serialize(bb);
        bb.putLong(this.m_buildingUid);
        bb.putLong(this.m_creationDate);
        bb.putShort(this.m_nextBuildingRefId);
    }
    
    @Override
    protected void _unserialize(final ByteBuffer bb) {
        super._unserialize(bb);
        this.m_buildingUid = bb.getLong();
        this.m_creationDate = bb.getLong();
        this.m_nextBuildingRefId = bb.getShort();
    }
    
    @Override
    public void check(final ActionChecker checker) {
        checker.checkEvolveBuilding(this);
    }
    
    @Override
    public void compute(final ActionComputer computer) {
        computer.evolveBuilding(this);
    }
    
    public long getBuildingUid() {
        return this.m_buildingUid;
    }
    
    public short getNextBuildingRefId() {
        return this.m_nextBuildingRefId;
    }
    
    public long getCreationDate() {
        return this.m_creationDate;
    }
    
    public void setCreationDate(final long creationDate) {
        this.m_creationDate = creationDate;
    }
    
    public void setNextBuildingRefId(final short nextBuildingRefId) {
        this.m_nextBuildingRefId = nextBuildingRefId;
    }
    
    @Override
    public String toString() {
        return "BuildingEvolve{m_buildingUid=" + this.m_buildingUid + ", m_nextBuildingRefId=" + this.m_nextBuildingRefId + ", m_creationDate=" + this.m_creationDate + "} " + super.toString();
    }
}
