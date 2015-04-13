package com.ankamagames.framework.graphics.engine.Anm2;

import java.util.*;
import com.ankamagames.framework.graphics.engine.*;

public final class AnmPartHelper
{
    public static final int CHEVEUX_CRC = 1460857362;
    public static final int TETE_CRC = 2571426;
    public static final int JAMBE_CRC = 70353081;
    public static final int PIED_CRC = 2455640;
    public static final int TRONC_CRC = 80097030;
    public static final int BRAS_CRC = 2047106;
    public static final int VETEMENT_CRC = -1157987650;
    public static final int EQUIPEMENT_CRC = -514753261;
    private static final String[] CHEVEUX;
    private static final String[] TETE;
    private static final String[] JAMBE;
    private static final String[] PIED;
    private static final String[] TRONC;
    private static final String[] BRAS;
    private static final String[] VETEMENT;
    private static final String[] EQUIPEMENT;
    private static final int PET_BRAS_BAS_CRC = 855858226;
    private static final String[] PET_BRAS_BAS;
    private static final int PET_BRAS_MILIEU_CRC = -2026252917;
    private static final String[] PET_BRAS_MILIEU;
    private static final int PET_BRAS_HAUT_CRC = 761980122;
    private static final String[] PET_BRAS_HAUT;
    private static final int PET_OEIL_CRC = 2422137;
    private static final String[] PET_OEIL;
    private static final int PET_MASK_CRC = -2027940606;
    private static final String[] PET_MASK;
    private static final int PET_EYE_MASK_CRC = 525252987;
    private static final String[] PET_EYE_MASK;
    private static final int CHEVEUX_CUSTOM_CRC = -485157885;
    private static final String[] CHEVEUX_CUSTOM;
    private static final int VETEMENT_CUSTOM_CRC = -1147503697;
    private static final String[] VETEMENT_CUSTOM;
    private static final HashSet<String> SET;
    
    public static String[] getParts(final String[] parts) {
        for (final String p : parts) {
            final String[] arr$;
            final String[] extended = arr$ = getParts(p);
            for (final String e : arr$) {
                AnmPartHelper.SET.add(e);
            }
        }
        final String[] result = new String[AnmPartHelper.SET.size()];
        AnmPartHelper.SET.toArray(result);
        return result;
    }
    
    public static String[] getParts(final String name) {
        final int crc = Engine.getPartName(name);
        switch (crc) {
            case 1460857362: {
                return AnmPartHelper.CHEVEUX;
            }
            case 2571426: {
                return AnmPartHelper.TETE;
            }
            case 70353081: {
                return AnmPartHelper.JAMBE;
            }
            case 2455640: {
                return AnmPartHelper.PIED;
            }
            case 80097030: {
                return AnmPartHelper.TRONC;
            }
            case 2047106: {
                return AnmPartHelper.BRAS;
            }
            case -1157987650: {
                return AnmPartHelper.VETEMENT;
            }
            case -514753261: {
                return AnmPartHelper.EQUIPEMENT;
            }
            case 855858226: {
                return AnmPartHelper.PET_BRAS_BAS;
            }
            case -2026252917: {
                return AnmPartHelper.PET_BRAS_MILIEU;
            }
            case 761980122: {
                return AnmPartHelper.PET_BRAS_HAUT;
            }
            case 2422137: {
                return AnmPartHelper.PET_OEIL;
            }
            case -2027940606: {
                return AnmPartHelper.PET_MASK;
            }
            case 525252987: {
                return AnmPartHelper.PET_EYE_MASK;
            }
            case -485157885: {
                return AnmPartHelper.CHEVEUX_CUSTOM;
            }
            case -1147503697: {
                return AnmPartHelper.VETEMENT_CUSTOM;
            }
            default: {
                return new String[] { name };
            }
        }
    }
    
    public static void main(final String[] args) {
        System.out.println(String.format("0x%X", Engine.getPartName("CHEVEUX")));
        System.out.println(String.format("0x%X", Engine.getPartName("TETE")));
        System.out.println(String.format("0x%X", Engine.getPartName("JAMBE")));
        System.out.println(String.format("0x%X", Engine.getPartName("PIED")));
        System.out.println(String.format("0x%X", Engine.getPartName("TRONC")));
        System.out.println(String.format("0x%X", Engine.getPartName("BRAS")));
        System.out.println(String.format("0x%X", Engine.getPartName("VETEMENT")));
        System.out.println(String.format("0x%X", Engine.getPartName("EQUIPEMENT")));
        System.out.println("-- gelutin :");
        System.out.println(String.format("0x%X", Engine.getPartName("BRASBAS")));
        System.out.println(String.format("0x%X", Engine.getPartName("BRASMILIEU")));
        System.out.println(String.format("0x%X", Engine.getPartName("BRASHAUT")));
        System.out.println(String.format("0x%X", Engine.getPartName("OEIL")));
        System.out.println(String.format("0x%X", Engine.getPartName("MASQUE")));
        System.out.println(String.format("0x%X", Engine.getPartName("MASQUEOEIL")));
        System.out.println("-- custom de perso :");
        System.out.println(String.format("0x%X", Engine.getPartName("CHEVEUXCUSTOM")));
        System.out.println(String.format("0x%X", Engine.getPartName("VETEMENTCUSTOM")));
        System.out.println("-- Anm color :");
        System.out.println(String.format("0x%X", Engine.getPartName("Peau")));
        System.out.println(String.format("0x%X", Engine.getPartName("Cheveux")));
        System.out.println(String.format("0x%X", Engine.getPartName("SymbolBg")));
        System.out.println(String.format("0x%X", Engine.getPartName("SymbolFg")));
        System.out.println(String.format("0x%X", Engine.getPartName("SymbolBorder")));
        System.out.println(String.format("0x%X", Engine.getPartName("Color_1")));
        System.out.println(String.format("0x%X", Engine.getPartName("Color_2")));
        System.out.println(String.format("0x%X", Engine.getPartName("Pupille")));
        System.out.println(String.format("0x%X", Engine.getPartName("Vetement")));
        System.out.println("--- ");
        System.out.println(String.format("0x%X", Engine.getPartName("LienClip")));
        System.out.println(String.format("0x%X", Engine.getPartName("Porteur")));
    }
    
    static {
        CHEVEUX = new String[] { "CheveuxHaut", "CheveuxBas", "CheveuxArriere" };
        TETE = new String[] { "BajouePeau", "Barbe", "Bouche", "Bouche01", "Bouche02", "Bouche03", "Bouche04", "Bouche05", "Bouche06", "Bouche07", "Bouche08", "Bouche09", "Bouche10", "Bouche11", "Bouche12", "Bouche13", "Bouche14", "CheveuxBas", "CheveuxHaut", "CheveuxArriere", "CheveuxNatteBasse", "CheuveuxNatteHaute", "Crane", "Oreille", "Joue_02", "Joue01", "Joue02", "Joue03", "Nez01", "Nez02", "Nez02Peau", "Oei02", "Oeil01", "Oeil02", "Oeil03", "Oeil04", "Oeil05", "Oeil06", "Oeil07", "Oeil08", "Oeil09", "Oeil10", "Oeil11", "Oeil12", "Oeil13", "Oeil14", "Oeil15", "Oeil16", "Pleur", "Pleur_2", "Pupille01", "Pupille02", "Pupille03", "Reflet", "Sourcil01", "Sourcil02", "Sourcil03" };
        JAMBE = new String[] { "JambeVetement", "JambePeau", "CuisseVetement", "CuissePeau" };
        PIED = new String[] { "Pied01Vetement", "Pied02Vetement", "Pied01Peau", "Pied02Peau" };
        TRONC = new String[] { "TroncVetement", "TroncPeau" };
        BRAS = new String[] { "BrasVetement", "BrasPeau" };
        VETEMENT = new String[] { "BrasVetement", "CuisseVetement", "EpauleVetement", "JambeVetement", "BassinVetement", "CorpsVetement", "JupeVetement", "Jupe2Vetement", "TroncVetement" };
        EQUIPEMENT = new String[] { "Epaulette-G", "Epaulette-D", "CorpsHabit", "Ceinture", "CuisseHabit", "JambeHabit", "PiedHabit01", "PiedHabit02", "Bouclier", "Cape", "CapeBas" };
        PET_BRAS_BAS = new String[] { "BrasBas", "Peau_BrasBas" };
        PET_BRAS_MILIEU = new String[] { "BrasMilieu", "Peau_BrasMilieu" };
        PET_BRAS_HAUT = new String[] { "BrasHaut", "Peau_BrasHaut" };
        PET_OEIL = new String[] { "OeilAmovible-Blanc01", "OeilAmovible-Blanc02", "OeilAmovible-Blanc03", "OeilAmovible-Blanc04", "OeilAmovible-Blanc05", "OeilAmovible-Blanc06", "OeilAmovible-Blanc07", "OeilAmovible-Blanc08", "OeilAmovible-Blanc09", "OeilAmovible-Halo01", "OeilAmovible-Halo02", "OeilAmovible-Noir01", "OeilAmovible-Noir02", "OeilAmovible-Noir03", "OeilAmovible-Noir04", "OeilAmovible-Halo01", "OeilAmovible-Halo02", "OeilAmovible-Marque" };
        PET_MASK = new String[] { "Marque" };
        PET_EYE_MASK = new String[] { "Oeil-Marque" };
        CHEVEUX_CUSTOM = new String[] { "Barbe", "Chapeau", "CheveuxBas", "CheveuxHaut", "CheveuxArriere", "CheveuxNatteBasse", "CheuveuxNatteHaute", "Oei02", "Oeil01", "Oeil02", "Oeil03", "Oeil04", "Oeil05", "Oeil06", "Oeil07", "Oeil08", "Oeil09", "Oeil10", "Oeil11", "Oeil12", "Oeil13", "Oeil14", "Oeil15", "Oeil16", "Pupille01", "Pupille02", "Pupille03", "Sourcil01", "Sourcil02", "Sourcil03", "Crane", "Oreille", "BassinPeau", "BrasPeau", "EpaulePeau", "JambePeau", "PiedPeau01", "PiedPeau02", "TroncPeau", "Nez02Peau", "CorpsPeau", "CuissePeau", "Main_01", "Main_02", "Main_Doigt_01", "Main_Doigt_02", "Main_Doigt_03", "Main_doigt_04", "Main_doigt_05", "Main_doigt_06", "Main_doigt_07", "Main_doigt_08", "Main_doigt_09", "Main_doigt_10", "Main_doigt_11", "Main_doigt_12", "Main_doigt_13", "Main_Paume_01", "Main_Paume_02", "Main_Paume_03", "Main_Paume_04", "Main_Paume_05", "Main_pouce_01", "Main_pouce_02", "Main_pouce_03", "Main_pouce_04", "Main_pouce_05", "Main_pouce_06", "Main_pouce_07", "Main_pouce_08", "Main_pouce_09", "Main_pouce_10", "Main_pouce_11", "Main_pouce_12", "Main_pouce_13", "Main_pouce_14", "Main_01", "Main_02", "Main_03", "Main_04", "Main_05" };
        VETEMENT_CUSTOM = new String[] { "EliatropeCoiffeBas", "EliatropeCoiffeHaut", "Barbe", "CuisseHabit", "JambeHabit", "PiedHabit01", "PiedHabit02", "Chapeau", "Epaulette-D", "Epaulette-G", "CorpsHabit", "BrasVetement", "EpauleVetement", "TroncVetement", "Jupe2Vetement", "JupeVetement", "CorpsVetement", "BassinVetement", "CuisseVetement", "JambeVetement", "Cape", "CapeBas", "CheveuxBas" };
        SET = new HashSet<String>(50);
    }
}
