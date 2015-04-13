package com.ankamagames.wakfu.client.core.game.characterInfo.monsters;

import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.framework.graphics.image.*;

public class MonsterSpecialGfx
{
    protected GrowingArray<Equipment> m_equipements;
    protected GrowingArray<Colors> m_colors;
    protected Anim[] m_anims;
    
    public void setEquipements(final Equipment[] equipements) {
        if (equipements == null || equipements.length == 0) {
            this.m_equipements = null;
            return;
        }
        this.m_equipements = new GrowingArray<Equipment>();
        for (final Equipment e : equipements) {
            this.addEquipement(e);
        }
    }
    
    public void addEquipement(final Equipment equipment) {
        if (this.m_equipements == null) {
            this.m_equipements = new GrowingArray<Equipment>(new Equipment[] { equipment });
            return;
        }
        final boolean[] found = { false };
        this.foreachEquipement(new TObjectProcedure<Equipment>() {
            @Override
            public boolean execute(final Equipment e) {
                if (e.m_fileId.equals(equipment.m_fileId)) {
                    e.mergeParts(equipment.m_parts);
                    found[0] = true;
                    return false;
                }
                return true;
            }
        });
        if (!found[0]) {
            this.m_equipements.add(equipment);
        }
    }
    
    public void setColors(final Colors[] colors) {
        if (colors == null) {
            this.m_colors = null;
        }
        else {
            this.m_colors = new GrowingArray<Colors>(colors);
        }
    }
    
    public void addColor(final Colors color) {
        if (this.m_colors == null) {
            this.m_colors = new GrowingArray<Colors>(new Colors[] { color });
        }
        else {
            this.m_colors.add(color);
        }
    }
    
    public void setAnims(final Anim[] anims) {
        this.m_anims = anims;
    }
    
    public boolean hasEquipement() {
        return this.m_equipements != null && this.m_equipements.size() > 0;
    }
    
    public void foreachEquipement(final TObjectProcedure<Equipment> procedure) {
        if (this.m_equipements == null) {
            return;
        }
        this.m_equipements.foreach(procedure);
    }
    
    public void foreachColor(final TObjectProcedure<Colors> procedure) {
        if (this.m_colors == null) {
            return;
        }
        this.m_colors.foreach(procedure);
    }
    
    public Anim[] getAnims() {
        return this.m_anims;
    }
    
    public static class Equipment
    {
        public final String m_fileId;
        public String[] m_parts;
        
        public Equipment(final String fileId, final String... parts) {
            super();
            this.m_fileId = fileId;
            this.mergeParts(parts);
        }
        
        void mergeParts(final String[] parts) {
            final THashSet<String> set = new THashSet<String>();
            if (this.m_parts != null) {
                Collections.addAll(set, this.m_parts);
            }
            Collections.addAll(set, parts);
            set.toArray(this.m_parts = new String[set.size()]);
        }
    }
    
    public static class Colors
    {
        public final int m_partIndex;
        public final Color m_color;
        
        public Colors(final String partName, final Color color) {
            super();
            this.m_partIndex = Integer.parseInt(partName);
            this.m_color = color;
        }
        
        public Colors(final int partIndex, final Color color) {
            super();
            this.m_partIndex = partIndex;
            this.m_color = color;
        }
    }
    
    public static class Anim
    {
        public static final byte STATIC_KEY = 1;
        public static final byte HIT_KEY = 2;
        public static final byte ON_SPAWN_KEY = 3;
        public final byte m_key;
        public final String m_anim;
        
        public Anim(final byte key, final String anim) {
            super();
            this.m_key = key;
            this.m_anim = anim;
        }
    }
}
