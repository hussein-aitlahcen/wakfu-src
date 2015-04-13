package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.gems.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.appearance.*;

class GemItemsDragNDropListener implements ItemDragNDropListener
{
    private static final String GEM_LIST_ID = "gemsList";
    private static final String SLOT_IMAGE_ID = "slotImage";
    private static final String GEM_IMAGE_ID = "gemImage";
    private static final int HIGHLIGHT_CYCLE_DURATION = 500;
    
    @Override
    public void onDrag(final Item item) {
        if (item == null) {
            return;
        }
        highlightGemSlots(item.getReferenceItem().getGemType());
    }
    
    @Override
    public void onDrop() {
        unhighlightGemSlots();
    }
    
    private static void unhighlightGemSlots() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("gemItemDialog");
        if (map == null) {
            return;
        }
        final List list = (List)map.getElement("gemsList");
        final ArrayList<Object> items = list.getItems();
        for (int i = 0, size = items.size(); i < size; ++i) {
            final Widget image = (Widget)list.getRenderableByOffset(i).getInnerElementMap().getElement("slotImage");
            final Widget gemImage = (Widget)list.getRenderableByOffset(i).getInnerElementMap().getElement("gemImage");
            highlightWidget(image, gemImage, false);
        }
    }
    
    private static void highlightGemSlots(final GemType type) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("gemItemDialog");
        if (map == null) {
            return;
        }
        final List list = (List)map.getElement("gemsList");
        final ArrayList<Object> items = list.getItems();
        for (int i = 0, size = items.size(); i < size; ++i) {
            final GemSlotDisplayer gemSlotDisplayer = items.get(i);
            final Widget image = (Widget)list.getRenderableByOffset(i).getInnerElementMap().getElement("slotImage");
            final Widget gemImage = (Widget)list.getRenderableByOffset(i).getInnerElementMap().getElement("gemImage");
            highlightWidget(image, gemImage, true);
        }
    }
    
    private static void highlightWidget(final Widget w, final Widget w2, final boolean add) {
        final DecoratorAppearance appearance = w.getAppearance();
        appearance.removeTweensOfType(ModulationColorTween.class);
        if (add) {
            final AbstractTween tween = new ModulationColorTween(Color.WHITE, Color.GRAY, appearance, 0, 500, -1, true, TweenFunction.PROGRESSIVE);
            appearance.addTween(tween);
        }
        final DecoratorAppearance appearance2 = w2.getAppearance();
        appearance2.removeTweensOfType(ModulationColorTween.class);
        if (add) {
            final AbstractTween tween2 = new ModulationColorTween(Color.WHITE, Color.WHITE_ALPHA, appearance, 0, 500, -1, true, TweenFunction.PROGRESSIVE);
            appearance2.addTween(tween2);
        }
    }
}
