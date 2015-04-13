package com.ankamagames.wakfu.client.core.action.FightBeginning;

import com.ankamagames.framework.script.action.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.console.command.display.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class StartPlacementAction extends TimedAction implements Releasable
{
    private static final Logger m_logger;
    private FightInfo m_fight;
    private int m_remainingTime;
    private long m_currentTime;
    private static final MonitoredPool m_staticPool;
    
    public static StartPlacementAction checkout(final int uniqueId, final int actionType, final int actionId, final FightInfo fight, final int remainingTime, final long currentTime) {
        try {
            final StartPlacementAction startPlacementAction = (StartPlacementAction)StartPlacementAction.m_staticPool.borrowObject();
            startPlacementAction.setUniqueId(uniqueId);
            startPlacementAction.setActionType(actionType);
            startPlacementAction.setActionId(actionId);
            startPlacementAction.m_fight = fight;
            startPlacementAction.m_remainingTime = remainingTime;
            startPlacementAction.m_currentTime = currentTime;
            return startPlacementAction;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut : ", e);
        }
    }
    
    @Override
    public void release() {
        try {
            StartPlacementAction.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            StartPlacementAction.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + "(normalement impossible)"));
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_fight = null;
        this.m_remainingTime = 0;
        this.m_currentTime = 0L;
    }
    
    private StartPlacementAction() {
        super(0, 0, 0);
    }
    
    @Override
    protected long onRun() {
        if (this.m_fight instanceof Fight) {
            final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
            try {
                final boolean applyAlphaMask = wakfuGamePreferences.getBooleanValue(WakfuKeyPreferenceStoreEnum.ALPHA_MASK_ACTIVATED_KEY);
                if (applyAlphaMask) {
                    AlphaMaskCommand.applyAlphaMaskOnFight(true);
                }
                OptionsDialogActions.proceedHideFightOccluders(wakfuGamePreferences.getBooleanValue(WakfuKeyPreferenceStoreEnum.HIDE_FIGHT_OCCLUDERS_ACTIVATED_KEY));
            }
            catch (Exception e) {
                StartPlacementAction.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
            final long newcurrentTime = System.currentTimeMillis();
            final long remainingTime = this.m_remainingTime - (newcurrentTime - this.m_currentTime);
            try {
                ((Fight)this.m_fight).startPlacement((int)remainingTime);
            }
            catch (Exception e2) {
                StartPlacementAction.m_logger.error((Object)"Erreur lors du d\u00e9but du placement : ", (Throwable)e2);
            }
        }
        else if (this.m_fight instanceof ExternalFightInfo) {
            ((ExternalFightInfo)this.m_fight).setStatus(AbstractFight.FightStatus.PLACEMENT);
        }
        return 0L;
    }
    
    @Override
    protected void onActionFinished() {
        this.release();
    }
    
    static {
        m_logger = Logger.getLogger((Class)StartPlacementAction.class);
        m_staticPool = new MonitoredPool(new ObjectFactory<StartPlacementAction>() {
            @Override
            public StartPlacementAction makeObject() {
                return new StartPlacementAction(null);
            }
        });
    }
}
