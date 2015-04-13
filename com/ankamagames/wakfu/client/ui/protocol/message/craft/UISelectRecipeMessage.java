package com.ankamagames.wakfu.client.ui.protocol.message.craft;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.craft.*;

public class UISelectRecipeMessage extends UIMessage
{
    private final RecipeView m_recipeView;
    
    public UISelectRecipeMessage(final RecipeView recipeView) {
        super();
        this.m_recipeView = recipeView;
    }
    
    @Override
    public int getId() {
        return 16847;
    }
    
    public RecipeView getRecipeView() {
        return this.m_recipeView;
    }
}
