package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.wakfu.common.game.item.bind.*;
import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.param.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.client.core.*;

public class IEParametersLoader implements ContentInitializer
{
    private static final Logger m_logger;
    private static final IEParametersLoader m_instance;
    private static final int DISPLAYER_VISUAL_ID = 186;
    
    public static IEParametersLoader getInstance() {
        return IEParametersLoader.m_instance;
    }
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        loadCraftTables();
        loadDecorations();
        loadDimensionalBagBackgroundDisplay();
        loadLights();
        loadBoards();
        loadAudioMarkers();
        loadBackgroundDisplays();
        loadDestructibles();
        loadLootChests();
        loadCollectors();
        loadTeleporters();
        loadStools();
        this.loadMarketBoards();
        loadStorageBoxes();
        loadGenericActivables();
        loadExchanges();
        loadRecycleMachines();
        loadDungeonDisplayer();
        loadRewardDisplayer();
        loadHavenWorldBoard();
        loadHavenWorldBuildingBoard();
        loadEquipableDummy();
        loadBookcase();
        loadKrosmozGameBoard();
        loadKrosmozGameCollection();
        loadDoor();
        clientInstance.fireContentInitializerDone(this);
    }
    
    private static void loadExchanges() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new ExchangeInteractiveElementParamBinaryData(), new LoadProcedure<ExchangeInteractiveElementParamBinaryData>() {
            @Override
            public void load(final ExchangeInteractiveElementParamBinaryData bs) {
                final int id = bs.getId();
                final int visualId = bs.getVisualMruId();
                final ExchangeInteractiveElementParamBinaryData.Exchange[] exchanges = bs.getExchanges();
                final ChaosParamBinaryData chaosParam = bs.getChaosParams();
                final int chaosParamId = IEParametersLoader.getChaosCollectorParamId(chaosParam);
                final ChaosInteractiveCategory chaosCategory = IEParametersLoader.getChaosCategory(chaosParam);
                final byte sortTypeId = bs.getSortTypeId();
                final IEExchangeParameter param = new IEExchangeParameter(id, visualId, chaosCategory, chaosParamId, sortTypeId, exchanges.length);
                for (int i = 0, length = exchanges.length; i < length; ++i) {
                    final ExchangeInteractiveElementParamBinaryData.Exchange bsExchange = exchanges[i];
                    final int exchangeId = bsExchange.getExchangeId();
                    final int consumableKama = bsExchange.getConsumableKama();
                    final int consumablePvpMoney = bsExchange.getConsumablePvpMoney();
                    final int resultingKama = bsExchange.getResultingKama();
                    final String criteria = bsExchange.getCriteria();
                    final ExchangeInteractiveElementParamBinaryData.Consumable[] bsConsumables = bsExchange.getConsumables();
                    final ExchangeInteractiveElementParamBinaryData.Resulting[] bsResultings = bsExchange.getResultings();
                    SimpleCriterion criterion;
                    try {
                        criterion = CriteriaCompiler.compileBoolean(criteria);
                    }
                    catch (Exception e) {
                        IEParametersLoader.m_logger.error((Object)("Impossible de compiler le crit\u00e8re de l'\u00e9change " + exchangeId), (Throwable)e);
                        continue;
                    }
                    final IEExchangeParameter.Exchange exchange = new IEExchangeParameter.Exchange(exchangeId, consumableKama, resultingKama, criterion, consumablePvpMoney, i);
                    for (int j = 0, length2 = bsConsumables.length; j < length2; ++j) {
                        final ExchangeInteractiveElementParamBinaryData.Consumable bsConsumable = bsConsumables[j];
                        final int itemId = bsConsumable.getItemId();
                        final short qty = bsConsumable.getQuantity();
                        exchange.addConsumable(itemId, qty);
                    }
                    for (int j = 0, length2 = bsResultings.length; j < length2; ++j) {
                        final ExchangeInteractiveElementParamBinaryData.Resulting bsResulting = bsResultings[j];
                        final int itemId = bsResulting.getItemId();
                        final short qty = bsResulting.getQuantity();
                        final ItemBindType baseBindType = ItemBindType.getFromId(bsResulting.getForcedBindType());
                        final ItemBindType forcedBindType = (baseBindType == ItemBindType.NOT_BOUND) ? ItemBindType.NOT_BOUND : ItemBindType.fromParameters(baseBindType.isOnPickup(), baseBindType.isCharacter(), true);
                        exchange.addResulting(itemId, qty, forcedBindType);
                    }
                    param.add(exchange);
                }
                IEParametersManager.INSTANCE.addExchangeParam(param);
            }
        });
    }
    
    private static void loadGenericActivables() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new GenericActivableInteractiveElementParamBinaryData(), new LoadProcedure<GenericActivableInteractiveElementParamBinaryData>() {
            @Override
            public void load(final GenericActivableInteractiveElementParamBinaryData bs) {
                final ChaosParamBinaryData chaosParam = bs.getChaosParams();
                final int chaosLevelId = IEParametersLoader.getChaosCollectorParamId(chaosParam);
                final ChaosInteractiveCategory chaosCategory = IEParametersLoader.getChaosCategory(chaosParam);
                final GenericActivableParameter param = new GenericActivableParameter(bs.getId(), chaosCategory, chaosLevelId, this.createVisuals(bs.getVisuals()));
                IEParametersManager.INSTANCE.addGenericActivableParam(param);
            }
            
            private GenericActivableParameter.Visual[] createVisuals(final GenericActivableInteractiveElementParamBinaryData.Visual[] dataVisuals) {
                final GenericActivableParameter.Visual[] visuals = new GenericActivableParameter.Visual[dataVisuals.length];
                int actionIndex = 0;
                for (int i = 0, size = dataVisuals.length; i < size; ++i) {
                    final GenericActivableInteractiveElementParamBinaryData.Visual dataVisual = dataVisuals[i];
                    visuals[i] = new GenericActivableParameter.Visual(dataVisual.getId(), dataVisual.getVisualId(), dataVisual.getItemConsumed(), dataVisual.getItemQuantity(), dataVisual.isDoConsumeItem(), dataVisual.getKamaCost(), this.createGroupActions(dataVisual.getGroupActions(), actionIndex), dataVisual.getDistributionDuration() * 1000);
                    actionIndex += visuals[i].getGroupActions().length;
                }
                return visuals;
            }
            
            private GenericActivableParameter.GroupAction[] createGroupActions(final GenericActivableInteractiveElementParamBinaryData.GroupAction[] groupActions, final int firstIndex) {
                final GenericActivableParameter.GroupAction[] groups = new GenericActivableParameter.GroupAction[groupActions.length];
                for (int i = 0, size = groupActions.length; i < size; ++i) {
                    final GenericActivableInteractiveElementParamBinaryData.GroupAction data = groupActions[i];
                    groups[i] = new GenericActivableParameter.GroupAction(data.getId(), firstIndex + i, data.getWeight(), data.getCriteria(), this.createActions(data.getActions()));
                }
                return groups;
            }
            
            private GenericActivableParameter.Action[] createActions(final GenericActivableInteractiveElementParamBinaryData.Action[] dataActions) {
                final GenericActivableParameter.Action[] visuals = new GenericActivableParameter.Action[dataActions.length];
                for (int i = 0, size = dataActions.length; i < size; ++i) {
                    final GenericActivableInteractiveElementParamBinaryData.Action dataAction = dataActions[i];
                    visuals[i] = new GenericActivableParameter.Action(dataAction.getActionId(), dataAction.getActionTypeId(), dataAction.getCriteria(), dataAction.getActionParams());
                }
                return visuals;
            }
        });
    }
    
    private void loadMarketBoards() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new MarketBoardInteractiveElementParamBinaryData(), new LoadProcedure<MarketBoardInteractiveElementParamBinaryData>() {
            @Override
            public void load(final MarketBoardInteractiveElementParamBinaryData bs) {
                final ChaosParamBinaryData chaosParams = bs.getChaosParams();
                final IEMarketBoardParameter param = new IEMarketBoardParameter(bs.getId(), bs.getVisualMruId(), bs.getMarketId(), IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams));
                IEParametersManager.INSTANCE.addMarketBoardParam(param);
            }
        });
    }
    
    private static void loadTeleporters() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new TeleporterBinaryData(), new LoadProcedure<TeleporterBinaryData>() {
            @Override
            public void load(final TeleporterBinaryData data) {
                final IETeleporterParameter tp = new IETeleporterParameter(data.getTeleporterId(), data.getLockId());
                IEParametersManager.INSTANCE.addTeleporterParam(tp);
                for (final TeleporterBinaryData.Destination dest : data.getDestinations()) {
                    final int id = dest.getDestinationId();
                    final int visualId = dest.getVisualId();
                    final int x = dest.getX();
                    final int y = dest.getY();
                    final int z = dest.getZ();
                    final int worldId = dest.getWorldId();
                    final Direction8 direction = Direction8.getDirectionFromIndex(dest.getDirection());
                    SimpleCriterion simpleCriterion = null;
                    try {
                        simpleCriterion = CriteriaCompiler.compileBoolean(dest.getCriteria());
                    }
                    catch (Exception e) {
                        IEParametersLoader.m_logger.warn((Object)("TP id=" + tp.getId() + "  " + e.getMessage() + " (criteria='" + dest.getCriteria() + "')"));
                    }
                    if (simpleCriterion == null) {
                        simpleCriterion = ConstantBooleanCriterion.TRUE;
                    }
                    final int apsId = dest.getApsId();
                    final short delay = dest.getDelay();
                    final int consumedItemRefId = dest.getItemConsumed();
                    final short consumedItemQty = dest.getItemQuantity();
                    final boolean consumeItem = dest.isDoConsumeItem();
                    final short consumedKamas = dest.getKamaCost();
                    final boolean isVisibleIfFalse = dest.isInvisible();
                    final IETeleporterParameter.Exit exit = new IETeleporterParameter.Exit(id, visualId, x, y, z, (short)worldId, direction, simpleCriterion, apsId, delay, consumedItemRefId, consumedItemQty, consumeItem, consumedKamas, isVisibleIfFalse);
                    tp.addDestination(exit);
                    exit.setLoadingInfo(new TravelLoadingInfo(dest.getLoadingAnimationName(), dest.getLoadingMinDuration(), dest.getLoadingFadeInDuration(), dest.getLoadingFadeOutDuration()));
                }
            }
        });
    }
    
    private static void loadCollectors() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new CollectInteractiveElementParamBinaryData(), new LoadProcedure<CollectInteractiveElementParamBinaryData>() {
            @Override
            public void load(final CollectInteractiveElementParamBinaryData bs) {
                final int id = bs.getId();
                final int visualId = bs.getVisualId();
                final short capacity = bs.getCapacity();
                final boolean locked = bs.isLocked();
                final int cashQty = bs.getCashQty();
                final IECollectorParameter param = new IECollectorParameter(id, visualId, capacity, locked, cashQty);
                IEParametersManager.INSTANCE.addCollectParam(param);
                for (final CollectInteractiveElementParamBinaryData.CollectItem item : bs.getItems()) {
                    param.addItem(item.getItemId(), item.getQty());
                }
            }
        });
    }
    
    private static void loadLootChests() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new LootChestInteractiveElementParamBinaryData(), new LoadProcedure<LootChestInteractiveElementParamBinaryData>() {
            @Override
            public void load(final LootChestInteractiveElementParamBinaryData bs) {
                final int id = bs.getId();
                final int visualId = bs.getVisualId();
                final int cost = bs.getCost();
                final int itemIdCost = bs.getItemIdCost();
                final int itemQuantityCost = bs.getItemQuantityCost();
                SimpleCriterion ieCriterion = null;
                try {
                    ieCriterion = CriteriaCompiler.compileBoolean(bs.getCriteria());
                }
                catch (Exception e) {
                    IEParametersLoader.m_logger.error((Object)("coffre de loot " + id + "  " + e.getMessage() + " (criteria='" + bs.getCriteria() + "')"));
                    return;
                }
                final ChaosParamBinaryData chaosParams = bs.getChaosParams();
                IEParametersManager.INSTANCE.addLootChestParam(new IELootChestParameter(id, visualId, Long.MIN_VALUE, cost, itemIdCost, itemQuantityCost, bs.isDoConsumeItem(), -32768, Long.MIN_VALUE, ieCriterion, IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams)));
            }
        });
    }
    
    private static void loadStorageBoxes() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new StorageBoxBinaryData(), new LoadProcedure<StorageBoxBinaryData>() {
            @Override
            public void load(final StorageBoxBinaryData bs) {
                final ChaosParamBinaryData chaosParams = bs.getChaosParams();
                final IEStorageBoxParameter ieStorageBoxParameter = new IEStorageBoxParameter(bs.getId(), bs.getVisualId(), IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams));
                for (final StorageBoxBinaryData.Compartment compartment : bs.getCompartments()) {
                    ieStorageBoxParameter.addCompartment(new IEStorageBoxParameter.Compartment(compartment.getUid(), compartment.getCapacity(), compartment.getUnlockItemId()));
                }
                IEParametersManager.INSTANCE.addStorageBoxParam(ieStorageBoxParameter);
            }
        });
    }
    
    private static void loadDestructibles() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new DestructibleElementParamBinaryData(), new LoadProcedure<DestructibleElementParamBinaryData>() {
            @Override
            public void load(final DestructibleElementParamBinaryData bs) {
                final int id = bs.getId();
                final int pdv = bs.getPdv();
                final long regenDelay = bs.getRegenDelay() * 1000L;
                final int resWater = bs.getResWater();
                final int resFire = bs.getResFire();
                final int resEarth = bs.getResEarth();
                final int resWind = bs.getResWind();
                IEParametersManager.INSTANCE.addDestructibleParam(new IEDestructibleParameter(id, pdv, regenDelay, resWater, resFire, resEarth, resWind));
            }
        });
    }
    
    private static void loadBackgroundDisplays() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new BackgroundInteractiveElementParamBinaryData(), new LoadProcedure<BackgroundInteractiveElementParamBinaryData>() {
            @Override
            public void load(final BackgroundInteractiveElementParamBinaryData bs) {
                final int id = bs.getId();
                final int visualId = bs.getVisualId();
                final int backgroundDisplayId = bs.getBackgroundFeedback();
                final ChaosParamBinaryData chaosParams = bs.getChaosParams();
                IEParametersManager.INSTANCE.addBackgroundDisplayParam(new IEBackgroundDisplayParameter(id, visualId, backgroundDisplayId, IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams)));
            }
        });
    }
    
    private static void loadBoards() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new BoardInteractiveElementParamBinaryData(), new LoadProcedure<BoardInteractiveElementParamBinaryData>() {
            @Override
            public void load(final BoardInteractiveElementParamBinaryData data) {
                IEParametersManager.INSTANCE.addBoardParam(new IEBoardParameter(data.getId()));
            }
        });
    }
    
    private static void loadAudioMarkers() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new AudioMarkerInteractiveElementParamBinaryData(), new LoadProcedure<AudioMarkerInteractiveElementParamBinaryData>() {
            @Override
            public void load(final AudioMarkerInteractiveElementParamBinaryData bs) {
                final int id = bs.getId();
                final int markerTypeId = bs.getAudioMarkerTypeId();
                final boolean isLocalized = bs.isLocalized();
                IEParametersManager.INSTANCE.addAudioMarkerParam(new IEAudioMarkerParameter(id, markerTypeId, isLocalized));
            }
        });
    }
    
    private static void loadLights() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new StreetLightInteractiveElementParamBinaryData(), new LoadProcedure<StreetLightInteractiveElementParamBinaryData>() {
            @Override
            public void load(final StreetLightInteractiveElementParamBinaryData bs) {
                final int id = bs.getId();
                final int color = bs.getColor();
                final float range = bs.getRange();
                final int apsId = bs.getApsId();
                final boolean activeInNightOnly = bs.isActiveOnlyInNight();
                final int ignitionVisualId = bs.getIgnitionVisualId();
                final boolean ignitionUseObject = bs.isIgnitionUseObject();
                final int ignitionDuration = bs.getIgnitionDuration() * 1000;
                final int extinctionVisualId = bs.getExtinctionVisualId();
                final boolean extinctionUseObject = bs.isExtinctionUseObject();
                final int extinctionDuration = bs.getExtinctionDuration() * 1000;
                final ChaosParamBinaryData chaosParams = bs.getChaosParams();
                final ChaosInteractiveCategory chaosCategory = IEParametersLoader.getChaosCategory(chaosParams);
                final int chaosCollectorParamId = IEParametersLoader.getChaosCollectorParamId(chaosParams);
                final IEStreetLightParameter parameter = new IEStreetLightParameter(id, color, range, apsId, activeInNightOnly, ignitionVisualId, ignitionUseObject, ignitionDuration, extinctionVisualId, extinctionUseObject, extinctionDuration, chaosCategory, chaosCollectorParamId);
                IEParametersManager.INSTANCE.addStreetLightParam(parameter);
            }
        });
    }
    
    private static void loadDimensionalBagBackgroundDisplay() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new GemBackgroundInteractiveElementParamBinaryData(), new LoadProcedure<GemBackgroundInteractiveElementParamBinaryData>() {
            @Override
            public void load(final GemBackgroundInteractiveElementParamBinaryData bs) {
                final int id = bs.getId();
                final int backgroundDisplayId = bs.getBackgroundFeedback();
                final int[] havreGemTypes = bs.getHavreGemTypes();
                final GemType[] gemTypes = new GemType[havreGemTypes.length];
                for (int i = 0; i < havreGemTypes.length; ++i) {
                    final GemType gemType = GemType.getFromItemReferenceId(havreGemTypes[i]);
                    if (gemType != null) {
                        gemTypes[i] = gemType;
                    }
                    else {
                        IEParametersLoader.m_logger.error((Object)("L'id de gemme id=" + havreGemTypes[i] + " pour le param\u00e8trage de d\u00e9coration id=" + id + " ne correspond pas \u00e0 une gemme valide"));
                    }
                }
                IEParametersManager.INSTANCE.addDimensionalBagBackgroundDisplayParam(new IEDimensionalBagBackgroundDisplayParameter(id, gemTypes, backgroundDisplayId));
            }
        });
    }
    
    private static void loadDecorations() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new DecorationInteractiveElementParamBinaryData(), new LoadProcedure<DecorationInteractiveElementParamBinaryData>() {
            @Override
            public void load(final DecorationInteractiveElementParamBinaryData bs) {
                final int id = bs.getId();
                final int[] havreGemTypes = bs.getHavreGemTypes();
                final GemType[] gemTypes = new GemType[havreGemTypes.length];
                for (int i = 0; i < havreGemTypes.length; ++i) {
                    final GemType gemType = GemType.getFromItemReferenceId(havreGemTypes[i]);
                    if (gemType != null) {
                        gemTypes[i] = gemType;
                    }
                    else {
                        IEParametersLoader.m_logger.error((Object)("L'id de gemme id=" + havreGemTypes[i] + " pour le param\u00e8trage de d\u00e9coration id=" + id + " ne correspond pas \u00e0 une gemme valide"));
                    }
                }
                IEParametersManager.INSTANCE.addDecorationParam(new IEDecorationParameter(id, gemTypes));
            }
        });
    }
    
    private static void loadCraftTables() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new CraftInteractiveElementParamBinaryData(), new LoadProcedure<CraftInteractiveElementParamBinaryData>() {
            @Override
            public void load(final CraftInteractiveElementParamBinaryData bs) {
                final int id = bs.getId();
                final int visualId = bs.getVisualMruId();
                final int apsId = bs.getApsId();
                final int craftId = bs.getSkillId();
                final int[] allowedRecipes = bs.getAllowedRecipes();
                final ChaosParamBinaryData chaosParams = bs.getChaosParams();
                IEParametersManager.INSTANCE.addCraftParam(new ClientIECraftParameter(id, visualId, craftId, allowedRecipes, apsId, IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams)));
            }
        });
    }
    
    private static void loadStools() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new StoolInteractiveElementParamBinaryData(), new LoadProcedure<StoolInteractiveElementParamBinaryData>() {
            @Override
            public void load(final StoolInteractiveElementParamBinaryData bs) {
                final int id = bs.getId();
                final ChaosParamBinaryData chaosParams = bs.getChaosParams();
                try {
                    final SimpleCriterion criterion = CriteriaCompiler.compileBoolean(bs.getCriterion());
                    IEParametersManager.INSTANCE.addStoolParam(new IEStoolParameter(id, criterion, bs.getVisualId(), IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams)));
                }
                catch (Exception e) {
                    IEParametersLoader.m_logger.error((Object)"", (Throwable)e);
                }
            }
        });
    }
    
    private static void loadRecycleMachines() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new RecycleMachineInteractiveElementParamBinaryData(), new LoadProcedure<RecycleMachineInteractiveElementParamBinaryData>() {
            @Override
            public void load(final RecycleMachineInteractiveElementParamBinaryData bs) {
                final ChaosParamBinaryData chaosParams = bs.getChaosParams();
                final IERecycleMachineParameter param = new IERecycleMachineParameter(bs.getId(), bs.getVisualMruId(), IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams));
                IEParametersManager.INSTANCE.addRecycleMachineParam(param);
            }
        });
    }
    
    private static void loadDungeonDisplayer() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new DungeonDisplayerInteractiveElementParamBinaryData(), new LoadProcedure<DungeonDisplayerInteractiveElementParamBinaryData>() {
            @Override
            public void load(final DungeonDisplayerInteractiveElementParamBinaryData bs) {
                final ChaosParamBinaryData chaosParams = null;
                final int id = bs.getId();
                IEParametersManager.INSTANCE.addDungeonDisplayerParam(new IEDungeonDisplayerParameter(id, 186, bs.getDungeonId(), IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams)));
            }
        });
    }
    
    private static void loadRewardDisplayer() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new RewardDisplayerInteractiveElementParamBinaryData(), new LoadProcedure<RewardDisplayerInteractiveElementParamBinaryData>() {
            @Override
            public void load(final RewardDisplayerInteractiveElementParamBinaryData bs) {
                final ChaosParamBinaryData chaosParams = null;
                final int id = bs.getId();
                IEParametersManager.INSTANCE.addRewardDisplayerParam(new IERewardDisplayerParameter(id, 186, bs.getItemIds(), IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams)));
            }
        });
    }
    
    private static void loadHavenWorldBoard() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new HavenWorldBoardInteractiveElementParamBinaryData(), new LoadProcedure<HavenWorldBoardInteractiveElementParamBinaryData>() {
            @Override
            public void load(final HavenWorldBoardInteractiveElementParamBinaryData bs) {
                final ChaosParamBinaryData chaosParams = null;
                final IEHavenWorldBoardParameter param = new IEHavenWorldBoardParameter(bs.getId(), bs.getVisualId(), IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams), bs.getHavenWorldId());
                param.setMiniOriginCell(bs.getMiniOriginCellX(), bs.getMiniOriginCellY(), bs.getMiniOriginCellZ());
                IEParametersManager.INSTANCE.addHavenWorldBoardParam(param);
            }
        });
    }
    
    private static void loadHavenWorldBuildingBoard() throws Exception {
        final IEHavenWorldBuildingBoardParameter param = new IEHavenWorldBuildingBoardParameter(1, 186, ChaosInteractiveCategory.NO_CHAOS, 0);
        IEParametersManager.INSTANCE.addHavenWorldBuildingBoardParam(param);
    }
    
    private static void loadEquipableDummy() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new EquipableDummyInteractiveElementParamBinaryData(), new LoadProcedure<EquipableDummyInteractiveElementParamBinaryData>() {
            @Override
            public void load(final EquipableDummyInteractiveElementParamBinaryData bs) {
                final ChaosParamBinaryData chaosParams = null;
                final int id = bs.getId();
                final String animName = bs.getAnimName();
                final byte sex = bs.getSex();
                IEParametersManager.INSTANCE.addEquipableDummyParam(new IEEquipableDummyParameter(id, 186, IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams), animName, sex));
            }
        });
    }
    
    private static void loadBookcase() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new BookcaseInteractiveElementParamBinaryData(), new LoadProcedure<BookcaseInteractiveElementParamBinaryData>() {
            @Override
            public void load(final BookcaseInteractiveElementParamBinaryData bs) {
                final ChaosParamBinaryData chaosParams = null;
                final int id = bs.getId();
                final byte size = bs.getSize();
                IEParametersManager.INSTANCE.addBookcaseParam(new IEBookcaseParameter(id, 186, IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams), size));
            }
        });
    }
    
    private static void loadKrosmozGameBoard() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new KrosmozGameBoardInteractiveElementParamBinaryData(), new LoadProcedure<KrosmozGameBoardInteractiveElementParamBinaryData>() {
            @Override
            public void load(final KrosmozGameBoardInteractiveElementParamBinaryData bs) {
                final ChaosParamBinaryData chaosParams = null;
                final int id = bs.getId();
                final byte gameId = bs.getGameId();
                IEParametersManager.INSTANCE.addKrosmozGameBoardParam(new IEKrosmozGameBoardParameter(id, 186, IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams), gameId));
            }
        });
    }
    
    private static void loadKrosmozGameCollection() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new KrosmozGameCollectionInteractiveElementParamBinaryData(), new LoadProcedure<KrosmozGameCollectionInteractiveElementParamBinaryData>() {
            @Override
            public void load(final KrosmozGameCollectionInteractiveElementParamBinaryData bs) {
                final ChaosParamBinaryData chaosParams = null;
                final int id = bs.getId();
                final byte gameId = bs.getGameId();
                IEParametersManager.INSTANCE.addKrosmozGameCollectionParam(new IEKrosmozGameCollectionParameter(id, 186, IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams), gameId));
            }
        });
    }
    
    private static void loadDoor() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new DoorInteractiveElementParamBinaryData(), new LoadProcedure<DoorInteractiveElementParamBinaryData>() {
            @Override
            public void load(final DoorInteractiveElementParamBinaryData bs) {
                final ChaosParamBinaryData chaosParams = null;
                SimpleCriterion criterion;
                try {
                    criterion = CriteriaCompiler.compileBoolean(bs.getCriterion());
                }
                catch (Exception e) {
                    IEParametersLoader.m_logger.error((Object)("Impossible de compiler le crit\u00e8re de la porte " + bs.getId()), (Throwable)e);
                    return;
                }
                IEParametersManager.INSTANCE.addDoorParam(new IEDoorParameter(bs.getId(), bs.getVisualId(), bs.isConsumeItem(), bs.getItemNeeded(), bs.getKamaCost(), criterion, IEParametersLoader.getChaosCategory(chaosParams), IEParametersLoader.getChaosCollectorParamId(chaosParams)));
            }
        });
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.ie.params");
    }
    
    public static int getChaosCollectorParamId(final ChaosParamBinaryData chaosParams) {
        if (chaosParams == null) {
            return 0;
        }
        return chaosParams.getChaosCollectorParamId();
    }
    
    public static ChaosInteractiveCategory getChaosCategory(final ChaosParamBinaryData chaosParams) {
        if (chaosParams == null) {
            return ChaosInteractiveCategory.NO_CHAOS;
        }
        return ChaosInteractiveCategory.fromId(chaosParams.getChaosLevel());
    }
    
    static {
        m_logger = Logger.getLogger((Class)IEParametersLoader.class);
        m_instance = new IEParametersLoader();
    }
}
