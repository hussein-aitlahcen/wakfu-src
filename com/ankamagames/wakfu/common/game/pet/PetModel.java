package com.ankamagames.wakfu.common.game.pet;

import org.jetbrains.annotations.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;

class PetModel implements Pet
{
    public static final int MAX_HUNGRY_CYCLES = 3;
    private final List<PetModelListener> m_listeners;
    private PetDefinition m_definition;
    @Nullable
    private String m_name;
    private int m_colorItemRefId;
    private int m_health;
    private int m_xp;
    private int m_equippedRefItemId;
    private final GameDate m_lastMealDate;
    private final GameDate m_lastHungryDate;
    private int m_sleepRefItemId;
    private final GameDate m_sleepDate;
    
    PetModel() {
        super();
        this.m_listeners = new ArrayList<PetModelListener>();
        this.m_lastMealDate = new GameDate(null);
        this.m_lastHungryDate = new GameDate(null);
        this.m_sleepDate = new GameDate(null);
    }
    
    void initialize(final PetDefinition definition) {
        this.m_definition = definition;
        this.m_colorItemRefId = definition.getBaseColorItemRefId();
        this.m_health = definition.getHealth();
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        this.m_lastMealDate.set(now);
        this.m_lastHungryDate.set(now);
    }
    
    @Override
    public PetDefinition getDefinition() {
        return this.m_definition;
    }
    
    @Nullable
    @Override
    public String getName() {
        return this.m_name;
    }
    
    @Override
    public int getHealth() {
        return this.m_health;
    }
    
    @Override
    public int getColorItemRefId() {
        return this.m_colorItemRefId;
    }
    
    @Override
    public PetDefinitionColor getColorDefinition() {
        return this.m_definition.getColorDefinition(this.m_colorItemRefId);
    }
    
    @Override
    public int getEquippedRefItemId() {
        return this.m_equippedRefItemId;
    }
    
    @Override
    public int getXp() {
        return this.m_xp;
    }
    
    @Override
    public short getLevel() {
        return MathHelper.ensureShort(this.m_xp / this.m_definition.getXpPerLevel());
    }
    
    @Override
    public short getMaxLevel() {
        return this.m_definition.getMaxLevel();
    }
    
    @Override
    public GameDateConst getLastMealDate() {
        return this.m_lastMealDate;
    }
    
    @Override
    public GameDate getLastHungryDate() {
        return this.m_lastHungryDate;
    }
    
    @Override
    public int getSleepRefItemId() {
        return this.m_sleepRefItemId;
    }
    
    @Override
    public GameDateConst getSleepDate() {
        return this.m_sleepDate;
    }
    
    @Override
    public boolean isSleeping() {
        return !this.m_sleepDate.isNull() && this.m_sleepRefItemId > 0;
    }
    
    @Override
    public boolean isActive() {
        return this.m_health > 0 && !this.isSleeping();
    }
    
    void setName(@Nullable final String name) {
        this.m_name = name;
        this.fireNameChanged();
    }
    
    void setHealth(final int value) {
        this.m_health = MathHelper.clamp(value, 0, this.m_definition.getHealth());
        this.fireHealthChanged();
    }
    
    void setColorItemRefId(final int colorItemRefId) {
        this.m_colorItemRefId = colorItemRefId;
        this.fireColorChanged();
    }
    
    void setEquippedRefItemId(final int equippedRefItemId) {
        this.m_equippedRefItemId = equippedRefItemId;
        this.fireEquippedItemChanged();
    }
    
    void setXp(final int xp) {
        this.m_xp = Math.min(Math.max(0, xp), this.m_definition.getXpPerLevel() * this.m_definition.getMaxLevel());
        this.fireXpChanged();
    }
    
    void setLastMealDate(final GameDateConst lastMealDate) {
        this.m_lastMealDate.set(lastMealDate);
        this.fireLastMealDateChanged();
    }
    
    void setLastHungryDate(final GameDateConst lastHungryDate) {
        this.m_lastHungryDate.set(lastHungryDate);
        this.fireLastHungryDateChanged();
    }
    
    void setSleepDate(final GameDateConst sleepDate) {
        this.m_sleepDate.set(sleepDate);
        this.fireSleepDateChanged();
    }
    
    void setSleepRefItemId(final int sleepRefItemId) {
        this.m_sleepRefItemId = sleepRefItemId;
        this.fireSleepItemChanged();
    }
    
    private void fireNameChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).nameChanged(this.m_name);
        }
    }
    
    private void fireColorChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).colorItemChanged(this.m_colorItemRefId);
        }
    }
    
    private void fireEquippedItemChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).equippedItemChanged(this.m_equippedRefItemId);
        }
    }
    
    private void fireHealthChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).healthChanged(this.m_health);
        }
    }
    
    private void fireXpChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).xpChanged(this.m_xp);
        }
    }
    
    private void fireLastMealDateChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).lastMealDateChanged(this.m_lastMealDate);
        }
    }
    
    private void fireLastHungryDateChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).lastHungryDateChanged(this.m_lastHungryDate);
        }
    }
    
    private void fireSleepDateChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).sleepDateChanged(this.m_sleepDate);
        }
    }
    
    private void fireSleepItemChanged() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).sleepItemChanged(this.m_sleepRefItemId);
        }
    }
    
    @Override
    public final boolean addListener(final PetModelListener listener) {
        return !this.m_listeners.contains(listener) && this.m_listeners.add(listener);
    }
    
    @Override
    public final boolean removeListener(final PetModelListener listener) {
        return this.m_listeners.remove(listener);
    }
    
    @Override
    public boolean toRaw(final RawPet raw) {
        raw.definitionId = this.m_definition.getId();
        raw.colorItemRefId = this.m_colorItemRefId;
        raw.equippedRefItemId = this.m_equippedRefItemId;
        raw.health = this.m_health;
        raw.lastMealDate = this.m_lastMealDate.toLong();
        raw.lastHungryDate = this.m_lastHungryDate.toLong();
        raw.sleepRefItemId = this.m_sleepRefItemId;
        raw.sleepDate = this.m_sleepDate.toLong();
        raw.name = this.m_name;
        raw.xp = this.m_xp;
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawPet raw) {
        this.m_definition = PetDefinitionManager.INSTANCE.get(raw.definitionId);
        this.setColorItemRefId(raw.colorItemRefId);
        this.setEquippedRefItemId(raw.equippedRefItemId);
        this.setHealth(raw.health);
        this.setLastMealDate(GameDate.fromLong(raw.lastMealDate));
        this.setLastHungryDate(GameDate.fromLong(raw.lastHungryDate));
        this.setSleepRefItemId(raw.sleepRefItemId);
        this.setSleepDate(GameDate.fromLong(raw.sleepDate));
        this.setName(raw.name);
        this.setXp(raw.xp);
        return true;
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("pet(");
        buffer.append("m_listeners=").append(this.m_listeners.size());
        buffer.append(", m_definition=").append(this.m_definition);
        buffer.append(", m_name='").append(this.m_name).append('\'');
        buffer.append(", m_colorItemRefId=").append(this.m_colorItemRefId);
        buffer.append(", m_health=").append(this.m_health);
        buffer.append(", m_xp=").append(this.m_xp);
        buffer.append(", m_equippedRefItemId=").append(this.m_equippedRefItemId);
        buffer.append(", m_lastMealDate=").append(this.m_lastMealDate);
        buffer.append(", m_lastHungryDate=").append(this.m_lastHungryDate);
        buffer.append(", m_sleepRefItemId=").append(this.m_sleepRefItemId);
        buffer.append(", m_sleepDate=").append(this.m_sleepDate);
        buffer.append(')');
        return buffer.toString();
    }
    
    @Override
    public String getLogRepresentation() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("pet(");
        buffer.append("m_name='").append(this.m_name).append('\'');
        buffer.append(", m_colorItemRefId=").append(this.m_colorItemRefId);
        buffer.append(", m_health=").append(this.m_health);
        buffer.append(", m_xp=").append(this.m_xp);
        buffer.append(", m_equippedRefItemId=").append(this.m_equippedRefItemId);
        buffer.append(')');
        return buffer.toString();
    }
}
