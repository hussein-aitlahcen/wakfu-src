package com.ankamagames.framework.graphics.engine.text.characterPacker;

public class ThaiCharacterPacker extends CharacterPacker
{
    @Override
    public boolean mustBePacked(final char character) {
        return character == '\u0e31' || (character >= '\u0e33' && character <= '\u0e3a') || (character >= '\u0e47' && character <= '\u0e4e');
    }
}
