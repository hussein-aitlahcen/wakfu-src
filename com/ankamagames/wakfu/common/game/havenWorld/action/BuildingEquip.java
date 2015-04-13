package com.ankamagames.wakfu.common.game.havenWorld.action;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class BuildingEquip extends HavenWorldAction
{
    private long m_buildingUid;
    private int m_itemId;
    
    BuildingEquip() {
        super();
    }
    
    public BuildingEquip(final long buildingUid, final int itemId) {
        super();
        this.m_buildingUid = buildingUid;
        this.m_itemId = itemId;
    }
    
    @Override
    public HavenWorldActionType getActionType() {
        return HavenWorldActionType.BUILDING_EQUIP;
    }
    
    @Override
    protected void _serialize(final ByteArray bb) {
        super._serialize(bb);
        bb.putLong(this.m_buildingUid);
        bb.putInt(this.m_itemId);
    }
    
    @Override
    protected void _unserialize(final ByteBuffer bb) {
        super._unserialize(bb);
        this.m_buildingUid = bb.getLong();
        this.m_itemId = bb.getInt();
    }
    
    @Override
    public void check(final ActionChecker checker) {
        checker.checkEquipBuilding(this);
    }
    
    @Override
    public void compute(final ActionComputer computer) {
        computer.equipBuilding(this);
    }
    
    public long getBuildingUid() {
        return this.m_buildingUid;
    }
    
    public void setBuildingUid(final long buildingUid) {
        this.m_buildingUid = buildingUid;
    }
    
    public int getItemId() {
        return this.m_itemId;
    }
    
    public void setItemId(final int itemId) {
        this.m_itemId = itemId;
    }
    
    @Override
    public String toString() {
        return "BuildingEquip{m_buildingUid=" + this.m_buildingUid + ", m_itemId=" + this.m_itemId + "} " + super.toString();
    }
}
