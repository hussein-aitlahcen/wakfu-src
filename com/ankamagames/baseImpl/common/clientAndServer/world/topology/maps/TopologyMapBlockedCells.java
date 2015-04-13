package com.ankamagames.baseImpl.common.clientAndServer.world.topology.maps;

import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public abstract class TopologyMapBlockedCells extends TopologyMap
{
    private final byte[] m_blockedCells;
    
    public TopologyMapBlockedCells() {
        super();
        this.m_blockedCells = new byte[ByteArrayBitSet.getDataLength(324)];
    }
    
    @Override
    public boolean isCellBlocked(final int x, final int y) {
        return ByteArrayBitSet.get(this.m_blockedCells, (y - this.m_y) * 18 + x - this.m_x);
    }
    
    @Override
    public void load(final ExtendedDataInputStream stream) throws IOException {
        super.load(stream);
        stream.readBytes(this.m_blockedCells);
    }
    
    protected final void setCellBlocked(final int x, final int y, final boolean blocked) {
        ByteArrayBitSet.set(this.m_blockedCells, (y - this.m_y) * 18 + x - this.m_x, blocked);
    }
}
