package com.ankamagames.wakfu.common.game.item.action;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.external.*;

public interface ItemActionParameters
{
    public static final ItemActionListSet MERGE_ITEMS_LIST = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Default", new Parameter[] { new ParserObjectParameter("nombre d'items source \u00e0 consommer", ParserType.NUMBER), new ParserObjectParameter("id de l'item r\u00e9sultat", ParserType.NUMBER) }), new DefaultParameterList("D\u00e9truit simplement les items sources (utile pour les sc\u00e9nars/quetes)", new Parameter[] { new ParserObjectParameter("nombre d'items source \u00e0 consommer", ParserType.NUMBER) }) });
    public static final ItemActionListSet FOLLOW_ACHIEVEMENT_LIST = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Default", new Parameter[] { new ParserObjectParameter("id de l'achievement \u00e0 suivre", ParserType.NUMBER) }) });
    public static final ItemActionListSet ACTIVATE_ACHIEVEMENT_LIST = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Default", new Parameter[] { new ParserObjectParameter("id de l'achievement \u00e0 activer", ParserType.NUMBER) }) });
    public static final ItemActionListSet PLAY_EMOTE = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("Id d'Emote", ParserType.NUMBER) }) });
    public static final ItemActionListSet REDUCE_DEAD_STATE = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Aucun", new Parameter[0]) });
    public static final ItemActionListSet ITEM_DISASSEMBLE = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Aucun", new Parameter[0]) });
    public static final ItemActionListSet SEED_ACTION = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Default", new Parameter[] { new ParserObjectParameter("Id du visuel", ParserType.NUMBER), new ParserObjectParameter("Id du m\u00e9tier", ParserType.NUMBER), new ParserObjectParameter("Niveau minimum", ParserType.NUMBER), new ParserObjectParameter("Id de ressource r\u00e9sultante", ParserType.NUMBER) }) });
    public static final ItemActionListSet PLAY_LUA = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Default", new Parameter[0]), new EffectAreaParameterList("Broadcast", new Parameter[] { new ParserObjectParameter("BroadCaster autour?", ParserType.BOOLEAN) }), new EffectAreaParameterList("Variable", new Parameter[] { new ParserObjectParameter("BroadCaster autour?", ParserType.BOOLEAN), new ParserObjectParameter("Nom de variable", ParserType.STRING), new ParserObjectParameter("Valeur de variable", ParserType.NUMBER) }) });
    public static final ItemActionListSet RIDE = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("Id de la monture", ParserType.NUMBER) }) });
    public static final ItemActionListSet DEPLOY_ELEMENT = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("Id du template d'\u00e9l\u00e9ment", ParserType.NUMBER) }) });
    public static final ItemActionListSet SPAWN_MONSTER = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("Id de la race du mob \u00e0 spawn", ParserType.NUMBER) }) });
    public static final ItemActionListSet START_SCENARIO = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("Id du sc\u00e9nario", ParserType.NUMBER) }) });
    public static final ItemActionListSet GIVE_KAMAS = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("NB Kamas", ParserType.NUMBER) }) });
    public static final ItemActionListSet TELEPORT = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("Position", ParserType.POSITION), new ParserObjectParameter("Instance ID", ParserType.NUMBER) }) });
    public static final ItemActionListSet GIVE_TITLE = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("Id du titre", ParserType.NUMBER) }) });
    public static final ItemActionListSet OPEN_BACKGROUND_DISPLAY = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Id simple", new Parameter[] { new ParserObjectParameter("Id du texte background", ParserType.NUMBER) }), new EffectAreaParameterList("Avec animation", new Parameter[] { new ParserObjectParameter("Id du texte background", ParserType.NUMBER), new ParserObjectParameter("animation", ParserType.STRING) }) });
    public static final ItemActionListSet OPEN_PASSPORT = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Aucun", new Parameter[0]) });
    public static final ItemActionListSet LEARN_EMOTE = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("Id d'Emote", ParserType.NUMBER) }) });
    public static final ItemActionListSet ADD_WORLD_POSITION_MARKER = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Position", new Parameter[] { new ParserObjectParameter("Position", ParserType.POSITION), new ParserObjectParameter("Instance ID", ParserType.NUMBER) }) });
    public static final ItemActionListSet LEARN_DIMENSIONAL_BAG_VIEW = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("Id de la custom de havre-sac", ParserType.NUMBER) }) });
    public static final ItemActionListSet GIVE_XP = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("XP", ParserType.NUMBER) }) });
    public static final ItemActionListSet RESET_ACHIEVEMENT = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("id de l'achievement", ParserType.NUMBER) }) });
    public static final ItemActionListSet SPLIT_ITEM_SET = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Default", new Parameter[] { new ParserObjectParameter("id de la panoplie \u00e0 cr\u00e9er", ParserType.NUMBER) }) });
    public static final ItemActionListSet GIVE_LEVELS_TO_SPELL_BRANCH = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("id de l'\u00e9l\u00e9ment", ParserType.NUMBER), new ParserObjectParameter("nombre de niveau \u00e0 ajouter", ParserType.NUMBER) }) });
    public static final ItemActionListSet GIVE_LEVELS_TO_ALL_SPELL_BRANCH = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("nombre de niveau \u00e0 ajouter", ParserType.NUMBER) }) });
    public static final ItemActionListSet GIVE_APTITUDE_LEVELS = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("1 aptitude", new Parameter[] { new ParserObjectParameter("id de l'aptitude", ParserType.NUMBER), new ParserObjectParameter("nombre de niveaux \u00e0 ajouter", ParserType.NUMBER) }), new EffectAreaParameterList("5 aptitudes", new Parameter[] { new ParserObjectParameter("id de l'aptitude 1", ParserType.NUMBER), new ParserObjectParameter("nombre de niveaux \u00e0 ajouter 1", ParserType.NUMBER), new ParserObjectParameter("id de l'aptitude 2", ParserType.NUMBER), new ParserObjectParameter("nombre de niveaux \u00e0 ajouter 2", ParserType.NUMBER), new ParserObjectParameter("id de l'aptitude 3", ParserType.NUMBER), new ParserObjectParameter("nombre de niveaux \u00e0 ajouter 3", ParserType.NUMBER), new ParserObjectParameter("id de l'aptitude 4", ParserType.NUMBER), new ParserObjectParameter("nombre de niveaux \u00e0 ajouter 4", ParserType.NUMBER), new ParserObjectParameter("id de l'aptitude 5", ParserType.NUMBER), new ParserObjectParameter("nombre de niveaux \u00e0 ajouter 5", ParserType.NUMBER) }) });
    public static final ItemActionListSet GIVE_RANDOM_ITEM_IN_LIST = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList(new Parameter[] { new ParserObjectParameter("id de la table de drop", ParserType.NUMBER) }) });
    public static final ItemActionListSet SPELL_RESTAT = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Aucun", new Parameter[0]) });
    public static final ItemActionListSet APTITUDE_RESTAT = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Aucun", new Parameter[0]) });
    public static final ItemActionListSet TP_TO_RESPAWN_POINT = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Aucun", new Parameter[0]) });
    public static final ItemActionListSet CONSUME_KROSMOZ_FIGURE = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Default", new Parameter[] { new ParserObjectParameter("id de la figurine \u00e0 consommer", ParserType.NUMBER), new ParserObjectParameter("Socle (5 = normal, 6 = rare)", ParserType.NUMBER) }) });
    public static final ItemActionListSet ACTIVATE_RESTAT = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Aucun", new Parameter[0]) });
    public static final ItemActionListSet REMOVE_LAST_GEM = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Aucun", new Parameter[0]) });
    public static final ItemActionListSet SEARCH_TREASURE = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Default", new Parameter[] { new ParserObjectParameter("Id du visuel", ParserType.NUMBER), new ParserObjectParameter("Dur\u00e9e en ms", ParserType.NUMBER) }) });
    public static final ItemActionListSet GIVE_ITEMS = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("1 objet", new Parameter[] { new ParserObjectParameter("Id de l'item", ParserType.NUMBER), new ParserObjectParameter("Quantit\u00e9", ParserType.NUMBER) }) });
    public static final ItemActionListSet CHANGE_NATION = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Nation fixe", new Parameter[] { new ParserObjectParameter("Id de la nation", ParserType.NUMBER) }) });
    public static final ItemActionListSet KILL_MONSTERS_IN_RADIUS = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Default", new Parameter[] { new ParserObjectParameter("Rayon (en cellules)", ParserType.NUMBER) }) });
    public static final ItemActionListSet COMPANION_ACTIVATION = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Default", new Parameter[] { new ParserObjectParameter("Id du compagnon", ParserType.NUMBER) }) });
    public static final ItemActionListSet CHANGE_GEM = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Default", new Parameter[] { new ParserObjectParameter("id de la m\u00e9ta gemme", ParserType.NUMBER), new ParserObjectParameter("id du type de gemme qu'on peut modifier (attack : 1, defense : 2, support : 3)", ParserType.NUMBER) }) });
    public static final ItemActionListSet EXTENDS_RENT_DURATION = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Default", new Parameter[] { new ParserObjectParameter("Type d'objet", ParserType.NUMBER), new ParserObjectParameter("Type de location", ParserType.NUMBER), new ParserObjectParameter("Extension de la dur\u00e9e", ParserType.NUMBER) }) });
    public static final ItemActionListSet ALL_SPELL_RESTAT = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Aucun", new Parameter[0]) });
    public static final ItemActionListSet RECUSTOM = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Default", new Parameter[] { new ParserObjectParameter("Type de recustom (0 : complete, 1 : breed, 2 : sexe, 3 : apparence, 4 : couleur, 5 : nom", ParserType.NUMBER) }) });
    public static final ItemActionListSet COMMON_APTITUDE_RESTAT = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Aucun", new Parameter[0]) });
    public static final ItemActionListSet INSTANCE_SPEAKER = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Aucun", new Parameter[0]) });
    public static final ItemActionListSet LEARN_COSTUME = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Learn Costume", new Parameter[0]) });
    public static final ItemActionListSet PET_XP = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Default", new Parameter[] { new ParserObjectParameter("Nombre de niveaux", ParserType.NUMBER) }) });
    public static final ItemActionListSet PET_HP = new ItemActionListSet(new ParameterList[] { new DefaultParameterList("Default", new Parameter[] { new ParserObjectParameter("Nombre de hp (<0 pour full vie)", ParserType.NUMBER) }) });
    public static final ItemActionListSet REROLL_ELEMENTS = new ItemActionListSet(new ParameterList[] { new EffectAreaParameterList("Default", new Parameter[0]), new EffectAreaParameterList("Level cap", new Parameter[] { new ParserObjectParameter("Max item level", ParserType.NUMBER) }) });
    
    public static class ItemActionListSet extends ParameterListSet
    {
        public ItemActionListSet(ParameterList... lists) {
            super(lists);
        }
        
        @Override
        public final boolean mapValueCount(int count) {
            return this.mapParameterCount(count);
        }
    }
}
