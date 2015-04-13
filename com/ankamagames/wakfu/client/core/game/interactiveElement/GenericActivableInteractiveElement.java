package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.script.*;
import java.util.regex.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.genericAction.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.common.game.gameActions.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class GenericActivableInteractiveElement extends WakfuClientMapInteractiveElement implements OccupationInteractiveElement
{
    private static final Logger m_logger;
    private static final TObjectProcedure<Object> DELETE_PROCEDURE;
    private GenericActivableParameter m_param;
    private static final String REGEX_MRU_PARAM_ONE = "\\{[^\\{\\}]*\\}";
    public static final String REGEX_MRU_PARAM_ALL = "(\\{[^\\{\\}]*\\})+";
    private static final String REGEX_GLOBAL = "(\\{[^\\{\\}]*\\})+(;[a-zA-Z0-9\\.]*)?";
    private static final Pattern PATTERN_MRU_PARAM_ONE;
    public static final Pattern PATTERN_MRU_PARAM_ALL;
    private String m_translatorKey;
    private final List<MRUParameters> m_mruParameters;
    private ArrayList<LuaScript> m_scripts;
    
    private void onCheckOutOld() {
        assert this.m_translatorKey == null;
        this.m_translatorKey = "";
        assert this.m_mruParameters.isEmpty();
    }
    
    private void onCheckInOld() {
        this.m_translatorKey = null;
        this.m_mruParameters.clear();
    }
    
    private String getNameOld() {
        return WakfuTranslator.getInstance().getString(this.m_translatorKey);
    }
    
    private AbstractMRUAction[] getInteractiveMRUActionsOld() {
        final int nbAction = this.m_mruParameters.size();
        final AbstractMRUAction[] mruActions = new AbstractMRUAction[nbAction];
        for (int i = 0; i < nbAction; ++i) {
            final MRUParameters mruParam = this.m_mruParameters.get(i);
            final MRUGenericInteractiveAction MruAction = MRUActions.GENERIC_INTERACTIVE_ACTION.getMRUAction();
            MruAction.setGfxId(mruParam.m_gfxId);
            MruAction.setName(mruParam.m_mruKey);
            MruAction.setActionToExecute(InteractiveElementAction.GENERIC_ACTIONS[i]);
            mruActions[i] = MruAction;
        }
        return mruActions;
    }
    
    private void newParameterLoading() {
        if (!this.m_parameter.matches("(\\{[^\\{\\}]*\\})+(;[a-zA-Z0-9\\.]*)?")) {
            GenericActivableInteractiveElement.m_logger.error((Object)("[LD] Erreur de param\u00e8tres pour l'IE : " + this.m_id + " - Param\u00e8tres : " + this.m_parameter));
            this.m_translatorKey = "";
            this.setOverHeadable(false);
            return;
        }
        final Matcher actionsMatcher = GenericActivableInteractiveElement.PATTERN_MRU_PARAM_ALL.matcher(this.m_parameter);
        if (!actionsMatcher.find() || actionsMatcher.groupCount() != 1) {
            GenericActivableInteractiveElement.m_logger.error((Object)("[LD] Erreur de param\u00e8tres pour l'IE : " + this.m_id + " - Param\u00e8tres : " + this.m_parameter));
            this.m_translatorKey = "";
            this.setOverHeadable(false);
            return;
        }
        final String actionParameter = actionsMatcher.group();
        if (actionsMatcher.hitEnd()) {
            this.m_translatorKey = "";
            this.setOverHeadable(false);
        }
        else {
            this.m_translatorKey = this.m_parameter.substring(actionsMatcher.end() + 1);
        }
        final Matcher mruMatcher = GenericActivableInteractiveElement.PATTERN_MRU_PARAM_ONE.matcher(actionParameter);
        while (mruMatcher.find()) {
            final String stringParam = mruMatcher.group();
            final String mruParam = mruMatcher.group().substring(1, stringParam.length() - 1);
            final String[] params = mruParam.split(";");
            this.m_mruParameters.add(new MRUParameters(Integer.valueOf(params[0]), params[1]));
        }
    }
    
    private void oldParameterLoading() {
        final String[] params = this.m_parameter.split(";");
        if (params.length != 3 && params.length != 2) {
            GenericActivableInteractiveElement.m_logger.error((Object)("[LevelDesign] Un GenericActivableInteractiveElement doit avoir 2 param\u00e8tres - typeId=" + this.getModelId() + " / id=" + this.getId()));
            this.m_translatorKey = "";
            this.setOverHeadable(false);
            return;
        }
        this.m_mruParameters.add(new MRUParameters(Integer.valueOf(params[0]), params[1]));
        if (params.length == 3) {
            this.m_translatorKey = params[2];
        }
        else {
            this.m_translatorKey = "";
            this.setOverHeadable(false);
        }
    }
    
    @Override
    public ActionVisual getVisual() {
        return ActionVisualManager.getInstance().get(this.m_param.getVisualId());
    }
    
    @Override
    public short getUsedState() {
        return 0;
    }
    
    protected GenericActivableInteractiveElement() {
        super();
        this.m_mruParameters = new ArrayList<MRUParameters>();
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_state = 1;
        this.setVisible(true);
        assert this.m_param == null;
        assert this.m_scripts == null;
        this.onCheckOutOld();
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_param = null;
        this.stopScripts();
        this.onCheckInOld();
    }
    
    @Override
    public boolean isUsable() {
        if (!super.isUsable()) {
            return false;
        }
        final AbstractMRUAction[] mruActions = this.getMRUActions();
        if (mruActions == null || mruActions.length <= 0) {
            return false;
        }
        for (int i = 0, length = mruActions.length; i < length; ++i) {
            final AbstractMRUAction mruAction = mruActions[i];
            if (mruAction != null) {
                mruAction.initFromSource(this);
                if (mruAction.isUsable() && mruAction.isRunnable()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public String getName() {
        if (this.m_param == null) {
            return this.getNameOld();
        }
        return WakfuTranslator.getInstance().getString(106, this.m_param.getId(), new Object[0]);
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (!this.isVisible()) {
            return false;
        }
        this.runScript(action);
        this.sendActionMessage(action);
        return true;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        if (this.m_param != null) {
            return this.m_param.getInteractiveDefaultAction();
        }
        return InteractiveElementAction.GENERIC_ACTIONS[0];
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        if (this.m_param != null) {
            return this.m_param.getInteractiveUsableActions();
        }
        return InteractiveElementAction.GENERIC_ACTIONS;
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        if (this.m_param == null) {
            return this.getInteractiveMRUActionsOld();
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final GenericActivableParameter.Visual[] visuals = this.m_param.getVisuals();
        final ArrayList<AbstractMRUAction> mRUActions = new ArrayList<AbstractMRUAction>();
        for (int i = 0; i < visuals.length; ++i) {
            final GenericActivableParameter.Visual visualParam = visuals[i];
            if (!visualParam.executeOnSpawn()) {
                final ActionVisual visual = ActionVisualManager.getInstance().get(visualParam.getVisualId());
                if (visualParam.getGroupActionCount() == 0) {
                    GenericActivableInteractiveElement.m_logger.error((Object)("Le visual " + i + " du generic param " + this.m_param.getId() + " n'a pas de group d'action"));
                }
                else {
                    final TextWidgetFormater sb = new TextWidgetFormater();
                    final short groupActionIndex = visualParam.getRandomGroupActionIndex();
                    final ArrayList<AbstractClientGenericAction> actions = new ArrayList<AbstractClientGenericAction>();
                    GenericActivableParameter.GroupAction groupAction = null;
                    for (final GenericActivableParameter.GroupAction groupActionCandidate : visualParam.getGroupActions()) {
                        if (groupActionCandidate.getGroupIndex() == groupActionIndex) {
                            groupAction = groupActionCandidate;
                            break;
                        }
                    }
                    if (groupAction != null) {
                        for (final GenericActivableParameter.Action action : groupAction.getActions()) {
                            final AbstractClientGenericAction genericAction = GenericActionFactory.newAction(action);
                            if (genericAction.getCriterion() == null || genericAction.getCriterion().isValid(localPlayer, null, null, localPlayer.getAppropriateContext())) {
                                final String errorMessage = genericAction.getErrorMessage();
                                if (errorMessage != null) {
                                    if (sb.length() != 0) {
                                        sb.newLine();
                                    }
                                    sb.append(errorMessage);
                                }
                                final boolean runnable = genericAction.isRunnable(localPlayer);
                                final boolean enabled = genericAction.isEnabled(localPlayer);
                                if (runnable && enabled) {
                                    actions.add(genericAction);
                                }
                            }
                        }
                        if (groupAction.getActions().length == 0 || !actions.isEmpty()) {
                            final MRUGenericActivableAction mruAction = MRUActions.GENERIC_ACTIVABLE_ACTION.getMRUAction();
                            mruAction.setGfxId(visual.getMruGfx());
                            mruAction.setTextKey("desc.mru." + visual.getMruLabelKey());
                            mruAction.setCostText(LootChest.getCostText(visualParam.getItemConsumed(), visualParam.getQuantity(), visualParam.getKamaCost()));
                            mruAction.setCanPay(LootChest.canPay(visualParam.getItemConsumed(), visualParam.getQuantity(), visualParam.getKamaCost()));
                            final boolean isEnabled = true;
                            mruAction.setEnabled(true);
                            mruAction.setErrorMsg(sb.finishAndToString());
                            mruAction.setActionIndex(groupActionIndex);
                            mruAction.setActions(actions);
                            mRUActions.add(mruAction);
                        }
                    }
                }
            }
        }
        return mRUActions.toArray(new AbstractMRUAction[mRUActions.size()]);
    }
    
    @Override
    public void initializeWithParameter() {
        super.initializeWithParameter();
        try {
            final int paramId = Integer.parseInt(this.m_parameter);
            final GenericActivableParameter param = (GenericActivableParameter)IEParametersManager.INSTANCE.getParam(IETypes.GENERIC_ACTIVABLE, paramId);
            if (param == null) {
                GenericActivableInteractiveElement.m_logger.error((Object)("[LD] L'IE de g\u00e9n\u00e9ric " + this.m_id + " \u00e0 un parametre [" + paramId + "] qui ne correspond a rien dans les Admins"));
                return;
            }
            this.m_param = param;
        }
        catch (NumberFormatException e) {
            GenericActivableInteractiveElement.m_logger.warn((Object)("ancien param\u00e9trage pour l'IE g\u00e9n\u00e9ric " + this.getId()));
            if (!this.m_parameter.isEmpty() && this.m_parameter.charAt(0) == '{') {
                this.newParameterLoading();
            }
            else {
                this.oldParameterLoading();
            }
        }
    }
    
    @Override
    public boolean checkSubscription() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer);
    }
    
    @Override
    public void onSpawn() {
        super.onSpawn();
        if (this.m_param == null) {
            return;
        }
        final Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("elementId", this.getId());
        for (final GenericActivableParameter.Visual visual : this.m_param.getVisuals()) {
            if (visual.executeOnSpawn()) {
                final GenericActivableParameter.GroupAction groupAction = visual.getRandomGroupAction();
                for (final GenericActivableParameter.Action action : groupAction.getActions()) {
                    if (action.getActionConstant() == GenericActionConstants.PLAY_SCRIPT) {
                        final LuaScript luaScript = NetScriptedEventsFrame.runScript(PlayScriptSource.GENERIC_IE, action.getActionId(), variables);
                        if (this.m_scripts == null) {
                            this.m_scripts = new ArrayList<LuaScript>();
                        }
                        this.m_scripts.add(luaScript);
                        luaScript.tryToMakeIdle();
                    }
                }
            }
        }
    }
    
    @Override
    public void onDeSpawn() {
        super.onDeSpawn();
        this.stopScripts();
    }
    
    private void stopScripts() {
        if (this.m_scripts == null) {
            return;
        }
        for (final LuaScript script : this.m_scripts) {
            script.foreachCreatedObject(GenericActivableInteractiveElement.DELETE_PROCEDURE);
            script.cancelWaitingTasks();
            script.interrupt();
        }
        this.m_scripts = null;
    }
    
    @Override
    public ItemizableInfo getOrCreateItemizableInfo() {
        if (this.m_itemizableInfo == null) {
            this.m_itemizableInfo = new GenericActivableItemizableInfo(this);
        }
        return this.m_itemizableInfo;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GenericActivableInteractiveElement.class);
        DELETE_PROCEDURE = new TObjectProcedure<Object>() {
            @Override
            public boolean execute(final Object object) {
                if (object instanceof Mobile) {
                    MobileManager.getInstance().removeMobile((Mobile)object);
                }
                return true;
            }
        };
        PATTERN_MRU_PARAM_ONE = Pattern.compile("\\{[^\\{\\}]*\\}");
        PATTERN_MRU_PARAM_ALL = Pattern.compile("(\\{[^\\{\\}]*\\})+");
    }
    
    private static class MRUParameters
    {
        private final int m_gfxId;
        private final String m_mruKey;
        
        MRUParameters(final int gfxId, final String mruKey) {
            super();
            this.m_gfxId = gfxId;
            this.m_mruKey = mruKey;
        }
    }
    
    public static class GenericActivableInteractiveElementFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static final MonitoredPool POOL;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            GenericActivableInteractiveElement dummy;
            try {
                dummy = (GenericActivableInteractiveElement)GenericActivableInteractiveElementFactory.POOL.borrowObject();
                dummy.setPool(GenericActivableInteractiveElementFactory.POOL);
            }
            catch (Exception e) {
                GenericActivableInteractiveElement.m_logger.error((Object)"Erreur lors de l'extraction d'un Lever du pool", (Throwable)e);
                dummy = new GenericActivableInteractiveElement();
            }
            return dummy;
        }
        
        static {
            POOL = new MonitoredPool(new ObjectFactory<GenericActivableInteractiveElement>() {
                @Override
                public GenericActivableInteractiveElement makeObject() {
                    return new GenericActivableInteractiveElement();
                }
            });
        }
    }
}
