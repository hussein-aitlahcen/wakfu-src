package com.ankamagames.wakfu.common.game.havenWorld.procedure;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;

public class FindBuildingFromElementUid implements TObjectProcedure<Building>
{
    private final long m_elementUid;
    private long m_buildingUid;
    
    public FindBuildingFromElementUid(final long elementUid) {
        super();
        this.m_elementUid = elementUid;
    }
    
    public long getBuildingUid() {
        return this.m_buildingUid;
    }
    
    @Override
    public boolean execute(final Building object) {
        if (object.getElement(this.m_elementUid) == null) {
            return true;
        }
        this.m_buildingUid = object.getUid();
        return false;
    }
    
    @Override
    public String toString() {
        return "FindBuildingFromElementUid{m_elementUid=" + this.m_elementUid + ", m_buildingUid=" + this.m_buildingUid + '}';
    }
}
