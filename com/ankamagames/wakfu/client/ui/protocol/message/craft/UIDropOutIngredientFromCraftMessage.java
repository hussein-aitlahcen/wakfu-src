package com.ankamagames.wakfu.client.ui.protocol.message.craft;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.craft.*;

public class UIDropOutIngredientFromCraftMessage extends UIMessage
{
    private final IngredientView m_ingredientView;
    
    public UIDropOutIngredientFromCraftMessage(final IngredientView ingredientView) {
        super();
        this.m_ingredientView = ingredientView;
    }
    
    @Override
    public int getId() {
        return 16845;
    }
    
    public IngredientView getIngredientView() {
        return this.m_ingredientView;
    }
}
