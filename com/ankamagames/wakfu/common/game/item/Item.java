package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.logs.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.bag.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.item.companion.*;
import com.ankamagames.wakfu.common.game.item.elements.*;
import com.ankamagames.wakfu.common.game.item.mergeSet.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.common.game.item.gems.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.pet.exception.*;
import com.ankamagames.wakfu.common.game.item.xp.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;
import com.ankamagames.wakfu.common.game.item.rent.*;
import com.ankamagames.wakfu.common.game.item.bind.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.exception.*;
import java.util.regex.*;

public class Item implements BagItem, InventoryContent, WakfuEffectContainer, Releasable, FieldProvider, RawConvertible<RawInventoryItem>, LoggableEntity, PetHolder, ItemXpHolder, GemsHolder
{
    private static final Logger m_logger;
    protected static final ItemComposer m_itemComposer;
    public static final short DEFAULT_AGGRO_WEIGHT = 1;
    private static final short DEFAULT_ALLY_EFFICACITY = 0;
    private static final short DEFAULT_FOE_EFFICACITY = 1;
    private static final int NB_DOUBLE_RELEASES_SAMPLES = 20;
    private static boolean traceRelease;
    private static int nbDoubleRelease;
    private final Collection<BagItemListener> m_listeners;
    private long m_uniqueId;
    private long m_timeStamp;
    private boolean m_active;
    private AbstractReferenceItem m_referenceItem;
    private short m_quantity;
    private boolean m_notPooled;
    private boolean m_isReleased;
    @Nullable
    private Pet m_pet;
    @Nullable
    private ItemXp m_xp;
    @Nullable
    private Gems m_gems;
    @Nullable
    private RentInfo m_rentInfo;
    @Nullable
    private CompanionItemInfo m_companionInfo;
    @Nullable
    private ItemBind m_bind;
    @Nullable
    private MultiElementsInfo m_multiElementsInfo;
    @Nullable
    private MergedSetInfo m_mergedSetItems;
    
    public Item() {
        super();
        this.m_listeners = new ArrayList<BagItemListener>();
        this.m_notPooled = true;
        this.m_active = true;
    }
    
    public Item(final long uniqueId) {
        super();
        this.m_listeners = new ArrayList<BagItemListener>();
        this.m_uniqueId = uniqueId;
        this.m_notPooled = true;
        this.m_active = true;
        this.resetCache();
    }
    
    public static Item newInstance(final AbstractReferenceItem refItem) {
        return newInstance(GUIDGenerator.getGUID(), refItem);
    }
    
    public static Item newInstance(final long uniqueId, final AbstractReferenceItem refItem) {
        try {
            final Item item = new Item();
            item.m_uniqueId = uniqueId;
            item.m_referenceItem = refItem;
            final PetDefinition petDefinition = PetDefinitionManager.INSTANCE.getFromRefItemId(refItem.getId());
            if (petDefinition != null) {
                item.m_pet = PetFactory.INSTANCE.createPet(petDefinition);
            }
            final ItemXpDefinition xpDefinition = ItemXpDefinitionManager.INSTANCE.get(refItem.getItemXpDefinitionId());
            if (xpDefinition != null) {
                item.m_xp = ItemXpFactory.create(xpDefinition);
            }
            if (refItem.isGemmable()) {
                item.m_gems = GemsHandlerFactory.create(refItem);
            }
            if (refItem.getBindType() != ItemBindType.NOT_BOUND) {
                item.m_bind = ItemBindFactory.create(refItem.getBindType());
            }
            if (refItem.hasRandomElementEffect()) {
                item.m_multiElementsInfo = new MultiElementsInfo();
            }
            item.m_timeStamp = 0L;
            item.m_notPooled = true;
            item.m_active = true;
            item.resetCache();
            return item;
        }
        catch (Exception e) {
            Item.m_logger.error((Object)"Erreur lors d'un checkOut sur un Item : ", (Throwable)e);
            return null;
        }
    }
    
    public void initializeWithReferenceItem(final AbstractReferenceItem refItem) {
        this.m_referenceItem = refItem;
        final PetDefinition petDefinition = PetDefinitionManager.INSTANCE.getFromRefItemId(refItem.getId());
        if (petDefinition != null) {
            this.m_pet = PetFactory.INSTANCE.createPet(petDefinition);
        }
        final ItemXpDefinition xpDefinition = ItemXpDefinitionManager.INSTANCE.get(refItem.getItemXpDefinitionId());
        if (xpDefinition != null) {
            this.m_xp = ItemXpFactory.create(xpDefinition);
        }
        if (refItem.isGemmable()) {
            this.m_gems = GemsHandlerFactory.create(refItem);
        }
        if (refItem.getBindType() != ItemBindType.NOT_BOUND) {
            this.m_bind = ItemBindFactory.create(refItem.getBindType());
        }
        if (refItem.hasRandomElementEffect()) {
            this.m_multiElementsInfo = new MultiElementsInfo();
        }
    }
    
    @Override
    public Item getCopy(final boolean pooled) {
        return this.getCopy(Item.m_itemComposer.getUidGenerator().getNextUID(), pooled);
    }
    
    public Item getInactiveCopy() {
        final Item item = new InactiveItem(Item.m_itemComposer.getUidGenerator().getNextUID(), this.m_referenceItem, this);
        item.setQuantity(this.m_quantity);
        item.m_timeStamp = this.m_timeStamp;
        item.m_active = false;
        return item;
    }
    
    public Item getCopy(final long itemId, final boolean pooled) {
        Item item;
        if (pooled) {
            item = newInstance(itemId, this.m_referenceItem);
        }
        else {
            item = new Item(itemId);
            item.m_referenceItem = this.m_referenceItem;
        }
        this.initializeOptionnalInfos(item);
        item.setQuantity(this.m_quantity);
        item.m_timeStamp = this.m_timeStamp;
        return item;
    }
    
    public void initializeOptionnalInfos(final Item item) {
        if (this.m_pet != null) {
            item.m_pet = PetFactory.INSTANCE.copyPet(this.m_pet);
        }
        else {
            item.m_pet = null;
        }
        if (this.m_xp != null) {
            item.m_xp = ItemXpFactory.copy(this.m_xp);
        }
        else {
            item.m_xp = null;
        }
        if (this.m_gems != null) {
            item.m_gems = GemsHandlerFactory.copy(this.m_referenceItem, this.m_gems);
        }
        else {
            item.m_gems = null;
        }
        if (this.m_rentInfo != null) {
            item.m_rentInfo = this.m_rentInfo;
        }
        else {
            item.m_rentInfo = null;
        }
        if (this.m_companionInfo != null) {
            item.m_companionInfo = new CompanionItemInfo(this.m_companionInfo.getXp());
        }
        else {
            item.m_companionInfo = null;
        }
        if (this.m_bind != null) {
            item.m_bind = new ItemBindModel(this.m_bind.getType(), this.m_bind.getData());
        }
        else {
            item.m_bind = null;
        }
        if (this.m_multiElementsInfo != null) {
            item.m_multiElementsInfo = new MultiElementsInfo(this.m_multiElementsInfo);
        }
        else {
            item.m_multiElementsInfo = null;
        }
        if (this.m_mergedSetItems != null) {
            item.m_mergedSetItems = new MergedSetInfo(this.m_mergedSetItems);
        }
        else {
            item.m_mergedSetItems = null;
        }
    }
    
    @Override
    public Item getClone() {
        return this.getCopy(this.m_uniqueId, Item.m_itemComposer.isPooledByDefault());
    }
    
    @Override
    public void release() {
        if (this.m_notPooled) {
            return;
        }
        if (Item.traceRelease) {
            Item.m_logger.info((Object)("On release un Item, hashCode = " + this.hashCode() + " StackTrace = " + ExceptionFormatter.currentStackTrace(1, 1)));
        }
        if (this.m_isReleased) {
            this.logItemReleased();
        }
    }
    
    @Override
    public void onCheckOut() {
        if (Item.traceRelease) {
            Item.m_logger.info((Object)("Item checkout, hashCode = " + this.hashCode() + " stackTrace = " + ExceptionFormatter.currentStackTrace(5, 1)));
        }
        this.m_isReleased = false;
    }
    
    @Override
    public void onCheckIn() {
        final ItemDisplayer provider = Item.m_itemComposer.getFieldProvider();
        if (provider != null) {
            provider.cleanUp(this);
        }
        this.m_referenceItem = null;
        this.m_pet = null;
        this.m_xp = null;
        this.m_gems = null;
        this.m_multiElementsInfo = null;
        this.m_mergedSetItems = null;
        this.m_isReleased = true;
        this.m_uniqueId = 0L;
        this.m_timeStamp = 0L;
        this.m_active = false;
        this.m_quantity = 0;
        this.m_listeners.clear();
    }
    
    @Override
    public boolean addListener(final BagItemListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    @Override
    public boolean removeListener(final BagItemListener listener) {
        return this.m_listeners.remove(listener);
    }
    
    @Override
    public boolean hasPet() {
        return this.m_pet != null;
    }
    
    @Override
    public Pet getPet() throws PetException {
        if (this.m_pet == null) {
            throw new PetException("Aucun familier d\u00ef¿½fini sur l'item " + this);
        }
        return this.m_pet;
    }
    
    @Override
    public boolean hasXp() {
        return this.m_xp != null;
    }
    
    @Override
    public ItemXp getXp() {
        if (this.m_xp == null) {
            throw new ItemXpException("Aucune xp d\u00ef¿½finie sur l'item " + this);
        }
        return this.m_xp;
    }
    
    @Override
    public boolean hasGems() {
        return this.m_gems != null;
    }
    
    @Override
    public boolean hasGemsSlotted() {
        return this.hasGems() && this.getGems().hasGems();
    }
    
    @Override
    public Gems getGems() throws GemsException {
        if (this.m_gems == null) {
            throw new GemsException("Aucune gemme d\u00ef¿½finie sur l'item " + this);
        }
        return this.m_gems;
    }
    
    @Override
    public short getQuantity() {
        return this.m_quantity;
    }
    
    @Override
    public void setQuantity(final short quantity) {
        if (this.hasPet() && quantity > 1) {
            throw new PetException("Les stacks d'item ne sont pas autoris\u00ef¿½s pour l'item " + this);
        }
        if (quantity == 0) {
            throw new IllegalArgumentException("Impossible de fixer la quantit\u00ef¿½ de l'item " + this.getReferenceId() + " \u00ef¿½ 0 : il doit \u00ef¿½tre d\u00ef¿½truit.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Impossible de fixer une quantit\u00ef¿½ n\u00ef¿½gative pour l'item " + this.getReferenceId());
        }
        if (quantity > this.getStackMaximumHeight()) {
            throw new IllegalArgumentException("Impossible de fixer la quantit\u00ef¿½ de l'objet " + this.getReferenceId() + " \u00ef¿½ " + quantity + " : stackMaximumHeight=" + this.getStackMaximumHeight());
        }
        this.m_quantity = quantity;
        final ItemQuantityChangeListener quantityChangeListener = Item.m_itemComposer.getQuantityChangeListener();
        if (quantityChangeListener != null) {
            quantityChangeListener.onQuantityChanged(this);
        }
    }
    
    @Override
    public void updateQuantity(final short quantityUpdate) {
        this.setQuantity((short)(this.m_quantity + quantityUpdate));
    }
    
    public short getStackFreePlace() {
        return (short)(this.getStackMaximumHeight() - this.m_quantity);
    }
    
    @Override
    public short getStackMaximumHeight() {
        return this.m_referenceItem.getStackMaximumHeight();
    }
    
    @Override
    public int getContainerType() {
        return 12;
    }
    
    @Override
    public long getEffectContainerId() {
        return this.m_referenceItem.getId();
    }
    
    @Override
    public short getLevel() {
        if (this.m_pet != null) {
            return this.m_pet.getLevel();
        }
        if (this.m_xp != null) {
            return this.m_xp.getLevel();
        }
        if (this.m_referenceItem != null) {
            return this.m_referenceItem.getLevel();
        }
        return 0;
    }
    
    public short getMaxLevel() {
        if (this.m_pet != null) {
            return this.m_pet.getMaxLevel();
        }
        if (this.m_xp != null) {
            return this.m_xp.getMaxLevel();
        }
        if (this.m_referenceItem != null) {
            return this.m_referenceItem.getLevel();
        }
        return 0;
    }
    
    @Override
    public ItemRarity getRarity() {
        return this.m_referenceItem.getRarity();
    }
    
    @Override
    public Iterator<WakfuEffect> iterator() {
        if (!this.hasGems()) {
            return (Iterator<WakfuEffect>)this.m_referenceItem.getEffectsIterator();
        }
        return this.getBaseAndGemsEffectsIterator();
    }
    
    public Iterator<WakfuEffect> getGemsEffectsIterator() {
        final MergedIterator<WakfuEffect> effectMergedIterator = new MergedIterator<WakfuEffect>();
        final Gems gems = this.getGems();
        for (byte i = 0, size = gems.getSlotCount(); i < size; ++i) {
            if (gems.hasGem(i)) {
                final int gemReferenceItemId = gems.getGem(i);
                final AbstractReferenceItem gemReferenceItem = ReferenceItemManager.getInstance().getReferenceItem(gemReferenceItemId);
                effectMergedIterator.merge(gemReferenceItem.getEffectsIterator());
            }
        }
        return effectMergedIterator;
    }
    
    public Iterator<WakfuEffect> getBaseAndGemsEffectsIterator() {
        final MergedIterator<WakfuEffect> effectMergedIterator = new MergedIterator<WakfuEffect>();
        effectMergedIterator.merge(this.m_referenceItem.getEffectsIterator());
        final Gems gems = this.getGems();
        for (byte i = 0, size = gems.getSlotCount(); i < size; ++i) {
            if (gems.hasGem(i)) {
                final int gemReferenceItemId = gems.getGem(i);
                final AbstractReferenceItem gemReferenceItem = ReferenceItemManager.getInstance().getReferenceItem(gemReferenceItemId);
                effectMergedIterator.merge(gemReferenceItem.getEffectsIterator());
            }
        }
        return effectMergedIterator;
    }
    
    @Override
    public short getAggroWeight() {
        return 1;
    }
    
    @Override
    public short getAllyEfficacity() {
        return 0;
    }
    
    @Override
    public short getFoeEfficacity() {
        return 1;
    }
    
    public int getGfxId() {
        return this.m_referenceItem.getGfxId();
    }
    
    public int getFemaleGfxId() {
        return this.m_referenceItem.getFemaleGfxId();
    }
    
    public int getFloorGfxId() {
        return this.m_referenceItem.getFloorGfxId();
    }
    
    public String getName() {
        final ItemDisplayer provider = Item.m_itemComposer.getFieldProvider();
        return (provider != null) ? provider.getName(this) : "<unknown>";
    }
    
    public String getIconUrl() {
        final ItemDisplayer provider = Item.m_itemComposer.getFieldProvider();
        return (provider != null) ? provider.getIconUrl(this) : "<unknown>";
    }
    
    @Override
    public String[] getFields() {
        final ItemDisplayer provider = Item.m_itemComposer.getFieldProvider();
        return (String[])((provider != null) ? provider.getFields() : null);
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        final ItemDisplayer provider = Item.m_itemComposer.getFieldProvider();
        return (provider != null) ? provider.getFieldValue(this, fieldName) : null;
    }
    
    public void resetCache() {
        final ItemDisplayer provider = Item.m_itemComposer.getFieldProvider();
        if (provider != null) {
            provider.resetCache(this);
        }
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    @Nullable
    public byte[] serialize() {
        final RawInventoryItem rawItem = new RawInventoryItem();
        if (this.toRaw(rawItem)) {
            final byte[] data = new byte[rawItem.serializedSize()];
            if (rawItem.serialize(ByteBuffer.wrap(data))) {
                return data;
            }
        }
        return null;
    }
    
    public void unserialize(final int version, final ByteBuffer bb) {
        final RawInventoryItem rawItem = new RawInventoryItem();
        rawItem.unserializeVersion(bb, version);
        this.fromRaw(rawItem);
    }
    
    @Override
    public boolean toRaw(final RawInventoryItem raw) {
        raw.uniqueId = this.m_uniqueId;
        raw.refId = this.m_referenceItem.getId();
        raw.quantity = this.m_quantity;
        if (this.m_timeStamp > 0L) {
            raw.timestamp = new RawInventoryItem.Timestamp();
            raw.timestamp.timestampValue = this.m_timeStamp;
        }
        else {
            raw.timestamp = null;
        }
        if (this.m_pet != null) {
            raw.pet = new RawInventoryItem.Pet();
            this.m_pet.toRaw(raw.pet.rawPet);
        }
        else {
            raw.pet = null;
        }
        if (this.m_xp != null) {
            raw.xp = new RawInventoryItem.Xp();
            this.m_xp.toRaw(raw.xp.rawXp);
        }
        else {
            raw.xp = null;
        }
        if (this.m_gems != null) {
            raw.gems = new RawInventoryItem.Gems();
            this.m_gems.toRaw(raw.gems.rawGems);
        }
        else {
            raw.gems = null;
        }
        if (this.m_rentInfo != null) {
            raw.rentInfo = new RawInventoryItem.RentInfo();
            this.m_rentInfo.toRaw(raw.rentInfo.rawRentInfo);
        }
        else {
            raw.rentInfo = null;
        }
        if (this.m_companionInfo != null) {
            raw.companionInfo = new RawInventoryItem.CompanionInfo();
            this.m_companionInfo.toRaw(raw.companionInfo.rawCompanionInfo);
        }
        else {
            raw.companionInfo = null;
        }
        if (this.m_bind != null) {
            raw.bind = new RawInventoryItem.Bind();
            this.m_bind.toRaw(raw.bind.rawItemBind);
        }
        else {
            raw.bind = null;
        }
        if (this.m_multiElementsInfo != null) {
            raw.elements = new RawInventoryItem.Elements();
            this.m_multiElementsInfo.toRaw(raw.elements.rawItemElements);
        }
        else {
            raw.elements = null;
        }
        if (this.m_mergedSetItems != null) {
            raw.mergedItems = new RawInventoryItem.MergedItems();
            this.m_mergedSetItems.toRaw(raw.mergedItems.rawMergedItems);
        }
        else {
            raw.mergedItems = null;
        }
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawInventoryItem raw) {
        this.m_uniqueId = raw.uniqueId;
        this.m_referenceItem = ReferenceItemManager.getInstance().getReferenceItem(raw.refId);
        if (this.m_referenceItem == null) {
            Item.m_logger.error((Object)("Impossible de d\u00ef¿½s\u00ef¿½rialiser un item : item d'id " + raw.refId + " inconnu UID : " + this.m_uniqueId), (Throwable)new Exception());
            return false;
        }
        this.m_quantity = raw.quantity;
        this.m_timeStamp = ((raw.timestamp != null) ? raw.timestamp.timestampValue : 0L);
        if (raw.pet != null) {
            this.restorePet(raw.pet.rawPet);
            final PetController petController = new PetController(this);
            final GameInterval hungryInterval = this.m_pet.getLastHungryDate().timeTo(WakfuGameCalendar.getInstance().getDate());
            final int cycles = MathHelper.clamp(hungryInterval.getDivisionResult(this.m_pet.getDefinition().getMealMaxInterval()), 0, 3);
            final int penaltyValue = this.m_pet.getDefinition().getHealthPenalty(HealthPenaltyType.MAX_MEAL_INTERVAL) * cycles;
            petController.setHealth(this.m_pet.getHealth() - penaltyValue);
        }
        if (raw.xp != null) {
            this.restoreXp(raw.xp.rawXp);
        }
        if (raw.gems != null) {
            final RawGems rawGems = raw.gems.rawGems;
            this.restoreGems(rawGems);
        }
        else if (this.m_referenceItem.isGemmable()) {
            this.m_gems = GemsHandlerFactory.create(this.m_referenceItem);
        }
        if (raw.rentInfo != null) {
            this.m_rentInfo = RentInfoSerializer.unserialize(raw.rentInfo.rawRentInfo);
        }
        else {
            this.m_rentInfo = null;
        }
        if (raw.companionInfo != null) {
            this.m_companionInfo = new CompanionItemInfo(raw.companionInfo.rawCompanionInfo.xp);
        }
        else {
            this.m_companionInfo = null;
        }
        if (raw.bind != null) {
            this.m_bind = ItemBindSerializer.unserialize(raw.bind.rawItemBind);
        }
        else {
            this.m_bind = null;
        }
        if (raw.elements != null) {
            this.m_multiElementsInfo = MultiElementsInfo.unserialize(raw.elements.rawItemElements);
        }
        else {
            this.m_multiElementsInfo = null;
        }
        if (raw.mergedItems != null) {
            this.m_mergedSetItems = MergedSetInfo.fromRaw(raw.mergedItems.rawMergedItems);
        }
        else {
            this.m_mergedSetItems = null;
        }
        this.checkBind();
        return true;
    }
    
    private void checkBind() {
        if (this.m_bind != null && this.m_bind.getType().isShop()) {
            return;
        }
        final ItemBindType bindType = this.m_referenceItem.getBindType();
        if (bindType == ItemBindType.NOT_BOUND) {
            this.m_bind = null;
            return;
        }
        if (this.m_bind == null || this.m_bind.getType() != bindType) {
            this.m_bind = ItemBindFactory.create(bindType);
        }
    }
    
    @Override
    public boolean shouldBeSerialized() {
        return this.m_active;
    }
    
    public static ItemComposer getItemComposer() {
        return Item.m_itemComposer;
    }
    
    public AbstractReferenceItem getReferenceItem() {
        return this.m_referenceItem;
    }
    
    @Override
    public long getUniqueId() {
        return this.m_uniqueId;
    }
    
    @Override
    public int getReferenceId() {
        return this.m_referenceItem.getId();
    }
    
    public AbstractItemType<AbstractItemType> getType() {
        return (AbstractItemType<AbstractItemType>)this.m_referenceItem.getItemType();
    }
    
    public boolean isUsableInFight() {
        return this.m_referenceItem.isUsableInFight();
    }
    
    public boolean isUsableInWorld() {
        return this.m_referenceItem.isUsableInWorld();
    }
    
    public long getTimeStamp() {
        return this.m_timeStamp;
    }
    
    public void setTimeStamp(final long timeStamp) {
        this.m_timeStamp = timeStamp;
    }
    
    public boolean isActive() {
        return this.m_active;
    }
    
    public boolean isUsable() {
        return this.m_referenceItem.isUsableInFight() || this.m_referenceItem.isUsableInWorld();
    }
    
    public boolean isStackable() {
        return this.canStackWith(this);
    }
    
    public boolean isMergeableIntoSet() {
        return this.canMergeIntoSet(this);
    }
    
    @Override
    public boolean canStackWith(final InventoryContent inv) {
        if (!(inv instanceof Item)) {
            return false;
        }
        final Item item = (Item)inv;
        if (item.getQuantity() + this.getQuantity() > this.getStackMaximumHeight()) {
            return false;
        }
        if (item.hasBind() ^ this.hasBind()) {
            return false;
        }
        if (this.m_bind != null) {
            final ItemBind bind = item.m_bind;
            if (!this.m_bind.equals(bind)) {
                return false;
            }
        }
        final boolean hasRandomElementEffect = this.m_referenceItem.hasRandomElementEffect();
        if (hasRandomElementEffect) {
            if (this.randomElementsInit() != item.randomElementsInit()) {
                return false;
            }
            if (!this.needRollRandomElementsEffect() || !item.needRollRandomElementsEffect()) {
                return false;
            }
        }
        return this.getReferenceId() == item.getReferenceId() && !this.hasXp() && !item.hasXp() && !this.hasPet() && !item.hasPet() && !this.hasGemsSlotted() && !item.hasGemsSlotted() && !this.isRent() && !item.isRent() && !this.hasCompanionInfo() && !item.hasCompanionInfo() && this.m_mergedSetItems == null && item.m_mergedSetItems == null && (this.m_timeStamp <= 0L || this.m_timeStamp == item.m_timeStamp);
    }
    
    public boolean canMergeIntoSet(final InventoryContent inv) {
        if (!(inv instanceof Item)) {
            return false;
        }
        final Item item = (Item)inv;
        if (item.hasBind() ^ this.hasBind()) {
            return false;
        }
        if (this.m_bind != null) {
            final ItemBind bind = item.m_bind;
            if (!this.m_bind.equals(bind)) {
                return false;
            }
        }
        return this.getReferenceId() == item.getReferenceId() && !this.hasXp() && !item.hasXp() && !this.hasPet() && !item.hasPet() && !this.hasGemsSlotted() && !item.hasGemsSlotted() && !this.isRent() && !item.isRent() && !this.hasCompanionInfo() && !item.hasCompanionInfo() && (this.m_timeStamp <= 0L || this.m_timeStamp == item.m_timeStamp);
    }
    
    public void restorePet(final RawPet rawPet) {
        if (rawPet != null) {
            this.m_pet = PetFactory.INSTANCE.createPet(rawPet);
        }
    }
    
    public void restoreXp(final RawItemXp rawItemXp) {
        if (rawItemXp != null) {
            this.m_xp = ItemXpFactory.create(rawItemXp);
        }
    }
    
    public void restoreGems(final RawGems rawGems) {
        this.m_gems = ((rawGems != null) ? GemsHandlerFactory.create(this.m_referenceItem, rawGems) : GemsHandlerFactory.create(this.m_referenceItem));
    }
    
    public void restoreCompanion(final RawCompanionInfo rawCompanion) {
        if (rawCompanion != null) {
            (this.m_companionInfo = new CompanionItemInfo()).setXp(rawCompanion.xp);
        }
    }
    
    public HashSet<Elements> getElementsForRunningEffect(final int actionId) {
        return this.m_multiElementsInfo.get(actionId);
    }
    
    public boolean hasBind() {
        return this.m_bind != null;
    }
    
    public boolean isBound() {
        return this.m_bind != null && this.m_bind.getData() != 0L;
    }
    
    public boolean isBoundToAccount() {
        return this.m_bind != null && this.m_bind.getData() != 0L && !this.m_bind.getType().isCharacter();
    }
    
    public boolean isBoundToCharacter() {
        return this.m_bind != null && this.m_bind.getData() != 0L && this.m_bind.getType().isCharacter();
    }
    
    @Nullable
    public ItemBind getBind() {
        return this.m_bind;
    }
    
    public void setBind(@Nullable final ItemBind bind) {
        this.m_bind = bind;
    }
    
    public void setMultiElementsInfo(@Nullable final MultiElementsInfo multiElementsInfo) {
        this.m_multiElementsInfo = multiElementsInfo;
    }
    
    public void rollRandomElementsEffects() {
        if (!this.randomElementsInit()) {
            return;
        }
        this.m_multiElementsInfo.rollRandomElementsEffects(this);
    }
    
    public void rollRandomElementsEffectsIfNecessary() {
        if (!this.needRollRandomElementsEffect()) {
            return;
        }
        this.rollRandomElementsEffects();
    }
    
    public boolean needRollRandomElementsEffect() {
        return !this.randomElementsInit() || this.isMultiElementInfoEmpty();
    }
    
    public boolean isMultiElementInfoEmpty() {
        return this.m_multiElementsInfo.isEmpty();
    }
    
    public boolean randomElementsInit() {
        return this.m_multiElementsInfo != null;
    }
    
    @Nullable
    public MultiElementsInfo getMultiElementsInfo() {
        return this.m_multiElementsInfo;
    }
    
    @Nullable
    public RentInfo getRentInfo() {
        return this.m_rentInfo;
    }
    
    public void setRentInfo(@Nullable final RentInfo rentInfo) {
        this.m_rentInfo = rentInfo;
    }
    
    public boolean isRent() {
        return this.m_rentInfo != null;
    }
    
    public boolean isExpiredRent() {
        return this.m_rentInfo != null && this.m_rentInfo.isExpired();
    }
    
    @Nullable
    public CompanionItemInfo getCompanionInfo() {
        return this.m_companionInfo;
    }
    
    public void setCompanionInfo(@Nullable final CompanionItemInfo companionInfo) {
        this.m_companionInfo = companionInfo;
    }
    
    public boolean hasCompanionInfo() {
        return this.m_companionInfo != null;
    }
    
    public boolean isRentalExpired() {
        return this.m_rentInfo != null && this.m_rentInfo.isExpired();
    }
    
    private void logItemReleased() {
        Item.m_logger.error((Object)new InventoryException("Attention : Double release sur un Item, Hashcode = " + this.hashCode()));
        if (Item.traceRelease) {
            Item.m_logger.info((Object)("Double release sur un item qu'on a trace, hashCode = " + this.hashCode()));
            ++Item.nbDoubleRelease;
            if (Item.nbDoubleRelease >= 20) {
                Item.traceRelease = false;
            }
        }
        this.m_notPooled = true;
    }
    
    public String getChatLogRepresentation() {
        final StringBuilder repr = new StringBuilder();
        repr.append('[').append(this.m_uniqueId).append(']');
        if (!this.m_active) {
            repr.append('i');
        }
        repr.append(this.getReferenceId());
        if (this.m_quantity != 1) {
            repr.append('x').append(this.m_quantity);
        }
        return repr.toString();
    }
    
    public void addMergeSetItem(final Item item) {
        if (this.m_mergedSetItems == null) {
            this.m_mergedSetItems = new MergedSetInfo();
        }
        this.m_mergedSetItems.add(item);
    }
    
    public void setMergedSetItems(@Nullable final MergedSetInfo mergedSetItems) {
        this.m_mergedSetItems = mergedSetItems;
    }
    
    @Nullable
    public MergedSetInfo getMergedSetItems() {
        return this.m_mergedSetItems;
    }
    
    @Override
    public String getLogRepresentation() {
        final StringBuilder repr = new StringBuilder();
        repr.append('{');
        repr.append("uid=").append(this.m_uniqueId);
        repr.append(", refId=").append(this.getReferenceId());
        repr.append(", qty=").append(this.m_quantity);
        repr.append(", opt=");
        if (this.hasGems() && this.m_gems != null) {
            repr.append(this.m_gems.getLogRepresentation());
        }
        else if (this.hasXp() && this.m_xp != null) {
            repr.append(this.m_xp.getLogRepresentation());
        }
        else if (this.hasPet() && this.m_pet != null) {
            repr.append(this.m_pet.getLogRepresentation());
        }
        else {
            repr.append("none");
        }
        if (this.m_rentInfo != null) {
            repr.append("rentInfo=").append(this.m_rentInfo);
        }
        if (this.m_bind != null) {
            repr.append("bind=").append(this.m_bind);
        }
        if (this.m_companionInfo != null) {
            repr.append("companionInfo=").append(this.m_companionInfo);
        }
        repr.append('}');
        return repr.toString();
    }
    
    @Override
    public String toString() {
        return "Item{m_uniqueId=" + this.m_uniqueId + ", m_referenceItem=" + this.m_referenceItem + ", m_quantity=" + this.m_quantity + ", m_pet=" + this.m_pet + ", m_xp=" + this.m_xp + ", m_gems=" + this.m_gems + ", m_rentInfo=" + this.m_rentInfo + ", m_companionInfo=" + this.m_companionInfo + ", m_mergedSetItems=" + this.m_mergedSetItems + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)Item.class);
        m_itemComposer = new ItemComposer();
    }
    
    public static class LogRepresentation
    {
        public static final Pattern ITEM_PATTERN;
        public long uid;
        public int refid;
        public short quantity;
        public long timestamp;
        public boolean inactive;
        public byte merchantPackSize;
        public int merchantPrice;
        
        public static LogRepresentation parse(final String itemRepr) {
            final Matcher matcher = LogRepresentation.ITEM_PATTERN.matcher(itemRepr);
            if (matcher.matches()) {
                final LogRepresentation result = new LogRepresentation();
                result.uid = Long.parseLong(matcher.group(1));
                if (matcher.group(2) != null) {
                    result.inactive = true;
                }
                result.refid = Integer.parseInt(matcher.group(3));
                if (matcher.group(4) != null) {
                    result.quantity = Short.parseShort(matcher.group(4));
                }
                if (matcher.group(6) != null) {
                    result.timestamp = Long.parseLong(matcher.group(6));
                }
                if (matcher.group(8) != null) {
                    result.merchantPackSize = Byte.parseByte(matcher.group(8));
                    result.merchantPrice = Integer.parseInt(matcher.group(9));
                }
                return result;
            }
            return null;
        }
        
        public static Item toItem(final LogRepresentation logRepresentation) {
            final Item item = new Item(logRepresentation.uid);
            item.m_referenceItem = ReferenceItemManager.getInstance().getReferenceItem(logRepresentation.refid);
            item.m_quantity = (short)((logRepresentation.quantity > 0) ? logRepresentation.quantity : 1);
            item.m_timeStamp = logRepresentation.timestamp;
            return item;
        }
        
        static {
            ITEM_PATTERN = Pattern.compile("\\[(\\d+)\\](i)?(\\d+)(?:x(\\d+))?(?:c(\\d+))?(?:e(\\d+))?(?:\\{(\\w+)\\})?(?:p(\\d+)k(\\d+))?");
        }
    }
}
