package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.external.*;

public enum EffectAreaType implements ExportableEnum, Parameterized
{
    GLYPH(0, "GLYPH", AbstractGlyphEffectArea.PARAMETER_LIST_SET), 
    HOUR(1, "HOUR", AbstractHourEffectArea.PARAMETER_LIST_SET), 
    BEACON(2, "BEACON", AbstractBeaconEffectArea.PARAMETER_LIST_SET), 
    TRAP(3, "TRAP", AbstractTrapEffectArea.PARAMETER_LIST_SET), 
    AURA(4, "AURA", AbstractAuraEffectArea.PARAMETER_LIST_SET), 
    BATTLEGROUND_BORDER(5, "BATTLEGROUND_BORDER", AbstractBattlegroundBorderEffectArea.PARAMETER_LIST_SET), 
    SIMPLE(6, "SIMPLE", AbstractSimpleEffectArea.PARAMETER_LIST_SET), 
    WALL(7, "WALL", AbstractWallEffectArea.PARAMETER_LIST_SET), 
    WARP(8, "WARP", AbstractWarpEffectArea.PARAMETER_LIST_SET), 
    BOMB(9, "BOMB", AbstractBombEffectArea.PARAMETER_LIST_SET), 
    SPELL_TUNNEL_MARKER(10, "TUNNEL_MARKER", AbstractSimpleEffectArea.PARAMETER_LIST_SET), 
    SPELL_TUNNEL(11, "SPELL_TUNNEL", AbstractChangeSpellTargetCellArea.PARAMETER_LIST_SET), 
    ENUTROF_DEPOSIT(12, "ENUTROF_DEPOSIT", AbstractDepositEffectArea.PARAMETER_LIST_SET), 
    LOOT_AREA(13, "LOOT_AREA", AbstractLootEffectArea.PARAMETER_LIST_SET), 
    SIMPLE_WITH_REM(14, "SIMPLE_WITH_REM", AbstractWithREMArea.PARAMETER_LIST_SET), 
    FAKE_FIGHTER(15, "FAKE_FIGHTER", AbstractFakeFighterEffectArea.PARAMETER_LIST_SET), 
    BARREL(16, "BARREL", AbstractBarrelEffectArea.PARAMETER_LIST_SET), 
    FECA_GLYPH(17, "FECA_GLYPH", AbstractFecaGlyphEffectArea.PARAMETER_LIST_SET), 
    BOMB_COMBO(18, "BOMB_COMBO", AbstractBombComboEffectArea.PARAMETER_LIST_SET), 
    GATE(19, "GATE", AbstractGateEffectArea.PARAMETER_LIST_SET);
    
    private final int m_typeId;
    private final String m_label;
    private final EffectAreaParameterListSet m_parameters;
    
    private EffectAreaType(final int id, final String label, final EffectAreaParameterListSet parameters) {
        this.m_typeId = id;
        this.m_label = label;
        this.m_parameters = parameters;
    }
    
    public int getTypeId() {
        return this.m_typeId;
    }
    
    @Override
    public String getEnumId() {
        return this.m_label;
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return this.m_parameters;
    }
    
    public static EffectAreaType getTypeFromId(final int id) {
        for (final EffectAreaType type : values()) {
            if (type.getTypeId() == id) {
                return type;
            }
        }
        return null;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
}
