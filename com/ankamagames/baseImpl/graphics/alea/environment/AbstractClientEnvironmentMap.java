package com.ankamagames.baseImpl.graphics.alea.environment;

import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public abstract class AbstractClientEnvironmentMap
{
    protected short m_x;
    protected short m_y;
    
    public AbstractClientEnvironmentMap() {
        this((short)0, (short)0);
    }
    
    public AbstractClientEnvironmentMap(final short x, final short y) {
        super();
        this.m_x = x;
        this.m_y = y;
    }
    
    public final short getX() {
        return this.m_x;
    }
    
    public final short getY() {
        return this.m_y;
    }
    
    public final void setCoord(final short x, final short y) {
        this.m_x = x;
        this.m_y = y;
    }
    
    public final boolean isInMap(final int x, final int y) {
        final int coordX = this.m_x * 18;
        final int coordY = this.m_y * 18;
        return x >= coordX && x < coordX + 18 && y >= coordY && y < coordY + 18;
    }
    
    public abstract void clear();
    
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        this.m_x = istream.readShort();
        this.m_y = istream.readShort();
    }
    
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        ostream.writeShort(this.m_x);
        ostream.writeShort(this.m_y);
    }
    
    public final int convertToWorldX(final int cellXInMap) {
        assert cellXInMap >= 0 && cellXInMap < 18;
        return cellXInMap + this.m_x * 18;
    }
    
    public final int convertToWorldY(final int cellYInMap) {
        assert cellYInMap >= 0 && cellYInMap < 18;
        return cellYInMap + this.m_y * 18;
    }
}
