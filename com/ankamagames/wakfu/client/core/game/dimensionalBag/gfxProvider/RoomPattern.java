package com.ankamagames.wakfu.client.core.game.dimensionalBag.gfxProvider;

import com.ankamagames.wakfu.client.core.game.dimensionalBag.gfxProvider.groundPattern.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public class RoomPattern implements ListElementUsed
{
    private final BorderPattern m_border;
    private final GroundPattern m_ground;
    
    public RoomPattern(final BorderPattern border, final GroundPattern ground) {
        super();
        this.m_border = border;
        this.m_ground = ground;
    }
    
    public int getElementId(final short x, final short y) {
        return this.m_ground.getGfx(x, y);
    }
    
    @Override
    public void getAllElementIds(final TIntHashSet elementIds) {
        this.m_border.getAllElementIds(elementIds);
        this.m_ground.getAllElementIds(elementIds);
    }
    
    public int getBorderElementIdFromMask(final int borderMask) {
        return this.m_border.getElementIdFromMask(borderMask);
    }
    
    public static void changeZOrder(final DisplayedScreenElement element, final int borderMask) {
        BorderPattern.changeZOrder(element, borderMask);
    }
}
