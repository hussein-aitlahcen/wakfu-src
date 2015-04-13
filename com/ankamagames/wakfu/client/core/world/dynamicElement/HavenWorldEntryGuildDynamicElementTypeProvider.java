package com.ankamagames.wakfu.client.core.world.dynamicElement;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.game.DynamicElement.*;
import com.ankamagames.framework.java.util.*;

public class HavenWorldEntryGuildDynamicElementTypeProvider extends GuildDynamicElementTypeProvider
{
    private int m_attachedHavenWorldBoardId;
    private HavenWorldBoard m_attachedHavenWorldBoard;
    
    public void setAttachedHavenWorldBoard(final HavenWorldBoard attachedHavenWorldBoard) {
        this.m_attachedHavenWorldBoard = attachedHavenWorldBoard;
    }
    
    public int getAttachedHavenWorldBoardId() {
        return this.m_attachedHavenWorldBoardId;
    }
    
    @Override
    public void initialize(final DynamicElement elt) {
        this.m_attachedHavenWorldBoardId = PrimitiveConverter.getInteger(elt.getParams());
        super.initialize(elt);
    }
    
    @Override
    protected long getGuildBlazon() {
        final HavenWorldBoard havenWorldBoard = this.m_attachedHavenWorldBoard;
        if (havenWorldBoard == null) {
            return 0L;
        }
        if (havenWorldBoard.getGuildInfo() == null) {
            return 0L;
        }
        return havenWorldBoard.getGuildInfo().getBlazon();
    }
}
