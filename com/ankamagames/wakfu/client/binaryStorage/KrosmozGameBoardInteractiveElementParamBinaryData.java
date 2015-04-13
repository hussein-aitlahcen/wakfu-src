package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class KrosmozGameBoardInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected byte m_gameId;
    
    public int getId() {
        return this.m_id;
    }
    
    public byte getGameId() {
        return this.m_gameId;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_gameId = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_gameId = buffer.get();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.KROSMOZ_GAME_BOARD_IE_PARAM.getId();
    }
}
