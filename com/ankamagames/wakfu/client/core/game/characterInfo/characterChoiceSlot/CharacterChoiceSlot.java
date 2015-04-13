package com.ankamagames.wakfu.client.core.game.characterInfo.characterChoiceSlot;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;

public class CharacterChoiceSlot extends AbstractCharacterChoiceSlot
{
    public static final String CHARACTER = "character";
    private final CharacterInfo m_characterInfo;
    
    protected CharacterChoiceSlot(final CharacterInfo characterInfo) {
        super(CharacterChoiceSlotType.CHARACTER_SLOT);
        this.m_characterInfo = characterInfo;
        if (this.m_characterInfo != null) {
            this.m_characterInfo.getActor().setDirection(Direction8.SOUTH_WEST);
        }
    }
    
    @Override
    public String[] getFields() {
        return CharacterChoiceSlot.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("character")) {
            return this.m_characterInfo;
        }
        return super.getFieldValue(fieldName);
    }
    
    public CharacterInfo getCharacterInfo() {
        return this.m_characterInfo;
    }
}
