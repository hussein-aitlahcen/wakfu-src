package com.ankamagames.framework.graphics.engine.Anm2;

import gnu.trove.*;

public class HidePartsHelper
{
    public static final HidePartsHelper NULL;
    private final TIntProcedure m_addValueProcedure;
    private final TIntHashSet m_all;
    private final TIntHashSet m_equipmentHide;
    private final TIntHashSet m_hide;
    private final TIntHashSet m_hideAllBut;
    private boolean m_useHideAllBut;
    private boolean m_needUpdate;
    
    public HidePartsHelper() {
        super();
        this.m_addValueProcedure = new TIntProcedure() {
            @Override
            public boolean execute(final int value) {
                HidePartsHelper.this.m_all.add(value);
                return true;
            }
        };
        this.m_all = new TIntHashSet();
        this.m_equipmentHide = new TIntHashSet();
        this.m_hide = new TIntHashSet();
        this.m_hideAllBut = new TIntHashSet();
        this.m_useHideAllBut = false;
        this.m_needUpdate = true;
    }
    
    public void hideEquipment(final int crc) {
        this.m_needUpdate |= this.m_equipmentHide.add(crc);
    }
    
    public void hide(final int crc) {
        this.m_needUpdate |= this.m_hide.add(crc);
    }
    
    public void unhide(final int crc) {
        this.m_needUpdate |= this.m_hide.remove(crc);
    }
    
    public void hideAllBut(final int crc) {
        this.m_needUpdate |= this.m_hideAllBut.add(crc);
        this.m_useHideAllBut = true;
    }
    
    public void unhideAllBut(final int crc) {
        this.m_needUpdate |= this.m_hideAllBut.remove(crc);
        this.m_useHideAllBut = !this.m_hideAllBut.isEmpty();
    }
    
    public void update() {
        if (!this.m_needUpdate) {
            return;
        }
        this.m_all.clear();
        this.m_equipmentHide.forEach(this.m_addValueProcedure);
        this.m_hide.forEach(this.m_addValueProcedure);
        this.m_needUpdate = false;
    }
    
    public void clear() {
        this.m_all.clear();
        this.m_hide.clear();
        this.m_hideAllBut.clear();
        this.m_useHideAllBut = false;
        this.m_equipmentHide.clear();
    }
    
    boolean contains(final int crc) {
        assert !this.m_needUpdate;
        if (this.m_useHideAllBut) {
            return !this.m_hideAllBut.contains(crc);
        }
        return !this.m_all.isEmpty() && this.m_all.contains(crc);
    }
    
    void clearEquipement() {
        this.m_equipmentHide.clear();
        this.m_needUpdate = true;
    }
    
    public HidePartsHelper duplicate() {
        final HidePartsHelper result = new HidePartsHelper();
        result.m_all.addAll(this.m_all.toArray());
        result.m_equipmentHide.addAll(this.m_equipmentHide.toArray());
        result.m_hide.addAll(this.m_hide.toArray());
        result.m_needUpdate = this.m_needUpdate;
        return result;
    }
    
    static {
        NULL = new HidePartsHelper() {
            @Override
            public void hideEquipment(final int crc) {
                throw new UnsupportedOperationException("Ne devrait pas \u00eatre appel\u00e9");
            }
            
            @Override
            public void hide(final int crc) {
                throw new UnsupportedOperationException("Ne devrait pas \u00eatre appel\u00e9");
            }
            
            @Override
            public HidePartsHelper duplicate() {
                return this;
            }
        };
    }
}
