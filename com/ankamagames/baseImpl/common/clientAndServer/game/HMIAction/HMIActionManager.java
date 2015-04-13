package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import com.ankamagames.framework.kernel.utils.*;

public class HMIActionManager
{
    protected static final Logger m_logger;
    private static final HMIActionManager m_instance;
    private GrowingArray<HMIAction> m_actions;
    
    public static HMIActionManager getInstance() {
        return HMIActionManager.m_instance;
    }
    
    private HMIActionManager() {
        super();
        this.m_actions = new GrowingArray<HMIAction>();
    }
    
    public static HMIAction createNewAction(final HMIActionType actionType, final String parameters, final boolean isTargetOnly) {
        try {
            final HMIAction action = (HMIAction)actionType.getRepresentationClass().newInstance();
            action.setTargetOnly(isTargetOnly);
            if (action.initialize(parameters)) {
                return action;
            }
            HMIActionManager.m_logger.error((Object)("Impossible d'initialiser l'HMIAction de type " + actionType + " parametres=" + parameters));
            return null;
        }
        catch (InstantiationException e) {
            HMIActionManager.m_logger.error((Object)("Erreur d'instanciation d'un actionType : " + actionType.getLabel()));
            return null;
        }
        catch (IllegalAccessException e2) {
            HMIActionManager.m_logger.error((Object)("Acc\u00e8s non authoris\u00e9 pour l'instanciation d'un actionType : " + actionType.getLabel()));
            return null;
        }
    }
    
    public HMIAction registerNewAction(final byte typeId, final String parameters, final boolean isTargetOnly) {
        return this.registerNewAction(HMIActionType.getFromId(typeId), parameters, isTargetOnly);
    }
    
    private HMIAction registerNewAction(final HMIActionType actionType, final String parameters, final boolean targetOnly) {
        final HMIAction action = createNewAction(actionType, parameters, targetOnly);
        if (action != null) {
            this.m_actions.set(action.getId(), action);
        }
        return action;
    }
    
    public static ArrayList<HMIAction> fromString(final String hmiActions) {
        final ArrayList<HMIAction> actions = new ArrayList<HMIAction>();
        if (hmiActions.length() == 0) {
            return actions;
        }
        for (final String hmiAction : StringUtils.split(hmiActions, '~')) {
            final HMIAction hmi = HMIAction.fromString(hmiAction);
            if (hmi != null) {
                actions.add(hmi);
            }
        }
        return actions;
    }
    
    public HMIAction getAction(final int actionId) {
        return this.m_actions.get(actionId);
    }
    
    public int actionCount() {
        return this.m_actions.size();
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIActionManager.class);
        m_instance = new HMIActionManager();
    }
}
