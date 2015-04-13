package com.ankamagames.wakfu.common.game.havenWorld.action;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class BuildingDelete extends HavenWorldAction
{
    private long m_buildingUid;
    
    BuildingDelete() {
        super();
    }
    
    public BuildingDelete(final long buildingUid) {
        super();
        this.m_buildingUid = buildingUid;
    }
    
    @Override
    public HavenWorldActionType getActionType() {
        return HavenWorldActionType.BUILDING_DELETE;
    }
    
    @Override
    protected void _serialize(final ByteArray bb) {
        super._serialize(bb);
        bb.putLong(this.m_buildingUid);
    }
    
    @Override
    protected void _unserialize(final ByteBuffer bb) {
        super._unserialize(bb);
        this.m_buildingUid = bb.getLong();
    }
    
    @Override
    public void check(final ActionChecker checker) {
        checker.checkDeleteBuilding(this);
    }
    
    @Override
    public void compute(final ActionComputer computer) {
        computer.deleteBuilding(this);
    }
    
    public long getBuildingUid() {
        return this.m_buildingUid;
    }
    
    @Override
    public String toString() {
        return "BuildingDelete{m_buildingUid=" + this.m_buildingUid + "} " + super.toString();
    }
}
