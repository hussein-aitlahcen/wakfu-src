package com.ankamagames.wakfu.client.core.game.companion;

import com.ankamagames.wakfu.common.game.companion.*;

public class CompanionViewShort extends ShortCharacterView
{
    private final long m_companionId;
    
    public CompanionViewShort(final CompanionModel companionModel) {
        super(companionModel.getName(), companionModel.getBreedId());
        this.m_companionId = companionModel.getId();
    }
    
    public long getCompanionId() {
        return this.m_companionId;
    }
    
    @Override
    public ShortCharacterView getCopy() {
        final CompanionModel companion = CompanionManager.INSTANCE.getCompanion(this.m_companionId);
        final ShortCharacterView shortCharacterView = new CompanionViewShort(companion);
        return shortCharacterView;
    }
}
