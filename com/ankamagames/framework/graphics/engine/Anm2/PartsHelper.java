package com.ankamagames.framework.graphics.engine.Anm2;

import java.util.*;
import com.ankamagames.framework.graphics.engine.Anm2.Index.*;
import org.jetbrains.annotations.*;
import gnu.trove.*;

public class PartsHelper
{
    public static final PartsHelper NULL;
    private boolean m_refreshMultiFrameCache;
    private boolean m_hasMultiFramePart;
    private boolean m_refreshHideCache;
    private final ArrayList<CanHidePart> m_canHideParts;
    private final ArrayList<Anm> m_used;
    private final ArrayList<EquipementDef> m_partsToSet;
    private final TIntObjectHashMap<EquipementDef> m_partsSetted;
    private int m_modification;
    private final TObjectProcedure<EquipementDef> m_multiFramePartProcedure;
    private final TObjectProcedure<EquipementDef> m_refreshHideCacheProcedure;
    
    public PartsHelper() {
        super();
        this.m_refreshMultiFrameCache = true;
        this.m_refreshHideCache = true;
        this.m_canHideParts = new ArrayList<CanHidePart>(1);
        this.m_used = new ArrayList<Anm>(4);
        this.m_partsToSet = new ArrayList<EquipementDef>(4);
        this.m_partsSetted = new TIntObjectHashMap<EquipementDef>();
        this.m_multiFramePartProcedure = new MultiFramePartProcedure();
        this.m_refreshHideCacheProcedure = new RefreshHideCacheProcedure();
    }
    
    protected final boolean hasModification() {
        return !this.m_used.isEmpty() || !this.m_partsToSet.isEmpty() || !this.m_partsSetted.isEmpty() || this.m_modification != 0;
    }
    
    public final boolean hasPartSetted() {
        return !this.m_partsSetted.isEmpty();
    }
    
    public final boolean hasPartToSet() {
        return !this.m_partsToSet.isEmpty();
    }
    
    final EquipementDef getDefinition(final int nameCRC) {
        return this.m_partsSetted.get(nameCRC);
    }
    
    public boolean hasMultiFramePart() {
        if (this.m_refreshMultiFrameCache) {
            this.m_refreshMultiFrameCache = false;
            this.m_hasMultiFramePart = false;
            this.m_hasMultiFramePart = (!this.m_partsSetted.isEmpty() && !this.m_partsSetted.forEachValue(this.m_multiFramePartProcedure));
        }
        return this.m_hasMultiFramePart;
    }
    
    public ArrayList<CanHidePart> getCanHideParts() {
        if (this.m_refreshHideCache) {
            this.m_refreshHideCache = false;
            this.m_canHideParts.clear();
            this.m_partsSetted.forEachValue(this.m_refreshHideCacheProcedure);
        }
        return this.m_canHideParts;
    }
    
    public void clear() {
        for (int i = this.m_used.size() - 1; i >= 0; --i) {
            removeReference(this.m_used.get(i));
        }
        this.m_used.clear();
        this.m_partsToSet.clear();
        this.m_partsSetted.clear();
        this.m_canHideParts.clear();
        this.m_refreshHideCache = true;
        this.m_hasMultiFramePart = false;
        this.m_refreshMultiFrameCache = true;
        this.m_modification = 0;
    }
    
    boolean setPartsFrom(@NotNull final Anm source, final TIntHashSet crcs) {
        this.addEquipment(source);
        ++this.m_modification;
        if (crcs == null) {
            return this.applyAllFrom(source, this.m_modification);
        }
        boolean changed = false;
        if (source.isReady()) {
            for (int j = 0, length = source.m_spriteDefinitions.length; j < length; ++j) {
                final SpriteDefinition spriteDefinition = source.m_spriteDefinitions[j];
                final int nameCRC = spriteDefinition.m_nameCRC;
                if (nameCRC != 0 && crcs.contains(spriteDefinition.m_baseNameCRC)) {
                    this.put(nameCRC, new EquipementDef(nameCRC, source, spriteDefinition, this.m_modification));
                    changed = true;
                    this.removeToSet(spriteDefinition.m_baseNameCRC, this.m_modification);
                }
            }
        }
        else {
            for (final int crc : crcs) {
                this.m_partsToSet.add(new EquipementDef(crc, source, null, this.m_modification));
            }
        }
        return changed;
    }
    
    private void removeToSet(final int basenameCRC, final int modification) {
        for (int i = this.m_partsToSet.size() - 1; i >= 0; --i) {
            final EquipementDef def = this.m_partsToSet.get(i);
            if (def.m_crc == basenameCRC && def.m_modification < modification) {
                this.m_partsToSet.remove(i);
            }
        }
    }
    
    boolean removePartsFrom(final Anm source, final TIntHashSet crcs) {
        boolean needUpdate = false;
        final TIntObjectIterator<EquipementDef> iterator = this.m_partsSetted.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            final EquipementDef def = iterator.value();
            if (crcs == null) {
                if (def.m_anm != source) {
                    continue;
                }
                iterator.remove();
                needUpdate = true;
            }
            else {
                if (!crcs.contains(def.m_spriteDef.m_baseNameCRC) || def.m_anm != source) {
                    continue;
                }
                iterator.remove();
                needUpdate = true;
            }
        }
        int i = 0;
        while (i < this.m_partsToSet.size()) {
            final EquipementDef def2 = this.m_partsToSet.get(i);
            if (crcs == null) {
                if (def2.m_anm == source) {
                    this.m_partsToSet.remove(i);
                    needUpdate = true;
                }
                else {
                    ++i;
                }
            }
            else if (crcs.contains(def2.m_crc) && def2.m_anm == source) {
                this.m_partsToSet.remove(i);
                needUpdate = true;
            }
            else {
                ++i;
            }
        }
        this.m_refreshHideCache = true;
        this.m_refreshMultiFrameCache = true;
        return needUpdate && this.updateUsage();
    }
    
    boolean update() {
        boolean updateFrame = false;
        int i = 0;
        while (i < this.m_partsToSet.size()) {
            final EquipementDef equipementDef = this.m_partsToSet.get(i);
            final int crc = equipementDef.m_crc;
            final Anm equipment = equipementDef.m_anm;
            if (equipment.isReady()) {
                updateFrame = true;
                boolean added = false;
                if (crc == 0) {
                    this.applyAllFrom(equipment, equipementDef.m_modification);
                }
                else {
                    for (int j = 0, length = equipment.m_spriteDefinitions.length; j < length; ++j) {
                        final SpriteDefinition spriteDefinition = equipment.m_spriteDefinitions[j];
                        if (spriteDefinition != null) {
                            if (spriteDefinition.m_nameCRC != 0 && spriteDefinition.m_baseNameCRC == crc) {
                                added = true;
                                this.put(spriteDefinition.m_nameCRC, new EquipementDef(crc, equipment, spriteDefinition, equipementDef.m_modification));
                            }
                        }
                    }
                }
                this.m_partsToSet.remove(equipementDef);
                if (!added) {
                    continue;
                }
                this.removeToSet(crc, equipementDef.m_modification);
            }
            else if (equipment.asyncLoadFailed()) {
                this.m_partsToSet.remove(i);
            }
            else {
                ++i;
            }
        }
        if (this.m_partsToSet.isEmpty()) {
            this.m_modification = 0;
        }
        return updateFrame;
    }
    
    private void put(final int nameCRC, final EquipementDef def) {
        this.m_partsSetted.put(nameCRC, def);
        this.m_refreshHideCache = true;
        this.m_refreshMultiFrameCache = true;
    }
    
    private void addEquipment(@NotNull final Anm equipment) {
        for (int numEquipments = this.m_used.size(), i = 0; i < numEquipments; ++i) {
            if (this.m_used.get(i) == equipment) {
                return;
            }
        }
        equipment.addReference();
        this.m_used.add(equipment);
    }
    
    private static void removeReference(@NotNull final Anm anm) {
        assert anm.getNumReferences() > 0;
        anm.removeReference();
    }
    
    private boolean updateUsage() {
        boolean changed = false;
        int i = 0;
        while (i < this.m_used.size()) {
            final Anm equipment = this.m_used.get(i);
            if (this.isUsing(equipment)) {
                ++i;
            }
            else {
                this.m_used.remove(i);
                removeReference(equipment);
                changed = true;
            }
        }
        return changed;
    }
    
    private boolean isUsing(final Anm equipment) {
        final TIntObjectIterator<EquipementDef> iterator = this.m_partsSetted.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            if (iterator.value().m_anm == equipment) {
                return true;
            }
        }
        for (int numEquipmentsToSet = this.m_partsToSet.size(), j = 0; j < numEquipmentsToSet; ++j) {
            if (this.m_partsToSet.get(j).m_anm == equipment) {
                return true;
            }
        }
        return false;
    }
    
    boolean applyAllFrom(final Anm equipment, final int modification) {
        ++this.m_modification;
        boolean changed = false;
        if (equipment.isReady()) {
            for (int j = 0, length = equipment.m_spriteDefinitions.length; j < length; ++j) {
                final SpriteDefinition spriteDefinition = equipment.m_spriteDefinitions[j];
                final int nameCRC = spriteDefinition.m_nameCRC;
                if (spriteDefinition.m_name != null) {
                    final EquipementDef old = this.m_partsSetted.get(nameCRC);
                    if (old == null || old.m_modification < modification) {
                        this.put(nameCRC, new EquipementDef(nameCRC, equipment, spriteDefinition, this.m_modification));
                    }
                    this.removeToSet(spriteDefinition.m_baseNameCRC, this.m_modification);
                }
                changed = true;
            }
        }
        else {
            this.m_partsToSet.add(new EquipementDef(0, equipment, null, this.m_modification));
        }
        return changed;
    }
    
    PartsHelper duplicate() {
        final PartsHelper result = new PartsHelper();
        final int numEquipments = this.m_used.size();
        result.m_used.ensureCapacity(numEquipments);
        for (int i = 0; i < numEquipments; ++i) {
            final Anm equipment = this.m_used.get(i);
            equipment.addReference();
            result.m_used.add(equipment);
        }
        final int numEquipmentToSet = this.m_partsToSet.size();
        result.m_partsToSet.ensureCapacity(numEquipmentToSet);
        for (int j = 0; j < numEquipmentToSet; ++j) {
            result.m_partsToSet.add(new EquipementDef(this.m_partsToSet.get(j)));
        }
        final TIntObjectIterator<EquipementDef> iterator = this.m_partsSetted.iterator();
        result.m_partsSetted.ensureCapacity(this.m_partsSetted.size());
        while (iterator.hasNext()) {
            iterator.advance();
            result.m_partsSetted.put(iterator.key(), iterator.value());
        }
        result.m_modification = this.m_modification;
        result.m_refreshHideCache = true;
        result.m_refreshMultiFrameCache = true;
        return result;
    }
    
    static {
        NULL = new NullPartsHelper();
    }
    
    private static class NullPartsHelper extends PartsHelper
    {
        @Override
        public void clear() {
            assert !this.hasModification();
            super.clear();
        }
        
        @Override
        boolean applyAllFrom(final Anm equipment, final int modification) {
            assert false : "on ne peut pas faire d'op\u00e9ration sur le NULL";
            return false;
        }
        
        public boolean setPartsFrom(@NotNull final Anm source, final TIntHashSet crcs) {
            assert false : "on ne peut pas faire d'op\u00e9ration sur le NULL";
            return false;
        }
        
        @Override
        PartsHelper duplicate() {
            return this;
        }
    }
    
    private static class MultiFramePartProcedure implements TObjectProcedure<EquipementDef>
    {
        @Override
        public boolean execute(final EquipementDef object) {
            return object.m_spriteDef.getFrameCount() <= 1;
        }
    }
    
    private class RefreshHideCacheProcedure implements TObjectProcedure<EquipementDef>
    {
        @Override
        public boolean execute(final EquipementDef object) {
            final CanHidePart part = object.getCanHidePart();
            if (part != null && !PartsHelper.this.m_canHideParts.contains(part)) {
                PartsHelper.this.m_canHideParts.add(part);
            }
            return true;
        }
    }
}
