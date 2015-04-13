package com.ankamagames.wakfu.common.game.havenWorld.procedure;

import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;

public class SerializePartitions implements TObjectProcedure<Partition>
{
    private int m_counter;
    private final ByteArray m_bb;
    
    public SerializePartitions() {
        super();
        this.m_bb = new ByteArray();
    }
    
    @Override
    public boolean execute(final Partition partition) {
        this.m_bb.put(HavenWorldSerializer.serializePartition(partition));
        ++this.m_counter;
        return true;
    }
    
    public byte[] getData() {
        return this.m_bb.toArray();
    }
    
    public int getCounter() {
        return this.m_counter;
    }
    
    @Override
    public String toString() {
        return "SerializePartitions{m_bb=" + this.m_bb + ", m_counter=" + this.m_counter + '}';
    }
}
