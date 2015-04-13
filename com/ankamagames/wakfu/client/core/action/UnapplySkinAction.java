package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class UnapplySkinAction extends TimedAction
{
    private CharacterInfo m_target;
    
    public UnapplySkinAction(final int uniqueId, final int actionType, final int actionId, final CharacterInfo character) {
        super(uniqueId, actionType, actionId);
        this.setUniqueId(uniqueId);
        this.setActionType(actionType);
        this.setActionId(actionId);
        this.setTarget(character);
    }
    
    @Override
    protected long onRun() {
        final CharacterActor actor = this.m_target.getActor();
        if (actor.containsAnimation("AnimTransEffect-Fin")) {
            actor.setAnimation("AnimTransEffect-Fin");
            return actor.getAnimationDuration("AnimTransEffect-Fin");
        }
        this.resetSkin();
        return 0L;
    }
    
    private void resetSkin() {
        final CharacterActor actor = this.m_target.getActor();
        this.m_target.setForcedGfxId(0);
        this.m_target.refreshDisplayEquipment();
        actor.onAnmLoaded(actor.getAnm(), new Runnable() {
            @Override
            public void run() {
                actor.getCurrentAttack().startUsageAndNotify(actor);
                actor.getAnmInstance().updateHiddenParts();
            }
        });
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_target, "name", "nameAndLevel");
    }
    
    @Override
    protected void onActionFinished() {
        this.resetSkin();
    }
    
    public CharacterInfo getTarget() {
        return this.m_target;
    }
    
    public void setTarget(final CharacterInfo target) {
        this.m_target = target;
    }
}
