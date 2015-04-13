package com.ankamagames.wakfu.client.core.action.FightBeginning;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class StartActionAction extends TimedAction implements Releasable
{
    private FightInfo m_fight;
    private boolean m_needSetTimePointGap;
    private int m_timePointGap;
    private static final MonitoredPool m_staticPool;
    
    public static StartActionAction checkout(final int uniqueId, final int actionType, final int actionId, final FightInfo fight) {
        try {
            final StartActionAction startActionAction = (StartActionAction)StartActionAction.m_staticPool.borrowObject();
            startActionAction.setUniqueId(uniqueId);
            startActionAction.setActionType(actionType);
            startActionAction.setActionId(actionId);
            startActionAction.setFight(fight);
            return startActionAction;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut : ", e);
        }
    }
    
    @Override
    public void release() {
        try {
            StartActionAction.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            StartActionAction.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + "(normalement impossible)"));
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_fight = null;
        this.m_needSetTimePointGap = false;
        this.m_timePointGap = 0;
    }
    
    private StartActionAction() {
        super(0, 0, 0);
    }
    
    @Override
    protected long onRun() {
        this.cleanAllReadyForFightIcons(this.m_fight);
        try {
            if (this.m_needSetTimePointGap) {
                ((Fight)this.m_fight).getTimeline().setTimePointGap(this.m_timePointGap);
            }
            ((Fight)this.m_fight).startAction();
        }
        catch (Exception e) {
            StartActionAction.m_logger.error((Object)"Error : ", (Throwable)e);
        }
        PropertiesProvider.getInstance().setPropertyValue("isInFightCreationOrPlacement", false);
        return 0L;
    }
    
    public void cleanAllReadyForFightIcons(final FightInfo fight) {
        for (final CharacterInfo characterInfo : fight.getFighters()) {
            characterInfo.getActor().clearCrossSwordParticleSystem();
        }
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
    
    public void setNeedSetTimePointGap(final boolean needSetTimePointGap) {
        this.m_needSetTimePointGap = needSetTimePointGap;
    }
    
    public void setTimePointGap(final int timePointGap) {
        this.m_timePointGap = timePointGap;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<StartActionAction>() {
            @Override
            public StartActionAction makeObject() {
                return new StartActionAction(null);
            }
        });
    }
}
