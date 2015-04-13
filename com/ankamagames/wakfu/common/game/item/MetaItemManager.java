package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;

public class MetaItemManager
{
    public static final MetaItemManager INSTANCE;
    private BinaryLoaderFromFile<BinaryData> m_loader;
    private BinaryTransformer<BinaryData, AbstractReferenceItem> m_binaryTransformer;
    protected final TIntObjectHashMap<IMetaItem> m_items;
    
    public MetaItemManager() {
        super();
        this.m_items = new TIntObjectHashMap<IMetaItem>();
    }
    
    public void setBinaryLoader(final BinaryLoaderFromFile loader) {
        this.m_loader = (BinaryLoaderFromFile<BinaryData>)loader;
    }
    
    public <I extends BinaryData> void setBinaryTransformer(final BinaryTransformer<BinaryData, AbstractReferenceItem> binaryTransformer) {
        this.m_binaryTransformer = binaryTransformer;
    }
    
    public void add(final IMetaItem item) {
        this.m_items.put(item.getId(), item);
    }
    
    @Nullable
    public <M extends IMetaItem> M get(final int metaId) {
        if (metaId <= 0) {
            return null;
        }
        final M metaItem = (M)this.m_items.get(metaId);
        if (metaItem != null) {
            return metaItem;
        }
        if (this.m_loader == null) {
            return null;
        }
        final BinaryData data = this.m_loader.createFromId(metaId);
        if (data == null) {
            return null;
        }
        final AbstractReferenceItem referenceItem = this.m_binaryTransformer.loadFromBinaryForm(data);
        if (!(referenceItem instanceof IMetaItem)) {
            return null;
        }
        final M item = (M)referenceItem;
        this.add(item);
        return item;
    }
    
    static {
        INSTANCE = new MetaItemManager();
    }
}
