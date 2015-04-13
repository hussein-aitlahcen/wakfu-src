package com.ankamagames.framework.graphics.engine.text.characterPacker;

public class VietCharacterPacker extends CharacterPacker
{
    @Override
    public boolean mustBePacked(final char character) {
        return character >= '\u0300' && character <= '\u036f';
    }
}
