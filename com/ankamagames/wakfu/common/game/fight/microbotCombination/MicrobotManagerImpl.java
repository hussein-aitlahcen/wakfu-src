package com.ankamagames.wakfu.common.game.fight.microbotCombination;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public class MicrobotManagerImpl implements MicrobotManager
{
    private static final Logger m_logger;
    private final TLongObjectHashMap<MicrobotSet> m_microbotsPerOwner;
    private MicrobotCombinationEventListener m_combinationsEventListener;
    @Nullable
    private FightMap m_figthMap;
    
    public MicrobotManagerImpl() {
        super();
        this.m_microbotsPerOwner = new TLongObjectHashMap<MicrobotSet>();
        this.m_combinationsEventListener = null;
        this.m_figthMap = null;
    }
    
    @Override
    public void setListener(final MicrobotCombinationEventListener combinationsEventListener) {
        this.m_combinationsEventListener = combinationsEventListener;
    }
    
    @Override
    public void setFightMap(@Nullable final FightMap fightMap) {
        this.m_figthMap = fightMap;
    }
    
    @Override
    public void handleMicrobotAdded(final AbstractFakeFighterEffectArea microbot) {
        if (!this.checkMicrobotValidity(microbot)) {
            return;
        }
        final long ownerId = microbot.getOriginalControllerId();
        if (!this.m_microbotsPerOwner.containsKey(ownerId)) {
            final EffectUser owner = microbot.getOwner();
            if (owner == null) {
                MicrobotManagerImpl.m_logger.error((Object)("Unable to create microbot for null owner. OriginalControllerId : " + ownerId));
                return;
            }
            int maximumSpaceBetweenMicrobots;
            if (owner.hasCharacteristic(FighterCharacteristicType.STEAMER_MICROBOT_MAX_DISTANCE)) {
                maximumSpaceBetweenMicrobots = owner.getCharacteristicValue(FighterCharacteristicType.STEAMER_MICROBOT_MAX_DISTANCE);
            }
            else {
                maximumSpaceBetweenMicrobots = 3;
            }
            this.m_microbotsPerOwner.put(ownerId, new MicrobotSet(ownerId, maximumSpaceBetweenMicrobots));
        }
        final MicrobotSet microbotSet = this.m_microbotsPerOwner.get(ownerId);
        microbotSet.add(microbot, this.m_figthMap, this.m_combinationsEventListener);
    }
    
    @Override
    public void handleMicrobotRemoved(final AbstractFakeFighterEffectArea microbot) {
        if (!this.checkMicrobotValidity(microbot)) {
            return;
        }
        final long ownerId = microbot.getOriginalControllerId();
        if (!this.m_microbotsPerOwner.containsKey(ownerId)) {
            return;
        }
        final MicrobotSet microbotSet = this.m_microbotsPerOwner.get(ownerId);
        microbotSet.remove(microbot, this.m_figthMap, this.m_combinationsEventListener);
    }
    
    @Nullable
    @Override
    public MicrobotSet getMicrobotSet(final long ownerId) {
        return this.m_microbotsPerOwner.get(ownerId);
    }
    
    private boolean checkMicrobotValidity(final AbstractFakeFighterEffectArea microbot) {
        if (microbot == null) {
            MicrobotManagerImpl.m_logger.error((Object)"Trying to handle a 'null' microbot", (Throwable)new RuntimeException());
            return false;
        }
        if (microbot.getUserDefinedId() != 4) {
            MicrobotManagerImpl.m_logger.error((Object)("Trying to handle a microbot wich is not a microbot (UserdefineID = " + microbot.getUserDefinedId() + ")"), (Throwable)new RuntimeException());
            return false;
        }
        if (microbot.getOriginalControllerId() == 0L) {
            MicrobotManagerImpl.m_logger.error((Object)("Trying to handle a microbot without owner (ownerId=0) : " + microbot), (Throwable)new RuntimeException());
            return false;
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MicrobotManagerImpl.class);
    }
}
