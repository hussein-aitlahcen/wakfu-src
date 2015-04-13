package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import java.util.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class BombComboEffectArea extends AbstractBombComboEffectArea implements GraphicalAreaProvider, ScriptProvider, OverHead
{
    private int m_scriptId;
    private final GraphicalArea m_graphicalArea;
    
    private BombComboEffectArea() {
        super();
        this.m_graphicalArea = new GraphicalAreaImplBuilder(this).build();
    }
    
    public BombComboEffectArea(final int id, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int scriptId, final int targetsToShow, final float[] deactivationDelay, final String areaGfx, final String cellGfx, final String aps, final String cellAps, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(id, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_scriptId = scriptId;
        final GraphicalAreaImplBuilder builder = new GraphicalAreaImplBuilder(this).withAps(aps).withCellAps(cellAps).withCellGfx(cellGfx).withGfx(areaGfx);
        this.m_graphicalArea = builder.build();
    }
    
    @Override
    protected BasicEffectArea instanceNew() {
        return new BombComboEffectArea();
    }
    
    @Override
    public BombComboEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final BombComboEffectArea area = (BombComboEffectArea)super.instanceAnother(parameters);
        area.m_scriptId = this.m_scriptId;
        area.m_graphicalArea.copy(this.m_graphicalArea);
        area.initialize();
        return area;
    }
    
    @Override
    public void initialize() {
    }
    
    @Override
    public void execute(final int targetX, final int targetY, final short targetZ, final RunningEffect triggeringRE) {
    }
    
    @Override
    public GraphicalArea getGraphicalArea() {
        return this.m_graphicalArea;
    }
    
    @Override
    public boolean hasAnimation(final String animationName) {
        return false;
    }
    
    @Override
    public int getIconId() {
        return 0;
    }
    
    @Override
    public String getName() {
        if (this.m_graphicalArea != null) {
            return this.m_graphicalArea.getName();
        }
        return "";
    }
    
    @Override
    public String getFormatedOverheadText() {
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(this.getName());
        final ArrayList<String> effects = CastableDescriptionGenerator.generateDescription(new EffectAreaWriter(this, this.m_level, 0));
        for (int i = 0, size = effects.size(); i < size; ++i) {
            sb.newLine().append(effects.get(i));
        }
        return sb.finishAndToString();
    }
    
    @Override
    public Color getOverHeadborderColor() {
        return Color.WHITE;
    }
    
    @Override
    public int getScriptId() {
        return this.m_scriptId;
    }
}
