package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

import java.util.*;
import gnu.trove.*;

public abstract class AbstractExchangerUser<ContentType extends InventoryContent> implements ExchangerUser<ContentType>
{
    protected boolean m_ready;
    protected final TLongObjectHashMap<ContentType> m_exchangeList;
    private int m_moneyExchanged;
    private final List<ExchangerUserListener> m_listeners;
    
    public AbstractExchangerUser() {
        super();
        this.m_exchangeList = new TLongObjectHashMap<ContentType>();
        this.m_listeners = new ArrayList<ExchangerUserListener>();
    }
    
    @Override
    public ContentType getInExchangeList(final long id) {
        return this.m_exchangeList.get(id);
    }
    
    @Override
    public void putInExchangeList(final long uid, final ContentType content) {
        this.m_exchangeList.put(uid, content);
    }
    
    @Override
    public void removeFromExchangeList(final long id) {
        this.m_exchangeList.remove(id);
    }
    
    @Override
    public int exchangeListSize() {
        return this.m_exchangeList.size();
    }
    
    @Override
    public boolean exchangeListIsEmpty() {
        return this.m_exchangeList.isEmpty();
    }
    
    @Override
    public TLongObjectIterator<ContentType> exchangeListIterator() {
        return this.m_exchangeList.iterator();
    }
    
    @Override
    public void clear() {
        this.m_moneyExchanged = 0;
        this.m_exchangeList.forEachValue((TObjectProcedure<ContentType>)ItemReleaseProcedure.INSTANCE);
        this.m_exchangeList.clear();
    }
    
    @Override
    public boolean isReady() {
        return this.m_ready;
    }
    
    @Override
    public void setReady(final boolean ready) {
        this.m_ready = ready;
        this.fireReadyChanged();
    }
    
    public int getMoneyExchanged() {
        return this.m_moneyExchanged;
    }
    
    public void setMoneyExchanged(final int moneyExchanged) {
        this.m_moneyExchanged = moneyExchanged;
    }
    
    private void fireReadyChanged() {
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).onReadyChanged(this);
        }
    }
    
    public boolean addListener(final ExchangerUserListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    public boolean remove(final ExchangerUserListener listener) {
        return this.m_listeners.remove(listener);
    }
}
