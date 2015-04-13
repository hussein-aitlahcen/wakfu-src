package com.ankamagames.framework.ai.criteria.antlrcriteria;

import com.ankamagames.framework.kernel.core.maths.*;

public abstract class PositionValue extends NumericalValue
{
    public static final int XY_BIT_COUNT = 18;
    public static final int Z_BIT_COUNT = 10;
    private static final long XY_MASK = 262143L;
    private static final long Z_MASK = 1023L;
    private static final long XY_SHIFT = 131071L;
    private static final long Z_SHIFT = 511L;
    
    public static long toLong(final int x, final int y, final short z) {
        final long ux = x + 131071L & 0x3FFFFL;
        final long uy = y + 131071L & 0x3FFFFL;
        final long uz = z + 511L & 0x3FFL;
        if (Math.abs(x) <= 131072 - ((x < 0) ? 1 : 0) && Math.abs(y) <= 131072 - ((y < 0) ? 1 : 0) && Math.abs(z) <= 512 - ((z < 0) ? 1 : 0)) {
            return ux << 28 | uy << 10 | uz;
        }
        throw new CriteriaExecutionException("Param\u00e8tres d'une position en dehors de la map - position : " + x + ", " + y + ", " + z);
    }
    
    public static long toLong(final Point3 pos) {
        return toLong(pos.getX(), pos.getY(), pos.getZ());
    }
    
    public static Point3 fromLong(final long value) {
        final short z = (short)((value & 0x3FFL) - 511L);
        final int y = (int)((value >> 10 & 0x3FFFFL) - 131071L);
        final int x = (int)((value >> 28 & 0x3FFFFL) - 131071L);
        return new Point3(x, y, z);
    }
    
    public static int getXFromLong(final long value) {
        return (int)((value >> 28 & 0x3FFFFL) - 131071L);
    }
    
    public static int getYFromLong(final long value) {
        return (int)((value >> 10 & 0x3FFFFL) - 131071L);
    }
    
    public static short getZFromLong(final long value) {
        return (short)((value & 0x3FFL) - 511L);
    }
    
    @Override
    public ParserType getType() {
        return ParserType.POSITION;
    }
    
    @Override
    public abstract boolean isConstant();
    
    @Override
    public void setOpposite() {
    }
    
    @Override
    public boolean isInteger() {
        return false;
    }
}
