package com.ankamagames.wakfu.common.game.havenWorld.procedure;

import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;

public class SerializeBuildings implements TObjectProcedure<Building>
{
    private final ByteArray m_bb;
    private int m_counter;
    
    public SerializeBuildings() {
        super();
        this.m_bb = new ByteArray();
    }
    
    @Override
    public boolean execute(final Building object) {
        this.m_bb.put(HavenWorldSerializer.serializeBuildingWithElements(object));
        ++this.m_counter;
        return true;
    }
    
    public int getCounter() {
        return this.m_counter;
    }
    
    public byte[] getData() {
        return this.m_bb.toArray();
    }
    
    @Override
    public String toString() {
        return "SerializeBuilding{m_bb=" + this.m_bb + '}';
    }
}
