package com.ankamagames.wakfu.client.core.utils;

import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class FamilyZoneComputer
{
    public static void execute(final EventType eventType, final Select selecter) {
        selecter.fill();
        selecter.computeRandom();
        WakfuSoundManager.getInstance().onEvent(new FamilySoundEvent(eventType, null, selecter.getSelectedFamilyId(), selecter.getQuantity()));
    }
    
    public abstract static class Select
    {
        private final TIntArrayList familyIds;
        private final TShortArrayList usage;
        private int m_selectedFamilyId;
        private short m_quantity;
        
        public Select() {
            super();
            this.familyIds = new TIntArrayList();
            this.usage = new TShortArrayList();
            this.m_selectedFamilyId = -1;
            this.m_quantity = 0;
        }
        
        protected abstract void fill();
        
        protected final void add(final int familyId) {
            int index = this.familyIds.indexOf(familyId);
            if (index == -1) {
                index = this.familyIds.size();
                this.familyIds.add(familyId);
                this.usage.add((short)0);
            }
            this.usage.setQuick(index, (short)(this.usage.getQuick(index) + 1));
        }
        
        private int getSelectedFamilyId() {
            return this.m_selectedFamilyId;
        }
        
        private short getQuantity() {
            return this.m_quantity;
        }
        
        private void compute() {
            this.m_quantity = 0;
            this.m_selectedFamilyId = -1;
            for (int i = 0, size = this.usage.size(); i < size; ++i) {
                final short maxUsage = this.usage.getQuick(i);
                if (maxUsage > this.m_quantity) {
                    this.m_quantity = maxUsage;
                    this.m_selectedFamilyId = this.familyIds.getQuick(i);
                }
            }
        }
        
        private void computeRandom() {
            this.m_quantity = 0;
            this.m_selectedFamilyId = -1;
            int total = 0;
            final int count = this.usage.size();
            for (int i = 0; i < count; ++i) {
                total += this.usage.getQuick(i);
            }
            int pos = MathHelper.random(total);
            for (int j = 0; j < count; ++j) {
                pos -= this.usage.getQuick(j);
                if (pos < 0) {
                    this.m_selectedFamilyId = this.familyIds.getQuick(j);
                    this.m_quantity = this.usage.getQuick(j);
                    return;
                }
            }
        }
    }
}
