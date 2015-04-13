package com.ankamagames.wakfu.client.core.companion;

import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.hero.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class HeroAddedMessageRunner implements MessageRunner<HeroAddedMessage>
{
    private static final Logger m_logger;
    
    @Override
    public boolean run(final HeroAddedMessage msg) {
        final LocalAccountInformations localPlayer = WakfuGameEntity.getInstance().getLocalAccount();
        final LocalPlayerCharacter playerCharacter = new LocalPlayerCharacter();
        playerCharacter.setAddActorToManager(false);
        playerCharacter.fromBuild(msg.getSerializedData());
        playerCharacter.initialize();
        final LocalPlayerCharacter character = (LocalPlayerCharacter)CharacterInfoManager.getInstance().getCharacter(playerCharacter.getId());
        if (character != null) {
            character.setAddActorToManager(true);
            MobileManager.getInstance().addMobile(character.getActor());
            character.fromBuild(msg.getSerializedData());
            character.initialize();
            character.getActor().setVisible(false);
        }
        final HeroManagerController<PlayerCharacter> controller = new HeroManagerControllerClient(localPlayer.getAccountId());
        try {
            controller.addHero(playerCharacter);
            controller.addToParty(playerCharacter);
        }
        catch (HeroException e) {
            HeroAddedMessageRunner.m_logger.error((Object)"Probl\u00e8me \u00e0 l'ajout du h\u00e9ros : ", (Throwable)e);
        }
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 5565;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HeroAddedMessageRunner.class);
    }
}
