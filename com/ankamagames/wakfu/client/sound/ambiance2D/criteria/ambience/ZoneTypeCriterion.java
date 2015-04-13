package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience;

import com.ankamagames.wakfu.client.sound.ambiance2D.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class ZoneTypeCriterion extends SimpleContainerCriterion
{
    public static final byte CRITERION_ID = 3;
    private AmbianceZoneType m_zoneType;
    
    public AmbianceZoneType getZoneType() {
        return this.m_zoneType;
    }
    
    public void setZoneType(final AmbianceZoneType zoneType) {
        this.m_zoneType = zoneType;
    }
    
    public boolean _isValid() {
        return ContainerCriterionParameterManager.getInstance().getZoneTypeId() == this.m_zoneType.getId();
    }
    
    @Override
    public byte getCriterionId() {
        return 3;
    }
    
    public void _load(final ExtendedDataInputStream is) {
        this.m_zoneType = AmbianceZoneType.getFromId(is.readByte());
    }
    
    public void _save(final OutputBitStream os) throws IOException {
        os.writeByte(this.m_zoneType.getId());
    }
    
    public String _toString() {
        return "Type de zone : " + this.m_zoneType.toString();
    }
    
    @Override
    public ContainerCriterion clone() {
        final ZoneTypeCriterion sc = new ZoneTypeCriterion();
        sc.setZoneType(this.m_zoneType);
        sc.setNegated(this.isNegated());
        return sc;
    }
}
