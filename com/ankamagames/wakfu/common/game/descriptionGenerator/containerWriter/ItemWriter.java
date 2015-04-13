package com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ItemWriter extends DefaultContainerWriter<EffectContainer<WakfuEffect>>
{
    private AbstractReferenceItem m_item;
    private Item m_realItem;
    private ItemEquipment m_equipment;
    private CharacteristicManager<FighterCharacteristic> m_characteristicManager;
    private WakfuEffect m_finalEffect;
    private boolean m_characteristics;
    
    public ItemWriter(final AbstractReferenceItem item, final Item realItem, final CharacteristicManager<FighterCharacteristic> manager, final ItemEquipment equipment, final ItemEffectType itemEffectType) {
        this(item, realItem, (realItem != null) ? realItem.getLevel() : item.getLevel(), manager, equipment, itemEffectType);
    }
    
    public ItemWriter(final AbstractReferenceItem item, final Item realItem, final short level, final CharacteristicManager<FighterCharacteristic> manager, final ItemEquipment equipment, final ItemEffectType itemEffectType) {
        super(itemEffectType.createEffectContainer(item, realItem), 0, level, true, null, null, itemEffectType.getMode(), 0);
        this.m_item = item;
        this.m_realItem = realItem;
        this.m_characteristicManager = manager;
        this.m_equipment = equipment;
        this.m_characteristics = itemEffectType.isACharacteristic();
    }
    
    @Override
    public void onContainerBegin(@NotNull final ArrayList<String> descriptions) {
        if (this.m_item.getMetaType() == ItemMetaType.META_ITEM) {
            final Iterator<AbstractEffectGroup> it = ((IMetaItem)this.m_item).variableEffectsIterator();
            while (it.hasNext()) {
                final AbstractEffectGroup group = it.next();
                final WakfuEffect effect = group.getEffect(0);
                if (!this.m_characteristics || !effect.isAnUsableEffect()) {
                    if (!this.m_characteristics && !effect.isAnUsableEffect()) {
                        continue;
                    }
                    final EffectWriter effectWriter = CastableDescriptionGenerator.getEffectWriter(effect.getActionId());
                    final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
                    this.m_finalEffect = group.getEffect(group.getEffectsCount() - 1);
                    effectWriter.writeEffect(sb, effect, this);
                    descriptions.add(sb.finishAndToString());
                }
            }
        }
        this.m_finalEffect = null;
    }
    
    @Override
    public String onEffectAdded(@NotNull final String effectDescription, @NotNull final WakfuEffect iEffect) {
        if ((this.m_characteristics && iEffect.isAnUsableEffect()) || (!this.m_characteristics && !iEffect.isAnUsableEffect())) {
            return "";
        }
        final short itemBaseLevel = (this.m_realItem != null) ? this.m_realItem.getLevel() : this.m_item.getLevel();
        final WakfuRunningEffect wre = RunningEffectConstants.getInstance().getObjectFromId(iEffect.getActionId());
        CharacteristicType type = null;
        if (wre instanceof CharacGain) {
            type = ((CharacGain)wre).getCharacteristicType();
        }
        else if (wre instanceof CharacLoss) {
            type = ((CharacLoss)wre).getCharacteristicType();
        }
        else if (wre instanceof CharacBuff) {
            type = ((CharacBuff)wre).getCharacteristicType();
        }
        else if (wre instanceof CharacDebuff) {
            type = ((CharacDebuff)wre).getCharacteristicType();
        }
        boolean equiped = false;
        Item equipedItem = null;
        final EquipmentPosition[] positions = (this.m_realItem == null) ? this.m_item.getItemType().getEquipmentPositions() : this.m_realItem.getReferenceItem().getItemType().getEquipmentPositions();
        int selectedPosition = 0;
        final int numPosition = positions.length;
        if (this.m_realItem != null && this.m_equipment != null) {
            while (selectedPosition < numPosition && !equiped) {
                final EquipmentPosition position = positions[selectedPosition];
                final Item equipment = ((ArrayInventoryWithoutCheck<Item, R>)this.m_equipment).getFromPosition(position.getId());
                if (equipment != null && equipment.getUniqueId() == this.m_realItem.getUniqueId()) {
                    equiped = true;
                    equipedItem = equipment;
                }
                ++selectedPosition;
            }
        }
        if (type != null && this.m_equipment != null && this.m_characteristicManager != null) {
            int difference = 0;
            int finalDifference = 0;
            if (!equiped && positions.length > 0) {
                difference = iEffect.getParam(0, itemBaseLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                if (this.m_finalEffect != null) {
                    finalDifference = this.m_finalEffect.getParam(0, itemBaseLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                }
                for (selectedPosition = 0; selectedPosition < numPosition; ++selectedPosition) {
                    final EquipmentPosition position2 = positions[selectedPosition];
                    final Item equipment2 = ((ArrayInventoryWithoutCheck<Item, R>)this.m_equipment).getFromPosition(position2.getId());
                    if (equipment2 == null) {
                        break;
                    }
                }
                if (selectedPosition == positions.length) {
                    for (selectedPosition = 0; selectedPosition < numPosition; ++selectedPosition) {
                        final EquipmentPosition position2 = positions[selectedPosition];
                        final Item equipment2 = ((ArrayInventoryWithoutCheck<Item, R>)this.m_equipment).getFromPosition(position2.getId());
                        if (equipment2 != null && equipment2.getReferenceId() == this.m_item.getId()) {
                            break;
                        }
                    }
                }
                if (selectedPosition == positions.length) {
                    selectedPosition = 0;
                }
                Item item;
                if (equiped) {
                    item = equipedItem;
                }
                else {
                    final EquipmentPosition position3 = positions[selectedPosition];
                    item = ((ArrayInventoryWithoutCheck<Item, R>)this.m_equipment).getFromPosition(position3.getId());
                }
                if (item != null) {
                    final short equipedItemLevel = item.getLevel();
                    for (final WakfuEffect effect : item) {
                        if (effect.getActionId() == iEffect.getActionId() && effect.getContainerMinLevel() <= equipedItemLevel && effect.getContainerMaxLevel() >= equipedItemLevel) {
                            difference -= effect.getParam(0, equipedItemLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                            if (this.m_finalEffect == null) {
                                continue;
                            }
                            finalDifference -= effect.getParam(0, equipedItemLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                        }
                    }
                }
            }
            if (type.isNegative() != (wre.getRunningEffectStatus() == RunningEffectStatus.NEGATIVE)) {
                difference = -difference;
                if (this.m_finalEffect != null) {
                    finalDifference = -finalDifference;
                }
            }
            final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
            if (difference != 0 || finalDifference != 0) {
                sb.append(" (");
                sb.b();
                sb.addColor((difference > 0) ? "00b400" : ((difference == 0) ? "ffffff" : "b40000"));
                if (difference > 0) {
                    sb.append("+");
                }
                sb.append(difference)._b();
                if (this.m_finalEffect != null) {
                    sb.append("/").b();
                    sb.addColor((finalDifference > 0) ? "00b400" : ((finalDifference == 0) ? "ffffff" : "b40000"));
                    if (finalDifference > 0) {
                        sb.append("+");
                    }
                    sb.append(finalDifference)._b();
                }
                sb.append(")");
                if (this.m_finalEffect != null) {
                    CastableDescriptionGenerator.m_utilityDelegate.getCraftIcon(sb);
                }
            }
            else {
                sb.append(" (").append(type.isExpandable() ? this.m_characteristicManager.getCharacteristicMaxValue(type) : this.m_characteristicManager.getCharacteristicValue(type)).append(")");
            }
            return effectDescription + sb.finishAndToString();
        }
        return effectDescription;
    }
    
    public Item getRealItem() {
        return this.m_realItem;
    }
}
