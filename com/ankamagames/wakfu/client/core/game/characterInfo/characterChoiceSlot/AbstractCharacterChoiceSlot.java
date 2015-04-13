package com.ankamagames.wakfu.client.core.game.characterInfo.characterChoiceSlot;

import com.ankamagames.wakfu.client.ui.component.*;
import org.jetbrains.annotations.*;

public abstract class AbstractCharacterChoiceSlot extends ImmutableFieldProvider
{
    public static final String TYPE = "type";
    private CharacterChoiceSlotType m_type;
    private int m_offset;
    
    protected AbstractCharacterChoiceSlot(final CharacterChoiceSlotType type) {
        super();
        this.m_type = type;
    }
    
    @Override
    public String[] getFields() {
        return AbstractCharacterChoiceSlot.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("type")) {
            return this.m_type.getId();
        }
        return null;
    }
    
    public int getOffset() {
        return this.m_offset;
    }
    
    public void setOffset(final int offset) {
        this.m_offset = offset;
    }
}
