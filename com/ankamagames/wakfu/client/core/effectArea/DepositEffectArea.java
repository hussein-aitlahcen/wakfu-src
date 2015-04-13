package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class DepositEffectArea extends AbstractDepositEffectArea implements GraphicalAreaProvider, OverHead, ScriptProvider
{
    private int m_scriptId;
    private final GraphicalArea m_graphicalArea;
    
    private DepositEffectArea() {
        super();
        this.m_graphicalArea = new GraphicalAreaImplBuilder(this).build();
    }
    
    public DepositEffectArea(final int id, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int scriptId, final int targetsToShow, final float[] deactivationDelay, final String areaGfx, final String cellGfx, final String aps, final String cellAps, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(id, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_scriptId = scriptId;
        final GraphicalAreaImplBuilder builder = new GraphicalAreaImplBuilder(this).withAps(aps).withCellAps(cellAps).withCellGfx(cellGfx).withGfx(areaGfx);
        this.m_graphicalArea = builder.build();
    }
    
    public BasicEffectArea instanceNew() {
        return new DepositEffectArea();
    }
    
    @Override
    public DepositEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final DepositEffectArea area = (DepositEffectArea)super.instanceAnother(parameters);
        area.m_scriptId = this.m_scriptId;
        area.m_graphicalArea.copy(this.m_graphicalArea);
        area.initialize();
        return area;
    }
    
    @Override
    public void execute(final int targetX, final int targetY, final short targetZ, final RunningEffect triggeringRE) {
    }
    
    @Override
    public int getScriptId() {
        return this.m_scriptId;
    }
    
    @Override
    public void setDirection(final Direction8 direction) {
        super.setDirection(direction);
        if (this.m_graphicalArea.getAnimatedElement() != null) {
            this.m_graphicalArea.getAnimatedElement().setDirection(this.getDirection());
        }
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
    public Color getOverHeadborderColor() {
        return Color.WHITE;
    }
    
    @Override
    public String getFormatedOverheadText() {
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.b().append(WakfuTranslator.getInstance().getString(6, (int)this.getBaseId(), new Object[0]))._b();
        final ArrayList<String> effects = CastableDescriptionGenerator.generateDescription(new EffectAreaWriter(this, this.m_level, 0));
        for (int i = 0, size = effects.size(); i < size; ++i) {
            sb.newLine().append(effects.get(i));
        }
        return sb.finishAndToString();
    }
    
    @Override
    public String getName() {
        if (this.m_graphicalArea != null) {
            return this.m_graphicalArea.getName();
        }
        return "";
    }
}
