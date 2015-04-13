package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class AltitudeCriterion extends SimpleContainerCriterion
{
    public static final byte CRITERION_ID = 8;
    private int m_minAltitude;
    private int m_maxAltitude;
    
    public int getMinAltitude() {
        return this.m_minAltitude;
    }
    
    public void setMinAltitude(final int minAltitude) {
        this.m_minAltitude = minAltitude;
    }
    
    public int getMaxAltitude() {
        return this.m_maxAltitude;
    }
    
    public void setMaxAltitude(final int maxAltitude) {
        this.m_maxAltitude = maxAltitude;
    }
    
    public boolean _isValid() {
        final int altitude = ContainerCriterionParameterManager.getInstance().getAltitude();
        return this.m_minAltitude <= altitude && this.m_maxAltitude >= altitude;
    }
    
    @Override
    public byte getCriterionId() {
        return 8;
    }
    
    public void _load(final ExtendedDataInputStream is) {
        this.m_minAltitude = is.readInt();
        this.m_maxAltitude = is.readInt();
    }
    
    public void _save(final OutputBitStream os) throws IOException {
        os.writeInt(this.m_minAltitude);
        os.writeInt(this.m_maxAltitude);
    }
    
    public String _toString() {
        return "Altitude - [" + this.m_minAltitude + " - " + this.m_maxAltitude + "]";
    }
    
    @Override
    public ContainerCriterion clone() {
        final AltitudeCriterion sc = new AltitudeCriterion();
        sc.setMinAltitude(this.m_minAltitude);
        sc.setMaxAltitude(this.m_maxAltitude);
        sc.setNegated(this.isNegated());
        return sc;
    }
}
