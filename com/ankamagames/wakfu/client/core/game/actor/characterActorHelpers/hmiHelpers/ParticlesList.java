package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import java.util.*;

public class ParticlesList extends List<ParticleListData>
{
    private static final Logger m_logger;
    private final LinkedList<ParticleListData> m_delayedDisplayDataQueue;
    private final ParticleListTimer m_timer;
    private boolean m_currentVisibility;
    
    public ParticlesList() {
        super();
        this.m_delayedDisplayDataQueue = new LinkedList<ParticleListData>();
        this.m_timer = new ParticleListTimer(this);
        this.m_currentVisibility = true;
    }
    
    public void addAndApply(final CharacterActor actor, final WakfuEffect effect, final int systemId, final ParticleLocalisation localisation, final Point3 forcedPos, final boolean alwaysActivated, final boolean isInstant) {
        final ParticleListData data = new ParticleListData(effect, systemId, forcedPos, alwaysActivated, isInstant, localisation, this);
        this.onAdding(actor, data);
        if (!data.m_isInstant) {
            this.m_stack.add((T)data);
        }
        data.apply(actor);
    }
    
    @Override
    public void addAndApply(final CharacterActor actor, final ParticleListData data) {
        if (data.m_associatedList == null) {
            data.m_associatedList = this;
        }
        super.addAndApply(actor, data);
    }
    
    private void applyData(final ParticleListData data) {
        final CharacterInfo characterInfo = data.m_actor.getCharacterInfo();
        if (characterInfo != null && characterInfo.isInvisibleForLocalPlayer()) {
            data.m_system = null;
            return;
        }
        (data.m_system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(data.m_systemId)).setVisible(this.m_currentVisibility);
        IsoParticleSystemManager.getInstance().addParticleSystem(data.m_system);
        if (data.m_forcedPos != null) {
            data.m_system.setWorldPosition(data.m_forcedPos.getX(), data.m_forcedPos.getY(), data.m_forcedPos.getZ());
        }
        else {
            float heightPercent = -1.0f;
            int zOffset = -1;
            if (characterInfo instanceof PlayerCharacter) {
                final PlayerCharacter playerCharacter = (PlayerCharacter)characterInfo;
                final AbstractOccupation currentOccupation = playerCharacter.getCurrentOccupation();
                final boolean isRiding = currentOccupation != null && currentOccupation.getOccupationTypeId() == 14 && ((RideOccupation)currentOccupation).getAnm() != null;
                if (isRiding) {
                    heightPercent = 0.0f;
                    zOffset = 3;
                }
            }
            switch (data.m_localisation) {
                case NONE:
                case FOOT: {
                    break;
                }
                case HEAD: {
                    heightPercent = 1.0f;
                    zOffset = 0;
                    break;
                }
                case MIDDLE: {
                    heightPercent = 0.5f;
                    zOffset = 0;
                    break;
                }
                default: {
                    ParticlesList.m_logger.error((Object)("Localisation d'aps non traitee " + data.m_localisation));
                    break;
                }
            }
            if (heightPercent < 0.0f || zOffset < 0) {
                data.m_system.setTarget(data.m_actor);
            }
            else {
                data.m_system.setTarget(data.m_actor, heightPercent, zOffset);
            }
        }
        if (characterInfo != null && characterInfo.isOnFight()) {
            data.m_system.setFightId(characterInfo.getCurrentFightId());
        }
        if (data.m_hasAlreadyRunOnce) {
            data.m_system.setSoundEnable(false);
        }
        else {
            data.m_hasAlreadyRunOnce = true;
        }
    }
    
    @Override
    protected void onAdding(final CharacterActor actor, final ParticleListData data) {
        if (!data.m_alwaysActivated && !data.m_isInstant) {
            this.m_delayedDisplayDataQueue.addLast(data);
        }
        if (!this.m_timer.isRunning()) {
            this.m_timer.startParticleDisplay();
        }
    }
    
    @Override
    public void onRemoved(final ParticleListData current, final ParticleListData removed, final CharacterActor actor) {
        this.removeSystem(removed);
        if (removed == this.m_delayedDisplayDataQueue.peek()) {
            this.m_delayedDisplayDataQueue.poll();
            this.startSystem(this.m_delayedDisplayDataQueue.peek());
        }
        else {
            this.removeStrictEquals(removed);
        }
        if (this.isEmpty()) {
            this.m_timer.stopParticleDisplay();
        }
    }
    
    protected void removeStrictEquals(final ParticleListData removed) {
        final Iterator<ParticleListData> iterator = this.m_delayedDisplayDataQueue.iterator();
        while (iterator.hasNext()) {
            final ParticleListData next = iterator.next();
            if (next == removed) {
                iterator.remove();
            }
        }
    }
    
    private void removeSystem(final ParticleListData removed) {
        if (removed == null) {
            return;
        }
        if (removed.m_system == null) {
            return;
        }
        removed.m_system.kill();
        removed.m_system = null;
    }
    
    public void stopCurrentAndStartNext() {
        if (this.m_delayedDisplayDataQueue.isEmpty() || this.m_delayedDisplayDataQueue.size() == 1) {
            return;
        }
        final ParticleListData current = this.m_delayedDisplayDataQueue.poll();
        this.stopSystem(current);
        this.m_delayedDisplayDataQueue.addLast(current);
        try {
            this.startSystem(this.m_delayedDisplayDataQueue.peek());
        }
        catch (Exception e) {
            ParticlesList.m_logger.error((Object)"Exception levee", (Throwable)e);
            this.m_delayedDisplayDataQueue.poll();
        }
    }
    
    private void stopSystem(final ParticleListData data) {
        if (data == null || data.m_system == null || !data.m_system.isAlive()) {
            return;
        }
        data.m_system.stopAndKill();
    }
    
    private void startSystem(final ParticleListData data) {
        if (data == null) {
            return;
        }
        if (data.m_system == null || !data.m_system.isAlive()) {
            this.applyData(data);
        }
        else {
            data.m_system.reset();
            data.m_system.start();
        }
    }
    
    public void setCurrentParticleVisibility(final boolean visible) {
        this.m_currentVisibility = visible;
        final ParticleListData currentlyPlayedParticleData = this.m_delayedDisplayDataQueue.peek();
        if (currentlyPlayedParticleData == null || currentlyPlayedParticleData.m_system == null) {
            return;
        }
        IsoParticleSystemManager.getInstance().setParticleVisible(currentlyPlayedParticleData.m_system.getId(), visible);
    }
    
    public void onDataApplication(final ParticleListData data) {
        if (data.m_alwaysActivated || data.m_isInstant) {
            this.applyData(data);
        }
        else if (this.m_delayedDisplayDataQueue.size() == 1) {
            if (this.m_delayedDisplayDataQueue.peek() == data) {
                this.applyData(data);
            }
            else {
                ParticlesList.m_logger.warn((Object)"Si la liste est de taille 1, le premier element devrait etre celui la, ce n'est pas nomal d'en avoir un autre");
            }
        }
    }
    
    @Override
    public void clear(final CharacterActor actor) {
        final ParticleListData current = this.getLast();
        this.m_stack.clear();
        this.onRemoved(current, current, actor);
    }
    
    public void remove(final CharacterActor actor, final WakfuEffect effect, final int systemId, final ParticleLocalisation localisation, final Point3 forcedPos, final boolean alwaysActivated, final boolean isInstant) {
        final ParticleListData data = new ParticleListData(effect, systemId, forcedPos, alwaysActivated, isInstant, localisation, this);
        if (data.m_isInstant) {
            final ParticleListData current = this.getLast();
            this.onRemoved(current, null, actor);
        }
        else {
            this.remove(actor, data, false);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ParticlesList.class);
    }
}
