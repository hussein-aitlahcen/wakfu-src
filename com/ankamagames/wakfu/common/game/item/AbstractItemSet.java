package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import org.apache.log4j.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class AbstractItemSet<R extends AbstractReferenceItem> implements Iterable<R>
{
    private static final Logger m_logger;
    private final short m_id;
    private final int m_linkedReferenceItemId;
    protected final List<R> m_referenceItems;
    protected final TIntObjectHashMap<ItemSetLevel> m_effects;
    
    public AbstractItemSet(final short id, final int linkedRefItemId, final List<R> referenceItems) {
        super();
        this.m_effects = new TIntObjectHashMap<ItemSetLevel>(1);
        this.m_id = id;
        this.m_linkedReferenceItemId = linkedRefItemId;
        this.m_referenceItems = referenceItems;
    }
    
    public AbstractItemSet(final short id, final int linkedRefItemId) {
        super();
        this.m_effects = new TIntObjectHashMap<ItemSetLevel>(1);
        this.m_id = id;
        this.m_linkedReferenceItemId = linkedRefItemId;
        this.m_referenceItems = new ArrayList<R>();
    }
    
    public short getId() {
        return this.m_id;
    }
    
    @Override
    public Iterator<R> iterator() {
        return this.m_referenceItems.iterator();
    }
    
    protected List<R> getReferenceItems() {
        return this.m_referenceItems;
    }
    
    public void addReferenceItem(final R item) {
        if (item == null) {
            AbstractItemSet.m_logger.warn((Object)("ajout d'un item null dans le set " + this.getId()));
            return;
        }
        this.m_referenceItems.add(item);
    }
    
    public void addAllReferenceItems(final Collection<R> items) {
        this.m_referenceItems.addAll((Collection<? extends R>)items);
    }
    
    public void addEffect(final int nbElements, final WakfuEffect effect) {
        ItemSetLevel effects = this.m_effects.get(nbElements);
        if (effects == null) {
            effects = new ItemSetLevel((short)nbElements, this.m_id);
            this.m_effects.put(nbElements, effects);
        }
        effects.addEffect(effect);
    }
    
    public ItemSetLevel getEffectsByNbElements(int nbElements, final boolean takePreviousIfEmpty) {
        if (nbElements == 0) {
            return null;
        }
        final ItemSetLevel effectsForThisNumberOfElements = this.m_effects.get(nbElements);
        if (effectsForThisNumberOfElements != null) {
            return effectsForThisNumberOfElements;
        }
        if (takePreviousIfEmpty) {
            --nbElements;
            return this.getEffectsByNbElements(nbElements, takePreviousIfEmpty);
        }
        return null;
    }
    
    public ArrayList<WakfuEffect> getItemEffects(final int equipedNumber) {
        final ItemSetLevel isl = this.getEffectsByNbElements(equipedNumber, false);
        if (isl == null) {
            return null;
        }
        final ArrayList<WakfuEffect> effects = new ArrayList<WakfuEffect>(0);
        for (final WakfuEffect e : isl) {
            effects.add(e);
        }
        return effects;
    }
    
    public ItemSetLevel getEffectsByNbElements(final int nbElements) {
        return this.getEffectsByNbElements(nbElements, true);
    }
    
    public ArrayList<ItemSetLevel> getEffectsToApplyByNbElements(final int nbElements) {
        final ArrayList<ItemSetLevel> list = new ArrayList<ItemSetLevel>();
        for (int i = 1; i <= nbElements; ++i) {
            final ItemSetLevel isl = this.getEffectsByNbElements(i, false);
            if (isl != null) {
                list.add(isl);
            }
        }
        return list;
    }
    
    public int getNumberOfEffectByNbElements(int nbElements) {
        if (nbElements == 0) {
            return 0;
        }
        final ItemSetLevel effetsForThisNumberOfElements = this.m_effects.get(nbElements);
        if (effetsForThisNumberOfElements == null) {
            --nbElements;
            return this.getNumberOfEffectByNbElements(nbElements);
        }
        return effetsForThisNumberOfElements.getEffectNumber();
    }
    
    public int getPiecesNumber() {
        return this.m_referenceItems.size();
    }
    
    public int getLinkedReferenceItemId() {
        return this.m_linkedReferenceItemId;
    }
    
    public final int[] getItemIds() {
        final int[] itemIds = new int[this.m_referenceItems.size()];
        for (int i = 0; i < this.m_referenceItems.size(); ++i) {
            itemIds[i] = this.m_referenceItems.get(i).getId();
        }
        return itemIds;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractItemSet.class);
    }
}
