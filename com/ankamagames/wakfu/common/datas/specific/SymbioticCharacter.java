package com.ankamagames.wakfu.common.datas.specific;

import com.ankamagames.wakfu.common.datas.specific.symbiot.*;

public interface SymbioticCharacter<Symbiot extends AbstractSymbiot>
{
    public static final int SEDUCTION_SPELL_ID = 730;
    public static final int INVOCATION_SPELL_ID = 787;
    
    Symbiot getSymbiot();
    
    void setSymbiot(Symbiot p0);
    
    void onSymbiotAddCreature(byte p0);
    
    void onSymbiotReleaseCreature(byte p0);
    
    void onSymbiotReset();
    
    byte getMaximumSeducableCreatures();
    
    void setMaximumSeducableCreatures(byte p0);
}
