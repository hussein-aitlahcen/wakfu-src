package com.ankamagames.framework.graphics.engine.text.characterPacker;

public class DefaultCharacterPacker extends CharacterPacker
{
    @Override
    public boolean mustBePacked(final char character) {
        return false;
    }
}
