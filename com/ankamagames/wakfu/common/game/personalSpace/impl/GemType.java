package com.ankamagames.wakfu.common.game.personalSpace.impl;

import com.ankamagames.framework.external.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;

public enum GemType implements ExportableEnum
{
    GEM_ID_MERCHANT((byte)0, "gemme de salle marchande", new int[] { 4262, 11098 }), 
    GEM_ID_DECORATION((byte)1, "gemme de salle de decoration", new int[] { 4263, 11100 }), 
    GEM_ID_CRAFT((byte)2, "gemme de salle de craft", new int[] { 4264 }), 
    GEM_ID_GUILD((byte)3, "gemme de salle de guilde", new int[] { 4265 }), 
    GEM_ID_RESOURCES((byte)4, "gemme de salle jardin", new int[] { 4266, 11099 }), 
    GEM_ID_FARMING((byte)5, "gemme de salle ?", new int[] { 4267 }), 
    GEM_ID_DUNGEON((byte)6, "gemme de salle donjon", new int[] { 4268 }), 
    GEM_ID_LIVING_ROOM((byte)7, "gemme de salle habitat", new int[] { 8023 });
    
    private static final TIntObjectHashMap<GemType> BY_REF_ID;
    public final byte idx;
    public final byte mask;
    private final int[] m_itemReferenceId;
    private final String m_comment;
    
    private GemType(final byte id, final String comment, final int[] itemReferenceId) {
        this.idx = id;
        this.mask = MathHelper.ensureByte(1 << id);
        this.m_itemReferenceId = itemReferenceId;
        this.m_comment = comment;
    }
    
    public int getItemReferenceId() {
        return this.m_itemReferenceId[0];
    }
    
    @Override
    public String getEnumId() {
        return Integer.toString(this.m_itemReferenceId[0]);
    }
    
    @Override
    public String getEnumLabel() {
        return this.name();
    }
    
    @Override
    public String getEnumComment() {
        return this.m_comment;
    }
    
    public static GemType getFromItemReferenceId(final int itemReferenceId) {
        return GemType.BY_REF_ID.get(itemReferenceId);
    }
    
    public static GemType getDefaultGemType() {
        return GemType.GEM_ID_MERCHANT;
    }
    
    static {
        BY_REF_ID = new TIntObjectHashMap<GemType>();
        for (final GemType gemType : values()) {
            for (final int refId : gemType.m_itemReferenceId) {
                GemType.BY_REF_ID.put(refId, gemType);
            }
        }
    }
}
