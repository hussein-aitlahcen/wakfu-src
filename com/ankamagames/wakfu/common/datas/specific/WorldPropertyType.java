package com.ankamagames.wakfu.common.datas.specific;

import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public enum WorldPropertyType implements ExportableEnum, PropertyType
{
    NONE(0, "Rien"), 
    RIGHT_HAND_LEVEL(1, "RIGHT_HAND_LEVEL"), 
    CANT_ATTACK(8, "N'attaque pas"), 
    IMMORTAL(12, "Est immortel", FightPropertyType.NO_DEATH), 
    CANT_BE_ATTACKED(13, "Ne peut \u00eatre attaqu\u00e9"), 
    IS_BLOKING(14, "Bloque les mouvements"), 
    CANT_BE_JOINED(15, "Ne peut \u00eatre rejoint"), 
    NO_SPELL_OR_SKILL_XP(16, "Ne donne pas xp", FightPropertyType.NO_XP), 
    PREGNANT(17, "Est enceinte"), 
    BUSY(18, "Est occup\u00e9"), 
    DEAD(19, "Est mort"), 
    CHALLENGE_NPC(20, "Est un npc de challenge"), 
    ADMIN_NPC(21, "Est un npc cr\u00e9\u00e9 par une commande admin"), 
    SEDUCE(28, "s\u00e9duite pour la fin de combat"), 
    JAIL_KEEPER(29, "Est gardien de prison"), 
    TRAINING_MOB(30, "Est un monstre d'entrainement"), 
    WELL_FED(31, "Est bien nourri"), 
    CANT_BE_TEMPORARY_ATTACKED(32, "Ne peut pas etre attaqu\u00e9 pour une dur\u00e9e d\u00e9termin\u00e9e"), 
    CANT_AGGRO_TRACK(33, "Ne peut pas lancer une poursuite (mais aggro quand m\u00eame)"), 
    DONT_NEED_PATH_TO_FIGHT(34, "Pas besoin d'avoir un chemin jusqu'au mob pour l'affronter"), 
    TELEPORT_ON_ENTER_FIGHT(35, "T\u00e9l\u00e9porte le monstre au lieu de le d\u00e9placer lors de son entr\u00e9e en combat"), 
    NPC(36, "Est un monstre PNJ (overhead special)"), 
    AMAKNA_SUPPORTER(37, "Est un partisan d'Amakna"), 
    BONTA_SUPPORTER(38, "Est un partisan de Bonta"), 
    BRAKMAR_SUPPORTER(39, "Est un partisan de Brakmar"), 
    RUSHU_RUSH_MONSTER(40, "Est un monstre de donjon type rushu rush"), 
    NOT_PRESENT_IN_TIMELINE(41, "Pas pr\u00e9sent dans la timeline"), 
    HOODED_MONSTER(42, "Est un encapuchonn\u00e9 (cache le niveau)"), 
    BOSS(43, "Est un boss"), 
    SUFOKIA_SUPPORTER(44, "Est un partisan de Sufokia"), 
    ARCH(45, "Est un Archimonstre"), 
    NPC_NO_MONSTER_ACTION(46, "Monstre avec lequel on ne peut pas effectuer d'actions"), 
    IS_ARCADE_NPC(47, "Est un monstre de donjon arcade"), 
    IS_ARCADE_WAVE_NPC(48, "Est un monstre d'une vague de donjon arcade"), 
    ULTIMATE_BOSS(49, "Est un boss ultime"), 
    CANT_BE_AGGRO(50, "Ne peut pas etre agress\u00e9"), 
    ALMANACH_NPC(51, "Est un npc d'Almanach"), 
    OGREST_CHAOS_NPC(52, "Est un npc de chaos d'ogrest"), 
    NO_CORPSE_WHEN_DEAD(53, "Ne laisse pas de cadavre \u00e0 sa mort"), 
    DUEL_DISABLED(54, "D\u00e9sactive les duels"), 
    AGRESSION_DISABLED(55, "D\u00e9sactive les agressions"), 
    EXCHANGE_DISABLED(56, "D\u00e9sactive les \u00e9changes"), 
    GROUP_DISABLED(57, "D\u00e9sactive les groupes (MRU + refus auto)"), 
    PRIVATE_CHAT_MRU_DISABLED(58, "D\u00e9sactive le MRU pour le chat priv\u00e9"), 
    FOLLOW_PLAYER_DISABLED(59, "D\u00e9sactive le MRU qui permet de suivre un joueur"), 
    MONSTER_ACTION_DISABLED(60, "D\u00e9sactive le MRU qui permet d'effectuer des actions sur les monstres"), 
    MONSTER_FIGHT_DISABLED(61, "D\u00e9sactive le MRU qui permet de lancer un combat contre un monstre"), 
    MONSTER_COLLECT_DISABLED(62, "D\u00e9sactive le MRU qui permet de lancer une r\u00e9colte sur un monstre"), 
    CANT_MODIFY_SHORTCUT_BARS(63, "D\u00e9sactive la possibilit\u00e9 de modifier la shortcutBar"), 
    CHAT_UI_INTERACTION_DISABLED(65, "D\u00e9sactive les interactions avec le chat"), 
    FOLLOW_ACHIEVEMENT_UI_FORCE_OPENED(66, "Force l'ouverture de l'UI de suivi de qu\u00eate"), 
    ACHIEVEMENT_AUTO_COMPASS(67, "Force l'activation de la boussole quand une qu\u00eate est activ\u00e9."), 
    DISPLAY_LIGHT_PROTECTOR_INFORMATION(68, "Affiche des informations de MDC all\u00e9g\u00e9es"), 
    PROTECTOR_SATISFACTION_GENERIC_BONUS_DISABLED(69, "Le protecteur ne donne pas son bonus de region"), 
    CALL_HELP_DISABLED(70, "Ne peut pas appeler \u00e0 l'aide en combat"), 
    FIGHT_LOCK_DISABLED(71, "Ne peut pas verrouiller le combat"), 
    CELL_REPORT_DISABLED(72, "Ne peut pas montrer une cellule"), 
    HIDE_FIGHTERS_DISABLED(73, "Combattants transparents d\u00e9sactiv\u00e9s"), 
    GIVE_UP_DISABLED(74, "Abandon d\u00e9sactiv\u00e9"), 
    JOIN_IN_FIGHT_MRU_DISABLED(75, "D\u00e9sactive le MRU qui permet de rejoindre un combat "), 
    CANT_UNLOAD_TUTORIAL_DIALOG(76, "Ne peut pas fermer un tutorial en cliquant dessus"), 
    NO_XP(77, "Ne gagne pas d'xp"), 
    RESOURCE_COLLECT_DISABLED(78, "D\u00e9sactive le MRU qui permet de lancer des collectes de resources"), 
    WAKFU_GAUGE_DISABLED(79, "D\u00e9sactive les modifications de la jauge de wakfu"), 
    GROUP_WITH_OTHER_BREED_DISABLED(80, "Ne peut pas grouper avec un personnage d'une autre classe"), 
    COMPANION(81, "Est un compagnon"), 
    COMPANION_FREE(82, "Compagnon dispo aux non-abo"), 
    COMPANION_DISABLED_FOR_FIGHT(83, "Les compagnons du personnage ne participent pas aux combats"), 
    NO_REROLL_XP(84, "Pas de bonus Xp pour les reroll"), 
    RELIC_SPECIAL_JUMP(85, "Peut sauter les obstacles"), 
    CHALLENGE_DISABLED(86, "Le joueur ne peut plus participer aux challenges/qu\u00eates environnementales"), 
    DO_NOT_USE_SCRIPTS_ON_SPELLS(87, "N'utilise pas de scripts sur ses sorts."), 
    BYPASS_EXCHANGE_REQUEST_DISABLED(88, "Permet de bypasser le blocage d'\u00e9change pour les mod\u00e9rateurs"), 
    DELETE_ITEM_DISABLED(89, "Emp\u00eache la suppression d'items de l'inventaire"), 
    DROP_ITEM_DISABLED(90, "Emp\u00eache de dropper les items de l'inventaire"), 
    USE_ITEM_DISABLED(91, "Interdit l'utilisation d'objets de l'inventaire"), 
    PET_CANT_LOST_LIFE(92, "Emp\u00eache aux familiers de perdre de la vie"), 
    COMPANION_ITEMS_CHANGE_DISABLED(93, "Emp\u00eache de modifier l'\u00e9quipement d'un compagnon"), 
    COMPANION_DELETE_DISABLED(94, "Emp\u00eache d'enlever un compagnon"), 
    GIFT_MANAGE_DISABLED(95, "Emp\u00eache de r\u00e9cup\u00e9rer les cadeaux depuis l'interface"), 
    BANDIT_SUPPORTER(96, "Est un partisan de la nation Bandit"), 
    CANT_OPEN_VAULT(97, "Emp\u00eache d'ouvrir le Vault"), 
    HERO_DISABLED_FOR_FIGHT(98, "Les h\u00e9ros du personnage ne participent pas aux combats");
    
    private byte m_propertyId;
    private String m_label;
    private FightPropertyType m_equivalentFightProperty;
    
    private WorldPropertyType(final int propertyId, final String label) {
        this.m_equivalentFightProperty = null;
        this.m_propertyId = (byte)propertyId;
        this.m_label = label;
    }
    
    private WorldPropertyType(final int propertyId, final String label, final FightPropertyType equivalentFightProperty) {
        this.m_equivalentFightProperty = null;
        this.m_propertyId = (byte)propertyId;
        this.m_label = label;
        this.m_equivalentFightProperty = equivalentFightProperty;
    }
    
    @Override
    public byte getPropertyTypeId() {
        return 0;
    }
    
    @Override
    public byte getId() {
        return this.m_propertyId;
    }
    
    public FightPropertyType getEquivalentFightProperty() {
        return this.m_equivalentFightProperty;
    }
    
    public static WorldPropertyType getPropertyFromId(final int id) {
        for (final WorldPropertyType prop : values()) {
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
