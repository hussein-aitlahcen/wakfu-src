package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.*;

public class QueueCollectManager implements Runnable
{
    private static final Logger m_logger;
    private static final QueueCollectManager m_instance;
    private static final float[] m_selectedColor;
    private final LinkedList<QueueCollectAction> m_collectActions;
    public static final boolean QUEUE_COLLECT_ACTIVATED = false;
    
    public static QueueCollectManager getInstance() {
        return QueueCollectManager.m_instance;
    }
    
    private QueueCollectManager() {
        super();
        this.m_collectActions = new LinkedList<QueueCollectAction>();
    }
    
    public void executeNextCollectAction() {
        while (!this.executeCollectAction(this.getNextCollectAction())) {}
    }
    
    private boolean executeCollectAction(final QueueCollectAction collectAction) {
        if (collectAction == null) {
            return true;
        }
        collectAction.removeParticle();
        if (collectAction.isEnabled() && collectAction.isRunnable()) {
            collectAction.run(true);
            return true;
        }
        return false;
    }
    
    QueueCollectAction getNextCollectAction() {
        if (this.m_collectActions.isEmpty()) {
            return null;
        }
        return this.m_collectActions.poll();
    }
    
    public void addCollectAction(final QueueCollectAction queueCollectAction) {
        if (this.containsCollectAction(queueCollectAction)) {
            return;
        }
        queueCollectAction.addParticle();
        this.m_collectActions.add(queueCollectAction);
    }
    
    public boolean isQueueEmpty() {
        return this.m_collectActions.isEmpty();
    }
    
    public void clear() {
        QueueCollectManager.m_logger.info((Object)"[QUEUE_COLLECT] on clear toutes les actions");
        this.clearCurrentCollectAction();
        for (int i = this.m_collectActions.size() - 1; i >= 0; --i) {
            this.removeCollectAction(this.m_collectActions.get(i));
        }
    }
    
    public boolean containsCollectAction(final QueueCollectAction mruCollectAction) {
        final CollectAction collectAction = mruCollectAction.getCollectAction();
        final Object collectedResource = mruCollectAction.getCollectedRessource();
        if (collectAction == null || collectedResource == null) {
            return true;
        }
        for (final QueueCollectAction queueCollectAction : this.m_collectActions) {
            if (collectAction.equals(queueCollectAction.getCollectAction()) && collectAction.equals(queueCollectAction.getCollectedRessource())) {
                return true;
            }
        }
        return false;
    }
    
    public void removeCollectAction(final QueueCollectAction mruCollectAction) {
        if (this.m_collectActions.contains(mruCollectAction)) {
            mruCollectAction.removeParticle();
            this.m_collectActions.remove(mruCollectAction);
        }
    }
    
    public boolean hasActionOnResource(final Resource resource) {
        for (final QueueCollectAction mruCollectAction : this.m_collectActions) {
            if (mruCollectAction.getCollectedRessource().equals(resource)) {
                return true;
            }
        }
        return false;
    }
    
    public void createTimeOut() {
        this.deleteTimeOut();
        ProcessScheduler.getInstance().schedule(this, 5000L, 1);
    }
    
    public void deleteTimeOut() {
        ProcessScheduler.getInstance().remove(this);
    }
    
    public boolean isCollectRunning() {
        final AbstractOccupation currentOccupation = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOccupation();
        return currentOccupation != null && (currentOccupation.getOccupationTypeId() == 3 || currentOccupation.getOccupationTypeId() == 6);
    }
    
    public void clearCurrentCollectAction() {
        this.deleteTimeOut();
    }
    
    @Override
    public void run() {
        QueueCollectManager.m_logger.warn((Object)"[QUEUE_COLLECT] timeOut execut\u00e9");
        this.executeNextCollectAction();
    }
    
    static {
        m_logger = Logger.getLogger((Class)QueueCollectManager.class);
        m_instance = new QueueCollectManager();
        m_selectedColor = new float[] { 1.0f, 1.0f, 1.0f, 0.6f };
    }
}
