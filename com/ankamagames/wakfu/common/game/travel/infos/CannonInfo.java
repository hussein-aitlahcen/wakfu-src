package com.ankamagames.wakfu.common.game.travel.infos;

import com.ankamagames.baseImpl.common.clientAndServer.game.loot.*;
import com.ankamagames.wakfu.common.game.travel.*;

public class CannonInfo extends TravelInfo
{
    private final int m_itemId;
    private final int m_itemQty;
    private final DropTable<CannonLink> m_links;
    
    public CannonInfo(final long id, final int visualId, final int itemId, final int itemQty) {
        super(id, visualId);
        this.m_links = new DropTable<CannonLink>();
        this.m_itemId = itemId;
        this.m_itemQty = itemQty;
    }
    
    public CannonInfo(final long id, final int visualId, final int itemId, final int itemQty, final int uiGfxId, final TravelType landmarkTravelType) {
        super(id, visualId, uiGfxId, landmarkTravelType);
        this.m_links = new DropTable<CannonLink>();
        this.m_itemId = itemId;
        this.m_itemQty = itemQty;
    }
    
    public int getItemId() {
        return this.m_itemId;
    }
    
    public int getItemQty() {
        return this.m_itemQty;
    }
    
    public CannonLink dropLink(final Object dropUser, final Object dropTarget, final Object dropContent, final Object dropContext) {
        return this.m_links.drop(dropUser, dropTarget, dropContent, dropContext);
    }
    
    public CannonLink addLink(final CannonLink link) {
        this.m_links.addDrop(link);
        return link;
    }
    
    public CannonLink getDrop(final int dropId) {
        return this.m_links.getDrop(dropId);
    }
}
