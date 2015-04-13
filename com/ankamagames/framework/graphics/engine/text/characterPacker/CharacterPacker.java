package com.ankamagames.framework.graphics.engine.text.characterPacker;

import com.ankamagames.framework.kernel.core.translator.*;
import org.jetbrains.annotations.*;

public abstract class CharacterPacker
{
    private static CharacterPacker m_characterPacker;
    
    public static void setLanguage(@NotNull final Language language) {
        switch (language) {
            case THA: {
                CharacterPacker.m_characterPacker = new ThaiCharacterPacker();
                break;
            }
            case VIE: {
                CharacterPacker.m_characterPacker = new VietCharacterPacker();
                break;
            }
            default: {
                CharacterPacker.m_characterPacker = new DefaultCharacterPacker();
                break;
            }
        }
    }
    
    @NotNull
    public static CharacterPacker getCharacterPacker() {
        return CharacterPacker.m_characterPacker;
    }
    
    public abstract boolean mustBePacked(final char p0);
    
    static {
        CharacterPacker.m_characterPacker = new CharacterPacker() {
            @Override
            public boolean mustBePacked(final char character) {
                return false;
            }
        };
    }
}
