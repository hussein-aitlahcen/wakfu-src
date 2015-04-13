package com.ankamagames.wakfu.common.game.characterInfo;

public class NameCheckerResult
{
    private final NameChecker.NameResult m_result;
    
    public NameCheckerResult(final NameChecker.NameResult result) {
        super();
        this.m_result = result;
    }
    
    public NameChecker.NameResult getResult() {
        return this.m_result;
    }
    
    public char getCharacter() {
        return '\uffff';
    }
}
