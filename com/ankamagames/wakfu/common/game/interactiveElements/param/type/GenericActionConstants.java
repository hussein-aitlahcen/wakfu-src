package com.ankamagames.wakfu.common.game.interactiveElements.param.type;

import com.ankamagames.framework.external.*;

public enum GenericActionConstants implements ExportableEnum, Parameterized
{
    PLAY_SCRIPT(1, (ParameterListSet)GenericActionParameters.PLAY_LUA, "Permet de jouer un Script"), 
    APPLY_STATE(2, (ParameterListSet)GenericActionParameters.APPLY_STATE, "Applique un \u00e9tat sur le joueur"), 
    TELEPORT(3, (ParameterListSet)GenericActionParameters.TELEPORT, "T\u00e9l\u00e9porte le joueur"), 
    GIVE_ITEM(4, (ParameterListSet)GenericActionParameters.GIVE_RANDOM_ITEM_IN_LIST, "Donne un item au joueur"), 
    GIVE_KAMA(5, (ParameterListSet)GenericActionParameters.GIVE_KAMAS, "Donne des kamas au joueur"), 
    RESET_ACHIEVEMENT(6, (ParameterListSet)GenericActionParameters.RESET_ACHIEVEMENT, "Active un achievement/qu\u00eate"), 
    GIVE_EMOTE(7, (ParameterListSet)GenericActionParameters.GIVE_EMOTE, "Donne une emote au joueur"), 
    KROSMOZ_GAME_PLAY(8, (ParameterListSet)GenericActionParameters.KROSMOZ_GAME_PLAY, "Lance une application Krosmoz"), 
    RECUSTOM_CHARACTER(9, (ParameterListSet)GenericActionParameters.RECUSTOM_CHARACTER, "Lance une recustom de perso"), 
    LAUNCH_SCENARIO(10, (ParameterListSet)GenericActionParameters.LAUNCH_SCENARIO, "Lance un sc\u00e9nario"), 
    OPEN_MERCENARY_DIALOG(11, (ParameterListSet)GenericActionParameters.MERCENARY_DIALOG, "Ouvre le comptoir de mercenaire");
    
    private final byte m_id;
    private final String m_label;
    private final ParameterListSet m_parameter;
    
    private GenericActionConstants(final int id, final ParameterListSet parameterListSet, final String label) {
        this.m_id = (byte)id;
        this.m_label = label;
        this.m_parameter = parameterListSet;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    public static GenericActionConstants getFromId(final int action_id) {
        for (final GenericActionConstants constants : values()) {
            if (constants.m_id == action_id) {
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
}
