package com.ankamagames.wakfu.common.game.part;

import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;

public class BeaconPart implements Part
{
    public static final int FRONT = 0;
    public static final int RIGHT_SIDE = 1;
    public static final int BACK = 2;
    public static final int LEFT_SIDE = 3;
    private int m_id;
    
    BeaconPart(final int id) {
        super();
        this.m_id = id;
    }
    
    @Override
    public int getPartId() {
        return this.m_id;
    }
}
