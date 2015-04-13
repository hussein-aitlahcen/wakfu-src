package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class WeatherBoard extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private static final String TRANSLATION_KEY = "weatherBoard.title";
    private WeatherHistory m_weatherHistory;
    private static final ReadWeatherBoardOccupation READ_OCCUPATION;
    private final BinarSerialPart SHARED_DATAS;
    
    public WeatherBoard() {
        super();
        this.m_weatherHistory = new WeatherHistory();
        this.SHARED_DATAS = new BinarSerialPart(71) {
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final int size = buffer.getShort() & 0xFFFF;
                if (size > 0) {
                    WeatherBoard.this.m_weatherHistory.fromBuild(buffer);
                }
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
    
    @Override
    public byte getHeight() {
        return 8;
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        this.sendActionMessage(action);
        switch (action) {
            case READ: {
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (localPlayer != null && localPlayer.getCurrentOccupation() != WeatherBoard.READ_OCCUPATION) {
                    WeatherBoard.READ_OCCUPATION.setBoard(this);
                    WeatherBoard.READ_OCCUPATION.begin();
                }
                return true;
            }
            default: {
                return true;
            }
        }
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.READ;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.READ };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final MRUInteractifMachine action = MRUActions.INTERACTIF_ACTION.getMRUAction();
        action.setGfxId(MRUGfxConstants.BUBBLE.m_id);
        return new AbstractMRUAction[] { action };
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("weatherBoard.title");
    }
    
    public void loadUI() {
        final UIWeatherInfoFrame boardFrame = UIWeatherInfoFrame.getInstance();
        if (WakfuGameEntity.getInstance().hasFrame(boardFrame)) {
            WakfuGameEntity.getInstance().removeFrame(boardFrame);
        }
        else {
            WeatherInfoManager.getInstance().updateFromWeatherHistory(this.m_weatherHistory);
            boardFrame.setBoard(this);
            WakfuGameEntity.getInstance().pushFrame(boardFrame);
        }
    }
    
    public void unloadUI() {
        final UIWeatherInfoFrame boardFrame = UIWeatherInfoFrame.getInstance();
        WakfuGameEntity.getInstance().removeFrame(boardFrame);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setOverHeadable(true);
        this.setBlockingMovements(true);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.setOverHeadable(false);
        this.setBlockingMovements(false);
        this.m_weatherHistory.reset();
    }
    
    static {
        m_logger = Logger.getLogger((Class)WeatherBoard.class);
        READ_OCCUPATION = new ReadWeatherBoardOccupation();
    }
    
    public static class WeatherBoardFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            WeatherBoard board;
            try {
                board = (WeatherBoard)WeatherBoardFactory.m_pool.borrowObject();
                board.setPool(WeatherBoardFactory.m_pool);
            }
            catch (Exception e) {
                WeatherBoard.m_logger.error((Object)"Erreur lors de l'extraction d'un WeatherBoard du pool", (Throwable)e);
                board = new WeatherBoard();
            }
            return board;
        }
        
        static {
            WeatherBoardFactory.m_pool = new MonitoredPool(new ObjectFactory<WeatherBoard>() {
                @Override
                public WeatherBoard makeObject() {
                    return new WeatherBoard();
                }
            });
        }
    }
}
