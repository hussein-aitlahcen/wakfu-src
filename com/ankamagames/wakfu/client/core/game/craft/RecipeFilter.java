package com.ankamagames.wakfu.client.core.game.craft;

import java.util.*;

public abstract class RecipeFilter
{
    protected final CraftView m_view;
    protected int m_currentPage;
    
    protected RecipeFilter(final CraftView view) {
        super();
        this.m_view = view;
    }
    
    public void setCurrentPage(final int currentPage) {
        this.m_currentPage = currentPage;
    }
    
    public int getCurrentPage() {
        return this.m_currentPage;
    }
    
    public abstract String getPageDescription();
    
    public abstract int getMaxNumPages();
    
    public abstract int getNumPerPage();
    
    public abstract Collection<RecipeView> getRecipes();
}
