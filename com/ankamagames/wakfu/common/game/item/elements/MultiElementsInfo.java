package com.ankamagames.wakfu.common.game.item.elements;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import java.util.*;
import gnu.trove.*;

public class MultiElementsInfo
{
    private static final Logger m_logger;
    public static final List<Elements> ELEMENTS_LIST;
    private TIntObjectHashMap<HashSet<Elements>> m_multiElementsEffects;
    
    public MultiElementsInfo() {
        super();
        this.m_multiElementsEffects = new TIntObjectHashMap<HashSet<Elements>>();
    }
    
    public MultiElementsInfo(final MultiElementsInfo multiElementsInfo) {
        super();
        this.m_multiElementsEffects = new TIntObjectHashMap<HashSet<Elements>>();
        final TIntObjectIterator<HashSet<Elements>> it = multiElementsInfo.m_multiElementsEffects.iterator();
        while (it.hasNext()) {
            it.advance();
            final HashSet<Elements> elements = new HashSet<Elements>();
            for (final Elements elem : it.value()) {
                elements.add(elem);
            }
            this.m_multiElementsEffects.put(it.key(), elements);
        }
    }
    
    public void toRaw(final RawItemElements raw) {
        raw.damageElements = getElementMask(this.m_multiElementsEffects.get(RunningEffectConstants.VARIABLE_ELEMENTS_DAMAGE_GAIN.getId()));
        raw.resistanceElements = getElementMask(this.m_multiElementsEffects.get(RunningEffectConstants.VARIABLE_ELEMENTS_RES_GAIN.getId()));
    }
    
    public static MultiElementsInfo unserialize(final RawItemElements rawItemElements) {
        final MultiElementsInfo multiElementsInfo = new MultiElementsInfo();
        putElementsFromRaw(multiElementsInfo, rawItemElements.damageElements, RunningEffectConstants.VARIABLE_ELEMENTS_DAMAGE_GAIN.getId());
        putElementsFromRaw(multiElementsInfo, rawItemElements.resistanceElements, RunningEffectConstants.VARIABLE_ELEMENTS_RES_GAIN.getId());
        return multiElementsInfo;
    }
    
    public static void putElementsFromRaw(final MultiElementsInfo multiElementsInfo, final byte elementsMask, final int actionId) {
        if (elementsMask != 0) {
            final HashSet<Elements> elements = new HashSet<Elements>();
            for (final Elements e : MultiElementsInfo.ELEMENTS_LIST) {
                if ((elementsMask & 1 << e.getId()) != 0x0) {
                    elements.add(e);
                }
            }
            multiElementsInfo.putElement(actionId, elements);
        }
    }
    
    private void putElement(final int actionId, final HashSet<Elements> elements) {
        this.m_multiElementsEffects.put(actionId, elements);
    }
    
    public static byte getElementMask(final HashSet<Elements> damageElements) {
        byte elemMask = 0;
        if (damageElements != null) {
            for (final Elements e : MultiElementsInfo.ELEMENTS_LIST) {
                if (damageElements.contains(e)) {
                    elemMask |= (byte)(1 << e.getId());
                }
            }
        }
        return elemMask;
    }
    
    public byte getElementMask(final int actionId) {
        return getElementMask(this.m_multiElementsEffects.get(actionId));
    }
    
    public void rollRandomElementsEffects(final Item forItem) {
        for (final WakfuEffect wakfuEffect : forItem) {
            final int actionId = wakfuEffect.getActionId();
            if (actionId == RunningEffectConstants.VARIABLE_ELEMENTS_DAMAGE_GAIN.getId() || actionId == RunningEffectConstants.VARIABLE_ELEMENTS_RES_GAIN.getId()) {
                if (this.m_multiElementsEffects == null) {
                    this.m_multiElementsEffects = new TIntObjectHashMap<HashSet<Elements>>();
                }
                final HashSet<Elements> set = this.m_multiElementsEffects.get(actionId);
                final HashSet<Elements> elementsToAddList = new HashSet<Elements>();
                do {
                    elementsToAddList.clear();
                    final ArrayList<Elements> elementsList = new ArrayList<Elements>(MultiElementsInfo.ELEMENTS_LIST);
                    final int nbElements = (int)wakfuEffect.getParam(1);
                    if (nbElements > elementsList.size()) {
                        MultiElementsInfo.m_logger.error((Object)("[GD-SAISIE] un effet \u00e0 \u00e9l\u00e9ments variables demande un nombre d'\u00e9l\u00e9ments trop important : " + wakfuEffect));
                    }
                    else {
                        while (nbElements > elementsToAddList.size()) {
                            final int index = DiceRoll.roll(elementsList.size()) - 1;
                            final Elements pickedElement = elementsList.remove(index);
                            elementsToAddList.add(pickedElement);
                        }
                    }
                } while (getElementMask(set) == getElementMask(elementsToAddList));
                this.m_multiElementsEffects.put(actionId, elementsToAddList);
            }
        }
    }
    
    public HashSet<Elements> get(final int actionId) {
        return this.m_multiElementsEffects.get(actionId);
    }
    
    public TIntObjectHashMap<HashSet<Elements>> getMultiElementsEffects() {
        return this.m_multiElementsEffects;
    }
    
    public boolean isEmpty() {
        if (this.m_multiElementsEffects.isEmpty()) {
            return true;
        }
        final TIntObjectIterator<HashSet<Elements>> it = this.m_multiElementsEffects.iterator();
        while (it.hasNext()) {
            it.advance();
            if (!it.value().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public int elementsCount(final int actionId) {
        if (!this.m_multiElementsEffects.containsKey(actionId)) {
            return 0;
        }
        return this.m_multiElementsEffects.get(actionId).size();
    }
    
    @Override
    public String toString() {
        return "MultiElementsInfo{m_multiElementsEffects=" + this.m_multiElementsEffects + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)MultiElementsInfo.class);
        ELEMENTS_LIST = Arrays.asList(Elements.AIR, Elements.EARTH, Elements.FIRE, Elements.WATER);
    }
}
