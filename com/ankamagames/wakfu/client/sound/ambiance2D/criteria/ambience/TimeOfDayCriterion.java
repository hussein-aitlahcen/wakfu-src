package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class TimeOfDayCriterion extends SimpleContainerCriterion
{
    public static final byte CRITERION_ID = 2;
    private float m_start;
    private float m_end;
    
    public float getStart() {
        return this.m_start;
    }
    
    public void setStart(final float start) {
        this.m_start = start;
    }
    
    public float getEnd() {
        return this.m_end;
    }
    
    public void setEnd(final float end) {
        this.m_end = end;
    }
    
    public boolean _isValid() {
        final float percentage = ContainerCriterionParameterManager.getInstance().getTimeOfDay();
        return this.m_start <= percentage && this.m_end > percentage;
    }
    
    @Override
    public byte getCriterionId() {
        return 2;
    }
    
    public void _load(final ExtendedDataInputStream is) {
        this.m_start = is.readFloat();
        this.m_end = is.readFloat();
    }
    
    public void _save(final OutputBitStream os) throws IOException {
        os.writeFloat(this.m_start);
        os.writeFloat(this.m_end);
    }
    
    public String _toString() {
        return "Moment de la journ\u00e9e - D\u00e9but : " + (int)(this.m_start * 100.0f) + " - Fin : " + (int)(this.m_end * 100.0f);
    }
    
    @Override
    public ContainerCriterion clone() {
        final TimeOfDayCriterion sc = new TimeOfDayCriterion();
        sc.setStart(this.m_start);
        sc.setEnd(this.m_end);
        sc.setNegated(this.isNegated());
        return sc;
    }
}
