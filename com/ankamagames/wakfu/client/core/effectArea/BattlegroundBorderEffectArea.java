package com.ankamagames.wakfu.client.core.effectArea;

import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import org.apache.commons.lang3.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public final class BattlegroundBorderEffectArea extends AbstractBattlegroundBorderEffectArea implements GraphicalAreaProvider, OverHead
{
    private int m_scriptId;
    private final GraphicalArea m_graphicalArea;
    
    private BattlegroundBorderEffectArea() {
        super();
        this.m_graphicalArea = new GraphicalAreaImplBuilder(this).build();
    }
    
    public BattlegroundBorderEffectArea(final int id, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int scriptId, final int targetsToShow, final float[] deactivationDelay, final String areaGfx, final String cellGfx, final String aps, final String cellAps, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(id, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_scriptId = scriptId;
        final GraphicalAreaImplBuilder builder = new GraphicalAreaImplBuilder(this);
        builder.withAps(aps).withCellAps(cellAps).withCellGfx(cellGfx).withGfx("");
        this.m_graphicalArea = builder.build();
    }
    
    public BasicEffectArea instanceNew() {
        return new BattlegroundBorderEffectArea();
    }
    
    @Override
    public BattlegroundBorderEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final BattlegroundBorderEffectArea bgEffectArea = (BattlegroundBorderEffectArea)super.instanceAnother(parameters);
        bgEffectArea.m_scriptId = this.m_scriptId;
        bgEffectArea.m_graphicalArea.copy(this.m_graphicalArea);
        bgEffectArea.initialize();
        return bgEffectArea;
    }
    
    @Override
    public void execute(final int targetX, final int targetY, final short targetZ, final RunningEffect triggeringRE) {
    }
    
    public int getScriptId() {
        return this.m_scriptId;
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
        final String areaName = WakfuTranslator.getInstance().getString(6, (int)this.getBaseId(), new Object[0]);
        if (StringUtils.isNotBlank(areaName)) {
            sb.b().append(areaName)._b();
        }
        final ArrayList<String> effects = CastableDescriptionGenerator.generateDescription(new EffectAreaWriter(this, this.m_level, 0));
        for (int i = 0, size = effects.size(); i < size; ++i) {
            sb.newLine().append(effects.get(i));
        }
        return sb.finishAndToString();
    }
    
    @Override
    public GraphicalArea getGraphicalArea() {
        return this.m_graphicalArea;
    }
    
    @Override
    public void release() {
        super.release();
        UIFightFrame.getInstance().onBattleGroundBorderEffectAreaReleased(this);
    }
    
    @Override
    public boolean hasAnimation(final String animationName) {
        return false;
    }
    
    @Override
    public String getName() {
        if (this.m_graphicalArea != null) {
            return this.m_graphicalArea.getName();
        }
        return "";
    }
}
