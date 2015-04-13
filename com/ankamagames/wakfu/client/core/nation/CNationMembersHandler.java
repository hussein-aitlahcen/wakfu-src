package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.handlers.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.event.*;
import com.ankamagames.wakfu.common.game.nation.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class CNationMembersHandler extends NationMembersHandler
{
    private static final Logger m_logger;
    
    public CNationMembersHandler(final Nation nation) {
        super(nation);
        this.registerEventHandler(ProtectorNationBuffEventHandler.INSTANCE);
    }
    
    @Override
    public void requestAddCitizen(@NotNull final Citizen character) {
        this.addCitizen(character);
    }
    
    @Override
    public void requestAddCitizen(final long characterId) {
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(characterId);
        if (character == null) {
            CNationMembersHandler.m_logger.error((Object)("[NATION] On demande l'ajout du character d'id " + characterId + " \u00e0 la nation " + this.getNation() + " mais il n'y a aucun personnage avec cet id"));
            return;
        }
        this.addCitizen(character);
    }
    
    static {
        m_logger = Logger.getLogger((Class)CNationMembersHandler.class);
    }
}
