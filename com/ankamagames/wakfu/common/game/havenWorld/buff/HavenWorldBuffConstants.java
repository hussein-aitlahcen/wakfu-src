package com.ankamagames.wakfu.common.game.havenWorld.buff;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.wakfu.common.game.guild.storage.*;

public final class HavenWorldBuffConstants extends Constants<HavenWorldBuffDefinition>
{
    public static final HavenWorldBuffConstants INSTANCE;
    public static final HavenWorldBuffConstantDefinition DECREASE_FISH_REPOP_DURATION;
    public static final HavenWorldBuffConstantDefinition DECREASE_MINERAL_REPOP_DURATION;
    public static final HavenWorldBuffConstantDefinition DECREASE_TREE_REPOP_DURATION;
    public static final HavenWorldBuffConstantDefinition DECREASE_BUILD_DURATION_BY_10_PERCENT;
    public static final HavenWorldBuffConstantDefinition DECREASE_BUILD_DURATION_BY_25_PERCENT;
    public static final HavenWorldBuffConstantDefinition UNLOCK_GUILD_STORAGE_COMPARTMENT_HOUSE_1;
    public static final HavenWorldBuffConstantDefinition UNLOCK_GUILD_STORAGE_COMPARTMENT_HOUSE_2;
    public static final HavenWorldBuffConstantDefinition UNLOCK_GUILD_STORAGE_COMPARTMENT_HOUSE_3;
    public static final HavenWorldBuffConstantDefinition UNLOCK_GUILD_STORAGE_COMPARTMENT_MANSION_1;
    public static final HavenWorldBuffConstantDefinition UNLOCK_GUILD_STORAGE_COMPARTMENT_MANSION_2;
    public static final HavenWorldBuffConstantDefinition UNLOCK_GUILD_STORAGE_COMPARTMENT_COLLECT;
    public static final HavenWorldBuffConstantDefinition PERCEPTION_BONUS_5;
    public static final HavenWorldBuffConstantDefinition PERCEPTION_BONUS_10;
    public static final HavenWorldBuffConstantDefinition DECREASE_BUILD_KAMA_COST_BY_2_PERCENT;
    public static final HavenWorldBuffConstantDefinition DECREASE_BUILD_KAMA_COST_BY_5_PERCENT;
    public static final HavenWorldBuffConstantDefinition DECREASE_BUILD_RESOURCE_COST_BY_2_PERCENT;
    public static final HavenWorldBuffConstantDefinition DECREASE_BUILD_RESOURCE_COST_BY_5_PERCENT;
    public static final HavenWorldBuffConstantDefinition PERCEPTION_BONUS_15;
    
    static {
        INSTANCE = new HavenWorldBuffConstants();
        DECREASE_FISH_REPOP_DURATION = new HavenWorldBuffConstantDefinition(new ModifyResourceRepopDurationDefinition(22, -0.1f), 1, HavenWorldBuffConstants.INSTANCE, "Diminue de 10% le temps de repop des poissons");
        DECREASE_MINERAL_REPOP_DURATION = new HavenWorldBuffConstantDefinition(new ModifyResourceRepopDurationDefinition(23, -0.1f), 2, HavenWorldBuffConstants.INSTANCE, "Diminue de 10% le temps de repop des minerais");
        DECREASE_TREE_REPOP_DURATION = new HavenWorldBuffConstantDefinition(new ModifyResourceRepopDurationDefinition(ResourceType.TREE.getAgtId(), -0.1f), 3, HavenWorldBuffConstants.INSTANCE, "Diminue de 10% le temps de repop des arbres");
        DECREASE_BUILD_DURATION_BY_10_PERCENT = new HavenWorldBuffConstantDefinition(new ModifyBuildDurationFactorDefinition(-10), 4, HavenWorldBuffConstants.INSTANCE, "Diminue le temps de construction de 10%");
        DECREASE_BUILD_DURATION_BY_25_PERCENT = new HavenWorldBuffConstantDefinition(new ModifyBuildDurationFactorDefinition(-25), 5, HavenWorldBuffConstants.INSTANCE, "Diminue le temps de construction de 25%");
        UNLOCK_GUILD_STORAGE_COMPARTMENT_HOUSE_1 = new HavenWorldBuffConstantDefinition(new UnlockGuildStorageCompartmentDefinition(GuildStorageCompartmentUnlockType.HAVEN_WORLD_HOUSE, 0), 6, HavenWorldBuffConstants.INSTANCE, "D\u00e9bloque un compartiment de coffre de guilde (Maison 1)");
        UNLOCK_GUILD_STORAGE_COMPARTMENT_HOUSE_2 = new HavenWorldBuffConstantDefinition(new UnlockGuildStorageCompartmentDefinition(GuildStorageCompartmentUnlockType.HAVEN_WORLD_HOUSE, 1), 7, HavenWorldBuffConstants.INSTANCE, "D\u00e9bloque un compartiment de coffre de guilde (Maison 2)");
        UNLOCK_GUILD_STORAGE_COMPARTMENT_HOUSE_3 = new HavenWorldBuffConstantDefinition(new UnlockGuildStorageCompartmentDefinition(GuildStorageCompartmentUnlockType.HAVEN_WORLD_HOUSE, 2), 8, HavenWorldBuffConstants.INSTANCE, "D\u00e9bloque un compartiment de coffre de guilde (Maison 3)");
        UNLOCK_GUILD_STORAGE_COMPARTMENT_MANSION_1 = new HavenWorldBuffConstantDefinition(new UnlockGuildStorageCompartmentDefinition(GuildStorageCompartmentUnlockType.HAVEN_WORLD_MANSION, 0), 9, HavenWorldBuffConstants.INSTANCE, "D\u00e9bloque un compartiment de coffre de guilde (Manoir 1)");
        UNLOCK_GUILD_STORAGE_COMPARTMENT_MANSION_2 = new HavenWorldBuffConstantDefinition(new UnlockGuildStorageCompartmentDefinition(GuildStorageCompartmentUnlockType.HAVEN_WORLD_MANSION, 1), 10, HavenWorldBuffConstants.INSTANCE, "D\u00e9bloque un compartiment de coffre de guilde (Manoir 2)");
        UNLOCK_GUILD_STORAGE_COMPARTMENT_COLLECT = new HavenWorldBuffConstantDefinition(new UnlockGuildStorageCompartmentDefinition(GuildStorageCompartmentUnlockType.COLLECT, 0), 11, HavenWorldBuffConstants.INSTANCE, "D\u00e9bloque un compartiment de coffre de guilde (Collecte)");
        PERCEPTION_BONUS_5 = new HavenWorldBuffConstantDefinition(new PerceptionBonusDefinition(5), 12, HavenWorldBuffConstants.INSTANCE, "Applique une perception de 5%");
        PERCEPTION_BONUS_10 = new HavenWorldBuffConstantDefinition(new PerceptionBonusDefinition(10), 13, HavenWorldBuffConstants.INSTANCE, "Applique une perception de 10%");
        DECREASE_BUILD_KAMA_COST_BY_2_PERCENT = new HavenWorldBuffConstantDefinition(new ModifyBuildKamaCostFactorDefinition(-2), 14, HavenWorldBuffConstants.INSTANCE, "Diminue le coup de construction en kama des batiments de 2%");
        DECREASE_BUILD_KAMA_COST_BY_5_PERCENT = new HavenWorldBuffConstantDefinition(new ModifyBuildKamaCostFactorDefinition(-5), 15, HavenWorldBuffConstants.INSTANCE, "Diminue le coup de construction en kama des batiments de 5%");
        DECREASE_BUILD_RESOURCE_COST_BY_2_PERCENT = new HavenWorldBuffConstantDefinition(new ModifyBuildResourceCostFactorDefinition(-2), 16, HavenWorldBuffConstants.INSTANCE, "Diminue le coup de construction en ressource des batiments de 2%");
        DECREASE_BUILD_RESOURCE_COST_BY_5_PERCENT = new HavenWorldBuffConstantDefinition(new ModifyBuildResourceCostFactorDefinition(-5), 17, HavenWorldBuffConstants.INSTANCE, "Diminue le coup de construction en ressource des batiments de 5%");
        PERCEPTION_BONUS_15 = new HavenWorldBuffConstantDefinition(new PerceptionBonusDefinition(15), 18, HavenWorldBuffConstants.INSTANCE, "Applique une perception de 15%");
    }
}
