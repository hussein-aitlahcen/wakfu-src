package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.graphics.*;

public class EffectAreaDestroyedAction extends AbstractFightTimedAction
{
    public EffectAreaDestroyedAction(final int uniqueId, final int actionType, final int actionId, final int fightId) {
        super(uniqueId, actionType, actionId, fightId);
    }
    
    public long onRun() {
        if (this.getEffectAreaManager() != null) {
            final BasicEffectArea area = this.getEffectAreaManager().getActiveEffectAreaWithId(this.getInstigatorId());
            if (area != null && area instanceof GraphicalAreaProvider) {
                final GraphicalAreaProvider effectArea = (GraphicalAreaProvider)area;
                final GraphicalArea graphicalArea = effectArea.getGraphicalArea();
                if (effectArea.hasAnimation("AnimMort") && graphicalArea.shouldPlayDeathAnimation()) {
                    return graphicalArea.setAnimation("AnimMort");
                }
                return 0L;
            }
        }
        return 0L;
    }
    
    @Override
    protected void onActionFinished() {
        if (this.getEffectAreaManager() != null) {
            final BasicEffectArea area = this.getEffectAreaManager().getActiveEffectAreaWithId(this.getInstigatorId());
            if (area != null) {
                final GraphicalAreaProvider effectArea = (GraphicalAreaProvider)area;
                UIOverHeadInfosFrame.getInstance().hideOverHead(effectArea.getGraphicalArea().getAnimatedElement());
                this.getEffectContext().getEffectAreaManager().removeEffectArea(area);
            }
        }
        super.onActionFinished();
    }
}
