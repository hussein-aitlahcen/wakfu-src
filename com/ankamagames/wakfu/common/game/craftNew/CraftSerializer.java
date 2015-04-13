package com.ankamagames.wakfu.common.game.craftNew;

import java.nio.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.craftNew.constant.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import gnu.trove.*;

public final class CraftSerializer
{
    public static byte[] serializeCraftInventory(final TIntIntHashMap inventory) {
        final ByteBuffer bb = ByteBuffer.allocate(inventory.size() * 8);
        final TIntIntIterator iterator = inventory.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            bb.putInt(iterator.key());
            bb.putInt(iterator.value());
        }
        return bb.array();
    }
    
    public static byte[] serializeCraftContract(final CraftContract craftContract) {
        final ByteArray bb = new ByteArray();
        bb.putLong(craftContract.getUniqueId());
        bb.putInt(craftContract.getRecipeId());
        bb.putLong(craftContract.getRequesterId());
        bb.putLong(craftContract.getRequestedId());
        bb.putLong(craftContract.getKamas());
        bb.put(craftContract.getContractState().getId());
        bb.put(serializeCraftTask(craftContract.getCraftTask()));
        return bb.toArray();
    }
    
    public static CraftContract unSerializeCraftContract(final ByteBuffer bb) {
        final CraftContract craftContract = new CraftContract(bb.getLong(), bb.getInt(), bb.getLong(), bb.getLong());
        craftContract.setKamas(bb.getLong());
        craftContract.setContractState(ContractState.getFromId(bb.get()));
        craftContract.setCraftTask(unSerializeCraftTask(bb));
        return craftContract;
    }
    
    private static byte[] serializeCraftTask(final CraftTask craftTask) {
        final ByteBuffer bb = ByteBuffer.allocate(25);
        bb.putLong(craftTask.getUniqueId());
        bb.putLong(craftTask.getContractId());
        bb.put(craftTask.getCraftTaskState().getId());
        bb.putLong(craftTask.getStartDate().toLong());
        return bb.array();
    }
    
    public static CraftTask unSerializeCraftTask(final ByteBuffer bb) {
        final CraftTask craftTask = new CraftTask(bb.getLong(), bb.getLong());
        craftTask.setCraftTaskState(CraftTaskState.getFromId(bb.get()));
        craftTask.setStartDate(GameDate.fromLong(bb.getLong()));
        return craftTask;
    }
    
    public static byte[] serializeCraft(final Craft craft) {
        final ByteBuffer bb = ByteBuffer.allocate(13);
        bb.putInt(craft.getReferenceId());
        bb.putLong(craft.getXp());
        bb.put((byte)(craft.isContractEnabled() ? 1 : 0));
        return bb.array();
    }
    
    public static Craft unSerializeCraft(final ByteBuffer bb) {
        final Craft craft = new Craft(bb.getInt());
        craft.setXp(bb.getLong());
        craft.setContractEnabled(bb.get() == 1);
        return craft;
    }
    
    public static byte[] serializeCraftRateEntry(final CraftUserRateEntry userRateEntry) {
        final ByteBuffer bb = ByteBuffer.allocate(22);
        bb.putLong(userRateEntry.getRaterUserId());
        bb.putLong(userRateEntry.getRatedUserId());
        bb.putShort(userRateEntry.getSympathyRate());
        bb.putShort(userRateEntry.getSpeedRate());
        bb.putShort(userRateEntry.getPriceRate());
        return bb.array();
    }
    
    public static CraftUserRateEntry unSerializeCraftRateEntry(final ByteBuffer bb) {
        final CraftUserRateEntry craftUserRateEntry = new CraftUserRateEntry(bb.getLong(), bb.getLong(), bb.getShort(), bb.getShort(), bb.getShort());
        return craftUserRateEntry;
    }
}
