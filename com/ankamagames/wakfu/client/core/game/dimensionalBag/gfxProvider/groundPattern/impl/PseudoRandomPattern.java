package com.ankamagames.wakfu.client.core.game.dimensionalBag.gfxProvider.groundPattern.impl;

import com.ankamagames.wakfu.client.core.game.dimensionalBag.gfxProvider.groundPattern.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class PseudoRandomPattern implements GroundPattern
{
    private static final int SEQUENCE_SIZE = 256;
    private static final byte[] RANDOM_SEQUENCE;
    private final int[] m_availableGfx;
    
    public PseudoRandomPattern(final int... availableGfx) {
        super();
        assert availableGfx != null && availableGfx.length >= 1 : "Random patterns must contain at least 1 gfx (2 would be even better)";
        this.m_availableGfx = availableGfx;
    }
    
    @Override
    public int getGfx(final int cellX, final int cellY) {
        final int hash = cellX * 7 + cellY * 39;
        final int randomValue = PseudoRandomPattern.RANDOM_SEQUENCE[Math.abs(hash) % 256];
        return this.m_availableGfx[Math.abs(randomValue) % this.m_availableGfx.length];
    }
    
    @Override
    public void getAllElementIds(final TIntHashSet result) {
        result.addAll(this.m_availableGfx);
    }
    
    static {
        MathHelper.randomBytes(RANDOM_SEQUENCE = new byte[256]);
    }
}
