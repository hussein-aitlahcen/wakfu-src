package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import java.util.*;
import gnu.trove.*;

public class CraftHarvestElementManager
{
    public static final CraftHarvestElementManager INSTANCE;
    private TIntObjectHashMap<AbstractCraftHarvestElement> m_elements;
    private TIntObjectHashMap<TIntArrayList> m_craftElements;
    private boolean m_initialized;
    private static final ArrayList<AbstractCraftHarvestElement> EMPTY_LIST;
    
    private CraftHarvestElementManager() {
        super();
        this.m_elements = new TIntObjectHashMap<AbstractCraftHarvestElement>();
        this.m_craftElements = new TIntObjectHashMap<TIntArrayList>();
        this.m_initialized = false;
    }
    
    public void init() {
        final TIntObjectIterator<ReferenceResource> it = ReferenceResourceManager.getInstance().iterator();
        while (it.hasNext()) {
            it.advance();
            final ReferenceResource resource = it.value();
            final ResourceType type = ResourceType.getByAgtIdOrHWCategory(resource.getResourceType());
            if (type == null) {
                continue;
            }
            for (int i = 0, stepCount = resource.getEvolutionStepCount(); i < stepCount; ++i) {
                final ResourceEvolutionStep step = resource.getQuickEvolutionStep(i);
                for (int j = 0, collectCount = step.getCollectsCount(); j < collectCount; ++j) {
                    final CollectAction action = step.getQuickCollect(j);
                    if (action.isDisplayInCraftDialog()) {
                        for (final int itemId : action.getLootList()) {
                            AbstractCraftHarvestElement elem = this.m_elements.get(itemId);
                            if (itemId != 0) {
                                if (elem == null) {
                                    elem = new ResourceCraftHarvestElement(itemId, action.getVisualId(), resource.getId(), action.getLevelMin(), action.getCollectDuration(), action.getNbRequiredPlayer() > 1, type);
                                    this.m_elements.put(itemId, elem);
                                }
                                else {
                                    final int levelMin = Math.min(elem.getLevelMin(), action.getLevelMin());
                                    elem.setLevelMin(levelMin);
                                }
                                final int craftId = action.getCraftId();
                                TIntArrayList craftItems = this.m_craftElements.get(craftId);
                                if (craftItems == null) {
                                    craftItems = new TIntArrayList();
                                    this.m_craftElements.put(craftId, craftItems);
                                }
                                if (!craftItems.contains(itemId)) {
                                    craftItems.add(itemId);
                                }
                            }
                        }
                    }
                }
            }
        }
        final TIntObjectIterator<MonsterBreed> monsterIt = MonsterBreedManager.getInstance().getFullList().iterator();
        while (monsterIt.hasNext()) {
            monsterIt.advance();
            final MonsterBreed monsterBreed = monsterIt.value();
            final TIntObjectIterator<CollectAction> collectIt = monsterBreed.collectActionIterator();
            while (collectIt.hasNext()) {
                collectIt.advance();
                final CollectAction action2 = collectIt.value();
                if (!action2.isDisplayInCraftDialog()) {
                    continue;
                }
                for (final int itemId2 : action2.getLootList()) {
                    AbstractCraftHarvestElement elem2 = this.m_elements.get(itemId2);
                    if (elem2 == null) {
                        final MonsterFamily family = MonsterFamilyManager.getInstance().getMonsterFamily(monsterBreed.getFamilyId());
                        elem2 = new MonsterFamilyCraftHarvestElement(itemId2, action2.getVisualId(), family.getParentFamilyId(), action2.getLevelMin(), action2.getCollectDuration(), action2.getNbRequiredPlayer() > 1);
                        this.m_elements.put(itemId2, elem2);
                    }
                    else {
                        final int levelMin2 = Math.min(elem2.getLevelMin(), action2.getLevelMin());
                        elem2.setLevelMin(levelMin2);
                    }
                    final int craftId2 = action2.getCraftId();
                    TIntArrayList crafts = this.m_craftElements.get(craftId2);
                    if (crafts == null) {
                        crafts = new TIntArrayList();
                        this.m_craftElements.put(craftId2, crafts);
                    }
                    if (!crafts.contains(itemId2)) {
                        crafts.add(itemId2);
                    }
                }
            }
        }
        this.m_initialized = true;
    }
    
    public boolean hasElements(final int craftId) {
        if (!this.m_initialized) {
            this.init();
        }
        final TIntArrayList elements = this.m_craftElements.get(craftId);
        return elements != null && elements.size() != 0;
    }
    
    public ArrayList<AbstractCraftHarvestElement> getElements(final int craftId, final int maxLevel) {
        if (!this.m_initialized) {
            this.init();
        }
        final TIntArrayList items = this.m_craftElements.get(craftId);
        if (items != null) {
            final ArrayList<AbstractCraftHarvestElement> elements = new ArrayList<AbstractCraftHarvestElement>(items.size());
            for (int i = 0, size = items.size(); i < size; ++i) {
                final AbstractCraftHarvestElement elem = this.m_elements.get(items.get(i));
                if (elem.getLevelMin() <= maxLevel) {
                    elements.add(elem);
                }
            }
            Collections.sort(elements, ElementLevelComparator.COMPARATOR);
            return elements;
        }
        return CraftHarvestElementManager.EMPTY_LIST;
    }
    
    static {
        INSTANCE = new CraftHarvestElementManager();
        EMPTY_LIST = new ArrayList<AbstractCraftHarvestElement>(0);
    }
    
    private static final class ElementLevelComparator implements Comparator<AbstractCraftHarvestElement>
    {
        private static final ElementLevelComparator COMPARATOR;
        
        @Override
        public int compare(final AbstractCraftHarvestElement o1, final AbstractCraftHarvestElement o2) {
            return o1.getLevelMin() - o2.getLevelMin();
        }
        
        static {
            COMPARATOR = new ElementLevelComparator();
        }
    }
}
