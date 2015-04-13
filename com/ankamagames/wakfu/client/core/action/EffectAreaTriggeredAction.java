package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.script.fightLibrary.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class EffectAreaTriggeredAction extends AbstractFightScriptedAction
{
    private final boolean m_apply;
    
    public EffectAreaTriggeredAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final boolean apply, final long areaBaseId) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_apply = apply;
        this.addJavaFunctionsLibrary(new EffectAreaFunctionsLibrary(this));
        StaticEffectAreaManager.getInstance().getAreaFromId(areaBaseId);
        if (this.getEffectAreaManager() != null) {
            final BasicEffectArea area = StaticEffectAreaManager.getInstance().getAreaFromId(areaBaseId);
            if (area instanceof ScriptProvider) {
                this.setScriptFileId(((ScriptProvider)area).getScriptId());
            }
        }
    }
    
    @Override
    public long onRun() {
        final CharacterInfo fighter = this.getFighterById(this.getTargetId());
        if (this.getEffectAreaManager() != null) {
            final BasicEffectArea area = this.getEffectAreaManager().getActiveEffectAreaWithId(this.getInstigatorId());
            if (area != null) {
                if (this.m_apply) {
                    area.triggers((RunningEffect)null, fighter);
                    long animationDuration = 0L;
                    if (area instanceof GraphicalAreaProvider) {
                        final GraphicalAreaProvider effectArea = (GraphicalAreaProvider)area;
                        if (effectArea.hasAnimation("AnimAttaque")) {
                            animationDuration = effectArea.getGraphicalArea().setAnimation("AnimAttaque");
                        }
                    }
                    final long scriptDuration = super.onRun();
                    return (scriptDuration > animationDuration) ? scriptDuration : animationDuration;
                }
                area.untriggers(fighter);
            }
        }
        this.fireActionFinishedEvent();
        return -1L;
    }
    
    @Override
    protected void onActionFinished() {
    }
}
