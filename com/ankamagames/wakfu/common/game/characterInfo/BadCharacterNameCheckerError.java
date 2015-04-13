package com.ankamagames.wakfu.common.game.characterInfo;

public class BadCharacterNameCheckerError extends NameCheckerResult
{
    private char m_character;
    
    public BadCharacterNameCheckerError(final NameChecker.NameResult result, final char character) {
        super(result);
        this.m_character = character;
    }
    
    @Override
    public char getCharacter() {
        return this.m_character;
    }
}
