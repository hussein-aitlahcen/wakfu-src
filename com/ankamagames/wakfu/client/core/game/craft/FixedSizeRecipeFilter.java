package com.ankamagames.wakfu.client.core.game.craft;

import java.util.*;

public class FixedSizeRecipeFilter extends RecipeFilter
{
    private final int m_numRecipesPerPage;
    
    public FixedSizeRecipeFilter(final CraftView view, final int numRecipesPerPage) {
        super(view);
        this.m_numRecipesPerPage = numRecipesPerPage;
    }
    
    @Override
    public String getPageDescription() {
        return this.m_currentPage + 1 + "/" + this.getMaxNumPages();
    }
    
    @Override
    public int getMaxNumPages() {
        final ArrayList<RecipeView> recipeViews = this.m_view.getRecipeViews();
        return (int)Math.ceil(recipeViews.size() / this.m_numRecipesPerPage);
    }
    
    @Override
    public int getNumPerPage() {
        return this.m_numRecipesPerPage;
    }
    
    @Override
    public Collection<RecipeView> getRecipes() {
        final ArrayList<RecipeView> views = this.m_view.getRecipeViews();
        final int startIndex = this.m_currentPage * this.m_numRecipesPerPage;
        final int endIndex = Math.min(views.size(), startIndex + this.m_numRecipesPerPage);
        final Collection<RecipeView> list = new ArrayList<RecipeView>();
        for (int i = startIndex; i < endIndex; ++i) {
            list.add(views.get(i));
        }
        return list;
    }
}
