package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action;

import com.ankamagames.wakfu.common.game.fight.*;
import java.nio.*;

public class DebugFightAccessSquareMessage extends AbstractFightActionMessage
{
    private long m_characterId;
    private int m_nbCells;
    private int[][] m_coordinates;
    
    @Override
    public int getActionId() {
        return 0;
    }
    
    @Override
    public FightActionType getFightActionType() {
        return null;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_characterId = bb.getLong();
        this.m_nbCells = bb.getInt();
        this.m_coordinates = new int[this.m_nbCells][3];
        for (int i = 0; i < this.m_nbCells; ++i) {
            this.m_coordinates[i][0] = bb.getInt();
            this.m_coordinates[i][1] = bb.getInt();
            this.m_coordinates[i][2] = bb.getInt();
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 8040;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public int getNbCells() {
        return this.m_nbCells;
    }
    
    public int[][] getCoordinates() {
        return this.m_coordinates;
    }
}
