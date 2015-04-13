package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;

public class ItemSetLevel implements WakfuEffectContainer
{
    public static short DEFAULT_AGGRO_WEIGHT;
    public static short DEFAULT_ALLY_EFFICACITY;
    public static short DEFAULT_FOE_EFFICACITY;
    private final ArrayList<WakfuEffect> m_effects;
    private short m_id;
    private short m_requirednumber;
    
    public ItemSetLevel(final short requiredNumber, final short id) {
        super();
        this.m_effects = new ArrayList<WakfuEffect>();
        this.m_requirednumber = requiredNumber;
        this.m_id = id;
    }
    
    public short getRequiredNumber() {
        return this.m_requirednumber;
    }
    
    @Override
    public short getLevel() {
        return 0;
    }
    
    @Override
    public int getContainerType() {
        return 14;
    }
    
    @Override
    public long getEffectContainerId() {
        return this.m_id;
    }
    
    @Override
    public Iterator<WakfuEffect> iterator() {
        return this.m_effects.iterator();
    }
    
    public void addEffect(final WakfuEffect effect) {
        this.m_effects.add(effect);
    }
    
    public int getEffectNumber() {
        return this.m_effects.size();
    }
    
    @Override
    public short getAggroWeight() {
        return ItemSetLevel.DEFAULT_AGGRO_WEIGHT;
    }
    
    @Override
    public short getAllyEfficacity() {
        return ItemSetLevel.DEFAULT_ALLY_EFFICACITY;
    }
    
    @Override
    public short getFoeEfficacity() {
        return ItemSetLevel.DEFAULT_FOE_EFFICACITY;
    }
    
    static {
        ItemSetLevel.DEFAULT_AGGRO_WEIGHT = 0;
        ItemSetLevel.DEFAULT_ALLY_EFFICACITY = 0;
        ItemSetLevel.DEFAULT_FOE_EFFICACITY = 0;
    }
}
