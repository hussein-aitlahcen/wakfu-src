package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class InteractiveElementUpdateAction extends TimedAction implements Releasable
{
    private long m_elementId;
    private byte[] m_datas;
    private static final MonitoredPool m_staticPool;
    
    public static InteractiveElementUpdateAction checkout(final int uniqueId, final int actionType, final int actionId, final long elementId, final byte[] data) {
        try {
            final InteractiveElementUpdateAction interactiveElementUpdateAction = (InteractiveElementUpdateAction)InteractiveElementUpdateAction.m_staticPool.borrowObject();
            interactiveElementUpdateAction.setUniqueId(uniqueId);
            interactiveElementUpdateAction.setActionType(actionType);
            interactiveElementUpdateAction.setActionId(actionId);
            interactiveElementUpdateAction.m_datas = data;
            interactiveElementUpdateAction.m_elementId = elementId;
            return interactiveElementUpdateAction;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut sur un InteractiveElementUpdateAction : ", e);
        }
    }
    
    @Override
    public void release() {
        try {
            InteractiveElementUpdateAction.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            InteractiveElementUpdateAction.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + "(normalement impossible)"));
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_elementId = 0L;
        this.m_datas = null;
    }
    
    private InteractiveElementUpdateAction() {
        super(0, 0, 0);
    }
    
    @Override
    protected long onRun() {
        if (this.m_datas != null) {
            final WakfuClientMapInteractiveElement element = (WakfuClientMapInteractiveElement)LocalPartitionManager.getInstance().getInteractiveElement(this.m_elementId);
            if (element != null) {
                element.fromBuild(this.m_datas);
                element.notifyViews();
            }
            else {
                InteractiveElementUpdateAction.m_logger.error((Object)"L'\u00e9l\u00e9ment interactif \u00e0 mettre \u00e0 jour n'est pas/plus dans les partitions g\u00e9r\u00e9es par le client.");
            }
        }
        else {
            InteractiveElementUpdateAction.m_logger.warn((Object)"Message de mise \u00e0 jour d'\u00e9l\u00e9ment interactif re\u00e7u sans aucune donn\u00e9e \u00e0 mettre \u00e0 jour.");
        }
        return 0L;
    }
    
    @Override
    protected void onActionFinished() {
        this.release();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<InteractiveElementUpdateAction>() {
            @Override
            public InteractiveElementUpdateAction makeObject() {
                return new InteractiveElementUpdateAction(null);
            }
        });
    }
}
