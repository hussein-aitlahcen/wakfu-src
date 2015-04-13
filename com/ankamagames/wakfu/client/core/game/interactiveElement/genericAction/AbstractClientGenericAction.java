package com.ankamagames.wakfu.client.core.game.interactiveElement.genericAction;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.parameter.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public abstract class AbstractClientGenericAction
{
    protected static final Logger m_logger;
    private final int m_id;
    private SimpleCriterion m_criterion;
    private ArrayList<ParserObject> m_params;
    private String m_errorMessage;
    
    AbstractClientGenericAction(final int id) {
        super();
        this.m_id = id;
    }
    
    public void execute() {
        this.runAction();
    }
    
    public void setAction(final GenericActivableParameter.Action action) {
        this.setCriterion(action);
        final ArrayList<ParserObject> params = this.setParams(action);
        if (ParametersChecker.checkType(action.getActionConstant(), params)) {
            this.m_params = params;
        }
        else {
            AbstractClientGenericAction.m_logger.error((Object)("L'action " + action.getActionTypeId() + " de l'ie generic id=" + this.getId() + " n'a pas des param\u00e8tres du bon type"));
        }
    }
    
    private ArrayList<ParserObject> setParams(final GenericActivableParameter.Action itemAction) {
        final String[] parameters = itemAction.getActionParams();
        final ArrayList<ParserObject> params = new ArrayList<ParserObject>(parameters.length);
        for (int j = 0, size = parameters.length; j < size; ++j) {
            try {
                final ArrayList<ParserObject> criterions = CriteriaCompiler.compileList(parameters[j]);
                if (criterions != null) {
                    params.addAll(criterions);
                }
                else {
                    params.add(null);
                }
            }
            catch (Exception e) {
                AbstractClientGenericAction.m_logger.error((Object)("Erreur lors de la compilation du crit\u00e8re sur une action de l'ie generic id=" + this.getId() + " actionid = " + itemAction.getActionTypeId() + " params : " + parameters[j]), (Throwable)e);
            }
        }
        return params;
    }
    
    private void setCriterion(final GenericActivableParameter.Action itemAction) {
        SimpleCriterion criterion = null;
        try {
            criterion = CriteriaCompiler.compileBoolean(itemAction.getCriteria());
        }
        catch (Exception e) {
            AbstractClientGenericAction.m_logger.error((Object)("Erreur de compilation du crit\u00e8re sur l'action id=" + itemAction.getActionTypeId() + " de l'ie generic id=" + this.getId()), (Throwable)e);
        }
        this.m_criterion = criterion;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public SimpleCriterion getCriterion() {
        return this.m_criterion;
    }
    
    final ParserObject getParam(final int i) {
        return this.m_params.get(i);
    }
    
    public final int getParamCount() {
        return this.m_params.size();
    }
    
    public ArrayList<ParserObject> getParams() {
        return this.m_params;
    }
    
    public String getErrorMessage() {
        return this.m_errorMessage;
    }
    
    protected void setErrorMessage(final String errorMessage) {
        this.m_errorMessage = errorMessage;
    }
    
    protected abstract void runAction();
    
    public abstract boolean isRunnable(final CharacterInfo p0);
    
    public abstract boolean isEnabled(final CharacterInfo p0);
    
    static {
        m_logger = Logger.getLogger((Class)AbstractClientGenericAction.class);
    }
}
