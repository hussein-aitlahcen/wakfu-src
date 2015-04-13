package com.ankamagames.framework.script.action.group;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import java.util.*;

final class ExecutionForcedActionGroup implements ActionGroup
{
    public static final boolean DEBUG_MODE = false;
    private static final Logger m_logger;
    private final Collection<ActionGroupEventListener> m_listeners;
    private final LinkedList<Action> m_actions;
    
    ExecutionForcedActionGroup() {
        super();
        this.m_listeners = new ArrayList<ActionGroupEventListener>();
        this.m_actions = new LinkedList<Action>();
    }
    
    @Override
    public LinkedList<Action> getActions() {
        return this.m_actions;
    }
    
    @Override
    public void addAction(final Action action) {
        this.m_actions.add(action);
    }
    
    public Action removeAction(final int uniqueId) {
        final Action action = this.getActionByUniqueId(uniqueId);
        if (action != null) {
            this.m_actions.remove(action);
        }
        return action;
    }
    
    @Override
    public Action getActionByUniqueId(final int uniqueId) {
        for (final Action action : this.m_actions) {
            if (action.getUniqueId() == uniqueId) {
                return action;
            }
        }
        return null;
    }
    
    @Override
    public Action getActionByTypeAndId(final int actionType, final int actionId) {
        for (final Action action : this.m_actions) {
            if (action.getActionId() == actionId && action.getActionType() == actionType) {
                return action;
            }
        }
        return null;
    }
    
    @Override
    public Action getActionByType(final int actionType) {
        for (final Action action : this.m_actions) {
            if (action.getActionType() == actionType) {
                return action;
            }
        }
        return null;
    }
    
    public Action getActionByTarget(final int target) {
        for (final Action action : this.m_actions) {
            if (action.getTargetId() == target) {
                return action;
            }
        }
        return null;
    }
    
    public Iterable<Long> getTargets() {
        final Collection<Long> list = new ArrayList<Long>();
        for (final Action action : this.m_actions) {
            final long targetId = action.getTargetId();
            if (targetId != Long.MIN_VALUE && !list.contains(targetId)) {
                list.add(targetId);
            }
        }
        return list;
    }
    
    public Action getActionBySpecificActionOnTarget(final int targetId, final int actionType, final int actionId) {
        for (final Action action : this.m_actions) {
            if (action.getTargetId() == targetId && action.getActionType() == actionType && action.getActionId() == actionId) {
                return action;
            }
        }
        return null;
    }
    
    @Override
    public void addListener(final ActionGroupEventListener listener) {
        this.m_listeners.add(listener);
    }
    
    @Override
    public void removeListener(final ActionGroupEventListener listener) {
        this.m_listeners.remove(listener);
    }
    
    @Override
    public void runNextAction() {
        if (this.m_actions == null || this.m_actions.isEmpty()) {
            this.fireActionGroupFinishedEvent();
            return;
        }
        final Action action = this.m_actions.getFirst();
        this.runAction(action, true);
    }
    
    @Override
    public void runAction(final Action action, final boolean waitFinishEventToRemove) {
        if (this.m_actions != null && this.m_actions.contains(action)) {
            final Iterator<Action> it = this.m_actions.iterator();
            while (it.hasNext()) {
                final Action forcedPrereqAction = it.next();
                if (forcedPrereqAction == action) {
                    break;
                }
                if (forcedPrereqAction.hasRunned()) {
                    continue;
                }
                if (waitFinishEventToRemove) {
                    forcedPrereqAction.addListener(this);
                }
                else {
                    it.remove();
                }
                this.runActionWithDebugMessage(forcedPrereqAction, "Forced execution");
            }
        }
        if (waitFinishEventToRemove) {
            action.addListener(this);
        }
        else {
            this.removeAction(action.getUniqueId());
        }
        this.runActionWithDebugMessage(action, "In Group order");
    }
    
    private void runActionWithDebugMessage(final Action action, final String message) {
        try {
            action.setHasRunned(true);
            action.run();
        }
        catch (Exception e) {
            ExecutionForcedActionGroup.m_logger.error((Object)("[_FL_] ACTION FAILURE (" + message + ") " + describeAction(action) + " - " + ExceptionFormatter.toString(e)));
            this.onActionFinished(action);
        }
    }
    
    private static String describeAction(final Action act) {
        return act.getClass().getSimpleName() + " : " + act.getUniqueId() + " #" + act.hashCode();
    }
    
    @Override
    public void onActionFinished(final Action action) {
        action.removeListener(this);
        this.removeAction(action.getUniqueId());
        this.runNextAction();
    }
    
    @Override
    public void kill() {
        ExecutionForcedActionGroup.m_logger.info((Object)("Kill des actions de la pile (" + this.m_actions.size() + ")"));
        final Collection<Action> actionsToKill = new ArrayList<Action>();
        for (final Action action : this.m_actions) {
            action.removeListener(this);
            actionsToKill.add(action);
        }
        this.m_actions.clear();
        for (final Action action : actionsToKill) {
            if (action instanceof ScriptedAction) {
                final int waitingScriptId = ((ScriptedAction)action).getWaitingEndScript();
                if (waitingScriptId == -1) {
                    continue;
                }
                LuaManager.getInstance().interruptScript(waitingScriptId);
            }
            else {
                if (!(action instanceof TimedAction)) {
                    continue;
                }
                final TimedAction timedAction = (TimedAction)action;
                MessageScheduler.getInstance().removeAllClocks(timedAction);
            }
        }
        this.fireActionGroupFinishedEvent();
    }
    
    private void fireActionGroupFinishedEvent() {
        for (final ActionGroupEventListener listener : this.m_listeners.toArray(new ActionGroupEventListener[this.m_listeners.size()])) {
            listener.onActionGroupFinished(this);
        }
    }
    
    @Override
    public void executeAllAction() {
        while (!this.m_actions.isEmpty()) {
            final Action firstAction = this.m_actions.remove();
            firstAction.removeListener(this);
            this.runActionWithDebugMessage(firstAction, "Executing all actions");
        }
    }
    
    @Override
    public Action getActionsBySpecialId(final int id) {
        for (final Action action : this.m_actions) {
            if (action.getSpecialId() == id) {
                return action;
            }
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ExecutionForcedActionGroup.class);
    }
}
