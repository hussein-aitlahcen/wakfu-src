package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;

public abstract class UIAbstractBrowseFleaFrame
{
    private static DimensionalBagView m_browsingDimentionalBag;
    protected static InteractiveElementDestructionListener m_destructionListener;
    
    public static DimensionalBagView getBrowsingDimentionalBag() {
        return UIAbstractBrowseFleaFrame.m_browsingDimentionalBag;
    }
    
    public static void setBrowsingDimentionalBag(final DimensionalBagView browsingDimentionalBag) {
        UIAbstractBrowseFleaFrame.m_browsingDimentionalBag = browsingDimentionalBag;
    }
    
    public void init() {
        if (UIAbstractBrowseFleaFrame.m_browsingDimentionalBag == null) {
            return;
        }
        UIAbstractBrowseFleaFrame.m_browsingDimentionalBag.getDimensionalBagFlea().clear();
        PropertiesProvider.getInstance().setPropertyValue("flea", UIAbstractBrowseFleaFrame.m_browsingDimentionalBag);
        PropertiesProvider.getInstance().setPropertyValue("fleaSelectedGood", null);
        PropertiesProvider.getInstance().setPropertyValue("fleaTitle", WakfuTranslator.getInstance().getString("flea.of", UIAbstractBrowseFleaFrame.m_browsingDimentionalBag.getOwnerName()));
        Xulor.getInstance().putActionClass("wakfu.flea", FleaDialogActions.class);
    }
    
    public void clean() {
        if (UIAbstractBrowseFleaFrame.m_browsingDimentionalBag == null) {
            return;
        }
        UIAbstractBrowseFleaFrame.m_browsingDimentionalBag.setCurrentMerchantInventory(null);
        PropertiesProvider.getInstance().removeProperty("flea");
        PropertiesProvider.getInstance().removeProperty("fleaSelectedGood");
        PropertiesProvider.getInstance().removeProperty("currentItemQuantity");
        PropertiesProvider.getInstance().removeProperty("currentItemTotalPrice");
        PropertiesProvider.getInstance().removeProperty("currentItemFormatedTotalPrice");
        PropertiesProvider.getInstance().removeProperty("fleaTitle");
        if (Xulor.getInstance().isLoaded("confirmFleaPurchaseDialog")) {
            Xulor.getInstance().unload("confirmFleaPurchaseDialog");
        }
        Xulor.getInstance().removeActionClass("wakfu.flea");
        AnimatedElementSceneViewManager.getInstance().removeDestructionListener(UIAbstractBrowseFleaFrame.m_destructionListener);
    }
    
    public abstract void addDestructionListener(final Object p0, final AbstractOccupation p1);
}
