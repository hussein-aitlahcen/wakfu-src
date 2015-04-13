package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class TemperatureCriterion extends SimpleContainerCriterion
{
    public static final byte CRITERION_ID = 1;
    private int m_min;
    private int m_max;
    
    public int getMin() {
        return this.m_min;
    }
    
    public void setMin(final int min) {
        this.m_min = min;
    }
    
    public int getMax() {
        return this.m_max;
    }
    
    public void setMax(final int max) {
        this.m_max = max;
    }
    
    public void setMinMax(final int min, final int max) {
        this.m_min = min;
        this.m_max = max;
    }
    
    public boolean _isValid() {
        final float temp = ContainerCriterionParameterManager.getInstance().getTemperature();
        return this.m_min <= temp && temp <= this.m_max;
    }
    
    @Override
    public byte getCriterionId() {
        return 1;
    }
    
    public void _load(final ExtendedDataInputStream is) {
        this.m_min = is.readInt();
        this.m_max = is.readInt();
    }
    
    public void _save(final OutputBitStream os) throws IOException {
        os.writeInt(this.m_min);
        os.writeInt(this.m_max);
    }
    
    public String _toString() {
        return "Temp\u00e9rature (Min : " + this.m_min + ", Max : " + this.m_max + ")";
    }
    
    @Override
    public ContainerCriterion clone() {
        final TemperatureCriterion sc = new TemperatureCriterion();
        sc.setMin(this.m_min);
        sc.setMax(this.m_max);
        sc.setNegated(this.isNegated());
        return sc;
    }
}
