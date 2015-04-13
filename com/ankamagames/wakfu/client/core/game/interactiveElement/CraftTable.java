package com.ankamagames.wakfu.client.core.game.interactiveElement;

import com.ankamagames.wakfu.client.core.game.interactiveElement.util.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.param.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.interactiveElements.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class CraftTable extends WakfuClientMapInteractiveElement implements CraftInteractiveElement, ChaosInteractiveElement
{
    private static final Logger m_logger;
    private static final AbstractMRUAction[] ABSTRACT_MRU_ACTION_EMPTY;
    private ClientIECraftParameter m_info;
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        return true;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.ACTIVATE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        if (this.m_info == null) {
            CraftTable.m_logger.error((Object)("Pas de m_info pour craftTableId=" + this.m_id));
            return CraftTable.ABSTRACT_MRU_ACTION_EMPTY;
        }
        final ActionVisual visual = ActionVisualManager.getInstance().get(this.m_info.getVisualId());
        if (visual == null) {
            CraftTable.m_logger.error((Object)("Pas de visual id=" + this.m_info.getVisualId()));
            return CraftTable.ABSTRACT_MRU_ACTION_EMPTY;
        }
        final MRUCraftAction craftAction = MRUActions.OPEN_CRAFT_ACTION.getMRUAction();
        craftAction.setVisual(visual);
        final boolean challengeIE = this.hasProperty(WakfuInteractiveElementProperty.CHALLENGE_IE);
        if (!challengeIE) {
            final MRUCraftAction craftAction2 = MRUActions.OPEN_CRAFT_FREE_ACTION.getMRUAction();
            craftAction2.setVisual(visual);
            return new AbstractMRUAction[] { craftAction, craftAction2 };
        }
        return new AbstractMRUAction[] { craftAction };
    }
    
    @Override
    public short getMRUHeight() {
        return this.getHeight();
    }
    
    @Override
    public String getName() {
        if (this.m_info == null) {
            CraftTable.m_logger.error((Object)("Pas de param\u00e9trage valide pour craftTableId=" + this.m_id));
            return "#ERROR#";
        }
        return WakfuTranslator.getInstance().getString(59, this.m_info.getId(), new Object[0]);
    }
    
    @Override
    public boolean isRecipeAllowed(final int recipeId, final byte recipeType) {
        return recipeType == -2 || recipeType == -1 || this.m_info.getAllowedRecipes().contains(recipeId);
    }
    
    @Override
    public boolean containsRecipe(final int recipeId) {
        return this.m_info.getAllowedRecipes().contains(recipeId);
    }
    
    @Override
    public int getVisualId() {
        return this.m_info.getVisualId();
    }
    
    @Override
    public int getCraftId() {
        return this.m_info.getCraftId();
    }
    
    @Override
    public void initializeWithParameter() {
        final ClientIECraftParameter parameter = (ClientIECraftParameter)IEParametersManager.INSTANCE.getParam(IETypes.CRAFT_TABLE, Integer.valueOf(this.m_parameter));
        if (parameter == null) {
            CraftTable.m_logger.error((Object)("[LD] La Machine de Craft " + this.m_id + " \u00e0 un param\u00e8tre [" + Integer.valueOf(this.m_parameter) + "] qui ne correspond a rien dans les Admins"));
            return;
        }
        this.m_info = parameter;
        this.setOverHeadable(true);
    }
    
    @Override
    public void onDeSpawn() {
        super.onDeSpawn();
        if (WakfuGameEntity.getInstance().hasFrame(UICraftTableFrame.getInstance())) {
            UICraftTableFrame.getInstance().onDespawn(this);
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_info = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        assert this.m_info == null;
    }
    
    @Override
    public boolean checkSubscription() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer);
    }
    
    @Override
    public ChaosIEParameter getChaosIEParameter() {
        return this.m_info;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CraftTable.class);
        ABSTRACT_MRU_ACTION_EMPTY = new AbstractMRUAction[] { new MRUNoAction() };
    }
    
    public static class CraftTableFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            CraftTable table;
            try {
                table = (CraftTable)CraftTableFactory.m_pool.borrowObject();
                table.setPool(CraftTableFactory.m_pool);
            }
            catch (Exception e) {
                CraftTable.m_logger.error((Object)"Erreur lors de l'extraction d'une CraftTable du pool", (Throwable)e);
                table = new CraftTable(null);
            }
            return table;
        }
        
        static {
            CraftTableFactory.m_pool = new MonitoredPool(new ObjectFactory<CraftTable>() {
                @Override
                public CraftTable makeObject() {
                    return new CraftTable(null);
                }
            });
        }
    }
}
