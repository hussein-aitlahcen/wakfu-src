package com.ankamagames.framework.fileFormat.io.binaryStorage2.index;

public class Entry
{
    public final long m_id;
    public final int m_position;
    public final int m_size;
    public final byte m_seed;
    
    public Entry(final long id, final int position, final int size, final byte seed) {
        super();
        this.m_id = id;
        this.m_position = position;
        this.m_size = size;
        this.m_seed = seed;
    }
}
