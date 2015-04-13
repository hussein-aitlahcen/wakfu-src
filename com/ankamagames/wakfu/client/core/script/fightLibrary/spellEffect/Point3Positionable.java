package com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect;

import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;

final class Point3Positionable implements WorldPositionable
{
    private final int m_x;
    private final int m_y;
    private final short m_z;
    
    Point3Positionable(@NotNull final Point3 point) {
        super();
        this.m_x = point.getX();
        this.m_y = point.getY();
        this.m_z = point.getZ();
    }
    
    Point3Positionable(final WorldPositionable target) {
        super();
        this.m_x = target.getWorldCellX();
        this.m_y = target.getWorldCellY();
        this.m_z = target.getWorldCellAltitude();
    }
    
    @Override
    public float getWorldX() {
        return this.getWorldCellX();
    }
    
    @Override
    public float getWorldY() {
        return this.getWorldCellY();
    }
    
    @Override
    public float getAltitude() {
        return this.getWorldCellAltitude();
    }
    
    @Override
    public int getWorldCellX() {
        return this.m_x;
    }
    
    @Override
    public int getWorldCellY() {
        return this.m_y;
    }
    
    @Override
    public short getWorldCellAltitude() {
        return this.m_z;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Point3Positionable that = (Point3Positionable)o;
        return this.m_x == that.m_x && this.m_y == that.m_y && this.m_z == that.m_z;
    }
    
    @Override
    public int hashCode() {
        int result = this.m_x;
        result = 31 * result + this.m_y;
        result = 31 * result + this.m_z;
        return result;
    }
}
