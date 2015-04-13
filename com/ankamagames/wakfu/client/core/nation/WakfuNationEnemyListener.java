package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.baseImpl.graphics.core.partitions.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;

public class WakfuNationEnemyListener implements PartitionChangedListener<PathMobile, LocalPartition>, TargetPositionListener<PathMobile>
{
    protected static final Logger m_logger;
    public static WakfuNationEnemyListener INSTANCE;
    
    public void initialize() {
        LocalPartitionManager.getInstance().addPartitionListener(this);
    }
    
    @Override
    public void partitionChanged(final PathMobile mobile, final LocalPartition oldPartition, final LocalPartition newPartition) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.getCurrentFight() == null && mobile instanceof CharacterActor) {
            final CharacterInfo character = ((CharacterActor)mobile).getCharacterInfo();
            if (character instanceof PlayerCharacter) {
                ((PlayerCharacter)character).updateAdditionalAppearance();
            }
        }
    }
    
    @Override
    public void cellPositionChanged(final PathMobile target, final int worldX, final int worldY, final short altitude) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuNationEnemyListener.class);
        WakfuNationEnemyListener.INSTANCE = new WakfuNationEnemyListener();
    }
}
