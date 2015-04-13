package com.ankamagames.wakfu.client.core.game.dimensionalBag.gfxProvider.groundPattern.impl;

import com.ankamagames.wakfu.client.core.game.dimensionalBag.gfxProvider.groundPattern.*;
import gnu.trove.*;

public class DirectionalPattern implements GroundPattern
{
    private final int m_gfxDirX;
    private final int m_gfxDirY;
    
    public DirectionalPattern(final int gfxDirX, final int gfxDirY) {
        super();
        this.m_gfxDirX = gfxDirX;
        this.m_gfxDirY = gfxDirY;
    }
    
    @Override
    public int getGfx(final int cellX, final int cellY) {
        if (cellX == 1 && cellY == 0) {
            return this.m_gfxDirX;
        }
        if (cellX == 0 && cellY == 1) {
            return this.m_gfxDirY;
        }
        return 0;
    }
    
    @Override
    public void getAllElementIds(final TIntHashSet result) {
        result.add(this.m_gfxDirX);
        result.add(this.m_gfxDirY);
    }
}
