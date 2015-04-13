package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import java.util.*;

public class VisibilityPartsList extends List<Data>
{
    private static final Logger m_logger;
    
    @Override
    protected void onAdding(final CharacterActor actor, final Data data) {
    }
    
    @Override
    public void onRemoved(final Data current, final Data removed, final CharacterActor actor) {
        if (removed != null) {
            actor.setLinkageVisible(removed.m_parts, !removed.m_visible);
            actor.onAppearanceChangedExternally();
        }
        if (!this.isEmpty()) {
            final Data next = this.getLast();
            next.apply(actor);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)VisibilityPartsList.class);
    }
    
    public static class Data extends AdditionnalData
    {
        private boolean m_visible;
        private String[] m_parts;
        
        public Data(final WakfuEffect effect, final boolean visible, final String[] parts) {
            super(effect);
            this.m_visible = visible;
            final ArrayList<String> listParts = new ArrayList<String>(parts.length);
            for (int i = 0; i < parts.length; ++i) {
                final String[] arr$;
                final String[] keys = arr$ = AnmPartHelper.getParts(parts[i]);
                for (final String key : arr$) {
                    if (!listParts.contains(key)) {
                        listParts.add(key);
                    }
                }
            }
            this.m_parts = listParts.toArray(new String[listParts.size()]);
        }
        
        private Data(final Data dataToCopy) {
            super(dataToCopy.m_effect);
            this.m_visible = dataToCopy.m_visible;
            this.m_parts = dataToCopy.m_parts;
        }
        
        @Override
        public Data duplicateForNewList() {
            return new Data(this);
        }
        
        @Override
        public void apply(final CharacterActor actor) {
            actor.setLinkageVisible(this.m_parts, this.m_visible);
            actor.onAppearanceChangedExternally();
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
            return this.m_visible == data.m_visible && Arrays.equals(this.m_parts, data.m_parts);
        }
        
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (this.m_visible ? 1 : 0);
            result = 31 * result + ((this.m_parts != null) ? Arrays.hashCode(this.m_parts) : 0);
            return result;
        }
    }
}
