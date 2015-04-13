package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import gnu.trove.*;

public interface ExchangerUser<ContentType extends InventoryContent>
{
    long getId();
    
    String getName();
    
    void onItemExchangerEvent(ItemExchangerEvent p0);
    
    void putInExchangeList(long p0, ContentType p1);
    
    ContentType getInExchangeList(long p0);
    
    TLongObjectIterator<ContentType> exchangeListIterator();
    
    boolean isReady();
    
    void setReady(boolean p0);
    
    void removeFromExchangeList(long p0);
    
    void clear();
    
    int exchangeListSize();
    
    boolean exchangeListIsEmpty();
}
