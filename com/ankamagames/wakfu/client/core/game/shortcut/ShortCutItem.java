package com.ankamagames.wakfu.client.core.game.shortcut;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.core.emote.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ShortCutItem extends AbstractShortCutItem implements FieldProvider
{
    public static final String UID_FIELD = "uid";
    public static final String RID_FIELD = "rid";
    public static final String SMALL_ICON_URL_FIELD = "smallIconUrl";
    public static final String TYPE_FIELD = "type";
    public static final String USABLE_FIELD = "usable";
    public static final String BACKGROUND_TYPE_ICON_FIELD = "backgroundTypeIcon";
    public static final String QUANTITY_FIELD = "quantity";
    public static final String COOLDOWN_FIELD = "cooldown";
    public static final String PARTICLE_FILE = "particleFile";
    private byte m_state;
    public static final byte UNEQUIPED = 0;
    public static final byte UNEQUIPED_UNEQUIPABLE = 1;
    public static final byte EQUIPED_USABLE = 2;
    public static final byte EQUIPED_UNUSABLE = 3;
    public static final byte UNEQUIPED_USABLE = 4;
    public static final byte UNEQUIPED_WEAPON = 5;
    private static final short[] m_stateBackground;
    public static final String[] FIELDS;
    private boolean m_usable;
    private static final ObjectPool m_staticPool;
    private final SpellCastValidatorForShortcuts m_spellCastValidator;
    
    private ShortCutItem() {
        super();
        this.m_usable = true;
        this.m_spellCastValidator = new SpellCastValidatorForShortcuts();
    }
    
    public static ShortCutItem checkOut() {
        ShortCutItem shortcut;
        try {
            shortcut = (ShortCutItem)ShortCutItem.m_staticPool.borrowObject();
            shortcut.m_pool = ShortCutItem.m_staticPool;
        }
        catch (Exception e) {
            shortcut = new ShortCutItem();
            shortcut.m_pool = null;
            shortcut.onCheckOut();
            ShortCutItem.m_logger.error((Object)("Erreur lors d'un checkOut sur un ShortCutItem : " + e.getMessage()));
        }
        return shortcut;
    }
    
    public static ShortCutItem checkOut(final ShortCutType type, final long uid, final int rid, final int gfxId) {
        final ShortCutItem shortcut = checkOut();
        shortcut.setItem(type, uid, rid, gfxId);
        return shortcut;
    }
    
    @Override
    public void release() {
        if (this.m_pool != null) {
            try {
                this.m_pool.returnObject(this);
            }
            catch (Exception e) {
                ShortCutItem.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + ". Normalement impossible"));
            }
            this.m_pool = null;
        }
        else {
            this.onCheckIn();
        }
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (fieldName.equals("uid")) {
            return this.getUniqueId();
        }
        if (fieldName.equals("rid")) {
            return this.getReferenceId();
        }
        if (fieldName.equals("type")) {
            return this.getType();
        }
        if (!fieldName.equals("smallIconUrl")) {
            if (fieldName.equals("usable")) {
                if (!this.m_usable) {
                    return this.m_usable;
                }
                CharacterInfo concernedFighter = UIFightTurnFrame.getInstance().getConcernedFighter();
                if (concernedFighter == null && localPlayer.isDead()) {
                    return false;
                }
                if (concernedFighter != null && concernedFighter.isDead()) {
                    return false;
                }
                switch (this.getType()) {
                    case EMOTE: {
                        return SubscriptionEmoteAndTitleLimitations.isAuthorizedEmote(localPlayer.getAccountInformationHandler().getActiveSubscriptionLevel(), this.getReferenceId());
                    }
                    case SPELL_LEVEL: {
                        final Fight fight = localPlayer.getCurrentFight();
                        if (fight == null) {
                            final SpellLevel spellLevelById = WakfuGameEntity.getInstance().getLocalPlayer().getSpellLevelById(this.getUniqueId());
                            if (spellLevelById.isLocked()) {
                                return false;
                            }
                            return true;
                        }
                        else {
                            if (concernedFighter == null || concernedFighter.isControlledByAI() || !concernedFighter.isControlledByLocalPlayer()) {
                                concernedFighter = localPlayer;
                            }
                            final SpellLevel spell = concernedFighter.getSpellLevelById(this.getUniqueId());
                            if (spell == null) {
                                return false;
                            }
                            if (!spell.getSpell().isAssociatedWithItemUse()) {
                                this.m_spellCastValidator.setLinkedFight(fight);
                                final CastValidity validity = this.m_spellCastValidator.getSpellCastValidity(concernedFighter, spell, null, true);
                                if (validity == CastValidity.OK) {
                                    spell.setCriterionOk(true);
                                    return true;
                                }
                                if (validity == CastValidity.CAST_CRITERIONS_NOT_VALID) {
                                    spell.setCriterionOk(false);
                                    return false;
                                }
                                spell.setCriterionOk(true);
                                return false;
                            }
                            else {
                                final Item contItem = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getFromPosition(spell.getSpell().getAssociatedItemPosition());
                                final CastValidity validity2 = fight.getItemAndSpellCastValidity(concernedFighter, contItem, spell, null);
                                if (validity2 == CastValidity.OK) {
                                    spell.setCriterionOk(true);
                                    return true;
                                }
                                if (validity2 == CastValidity.CAST_CRITERIONS_NOT_VALID) {
                                    spell.setCriterionOk(false);
                                    return false;
                                }
                                spell.setCriterionOk(true);
                                return false;
                            }
                        }
                        break;
                    }
                    case EQUIPMENT_SLOT: {
                        final Fight fight = localPlayer.getCurrentFight();
                        if (fight == null) {
                            return true;
                        }
                        Item item;
                        if (this.getReferenceId() != 2145) {
                            item = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getFromPosition((short)(-(this.getUniqueId() + 1L)));
                        }
                        else {
                            final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(2145);
                            final Item realItem = new Item(-1L);
                            realItem.initializeWithReferenceItem(refItem);
                            realItem.setQuantity((short)1);
                            item = realItem;
                            final CharacterInfo caster = UIFightTurnFrame.getInstance().getConcernedFighter();
                            if (caster == null) {
                                return false;
                            }
                        }
                        return item != null && item.isActive() && fight.getItemCastValidity(localPlayer, item, null, true) == CastValidity.OK;
                    }
                    case USABLE_REFERENCE_ITEM: {
                        final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(this.getReferenceId());
                        if (refItem == null) {
                            return false;
                        }
                        if (refItem.getItemAction() instanceof SeedItemAction) {
                            final int levelMin = ((SeedItemAction)refItem.getItemAction()).getLevelMin();
                            final int craftId = ((SeedItemAction)refItem.getItemAction()).getCraftId();
                            return localPlayer.getCraftHandler().contains(craftId) && localPlayer.getCraftHandler().getLevel(craftId) >= levelMin && this.getQuantity() > 0;
                        }
                        if (refItem.getLevel() > localPlayer.getLevel()) {
                            return false;
                        }
                        final Fight fight = localPlayer.getCurrentFight();
                        if (fight == null) {
                            final SimpleCriterion critOnUse = refItem.getCriterion(ActionsOnItem.USE);
                            final CharacterInfo caster = localPlayer;
                            return (critOnUse == null || critOnUse.isValid(caster, caster.getPosition(), null, caster.getEffectContext())) && this.getQuantity() > 0;
                        }
                        if (fight.getStatus() == AbstractFight.FightStatus.PLACEMENT) {
                            return ItemDisplayerImpl.isUsableInFight(refItem, localPlayer, fight, this.getUniqueId());
                        }
                        final CharacterInfo caster = UIFightTurnFrame.getInstance().getConcernedFighter();
                        if (caster == null) {
                            return false;
                        }
                        return ItemDisplayerImpl.isUsableInFight(refItem, caster, fight, this.getUniqueId());
                    }
                    case ITEM: {
                        Item contItem2 = localPlayer.getBags().getItemFromInventories(this.getUniqueId());
                        if (contItem2 != null && contItem2.isActive()) {
                            if (contItem2.getReferenceItem().getItemType().getEquipmentPositions().length == 0 || this.getState() == 4) {
                                CharacterInfo caster;
                                SimpleCriterion critOnUse;
                                if (localPlayer.isOnFight()) {
                                    critOnUse = contItem2.getReferenceItem().getCriterion(ActionsOnItem.USE_IN_FIGHT);
                                    caster = UIFightTurnFrame.getInstance().getConcernedFighter();
                                    if (caster == null) {
                                        return false;
                                    }
                                }
                                else {
                                    critOnUse = contItem2.getReferenceItem().getCriterion(ActionsOnItem.USE);
                                    caster = localPlayer;
                                }
                                return (critOnUse == null || critOnUse.isValid(caster, caster.getPosition(), contItem2, caster.getEffectContext())) && this.getQuantity() > 0;
                            }
                            CharacterInfo caster2 = localPlayer;
                            if (localPlayer.isOnFight()) {
                                if (!contItem2.getReferenceItem().isEquipmentPositionValid(EquipmentPosition.FIRST_WEAPON) && !contItem2.getReferenceItem().isEquipmentPositionValid(EquipmentPosition.SECOND_WEAPON)) {
                                    return false;
                                }
                                caster2 = UIFightTurnFrame.getInstance().getConcernedFighter();
                                if (caster2 == null) {
                                    return false;
                                }
                            }
                            final SimpleCriterion critOnEquip = contItem2.getReferenceItem().getCriterion(ActionsOnItem.EQUIP);
                            return (critOnEquip == null || critOnEquip.isValid(caster2, caster2.getPosition(), contItem2, caster2.getEffectContext())) && this.getQuantity() > 0;
                        }
                        else {
                            contItem2 = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getFirstWithReferenceId(this.getReferenceId());
                            if (contItem2 == null || !contItem2.isActive()) {
                                return this.getQuantity() > 0;
                            }
                            if (localPlayer.isActiveProperty(FightPropertyType.CANNOT_USE_ITEM_CAST)) {
                                return false;
                            }
                            if (contItem2.isExpiredRent()) {
                                return false;
                            }
                            if (localPlayer.isOnFight() && !contItem2.isUsableInFight()) {
                                return false;
                            }
                            CharacterInfo caster;
                            SimpleCriterion critOnUse;
                            if (localPlayer.isOnFight()) {
                                critOnUse = contItem2.getReferenceItem().getCriterion(ActionsOnItem.USE_IN_FIGHT);
                                caster = UIFightTurnFrame.getInstance().getConcernedFighter();
                                if (caster == null) {
                                    return false;
                                }
                            }
                            else {
                                critOnUse = contItem2.getReferenceItem().getCriterion(ActionsOnItem.USE);
                                caster = localPlayer;
                            }
                            return (critOnUse == null || critOnUse.isValid(caster, caster.getPosition(), contItem2, caster.getEffectContext())) && contItem2.getReferenceItem().getActionPoints() <= caster.getCharacteristicValue(FighterCharacteristicType.AP) && this.getQuantity() > 0;
                        }
                        break;
                    }
                }
            }
            else {
                if (fieldName.equals("cooldown")) {
                    switch (this.getType()) {
                        case SPELL_LEVEL: {
                            final Fight fight2 = localPlayer.getCurrentFight();
                            if (fight2 == null) {
                                return "";
                            }
                            final SpellLevel spell2 = localPlayer.getSpellLevelById(this.getUniqueId());
                            if (spell2 == null) {
                                break;
                            }
                            if (!spell2.getSpell().isAssociatedWithItemUse()) {
                                final CastValidity validity3 = localPlayer.getSpellLevelCastHistory().canCastSpell(spell2, fight2.getTimeline().getCurrentTableturn());
                                if (validity3 == CastValidity.OK) {
                                    return "";
                                }
                                if (validity3 == CastValidity.LAST_CAST_TOO_RECENT) {
                                    final int beforeBeingAbleToCastSpell = localPlayer.getSpellLevelCastHistory().getTurnsBeforeBeingAbleToCastSpell(spell2, fight2.getTimeline().getCurrentTableturn());
                                    if (beforeBeingAbleToCastSpell > 0) {
                                        return String.valueOf(beforeBeingAbleToCastSpell);
                                    }
                                    return String.valueOf('\u221e');
                                }
                                else {
                                    if (validity3 == CastValidity.TOO_MUCH_CASTS_THIS_TURN) {
                                        return "1";
                                    }
                                    return "";
                                }
                            }
                            else {
                                final Item contItem3 = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getFromPosition(spell2.getSpell().getAssociatedItemPosition());
                                final CastValidity validity = fight2.getItemAndSpellCastValidity(localPlayer, contItem3, spell2, null);
                                if (validity == CastValidity.OK) {
                                    return "";
                                }
                                if (validity == CastValidity.LAST_CAST_TOO_RECENT) {
                                    return String.valueOf(localPlayer.getSpellLevelCastHistory().getTurnsBeforeBeingAbleToCastSpell(spell2, fight2.getTimeline().getCurrentTableturn()));
                                }
                                if (validity == CastValidity.TOO_MUCH_CASTS_THIS_TURN) {
                                    return "1";
                                }
                                return "";
                            }
                            break;
                        }
                    }
                    return "";
                }
                if (fieldName.equals("backgroundTypeIcon")) {
                    try {
                        return String.format(WakfuConfiguration.getInstance().getString("shortcutBackgroundPath"), ShortCutItem.m_stateBackground[this.m_state]);
                    }
                    catch (Exception e) {
                        ShortCutItem.m_logger.error((Object)e);
                        return null;
                    }
                }
                if (fieldName.equals("quantity")) {
                    final ShortCutType type = this.getType();
                    if (type != ShortCutType.ITEM && type != ShortCutType.USABLE_REFERENCE_ITEM) {
                        return null;
                    }
                    final short qty = this.getQuantity();
                    if (qty > 999) {
                        return "999+";
                    }
                    if (qty == 0) {
                        return null;
                    }
                    return qty;
                }
                else if (fieldName.equals("particleFile")) {
                    return this.getParticleFilename();
                }
            }
            return null;
        }
        switch (this.getType()) {
            case EMOTE: {
                final EmoteSmileyFieldProvider emote = ReferenceEmoteManager.INSTANCE.getEmoteOrSmiley(this.getReferenceId());
                if (emote == null) {
                    return null;
                }
                return emote.getIconUrl();
            }
            case SPELL_LEVEL: {
                return WakfuConfiguration.getInstance().getSpellSmallIcon(this.getTargetGfxId());
            }
            case EQUIPMENT_SLOT:
            case ITEM:
            case USABLE_REFERENCE_ITEM: {
                return WakfuConfiguration.getInstance().getItemSmallIconUrl(this.getTargetGfxId());
            }
            default: {
                return null;
            }
        }
    }
    
    private String getParticleFilename() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (this.getType()) {
            case SPELL_LEVEL: {
                final Spell spell = SpellManager.getInstance().getSpell(this.getReferenceId());
                if (spell == null) {
                    break;
                }
                if (localPlayer.getBreedId() != AvatarBreed.ROUBLARD.getBreedId()) {
                    break;
                }
                final Elements spellElement = Elements.getElementFromId(spell.getElementId());
                if (spellElement == Elements.EARTH && localPlayer.getRunningEffectManager().containsWithSpecificId(63708)) {
                    return getParticleFilename(800261);
                }
                break;
            }
        }
        return null;
    }
    
    private static String getParticleFilename(final int id) {
        return id + ".xps";
    }
    
    @Override
    public void setItem(final ShortCutType type, final long uid, final int rid, final int gfxId) {
        super.setItem(type, uid, rid, gfxId);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "rid", "uid", "type", "backgroundTypeIcon", "smallIconUrl");
    }
    
    @Override
    protected void setTargetIds(final int referenceId, final long uniqueId, final int gfxId) {
        super.setTargetIds(referenceId, uniqueId, gfxId);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "rid", "uid", "smallIconUrl");
    }
    
    @Override
    public void setTargetGfxId(final int targetGfxId) {
        super.setTargetGfxId(targetGfxId);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "smallIconUrl");
    }
    
    @Override
    public void setQuantity(final short quantity) {
        super.setQuantity(quantity);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "quantity");
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "usable");
    }
    
    @Override
    public void updateQuantity(final short quantityUpdate) {
        super.updateQuantity(quantityUpdate);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "quantity", "usable");
    }
    
    @Override
    public String[] getFields() {
        return ShortCutItem.FIELDS;
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    private InventoryContent getCopy(final long id) {
        final ShortCutItem item = checkOut(this.getType(), id, this.getReferenceId(), this.getTargetGfxId());
        item.setState(this.m_state);
        item.setUsable(this.m_usable);
        item.setQuantity(this.getQuantity());
        return item;
    }
    
    @Override
    public InventoryContent getCopy(final boolean pooled) {
        return this.getCopy(GUIDGenerator.getGUID());
    }
    
    @Override
    public InventoryContent getClone() {
        return this.getCopy(this.getUniqueId());
    }
    
    @Override
    public short getQuantity() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (this.getType() == ShortCutType.USABLE_REFERENCE_ITEM || this.getType() == ShortCutType.ITEM) {
            int qty = 0;
            ArrayList<Item> items = localPlayer.getBags().getItemListFromInventories(this.m_targetReferenceId);
            if (items != null) {
                for (int i = items.size() - 1; i >= 0; --i) {
                    qty += items.get(i).getQuantity();
                }
            }
            if (this.getType() == ShortCutType.ITEM) {
                items = ((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).getAllWithReferenceId(this.m_targetReferenceId);
                if (items != null) {
                    for (int i = items.size() - 1; i >= 0; --i) {
                        qty += items.get(i).getQuantity();
                    }
                }
            }
            return MathHelper.ensureShort(qty);
        }
        return 1;
    }
    
    public byte getState() {
        return this.m_state;
    }
    
    public void setState(final byte state) {
        this.m_state = state;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "backgroundTypeIcon");
    }
    
    public boolean isUsableInFight() {
        if (this.getType() == ShortCutType.SPELL_LEVEL) {
            return true;
        }
        if (this.getType() == ShortCutType.EQUIPMENT_SLOT) {
            return true;
        }
        if (this.getType() != ShortCutType.ITEM) {
            return false;
        }
        switch (this.getState()) {
            case 0:
            case 1:
            case 3: {
                return false;
            }
            case 2:
            case 4:
            case 5: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public boolean isUsable() {
        return this.m_usable;
    }
    
    public void setUsable(final boolean usable) {
        this.m_usable = usable;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_usable = true;
        this.m_state = 0;
        this.m_spellCastValidator.clear();
    }
    
    static {
        m_stateBackground = new short[] { 0, 0, 1, 1, 0, 0 };
        FIELDS = new String[] { "uid", "rid", "smallIconUrl", "type", "usable", "backgroundTypeIcon", "quantity" };
        m_staticPool = new MonitoredPool(new ObjectFactory<ShortCutItem>() {
            @Override
            public ShortCutItem makeObject() {
                return new ShortCutItem(null);
            }
        });
    }
}
