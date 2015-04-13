package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;

@XulorActionsTag
public class CraftDialogsActions
{
    public static final String PACKAGE = "wakfu.crafts";
    
    public static void openLinkedBook(final Event event) {
        ((CraftView)PropertiesProvider.getInstance().getObjectProperty("craft", event.getTarget().getElementMap().getId())).displayBook();
    }
    
    public static void checkOkFilter(final Event event) {
        ((CraftView)PropertiesProvider.getInstance().getObjectProperty("craft", event.getTarget().getElementMap().getId())).toggleOkFilter();
    }
    
    public static void checkAlphabeticalSorter(final Event event) {
        ((CraftView)PropertiesProvider.getInstance().getObjectProperty("craft", event.getTarget().getElementMap().getId())).toggleAbcSorter();
    }
    
    public static void checkLevelSorter(final Event event) {
        ((CraftView)PropertiesProvider.getInstance().getObjectProperty("craft", event.getTarget().getElementMap().getId())).toggleLevelSorter();
    }
    
    public static void validNameFilter(final Event event, final TextEditor textEditor) {
        final CraftView craftView = (CraftView)PropertiesProvider.getInstance().getObjectProperty("craft", event.getTarget().getElementMap().getId());
        final String text = textEditor.getText();
        if (!text.equals(craftView.getNameFilter())) {
            craftView.setNameFilter(text);
        }
    }
    
    public static void previousPage(final Event e, final CraftView view) {
        final int newPage = view.getCurrentPage() - 1;
        if (newPage < 0) {
            return;
        }
        view.setCurrentPage(newPage);
    }
    
    public static void nextPage(final Event e, final CraftView view) {
        final int newPage = view.getCurrentPage() + 1;
        if (newPage >= view.getCurrentMaxPages()) {
            return;
        }
        view.setCurrentPage(newPage);
    }
    
    public static void displayRecipesWithIngredient(final Event e, final AbstractCraftHarvestElement elem) {
        UICraftFrame.getInstance().createLinkedRecipes(elem.getItemId());
    }
    
    public static void displayRecipesWithIngredient(final Event e, final RecipeView recipe) {
        UICraftFrame.getInstance().createLinkedRecipes(recipe.getEffectiveItem().getId());
    }
    
    public static void onCraftTypeChanged(final SelectionChangedEvent e, final CraftView view) {
        final byte id = (byte)(e.isSelected() ? 0 : 1);
        view.setCurrentType(CraftType.getFromId(id));
        PropertiesProvider.getInstance().setPropertyValue("craftDisplayType", id);
    }
    
    public static void openProducedItemDescription(final ItemEvent itemEvent, final Window window) {
        if (itemEvent.getItemValue() instanceof RecipeView) {
            openProducedItemDescription(itemEvent, (RecipeView)itemEvent.getItemValue(), window);
        }
        else if (itemEvent.getItemValue() instanceof AbstractCraftHarvestElement) {
            openProducedItemDescription(itemEvent, (AbstractCraftHarvestElement)itemEvent.getItemValue(), window);
        }
    }
    
    public static void openProducedItemDescription(final MouseEvent e, final AbstractCraftHarvestElement element, final Window window) {
        if (e.getButton() != 3) {
            return;
        }
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(element.getItemId());
        if (referenceItem == null) {
            return;
        }
        final Item item = new Item(referenceItem.getId());
        item.initializeWithReferenceItem(referenceItem);
        final UIItemDetailMessage msg = new UIItemDetailMessage();
        msg.setId(16415);
        msg.setParentWindowId((window == null) ? null : window.getElementMap().getId());
        msg.setItem(item);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void openProducedItemDescription(final MouseEvent e, final RecipeView recipeView, final Window window) {
        if (e.getButton() != 3) {
            return;
        }
        final UICraftItemDetailMessage msg = new UICraftItemDetailMessage();
        msg.setParentWindowId((window == null) ? null : window.getElementMap().getId());
        msg.setItem(recipeView);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void outRecipe(final Event event, final Image image) {
        outRecipe(event, image, null);
    }
    
    public static void overRecipe(final Event event, final Image image) {
        overRecipe(event, image, null);
    }
    
    public static void outRecipe(final Event event, final Image image, final Widget w) {
        image.setDisplaySize(new Dimension(46, 46));
        if (w != null) {
            w.setVisible(false);
        }
    }
    
    public static void overRecipe(final Event event, final Image image, final Widget w) {
        image.setDisplaySize(new Dimension(50, 50));
        if (w != null) {
            w.setVisible(true);
        }
    }
    
    public static void switchToFreeMode(final SelectionChangedEvent e) {
        final UIMessage msg = new UIMessage();
        msg.setId(16851);
        msg.setBooleanValue(e.isSelected());
        Worker.getInstance().pushMessage(msg);
    }
}
