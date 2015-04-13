package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ParticleListData extends List.AdditionnalData
{
    public final int m_systemId;
    public final Point3 m_forcedPos;
    public final boolean m_alwaysActivated;
    public final ParticleLocalisation m_localisation;
    public final boolean m_isInstant;
    public FreeParticleSystem m_system;
    public CharacterActor m_actor;
    ParticlesList m_associatedList;
    public boolean m_hasAlreadyRunOnce;
    
    ParticleListData(final WakfuEffect effect, final int systemId, final Point3 forcedPos, final boolean alwaysActivated, final boolean isInstant, final ParticleLocalisation localisation, final ParticlesList particlesList) {
        super(effect);
        this.m_hasAlreadyRunOnce = false;
        this.m_systemId = systemId;
        this.m_forcedPos = forcedPos;
        this.m_alwaysActivated = alwaysActivated;
        this.m_associatedList = particlesList;
        this.m_localisation = localisation;
        this.m_isInstant = isInstant;
    }
    
    private ParticleListData(final ParticleListData dataToCopy) {
        super(dataToCopy.m_effect);
        this.m_hasAlreadyRunOnce = false;
        this.m_systemId = dataToCopy.m_systemId;
        this.m_forcedPos = dataToCopy.m_forcedPos;
        this.m_alwaysActivated = dataToCopy.m_alwaysActivated;
        this.m_associatedList = null;
        this.m_localisation = dataToCopy.m_localisation;
        this.m_isInstant = dataToCopy.m_isInstant;
    }
    
    @Override
    public void apply(final CharacterActor actor) {
        this.m_actor = actor;
        this.m_associatedList.onDataApplication(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final ParticleListData that = (ParticleListData)o;
        if (this.m_alwaysActivated != that.m_alwaysActivated) {
            return false;
        }
        if (this.m_isInstant != that.m_isInstant) {
            return false;
        }
        if (this.m_systemId != that.m_systemId) {
            return false;
        }
        if (this.m_forcedPos != null) {
            if (this.m_forcedPos.equals(that.m_forcedPos)) {
                return this.m_localisation == that.m_localisation;
            }
        }
        else if (that.m_forcedPos == null) {
            return this.m_localisation == that.m_localisation;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.m_systemId;
        result = 31 * result + ((this.m_forcedPos != null) ? this.m_forcedPos.hashCode() : 0);
        result = 31 * result + (this.m_alwaysActivated ? 1 : 0);
        result = 31 * result + ((this.m_localisation != null) ? this.m_localisation.hashCode() : 0);
        result = 31 * result + (this.m_isInstant ? 1 : 0);
        return result;
    }
    
    @Override
    public ParticleListData duplicateForNewList() {
        return new ParticleListData(this);
    }
}
