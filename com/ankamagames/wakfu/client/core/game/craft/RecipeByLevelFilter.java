package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.wakfu.client.core.*;
import java.util.*;

public class RecipeByLevelFilter extends RecipeFilter
{
    private final int m_levelRange;
    
    public RecipeByLevelFilter(final CraftView view, final int levelRange) {
        super(view);
        this.m_levelRange = levelRange;
    }
    
    @Override
    public String getPageDescription() {
        final int minLevel = this.m_currentPage * this.m_levelRange;
        final int maxLevel = minLevel + this.m_levelRange - 1;
        final String levelDesc = minLevel + "-" + maxLevel;
        return WakfuTranslator.getInstance().getString("levelShort.custom", levelDesc);
    }
    
    @Override
    public int getMaxNumPages() {
        int maxLevel = Integer.MIN_VALUE;
        final ArrayList<RecipeView> views = this.m_view.getRecipeViews();
        if (views.isEmpty()) {
            return 0;
        }
        for (int i = 0, size = views.size(); i < size; ++i) {
            final RecipeView recipeView = views.get(i);
            if (recipeView.getRequiredLevel() > maxLevel) {
                maxLevel = recipeView.getRequiredLevel();
            }
        }
        return (int)Math.ceil(maxLevel / this.m_levelRange) + 1;
    }
    
    @Override
    public int getNumPerPage() {
        return this.getRecipes().size();
    }
    
    @Override
    public Collection<RecipeView> getRecipes() {
        return this.createRecipes();
    }
    
    private Collection<RecipeView> createRecipes() {
        final ArrayList<RecipeView> views = this.m_view.getRecipeViews();
        final int minLevel = this.m_currentPage * this.m_levelRange;
        final int maxLevel = minLevel + this.m_levelRange - 1;
        final Collection<RecipeView> list = new ArrayList<RecipeView>();
        for (int i = 0, size = views.size(); i < size; ++i) {
            final RecipeView recipeView = views.get(i);
            final short level = recipeView.getRequiredLevel();
            if (level >= minLevel && level <= maxLevel) {
                list.add(recipeView);
            }
        }
        return list;
    }
}
