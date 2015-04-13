package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.fileFormat.properties.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class CostumeListData extends List.AdditionnalData
{
    private static final Logger m_logger;
    private final ArrayList<HMICostumeAction.Appearance> m_appearances;
    private final int m_particleId;
    private final int m_weight;
    
    public CostumeListData(final WakfuRunningEffect effect, final ArrayList<HMICostumeAction.Appearance> appearances, final int weight, final int particleId) {
        super(((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect());
        this.m_appearances = appearances;
        this.m_weight = weight;
        this.m_particleId = particleId;
    }
    
    public CostumeListData(final WakfuEffect effect, final ArrayList<HMICostumeAction.Appearance> appearances, final int weight, final int particleId) {
        super(effect);
        this.m_appearances = appearances;
        this.m_weight = weight;
        this.m_particleId = particleId;
    }
    
    CostumeListData(final CostumeListData dataToCopy) {
        super(dataToCopy.m_effect);
        this.m_appearances = dataToCopy.m_appearances;
        this.m_particleId = dataToCopy.m_particleId;
        this.m_weight = dataToCopy.m_weight;
    }
    
    @Override
    public void apply(final CharacterActor actor) {
        this.apply(actor, true);
    }
    
    public void apply(final AnimatedInteractiveElement actor, final boolean refreshDisplay) {
        try {
            if (refreshDisplay) {
                if (actor instanceof CharacterActor) {
                    ((CharacterActor)actor).getCharacterInfo().refreshDisplayEquipment();
                }
            }
            else {
                for (final HMICostumeAction.Appearance appearance : this.m_appearances) {
                    this.applyAppearance(actor, appearance);
                }
            }
        }
        catch (PropertyException e) {
            CostumeListData.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    private void applyAppearance(final AnimatedInteractiveElement actor, final HMICostumeAction.Appearance appearance) throws PropertyException {
        final String appearanceId = appearance.getAppearanceId();
        final int index = appearanceId.indexOf(47);
        final String dir = (index == -1) ? null : appearanceId.substring(0, index);
        final String gfxId = (index == -1) ? appearanceId : appearanceId.substring(index + 1);
        final String gfxFile = Actor.getGfxFile(dir, Integer.parseInt(gfxId));
        if (gfxFile == null) {
            CostumeListData.m_logger.error((Object)("dossier inconnu ( +" + dir + ") pour le chargement de l'anm " + gfxId + " [valeurs autoris\u00e9es: equipment/, npc/, player/]"));
            return;
        }
        final Anm equipement = AnimatedElement.loadEquipment(gfxFile);
        actor.applyParts(equipement, appearance.getPartsToChange());
    }
    
    public int getWeight() {
        return this.m_weight;
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
        final CostumeListData data = (CostumeListData)o;
        return this.m_particleId == data.m_particleId && this.m_weight == data.m_weight && this.m_appearances.equals(data.m_appearances);
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.m_appearances.hashCode();
        result = 31 * result + this.m_particleId;
        result = 31 * result + this.m_weight;
        return result;
    }
    
    @Override
    public CostumeListData duplicateForNewList() {
        return new CostumeListData(this);
    }
    
    static {
        m_logger = Logger.getLogger((Class)CostumeListData.class);
    }
}
