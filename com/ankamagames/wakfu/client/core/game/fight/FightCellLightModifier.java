package com.ankamagames.wakfu.client.core.game.fight;

import com.ankamagames.baseImpl.graphics.isometric.lights.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;

public class FightCellLightModifier extends InOutCellLightModifier implements LitSceneModifier
{
    public static final float BORDER_RATIO = 1.5f;
    private static final float[] OUTSIDE_FIGHT_LIGHT_MODIFIER_START;
    private static final float[] OUTSIDE_FIGHT_LIGHT_MODIFIER_END;
    private static final long OUTSIDE_FIGHT_LIGHT_MODIFIER_DURATION = 500L;
    private static final float MAX_BUBBLE_LIGHTNING_FACTOR = 1.4f;
    private final FightMap m_fightMap;
    private float m_color;
    private final boolean m_useless;
    private boolean m_hideBorder;
    
    public FightCellLightModifier(final FightMap fightMap) {
        super(FightCellLightModifier.OUTSIDE_FIGHT_LIGHT_MODIFIER_START, FightCellLightModifier.OUTSIDE_FIGHT_LIGHT_MODIFIER_END, 500L);
        this.m_color = 1.0f;
        this.m_fightMap = fightMap;
        final WorldInfoManager.WorldInfo info = WorldInfoManager.getInstance().getInfo(fightMap.getWorldId());
        this.m_useless = (info != null && !info.m_blackOutOfFight);
    }
    
    public void setColor(final float color) {
        this.m_color = 0.5f * color + 0.5f;
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
    public void apply(final int x, final int y, final int z, final int layerId, final float[] color) {
        final boolean isBorderCell = this.m_fightMap.isBorder(x, y);
        if (isBorderCell || !this.m_fightMap.isInsideMapShape(x, y)) {
            this.multColor(color, (!this.m_hideBorder && isBorderCell) ? 0.9f : 0.5f);
        }
        else if (this.m_color != 1.0f && this.m_fightMap.isBlocked(x, y)) {
            this.multColor(color, this.m_color);
        }
        final float nightIntensity = IsoSceneLightManager.INSTANCE.getNightLightIntensityFactor();
        if (nightIntensity > 0.00390625f) {
            final int n = 0;
            color[n] *= 1.0f + 0.9f * nightIntensity;
            final int n2 = 1;
            color[n2] *= 1.0f + 0.9f * nightIntensity;
            final int n3 = 2;
            color[n3] *= 1.0f + 0.9f * nightIntensity;
        }
    }
    
    private void multColor(final float[] color, final float modifier) {
        final int n = 0;
        color[n] *= modifier * this.m_currentModifierColor[0];
        final int n2 = 1;
        color[n2] *= modifier * this.m_currentModifierColor[1];
        final int n3 = 2;
        color[n3] *= modifier * this.m_currentModifierColor[2];
    }
    
    public void hideBorder(final boolean hideBorder) {
        this.m_hideBorder = hideBorder;
    }
    
    static {
        OUTSIDE_FIGHT_LIGHT_MODIFIER_START = new float[] { 1.0f, 1.0f, 1.0f };
        OUTSIDE_FIGHT_LIGHT_MODIFIER_END = new float[] { 0.5f, 0.5f, 0.5f };
    }
}
