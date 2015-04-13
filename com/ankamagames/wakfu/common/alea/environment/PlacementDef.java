package com.ankamagames.wakfu.common.alea.environment;

import com.ankamagames.framework.kernel.core.maths.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class PlacementDef
{
    public final int m_x;
    public final int m_y;
    public final short m_z;
    public final Direction8 m_direction;
    
    public PlacementDef(final int x, final int y, final short z, final Direction8 dir) {
        super();
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
        this.m_direction = dir;
    }
    
    PlacementDef(final ExtendedDataInputStream istream) throws IOException {
        super();
        this.m_x = istream.readInt();
        this.m_y = istream.readInt();
        this.m_z = istream.readShort();
        this.m_direction = Direction8.getDirectionFromIndex(istream.readByte());
    }
    
    void save(final OutputBitStream ostream) throws IOException {
        ostream.writeInt(this.m_x);
        ostream.writeInt(this.m_y);
        ostream.writeShort(this.m_z);
        ostream.writeByte((byte)this.m_direction.m_index);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlacementDef)) {
            return false;
        }
        final PlacementDef that = (PlacementDef)o;
        return this.m_x == that.m_x && this.m_y == that.m_y && this.m_z == that.m_z && this.m_direction == that.m_direction;
    }
    
    @Override
    public int hashCode() {
        int result = this.m_x;
        result = 31 * result + this.m_y;
        result = 31 * result + this.m_z;
        result = 31 * result + ((this.m_direction != null) ? this.m_direction.hashCode() : 0);
        return result;
    }
}
