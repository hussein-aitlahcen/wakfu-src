package com.ankamagames.wakfu.client.core.game.stuffPreview;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import gnu.trove.*;

public class StuffPreview extends ImmutableFieldProvider implements InventoryObserver
{
    protected static final Logger m_logger;
    public static final String ANIM_NAME = "animName";
    public static final String ACTOR_DESCRIPTOR_LIBRARY = "actorDescriptorLibrary";
    public static final String EQUIPMENT_BONUSES = "equipmentBonuses";
    public static final String ACTOR_ANIMATION_NAME_FIELD = "actorAnimationName";
    public static final String HEAD_EQUIPMENT_FIELD = "headEquipment";
    public static final String HAIR_EQUIPMENT_FIELD = "hairEquipment";
    public static final String FACE_EQUIPMENT_FIELD = "faceEquipment";
    public static final String SHOULDERS_EQUIPMENT_FIELD = "shoulderEquipment";
    public static final String NECK_EQUIPMENT_FIELD = "neckEquipment";
    public static final String CHEST_EQUIPMENT_FIELD = "chestEquipment";
    public static final String ARMS_EQUIPMENT_FIELD = "armsEquipment";
    public static final String LEFT_HAND_EQUIPMENT_FIELD = "leftHandEquipment";
    public static final String RIGHT_HAND_EQUIPMENT_FIELD = "rightHandEquipment";
    public static final String BELT_EQUIPMENT_FIELD = "beltEquipment";
    public static final String SKIRT_EQUIPMENT_FIELD = "skirtEquipment";
    public static final String TROUSERS_EQUIPMENT_FIELD = "trousersEquipment";
    public static final String LEGS_EQUIPMENT_FIELD = "legsEquipment";
    public static final String PET_EQUIPMENT_FIELD = "petEquipment";
    public static final String MOUNT_EQUIPMENT_FIELD = "mountEquipment";
    public static final String COSTUME_EQUIPMENT_FIELD = "costumeEquipment";
    public static final String BACK_EQUIPMENT_FIELD = "backEquipment";
    public static final String WING_EQUIPMENT_FIELD = "wingEquipment";
    public static final String FIRST_WEAPON_EQUIPMENT_FIELD = "firstWeaponEquipment";
    public static final String SECOND_WEAPON_EQUIPMENT_FIELD = "secondWeaponEquipment";
    public static final String ACCESSORY_EQUIPMENT_FIELD = "accessoryEquipment";
    private final TByteObjectHashMap<Item> m_equipmentInventory;
    private final CharacterActor m_animatedElement;
    private final TShortObjectHashMap<Anm> m_equipedPositions;
    private static final String[] FIELDS;
    
    public StuffPreview() {
        super();
        this.m_equipmentInventory = new TByteObjectHashMap<Item>();
        this.m_equipedPositions = new TShortObjectHashMap<Anm>(EquipmentType.values().length);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        (this.m_animatedElement = new CharacterActor(new PlayerCharacter())).copyData(localPlayer.getActor());
        final ItemEquipment equipmentInventory = localPlayer.getEquipmentInventory();
        for (final Item item : equipmentInventory) {
            this.addEquipment(item, (byte)equipmentInventory.getPosition(item.getUniqueId()));
        }
        equipmentInventory.addObserver(this);
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("animName")) {
            return null;
        }
        if (fieldName.equals("actorDescriptorLibrary")) {
            return this.m_animatedElement;
        }
        if (fieldName.equals("actorAnimationName")) {
            final Item weapon1 = this.m_equipmentInventory.get(EquipmentPosition.FIRST_WEAPON.m_id);
            if (weapon1 != null) {
                return "AnimStatique03-Boucle-" + WeaponAttack.getWeaponTypeId(weapon1.getType());
            }
            return "AnimStatique";
        }
        else {
            if (fieldName.equals("headEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.HEAD.m_id);
            }
            if (fieldName.equals("hairEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.HAIR.m_id);
            }
            if (fieldName.equals("faceEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.FACE.m_id);
            }
            if (fieldName.equals("shoulderEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.SHOULDERS.m_id);
            }
            if (fieldName.equals("neckEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.NECK.m_id);
            }
            if (fieldName.equals("chestEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.CHEST.m_id);
            }
            if (fieldName.equals("armsEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.ARMS.m_id);
            }
            if (fieldName.equals("leftHandEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.LEFT_HAND.m_id);
            }
            if (fieldName.equals("rightHandEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.RIGHT_HAND.m_id);
            }
            if (fieldName.equals("beltEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.BELT.m_id);
            }
            if (fieldName.equals("skirtEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.SKIRT.m_id);
            }
            if (fieldName.equals("trousersEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.TROUSERS.m_id);
            }
            if (fieldName.equals("legsEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.LEGS.m_id);
            }
            if (fieldName.equals("backEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.BACK.m_id);
            }
            if (fieldName.equals("wingEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.WING.m_id);
            }
            if (fieldName.equals("firstWeaponEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.FIRST_WEAPON.m_id);
            }
            if (fieldName.equals("secondWeaponEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.SECOND_WEAPON.m_id);
            }
            if (fieldName.equals("accessoryEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.ACCESSORY.m_id);
            }
            if (fieldName.equals("petEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.PET.m_id);
            }
            if (fieldName.equals("mountEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.MOUNT.m_id);
            }
            if (fieldName.equals("costumeEquipment")) {
                return this.m_equipmentInventory.get(EquipmentPosition.COSTUME.m_id);
            }
            if (fieldName.equals("equipmentBonuses")) {
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final ArrayList<Item> items = new ArrayList<Item>();
                final TByteObjectIterator<Item> it = this.m_equipmentInventory.iterator();
                while (it.hasNext()) {
                    it.advance();
                    items.add(it.value());
                }
                return PlayerCharacter.getEquipmentBonus(localPlayer, items);
            }
            return null;
        }
    }
    
    public void addEquipment(final Item item, final byte position) {
        final AbstractItemType itemType = item.getReferenceItem().getItemType();
        if (!itemType.isEquipmentPositionValid(EquipmentPosition.fromId(position))) {
            return;
        }
        if (this.m_equipmentInventory.get(position) != null) {
            this.removeEquipment(position);
        }
        this.m_equipmentInventory.put(position, item);
        this.applyEquipmentAnimation(item, position);
        for (final EquipmentPosition ep : itemType.getLinkedPositions()) {
            this.m_equipmentInventory.put(ep.getId(), item.getInactiveCopy());
        }
        this.refresh();
        WakfuSoundManager.getInstance().equipItem(EquipmentPosition.fromId(position));
    }
    
    private void applyEquipmentAnimation(final Item item, final byte position) {
        final AbstractItemType itemType = item.getReferenceItem().getItemType();
        this.unapplyEquipmentAnimation(position);
        if (!itemType.isVisibleInAnimations()) {
            return;
        }
        final int gfxId = (WakfuGameEntity.getInstance().getLocalPlayer().getSex() == 0) ? item.getGfxId() : item.getFemaleGfxId();
        final EquipmentType equipmentType = EquipmentType.getActorEquipmentTypeFromPosition(position);
        if (equipmentType == null) {
            return;
        }
        try {
            final String equipmentFileName = WakfuConfiguration.getInstance().getString("ANMEquipmentPath");
            final CharacterActor animatedElement = this.m_animatedElement;
            final Anm equipment = AnimatedElement.loadEquipment(String.format(equipmentFileName, gfxId));
            this.m_animatedElement.applyParts(equipment, equipmentType.m_linkageCrc);
            this.m_equipedPositions.put(position, equipment);
        }
        catch (Exception e) {
            StuffPreview.m_logger.error((Object)("Erreur au chargement de l'\u00e9quipment : " + gfxId), (Throwable)e);
        }
    }
    
    private void unapplyEquipmentAnimation(final short position) {
        final Anm anm = this.m_equipedPositions.remove(position);
        if (anm != null) {
            final EquipmentType equipmentType = EquipmentType.getActorEquipmentTypeFromPosition(position);
            if (equipmentType != null) {
                this.m_animatedElement.removeParts(anm, equipmentType.m_linkageCrc);
            }
        }
    }
    
    public void removeEquipment(final byte position) {
        final EquipmentPosition ep = EquipmentPosition.fromId(position);
        final Item item = this.m_equipmentInventory.get(position);
        if (ep == EquipmentPosition.COSTUME || ep == EquipmentPosition.ACCESSORY) {
            this.updateAnmForBadge(item.getReferenceItem(), false);
        }
        this.m_equipmentInventory.remove(position);
        for (final EquipmentPosition ep2 : item.getReferenceItem().getItemType().getLinkedPositions()) {
            if (ep2 == ep) {
                for (final EquipmentPosition ep3 : item.getReferenceItem().getItemType().getEquipmentPositions()) {
                    final Item mainItem = this.m_equipmentInventory.get(ep3.getId());
                    if (mainItem != null && mainItem.getReferenceId() == item.getReferenceId()) {
                        this.m_equipmentInventory.remove(ep3.getId());
                    }
                }
            }
            else {
                this.m_equipmentInventory.remove(ep2.getId());
            }
        }
        this.refresh();
    }
    
    public void refresh() {
        this.m_animatedElement.unapplyAllEquipments();
        this.m_animatedElement.getAnmInstance().unapplyAllParts();
        this.m_animatedElement.setGfx(Integer.toString(WakfuGameEntity.getInstance().getLocalPlayer().getGfxId()));
        this.applyCustoms();
        final TByteObjectIterator<Item> it = this.m_equipmentInventory.iterator();
        while (it.hasNext()) {
            it.advance();
            this.applyEquipmentAnimation(it.value(), it.key());
        }
        this.applyHmiItem();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, StuffPreview.FIELDS);
    }
    
    private void applyCustoms() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final String dressStyle = localPlayer.getCurrentDressStyle();
        final String currentHairStyle = localPlayer.getCurrentHairStyle();
        if (dressStyle == null) {
            if (currentHairStyle == null) {
                return;
            }
        }
        String equipmentFileName;
        try {
            equipmentFileName = WakfuConfiguration.getInstance().getString("ANMEquipmentPath");
        }
        catch (PropertyException e) {
            StuffPreview.m_logger.error((Object)"Erreur au chargement d'une propri\u00e9t\u00e9", (Throwable)e);
            return;
        }
        if (dressStyle != null) {
            final String dressFileName = String.format(equipmentFileName, dressStyle);
            this.m_animatedElement.applyParts(dressFileName, AnmPartHelper.getParts("VETEMENTCUSTOM"));
        }
        if (currentHairStyle != null) {
            final String hairFileName = String.format(equipmentFileName, currentHairStyle);
            this.m_animatedElement.applyParts(hairFileName, AnmPartHelper.getParts("CHEVEUXCUSTOM"));
        }
    }
    
    public Item getFromPosition(final byte position) {
        return this.m_equipmentInventory.get(position);
    }
    
    private void applyHmiItem() {
        byte position = EquipmentPosition.COSTUME.getId();
        final Item costumeItem = this.getFromPosition(position);
        if (costumeItem != null) {
            this.updateAnmForBadge(costumeItem.getReferenceItem(), true);
            return;
        }
        position = EquipmentPosition.ACCESSORY.getId();
        final Item accessoryItem = this.getFromPosition(position);
        if (accessoryItem != null) {
            this.updateAnmForBadge(accessoryItem.getReferenceItem(), true);
        }
    }
    
    private boolean updateAnmForBadge(final AbstractReferenceItem setPack, final boolean apply) {
        final GrowingArray<WakfuEffect> effects = (GrowingArray<WakfuEffect>)setPack.getEffects();
        for (final WakfuEffect effect : effects) {
            if (effect.getActionId() != RunningEffectConstants.STATE_APPLY.getId()) {
                continue;
            }
            final int stateId = effect.getParam(0, (short)0, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            final State state = StateManager.getInstance().getState(stateId);
            if (state == null) {
                continue;
            }
            this.updateStateEffectsHmi(state, apply);
        }
        return false;
    }
    
    private void updateStateEffectsHmi(final State state, final boolean apply) {
        final ArrayList<WakfuEffect> stateEffects = state.getEffectsForLevelAsList((short)0);
        for (final WakfuEffect stateEffect : stateEffects) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final byte sex = localPlayer.getSex();
            final SimpleCriterion conditions = stateEffect.getConditions();
            if (conditions != null && !conditions.isValid(localPlayer, localPlayer, localPlayer, null)) {
                continue;
            }
            final SimpleCriterion criterion = this.getSexCriterion(conditions);
            if (criterion == null) {
                this.updateEffectHmi(stateEffect, apply);
            }
            else {
                if (!(criterion instanceof IsSex) || ((IsSex)criterion).getSex() != sex) {
                    continue;
                }
                this.updateEffectHmi(stateEffect, apply);
            }
        }
    }
    
    private SimpleCriterion getSexCriterion(final SimpleCriterion criterion) {
        if (criterion == null) {
            return null;
        }
        if (criterion instanceof IsSex) {
            return criterion;
        }
        if (criterion instanceof AndCriterion) {
            final AndCriterion andCriterion = (AndCriterion)criterion;
            SimpleCriterion sexCriterion = this.getSexCriterion(andCriterion.getLeft());
            if (sexCriterion != null) {
                return sexCriterion;
            }
            sexCriterion = this.getSexCriterion(andCriterion.getRight());
            if (sexCriterion != null) {
                return sexCriterion;
            }
        }
        if (criterion instanceof OrCriterion) {
            final OrCriterion orCriterion = (OrCriterion)criterion;
            SimpleCriterion sexCriterion = this.getSexCriterion(orCriterion.getLeft());
            if (sexCriterion != null) {
                return sexCriterion;
            }
            sexCriterion = this.getSexCriterion(orCriterion.getRight());
            if (sexCriterion != null) {
                return sexCriterion;
            }
        }
        return null;
    }
    
    private void updateEffectHmi(final WakfuEffect stateEffect, final boolean apply) {
        final List<HMIAction> actionsOrder = stateEffect.getActionsOrder();
        if (actionsOrder == null) {
            return;
        }
        for (final HMIAction hmiAction : actionsOrder) {
            if (apply) {
                this.applyHmi(stateEffect, hmiAction);
            }
            else {
                this.unapplyHmi(stateEffect, hmiAction);
            }
        }
    }
    
    private boolean applyHmi(final WakfuEffect stateEffect, final HMIAction hmiAction) {
        if (hmiAction.getType() == HMIActionType.SKIN_PART_OTHER_CHANGE) {
            final HMIChangeSkinPartOtherAction changePartSkin = (HMIChangeSkinPartOtherAction)hmiAction;
            final ChangePartsList.Data data = new ChangePartsList.Data(stateEffect, changePartSkin.getAppearanceId(), changePartSkin.getWeight(), changePartSkin.getPartsToChange());
            data.apply(this.m_animatedElement, false);
            return true;
        }
        if (hmiAction.getType() == HMIActionType.SKIN_PART_VISIBILITY) {
            final HMIVisibilitySkinPartAction changePartSkin2 = (HMIVisibilitySkinPartAction)hmiAction;
            final VisibilityPartsList.Data data2 = new VisibilityPartsList.Data(stateEffect, changePartSkin2.getVisibility(), changePartSkin2.getParts());
            data2.apply(this.m_animatedElement);
            return true;
        }
        if (hmiAction.getType() == HMIActionType.COSTUME) {
            final HMICostumeAction action = (HMICostumeAction)hmiAction;
            final CostumeListData data3 = new CostumeListData(stateEffect, action.getAppearances(), action.getWeight(), action.getParticleId());
            data3.apply(this.m_animatedElement, false);
            return true;
        }
        return false;
    }
    
    private boolean unapplyHmi(final WakfuEffect stateEffect, final HMIAction hmiAction) {
        if (hmiAction.getType() == HMIActionType.SKIN_PART_VISIBILITY) {
            final HMIVisibilitySkinPartAction changePartSkin = (HMIVisibilitySkinPartAction)hmiAction;
            final VisibilityPartsList.Data data = new VisibilityPartsList.Data(stateEffect, !changePartSkin.getVisibility(), changePartSkin.getParts());
            data.apply(this.m_animatedElement);
            return true;
        }
        return false;
    }
    
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "equipmentBonuses");
    }
    
    static {
        m_logger = Logger.getLogger((Class)StuffPreview.class);
        FIELDS = new String[] { "actorAnimationName", "headEquipment", "hairEquipment", "faceEquipment", "shoulderEquipment", "neckEquipment", "chestEquipment", "armsEquipment", "leftHandEquipment", "rightHandEquipment", "beltEquipment", "skirtEquipment", "trousersEquipment", "legsEquipment", "petEquipment", "mountEquipment", "backEquipment", "wingEquipment", "firstWeaponEquipment", "secondWeaponEquipment", "accessoryEquipment", "costumeEquipment", "actorDescriptorLibrary", "equipmentBonuses" };
    }
}
