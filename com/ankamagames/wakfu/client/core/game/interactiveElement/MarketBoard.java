package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class MarketBoard extends WakfuClientMapInteractiveElement implements ChaosInteractiveElement
{
    private static final Logger m_logger;
    private int m_marketId;
    private IEMarketBoardParameter m_info;
    private final BinarSerialPart SHARED_DATAS;
    
    public MarketBoard() {
        super();
        this.SHARED_DATAS = new BinarSerialPart(4) {
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                MarketBoard.this.m_marketId = buffer.getInt();
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => pas de s\u00e9rialisation");
            }
        };
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.SHARED_DATAS;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    public int getMarketId() {
        return this.m_marketId;
    }
    
    @Override
    public boolean isBlockingMovements() {
        return false;
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        this.sendActionMessage(action);
        return true;
    }
    
    @Override
    public void initializeWithParameter() {
        super.initializeWithParameter();
        this.m_info = (IEMarketBoardParameter)IEParametersManager.INSTANCE.getParam(IETypes.MARKET_BOARD, Integer.parseInt(this.m_parameter));
    }
    
    @Override
    public byte getHeight() {
        return 8;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.START_BROWSING;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.START_BROWSING, InteractiveElementAction.STOP_BROWSING };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.isInHavenWorld() && !localPlayer.isInOwnHavenWorld()) {
            return AbstractMRUAction.EMPTY_ARRAY;
        }
        final MRUMarketAction mruAction = MRUActions.MARKET_ACTION.getMRUAction();
        mruAction.setVisual(this.m_info.getVisualId());
        return new AbstractMRUAction[] { mruAction };
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.setOverHeadable(false);
        this.setBlockingMovements(false);
        this.m_info = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setOverHeadable(true);
        this.setBlockingMovements(true);
        this.m_marketId = 0;
        assert this.m_info == null;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString(101, this.m_info.getId(), new Object[0]);
    }
    
    public String getMarketName() {
        return WakfuTranslator.getInstance().getString(102, this.m_info.getMarketId(), new Object[0]);
    }
    
    @Override
    public ChaosIEParameter getChaosIEParameter() {
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MarketBoard.class);
    }
    
    public static class MarketBoardFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            MarketBoard board;
            try {
                board = (MarketBoard)MarketBoardFactory.m_pool.borrowObject();
                board.setPool(MarketBoardFactory.m_pool);
            }
            catch (Exception e) {
                MarketBoard.m_logger.error((Object)"Erreur lors de l'extraction d'un MarketBoard du pool", (Throwable)e);
                board = new MarketBoard();
            }
            return board;
        }
        
        static {
            MarketBoardFactory.m_pool = new MonitoredPool(new ObjectFactory<MarketBoard>() {
                @Override
                public MarketBoard makeObject() {
                    return new MarketBoard();
                }
            });
        }
    }
}
