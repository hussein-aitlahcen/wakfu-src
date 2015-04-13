package com.ankamagames.framework.graphics.image;

public final class Kernel
{
    private float[] m_data;
    private int m_size;
    
    public Kernel(final int size) {
        super();
        this.m_data = new float[size * size];
        this.m_size = size;
    }
    
    public Kernel(final float[] kernel) {
        super();
        this.setKernel(kernel);
    }
    
    public final void setKernel(final float[] kernel) {
        assert Math.sqrt(kernel.length) == (int)Math.sqrt(kernel.length);
        this.m_size = (int)Math.sqrt(kernel.length);
        System.arraycopy(kernel, 0, this.m_data = new float[kernel.length], 0, this.m_data.length);
    }
    
    public final float[] getBuffer() {
        return this.m_data;
    }
    
    public final int getSize() {
        return this.m_size;
    }
}
