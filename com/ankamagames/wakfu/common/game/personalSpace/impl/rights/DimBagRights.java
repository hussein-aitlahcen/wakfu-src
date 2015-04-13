package com.ankamagames.wakfu.common.game.personalSpace.impl.rights;

import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import gnu.trove.*;

public class DimBagRights
{
    public static final int MAX_INDIVIDUAL_ENTRIES = 25;
    private final TByteObjectHashMap<DimBagGroupRight> m_groupRights;
    private final TLongObjectHashMap<DimBagIndividualRight> m_individualRights;
    
    public DimBagRights() {
        super();
        this.m_groupRights = new TByteObjectHashMap<DimBagGroupRight>();
        this.m_individualRights = new TLongObjectHashMap<DimBagIndividualRight>();
    }
    
    public DimBagGroupRight getGroupRight(final GroupType groupType) {
        return this.m_groupRights.get(groupType.idx);
    }
    
    public DimBagIndividualRight getIndividualRight(final long rightId) {
        return this.m_individualRights.get(rightId);
    }
    
    public void addGroupRight(final DimBagGroupRight right) {
        this.m_groupRights.put(right.getGroupType().idx, right);
    }
    
    public void addIndividualRight(final DimBagIndividualRight right) {
        this.m_individualRights.put(right.getId(), right);
    }
    
    public void removeIndividualRight(final long id) {
        this.m_individualRights.remove(id);
    }
    
    public void toRaw(final RawDimensionalBagPermissions raw) {
        this.toRawGroupRights(raw);
        this.toRawIndividualRights(raw);
    }
    
    public void fromRaw(final RawDimensionalBagPermissions raw) {
        this.fromRawGroupRights(raw);
        this.fromRawIndividualRights(raw);
    }
    
    private void toRawGroupRights(final RawDimensionalBagPermissions raw) {
        raw.groupEntries.clear();
        final TByteObjectIterator<DimBagGroupRight> it = this.m_groupRights.iterator();
        while (it.hasNext()) {
            it.advance();
            final RawDimensionalBagPermissions.GroupEntry entry = new RawDimensionalBagPermissions.GroupEntry();
            it.value().toRaw(entry.entry);
            raw.groupEntries.add(entry);
        }
    }
    
    private void toRawIndividualRights(final RawDimensionalBagPermissions raw) {
        raw.individualEntries.clear();
        final TLongObjectIterator<DimBagIndividualRight> it = this.m_individualRights.iterator();
        while (it.hasNext()) {
            it.advance();
            final RawDimensionalBagPermissions.IndividualEntry entry = new RawDimensionalBagPermissions.IndividualEntry();
            it.value().toRaw(entry.entry);
            raw.individualEntries.add(entry);
        }
    }
    
    private void fromRawGroupRights(final RawDimensionalBagPermissions raw) {
        this.m_groupRights.clear();
        for (int i = 0; i < raw.groupEntries.size(); ++i) {
            final DimBagGroupRight right = new DimBagGroupRight();
            right.fromRaw(raw.groupEntries.get(i).entry);
            this.addGroupRight(right);
        }
    }
    
    private void fromRawIndividualRights(final RawDimensionalBagPermissions raw) {
        this.m_individualRights.clear();
        for (int i = 0; i < raw.individualEntries.size(); ++i) {
            final DimBagIndividualRight right = new DimBagIndividualRight();
            right.fromRaw(raw.individualEntries.get(i).entry);
            this.addIndividualRight(right);
        }
    }
    
    public SortedList<DimBagIndividualRight> getIndividualsList() {
        final SortedList<DimBagIndividualRight> result = new SortedList<DimBagIndividualRight>(new DimBagIndividualRightComparator());
        final TLongObjectIterator<DimBagIndividualRight> it = this.m_individualRights.iterator();
        while (it.hasNext()) {
            it.advance();
            result.add(it.value());
        }
        return result;
    }
    
    @Override
    public String toString() {
        return "DimBagRights{m_groupRights=" + this.m_groupRights.size() + ", m_individualRights=" + this.m_individualRights.size() + '}';
    }
    
    private static class DimBagIndividualRightComparator implements Comparator<DimBagIndividualRight>
    {
        @Override
        public int compare(final DimBagIndividualRight o1, final DimBagIndividualRight o2) {
            final String name1 = o1.getName().toUpperCase();
            final String name2 = o2.getName().toUpperCase();
            return name1.compareTo(name2);
        }
    }
}
