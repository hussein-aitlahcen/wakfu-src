package com.ankamagames.wakfu.client.core.havenWorld.view.catalogEntry;

import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;

public class HavenWorldCatalogPatchEntryView extends HavenWorldCatalogEntryView<PatchCatalogEntry>
{
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String SIDOA_NEED_FIELD = "sidoaNeed";
    public static final String BUILD_DELAY_FIELD = "buildDelay";
    public static final String CONDITIONS_FIELD = "conditions";
    public static final String RESSOURCES_FIELD = "ressources";
    public static final String[] FIELDS;
    private final HavenWorldImagesLibrary m_havenWorldImages;
    
    @Override
    public String[] getFields() {
        return HavenWorldCatalogPatchEntryView.FIELDS;
    }
    
    public HavenWorldCatalogPatchEntryView(@NotNull final PatchCatalogEntry patchCatalogEntry, @NotNull final HavenWorldImagesLibrary havenWorldImages) {
        super(patchCatalogEntry);
        this.m_havenWorldImages = havenWorldImages;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        final Object result = super.getFieldValue(fieldName);
        if (result != null) {
            return result;
        }
        if (fieldName.equals("iconUrl")) {
            return this.m_havenWorldImages.getPatchTexture(((PatchCatalogEntry)this.m_catalogEntry).getPatchId());
        }
        if (fieldName.equals("description")) {
            return WakfuTranslator.getInstance().getString(125, ((PatchCatalogEntry)this.m_catalogEntry).getId(), new Object[0]);
        }
        if (fieldName.equals("conditions")) {
            return null;
        }
        return super.getFieldValue(fieldName);
    }
    
    @Override
    public boolean hasEnoughKamas() {
        return this.isAvailable();
    }
    
    @Override
    public int getCurrentQuantity() {
        return 0;
    }
    
    @Override
    public HavenWorldCatalogEntryView getCopy() {
        final HavenWorldCatalogPatchEntryView havenWorldCatalogPartitionEntryView = new HavenWorldCatalogPatchEntryView((PatchCatalogEntry)this.m_catalogEntry, HavenWorldImagesLibrary.INSTANCE);
        return havenWorldCatalogPartitionEntryView;
    }
    
    @Override
    public HavenWorldCatalogView.CatalogCategory getCategory() {
        return HavenWorldCatalogView.CatalogCategory.PATCH;
    }
    
    static {
        FIELDS = HavenWorldCatalogEntryView.concatFields("iconUrl", "description", "sidoaNeed", "buildDelay", "conditions", "ressources");
    }
}
