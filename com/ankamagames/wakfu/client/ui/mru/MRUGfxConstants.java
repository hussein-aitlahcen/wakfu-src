package com.ankamagames.wakfu.client.ui.mru;

import com.ankamagames.framework.external.*;
import org.jetbrains.annotations.*;

public enum MRUGfxConstants implements ExportableEnum
{
    BAG(0, "Sac"), 
    EXCHANGE(1, "Echange"), 
    SWORD(2, "Epee"), 
    BOOK(3, "Livre"), 
    RELEASE(4, "Lib\u00e9rer"), 
    BUBBLE(5, "Bulle"), 
    BASKET(6, "Panier"), 
    PECKER(7, "Pioche"), 
    AXE(8, "Hache"), 
    METAL_SHEARS(9, "Cisailles"), 
    SCYTHE(10, "Faux"), 
    HAND(11, "Une main"), 
    JAR(12, "Jarre"), 
    PADLOCK(13, "Cadena"), 
    KICK(14, "Kicker"), 
    BARREL(15, "Tonneau"), 
    FRIENDS(16, "Amis"), 
    FLEA(17, "March\u00e9"), 
    BELL(18, "Cloche"), 
    FLOWERS(19, "Fleurs"), 
    BILLET(20, "Billet"), 
    PIN(21, "Epingle"), 
    HAMMER_MAGIC(22, "Marteau enchant\u00e9"), 
    HAMMMER(23, "Marteau"), 
    FELLOW(24, "Personnage"), 
    BARREL_OPENED(25, "Tonneau ouvert"), 
    BARREL_CLOSED(26, "Tonneau ferm\u00e9"), 
    BARREL_MOVE(27, "Tonneau en d\u00e9placement"), 
    UPGRADE(28, "Blop upgrade"), 
    DOWNGRADE(29, "Blop downgrade"), 
    HOOK(30, "Crochet"), 
    LIST(31, "Liste"), 
    BAG_DIMENSIONAL(32, "Sac dimensionnel"), 
    HAND_LEFT(33, "Main droite"), 
    HAND_RIGHT(34, "Main gauche"), 
    HAND_BOTH(35, "Deux mains"), 
    RUNE(36, "Rune"), 
    VIEWFINDER(37, "Cible"), 
    CLIPPERS(38, "Tondeuse"), 
    MILKPOT(39, "Pot \u00e0 lait"), 
    NEEDLE(40, "Aiguille"), 
    SHOE(41, "Chaussure"), 
    FLASK(42, "Flasque"), 
    POISON(43, "Poison"), 
    JEWEL(44, "Joyau"), 
    MEAT(45, "Viande"), 
    BREAD(46, "Pain"), 
    GEMME(47, "Gemme"), 
    HERBO_TUBE(48, "Tube \u00e0 essai"), 
    JUICE(49, "Tire-jus"), 
    MUSHROOM(50, "Champignon"), 
    ZAAP(51, "Zaap"), 
    TRAP(52, "Piege"), 
    GUILD(53, "Guilde"), 
    PET_PACK(54, "PetPack"), 
    PET_UNPACK(55, "PetUnpack"), 
    PET_INVENTORY(56, "PetInventory"), 
    SPECTATOR(57, "spectator"), 
    SIT(58, "sit"), 
    KISS(59, "Bisou"), 
    LIGHT_ON(60, "Allumer"), 
    LIGHT_OFF(61, "Eteindre"), 
    PADLOCK_OPEN(62, "Cadenas Ouvert"), 
    WHITE_SWORD(63, "\u00c9p\u00e9e blanche"), 
    JOIN_FIGHT(64, "Fl\u00e8che sur quadrillage"), 
    INTERROGATION_MARK(65, "Point d'interrogation"), 
    CHEF_S_HAT(66, "Toque de chef"), 
    SHIFUMI_PAPER(67, "Shifumi - feuille"), 
    SHIFUMI_SCISSORS(68, "Shifumi - ciseaux"), 
    SHIFUMI_ROCK(69, "Shifumi - pierre"), 
    SHOVEL(70, "Pelle"), 
    ROTATION(71, "Rotation"), 
    BROKEN_GEM(72, "Rune Bris\u00e9e"), 
    GEM(73, "Rune"), 
    GEM_MERGE(74, "Fusion de Runes"), 
    FOLLOW(75, "Suivre"), 
    TOMBSTONE(76, "Pierre tombale"), 
    FORK_KNIFE(77, "Fourchette et couteau"), 
    HEART(78, "Coeur");
    
    public final int m_id;
    private final String m_text;
    
    private MRUGfxConstants(final int id, final String text) {
        this.m_id = id;
        this.m_text = text;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_text;
    }
    
    @Nullable
    @Override
    public String getEnumComment() {
        return null;
    }
}
