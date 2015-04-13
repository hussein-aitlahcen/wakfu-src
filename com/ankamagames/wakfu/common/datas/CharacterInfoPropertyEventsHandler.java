package com.ankamagames.wakfu.common.datas;

public interface CharacterInfoPropertyEventsHandler
{
    void onIdentityChanged(BasicCharacterInfo p0);
    
    void onNameChanged(BasicCharacterInfo p0);
    
    void onBreedChanged(BasicCharacterInfo p0);
    
    @Deprecated
    void onSexChanged(BasicCharacterInfo p0);
    
    void onSymbiotChanged(BasicCharacterInfo p0);
    
    void onPositionChanged(BasicCharacterInfo p0);
    
    void onDirectionChanged(BasicCharacterInfo p0);
    
    void onAppearanceChanged(BasicCharacterInfo p0);
    
    void onCharacteristicChanged(BasicCharacterInfo p0);
    
    void onPropertyChanged(BasicCharacterInfo p0);
}
