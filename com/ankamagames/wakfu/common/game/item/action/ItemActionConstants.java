package com.ankamagames.wakfu.common.game.item.action;

import com.ankamagames.framework.external.*;

public enum ItemActionConstants implements ExportableEnum, Parameterized
{
    DISASSEMBLE_ITEM(1, (ParameterListSet)ItemActionParameters.ITEM_DISASSEMBLE, "Detruit un item  pour r\u00e9cup\u00e9rer des poudres", false), 
    SEED_ACTION(2, (ParameterListSet)ItemActionParameters.SEED_ACTION, "Permet la plantation d'un item", false), 
    PLAY_SCRIPT(3, (ParameterListSet)ItemActionParameters.PLAY_LUA, "Permet de jouer un Script", false), 
    DEPLOY_ELEMENT(5, (ParameterListSet)ItemActionParameters.DEPLOY_ELEMENT, "Permet de d\u00e9ployer un \u00e9l\u00e9ment interactif", false), 
    SPAWN_MONSTER(6, (ParameterListSet)ItemActionParameters.SPAWN_MONSTER, "Permet de spawn un monstre \u00e0 cot\u00e9 du joueur", false), 
    START_SCENARIO(7, (ParameterListSet)ItemActionParameters.START_SCENARIO, "Permet de lancer un sc\u00e9nario sur un joueur", false), 
    GIVE_KAMAS(8, (ParameterListSet)ItemActionParameters.GIVE_KAMAS, "Donne des kamas au joueur", false), 
    TELEPORT(9, (ParameterListSet)ItemActionParameters.TELEPORT, "T\u00e9l\u00e9porte un joueur", false), 
    GIVE_TITLE(10, (ParameterListSet)ItemActionParameters.GIVE_TITLE, "Donne un titre \u00e0 un joueur", false), 
    OPEN_BACKGROUND_DISPLAY(11, (ParameterListSet)ItemActionParameters.OPEN_BACKGROUND_DISPLAY, "Ouvre une interface background", false), 
    OPEN_PASSPORT(12, (ParameterListSet)ItemActionParameters.OPEN_PASSPORT, "Affiche le passeport", false), 
    LEARN_EMOTE(13, (ParameterListSet)ItemActionParameters.LEARN_EMOTE, "Apprend une Emote", false), 
    ADD_WORLD_POSITION_MARKER(15, (ParameterListSet)ItemActionParameters.ADD_WORLD_POSITION_MARKER, "Ajoute une boussole", false), 
    REDUCE_DEAD_STATE(16, (ParameterListSet)ItemActionParameters.REDUCE_DEAD_STATE, "r\u00e9duit de 1 les Malus de mort", false), 
    PLAY_EMOTE(17, (ParameterListSet)ItemActionParameters.PLAY_EMOTE, "Joue une emote", false), 
    ACTIVATE_ACHIEVEMENT(18, (ParameterListSet)ItemActionParameters.ACTIVATE_ACHIEVEMENT_LIST, "Active un exploit", false), 
    FOLLOW_ACHIEVEMENT(19, (ParameterListSet)ItemActionParameters.FOLLOW_ACHIEVEMENT_LIST, "Suit un exploit", false), 
    MERGE_ITEMS(20, (ParameterListSet)ItemActionParameters.MERGE_ITEMS_LIST, "Fusionne des items", false), 
    LEARN_DIMENSIONAL_BAG_VIEW(21, (ParameterListSet)ItemActionParameters.LEARN_DIMENSIONAL_BAG_VIEW, "Apprend une custom de Havre-Sac", false), 
    GIVE_LEVELS_TO_ALL_SPELL_BRANCH(22, (ParameterListSet)ItemActionParameters.GIVE_LEVELS_TO_ALL_SPELL_BRANCH, "Ajoute X niveaux \u00e0 tous les sorts \u00e9l\u00e9mentaires", false), 
    GIVE_XP(23, (ParameterListSet)ItemActionParameters.GIVE_XP, "Donne de l'xp au joueur", false), 
    RESET_ACHIEVEMENT(24, (ParameterListSet)ItemActionParameters.RESET_ACHIEVEMENT, "R\u00e9initialise un achievement", false), 
    GIVE_LEVELS_TO_SPELL_BRANCH(25, (ParameterListSet)ItemActionParameters.GIVE_LEVELS_TO_SPELL_BRANCH, "Ajoute X niveaux aux sorts d'une branche \u00e9l\u00e9mentaire", false), 
    SPLIT_ITEM_SET(26, (ParameterListSet)ItemActionParameters.SPLIT_ITEM_SET, "S\u00e9pare un objet \u00e9l\u00e9ments d'une panoplie", true), 
    GIVE_APTITUDE_LEVELS(27, (ParameterListSet)ItemActionParameters.GIVE_APTITUDE_LEVELS, "Donne des niveaux \u00e0 une aptitude pr\u00e9cise", false), 
    GIVE_RANDOM_ITEM_IN_LIST(28, (ParameterListSet)ItemActionParameters.GIVE_RANDOM_ITEM_IN_LIST, "Donne un item dans la liste", false), 
    SPELL_RESTAT(29, (ParameterListSet)ItemActionParameters.SPELL_RESTAT, "R\u00e9initialise les sorts \u00e9l\u00e9mentaires", false), 
    APTITUDE_RESTAT(30, (ParameterListSet)ItemActionParameters.APTITUDE_RESTAT, "R\u00e9initialise les aptitudes", false), 
    TP_TO_RESPAWN_POINT(31, (ParameterListSet)ItemActionParameters.TP_TO_RESPAWN_POINT, "Retour au point de respawn", false), 
    CONSUME_KROSMOZ_FIGURE(32, (ParameterListSet)ItemActionParameters.CONSUME_KROSMOZ_FIGURE, "Consomme une figurine Krosmaster", false), 
    ACTIVATE_RESTAT(33, (ParameterListSet)ItemActionParameters.ACTIVATE_RESTAT, "Active un restat", false), 
    SEARCH_TREASURE(34, (ParameterListSet)ItemActionParameters.SEARCH_TREASURE, "Active la recherche de tr\u00e9sor", false), 
    REMOVE_LAST_GEM(35, (ParameterListSet)ItemActionParameters.REMOVE_LAST_GEM, "Retire la derniere gemme d'un item", false), 
    GIVE_ITEMS(36, (ParameterListSet)ItemActionParameters.GIVE_ITEMS, "Donne des items", false), 
    CHANGE_NATION(37, (ParameterListSet)ItemActionParameters.CHANGE_NATION, "Change de nation", false), 
    KILL_MONSTERS_IN_RADIUS(38, (ParameterListSet)ItemActionParameters.KILL_MONSTERS_IN_RADIUS, "Tue les monstres dans un rayon", false), 
    COMPANION_ACTIVATION(39, (ParameterListSet)ItemActionParameters.COMPANION_ACTIVATION, "Active un compagnon", false), 
    CHANGE_GEM_TYPE(40, (ParameterListSet)ItemActionParameters.CHANGE_GEM, "Change le type de la gemme", false), 
    EXTENDS_RENT_DURATION(41, (ParameterListSet)ItemActionParameters.EXTENDS_RENT_DURATION, "Allonge la dur\u00e9e de location d'un item", false), 
    ALL_SPELL_RESTAT(42, (ParameterListSet)ItemActionParameters.ALL_SPELL_RESTAT, "Reinitialise les sorts \u00e9l\u00e9mentaires et les sorts passifs", false), 
    RECUSTOM(43, (ParameterListSet)ItemActionParameters.RECUSTOM, "Lance la recustom d'un personnage", false), 
    COMMON_APTITUDE_RESTAT_ACTION(44, (ParameterListSet)ItemActionParameters.COMMON_APTITUDE_RESTAT, "Reinitialise les aptitudes de carac", false), 
    INSTANCE_SPEAKER(45, (ParameterListSet)ItemActionParameters.INSTANCE_SPEAKER, "Diffuse un message aux joueurs pr\u00e9sents dans l'instance", false), 
    PET_XP(46, (ParameterListSet)ItemActionParameters.PET_XP, "Rajoute des niveaux aux pets", false), 
    PET_HP(47, (ParameterListSet)ItemActionParameters.PET_HP, "Heal un pet", false), 
    LEARN_COSTUME(48, (ParameterListSet)ItemActionParameters.LEARN_COSTUME, "Apprend cet objet en tant que costume", false), 
    LEARN_PET_EQUIPMENT(49, (ParameterListSet)ItemActionParameters.LEARN_COSTUME, "Apprend cet objet en tant qu'equipement de familier", false), 
    REROLL_ELEMENTS(50, (ParameterListSet)ItemActionParameters.REROLL_ELEMENTS, "Roll de nouveaux \u00e9l\u00e9ments (effets \u00e0 \u00e9l\u00e9ts variables) pour cet item", false);
    
    private final byte m_id;
    private final String m_label;
    private final ParameterListSet m_parameter;
    private final boolean m_canBeUsedDuringOccupation;
    
    private ItemActionConstants(final int id, final ParameterListSet parameterListSet, final String label, final boolean canBeUsedDuringOccupation) {
        this.m_id = (byte)id;
        this.m_label = label;
        this.m_parameter = parameterListSet;
        this.m_canBeUsedDuringOccupation = canBeUsedDuringOccupation;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    public boolean canBeUsedDuringOccupation() {
        return this.m_canBeUsedDuringOccupation;
    }
    
    public static ItemActionConstants getFromId(final int action_id) {
        for (final ItemActionConstants constants : values()) {
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
