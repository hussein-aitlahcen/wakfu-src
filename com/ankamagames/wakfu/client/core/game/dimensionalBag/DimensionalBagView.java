package com.ankamagames.wakfu.client.core.game.dimensionalBag;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.room.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.alea.environment.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;
import com.ankamagames.wakfu.common.game.personalSpace.data.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.gfxProvider.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.rights.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.dimensionalBag.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.item.*;

public class DimensionalBagView implements FieldProvider, DisplayedScreenWorld.ReloadListener, MapLoadListener
{
    protected static final Logger m_logger;
    public static final String SELECTED_NAME_FIELD = "selectedName";
    public static final String FLEA_LIST_FIELD = "fleaList";
    public static final String CURRENT_FLEA_FIELD = "currentFlea";
    public static final String SELECTED_ROOM_FIELD = "selectedRoom";
    private static final int ROOM_TYPE_NOT_A_ROOM = -1;
    private static final int ROOM_TYPE_NOT_GEMMED = -2;
    public static final String ROOM_0_FIELD = "room0";
    public static final String ROOM_1_FIELD = "room1";
    public static final String ROOM_2_FIELD = "room2";
    public static final String ROOM_3_FIELD = "room3";
    public static final String ROOM_4_FIELD = "room4";
    public static final String ROOM_5_FIELD = "room5";
    public static final String ROOM_6_FIELD = "room6";
    public static final String ROOM_7_FIELD = "room7";
    public static final String ROOM_8_FIELD = "room8";
    public static final String[] FIELDS;
    private final DimensionalBagFlea m_dimensionalBagFlea;
    private MerchantInventory m_currentMerchantInventory;
    private final DimensionalBag m_dimensionalBag;
    private boolean m_fleaAllowed;
    private boolean m_onMarket;
    private final byte m_baseBuyDuty = 0;
    private final byte m_nationBuyDuty = 0;
    private int m_partitionNationId;
    private String m_nameToAdd;
    private String m_selectedName;
    private short m_instanceId;
    private final int m_worldX;
    private final int m_worldY;
    private short m_altitude;
    private final TShortArrayList m_externalDisplayInfoItemType;
    private final TByteArrayList m_externalDisplayInfoQuantity;
    private final TByteArrayList m_externalDisplayInfoQuality;
    private final ArrayList<GemControlledRoomView> m_rooms;
    private final ArrayList<MerchantInventoryItem> m_shelfMerchantInventoryItems;
    
    public static String resultToTranslationKey(final GemControlledRoom.ModResult result) {
        return WakfuTranslator.getInstance().getString("dimensionalBag.gem.error." + result.name());
    }
    
    public DimensionalBagView() {
        super();
        this.m_fleaAllowed = true;
        this.m_onMarket = false;
        this.m_partitionNationId = 0;
        this.m_nameToAdd = "";
        this.m_selectedName = "";
        this.m_externalDisplayInfoItemType = new TShortArrayList(3);
        this.m_externalDisplayInfoQuantity = new TByteArrayList(3);
        this.m_externalDisplayInfoQuality = new TByteArrayList(3);
        this.m_rooms = new ArrayList<GemControlledRoomView>();
        this.m_shelfMerchantInventoryItems = new ArrayList<MerchantInventoryItem>();
        this.m_dimensionalBag = new DimensionalBag();
        (this.m_dimensionalBagFlea = new DimensionalBagFlea()).addObserver(new MerchantInventoryCollectionObserver() {
            @Override
            public void onMerchantInventoryAdded(final AbstractMerchantInventory inventory) {
                PropertiesProvider.getInstance().firePropertyValueChanged(DimensionalBagView.this, "fleaList");
            }
            
            @Override
            public void onMerchantInventoryRemoved(final AbstractMerchantInventory inventory) {
                DimensionalBagView.this.clearCurrentMerchantInventoryView(inventory);
            }
            
            @Override
            public void onMerchantInventoryUpdated(final AbstractMerchantInventory inventory, final InventoryEvent modification) {
                ((MerchantInventory)inventory).notifyChanges();
            }
        });
        this.m_worldX = 0;
        this.m_worldY = 0;
        this.m_altitude = 0;
    }
    
    public void clearCurrentMerchantInventoryView() {
        this.clearCurrentMerchantInventoryView(null);
    }
    
    private void clearCurrentMerchantInventoryView(final AbstractMerchantInventory inventory) {
        if (inventory == null || inventory.equals(this.m_currentMerchantInventory)) {
            UIBrowseDimensionalBagFleaFrame.getInstance().unloadFleaDialog();
            this.m_currentMerchantInventory = null;
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "fleaList", "currentFlea");
    }
    
    public boolean initializeToEnter() {
        DimensionalBagView.m_logger.debug((Object)"DimensionalBag.initializeToEnter()");
        this.update();
        return true;
    }
    
    private void update() {
        final TopologyMapInstance mapInstance = TopologyMapManager.getMap((short)0, (short)0);
        this.m_dimensionalBag.updateTopology(mapInstance);
        final WakfuClientEnvironmentMap environmentMap = (WakfuClientEnvironmentMap)EnvironmentMapManager.getInstance().getMap((short)0, (short)0);
        this.m_dimensionalBag.updateGroundTypes(environmentMap);
        DisplayedScreenWorld.getInstance().rebuildCache();
        this.updateGroundLook();
    }
    
    public void updateFromRaw(final RawDimensionalBagForClient serializedPersonalSpace) {
        if (serializedPersonalSpace != null) {
            this.fromRaw(serializedPersonalSpace);
            this.update();
        }
    }
    
    public void setCustomModelView(final int view) {
        this.m_dimensionalBag.setCustomViewModelId(view);
    }
    
    public int getCustomModelView() {
        return this.m_dimensionalBag.getCustomViewModelId();
    }
    
    public int getWorldX() {
        return this.m_worldX;
    }
    
    public int getWorldY() {
        return this.m_worldY;
    }
    
    public short getAltitude() {
        return this.m_altitude;
    }
    
    public void setAltitude(final short altitude) {
        this.m_altitude = altitude;
    }
    
    public short getInstanceId() {
        return this.m_instanceId;
    }
    
    public void setInstanceId(final short instanceId) {
        this.m_instanceId = instanceId;
    }
    
    public ArrayList<MerchantInventoryItem> getShelfMerchantInventoryItems() {
        return this.m_shelfMerchantInventoryItems;
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public String[] getFields() {
        return DimensionalBagView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("selectedName")) {
            return this.getSelectedName();
        }
        if (fieldName.equals("fleaList")) {
            final ArrayList<MerchantInventory> inventories = new ArrayList<MerchantInventory>();
            final TLongObjectIterator<MerchantInventory> it = ((AbstractMerchantInventoryCollection<IT, MerchantInventory>)this.m_dimensionalBagFlea).getMerchantInventoriesIterator();
            while (it.hasNext()) {
                it.advance();
                inventories.add(it.value());
            }
            return inventories;
        }
        if (fieldName.equals("currentFlea")) {
            return this.m_currentMerchantInventory;
        }
        if (fieldName.equals("room0")) {
            return this.m_rooms.get(0);
        }
        if (fieldName.equals("room1")) {
            return this.m_rooms.get(1);
        }
        if (fieldName.equals("room2")) {
            return this.m_rooms.get(2);
        }
        if (fieldName.equals("room3")) {
            return this.m_rooms.get(3);
        }
        if (fieldName.equals("room4")) {
            return this.m_rooms.get(4);
        }
        if (fieldName.equals("room5")) {
            return this.m_rooms.get(5);
        }
        if (fieldName.equals("room6")) {
            return this.m_rooms.get(6);
        }
        if (fieldName.equals("room7")) {
            return this.m_rooms.get(7);
        }
        if (fieldName.equals("room8")) {
            return this.m_rooms.get(8);
        }
        return null;
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return fieldName.equals("selectedName");
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
        if (fieldName.equals("selectedName") && value != null) {
            this.setSelectedName(value.toString());
        }
    }
    
    public PathFindResult checkPath(final long visiterId, final PathFindResult inputPath) {
        final ArrayList<int[]> steps = new ArrayList<int[]>();
        final boolean isOwnersBag = visiterId == this.m_dimensionalBag.getOwnerId();
        for (int pathLen = inputPath.getPathLength(), i = 0; i < pathLen; ++i) {
            final int[] step = inputPath.getPathStep(i);
            final short stepX = (short)step[0];
            final short stepY = (short)step[1];
            if (stepX >= 0 && stepX < 18 && stepY < 0 && !isOwnersBag) {
                break;
            }
            if (stepX >= 0 && stepX < 18 && stepY >= 0 && stepY <= 18) {
                final Room room = this.m_dimensionalBag.getLayout().getRoomFromUnit(stepX, stepY);
                if (room == null) {
                    if (!this.m_dimensionalBag.isCorridorCell(stepX, stepY)) {
                        break;
                    }
                }
                else if (room instanceof GemControlledRoom) {
                    final GemControlledRoom gRoom = (GemControlledRoom)room;
                    final Item gem = gRoom.getGem(true);
                    if (gem == null) {
                        break;
                    }
                    final int gemId = gem.getReferenceId();
                    final boolean allowed = this.m_dimensionalBag.canCharacterAccessRoom(visiterId, gemId);
                    if (!allowed) {
                        DimensionalBagView.m_logger.info((Object)("[Checkpath] L'acces \u00e0 ce type de salle est interdite au joueur ID=" + visiterId));
                        break;
                    }
                }
            }
            steps.add(step);
        }
        if (steps.isEmpty()) {
            return null;
        }
        final int len = steps.size();
        final PathFindResult clippedPath = new PathFindResult(len);
        for (int j = 0; j < len; ++j) {
            clippedPath.setStep(j, steps.get(j));
        }
        return clippedPath;
    }
    
    public void setFleaAllowed(final boolean fleaAllowed) {
        this.m_fleaAllowed = fleaAllowed;
    }
    
    public void setOnMarket(final boolean onMarket) {
        this.m_onMarket = onMarket;
    }
    
    public void setPartitionNationId(final int partitionNationId) {
        this.m_partitionNationId = partitionNationId;
    }
    
    public boolean isFleaAllowed() {
        return this.m_fleaAllowed;
    }
    
    public boolean isOnMarket() {
        return this.m_onMarket;
    }
    
    public int getPartitionNationId() {
        return this.m_partitionNationId;
    }
    
    public String getNameToAdd() {
        return this.m_nameToAdd;
    }
    
    public void setNameToAdd(final String nameToAdd) {
        this.m_nameToAdd = nameToAdd;
    }
    
    public String getSelectedName() {
        return this.m_selectedName;
    }
    
    public void setSelectedName(final String selectedName) {
        this.m_selectedName = selectedName;
    }
    
    public long getOwnerId() {
        return this.m_dimensionalBag.getOwnerId();
    }
    
    public String getOwnerName() {
        return this.m_dimensionalBag.getOwnerName();
    }
    
    public Wallet getWallet() {
        return this.m_dimensionalBag.getWallet();
    }
    
    public PersonalSpaceLayout getLayout() {
        return this.m_dimensionalBag.getLayout();
    }
    
    public DimensionalBag getDimensionalBag() {
        return this.m_dimensionalBag;
    }
    
    public boolean fromRaw(final RawDimensionalBagForClient serializedPersonalSpace) {
        final boolean result = this.m_dimensionalBag.fromRaw(serializedPersonalSpace);
        this.updateRoomData();
        return result;
    }
    
    public boolean fromRaw(final RawDimensionalBagForSpawn rawBag) {
        this.m_dimensionalBag.setOwnerId(rawBag.ownerId);
        this.m_dimensionalBag.setOwnerName(rawBag.ownerName);
        this.m_dimensionalBag.setGuildId(rawBag.guildId);
        this.m_dimensionalBag.setCustomViewModelId(rawBag.customViewModelId);
        this.m_externalDisplayInfoItemType.reset();
        this.m_externalDisplayInfoQuantity.reset();
        this.m_externalDisplayInfoQuality.reset();
        this.m_shelfMerchantInventoryItems.clear();
        for (final RawDimensionalBagForSpawn.ShelfItem shelfItem : rawBag.shelfItems) {
            final MerchantInventoryItem merchantItem = MerchantInventoryItem.checkout();
            merchantItem.fromRaw(shelfItem.shelfItem);
            this.m_shelfMerchantInventoryItems.add(merchantItem);
        }
        for (final RawDimensionalBagForSpawn.MerchantDisplayInfo displayInfo : rawBag.merchantDisplays) {
            this.m_externalDisplayInfoItemType.add(displayInfo.itemType);
            this.m_externalDisplayInfoQuantity.add(displayInfo.contentQuantity);
            this.m_externalDisplayInfoQuality.add(displayInfo.contentQuality);
        }
        return true;
    }
    
    private void updateRoomData() {
        this.m_rooms.clear();
        final PersonalSpaceLayout layout = this.m_dimensionalBag.getLayout();
        for (byte i = 0, size = layout.roomCount(); i < size; ++i) {
            this.m_rooms.add(new GemControlledRoomView((GemControlledRoom)layout.getRoom(i)));
        }
    }
    
    public void updateRoomUI(final byte roomId) {
        final GemControlledRoomView controlledRoomView = this.m_rooms.get(roomId);
        if (controlledRoomView != null) {
            controlledRoomView.updateUI();
        }
    }
    
    public DimensionalBagFlea getDimensionalBagFlea() {
        return this.m_dimensionalBagFlea;
    }
    
    public boolean isCellUsable(final short x, final short y) {
        return this.m_dimensionalBag.isCellUsable(x, y);
    }
    
    public GemControlledRoom getRoom(final byte index) {
        return (GemControlledRoom)this.m_dimensionalBag.getLayout().getRoom(index);
    }
    
    public int getRoomTypeFromCell(final short x, final short y) {
        final Room room = this.m_dimensionalBag.getLayout().getRoomFromUnit(x, y);
        if (room == null || !(room instanceof GemControlledRoom)) {
            return -1;
        }
        final Item gem = ((GemControlledRoom)room).getGem(true);
        if (gem != null) {
            return gem.getReferenceId();
        }
        return -2;
    }
    
    private boolean isCellEmpty(final int x, final int y, final boolean[][] cellsArray) {
        assert cellsArray.length == 18 && cellsArray[0].length == 18 : "Array for ground must be of size [MapConstants.MAP_WIDTH][MapConstants.MAP_LENGTH]";
        return x < 0 || y < 0 || x >= 18 || y >= 18 || cellsArray[x][y];
    }
    
    @Override
    public void onMapLoaded() {
        this.updateGroundLook();
    }
    
    public int getBackgroundMapId() {
        return DimensionalBagModelViewManager.INSTANCE.getBackgroundMapId(this.getCustomModelView());
    }
    
    @Override
    public void onReload() {
        this.updateGroundLook();
    }
    
    private void updateGroundLook() {
        MathHelper.setSeed(this.getOwnerId());
        final DisplayedScreenWorld screenWorld = DisplayedScreenWorld.getInstance();
        final boolean[][] emptyCells = new boolean[18][18];
        final RoomPattern[][] roomPatterns = new RoomPattern[18][18];
        final DimensionalBagGfxProvider gfxProvider = DimensionalBagGfxProvider.getInstance();
        final RoomPattern corridor = DimensionalBagGfxProvider.getCorridor();
        for (short x = 0; x < 18; ++x) {
            for (short y = 0; y < 18; ++y) {
                final DisplayedScreenElement element = screenWorld.getElementFromTop(1, x, y, ElementFilter.ALL);
                if (element == null) {
                    emptyCells[x][y] = true;
                }
                else {
                    final int roomType = this.getRoomTypeFromCell(x, y);
                    RoomPattern roomPattern;
                    if (roomType == -1) {
                        if (!this.isCellUsable(x, y)) {
                            emptyCells[x][y] = true;
                            element.setVisible(false);
                            continue;
                        }
                        emptyCells[x][y] = false;
                        roomPattern = corridor;
                    }
                    else {
                        if (roomType == -2) {
                            emptyCells[x][y] = true;
                            element.setVisible(false);
                            continue;
                        }
                        emptyCells[x][y] = false;
                        roomPattern = DimensionalBagGfxProvider.getRoomPattern(roomType);
                        if (roomPattern == null) {
                            DimensionalBagView.m_logger.error((Object)("Pas de pattern pour les salles de type " + roomType + ". S\u00e9lection du premier type disponible'."));
                            roomPattern = DimensionalBagGfxProvider.getDefaultRoomPattern();
                        }
                    }
                    element.setVisible(true);
                    if ((roomPatterns[x][y] = roomPattern) != corridor) {
                        final int gfxId = roomPattern.getElementId(x, y);
                        element.replaceGfx(gfxId);
                    }
                }
            }
        }
        computeCorridor(screenWorld, emptyCells, roomPatterns, corridor);
        for (short x = 0; x < 18; ++x) {
            for (short y = 0; y < 18; ++y) {
                final DisplayedScreenElement element = screenWorld.getElementAtTop(x, y, ElementFilter.ALL);
                if (element != null) {
                    if (emptyCells[x][y]) {
                        element.setVisible(false);
                    }
                    else {
                        int borderMask = 0;
                        if (this.isCellEmpty(x - 1, y, emptyCells) || roomPatterns[x - 1][y] == corridor) {
                            borderMask |= 0x8;
                        }
                        if (this.isCellEmpty(x + 1, y, emptyCells) || roomPatterns[x + 1][y] == corridor) {
                            borderMask |= 0x2;
                        }
                        if (this.isCellEmpty(x, y - 1, emptyCells) || roomPatterns[x][y * 1] == corridor) {
                            borderMask |= 0x1;
                        }
                        if (this.isCellEmpty(x, y + 1, emptyCells) || roomPatterns[x][y + 1] == corridor) {
                            borderMask |= 0x4;
                        }
                        final RoomPattern roomPattern = roomPatterns[x][y];
                        if (roomPattern == null) {
                            element.setVisible(false);
                        }
                        else {
                            final int gfxId = roomPattern.getBorderElementIdFromMask(borderMask);
                            if (gfxId == -1) {
                                element.setVisible(false);
                            }
                            else if (gfxId == 0) {
                                element.setVisible(false);
                            }
                            else {
                                element.setVisible(true);
                                element.replaceGfx(gfxId);
                                RoomPattern.changeZOrder(element, borderMask);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private static void computeCorridor(final DisplayedScreenWorld screenWorld, final boolean[][] emptyCells, final RoomPattern[][] roomPatterns, final RoomPattern corridor) {
        for (short x = 0; x < 18; ++x) {
            for (short y = 0; y < 18; ++y) {
                if (roomPatterns[x][y] == corridor) {
                    final DisplayedScreenElement element = screenWorld.getElementFromTop(1, x, y, ElementFilter.ALL);
                    if (emptyCells[x - 1][y] || emptyCells[x + 1][y]) {
                        element.replaceGfx(corridor.getElementId((short)0, (short)1));
                    }
                    else {
                        element.replaceGfx(corridor.getElementId((short)1, (short)0));
                    }
                }
            }
        }
    }
    
    public GemControlledRoom.ModResult checkPutGem(final byte roomLayoutPosition, final Item gem, final boolean primary) {
        return this.m_dimensionalBag.putGem(roomLayoutPosition, gem, primary, false);
    }
    
    public GemControlledRoom.ModResult checkRemoveGem(final byte roomLayoutPosition, final boolean primary) {
        return this.m_dimensionalBag.removeGem(roomLayoutPosition, primary, false);
    }
    
    public GemControlledRoom.ModResult checkGemExchange(final byte sourceRoomLayoutPosition, final boolean sourceIsPrimary, final byte destRoomLayoutPosition, final boolean destIsPrimary) {
        if (sourceRoomLayoutPosition == destRoomLayoutPosition) {
            return GemControlledRoom.ModResult.INVALID_ARGUMENT;
        }
        return this.m_dimensionalBag.exchangeGem(sourceRoomLayoutPosition, sourceIsPrimary, destRoomLayoutPosition, destIsPrimary, false);
    }
    
    public GemControlledRoom.ModResult exchangeGems(final byte sourceRoomLayoutPosition, final boolean sourceIsPrimary, final byte destRoomLayoutPosition, final boolean destIsPrimary, final boolean commit) {
        GemControlledRoom.ModResult result = this.checkGemExchange(sourceRoomLayoutPosition, sourceIsPrimary, destRoomLayoutPosition, destIsPrimary);
        if (result == GemControlledRoom.ModResult.OK) {
            WakfuClientInstance.getInstance();
            final LocalPlayerCharacter localPlayer = WakfuClientInstance.getGameEntity().getLocalPlayer();
            if (localPlayer != null) {
                if (commit) {
                    DimensionalBagView.m_logger.info((Object)("[EXCHANGE GEM] commit ! sprimary=" + sourceIsPrimary + " (pos:" + sourceRoomLayoutPosition + "), dprimary=" + destIsPrimary + " (pos:" + destRoomLayoutPosition + ')'));
                    this.m_dimensionalBag.exchangeGem(sourceRoomLayoutPosition, sourceIsPrimary, destRoomLayoutPosition, destIsPrimary, true);
                    this.update();
                }
                else {
                    DimensionalBagView.m_logger.info((Object)("[EXCHANGE GEM] query ! sprimary=" + sourceIsPrimary + " (pos:" + sourceRoomLayoutPosition + "), dprimary=" + destIsPrimary + " (pos:" + destRoomLayoutPosition + ')'));
                    final RoomsGemsExchange message = new RoomsGemsExchange();
                    message.setSourcePrimary(sourceIsPrimary);
                    message.setDestPrimary(destIsPrimary);
                    message.setSourceRoomLayoutPosition(sourceRoomLayoutPosition);
                    message.setDestRoomLayoutPosition(destRoomLayoutPosition);
                    WakfuClientInstance.getInstance();
                    WakfuClientInstance.getGameEntity().getNetworkEntity().sendMessage(message);
                }
            }
            else {
                result = GemControlledRoom.ModResult.INVALID_ARGUMENT;
            }
        }
        return result;
    }
    
    public GemControlledRoom.ModResult putGem(final byte roomLayoutPosition, final Item gem, final long bagInventoryUID, final boolean primary, final boolean commit) {
        final GemControlledRoom.ModResult modResult = this.checkPutGem(roomLayoutPosition, gem, primary);
        if (modResult != GemControlledRoom.ModResult.OK) {
            return modResult;
        }
        if (bagInventoryUID == 0L) {
            DimensionalBagView.m_logger.error((Object)"Cas non trait\u00e9");
            return modResult;
        }
        WakfuClientInstance.getInstance();
        final LocalPlayerCharacter localPlayer = WakfuClientInstance.getGameEntity().getLocalPlayer();
        if (localPlayer == null) {
            return modResult;
        }
        final ClientBagContainer bagContainer = localPlayer.getBags();
        final AbstractBag bag = bagContainer.getFromUid(bagInventoryUID);
        if (bag == null) {
            return GemControlledRoom.ModResult.INVALID_ARGUMENT;
        }
        if (!bag.contains(gem)) {
            return GemControlledRoom.ModResult.INVALID_ARGUMENT;
        }
        if (commit) {
            DimensionalBagView.m_logger.info((Object)("[PUT GEM] commit ! primary=" + primary + ", gemUID=" + gem.getUniqueId() + ", bagUID=" + bagInventoryUID + ", room=" + roomLayoutPosition));
            bag.remove(gem);
            this.m_dimensionalBag.putGem(roomLayoutPosition, gem, primary, true);
            this.update();
        }
        else {
            DimensionalBagView.m_logger.info((Object)("[PUT GEM] query ! primary=" + primary + ", gemUID=" + gem.getUniqueId() + ", bagUID=" + bagInventoryUID + ", room=" + roomLayoutPosition));
            final InventoryToRoomGemExchange message = new InventoryToRoomGemExchange();
            message.setGemItemUid(gem.getUniqueId());
            message.setPrimary(primary);
            message.setRoomlayoutPosition(roomLayoutPosition);
            WakfuClientInstance.getInstance();
            WakfuClientInstance.getGameEntity().getNetworkEntity().sendMessage(message);
        }
        return modResult;
    }
    
    public GemControlledRoom.ModResult removeGem(final byte roomLayoutPosition, final long bagInventoryUID, final byte inventoryPos, final boolean primary, final boolean commit) {
        GemControlledRoom.ModResult modResult = this.checkRemoveGem(roomLayoutPosition, primary);
        if (modResult == GemControlledRoom.ModResult.OK) {
            if (bagInventoryUID == 0L) {
                DimensionalBagView.m_logger.error((Object)"Cas non trait\u00e9");
            }
            else {
                WakfuClientInstance.getInstance();
                final LocalPlayerCharacter localPlayer = WakfuClientInstance.getGameEntity().getLocalPlayer();
                if (localPlayer != null) {
                    final AbstractBag bag = localPlayer.getBags().getFromUid(bagInventoryUID);
                    if (bag != null) {
                        try {
                            final Item gem = this.getGem(roomLayoutPosition, primary);
                            if (bag.canAdd(gem, inventoryPos)) {
                                if (commit) {
                                    DimensionalBagView.m_logger.info((Object)("[REMOVE GEM] commit ! primary=" + primary + ", gemUID=" + gem.getUniqueId() + ", bagUID=" + bagInventoryUID + ", room=" + roomLayoutPosition));
                                    this.m_dimensionalBag.removeGem(roomLayoutPosition, primary, true);
                                    bag.addAt(gem, inventoryPos);
                                    this.update();
                                }
                                else {
                                    DimensionalBagView.m_logger.info((Object)("[REMOVE GEM] query ! primary=" + primary + ", gemUID=" + gem.getUniqueId() + ", bagUID=" + bagInventoryUID + ", room=" + roomLayoutPosition));
                                    final RoomToInventoryGemExchange message = new RoomToInventoryGemExchange();
                                    message.setInventoryUid(bagInventoryUID);
                                    message.setPosition(inventoryPos);
                                    message.setPrimary(primary);
                                    message.setRoomLayoutPosition(roomLayoutPosition);
                                    WakfuClientInstance.getInstance();
                                    WakfuClientInstance.getGameEntity().getNetworkEntity().sendMessage(message);
                                }
                            }
                            else {
                                modResult = GemControlledRoom.ModResult.INVALID_ARGUMENT;
                            }
                        }
                        catch (Exception e) {
                            DimensionalBagView.m_logger.error((Object)"Exception", (Throwable)e);
                            modResult = GemControlledRoom.ModResult.INVALID_ARGUMENT;
                        }
                    }
                    else {
                        modResult = GemControlledRoom.ModResult.INVALID_ARGUMENT;
                    }
                }
                else {
                    modResult = GemControlledRoom.ModResult.INVALID_ARGUMENT;
                }
            }
        }
        return modResult;
    }
    
    public Item getGem(final byte roomLayoutPosition, final boolean primary) {
        return this.m_dimensionalBag.getGem(roomLayoutPosition, primary);
    }
    
    public DimBagRights getBagPermissions() {
        WakfuClientInstance.getInstance();
        final LocalPlayerCharacter localPlayer = WakfuClientInstance.getGameEntity().getLocalPlayer();
        final DimensionalBagView visitingBag = localPlayer.getVisitingDimentionalBag();
        if (visitingBag != null) {
            return this.m_dimensionalBag.getRights();
        }
        return null;
    }
    
    public void setBagPermissions(final DimBagRights permissions) {
        WakfuClientInstance.getInstance();
        final LocalPlayerCharacter localPlayer = WakfuClientInstance.getGameEntity().getLocalPlayer();
        final DimensionalBagView visitingBag = localPlayer.getVisitingDimentionalBag();
        if (visitingBag != null && visitingBag == localPlayer.getOwnedDimensionalBag()) {
            final RawDimensionalBagPermissions rawPermissions = new RawDimensionalBagPermissions();
            permissions.toRaw(rawPermissions);
            final int size = rawPermissions.serializedSize();
            final ByteBuffer buffer = ByteBuffer.allocate(size);
            rawPermissions.serialize(buffer);
            buffer.flip();
            this.m_dimensionalBag.fromRawPermissions(rawPermissions);
            final DimensionalBagPermissionsMessage message = new DimensionalBagPermissionsMessage();
            message.setSerializedPermissions(buffer.array());
            WakfuClientInstance.getInstance();
            WakfuClientInstance.getGameEntity().getNetworkEntity().sendMessage(message);
        }
    }
    
    public void updatePermissions(final RawDimensionalBagPermissions rawPermissions) {
        DimensionalBagView.m_logger.info((Object)"[DIMENSIONAL BAG PERMISSIONS UPDATE]");
        this.m_dimensionalBag.fromRawPermissions(rawPermissions);
    }
    
    public void setMerchantInventoryLocked(final long merchantInventoryUid, final boolean locked) {
        if (WakfuGameEntity.getInstance().getLocalPlayer().getOwnedDimensionalBag() != this) {
            final MerchantInventory inventory = ((AbstractMerchantInventoryCollection<IT, MerchantInventory>)this.m_dimensionalBagFlea).getInventoryWithUid(merchantInventoryUid);
            if (inventory != null) {
                inventory.setLocked(locked);
                if (locked && Xulor.getInstance().isLoaded("confirmFleaPurchaseDialog")) {
                    Xulor.getInstance().unload("confirmFleaPurchaseDialog");
                }
                inventory.notifyChanges();
                PropertiesProvider.getInstance().firePropertyValueChanged(this, "fleaList");
            }
        }
    }
    
    public void leave() {
        WakfuClientInstance.getInstance();
        final WakfuGameEntity gameEntity = WakfuClientInstance.getGameEntity();
        final PSLeaveRequestMessage message = new PSLeaveRequestMessage();
        gameEntity.getNetworkEntity().sendMessage(message);
    }
    
    public Room putRoomContent(final RoomContent content) {
        return this.m_dimensionalBag.putRoomContent(content);
    }
    
    public boolean removeRoomContent(final RoomContent content) {
        return this.m_dimensionalBag.removeRoomContent(content);
    }
    
    public List<RoomContent> getRoomContents() {
        final ArrayList<RoomContent> contents = new ArrayList<RoomContent>();
        for (final Room room : this.m_dimensionalBag.getLayout()) {
            contents.addAll(room.getContents());
        }
        return contents;
    }
    
    public boolean canPlayerInteractWithContentInRoom(final LocalPlayerCharacter player, final int worldCellX, final int worldCellY) {
        if (player.getOwnedDimensionalBag() == this) {
            return true;
        }
        final GemControlledRoom room = (GemControlledRoom)this.m_dimensionalBag.getLayout().getRoomFromUnit(worldCellX, worldCellY);
        final Item gem = room.getGem(true);
        return gem != null && this.m_dimensionalBag.canCharacterAccessRoom(player.getId(), gem.getReferenceId());
    }
    
    public void release() {
        this.m_dimensionalBag.release();
    }
    
    public void setCurrentMerchantInventory(final MerchantInventory currentMerchantInventory) {
        this.m_currentMerchantInventory = currentMerchantInventory;
        final AbstractOccupation currentOccupation = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOccupation();
        if (currentOccupation != null && this.m_currentMerchantInventory != null) {
            if (currentOccupation instanceof MerchantDisplayOwner) {
                final MerchantDisplayOwner merchantDisplayOwner = (MerchantDisplayOwner)currentOccupation;
                this.m_currentMerchantInventory.setFleaTitle(merchantDisplayOwner.getMerchantDisplay().getName());
                this.m_currentMerchantInventory.setGfxId(merchantDisplayOwner.getMerchantDisplay().getGfxId());
                this.m_currentMerchantInventory.setFleaMarketRegistered(merchantDisplayOwner.getMerchantDisplay().isMarketregistered());
            }
            else {
                final ObjectPair op = UIBrowseDimensionalBagFleaFrame.getInstance().getFleaRefItem(this.m_currentMerchantInventory.getUid());
                final AbstractReferenceItem referenceItem = op.getSecond();
                this.m_currentMerchantInventory.setFleaTitle(referenceItem.getName());
                this.m_currentMerchantInventory.setGfxId(referenceItem.getGfxId());
                this.m_currentMerchantInventory.setSize(op.getFirst());
            }
            this.m_currentMerchantInventory.notifyChanges();
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentFlea");
    }
    
    public MerchantInventory getCurrentMerchantInventory() {
        return this.m_currentMerchantInventory;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagView.class);
        FIELDS = new String[] { "selectedName", "fleaList", "currentFlea", "selectedRoom", "room0", "room1", "room2", "room3", "room4", "room5", "room6", "room7", "room8" };
    }
}
