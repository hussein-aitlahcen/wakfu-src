package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.core.game.events.actions.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.client.core.*;

public class ClientGameEventLoader implements ContentInitializer
{
    protected static Logger m_logger;
    public static final ClientGameEventLoader INSTANCE;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        this.loadFromStorage();
        clientInstance.fireContentInitializerDone(this);
    }
    
    private boolean loadFromStorage() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new ClientEventBinaryData(), new LoadProcedure<ClientEventBinaryData>() {
            @Override
            public void load(final ClientEventBinaryData eventBs) {
                final int eventListenerId = eventBs.getId();
                final int listenedEventId = eventBs.getType();
                final String[] bsListenedEventFilters = eventBs.getFilters();
                final String listenedEventCriterionAsString = eventBs.getCriterion();
                final short dropRate = eventBs.getDropRate();
                final int maxCount = eventBs.getMaxCount();
                final boolean activeOnStart = eventBs.isActiveOnStart();
                final String[] listenedEventFilters = (bsListenedEventFilters != null) ? new String[bsListenedEventFilters.length] : new String[0];
                for (int i = 0; i < listenedEventFilters.length; ++i) {
                    if (bsListenedEventFilters[i] != null && bsListenedEventFilters[i].equals("DEFAULT")) {
                        listenedEventFilters[i] = null;
                    }
                    else {
                        listenedEventFilters[i] = bsListenedEventFilters[i];
                    }
                }
                SimpleCriterion listenedEventCriterion;
                try {
                    listenedEventCriterion = CriteriaCompiler.compileBoolean(listenedEventCriterionAsString);
                }
                catch (Exception e) {
                    listenedEventCriterion = null;
                    ClientGameEventLoader.m_logger.error((Object)("Crit\u00e8re " + listenedEventCriterionAsString), (Throwable)e);
                }
                final ClientGameEventListener eventListener = new ClientGameEventListener(eventListenerId, listenedEventId, listenedEventFilters, listenedEventCriterion, dropRate, maxCount, activeOnStart);
                ClientGameEventManager.INSTANCE.registerEventListener(eventListener);
                for (final ClientEventBinaryData.EventActionGroup group : eventBs.getActionGroups()) {
                    ClientGameEventLoader.this.loadActionGroup(eventListener, group);
                }
            }
        });
        ClientGameEventManager.INSTANCE.initialize();
        return true;
    }
    
    private void loadActionGroup(final ClientGameEventListener eventListener, final ClientEventBinaryData.EventActionGroup group) {
        final int actionListId = group.getId();
        final short actionListDropRate = group.getDropRate();
        final String actionListDropCriterionAsString = group.getCriterion();
        SimpleCriterion actionListDropCriterion;
        try {
            actionListDropCriterion = CriteriaCompiler.compileBoolean(actionListDropCriterionAsString);
        }
        catch (Exception e) {
            ClientGameEventLoader.m_logger.error((Object)("Exception \u00e0 la compilation du crit\u00e8re " + actionListDropCriterionAsString), (Throwable)e);
            actionListDropCriterion = null;
        }
        final ClientEventActionList actionList = new ClientEventActionList(actionListId, actionListDropRate, actionListDropCriterion);
        eventListener.addActionList(actionList);
        for (final ClientEventBinaryData.EventAction bsaction : group.getActions()) {
            this.loadAction(actionList, bsaction);
        }
    }
    
    private void loadAction(final ClientEventActionList actionList, final ClientEventBinaryData.EventAction bsaction) {
        final int actionId = bsaction.getId();
        final int actionTypeId = bsaction.getType();
        final ClientEventAction action = ClientEventActionType.createFromType(actionTypeId, actionId);
        final ArrayList<ParserObject> params = new ArrayList<ParserObject>();
        final String[] actionParametersAsString = bsaction.getParams();
        final ParameterListSet parameterListSet = action.getParametersListSet();
        final ParameterList parameterList = parameterListSet.getParameterList(actionParametersAsString.length);
        if (parameterList != null) {
            boolean parametersLoaded = true;
            try {
                for (int i = 0; i < actionParametersAsString.length; ++i) {
                    final String parameterAsString = actionParametersAsString[i];
                    final ParserObjectParameter parameter = (ParserObjectParameter)parameterList.getParameter(i);
                    final ParserObject parserObject = parameter.getType().getStringParser().fromString(parameterAsString);
                    params.add(parserObject);
                }
            }
            catch (Exception e) {
                ClientGameEventLoader.m_logger.error((Object)("Exception pendant le chargement des param\u00e8tres de l'action id=" + action.getId() + " : "), (Throwable)e);
                parametersLoaded = false;
            }
            try {
                if (parametersLoaded) {
                    action.setParams(params);
                    actionList.addAction(action);
                }
                else {
                    ClientGameEventLoader.m_logger.error((Object)("impossible de charger l'action id=" + action.getId() + " : \u00e9chec du chargement des param\u00e8tres"));
                }
            }
            catch (Exception e) {
                ClientGameEventLoader.m_logger.error((Object)("Exception pendant l'application des param\u00e8tres du comportement id=" + action.getId() + " : "), (Throwable)e);
            }
        }
        else {
            ClientGameEventLoader.m_logger.error((Object)("L'action client id=" + action.getId() + " n'a pas un param\u00e8trage correct (pas de template d\u00e9fini avec " + actionParametersAsString.length + " param\u00e8tres)"));
        }
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.scriptAction");
    }
    
    static {
        ClientGameEventLoader.m_logger = Logger.getLogger((Class)ClientGameEventLoader.class);
        INSTANCE = new ClientGameEventLoader();
    }
}
