package com.ankamagames.wakfu.client.core.game.fight;

import com.ankamagames.baseImpl.graphics.isometric.lights.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class ExternalFightCellLightModifier extends InOutCellLightModifier implements LitSceneModifier
{
    private static final int MAX_DISTANCE_COLORATION = 3;
    private static final float[] OUTSIDE_FIGHT_LIGHT_MODIFIER_START;
    private static final float[] OUTSIDE_FIGHT_LIGHT_MODIFIER_END;
    private static final long OUTSIDE_FIGHT_LIGHT_MODIFIER_DURATION = 500L;
    private static final int BORDER_LIGHT_MODIFICATION_RATIO = 2;
    private FightMap m_fightMap;
    private boolean m_useless;
    private final TLongFloatHashMap m_cellsAroundBubble;
    
    public ExternalFightCellLightModifier() {
        super(ExternalFightCellLightModifier.OUTSIDE_FIGHT_LIGHT_MODIFIER_START, ExternalFightCellLightModifier.OUTSIDE_FIGHT_LIGHT_MODIFIER_END, 500L);
        this.m_cellsAroundBubble = new TLongFloatHashMap();
    }
    
    public void setFightMap(final FightMap fightMap) {
        this.m_fightMap = fightMap;
        final WorldInfoManager.WorldInfo info = WorldInfoManager.getInstance().getInfo(fightMap.getWorldId());
        this.m_useless = (info != null && !info.m_blackOutOfFight);
    }
    
    @Override
    public int getPriority() {
        return 700;
    }
    
    @Override
    public boolean useless() {
        return this.m_useless;
    }
    
    @Override
    public void apply(final int x, final int y, final int z, final int layerId, final float[] colors) {
        final long coordHashCode = MathHelper.getLongFromTwoInt(x, y);
        if (this.m_cellsAroundBubble.contains(coordHashCode)) {
            final float distanceColoringRatio = this.m_cellsAroundBubble.get(coordHashCode);
            final int n = 0;
            colors[n] *= Math.min(this.m_currentModifierColor[0] + distanceColoringRatio, 1.0f);
            final int n2 = 1;
            colors[n2] *= Math.min(this.m_currentModifierColor[1] + distanceColoringRatio, 1.0f);
            final int n3 = 2;
            colors[n3] *= Math.min(this.m_currentModifierColor[2] + distanceColoringRatio, 1.0f);
        }
    }
    
    static {
        OUTSIDE_FIGHT_LIGHT_MODIFIER_START = new float[] { 1.0f, 1.0f, 1.0f };
        OUTSIDE_FIGHT_LIGHT_MODIFIER_END = new float[] { 0.35f, 0.35f, 0.35f };
    }
}
