package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.systemMessage.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class EndPlacementAction extends TimedAction implements Releasable
{
    private FightInfo m_fight;
    private static final MonitoredPool m_staticPool;
    
    public static EndPlacementAction checkout(final int uniqueId, final int actionType, final int actionId, final FightInfo fight) {
        try {
            final EndPlacementAction endPlacementAction = (EndPlacementAction)EndPlacementAction.m_staticPool.borrowObject();
            endPlacementAction.setUniqueId(uniqueId);
            endPlacementAction.setActionType(actionType);
            endPlacementAction.setActionId(actionId);
            endPlacementAction.setFight(fight);
            return endPlacementAction;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut sur un EndPlacementAction : ", e);
        }
    }
    
    @Override
    public void release() {
        try {
            EndPlacementAction.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            EndPlacementAction.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + "(normalement impossible)"));
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_fight = null;
    }
    
    private EndPlacementAction() {
        super(0, 0, 0);
    }
    
    @Override
    protected long onRun() {
        for (final CharacterInfo characterInfo : this.m_fight.getFighters()) {
            if (characterInfo instanceof PlayerCharacter) {
                final CharacterActor actor = characterInfo.getActor();
                if (actor.getCurrentAttack() != NoneAttack.getInstance()) {
                    continue;
                }
                characterInfo.changeToSpellAttackIfNecessary();
            }
        }
        if (WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOrObservedFight() != this.m_fight) {
            return 0L;
        }
        WakfuSystemMessageManager.getInstance().showMessage(new SystemMessageData(WakfuSystemMessageManager.SystemMessageType.FIGHT_INFO, WakfuTranslator.getInstance().getString("fight.start"), 3000));
        try {
            ((Fight)this.m_fight).onPlacementEnd();
        }
        catch (Exception e) {
            EndPlacementAction.m_logger.error((Object)"Erreur lors du placement : ", (Throwable)e);
        }
        PropertiesProvider.getInstance().setPropertyValue("isInFightCreationOrPlacement", false);
        return 0L;
    }
    
    @Override
    protected void onActionFinished() {
        this.release();
    }
    
    public FightInfo getFight() {
        return this.m_fight;
    }
    
    public void setFight(final FightInfo fight) {
        this.m_fight = fight;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<EndPlacementAction>() {
            @Override
            public EndPlacementAction makeObject() {
                return new EndPlacementAction(null);
            }
        });
    }
}
