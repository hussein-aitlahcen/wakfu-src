package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class ChangePartsList extends List<Data>
{
    private static final Logger m_logger;
    private static final Comparator<? super Data> COMPARATOR;
    
    public Data[] getSorted() {
        final Data[] data = new Data[this.m_stack.size()];
        this.m_stack.toArray(data);
        Arrays.sort(data, ChangePartsList.COMPARATOR);
        return data;
    }
    
    @Override
    protected void onAdding(final CharacterActor actor, final Data data) {
    }
    
    @Override
    public void onRemoved(final Data current, final Data removed, final CharacterActor actor) {
        actor.getCharacterInfo().refreshDisplayEquipment();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChangePartsList.class);
        COMPARATOR = new Comparator<Data>() {
            @Override
            public int compare(final Data o1, final Data o2) {
                return o1.m_weight - o2.m_weight;
            }
        };
    }
    
    public static class Data extends AdditionnalData
    {
        public static final String[] EMPTY_STRINGS;
        private final String m_gfxId;
        private final String[] m_parts;
        private final int m_weight;
        private Anm m_equipement;
        
        public Data(final WakfuRunningEffect effect, final String gfxId, final int weight, final String[] parts) {
            super(((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect());
            this.m_gfxId = gfxId;
            this.m_weight = weight;
            this.m_parts = this.getCleanParts(parts);
        }
        
        public Data(final WakfuEffect effect, final String gfxId, final int weight, final String[] parts) {
            super(effect);
            this.m_gfxId = gfxId;
            this.m_weight = weight;
            this.m_parts = this.getCleanParts(parts);
        }
        
        private String[] getCleanParts(final String[] parts) {
            if (parts == null) {
                return Data.EMPTY_STRINGS;
            }
            final int partCount = parts.length;
            final ArrayList<String> listParts = new ArrayList<String>(partCount);
            for (int i = 0; i < partCount; ++i) {
                for (final String key : AnmPartHelper.getParts(parts[i])) {
                    if (!listParts.contains(key)) {
                        listParts.add(key);
                    }
                }
            }
            return listParts.toArray(new String[listParts.size()]);
        }
        
        private Data(final Data dataToCopy) {
            super(dataToCopy.m_effect);
            this.m_gfxId = dataToCopy.m_gfxId;
            this.m_weight = dataToCopy.m_weight;
            this.m_parts = dataToCopy.m_parts;
        }
        
        @Override
        public void apply(final CharacterActor actor) {
            this.apply(actor, true);
        }
        
        public void apply(final AnimatedInteractiveElement actor, final boolean refreshDisplay) {
            try {
                final int index = this.m_gfxId.indexOf(47);
                final String dir = (index == -1) ? null : this.m_gfxId.substring(0, index);
                final String gfxId = (index == -1) ? this.m_gfxId : this.m_gfxId.substring(index + 1);
                final String gfxFile = Actor.getGfxFile(dir, Integer.parseInt(gfxId));
                if (gfxFile == null) {
                    ChangePartsList.m_logger.error((Object)("dossier inconnu ( +" + dir + ") pour le chargement de l'anm " + gfxId + " [valeurs autoris\u00e9es: equipment/, npc/, player/]"));
                    return;
                }
                this.m_equipement = AnimatedElement.loadEquipment(gfxFile);
                if (refreshDisplay) {
                    if (actor instanceof CharacterActor) {
                        ((CharacterActor)actor).getCharacterInfo().refreshDisplayEquipment();
                    }
                }
                else {
                    actor.applyParts(this.m_equipement, this.m_parts);
                }
            }
            catch (PropertyException e) {
                ChangePartsList.m_logger.error((Object)"", (Throwable)e);
            }
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
            final Data data = (Data)o;
            if (this.m_gfxId != null) {
                if (this.m_gfxId.equals(data.m_gfxId)) {
                    return Arrays.equals(this.m_parts, data.m_parts);
                }
            }
            else if (data.m_gfxId == null) {
                return Arrays.equals(this.m_parts, data.m_parts);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + ((this.m_gfxId != null) ? this.m_gfxId.hashCode() : 0);
            result = 31 * result + ((this.m_parts != null) ? Arrays.hashCode(this.m_parts) : 0);
            return result;
        }
        
        @Override
        public Data duplicateForNewList() {
            return new Data(this);
        }
        
        static {
            EMPTY_STRINGS = new String[0];
        }
    }
}
