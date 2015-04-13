package com.ankamagames.wakfu.common.game.companion;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.common.datas.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;

public final class CompanionModel
{
    private static final Logger m_logger;
    private final List<CompanionModelListener> m_listeners;
    private long m_id;
    private final short m_breedId;
    private long m_xp;
    private String m_name;
    private long m_ownerId;
    private final ItemEquipment m_itemEquipment;
    private byte[] m_serializedEquipment;
    private boolean m_isUnlocked;
    private int m_hpMax;
    private int m_currentHp;
    private int m_serializationVersion;
    
    public CompanionModel(final short breedId) {
        super();
        this.m_listeners = new ArrayList<CompanionModelListener>();
        this.m_itemEquipment = new ItemEquipment();
        this.m_breedId = breedId;
        this.m_itemEquipment.lockPosition(EquipmentPosition.COSTUME, true);
    }
    
    public short getBreedId() {
        return this.m_breedId;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public long getXp() {
        return this.m_xp;
    }
    
    public short getLevel() {
        return CharacterXpTable.getInstance().getLevelByXp(this.getXp());
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public void setXp(final long xp) {
        final long previousXp = this.m_xp;
        this.m_xp = xp;
        if (previousXp != this.m_xp) {
            this.fireXpChanged(previousXp);
        }
    }
    
    public void addXp(final long xpToAdd) {
        this.setXp(this.m_xp + xpToAdd);
    }
    
    public void setName(final String name) {
        final boolean nameChanged = (name != null || this.m_name != null) && name != null && !name.equals(this.m_name);
        this.m_name = name;
        if (nameChanged) {
            this.fireNameChanged();
        }
    }
    
    public void setId(final long id) {
        this.m_id = id;
        this.fireIdChanged();
    }
    
    public long getOwnerId() {
        return this.m_ownerId;
    }
    
    public void setOwnerId(final long ownerId) {
        this.m_ownerId = ownerId;
    }
    
    public byte[] getSerializedEquipment() {
        return this.m_serializedEquipment.clone();
    }
    
    public void setSerializedEquipment(final byte[] serializedEquipment) {
        this.m_serializedEquipment = serializedEquipment.clone();
    }
    
    public ItemEquipment getItemEquipment() {
        return this.m_itemEquipment;
    }
    
    public boolean isUnlocked() {
        return this.m_isUnlocked;
    }
    
    public void setUnlocked(final boolean unlocked) {
        this.m_isUnlocked = unlocked;
        this.fireUnlockedChanged();
    }
    
    public int getHpMax() {
        return this.m_hpMax;
    }
    
    public void setHpMax(final int hpMax) {
        this.m_hpMax = hpMax;
        this.fireMaxHpChanged();
    }
    
    public int getCurrentHp() {
        return this.m_currentHp;
    }
    
    public void setCurrentHp(final int currentHp) {
        this.m_currentHp = currentHp;
        this.fireCurrentHpChanged();
    }
    
    public int getSerializationVersion() {
        return this.m_serializationVersion;
    }
    
    public void setSerializationVersion(final int serializationVersion) {
        this.m_serializationVersion = serializationVersion;
    }
    
    public boolean hasEquipment(final CompanionModel companion) {
        if (!this.m_itemEquipment.isEmpty()) {
            return true;
        }
        if (this.m_serializedEquipment == null) {
            return false;
        }
        final CharacterSerializedEquipmentInventory equipmentInventory = new CharacterSerializedEquipmentInventory();
        try {
            equipmentInventory.unserializeVersion(ByteBuffer.wrap(companion.m_serializedEquipment.clone()), this.m_serializationVersion);
        }
        catch (Exception e) {
            CompanionModel.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        return !equipmentInventory.equipment.contents.isEmpty();
    }
    
    public boolean assertEquipmentSerialisationValid() {
        final VersionableObject equipmentInventory = new CharacterSerializedEquipmentInventory();
        try {
            equipmentInventory.unserializeVersion(ByteBuffer.wrap(this.m_serializedEquipment.clone()), this.m_serializationVersion);
        }
        catch (Exception e) {
            CompanionModel.m_logger.error((Object)"Exception levee", (Throwable)e);
            return false;
        }
        return true;
    }
    
    public void removeListener(final CompanionModelListener listener) {
        if (!this.m_listeners.contains(listener)) {
            return;
        }
        this.m_listeners.remove(listener);
    }
    
    public void addListener(final CompanionModelListener listener) {
        if (this.m_listeners.contains(listener)) {
            return;
        }
        this.m_listeners.add(listener);
    }
    
    private void fireNameChanged() {
        final ArrayList<CompanionModelListener> listeners = new ArrayList<CompanionModelListener>(this.m_listeners);
        for (final CompanionModelListener listener : listeners) {
            listener.nameChanged(this);
        }
    }
    
    private void fireXpChanged(final long previousXp) {
        final ArrayList<CompanionModelListener> listeners = new ArrayList<CompanionModelListener>(this.m_listeners);
        for (final CompanionModelListener listener : listeners) {
            listener.xpChanged(this, previousXp);
        }
    }
    
    private void fireIdChanged() {
        final ArrayList<CompanionModelListener> listeners = new ArrayList<CompanionModelListener>(this.m_listeners);
        for (final CompanionModelListener listener : listeners) {
            listener.idChanged(this);
        }
    }
    
    private void fireCurrentHpChanged() {
        final ArrayList<CompanionModelListener> listeners = new ArrayList<CompanionModelListener>(this.m_listeners);
        for (final CompanionModelListener listener : listeners) {
            listener.onCurrentHpChanged(this);
        }
    }
    
    private void fireMaxHpChanged() {
        final ArrayList<CompanionModelListener> listeners = new ArrayList<CompanionModelListener>(this.m_listeners);
        for (final CompanionModelListener listener : listeners) {
            listener.onMaxHpChanged(this);
        }
    }
    
    private void fireUnlockedChanged() {
        final ArrayList<CompanionModelListener> listeners = new ArrayList<CompanionModelListener>(this.m_listeners);
        for (final CompanionModelListener listener : listeners) {
            listener.onUnlockedChanged(this);
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CompanionModel && this.m_id == ((CompanionModel)obj).m_id;
    }
    
    @Override
    public String toString() {
        return "CompanionModel{m_id=" + this.m_id + ", m_breedId=" + this.m_breedId + ", m_xp=" + this.m_xp + ", level=" + this.getLevel() + ", m_name='" + this.m_name + '\'' + ", m_ownerId=" + this.m_ownerId + ", m_itemEquipment=" + this.m_itemEquipment + ", m_serializedEquipment=" + Arrays.toString(this.m_serializedEquipment) + ", m_isUnlocked=" + this.m_isUnlocked + ", m_hpMax=" + this.m_hpMax + ", m_currentHp=" + this.m_currentHp + ", m_serializationVersion=" + this.m_serializationVersion + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompanionModel.class);
    }
}
