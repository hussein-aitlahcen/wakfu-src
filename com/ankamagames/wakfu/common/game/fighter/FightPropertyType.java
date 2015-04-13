package com.ankamagames.wakfu.common.game.fighter;

import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public enum FightPropertyType implements ExportableEnum, PropertyType
{
    NONE(0, "Rien"), 
    SKIP_TURN(1, "Passe son tour"), 
    RANGE_REDUCED_TO_CAC(2, "Reduit au CAC"), 
    ROOTED(3, "Rooted, entrav\u00e9"), 
    IMMUNE_TO_MP_LOSS(4, "Ne perd pas de PM"), 
    INVISIBLE(5, "Est invisible"), 
    INVISIBLE_REVEALED(6, "Voit les invisibles"), 
    TRANSPARENT_FOR_CONTROLLER(7, ""), 
    COWARD(9, "S'enfuit"), 
    HASTE(10, "Se dep\u00eache"), 
    STABILIZED(11, "Est stabilis\u00e9"), 
    NO_DEATH(12, "Ne meurt pas"), 
    NO_DROP(14, "Ne peut pas dropper d'objet"), 
    CANT_TACKLE(15, "Ne peut pas tacler"), 
    NO_XP(16, "Ne donne pas d'xp"), 
    MAX_PA_COST(17, "MAX_PA_COST"), 
    INVISIBLE_SUPERIOR(18, "Est invisible ++"), 
    NO_KO(19, "Ne peut pas \u00eatre KO"), 
    UNDEAD(20, "Est mort vivant"), 
    UNTARGETTABLE_BY_OTHER(21, "Intouchable directement"), 
    IA_DOUBLE_FOCUS_ON_TARGET(22, "Multiplie le focus de l'ia sur la personne portant cette propri\u00e9t\u00e9"), 
    STASIS_1(23, "Augmente le cout en PW de 1"), 
    STASIS_2(24, "Augmente le cout en PW de 2"), 
    STASIS_3(25, "Interdit les couts en PW"), 
    IA_MINIMIZE_FOCUS_ON_TARGET(26, "Divise le focus de l'ia sur la personne portant cette propri\u00e9t\u00e9"), 
    LOOTER(27, "loot en fin de combat"), 
    CANNOT_BE_SEDUCED(28, "Ne peut pas \u00eatre s\u00e9duite"), 
    GROGGY_1(29, "Augmente le cout en PA de 1"), 
    GROGGY_2(30, "Augmente le cout en PA de 2"), 
    GROGGY_3(31, "Ne peut plus utiliser de PA"), 
    CRIPPLED_1(32, "Augmente le cout en PM de 1"), 
    CRIPPLED_2(33, "Augmente le cout en PM de 2"), 
    CRIPPLED_3(34, "Ne peut plus utiliser de PM"), 
    LAME(35, "Double le co\u00fbt des d\u00e9placements en PM"), 
    SEVEN_LEAGUE_BOOTS(36, "Double les PM"), 
    EXTRA_TURN(37, "Rejoue apr\u00e8s son prochain tour"), 
    CONFUSED(38, "Essaye de cibler toutes les cases \u00e0 port\u00e9e (pour l'IA)"), 
    CAN_DETECT_TRAPS(39, "Voit les pi\u00e8ges (pour l'IA)"), 
    CAN_BE_SEDUCED(40, "Peut etre s\u00e9duite"), 
    CANNOT_USE_ITEM_CAST(41, "L'utilisation d'objet est proscrite"), 
    CANNOT_BE_EFFECT_TARGET(42, "Ne peut pas etre la cible d'effets"), 
    IS_CURRENT_SPELL_CAST_BROKEN(43, "Interrompt le lancement de sort en cours"), 
    IS_SADIDA_PUPPET(44, "Est une poup\u00e9e de Sadida"), 
    IS_WALKABLE_SADIDA_TOTEM(45, "Est un totem de Sadida non bloquant"), 
    IA_INVERT_FOCUS_ON_TARGET(46, "Inverse l'\u00e9valuation des sorts sur une cible qui porte cette propri\u00e9t\u00e9"), 
    DO_NOT_USE_LOCALISATION(47, "Pas de localisation de degats sur le porteur de cette propri\u00e9t\u00e9"), 
    IA_CAN_TARGET_KO_FIGHTERS(48, "Peut cibler des combattants KO (pour l'IA)"), 
    PHORZERKER_MODE(49, "Mode Phorzerker"), 
    AP_AS_MP(50, "Les PA deviennent des PM"), 
    DO_NOT_ORIENTATE_FIGHTER_DURING_FIGHT(51, "On oriente pas le perso pendant le combat"), 
    CHRAGE_CANCEL_TRIGGERING_EFFECT(52, "La chrage annule les effets qui la modifie"), 
    INVERT_DMG_AND_RES(53, "On inverse les dommages avec les resist"), 
    DONT_BLOCK_LOS(54, "Ne bloque plus les lignes de vue"), 
    CANT_TRANSPOSE(55, "Ne peut pas transposer"), 
    CANT_BE_PUSH_OR_PULLED(56, "Ne peut pas etre tirer/pousser"), 
    CANT_TELEPORT(57, "Ne peut pas se tp"), 
    WONT_LOOSE_CHRAGE_WHEN_HEAL(58, "Ne perd pas de charge quand re\u00e7oit un soin"), 
    AI_CAN_TARGET_OFFPLAY_FIGHTERS(59, "Permet \u00e0 l'IA de cibler des fighters KO"), 
    IS_A_COPY_OF_HIS_CONTROLLER(60, "Est une copie de son controller"), 
    CANT_BE_DIFFERENTIATED_FROM_COPIES(61, "A invoqu\u00e9 des copies de lui m\u00eame, et ne veut pas qu'on le diff\u00e9rencie"), 
    STILL_LIFE(62, "Est sous l'effet du sort Nature Morte"), 
    DOESNT_TRIGGER_EFFECT_ZONE_WHEN_JOINING_FIGHT(63, "Ne d\u00e9clenche pas les zone en arrivant en combat"), 
    XELOR_HAS_BEEN_ROLLBACKED(64, "A \u00e9t\u00e9 Rollback\u00e9 ce tour"), 
    DONT_TRIGGER_KO(65, "Ne d\u00e9clenche pas de trigger de KO"), 
    CANNOT_BE_RAISED(66, "Ne peut pas etre ramener a la vie"), 
    SPELL_WONT_BREAK_INVISIBILITY(67, "Reste invisible apr\u00e8s avoir utilis\u00e9 un sort ou une arme"), 
    CANNOT_BE_EFFECT_TARGET_BUT_CAN_BE_AI_TARGET(68, "Force la validation de la cible dans l'ia si le mob possede aussi la prop 42 CANNOT_BE_EFFECT_TARGET"), 
    ESCAPE_TACKLE(69, "Ne peut pas etre tacl\u00e9"), 
    CANNOT_BE_CARRIED(70, "Ne peut pas etre port\u00e9"), 
    CANT_GET_CLOSER(71, "Ne peut pas se rapproch\u00e9 via GetCloser"), 
    CANT_BE_STATE_TARGET(72, "Ne peut pas \u00eatre la cible d'\u00e9tats"), 
    DISPLAYED_LIKE_A_DECORATION(73, "Est affich\u00e9 comme un d\u00e9cors et non un joueur (pas de rollOver, de Pdv, ..."), 
    DONT_COUNT_AS_FIGHTER_ON_FIGHT_END(74, "Ne compte pas comme un fighter lors du calcul de fin de combat (utile pour Gris XIII combat mob vs (mob + joueur))"), 
    IS_NOT_VALID_TARGET(75, "La cible est invalide pour l'ex\u00e9cution des effets de modification de charac, de deplacement et d'etat non speciaux"), 
    SUMMON_FORCE_CHARACTERISTIC_DISPLAY(76, "Les caract\u00e9ristiques de l'invoc du localPlayer cibl\u00e9e sont affich\u00e9es quand ce n'est pas son tour, \u00e0 la place de celle du localPlayer"), 
    MOVEMENT_ON_RAILS_ONLY(77, "Le personnage ne peut se d\u00e9placer que sur des rails (ceux du steamer, par exemple)"), 
    DO_NOT_USE_TIMESCORE_GAUGE(78, "Le score de v\u00e9locit\u00e9 du personnage n'augmente pas (Bonus de timeline)"), 
    CANT_JUMP_FROM_CARRIER(79, "Ne saute pas quand il est port\u00e9"), 
    USE_MECANISM_MASTERY(80, "Utilise la maitrise des m\u00e9canismes (Obsol\u00e8te)"), 
    USE_SUMMONER_DMG_PERCENT(81, "Utilise la valeur de degats en % de l'invocateur"), 
    AI_STOP_COMPUTE(82, "Permet d'arreter le calcul d'ia en cours (suite a un changement de type d'ia par ex)"), 
    CANT_MOVE_AWAY_OR_REPELL(83, "Ne peut pas s'\u00e9loigner d'une cible ou d'une cellule"), 
    DONT_USE_SUMMONING_MASTERY(84, "N'utilise pas la maitrise des invocations"), 
    SIMULATING(85, "Simule un KO"), 
    DONT_SKIP_TURN_ON_TACKLE(86, "Ne passe pas son tour apres un tacle"), 
    TELEPORT_ON_MOVEMENT(87, "Se teleporte quand se deplace en combat"), 
    DO_NOT_MOVE_IN_FIGHT(88, "Ne se d\u00e9place pas en combat"), 
    IS_SADIDA_TOTEM(89, "Est un totem sadida bloquant"), 
    NO_DODGE_LIMIT(90, "Pas de limite au % d'esquive"), 
    NO_KAMAS(91, "Ne droppe pas de kamas"), 
    SMOOTH_MOVE_ON_AREA(92, "Se d\u00e9place sans \u00e0 coup sur les zones d'effets"), 
    MOVE_STRAIGHT_FORWARD(93, "Se d\u00e9place droit devant en combat"), 
    ATTACK_STRAIGHT_FORWARD(94, "Attaque droit devant en combat"), 
    CANNOT_CAST_SPELL(95, "Ne peut pas lancer de sort"), 
    NO_SPELL_XP_GAIN(96, "Le joueur ne gagne pas d'xp de sort."), 
    GIVE_DOUBLE_XP(97, "Le monstre donne le double d'xp (utile pour des \"challenges de combat\""), 
    MOVEMENT_ON_RAILS_ALWAYS_COST_1_PM(98, "Le mouvement sur un rail de steamer coute 1PM peu importe la distance"), 
    NO_LOOT(99, "Le joueur ne loote pas en fin de combat"), 
    ALWAYS_LOOT(100, "Le joueur loot \u00e0 chaque fois"), 
    SUMMONER_VISUAL_COPY(101, "Est une copie visuelle de l'invocateur"), 
    DISABLE_FIGHT_CHALLENGE(102, "D\u00e9sactive les challenges pour le combat"), 
    BYPASS_SUMMONS_COUNT(103, "N'est pas compt\u00e9 comme une invocation"), 
    REVERSI_REVERSABLE_PIECE(104, "Mini jeu reversi, le pion est inversable"), 
    CANNOT_USE_GATE_TO_MOVE(105, "Ne peut pas utiliser les portails pour se d\u00e9placer");
    
    private final byte m_propertyId;
    private final String m_label;
    
    private FightPropertyType(final int propertyId, final String label) {
        this.m_label = label;
        this.m_propertyId = (byte)propertyId;
    }
    
    @Override
    public byte getId() {
        return this.m_propertyId;
    }
    
    @Override
    public byte getPropertyTypeId() {
        return 1;
    }
    
    public static FightPropertyType getPropertyFromId(final int id) {
        for (final FightPropertyType prop : values()) {
            if (prop.getId() == id) {
                return prop;
            }
        }
        return null;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_propertyId);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
}
