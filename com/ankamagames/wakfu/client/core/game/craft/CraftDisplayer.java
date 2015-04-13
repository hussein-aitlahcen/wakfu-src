package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import java.util.*;

public class CraftDisplayer extends ImmutableFieldProvider
{
    public static final CraftDisplayer INSTANCE;
    public static final String ALL_KNOWN_CRAFTS = "allKnownCrafts";
    public static final String CRAFTS_FIELD = "crafts";
    public static final String CRAFTS_BY_LEVEL_FIELD = "craftsByLevel";
    public static final String CRAFTS_BY_KNOWN_FIELD = "craftsByKnown";
    public static final String HARVESTS_FIELD = "harvests";
    public static final String HARVESTS_BY_LEVEL_FIELD = "harvestsByLevel";
    public static final String HARVESTS_BY_KNOWN_FIELD = "harvestsByKnown";
    private static final Logger m_logger;
    private final ArrayList<AbstractCraftView> m_harvests;
    private final ArrayList<AbstractCraftView> m_crafts;
    
    public CraftDisplayer() {
        super();
        this.m_harvests = new ArrayList<AbstractCraftView>();
        this.m_crafts = new ArrayList<AbstractCraftView>();
    }
    
    public void clear() {
        this.m_crafts.clear();
        this.m_harvests.clear();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "crafts", "harvests", "craftsByLevel", "harvestsByLevel", "craftsByKnown", "harvestsByKnown", "allKnownCrafts");
    }
    
    public AbstractCraftView getCraft(final int craftId) {
        for (int i = this.m_crafts.size() - 1; i >= 0; --i) {
            final AbstractCraftView view = this.m_crafts.get(i);
            if (view.getCraftReferenceId() == craftId) {
                return view;
            }
        }
        for (int i = this.m_harvests.size() - 1; i >= 0; --i) {
            final AbstractCraftView view = this.m_harvests.get(i);
            if (view.getCraftReferenceId() == craftId) {
                return view;
            }
        }
        return null;
    }
    
    protected AbstractCraftView removeCraft(final int craftId) {
        for (int i = this.m_crafts.size() - 1; i >= 0; --i) {
            final AbstractCraftView view = this.m_crafts.get(i);
            if (view.getCraftReferenceId() == craftId) {
                this.m_crafts.remove(i);
                return view;
            }
        }
        for (int i = this.m_harvests.size() - 1; i >= 0; --i) {
            final AbstractCraftView view = this.m_harvests.get(i);
            if (view.getCraftReferenceId() == craftId) {
                this.m_harvests.remove(i);
                return view;
            }
        }
        return null;
    }
    
    private boolean contains(final int[] array, final int value) {
        for (final int i : array) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }
    
    public void addAllCrafts(final int[] knownCrafts) {
        this.m_crafts.clear();
        this.m_harvests.clear();
        CraftManager.INSTANCE.foreachCraft(new TObjectProcedure<ReferenceCraft>() {
            @Override
            public boolean execute(final ReferenceCraft referenceCraft) {
                try {
                    if (referenceCraft.isHiddenCraft()) {
                        return true;
                    }
                    final int craftId = referenceCraft.getId();
                    AbstractCraftView view;
                    if (CraftDisplayer.this.contains(knownCrafts, craftId)) {
                        final CraftView craftView = new CraftView(craftId);
                        craftView.setRecipeFilter(new FixedSizeRecipeFilter(craftView, 8));
                        view = craftView;
                    }
                    else {
                        view = new UnknownCraftView(craftId);
                    }
                    if (view.hasHarvests()) {
                        CraftDisplayer.this.m_harvests.add(view);
                    }
                    else {
                        CraftDisplayer.this.m_crafts.add(view);
                    }
                }
                catch (Exception e) {
                    CraftDisplayer.m_logger.error((Object)("Exception lors de l'initialisation d'un craft id=" + referenceCraft.getId()), (Throwable)e);
                }
                return true;
            }
        });
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "crafts", "harvests", "craftsByLevel", "harvestsByLevel", "craftsByKnown", "harvestsByKnown", "allKnownCrafts");
    }
    
    public void addCraft(final int craftId) {
        final AbstractCraftView craftView = this.getCraft(craftId);
        if (craftView == null) {
            CraftDisplayer.m_logger.warn((Object)("On essaye d'apprendre un craft qui n'est pas dans le CraftManager : " + craftId));
            return;
        }
        if (craftView.isUnknown()) {
            this.removeCraft(craftId);
            final CraftView view = new CraftView(craftId);
            view.setRecipeFilter(new FixedSizeRecipeFilter(view, 8));
            if (view.hasHarvests()) {
                this.m_harvests.add(view);
            }
            else {
                this.m_crafts.add(view);
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "crafts", "harvests", "craftsByLevel", "harvestsByLevel", "craftsByKnown", "harvestsByKnown", "allKnownCrafts");
    }
    
    public CraftView getKnownCraftView(final int craftId) {
        for (final AbstractCraftView craftView : this.m_crafts) {
            if (craftView.getCraftReferenceId() == craftId && !craftView.isUnknown()) {
                return (CraftView)craftView;
            }
        }
        for (final AbstractCraftView craftView : this.m_harvests) {
            if (craftView.getCraftReferenceId() == craftId && !craftView.isUnknown()) {
                return (CraftView)craftView;
            }
        }
        return this.getFirstKnownCraftView();
    }
    
    public CraftView getFirstKnownCraftView() {
        for (int i = 0, size = this.m_crafts.size(); i < size; ++i) {
            final AbstractCraftView craftView = this.m_crafts.get(i);
            if (!craftView.isUnknown()) {
                return (CraftView)craftView;
            }
        }
        for (int i = 0, size = this.m_harvests.size(); i < size; ++i) {
            final AbstractCraftView craftView = this.m_harvests.get(i);
            if (!craftView.isUnknown()) {
                return (CraftView)craftView;
            }
        }
        return null;
    }
    
    public boolean isEmpty() {
        return this.m_crafts.isEmpty() && this.m_harvests.isEmpty();
    }
    
    public int size() {
        return this.m_crafts.size() + this.m_harvests.size();
    }
    
    public void onCraftXpGained(final int craftId, final long xpAdded) {
        final AbstractCraftView craftView = this.getCraft(craftId);
        if (craftView == null || craftView.isUnknown()) {
            return;
        }
        final CraftView view = (CraftView)craftView;
        view.onRecipeLearnt();
    }
    
    public void onRecipeLearnt(final int craftId) {
        final AbstractCraftView craftView = this.getCraft(craftId);
        if (craftView == null || craftView.isUnknown()) {
            return;
        }
        final CraftView view = (CraftView)craftView;
        view.onRecipeLearnt();
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("crafts")) {
            Collections.sort(this.m_crafts, CraftComparator.COMPARATOR);
            return this.m_crafts;
        }
        if (fieldName.equals("craftsByLevel")) {
            Collections.sort(this.m_crafts, CraftLevelComparator.LEVEL_COMPARATOR);
            return this.m_crafts;
        }
        if (fieldName.equals("craftsByKnown")) {
            Collections.sort(this.m_crafts, CraftKnownComparator.KNOWN_COMPARATOR);
            return this.m_crafts;
        }
        if (fieldName.equals("harvests")) {
            Collections.sort(this.m_harvests, CraftComparator.COMPARATOR);
            return this.m_harvests;
        }
        if (fieldName.equals("harvestsByLevel")) {
            Collections.sort(this.m_harvests, CraftLevelComparator.LEVEL_COMPARATOR);
            return this.m_harvests;
        }
        if (fieldName.equals("harvestsByKnown")) {
            Collections.sort(this.m_harvests, CraftKnownComparator.KNOWN_COMPARATOR);
            return this.m_harvests;
        }
        if (fieldName.equals("allKnownCrafts")) {
            final ArrayList<AbstractCraftView> views = new ArrayList<AbstractCraftView>();
            for (int i = 0, size = this.m_crafts.size(); i < size; ++i) {
                final AbstractCraftView craftView = this.m_crafts.get(i);
                if (!craftView.isUnknown()) {
                    views.add(craftView);
                }
            }
            for (int i = 0, size = this.m_harvests.size(); i < size; ++i) {
                final AbstractCraftView craftView = this.m_harvests.get(i);
                if (!craftView.isUnknown()) {
                    views.add(craftView);
                }
            }
            return views;
        }
        return null;
    }
    
    static {
        INSTANCE = new CraftDisplayer();
        m_logger = Logger.getLogger((Class)CraftDisplayer.class);
    }
    
    private static class CraftComparator implements Comparator<AbstractCraftView>
    {
        private static final CraftComparator COMPARATOR;
        
        @Override
        public int compare(final AbstractCraftView o1, final AbstractCraftView o2) {
            return o1.getCraftReferenceId() - o2.getCraftReferenceId();
        }
        
        static {
            COMPARATOR = new CraftComparator();
        }
    }
    
    private static class CraftLevelComparator implements Comparator<AbstractCraftView>
    {
        private static final CraftLevelComparator LEVEL_COMPARATOR;
        
        @Override
        public int compare(final AbstractCraftView o1, final AbstractCraftView o2) {
            return o2.getLevel() - o1.getLevel();
        }
        
        static {
            LEVEL_COMPARATOR = new CraftLevelComparator();
        }
    }
    
    private static class CraftKnownComparator implements Comparator<AbstractCraftView>
    {
        private static final CraftKnownComparator KNOWN_COMPARATOR;
        
        @Override
        public int compare(final AbstractCraftView o1, final AbstractCraftView o2) {
            if (o2.isUnknown() == o1.isUnknown()) {
                return 0;
            }
            return o2.isUnknown() ? -1 : 1;
        }
        
        static {
            KNOWN_COMPARATOR = new CraftKnownComparator();
        }
    }
}
