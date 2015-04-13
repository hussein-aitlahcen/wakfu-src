package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class Decoration extends AbstractItemizableInteractiveElement
{
    private static final Logger m_logger;
    public static final short STATE_NORMAL = 1;
    private int m_linkedItemReferenceId;
    protected IEDecorationParameter m_decorationParameters;
    
    @Override
    protected void unserializeSpecificSharedData(final ByteBuffer buffer) {
        this.m_linkedItemReferenceId = buffer.getInt();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setState((short)1);
        this.setVisible(true);
        this.setBlockingMovements(true);
        this.setBlockingLineOfSight(true);
        this.m_linkedItemReferenceId = -1;
        assert this.m_decorationParameters == null;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_decorationParameters = null;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public GemType[] getAllowedInRooms() {
        final GemType[] array;
        if (this.m_decorationParameters == null) {
            array = new GemType[] { GemType.GEM_ID_DECORATION };
        }
        else {
            this.m_decorationParameters.getAllowedGemTypes();
        }
        return array;
    }
    
    @Override
    public void initializeWithParameter() {
        final String[] params = this.m_parameter.split(";");
        if (params.length != 1) {
            Decoration.m_logger.error((Object)("[LD] L'IE de Decoration " + this.m_id + " doit avoir " + 1 + " param\u00e8tre"));
            return;
        }
        final IEDecorationParameter param = (IEDecorationParameter)IEParametersManager.INSTANCE.getParam(IETypes.DECORATION, Integer.valueOf(params[0]));
        if (param == null) {
            Decoration.m_logger.error((Object)("[LD] L'IE de Decoration " + this.m_id + " \u00e0 un parametre [" + Integer.valueOf(params[0]) + "] qui ne correspond a rien dans les Admins"));
            return;
        }
        this.m_decorationParameters = param;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return null;
    }
    
    @Override
    protected InteractiveElementAction[] getAdditionalUsableActions() {
        return InteractiveElementAction.EMPTY_ACTIONS;
    }
    
    @Override
    protected AbstractMRUAction[] getAdditionalMRUActions() {
        return AbstractMRUAction.EMPTY_ARRAY;
    }
    
    @Override
    public boolean isOverHeadable() {
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_linkedItemReferenceId);
        return referenceItem != null;
    }
    
    @Override
    public String getName() {
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_linkedItemReferenceId);
        if (referenceItem != null) {
            return referenceItem.getName();
        }
        return "<UNKNOWN>";
    }
    
    @Override
    protected void unserializePersistantData(final AbstractRawPersistantData specificData) {
    }
    
    @Override
    public RoomContentType getContentType() {
        return RoomContentType.DECORATION;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Decoration.class);
    }
    
    public static class DecorationFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            Decoration element;
            try {
                element = (Decoration)DecorationFactory.m_pool.borrowObject();
                element.setPool(DecorationFactory.m_pool);
            }
            catch (Exception e) {
                Decoration.m_logger.error((Object)("Erreur lors de l'extraction d'un " + Decoration.class.getName() + " du pool"), (Throwable)e);
                element = new Decoration();
            }
            return element;
        }
        
        static {
            DecorationFactory.m_pool = new MonitoredPool(new ObjectFactory<Decoration>() {
                @Override
                public Decoration makeObject() {
                    return new Decoration();
                }
            });
        }
    }
}
