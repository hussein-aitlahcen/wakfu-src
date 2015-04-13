package com.ankamagames.wakfu.common.game.hero;

import com.ankamagames.wakfu.common.datas.*;

public class HeroManagerController<T extends BasicCharacterInfo>
{
    protected final long m_ownerId;
    
    public HeroManagerController(final long ownerId) {
        super();
        this.m_ownerId = ownerId;
    }
    
    public void addHero(final T info) throws HeroException {
        if (info.getOwnerId() != this.m_ownerId) {
            throw new HeroException("Le h\u00e9ros qu'on essaye d'ajouter n'appartient pas au client : " + this.m_ownerId);
        }
        HeroesManager.INSTANCE.addHero(this.m_ownerId, info);
    }
    
    public void addToParty(final T info) throws HeroException {
        if (info.getOwnerId() != this.m_ownerId) {
            throw new HeroException("Le h\u00e9ros qu'on essaye d'ajouter au groupe n'appartient pas au client : " + this.m_ownerId);
        }
        final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(info.getId());
        if (hero == null) {
            throw new HeroException("Le h\u00e9ros qu'on veut grouper n'est pas charg\u00e9");
        }
        HeroesManager.INSTANCE.addHeroToParty(this.m_ownerId, info.getId());
    }
    
    public void removeFromParty(final T info) throws HeroException {
        if (info.getOwnerId() != this.m_ownerId) {
            throw new HeroException("Le h\u00e9ros qu'on essaye de retirer du groupe n'appartient pas au client : " + this.m_ownerId);
        }
        final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(info.getId());
        if (hero == null) {
            throw new HeroException("Le h\u00e9ros qu'on veut d\u00e9grouper n'est pas charg\u00e9");
        }
        HeroesManager.INSTANCE.removeHeroFromParty(this.m_ownerId, info.getId());
    }
    
    @Override
    public String toString() {
        return "HeroManagerController{m_ownerId=" + this.m_ownerId + '}';
    }
}
