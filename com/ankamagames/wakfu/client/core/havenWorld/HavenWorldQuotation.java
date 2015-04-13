package com.ankamagames.wakfu.client.core.havenWorld;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.catalogEntry.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif.*;
import org.jetbrains.annotations.*;

public class HavenWorldQuotation extends ImmutableFieldProvider
{
    public static final String IS_NEW_ENTRY_FIELD = "isNewEntry";
    private final HavenWorldCatalogEntryView m_catalogEntryView;
    private Modification m_modification;
    
    private HavenWorldQuotation(final HavenWorldCatalogEntryView catalogEntryView) {
        super();
        this.m_catalogEntryView = catalogEntryView.getCopy();
    }
    
    public static HavenWorldQuotation fromNewEntry(final HavenWorldCatalogEntryView catalogEntryView) {
        return new HavenWorldQuotation(catalogEntryView);
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isNewEntry")) {
            return this.m_modification.getType() == Modification.Type.ADD;
        }
        return this.m_catalogEntryView.getFieldValue(fieldName);
    }
    
    public String getName() {
        return this.m_catalogEntryView.getName();
    }
    
    public HavenWorldCatalogEntryView getCatalogEntryView() {
        return this.m_catalogEntryView;
    }
    
    public Modification getModification() {
        return this.m_modification;
    }
    
    public void setModification(final Modification modification) {
        this.m_modification = modification;
    }
}
