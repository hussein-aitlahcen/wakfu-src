package com.ankamagames.wakfu.client.core.game.breed;

import com.ankamagames.wakfu.common.datas.Breed.*;

public class PlayerCharacterBreedManager extends AbstractBreedManager<AvatarBreed>
{
    private static final PlayerCharacterBreedManager m_instance;
    
    private PlayerCharacterBreedManager() {
        super();
        for (final AvatarBreed breed : AvatarBreed.values()) {
            this.addBreed(breed);
        }
    }
    
    public static PlayerCharacterBreedManager getInstance() {
        return PlayerCharacterBreedManager.m_instance;
    }
    
    static {
        m_instance = new PlayerCharacterBreedManager();
    }
}
