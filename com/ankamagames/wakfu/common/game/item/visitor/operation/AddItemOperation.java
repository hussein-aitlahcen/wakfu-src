package com.ankamagames.wakfu.common.game.item.visitor.operation;

import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.item.elements.*;
import com.ankamagames.wakfu.common.game.item.mergeSet.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.game.item.rent.*;
import com.ankamagames.wakfu.common.game.item.bind.*;

public class AddItemOperation extends BagOperation implements QuantityOperation
{
    private int m_refId;
    private short m_qty;
    private long m_bagId;
    private short m_posInBag;
    private boolean m_insideMove;
    @Nullable
    private RawPet m_pet;
    @Nullable
    private RawItemXp m_xp;
    @Nullable
    private RawGems m_gems;
    @Nullable
    private RawCompanionInfo m_companion;
    @Nullable
    private RentInfo m_rentInfo;
    @Nullable
    private ItemBind m_bind;
    @Nullable
    private MultiElementsInfo m_multiElementsEffects;
    @Nullable
    private MergedSetInfo m_mergedSetInfo;
    
    public AddItemOperation(final Item item, final long bagUId, final short positionInBag) {
        super();
        this.m_refId = item.getReferenceId();
        this.m_qty = item.getQuantity();
        this.m_bagId = bagUId;
        this.m_posInBag = positionInBag;
        if (item.hasPet()) {
            this.m_pet = new RawPet();
            item.getPet().toRaw(this.m_pet);
        }
        if (item.hasXp()) {
            this.m_xp = new RawItemXp();
            item.getXp().toRaw(this.m_xp);
        }
        if (item.hasGems()) {
            this.m_gems = new RawGems();
            item.getGems().toRaw(this.m_gems);
        }
        if (item.hasCompanionInfo()) {
            this.m_companion = new RawCompanionInfo();
            item.getCompanionInfo().toRaw(this.m_companion);
        }
        this.m_rentInfo = item.getRentInfo();
        this.m_bind = item.getBind();
        this.m_multiElementsEffects = item.getMultiElementsInfo();
        this.m_mergedSetInfo = item.getMergedSetItems();
    }
    
    public AddItemOperation() {
        super();
    }
    
    public int getRefId() {
        return this.m_refId;
    }
    
    public short getQty() {
        return this.m_qty;
    }
    
    public long getBagId() {
        return this.m_bagId;
    }
    
    public short getPosInBag() {
        return this.m_posInBag;
    }
    
    @Override
    public byte getOperationType() {
        return 0;
    }
    
    @Override
    public void updateQuantity(final short quantity) {
        this.m_qty = quantity;
    }
    
    public boolean isInsideMove() {
        return this.m_insideMove;
    }
    
    public void setInsideMove(final boolean inside) {
        this.m_insideMove = inside;
    }
    
    @Nullable
    public RawPet getPet() {
        return this.m_pet;
    }
    
    @Nullable
    public RawItemXp getXp() {
        return this.m_xp;
    }
    
    @Nullable
    public RawGems getGems() {
        return this.m_gems;
    }
    
    @Nullable
    public RawCompanionInfo getCompanion() {
        return this.m_companion;
    }
    
    public RentInfo getRentInfo() {
        return this.m_rentInfo;
    }
    
    public ItemBind getBind() {
        return this.m_bind;
    }
    
    public MultiElementsInfo getMultiElementsEffects() {
        return this.m_multiElementsEffects;
    }
    
    @Nullable
    public MergedSetInfo getMergedSetInfo() {
        return this.m_mergedSetInfo;
    }
    
    @Override
    public void serialize(final ByteArray buff) {
        buff.putInt(this.m_refId);
        buff.putShort(this.m_qty);
        buff.putLong(this.m_bagId);
        buff.putShort(this.m_posInBag);
        buff.put((byte)((this.m_pet != null) ? 1 : 0));
        if (this.m_pet != null) {
            final ByteBuffer bb = ByteBuffer.allocate(this.m_pet.serializedSize());
            this.m_pet.serialize(bb);
            buff.put(bb.array());
        }
        buff.put((byte)((this.m_xp != null) ? 1 : 0));
        if (this.m_xp != null) {
            final ByteBuffer bb = ByteBuffer.allocate(this.m_xp.serializedSize());
            this.m_xp.serialize(bb);
            buff.put(bb.array());
        }
        buff.put((byte)((this.m_gems != null) ? 1 : 0));
        if (this.m_gems != null) {
            final ByteBuffer bb = ByteBuffer.allocate(this.m_gems.serializedSize());
            this.m_gems.serialize(bb);
            buff.put(bb.array());
        }
        buff.put((byte)((this.m_companion != null) ? 1 : 0));
        if (this.m_companion != null) {
            final ByteBuffer bb = ByteBuffer.allocate(this.m_companion.serializedSize());
            this.m_companion.serialize(bb);
            buff.put(bb.array());
        }
        buff.put((byte)((this.m_rentInfo != null) ? 1 : 0));
        if (this.m_rentInfo != null) {
            final RawRentInfo rawRentInfo = new RawRentInfo();
            this.m_rentInfo.toRaw(rawRentInfo);
            final ByteBuffer bb2 = ByteBuffer.allocate(rawRentInfo.serializedSize());
            rawRentInfo.serialize(bb2);
            buff.put(bb2.array());
        }
        buff.put((byte)((this.m_bind != null) ? 1 : 0));
        if (this.m_bind != null) {
            final RawItemBind raw = new RawItemBind();
            this.m_bind.toRaw(raw);
            final ByteBuffer bb2 = ByteBuffer.allocate(raw.serializedSize());
            raw.serialize(bb2);
            buff.put(bb2.array());
        }
        buff.put((byte)((this.m_multiElementsEffects != null) ? 1 : 0));
        if (this.m_multiElementsEffects != null) {
            final RawItemElements raw2 = new RawItemElements();
            this.m_multiElementsEffects.toRaw(raw2);
            final ByteBuffer bb2 = ByteBuffer.allocate(raw2.serializedSize());
            raw2.serialize(bb2);
            buff.put(bb2.array());
        }
        buff.put((byte)((this.m_mergedSetInfo != null) ? 1 : 0));
        if (this.m_mergedSetInfo != null) {
            final RawMergedItems raw3 = new RawMergedItems();
            this.m_mergedSetInfo.toRaw(raw3);
            final ByteBuffer bb2 = ByteBuffer.allocate(raw3.serializedSize());
            raw3.serialize(bb2);
            buff.put(bb2.array());
        }
    }
    
    @Override
    public void unSerialize(final ByteBuffer buffer) {
        this.m_refId = buffer.getInt();
        this.m_qty = buffer.getShort();
        this.m_bagId = buffer.getLong();
        this.m_posInBag = buffer.getShort();
        if (buffer.get() == 1) {
            (this.m_pet = new RawPet()).unserialize(buffer);
        }
        if (buffer.get() == 1) {
            (this.m_xp = new RawItemXp()).unserialize(buffer);
        }
        if (buffer.get() == 1) {
            (this.m_gems = new RawGems()).unserialize(buffer);
        }
        if (buffer.get() == 1) {
            (this.m_companion = new RawCompanionInfo()).unserialize(buffer);
        }
        if (buffer.get() == 1) {
            final RawRentInfo rawRentInfo = new RawRentInfo();
            rawRentInfo.unserialize(buffer);
            this.m_rentInfo = RentInfoSerializer.unserialize(rawRentInfo);
        }
        if (buffer.get() == 1) {
            final RawItemBind raw = new RawItemBind();
            raw.unserialize(buffer);
            this.m_bind = ItemBindSerializer.unserialize(raw);
        }
        if (buffer.get() == 1) {
            final RawItemElements raw2 = new RawItemElements();
            raw2.unserialize(buffer);
            this.m_multiElementsEffects = MultiElementsInfo.unserialize(raw2);
        }
        if (buffer.get() == 1) {
            final RawMergedItems raw3 = new RawMergedItems();
            raw3.unserialize(buffer);
            this.m_mergedSetInfo = MergedSetInfo.fromRaw(raw3);
        }
    }
    
    @Override
    public String toString() {
        return "AddItemOperation{m_refId=" + this.m_refId + ", m_qty=" + this.m_qty + ", m_rentInfo=" + this.m_rentInfo + ", m_bind=" + this.m_bind + ", m_bagId=" + this.m_bagId + ", m_posInBag=" + this.m_posInBag + ", m_insideMove=" + this.m_insideMove + ", m_multiElementsEffects=" + this.m_multiElementsEffects + ", m_mergedSetInfo=" + this.m_mergedSetInfo + '}';
    }
}
