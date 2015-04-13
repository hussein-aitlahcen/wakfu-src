package com.ankamagames.wakfu.client.core.game.interactiveElement;

import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.client.core.landMarks.agtEnum.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.travel.*;

public enum WakfuClientInteractiveElementTypes implements InteractiveElementDefinition<WakfuClientMapInteractiveElement>
{
    Lever((short)1, (ObjectFactory<WakfuClientMapInteractiveElement>)new Lever.LeverFactory()), 
    PositionTrigger((short)2, (ObjectFactory<WakfuClientMapInteractiveElement>)new PositionTrigger.PositionTriggerFactory()), 
    StoppingPositionTrigger((short)3, (ObjectFactory<WakfuClientMapInteractiveElement>)new StoppingPositionTrigger.StoppingPositionTriggerFactory()), 
    Stool((short)4, (ObjectFactory<WakfuClientMapInteractiveElement>)new Stool.StoolFactory()), 
    Dummy((short)5, (ObjectFactory<WakfuClientMapInteractiveElement>)new Dummy.DummyFactory()), 
    ZoneTrigger((short)6, (ObjectFactory<WakfuClientMapInteractiveElement>)new ZoneTrigger.ZoneTriggerFactory()), 
    DimensionalBag((short)7, (ObjectFactory<WakfuClientMapInteractiveElement>)new DimensionalBagInteractiveElement.DimensionalBagInteractiveElementFactory()), 
    CraftTable((short)8, (ObjectFactory<WakfuClientMapInteractiveElement>)new CraftTable.CraftTableFactory(), LandMarkEnum.CRAFT), 
    DimensionalBagMerchantConsole((short)9, (ObjectFactory<WakfuClientMapInteractiveElement>)new DimensionalBagMerchantConsole.Factory()), 
    Door((short)12, (ObjectFactory<WakfuClientMapInteractiveElement>)new Door.DoorFactory()), 
    Destructible((short)13, (ObjectFactory<WakfuClientMapInteractiveElement>)new DestructibleMachine.DestructibleFactory()), 
    Chest((short)14, (ObjectFactory<WakfuClientMapInteractiveElement>)new Chest.ChestFactory()), 
    FloorItem((short)15, (ObjectFactory<WakfuClientMapInteractiveElement>)new FloorItemInteractiveElement.FloorItemInteractiveElementFactory()), 
    RespawnPoint((short)16, (ObjectFactory<WakfuClientMapInteractiveElement>)new RespawnPointPhoenix.RespawnPointPhoenixFactory(), LandMarkEnum.RESPAWN_POINT), 
    ChallengeGenericInteractiveelement((short)17, (ObjectFactory<WakfuClientMapInteractiveElement>)new ChallengeGenericInteractiveElement.ChallengeGenericInteractiveElementFactory()), 
    ChallengeCraftTable((short)18, (ObjectFactory<WakfuClientMapInteractiveElement>)new ChallengeCraftTable.ChallengeCraftTableFactory()), 
    LootChest((short)19, (ObjectFactory<WakfuClientMapInteractiveElement>)new LootChest.LootChestFactory(), LandMarkEnum.PERSONAL_NOTE), 
    Board((short)20, (ObjectFactory<WakfuClientMapInteractiveElement>)new Board.BoardFactory()), 
    GenericStaticInteractiveElement((short)21, (ObjectFactory<WakfuClientMapInteractiveElement>)new GenericStaticInteractiveElement.GenericStaticInteractiveElementFactory(), LandMarkEnum.PERSONAL_NOTE), 
    NPCBlocker((short)22, (ObjectFactory<WakfuClientMapInteractiveElement>)new NPCBlocker.NPCBlockerFactory()), 
    TicketCollector((short)23, (ObjectFactory<WakfuClientMapInteractiveElement>)new TicketCollector.TicketCollectorFactory()), 
    BoatBoard((short)24, (ObjectFactory<WakfuClientMapInteractiveElement>)new BoatBoard.BoatBoardFactory()), 
    Mailbox((short)38, (ObjectFactory<WakfuClientMapInteractiveElement>)new Mailbox.MailboxFactory()), 
    MerchantDisplayer((short)29, (ObjectFactory<WakfuClientMapInteractiveElement>)new MerchantDisplay.MerchantDisplayFactory()), 
    GuildMachine((short)30, (ObjectFactory<WakfuClientMapInteractiveElement>)new GuildMachine.GuildMachineFactory()), 
    BallotBox((short)31, (ObjectFactory<WakfuClientMapInteractiveElement>)new BallotBox.BallotBoxFactory()), 
    RecycleMachine((short)32, (ObjectFactory<WakfuClientMapInteractiveElement>)new RecycleMachine.RecycleMachineFactory()), 
    Decoration((short)28, (ObjectFactory<WakfuClientMapInteractiveElement>)new Decoration.DecorationFactory()), 
    PortableCraftTable((short)33, (ObjectFactory<WakfuClientMapInteractiveElement>)new PortableCraftTable.PortableCraftTableFactory()), 
    DimensionalBagAdminConsole((short)34, (ObjectFactory<WakfuClientMapInteractiveElement>)new DimensionalBagAdminConsole.Factory()), 
    DimensionalBagExitTrigger((short)36, (ObjectFactory<WakfuClientMapInteractiveElement>)new DimensionalBagExitTrigger.Factory()), 
    DimensionalBagPermissionsConsole((short)37, (ObjectFactory<WakfuClientMapInteractiveElement>)new DimensionalBagPermissionsConsole.Factory()), 
    DimensionalBagShelf((short)35, (ObjectFactory<WakfuClientMapInteractiveElement>)new DimensionalBagShelf.Factory()), 
    GenericActivableInteractiveElement((short)39, (ObjectFactory<WakfuClientMapInteractiveElement>)new GenericActivableInteractiveElement.GenericActivableInteractiveElementFactory(), LandMarkEnum.PERSONAL_NOTE), 
    MarketBoard((short)40, (ObjectFactory<WakfuClientMapInteractiveElement>)new MarketBoard.MarketBoardFactory()), 
    WeatherBoard((short)41, (ObjectFactory<WakfuClientMapInteractiveElement>)new WeatherBoard.WeatherBoardFactory()), 
    StoppingPositionZoneTrigger((short)42, (ObjectFactory<WakfuClientMapInteractiveElement>)new StoppinPositionZoneTrigger.StoppinPositionZoneTriggerFactory()), 
    DimensionalBagBackgroundDisplay((short)43, (ObjectFactory<WakfuClientMapInteractiveElement>)new DimensionalBagBackgroundDisplay.BackgroundDisplayFactory()), 
    DimensionalBagLock((short)44, (ObjectFactory<WakfuClientMapInteractiveElement>)new DimensionalBagLock.DimensionalBagLockFactory()), 
    StreetLight((short)45, (ObjectFactory<WakfuClientMapInteractiveElement>)new StreetLight.StreetLightFactory()), 
    BackgroundDisplay((short)46, (ObjectFactory<WakfuClientMapInteractiveElement>)new BackgroundDisplay.BackgroundDisplayFactory(), LandMarkEnum.PERSONAL_NOTE), 
    Zaap((short)26, (ObjectFactory<WakfuClientMapInteractiveElement>)new TravelMachine.Factory(TravelType.ZAAP), LandMarkEnum.ZAAP), 
    Drago((short)47, (ObjectFactory<WakfuClientMapInteractiveElement>)new TravelMachine.Factory(TravelType.DRAGO), LandMarkEnum.ZAAP), 
    Boat((short)48, (ObjectFactory<WakfuClientMapInteractiveElement>)new TravelMachine.Factory(TravelType.BOAT), LandMarkEnum.ZAAP), 
    Cannon((short)49, (ObjectFactory<WakfuClientMapInteractiveElement>)new TravelMachine.Factory(TravelType.CANNON), LandMarkEnum.ZAAP), 
    Collector((short)50, (ObjectFactory<WakfuClientMapInteractiveElement>)new Collector.Factory()), 
    Teleporter((short)51, (ObjectFactory<WakfuClientMapInteractiveElement>)new Teleporter.Factory(), LandMarkEnum.TP), 
    AudioMarker((short)52, (ObjectFactory<WakfuClientMapInteractiveElement>)new AudioMarker.Factory()), 
    DungeonLadderBoard((short)53, (ObjectFactory<WakfuClientMapInteractiveElement>)new DungeonLadderBoard.DungeonLadderBoardFactory()), 
    DungeonLadderStatue((short)54, (ObjectFactory<WakfuClientMapInteractiveElement>)new CharacterStatue.CharacterStatueFactory()), 
    Bed((short)55, (ObjectFactory<WakfuClientMapInteractiveElement>)new Bed.Factory()), 
    StorageBox((short)56, (ObjectFactory<WakfuClientMapInteractiveElement>)new StorageBox.Factory()), 
    CoinMachine((short)57, (ObjectFactory<WakfuClientMapInteractiveElement>)new CoinMachine.Factory()), 
    DialogMachine((short)58, (ObjectFactory<WakfuClientMapInteractiveElement>)new DialogMachine.Factory()), 
    ExchangeMachine((short)59, (ObjectFactory<WakfuClientMapInteractiveElement>)new ExchangeMachine.Factory()), 
    GuildStorageBox((short)60, (ObjectFactory<WakfuClientMapInteractiveElement>)new GuildStorageBox.Factory()), 
    GuildLadder((short)61, (ObjectFactory<WakfuClientMapInteractiveElement>)new GuildLadder.Factory()), 
    DungeonDisplayer((short)62, (ObjectFactory<WakfuClientMapInteractiveElement>)new DungeonDisplayer.Factory()), 
    RewardDisplayer((short)63, (ObjectFactory<WakfuClientMapInteractiveElement>)new RewardDisplayer.Factory()), 
    HavenWorldBoard((short)64, (ObjectFactory<WakfuClientMapInteractiveElement>)new HavenWorldBoard.Factory(), LandMarkEnum.HAVEN_WORLD_BUILDING), 
    EquipableDummy((short)65, (ObjectFactory<WakfuClientMapInteractiveElement>)new EquipableDummy.Factory()), 
    Bookcase((short)66, (ObjectFactory<WakfuClientMapInteractiveElement>)new Bookcase.Factory()), 
    HavenWorldBuildingBoard((short)67, (ObjectFactory<WakfuClientMapInteractiveElement>)new HavenWorldBuildingBoard.Factory()), 
    KrosmozGameBoard((short)68, (ObjectFactory<WakfuClientMapInteractiveElement>)new KrosmozGameBoard.Factory()), 
    KrosmozGameCollection((short)69, (ObjectFactory<WakfuClientMapInteractiveElement>)new KrosmozGameCollection.Factory()), 
    ZaapOutOnly((short)70, (ObjectFactory<WakfuClientMapInteractiveElement>)new HavenWorldTravelMachine.Factory(TravelType.ZAAP_OUT_ONLY)), 
    HavenWorldDrago((short)71, (ObjectFactory<WakfuClientMapInteractiveElement>)new HavenWorldTravelMachine.Factory(TravelType.HAVEN_WORLD_DRAGO)), 
    HavenWorldExit((short)72, (ObjectFactory<WakfuClientMapInteractiveElement>)new HavenWorldExit.Factory()), 
    HavenWorldResourcesCollector((short)73, (ObjectFactory<WakfuClientMapInteractiveElement>)new HavenWorldResourcesCollector.Factory()), 
    SeedSpreader((short)74, (ObjectFactory<WakfuClientMapInteractiveElement>)new SeedSpreader.Factory()), 
    NationSelectionBoard((short)75, (ObjectFactory<WakfuClientMapInteractiveElement>)new NationSelectionBoard.Factory());
    
    private final short m_modelId;
    private final ObjectFactory<WakfuClientMapInteractiveElement> m_factory;
    private final LandMarkEnum m_landMarkType;
    
    private WakfuClientInteractiveElementTypes(final short modelId, final ObjectFactory<WakfuClientMapInteractiveElement> factory) {
        this(modelId, factory, LandMarkEnum.NONE);
    }
    
    private WakfuClientInteractiveElementTypes(final short modelId, final ObjectFactory<WakfuClientMapInteractiveElement> factory, final LandMarkEnum landMarkType) {
        this.m_modelId = modelId;
        this.m_factory = factory;
        this.m_landMarkType = landMarkType;
    }
    
    @Override
    public String getEnumId() {
        return Short.toString(this.m_modelId);
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    @Override
    public short getFactoryId() {
        return this.m_modelId;
    }
    
    public LandMarkEnum getLandMarkType() {
        return this.m_landMarkType;
    }
    
    @Override
    public ObjectFactory<WakfuClientMapInteractiveElement> getFactory() {
        return this.m_factory;
    }
    
    @Nullable
    public static WakfuClientInteractiveElementTypes getFromId(final short id) {
        for (final WakfuClientInteractiveElementTypes type : values()) {
            if (type.m_modelId == id) {
                return type;
            }
        }
        return null;
    }
    
    @Override
    public String getEnumComment() {
        return this.getEnumLabel();
    }
}
