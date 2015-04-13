package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.framework.kernel.core.common.*;
import org.jetbrains.annotations.*;

public final class ItemComposer
{
    private boolean m_pooledByDefault;
    private LongUIDGenerator m_uidGenerator;
    @Nullable
    private ItemDisplayer m_fieldProvider;
    @Nullable
    private ItemQuantityChangeListener m_quantityChangeListener;
    
    public void init(final LongUIDGenerator uIDGenerator, final boolean pooledByDefault, @Nullable final ItemDisplayer itemFieldProvider, @Nullable final ItemQuantityChangeListener itemQuantityChangeListener) {
        assert this.m_uidGenerator == null : "ItemComposer already initialized. Can't be initialized twice.";
        this.m_uidGenerator = uIDGenerator;
        this.m_pooledByDefault = pooledByDefault;
        this.m_fieldProvider = itemFieldProvider;
        this.m_quantityChangeListener = itemQuantityChangeListener;
    }
    
    public LongUIDGenerator getUidGenerator() {
        return this.m_uidGenerator;
    }
    
    public boolean isPooledByDefault() {
        return this.m_pooledByDefault;
    }
    
    @Nullable
    public ItemDisplayer getFieldProvider() {
        return this.m_fieldProvider;
    }
    
    @Nullable
    public ItemQuantityChangeListener getQuantityChangeListener() {
        return this.m_quantityChangeListener;
    }
    
    @Override
    public String toString() {
        return "ItemComposer{m_pooledByDefault=" + this.m_pooledByDefault + ", m_uidGenerator=" + this.m_uidGenerator + ", m_fieldProvider=" + this.m_fieldProvider + ", m_quantityChangeListener=" + this.m_quantityChangeListener + '}';
    }
}
