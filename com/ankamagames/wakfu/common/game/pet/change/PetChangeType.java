package com.ankamagames.wakfu.common.game.pet.change;

import com.ankamagames.framework.kernel.core.common.*;
import org.jetbrains.annotations.*;

public enum PetChangeType
{
    NAME((SimpleObjectFactory<? extends PetChange>)new NameFactory()), 
    HEALTH((SimpleObjectFactory<? extends PetChange>)new HealthFactory()), 
    XP((SimpleObjectFactory<? extends PetChange>)new XpFactory()), 
    LAST_MEAL_DATE((SimpleObjectFactory<? extends PetChange>)new LasMealDateFactory()), 
    LAST_HUNGRY_DATE((SimpleObjectFactory<? extends PetChange>)new LastHungryDateFactory()), 
    COLOR((SimpleObjectFactory<? extends PetChange>)new ColorFactory()), 
    EQUIPMENT((SimpleObjectFactory<? extends PetChange>)new EquipmentFactory()), 
    SLEEP_DATE((SimpleObjectFactory<? extends PetChange>)new SleepDateFactory()), 
    SLEEP_ITEM((SimpleObjectFactory<? extends PetChange>)new SleepItemFactory());
    
    public final byte idx;
    private final SimpleObjectFactory<? extends PetChange> factory;
    
    private PetChangeType(final SimpleObjectFactory<? extends PetChange> fact) {
        this.idx = (byte)this.ordinal();
        this.factory = fact;
    }
    
    public PetChange createNew() {
        return (PetChange)this.factory.createNew();
    }
    
    @Nullable
    public static PetChangeType fromId(final byte id) {
        final PetChangeType[] values = values();
        for (int i = 0, length = values.length; i < length; ++i) {
            final PetChangeType type = values[i];
            if (type.idx == id) {
                return type;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "PetChangeType{idx=" + this.idx + ", factory=" + this.factory + '}';
    }
    
    private static class NameFactory implements SimpleObjectFactory<PetNameChange>
    {
        @Override
        public PetNameChange createNew() {
            return new PetNameChange();
        }
    }
    
    private static class HealthFactory implements SimpleObjectFactory<PetHealthChange>
    {
        @Override
        public PetHealthChange createNew() {
            return new PetHealthChange();
        }
    }
    
    private static class XpFactory implements SimpleObjectFactory<PetXpChange>
    {
        @Override
        public PetXpChange createNew() {
            return new PetXpChange();
        }
    }
    
    private static class LasMealDateFactory implements SimpleObjectFactory<PetLastMealDateChange>
    {
        @Override
        public PetLastMealDateChange createNew() {
            return new PetLastMealDateChange();
        }
    }
    
    private static class LastHungryDateFactory implements SimpleObjectFactory<PetLastHungryDateChange>
    {
        @Override
        public PetLastHungryDateChange createNew() {
            return new PetLastHungryDateChange();
        }
    }
    
    private static class ColorFactory implements SimpleObjectFactory<PetColorChange>
    {
        @Override
        public PetColorChange createNew() {
            return new PetColorChange();
        }
    }
    
    private static class EquipmentFactory implements SimpleObjectFactory<PetEquipmentChange>
    {
        @Override
        public PetEquipmentChange createNew() {
            return new PetEquipmentChange();
        }
    }
    
    private static class SleepDateFactory implements SimpleObjectFactory<PetSleepDateChange>
    {
        @Override
        public PetSleepDateChange createNew() {
            return new PetSleepDateChange();
        }
    }
    
    private static class SleepItemFactory implements SimpleObjectFactory<PetSleepRefItemIdChange>
    {
        @Override
        public PetSleepRefItemIdChange createNew() {
            return new PetSleepRefItemIdChange();
        }
    }
}
