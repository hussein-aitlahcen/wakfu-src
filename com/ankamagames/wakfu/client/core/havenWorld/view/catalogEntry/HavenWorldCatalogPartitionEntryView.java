package com.ankamagames.wakfu.client.core.havenWorld.view.catalogEntry;

import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;

public class HavenWorldCatalogPartitionEntryView extends HavenWorldCatalogEntryView<PartitionCatalogEntry>
{
    @Override
    public String[] getFields() {
        return HavenWorldCatalogPartitionEntryView.COMMON_FIELDS;
    }
    
    public HavenWorldCatalogPartitionEntryView(final PartitionCatalogEntry partitionCatalogEntry) {
        super(partitionCatalogEntry);
    }
    
    @Override
    public HavenWorldCatalogView.CatalogCategory getCategory() {
        return HavenWorldCatalogView.CatalogCategory.PARTITION;
    }
    
    @Override
    public boolean hasEnoughKamas() {
        return true;
    }
    
    @Override
    public int getCurrentQuantity() {
        return 0;
    }
    
    @Override
    public void setAvailable(final boolean available) {
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
    
    @Override
    public HavenWorldCatalogEntryView getCopy() {
        final HavenWorldCatalogPartitionEntryView havenWorldCatalogPartitionEntryView = new HavenWorldCatalogPartitionEntryView((PartitionCatalogEntry)this.m_catalogEntry);
        return havenWorldCatalogPartitionEntryView;
    }
}
