package com.ankamagames.baseImpl.common.clientAndServer.game.part.basicImpl;

import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;

public class CharacterPart implements Part, Comparable<CharacterPart>
{
    public static final int FRONT = 0;
    public static final int RIGHT_SIDE = 1;
    public static final int BACK = 2;
    public static final int LEFT_SIDE = 3;
    private int m_id;
    
    CharacterPart(final int id) {
        super();
        this.m_id = id;
    }
    
    @Override
    public int getPartId() {
        return this.m_id;
    }
    
    @Override
    public int compareTo(final CharacterPart characterPart) {
        if (characterPart.m_id == this.m_id) {
            return 0;
        }
        if (this.m_id == 2) {
            return -1;
        }
        if ((this.m_id == 1 || this.m_id == 3) && characterPart.m_id != 2) {
            return -1;
        }
        return 1;
    }
}
