package com.ankamagames.wakfu.client.core.game.interactiveElement;

import com.ankamagames.wakfu.client.core.game.interactiveElement.util.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.param.*;
import java.nio.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class PortableCraftTable extends AbstractItemizableInteractiveElement implements CraftInteractiveElement
{
    private static final Logger m_logger;
    public static final short STATE_NORMAL = 1;
    private ClientIECraftParameter m_info;
    private static final AbstractMRUAction[] ABSTRACT_MRU_ACTION_EMPTY;
    
    @Override
    protected void unserializeSpecificSharedData(final ByteBuffer buffer) {
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_info = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setState((short)1);
        this.setVisible(true);
        this.setBlockingLineOfSight(false);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
        this.setSelectable(true);
        assert this.m_info == null;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public GemType[] getAllowedInRooms() {
        return new GemType[] { GemType.GEM_ID_CRAFT };
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (!super.onAction(action, user)) {
            this.runScript(action);
        }
        return true;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.ACTIVATE;
    }
    
    @Override
    protected InteractiveElementAction[] getAdditionalUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    @Override
    protected AbstractMRUAction[] getAdditionalMRUActions() {
        if (this.m_info == null) {
            PortableCraftTable.m_logger.error((Object)("Pas de m_info pour craftTableId=" + this.m_id));
            return PortableCraftTable.ABSTRACT_MRU_ACTION_EMPTY;
        }
        final ActionVisual visual = ActionVisualManager.getInstance().get(this.m_info.getVisualId());
        if (visual == null) {
            PortableCraftTable.m_logger.error((Object)("Pas de visual id=" + this.m_info.getVisualId()));
            return PortableCraftTable.ABSTRACT_MRU_ACTION_EMPTY;
        }
        final MRUCraftAction craftAction = MRUActions.OPEN_CRAFT_ACTION.getMRUAction();
        craftAction.setVisual(visual);
        return new AbstractMRUAction[] { craftAction };
    }
    
    @Override
    public short getMRUHeight() {
        return 60;
    }
    
    @Override
    public String getName() {
        if (this.m_info == null) {
            PortableCraftTable.m_logger.error((Object)("Pas de param\u00e9trage valide pour craftTableId=" + this.m_id));
            return "#ERROR#";
        }
        return WakfuTranslator.getInstance().getString(59, this.m_info.getId(), new Object[0]);
    }
    
    @Override
    public void initializeWithParameter() {
        super.initializeWithParameter();
        final ClientIECraftParameter parameter = (ClientIECraftParameter)IEParametersManager.INSTANCE.getParam(IETypes.CRAFT_TABLE, Integer.valueOf(this.m_parameter));
        if (parameter == null) {
            PortableCraftTable.m_logger.error((Object)("[LD] La Machine de Craft " + this.m_id + " \u00e0 un param\u00e8tre [" + Integer.valueOf(this.m_parameter) + "] qui ne correspond a rien dans les Admins"));
            return;
        }
        this.m_info = parameter;
        this.setOverHeadable(true);
    }
    
    @Override
    public int getCraftId() {
        return this.m_info.getCraftId();
    }
    
    @Override
    public boolean containsRecipe(final int recipeId) {
        return this.m_info.getAllowedRecipes().contains(recipeId);
    }
    
    @Override
    protected void unserializePersistantData(final AbstractRawPersistantData specificData) {
    }
    
    @Override
    public boolean isRecipeAllowed(final int recipeId, final byte recipeType) {
        return this.m_info != null && this.m_info.getAllowedRecipes().contains(recipeId);
    }
    
    @Override
    public int getVisualId() {
        return (this.m_info != null) ? this.m_info.getVisualId() : 0;
    }
    
    @Override
    public RoomContentType getContentType() {
        return RoomContentType.CRAFT_TABLE;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PortableCraftTable.class);
        ABSTRACT_MRU_ACTION_EMPTY = AbstractMRUAction.EMPTY_ARRAY;
    }
    
    public static class PortableCraftTableFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            PortableCraftTable element;
            try {
                element = (PortableCraftTable)PortableCraftTableFactory.m_pool.borrowObject();
                element.setPool(PortableCraftTableFactory.m_pool);
            }
            catch (Exception e) {
                PortableCraftTable.m_logger.error((Object)("Erreur lors de l'extraction d'un " + PortableCraftTable.class.getName() + " du pool"), (Throwable)e);
                element = new PortableCraftTable();
            }
            return element;
        }
        
        static {
            PortableCraftTableFactory.m_pool = new MonitoredPool(new ObjectFactory<PortableCraftTable>() {
                @Override
                public PortableCraftTable makeObject() {
                    return new PortableCraftTable();
                }
            });
        }
    }
}
