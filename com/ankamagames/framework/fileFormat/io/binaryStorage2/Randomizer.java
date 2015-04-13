package com.ankamagames.framework.fileFormat.io.binaryStorage2;

public abstract class Randomizer
{
    protected byte m_seed;
    private final int m_add;
    private final int m_mult;
    
    protected Randomizer(final int mult, final int add) {
        super();
        this.m_mult = mult;
        this.m_add = add;
        this.m_seed = (byte)(this.m_mult ^ add);
    }
    
    protected final void inc() {
        this.m_seed += (byte)(this.m_mult * this.position() + this.m_add);
    }
    
    protected abstract long position();
}
