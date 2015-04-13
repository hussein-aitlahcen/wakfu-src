package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.wakfu.client.core.game.interactiveElement.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.craft.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.wakfu.common.game.craft.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;
import gnu.trove.*;

public class CraftView extends AbstractCraftView
{
    public static final String LEVEL_FIELD = "level";
    public static final String LEVEL_TEXT_FIELD = "levelText";
    public static final String LEVEL_TEXT_COLOR_FIELD = "levelTextColor";
    public static final String CURRENT_XP_PERCENTAGE_FIELD = "currentXpPercentage";
    public static final String NEXT_XP_PERCENTAGE_FIELD = "nextXpPercentage";
    public static final String NEXT_XP_PERCENTAGE_INIT_FIELD = "nextXpPercentageInit";
    public static final String XP_RATIO_FIELD = "xpRatio";
    public static final String RECIPES_FIELD = "recipes";
    public static final String HARVESTS_FIELD = "harvests";
    public static final String CURRENT_RECIPES_PAGE_FIELD = "currentRecipesPage";
    public static final String CURRENT_HARVESTS_PAGE_FIELD = "currentHarvestsPage";
    public static final String HAS_HARVEST = "hasHarvest";
    public static final String HAS_SEEDS_TO_PLANT = "hasSeedsToPlant";
    public static final String HAS_CUTTINGS_TO_TAKE = "hasCuttingsToTake";
    public static final String HAS_RESOURCES_TO_HARVEST = "hasResourcesToHarvest";
    public static final String HAS_RECIPE = "hasRecipe";
    public static final String NUM_RESOURCES_HARVESTED_TITLE = "numResourcesHarvestedTitle";
    public static final String NUM_SEEDS_PLANTED_TITLE = "numSeedsPlantedTitle";
    public static final String NUM_SEEDS_HARVESTED_TITLE = "numSeedsHarvestedTitle";
    public static final String CURRENT_PAGE_TEXT = "currentPageText";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String MAX_NUM_PAGES = "maxNumPages";
    public static final String NUM_PER_PAGE = "numPerPage";
    public static final String IS_OK_FILTERED_FIELD = "isOkFiltered";
    public static final String IS_ABC_SORTED_FIELD = "isAbcSorted";
    public static final String IS_LEVEL_SORTED_FIELD = "isLevelSorted";
    public static final String IS_ABC_SORT_ACTIVATED_FIELD = "isAbcSortActivated";
    public static final String IS_LEVEL_SORT_ACTIVATED_FIELD = "isLevelSortActivated";
    public static final String PASSPORT_ICON_URL_FIELD = "passportIconUrl";
    public static final String IS_CRAFT_FIELD = "isCraft";
    public static final String NUM_SEEDS_PLANTED = "numSeedsPlanted";
    public static final String NUM_CUTTINGS_TAKEN = "numCuttingsTaken";
    public static final String NUM_RESOURCES_HARVESTED = "numResourcesHarvested";
    public static final String NUM_RECIPES_DONE = "numRecipesDone";
    public static final String FREE_MODE = "freeMode";
    public static final String[] FIELDS;
    private CraftInteractiveElement m_craftTable;
    private ArrayList<RecipeView> m_recipeViews;
    private ArrayList<AbstractCraftHarvestElement> m_harvests;
    private boolean m_isOkFiltered;
    private SORTER_STATE m_abcSorterState;
    private SORTER_STATE m_levelSorterState;
    private String m_nameFilter;
    private long m_nextXp;
    private int m_currentLevelRange;
    private boolean m_isInFreeMode;
    private int m_currentPage;
    private CraftType m_currentType;
    private RecipeFilter m_recipeFilter;
    public static final int NUM_RECIPES_PER_PAGE = 8;
    private static final int VISIBLE_DELTA_LEVEL_MAX = 30;
    
    public CraftView(final int craftId, final CraftInteractiveElement wakfuClientMapInteractiveElement) {
        super(craftId);
        this.m_isOkFiltered = false;
        this.m_abcSorterState = SORTER_STATE.DISABLED;
        this.m_levelSorterState = SORTER_STATE.ACTIVATED;
        this.m_nameFilter = "";
        this.m_nextXp = -1L;
        this.m_currentLevelRange = 0;
        this.m_isInFreeMode = false;
        this.m_craftTable = wakfuClientMapInteractiveElement;
        this.getRecipeViews();
        this.getHarvestViews();
    }
    
    public CraftView(final int refCraftId) {
        this(refCraftId, null);
    }
    
    public void setRecipeFilter(final RecipeFilter recipeFilter) {
        this.m_recipeFilter = recipeFilter;
    }
    
    @Override
    public boolean hasRecipes() {
        return !this.m_recipeViews.isEmpty();
    }
    
    @Override
    public boolean hasHarvests() {
        return !this.m_harvests.isEmpty();
    }
    
    public void setInFreeMode(final boolean inFreeMode) {
        this.m_isInFreeMode = inFreeMode;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "freeMode");
    }
    
    @Override
    public int getCraftReferenceId() {
        return this.m_refCraftId;
    }
    
    @Override
    public short getLevel() {
        return WakfuGameEntity.getInstance().getLocalPlayer().getCraftHandler().getLevel(this.m_refCraftId);
    }
    
    @Override
    public String[] getFields() {
        return CraftView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        final CraftHandler craftHandler = WakfuGameEntity.getInstance().getLocalPlayer().getCraftHandler();
        if (fieldName.equals("level")) {
            return this.getLevel();
        }
        if (fieldName.equals("levelText")) {
            return WakfuTranslator.getInstance().getString("levelShort.custom", this.getLevel());
        }
        if (fieldName.equals("levelTextColor")) {
            final String levelText = new TextWidgetFormater().openText().addColor(getColor().getRGBtoHex()).append(this.getLevel()).closeText().finishAndToString();
            return WakfuTranslator.getInstance().getString("levelShort.custom", levelText);
        }
        if (fieldName.equals("xpRatio")) {
            final long currentXp = craftHandler.getXp(this.m_refCraftId);
            final long totalXpNeeded = CraftXPUtil.getTotalXPNeeded(craftHandler.getLevel(this.m_refCraftId));
            final long nextTotalXpNeeded = CraftXPUtil.getTotalXPNeeded((short)(craftHandler.getLevel(this.m_refCraftId) + 1));
            final String formattedCurrentXp = WakfuTranslator.getInstance().formatNumber(currentXp - totalXpNeeded);
            final String formattedTotalXp = WakfuTranslator.getInstance().formatNumber(nextTotalXpNeeded - totalXpNeeded);
            return new TextWidgetFormater().append(formattedCurrentXp).append("/").append(formattedTotalXp).finishAndToString();
        }
        if (fieldName.equals("currentXpPercentage")) {
            return Math.min(1.0, craftHandler.getCurrentPercentLevel(this.m_refCraftId));
        }
        if (fieldName.equals("nextXpPercentage") || fieldName.equals("nextXpPercentageInit")) {
            if (this.m_nextXp == -1L) {
                return craftHandler.getCurrentPercentLevel(this.m_refCraftId);
            }
            if (CraftXPUtil.getCurrentLevel(this.m_nextXp) > this.getLevel()) {
                return 1.0f;
            }
            return craftHandler.getPercentLevelForXp(this.m_refCraftId, this.m_nextXp);
        }
        else {
            if (fieldName.equals("passportIconUrl")) {
                try {
                    if (!URLUtils.urlExists(String.format(WakfuConfiguration.getInstance().getString("craftPassportIconsPath"), this.m_refCraftId))) {
                        return null;
                    }
                }
                catch (PropertyException e) {
                    return null;
                }
                return WakfuConfiguration.getInstance().getIconUrl("craftPassportIconsPath", "defaultIconPath", this.m_refCraftId);
            }
            if (fieldName.equals("hasRecipe")) {
                return !this.getRecipeViews().isEmpty();
            }
            if (fieldName.equals("hasHarvest")) {
                return !this.getHarvestViews().isEmpty();
            }
            if (fieldName.equals("hasSeedsToPlant")) {
                final ArrayList<AbstractCraftHarvestElement> harvests = this.getHarvestViews();
                if (harvests.isEmpty()) {
                    return false;
                }
                final AbstractCraftHarvestElement elem = harvests.get(0);
                switch (elem.getResourceType()) {
                    case MOB:
                    case CULTIVATION:
                    case PLANT:
                    case TREE: {
                        return true;
                    }
                    default: {
                        return false;
                    }
                }
            }
            else if (fieldName.equals("hasResourcesToHarvest")) {
                final ArrayList<AbstractCraftHarvestElement> harvests = this.getHarvestViews();
                if (harvests.isEmpty()) {
                    return false;
                }
                final AbstractCraftHarvestElement elem = harvests.get(0);
                if (elem.getResourceType() == ResourceType.FISH) {
                    return false;
                }
                return true;
            }
            else if (fieldName.equals("hasCuttingsToTake")) {
                final ArrayList<AbstractCraftHarvestElement> harvests = this.getHarvestViews();
                if (harvests.isEmpty()) {
                    return false;
                }
                final AbstractCraftHarvestElement elem = harvests.get(0);
                switch (elem.getResourceType()) {
                    case MOB:
                    case CULTIVATION:
                    case PLANT:
                    case TREE:
                    case FISH: {
                        return true;
                    }
                    default: {
                        return false;
                    }
                }
            }
            else {
                if (fieldName.equals("numSeedsHarvestedTitle")) {
                    return this.getResourceCuttingsTakenName();
                }
                if (fieldName.equals("numSeedsPlantedTitle")) {
                    return this.getResourcePlantedName();
                }
                if (fieldName.equals("numResourcesHarvestedTitle")) {
                    return this.getResourceHarvestedName();
                }
                if (fieldName.equals("harvests")) {
                    return this.getHarvestViews();
                }
                if (fieldName.equals("recipes")) {
                    return this.getRecipeViews();
                }
                if (fieldName.equals("isOkFiltered")) {
                    return this.m_isOkFiltered;
                }
                if (fieldName.equals("isAbcSorted")) {
                    return this.m_abcSorterState == SORTER_STATE.ACTIVATED;
                }
                if (fieldName.equals("isLevelSorted")) {
                    return this.m_levelSorterState == SORTER_STATE.ACTIVATED;
                }
                if (fieldName.equals("isAbcSortActivated")) {
                    return this.m_abcSorterState != SORTER_STATE.DISABLED;
                }
                if (fieldName.equals("isLevelSortActivated")) {
                    return this.m_levelSorterState != SORTER_STATE.DISABLED;
                }
                if (fieldName.equals("isCraft")) {
                    return this.isCraft();
                }
                if (fieldName.equals("numSeedsPlanted")) {
                    final int counter = craftHandler.getPlantationCounter(this.m_refCraftId);
                    return new TextWidgetFormater().append(": ").append(counter).finishAndToString();
                }
                if (fieldName.equals("numCuttingsTaken")) {
                    final int counter = craftHandler.getNonDestructiveCollectCounter(this.m_refCraftId);
                    return new TextWidgetFormater().append(": ").append(counter).finishAndToString();
                }
                if (fieldName.equals("numResourcesHarvested")) {
                    final int counter = craftHandler.getDestructiveCollectCounter(this.m_refCraftId);
                    return new TextWidgetFormater().append(": ").append(counter).finishAndToString();
                }
                if (fieldName.equals("numRecipesDone")) {
                    final int counter = craftHandler.getRecipeCounter(this.m_refCraftId);
                    return new TextWidgetFormater().append(": ").append(counter).finishAndToString();
                }
                if (fieldName.equals("freeMode")) {
                    return this.m_isInFreeMode;
                }
                if (fieldName.equals("currentPage")) {
                    if (this.m_recipeFilter != null) {
                        return this.m_recipeFilter.getCurrentPage();
                    }
                    return 0;
                }
                else {
                    if (!fieldName.equals("currentPageText")) {
                        if (fieldName.equals("numPerPage")) {
                            if (this.m_recipeFilter != null) {
                                return this.m_recipeFilter.getNumPerPage();
                            }
                        }
                        else {
                            if (fieldName.equals("maxNumPages")) {
                                return this.getCurrentMaxPages() - 1;
                            }
                            if (fieldName.equals("currentRecipesPage")) {
                                if (this.m_recipeFilter != null) {
                                    return this.m_recipeFilter.getRecipes();
                                }
                                return null;
                            }
                            else if (fieldName.equals("currentHarvestsPage")) {
                                final int startIndex = this.m_currentPage * 8;
                                final int endIndex = Math.min(this.m_harvests.size(), startIndex + 8);
                                final Collection<AbstractCraftHarvestElement> list = new ArrayList<AbstractCraftHarvestElement>();
                                for (int i = startIndex; i < endIndex; ++i) {
                                    list.add(this.m_harvests.get(i));
                                }
                                return list;
                            }
                        }
                        return super.getFieldValue(fieldName);
                    }
                    if (this.m_currentType != CraftType.HARVEST && this.m_recipeFilter != null) {
                        return this.m_recipeFilter.getPageDescription();
                    }
                    return this.m_currentPage + 1 + "/" + this.getCurrentMaxPages();
                }
            }
        }
    }
    
    private String getResourcePlantedName() {
        final ArrayList<AbstractCraftHarvestElement> harvests = this.getHarvestViews();
        if (harvests.isEmpty()) {
            return null;
        }
        final AbstractCraftHarvestElement elem = harvests.get(0);
        return WakfuTranslator.getInstance().getString(100, elem.getResourceType().getAgtId(), new Object[0]);
    }
    
    private String getResourceHarvestedName() {
        final ArrayList<AbstractCraftHarvestElement> harvests = this.getHarvestViews();
        if (harvests.isEmpty()) {
            return null;
        }
        final AbstractCraftHarvestElement elem = harvests.get(0);
        return WakfuTranslator.getInstance().getString(109, elem.getResourceType().getAgtId(), new Object[0]);
    }
    
    private String getResourceCuttingsTakenName() {
        final ArrayList<AbstractCraftHarvestElement> harvests = this.getHarvestViews();
        if (harvests.isEmpty()) {
            return null;
        }
        final AbstractCraftHarvestElement elem = harvests.get(0);
        return WakfuTranslator.getInstance().getString(108, elem.getResourceType().getAgtId(), new Object[0]);
    }
    
    private boolean isCraft() {
        final CraftHandler craftHandler = WakfuGameEntity.getInstance().getLocalPlayer().getCraftHandler();
        return craftHandler.getPlantationCounter(this.m_refCraftId) <= 0 && craftHandler.getNonDestructiveCollectCounter(this.m_refCraftId) <= 0 && craftHandler.getDestructiveCollectCounter(this.m_refCraftId) <= 0;
    }
    
    private static Color getColor() {
        return WakfuClientConstants.PASSPORT_TEXT_COLOR;
    }
    
    public ArrayList<AbstractCraftHarvestElement> getHarvestViews() {
        if (this.m_harvests != null) {
            return this.m_harvests;
        }
        return this.m_harvests = CraftHarvestElementManager.INSTANCE.getElements(this.m_refCraftId, this.getLevel() + 30);
    }
    
    public ArrayList<RecipeView> getRecipeViews() {
        if (this.m_recipeViews != null) {
            return this.m_recipeViews;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        this.m_recipeViews = new ArrayList<RecipeView>();
        final TIntObjectIterator<CraftRecipe> it = CraftManager.INSTANCE.getCraft(this.m_refCraftId).recipesIterator();
        while (it.hasNext()) {
            it.advance();
            final CraftRecipe craftRecipe = it.value();
            final String recipeName = WakfuTranslator.getInstance().getString(46, craftRecipe.getId(), new Object[0]);
            if (this.getLevel() + 30 < craftRecipe.getRequiredLevel()) {
                continue;
            }
            if (craftRecipe.hasProperty(CraftRecipeProperty.SECRET) && !localPlayer.getCraftHandler().isKnownRecipe(this.m_refCraftId, craftRecipe.getId())) {
                continue;
            }
            if (!craftRecipe.isVisible(localPlayer)) {
                continue;
            }
            final boolean canAfford = localPlayer.canAffordRecipe(craftRecipe);
            if (this.m_isOkFiltered && !canAfford) {
                continue;
            }
            if (this.m_nameFilter.length() != 0 && !recipeName.toUpperCase().contains(this.m_nameFilter.toUpperCase())) {
                continue;
            }
            if (this.m_craftTable != null && !this.m_craftTable.isRecipeAllowed(craftRecipe.getId(), craftRecipe.getType())) {
                continue;
            }
            final RecipeView view = new RecipeView(craftRecipe, this);
            view.setCanAfford(canAfford);
            this.m_recipeViews.add(view);
        }
        this.sortRecipeViews();
        return this.m_recipeViews;
    }
    
    private void sortRecipeViews() {
        if (this.m_abcSorterState.equals(SORTER_STATE.DISABLED) && this.m_levelSorterState.equals(SORTER_STATE.DISABLED)) {
            return;
        }
        Collections.sort(this.m_recipeViews, new Comparator<RecipeView>() {
            private int alphaCompare(final RecipeView o1, final RecipeView o2) {
                String name1;
                String name2;
                if (CraftView.this.m_abcSorterState.equals(SORTER_STATE.ACTIVATED)) {
                    name1 = o2.getName();
                    name2 = o1.getName();
                }
                else {
                    name1 = o1.getName();
                    name2 = o2.getName();
                }
                return StringUtils.alphanumericCompare(name1, name2);
            }
            
            @Override
            public int compare(final RecipeView o1, final RecipeView o2) {
                switch (CraftView.this.m_levelSorterState) {
                    case DISACTIVATED:
                    case ACTIVATED: {
                        if (o1.getRequiredLevel() == o2.getRequiredLevel()) {
                            return this.alphaCompare(o1, o2);
                        }
                        if (CraftView.this.m_levelSorterState.equals(SORTER_STATE.ACTIVATED)) {
                            return o1.getRequiredLevel() - o2.getRequiredLevel();
                        }
                        return o2.getRequiredLevel() - o1.getRequiredLevel();
                    }
                    case DISABLED: {
                        return this.alphaCompare(o1, o2);
                    }
                    default: {
                        return 0;
                    }
                }
            }
        });
    }
    
    public CraftInteractiveElement getCraftTable() {
        return this.m_craftTable;
    }
    
    public void setNameFilter(final String nameFilter) {
        this.m_nameFilter = nameFilter;
        this.refreshRecipesHarvestList();
    }
    
    public String getNameFilter() {
        return this.m_nameFilter;
    }
    
    public void setNextXp(final long nextXp) {
        this.m_nextXp = nextXp;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "nextXpPercentage");
    }
    
    public void toggleOkFilter() {
        this.m_isOkFiltered = !this.m_isOkFiltered;
        this.refreshRecipesHarvestList();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isOkFiltered");
    }
    
    public void toggleAbcSorter() {
        this.m_abcSorterState = this.m_abcSorterState.getNext();
        this.m_levelSorterState = SORTER_STATE.DISABLED;
        this.sortRecipeViews();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isLevelSorted", "isLevelSortActivated", "isAbcSortActivated", "isAbcSorted", "recipes");
    }
    
    public void toggleLevelSorter() {
        this.m_levelSorterState = this.m_levelSorterState.getNext();
        this.m_abcSorterState = SORTER_STATE.DISABLED;
        this.sortRecipeViews();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isLevelSorted", "isLevelSortActivated", "isAbcSortActivated", "isAbcSorted", "recipes");
    }
    
    public void onRecipeDone() {
        if (this.m_recipeViews == null) {
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        boolean changedList = false;
        for (int i = this.m_recipeViews.size() - 1; i >= 0; --i) {
            final RecipeView recipeView = this.m_recipeViews.get(i);
            final boolean canAfford = localPlayer.canAffordRecipe(recipeView.getCraftRecipe());
            if (this.m_isOkFiltered && !canAfford) {
                this.m_recipeViews.remove(i);
                changedList = true;
            }
            recipeView.setCanAfford(canAfford);
        }
        if (changedList) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "recipes");
        }
    }
    
    public void onRecipeLearnt() {
        this.refreshRecipesHarvestList();
    }
    
    public void onCraftLevelChanged() {
        this.refreshRecipesHarvestList();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "level", "levelTextColor", "levelText", "nextXpPercentageInit", "currentXpPercentage");
    }
    
    public void refreshRecipesHarvestList() {
        this.m_recipeViews = null;
        this.m_harvests = null;
        this.getRecipeViews();
        this.getHarvestViews();
        final String pageField = (this.m_currentType == CraftType.HARVEST) ? "currentHarvestsPage" : "currentRecipesPage";
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "recipes", "harvests", "maxNumPages", "currentPage", "currentPageText", "numPerPage", pageField);
    }
    
    @Override
    public boolean isUnknown() {
        return false;
    }
    
    public void setCurrentType(final CraftType currentType) {
        this.m_currentType = currentType;
        this.m_currentPage = 0;
        if (this.m_recipeFilter != null) {
            this.m_recipeFilter.setCurrentPage(this.m_currentPage);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "maxNumPages", "currentPage", "currentPageText", "numPerPage", "currentHarvestsPage", "currentRecipesPage");
    }
    
    public int getCurrentMaxPages() {
        if (this.m_currentType == CraftType.HARVEST) {
            return (int)Math.ceil(this.m_harvests.size() / 8.0f);
        }
        if (this.m_recipeFilter != null) {
            return this.m_recipeFilter.getMaxNumPages();
        }
        return 0;
    }
    
    public void setCurrentPage(int page) {
        final int maxPage = this.getCurrentMaxPages();
        if (page < 0) {
            page = 0;
        }
        if (page >= maxPage) {
            page = maxPage - 1;
        }
        this.m_currentPage = page;
        if (this.m_recipeFilter != null) {
            this.m_recipeFilter.setCurrentPage(page);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentPage", "currentPageText", "numPerPage", "currentHarvestsPage", "currentRecipesPage");
    }
    
    public int getCurrentPage() {
        return (this.m_recipeFilter == null) ? 0 : this.m_recipeFilter.getCurrentPage();
    }
    
    static {
        FIELDS = new String[] { "level", "levelText", "currentXpPercentage", "nextXpPercentage", "recipes", "passportIconUrl", "isOkFiltered", "isAbcSorted", "isLevelSorted", "isAbcSortActivated", "isLevelSortActivated" };
    }
    
    private enum SORTER_STATE
    {
        ACTIVATED, 
        DISACTIVATED, 
        DISABLED;
        
        public SORTER_STATE getNext() {
            final SORTER_STATE[] sorter_states = values();
            if (this.equals(sorter_states[sorter_states.length - 1])) {
                return sorter_states[0];
            }
            for (int i = 0; i < sorter_states.length; ++i) {
                if (sorter_states[i].equals(this)) {
                    return sorter_states[i + 1];
                }
            }
            return null;
        }
    }
}
