package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public abstract class SimpleContainerCriterion implements ContainerCriterion
{
    private boolean m_isNegated;
    
    public SimpleContainerCriterion() {
        super();
        this.m_isNegated = false;
    }
    
    @Override
    public boolean isNegated() {
        return this.m_isNegated;
    }
    
    public void setNegated(final boolean negated) {
        this.m_isNegated = negated;
    }
    
    @Override
    public void load(final ExtendedDataInputStream is) {
        this.m_isNegated = is.readBooleanBit();
        this._load(is);
    }
    
    @Override
    public void save(final OutputBitStream os) throws IOException {
        os.writeBooleanBit(this.m_isNegated);
        this._save(os);
    }
    
    @Override
    public boolean isValid() {
        if (this.m_isNegated) {
            return !this._isValid();
        }
        return this._isValid();
    }
    
    @Override
    public String toString() {
        String ret = this._toString();
        if (this.m_isNegated) {
            ret = "[NON] " + ret;
        }
        return ret;
    }
    
    @Override
    public abstract ContainerCriterion clone();
    
    protected abstract boolean _isValid();
    
    protected abstract void _load(final ExtendedDataInputStream p0);
    
    protected abstract void _save(final OutputBitStream p0) throws IOException;
    
    protected abstract String _toString();
}
