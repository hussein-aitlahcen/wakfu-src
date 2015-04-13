package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;

public class ReadWeatherBoardOccupation extends AbstractOccupation
{
    private static final Logger m_logger;
    private WeatherBoard m_board;
    
    public void setBoard(final WeatherBoard board) {
        this.m_board = board;
        if (this.m_localPlayer == null) {
            this.m_localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        }
    }
    
    @Override
    public short getOccupationTypeId() {
        return 15;
    }
    
    @Override
    public boolean isAllowed() {
        return true;
    }
    
    @Override
    public void begin() {
        ReadWeatherBoardOccupation.m_logger.info((Object)"Lancement de l'occupation READ_BOARD");
        this.m_localPlayer.cancelCurrentOccupation(false, true);
        this.m_board.loadUI();
        this.m_localPlayer.setCurrentOccupation(this);
    }
    
    @Override
    public boolean cancel(final boolean fromServer, final boolean sendMessage) {
        return this.finish();
    }
    
    @Override
    public boolean finish() {
        this.m_board.unloadUI();
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ReadWeatherBoardOccupation.class);
    }
}
