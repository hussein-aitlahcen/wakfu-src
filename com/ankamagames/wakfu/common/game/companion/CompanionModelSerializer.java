package com.ankamagames.wakfu.common.game.companion;

import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.companion.equipment.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;

public final class CompanionModelSerializer
{
    public static int size(final CompanionModel companion) {
        final byte[] bytes = StringUtils.toUTF8(companion.getName());
        final CharacterSerializedEquipmentInventory equipmentInventory = new CharacterSerializedEquipmentInventory();
        companion.getItemEquipment().toRaw(equipmentInventory.equipment);
        return 30 + bytes.length + equipmentInventory.serializedSize() + 1 + 4 + 4 + 4;
    }
    
    public static byte[] serialize(final CompanionModel companion) {
        final ByteArray ba = serializeWithoutEquipment(companion);
        final ByteBuffer bb = getSerializedEquipment(companion);
        ba.put(bb.array());
        return ba.toArray();
    }
    
    public static ByteArray serializeWithoutEquipment(final CompanionModel companion) {
        final ByteArray ba = new ByteArray();
        ba.putLong(companion.getId());
        ba.putShort(companion.getBreedId());
        ba.putLong(companion.getOwnerId());
        ba.putLong(companion.getXp());
        final byte[] bytes = StringUtils.toUTF8(companion.getName());
        ba.putInt(bytes.length);
        ba.put(bytes);
        ba.put((byte)(companion.isUnlocked() ? 1 : 0));
        ba.putInt(companion.getCurrentHp());
        ba.putInt(companion.getHpMax());
        ba.putInt(companion.getSerializationVersion());
        return ba;
    }
    
    public static ByteBuffer getSerializedEquipment(final CompanionModel companion) {
        final ItemEquipment itemEquipment = companion.getItemEquipment();
        return getSerializedEquipment(itemEquipment);
    }
    
    public static ByteBuffer getSerializedEquipment(final ItemEquipment itemEquipment) {
        final CharacterSerializedEquipmentInventory equipmentInventory = new CharacterSerializedEquipmentInventory();
        itemEquipment.toRaw(equipmentInventory.equipment);
        final ByteBuffer bb = ByteBuffer.allocate(equipmentInventory.serializedSize());
        equipmentInventory.serialize(bb);
        return bb;
    }
    
    public static CompanionModel unserializeToCurrentVersion(final ByteBuffer bb) {
        final CompanionModel res = unserializeWithoutEquipment(bb);
        return unserializeEquipmentToVersion(bb, res, Version.SERIALIZATION_VERSION);
    }
    
    public static CompanionModel unserialize(final ByteBuffer bb) {
        final CompanionModel res = unserializeWithoutEquipment(bb);
        final int serializationVersion = res.getSerializationVersion();
        return unserializeEquipmentToVersion(bb, res, serializationVersion);
    }
    
    public static CompanionModel unserializeEquipmentToVersion(final ByteBuffer bb, final CompanionModel res, final int serializationVersion) {
        final CharacterSerializedEquipmentInventory equipmentInventory = new CharacterSerializedEquipmentInventory();
        equipmentInventory.unserializeVersion(bb, serializationVersion);
        res.getItemEquipment().addObserver(new MultipleSlotEquipmentHandler());
        res.getItemEquipment().fromRaw(equipmentInventory.equipment);
        return res;
    }
    
    public static CompanionModel unserializeWithoutEquipment(final ByteBuffer bb) {
        final long id = bb.getLong();
        final short breedId = bb.getShort();
        final long ownerId = bb.getLong();
        final long xp = bb.getLong();
        final byte[] bytes = new byte[bb.getInt()];
        bb.get(bytes);
        final String name = StringUtils.fromUTF8(bytes);
        final boolean isUnlocked = bb.get() == 1;
        final int currentHp = bb.getInt();
        final int maxHp = bb.getInt();
        final int serializationVersion = bb.getInt();
        final CompanionModel res = new CompanionModel(breedId);
        res.setId(id);
        res.setOwnerId(ownerId);
        res.setXp(xp);
        res.setName(name);
        res.setUnlocked(isUnlocked);
        res.setCurrentHp(currentHp);
        res.setHpMax(maxHp);
        res.setSerializationVersion(serializationVersion);
        return res;
    }
}
