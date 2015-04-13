package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience;

import java.util.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class AndCriterion extends SimpleContainerCriterion
{
    public static final byte CRITERION_ID = 6;
    private final ArrayList<ContainerCriterion> m_criteria;
    
    public AndCriterion() {
        super();
        this.m_criteria = new ArrayList<ContainerCriterion>(2);
    }
    
    public void addCriterion(final ContainerCriterion critrion) {
        this.m_criteria.add(critrion);
    }
    
    public ArrayList<ContainerCriterion> getCriteria() {
        return this.m_criteria;
    }
    
    public boolean _isValid() {
        for (int i = this.m_criteria.size() - 1; i >= 0; --i) {
            if (!this.m_criteria.get(i).isValid()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public byte getCriterionId() {
        return 6;
    }
    
    public void _load(final ExtendedDataInputStream is) {
        for (int size = is.readByte(), i = 0; i < size; ++i) {
            this.m_criteria.add(AmbianceCriteria.read(is));
        }
    }
    
    public void _save(final OutputBitStream os) throws IOException {
        os.writeByte((byte)this.m_criteria.size());
        for (int i = 0, size = this.m_criteria.size(); i < size; ++i) {
            AmbianceCriteria.write(os, this.m_criteria.get(i));
        }
    }
    
    public String _toString() {
        final StringBuilder sb = new StringBuilder("{");
        for (int i = 0, size = this.m_criteria.size(); i < size; ++i) {
            if (i > 0) {
                sb.append(" ET ");
            }
            sb.append(this.m_criteria.get(i).toString());
        }
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public ContainerCriterion clone() {
        final AndCriterion sc = new AndCriterion();
        sc.setNegated(this.isNegated());
        for (int i = 0, size = this.m_criteria.size(); i < size; ++i) {
            sc.addCriterion(this.m_criteria.get(i).clone());
        }
        return sc;
    }
}
