package com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;
import com.ankamagames.wakfu.common.rawData.*;

public final class BookcaseItemizableInfo extends BasicItemizableInfo<Bookcase>
{
    public BookcaseItemizableInfo(final Bookcase linkedElement) {
        super(linkedElement);
    }
    
    @Override
    public boolean canBeRepacked() {
        return ((Bookcase)this.m_linkedElement).fillSize() == 0;
    }
    
    @Override
    public GemType[] getAllowedInRooms() {
        return new GemType[] { GemType.GEM_ID_DECORATION, GemType.GEM_ID_MERCHANT };
    }
    
    @Override
    public RoomContentType getContentType() {
        return RoomContentType.DECORATION;
    }
    
    public void unserializePersistantData(final AbstractRawPersistantData data) {
        if (data.getVirtualId() == 3) {
            final RawPersistantBookcase bookcaseData = (RawPersistantBookcase)data;
            final ArrayList<RawBookcase.Content> bookRefIds = bookcaseData.content.bookRefIds;
            final int[] booksRefIds = new int[bookRefIds.size()];
            for (int i = 0, n = bookRefIds.size(); i < n; ++i) {
                final RawBookcase.Content bookRefId = bookRefIds.get(i);
                booksRefIds[i] = bookRefId.bookRefId;
            }
            ((Bookcase)this.m_linkedElement).setBooksRefIds(booksRefIds);
            ((Bookcase)this.m_linkedElement).updateBag();
        }
    }
}
