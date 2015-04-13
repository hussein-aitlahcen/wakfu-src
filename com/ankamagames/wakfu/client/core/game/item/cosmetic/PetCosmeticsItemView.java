package com.ankamagames.wakfu.client.core.game.item.cosmetic;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import org.jetbrains.annotations.*;

public class PetCosmeticsItemView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String NO_COSTUMES = "noCostumes";
    public static final String COMPATIBLE = "compatible";
    public static final String HAS_CONDITIONS = "hasConditions";
    public static final String REF_ITEM = "refItem";
    private final ReferenceItem m_item;
    
    public PetCosmeticsItemView(final int refItemId) {
        super();
        this.m_item = ((refItemId > 0) ? ReferenceItemManager.getInstance().getReferenceItem(refItemId) : null);
    }
    
    public int getRefId() {
        return (this.m_item == null) ? 0 : this.m_item.getId();
    }
    
    private boolean isCompatible() {
        if (this.m_item == null) {
            return true;
        }
        final PetDetailDialogView petView = UIPetCosmeticsFrame.getInstance().getPetView();
        if (petView == null) {
            return false;
        }
        final PetDefinition definition = petView.getPet().getDefinition();
        return definition.containsEquipment(this.m_item.getId()) || definition.containsReskinItem(this.m_item.getId());
    }
    
    @Override
    public String[] getFields() {
        return PetCosmeticsItemView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("noCostumes")) {
            return this.m_item == null;
        }
        if (fieldName.equals("compatible")) {
            return this.isCompatible();
        }
        if ("refItem".equals(fieldName)) {
            return this.m_item;
        }
        if (fieldName.equals("hasConditions")) {
            if (!this.isCompatible()) {
                return true;
            }
            if (this.m_item == null) {
                return false;
            }
            final ArrayList<String> fieldValue = (ArrayList<String>)this.m_item.getReferenceItemDisplayer().getFieldValue("conditionDescription");
            if (fieldValue != null && fieldValue.size() > 0) {
                return true;
            }
            return false;
        }
        else {
            if (this.m_item != null) {
                return this.m_item.getFieldValue(fieldName);
            }
            if (fieldName.equals("name")) {
                return WakfuTranslator.getInstance().getString("cosmetics.noCostumes");
            }
            if (fieldName.equals("iconUrl")) {
                try {
                    return WakfuConfiguration.getContentPath("defaultIconPath");
                }
                catch (PropertyException e) {
                    PetCosmeticsItemView.m_logger.info((Object)e.getMessage(), (Throwable)e);
                }
            }
            return null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)PetCosmeticsItemView.class);
    }
}
