package com.ankamagames.framework.script.action;

import org.apache.log4j.*;
import com.ankamagames.framework.script.action.group.*;
import java.util.*;

public class QueueActionGroupManager implements ActionGroupEventListener
{
    protected static final Logger m_logger;
    private static final QueueActionGroupManager m_instance;
    private ActionGroupFactory m_groupFactory;
    private ActionGroup m_pendingActionGroup;
    private final LinkedList<ActionGroup> m_executingActionGroups;
    private boolean m_actionInExecution;
    
    public QueueActionGroupManager() {
        super();
        this.m_groupFactory = ExecutionForcedActionGroupFactory.INSTANCE;
        this.m_executingActionGroups = new LinkedList<ActionGroup>();
        this.m_actionInExecution = false;
    }
    
    public static QueueActionGroupManager getInstance() {
        return QueueActionGroupManager.m_instance;
    }
    
    public ActionGroup addActionToPendingGroup(final Action action) {
        if (this.m_pendingActionGroup == null) {
            this.m_pendingActionGroup = this.m_groupFactory.createActionGroup();
        }
        this.m_pendingActionGroup.addAction(action);
        action.setGroup(this.m_pendingActionGroup);
        return this.m_pendingActionGroup;
    }
    
    public ActionGroup addActionToExecutingGroup(final Action action) {
        final ActionGroup actionGroup = this.m_groupFactory.createActionGroup();
        actionGroup.addAction(action);
        this.m_executingActionGroups.add(actionGroup);
        action.setGroup(actionGroup);
        return actionGroup;
    }
    
    public void killActionsToPendingGroup() {
        if (this.m_pendingActionGroup != null) {
            this.m_pendingActionGroup.kill();
        }
        this.m_pendingActionGroup = null;
    }
    
    public void clear() {
        this.killActionsToPendingGroup();
        for (int i = 0, size = this.m_executingActionGroups.size(); i < size; ++i) {
            try {
                this.m_executingActionGroups.get(i).kill();
            }
            catch (Exception e) {
                QueueActionGroupManager.m_logger.error((Object)"", (Throwable)e);
            }
        }
        this.m_executingActionGroups.clear();
        this.m_actionInExecution = false;
    }
    
    public ActionGroup getPendingActionGroup() {
        return this.m_pendingActionGroup;
    }
    
    public LinkedList<ActionGroup> getExecutingActionGroups() {
        return this.m_executingActionGroups;
    }
    
    public void executePendingGroup() {
        if (this.m_pendingActionGroup == null) {
            return;
        }
        this.m_executingActionGroups.add(this.m_pendingActionGroup);
        this.m_pendingActionGroup = null;
        if (!this.m_actionInExecution) {
            this.executeNextGroup();
        }
    }
    
    private void executeNextGroup() {
        if (!this.m_actionInExecution && !this.m_executingActionGroups.isEmpty()) {
            this.m_actionInExecution = true;
            final ActionGroup group = this.m_executingActionGroups.getFirst();
            group.addListener(this);
            group.runNextAction();
        }
    }
    
    @Override
    public void onActionGroupFinished(final ActionGroup group) {
        this.m_executingActionGroups.remove(group);
        this.m_actionInExecution = false;
        this.executeNextGroup();
    }
    
    public void traceActionContent() {
        QueueActionGroupManager.m_logger.info((Object)("Action In Execution : " + this.m_actionInExecution));
        if (!this.m_executingActionGroups.isEmpty()) {
            for (final ActionGroup group : this.m_executingActionGroups) {
                QueueActionGroupManager.m_logger.info((Object)("Executing Action Group (" + group.getActions().size() + " actions)"));
                for (final Action action : group.getActions()) {
                    QueueActionGroupManager.m_logger.info((Object)(" * " + action.getClass().getSimpleName()));
                }
            }
        }
        if (this.m_pendingActionGroup != null) {
            QueueActionGroupManager.m_logger.info((Object)("Pending Action Group (" + this.m_pendingActionGroup.getActions().size() + " groupes)"));
            for (final Action action2 : this.m_pendingActionGroup.getActions()) {
                QueueActionGroupManager.m_logger.info((Object)(" - " + action2.getClass().getSimpleName()));
            }
        }
        else {
            QueueActionGroupManager.m_logger.info((Object)"Pending Action Group is null");
        }
    }
    
    public void executeAllAction() {
        while (!this.m_executingActionGroups.isEmpty()) {
            final ActionGroup actionGroup = this.m_executingActionGroups.getFirst();
            actionGroup.removeListener(this);
            actionGroup.executeAllAction();
            this.m_executingActionGroups.remove(actionGroup);
        }
    }
    
    public void setGroupFactory(final ActionGroupFactory groupFactory) {
        this.m_groupFactory = groupFactory;
    }
    
    static {
        m_logger = Logger.getLogger((Class)QueueActionGroupManager.class);
        m_instance = new QueueActionGroupManager();
    }
}
