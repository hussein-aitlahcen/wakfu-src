package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class WakfuCriterion extends SimpleContainerCriterion
{
    public static final byte CRITERION_ID = 4;
    private float m_min;
    private float m_max;
    
    public float getMin() {
        return this.m_min;
    }
    
    public void setMin(final float min) {
        this.m_min = min;
    }
    
    public float getMax() {
        return this.m_max;
    }
    
    public void setMax(final float max) {
        this.m_max = max;
    }
    
    public void setMinMax(final float min, final float max) {
        this.m_min = min;
        this.m_max = max;
    }
    
    public boolean _isValid() {
        final float temp = ContainerCriterionParameterManager.getInstance().getWakfuScore();
        return this.m_min <= temp && temp <= this.m_max;
    }
    
    @Override
    public byte getCriterionId() {
        return 4;
    }
    
    public void _load(final ExtendedDataInputStream is) {
        this.m_min = is.readFloat();
        this.m_max = is.readFloat();
    }
    
    public void _save(final OutputBitStream os) throws IOException {
        os.writeFloat(this.m_min);
        os.writeFloat(this.m_max);
    }
    
    public String _toString() {
        return "Wakfu (Min : " + this.m_min + ", Max : " + this.m_max + ")";
    }
    
    @Override
    public ContainerCriterion clone() {
        final WakfuCriterion sc = new WakfuCriterion();
        sc.setMin(this.m_min);
        sc.setMax(this.m_max);
        sc.setNegated(this.isNegated());
        return sc;
    }
}
