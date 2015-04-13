package com.ankamagames.wakfu.common.game.characterInfo.action;

import com.ankamagames.framework.external.*;

public enum MonsterActionConstants implements ExportableEnum, Parameterized
{
    DO_NOTHING(1, MonsterActionParameters.DO_NOTHING, "Ne fait rien, permet juste le lancement d'un Script"), 
    SET_COMPORTMENT(2, MonsterActionParameters.SET_COMPORTMENT, "Permet de forcer le comportement d'un monstre"), 
    DESTROY(3, MonsterActionParameters.DESTROY, "Detrui le monstre"), 
    START_DIALOG(4, MonsterActionParameters.START_DIALOG, "D\u00e9marre un dialogue", true), 
    MANAGE_HAVEN_WORLD(5, MonsterActionParameters.MANAGE_HAVEN_WORLD, "Ouvre l'\u00e9diteur de Havre-monde");
    
    private final byte m_id;
    private final String m_label;
    private final ParameterListSet m_parameter;
    private final boolean m_canBeTriggeredWhenBusy;
    
    private MonsterActionConstants(final int id, final ParameterListSet parameterListSet, final String label) {
        this(id, parameterListSet, label, false);
    }
    
    private MonsterActionConstants(final int id, final ParameterListSet parameterListSet, final String label, final boolean canBeTriggeredWhenBusy) {
        this.m_id = (byte)id;
        this.m_label = label;
        this.m_parameter = parameterListSet;
        this.m_canBeTriggeredWhenBusy = canBeTriggeredWhenBusy;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    public static MonsterActionConstants getFromId(final int typeId) {
        for (final MonsterActionConstants constants : values()) {
            if (constants.m_id == typeId) {
                return constants;
            }
        }
        return null;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return this.m_parameter;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public boolean isCanBeTriggeredWhenBusy() {
        return this.m_canBeTriggeredWhenBusy;
    }
}
