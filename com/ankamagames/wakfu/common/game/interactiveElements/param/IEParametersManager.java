package com.ankamagames.wakfu.common.game.interactiveElements.param;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;

public class IEParametersManager
{
    private static final Logger m_logger;
    public static final IEParametersManager INSTANCE;
    private final EnumMap<IETypes, TIntObjectHashMap<IEParameter>> m_params;
    
    private IEParametersManager() {
        super();
        this.m_params = new EnumMap<IETypes, TIntObjectHashMap<IEParameter>>(IETypes.class);
    }
    
    public void addGenericActivableParam(@NotNull final GenericActivableParameter param) {
        this.addParam(param, IETypes.GENERIC_ACTIVABLE);
    }
    
    public void addMarketBoardParam(@NotNull final IEMarketBoardParameter param) {
        this.addParam(param, IETypes.MARKET_BOARD);
    }
    
    public void addTeleporterParam(@NotNull final IETeleporterParameter param) {
        this.addParam(param, IETypes.TELEPORTER);
    }
    
    public void addStorageBoxParam(@NotNull final IEStorageBoxParameter param) {
        this.addParam(param, IETypes.STORAGE_BOX);
    }
    
    public void addCollectParam(@NotNull final IECollectorParameter param) {
        this.addParam(param, IETypes.COLLECT_MACHINE);
    }
    
    public void addLootChestParam(@NotNull final IELootChestParameter param) {
        this.addParam(param, IETypes.LOOT_CHEST);
    }
    
    public void addCraftParam(@NotNull final IECraftParameter param) {
        this.addParam(param, IETypes.CRAFT_TABLE);
    }
    
    public void addDecorationParam(@NotNull final IEDecorationParameter param) {
        this.addParam(param, IETypes.DECORATION);
    }
    
    public void addDimensionalBagBackgroundDisplayParam(@NotNull final IEDimensionalBagBackgroundDisplayParameter param) {
        this.addParam(param, IETypes.DIMENSIONAL_BAG_BACKGROUND_DISPLAY);
    }
    
    public void addStreetLightParam(@NotNull final IEStreetLightParameter param) {
        this.addParam(param, IETypes.STREET_LIGHT);
    }
    
    public void addBoardParam(@NotNull final IEBoardParameter param) {
        this.addParam(param, IETypes.BOARD);
    }
    
    public void addAudioMarkerParam(@NotNull final IEAudioMarkerParameter param) {
        this.addParam(param, IETypes.AUDIO_MARKER);
    }
    
    public void addBackgroundDisplayParam(@NotNull final IEBackgroundDisplayParameter param) {
        this.addParam(param, IETypes.BACKGROUND_DISPLAY);
    }
    
    public void addDestructibleParam(@NotNull final IEDestructibleParameter param) {
        this.addParam(param, IETypes.DESTRUCTIBLE);
    }
    
    public void addStoolParam(@NotNull final IEStoolParameter param) {
        this.addParam(param, IETypes.STOOL);
    }
    
    public void addExchangeParam(@NotNull final IEExchangeParameter param) {
        this.addParam(param, IETypes.EXCHANGE_MACHINE);
    }
    
    public void addRecycleMachineParam(@NotNull final IERecycleMachineParameter param) {
        this.addParam(param, IETypes.RECYCLE_MACHINE);
    }
    
    public void addDungeonDisplayerParam(@NotNull final IEDungeonDisplayerParameter param) {
        this.addParam(param, IETypes.DUNGEON_DIPLAYER);
    }
    
    public void addRewardDisplayerParam(@NotNull final IERewardDisplayerParameter param) {
        this.addParam(param, IETypes.REWARD_DISPLAYER);
    }
    
    public void addHavenWorldBoardParam(@NotNull final IEHavenWorldBoardParameter param) {
        this.addParam(param, IETypes.HAVEN_WORLD_BOARD);
    }
    
    public void addEquipableDummyParam(@NotNull final IEEquipableDummyParameter param) {
        this.addParam(param, IETypes.EQUIPABLE_DUMMY);
    }
    
    public void addBookcaseParam(@NotNull final IEBookcaseParameter param) {
        this.addParam(param, IETypes.BOOKCASE);
    }
    
    public void addHavenWorldBuildingBoardParam(@NotNull final IEHavenWorldBuildingBoardParameter param) {
        this.addParam(param, IETypes.HAVEN_WORLD_BUILDING_BOARD);
    }
    
    public void addKrosmozGameBoardParam(@NotNull final IEKrosmozGameBoardParameter param) {
        this.addParam(param, IETypes.KROSMOZ_GAME_BOARD);
    }
    
    public void addKrosmozGameCollectionParam(@NotNull final IEKrosmozGameCollectionParameter param) {
        this.addParam(param, IETypes.KROSMOZ_GAME_COLLECTION);
    }
    
    public void addDoorParam(@NotNull final IEDoorParameter param) {
        this.addParam(param, IETypes.DOOR);
    }
    
    @Nullable
    public IEParameter getParam(@NotNull final IETypes type, final int paramId) {
        final TIntObjectHashMap<IEParameter> params = this.m_params.get(type);
        return (params != null) ? params.get(paramId) : null;
    }
    
    private void addParam(@NotNull final IEParameter param, final IETypes type) {
        TIntObjectHashMap<IEParameter> params = this.m_params.get(type);
        if (params == null) {
            this.m_params.put(type, params = new TIntObjectHashMap<IEParameter>());
        }
        if (params.put(param.getId(), param) != null) {
            IEParametersManager.m_logger.error((Object)new IllegalArgumentException("on essaye d'enregistrer des param\u00e8tres d' IE " + type.toString() + " avec le meme id " + param.getId()));
        }
    }
    
    @Override
    public String toString() {
        return "IEParametersManager{m_params=" + this.m_params.size() + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)IEParametersManager.class);
        INSTANCE = new IEParametersManager();
    }
}
