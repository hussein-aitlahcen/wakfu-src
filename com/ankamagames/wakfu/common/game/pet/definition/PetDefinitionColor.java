package com.ankamagames.wakfu.common.game.pet.definition;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.framework.graphics.image.*;
import gnu.trove.*;

public class PetDefinitionColor
{
    private final IntObjectLightWeightMap<Color> m_colorParts;
    
    public PetDefinitionColor() {
        super();
        this.m_colorParts = new IntObjectLightWeightMap<Color>();
    }
    
    void add(final int partIdx, final Color color) {
        this.m_colorParts.put(partIdx, color);
    }
    
    public void foreach(final TIntObjectProcedure<Color> procedure) {
        for (int i = 0, size = this.m_colorParts.size(); i < size; ++i) {
            if (!procedure.execute(this.m_colorParts.getQuickKey(i), this.m_colorParts.getQuickValue(i))) {
                return;
            }
        }
    }
    
    @Override
    public String toString() {
        return "PetDefinitionColor{m_colorParts=" + this.m_colorParts.size() + '}';
    }
}
