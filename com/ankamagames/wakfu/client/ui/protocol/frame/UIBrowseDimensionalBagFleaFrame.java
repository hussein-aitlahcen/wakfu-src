package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.xulor2.util.*;

public class UIBrowseDimensionalBagFleaFrame extends UIAbstractBrowseFleaFrame implements MessageFrame
{
    private static UIBrowseDimensionalBagFleaFrame m_instance;
    protected static final Logger m_logger;
    private TLongObjectHashMap<ObjectPair<Byte, AbstractReferenceItem>> m_fleaRefItems;
    private FleaContentLoadedListener m_fleaListContentLoadedListener;
    private FleaContentLoadedListener m_itemListContentLoadedListener;
    private List m_fleaList;
    private List m_itemList;
    private DialogUnloadListener m_dimBagdialogUnloadListener;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIBrowseDimensionalBagFleaFrame getInstance() {
        return UIBrowseDimensionalBagFleaFrame.m_instance;
    }
    
    private UIBrowseDimensionalBagFleaFrame() {
        super();
        this.m_fleaRefItems = new TLongObjectHashMap<ObjectPair<Byte, AbstractReferenceItem>>();
    }
    
    @Override
    public boolean onMessage(final Message message) {
        return true;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("dimensionalBagFleaListDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIBrowseDimensionalBagFleaFrame.getInstance());
                        WakfuGameEntity.getInstance().getLocalPlayer().cancelCurrentOccupation(false, true);
                    }
                    else if (id.equals("dimensionalBagFleaDialog")) {
                        UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag().clearCurrentMerchantInventoryView();
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            this.init();
            final Widget w = (Widget)Xulor.getInstance().load("dimensionalBagFleaListDialog", Dialogs.getDialogPath("dimensionalBagFleaListDialog"), 32769L, (short)10000);
            this.m_fleaList = (List)w.getElementMap().getElement("fleaList");
            final Window window = (Window)w.getElementMap().getElement("fleaWindow");
            this.m_fleaListContentLoadedListener = new FleaContentLoadedListener(this.m_fleaList, window);
            this.m_fleaList.addListContentListener(this.m_fleaListContentLoadedListener);
            WakfuGameEntity.getInstance().getNetworkEntity().pushFrame(NetDimensionalBagFleaFrame.getInstance());
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().removeDialogUnloadListener(this.m_dimBagdialogUnloadListener);
            this.m_dimBagdialogUnloadListener = null;
            this.m_fleaList.removeListContentLoadListener(this.m_fleaListContentLoadedListener);
            this.m_fleaList = null;
            this.m_fleaListContentLoadedListener = null;
            if (this.m_itemList != null) {
                this.m_itemList.removeListContentLoadListener(this.m_itemListContentLoadedListener);
                this.m_itemList = null;
                this.m_itemListContentLoadedListener = null;
            }
            this.clean();
            Xulor.getInstance().unload("dimensionalBagFleaListDialog");
            Xulor.getInstance().unload("dimensionalBagFleaDialog");
            Xulor.getInstance().unload("confirmFleaPurchaseDialog");
            WakfuGameEntity.getInstance().getNetworkEntity().removeFrame(NetDimensionalBagFleaFrame.getInstance());
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void removeFleaRefItem(final long uid) {
        this.m_fleaRefItems.remove(uid);
    }
    
    public void addFleaRefItem(final long uid, final ObjectPair referenceItem) {
        this.m_fleaRefItems.put(uid, referenceItem);
    }
    
    public ObjectPair getFleaRefItem(final long uid) {
        return this.m_fleaRefItems.get(uid);
    }
    
    public void unloadFleaDialog() {
        Xulor.getInstance().unload("dimensionalBagFleaDialog");
    }
    
    public void loadFleaDialog(final MerchantInventory merchantInventory) {
        if (Xulor.getInstance().isLoaded("dimensionalBagFleaDialog")) {
            return;
        }
        final Window window = (Window)Xulor.getInstance().getEnvironment().getElementMap("dimensionalBagFleaListDialog").getElement("fleaWindow");
        Window w;
        if (window.getX() + window.getWidth() * 2 > Xulor.getInstance().getScene().getFrustumWidth()) {
            w = (Window)Xulor.getInstance().loadAsMultiple("dimensionalBagFleaDialog", Dialogs.getDialogPath("dimensionalBagFleaDialog"), "dimensionalBagFleaListDialog", "dimensionalBagFleaListDialog", FleaDialogActions.FLEA_CONTROL_GROUP_ID, 1L, (short)10000);
        }
        else {
            w = (Window)Xulor.getInstance().load("dimensionalBagFleaDialog", Dialogs.getDialogPath("dimensionalBagFleaDialog"), 17L, (short)10000);
        }
        if (w != null) {
            this.m_itemList = (List)w.getElementMap().getElement("itemsList");
            this.m_itemListContentLoadedListener = new FleaContentLoadedListener(this.m_itemList, window);
            this.m_itemList.addListContentListener(this.m_itemListContentLoadedListener);
        }
    }
    
    @Override
    public void addDestructionListener(final Object source, final AbstractOccupation occupation) {
        UIBrowseDimensionalBagFleaFrame.m_destructionListener = new InteractiveElementDestructionListener() {
            @Override
            public void onInteractiveElementDestruction(final ClientInteractiveAnimatedElementSceneView element) {
                if (element.getInteractiveElement().equals(source)) {
                    if (WakfuGameEntity.getInstance().hasFrame(UIBrowseDimensionalBagFleaFrame.getInstance())) {
                        WakfuGameEntity.getInstance().removeFrame(UIBrowseDimensionalBagFleaFrame.getInstance());
                    }
                    occupation.cancel(false, true);
                    AnimatedElementSceneViewManager.getInstance().removeDestructionListener(this);
                }
            }
        };
        AnimatedElementSceneViewManager.getInstance().addDestructionListener(UIBrowseDimensionalBagFleaFrame.m_destructionListener);
    }
    
    public void reflowFleaListOffset() {
        if (this.m_fleaList == null) {
            return;
        }
        this.m_fleaListContentLoadedListener.setListOffset(this.m_fleaList.getOffset());
    }
    
    static {
        UIBrowseDimensionalBagFleaFrame.m_instance = new UIBrowseDimensionalBagFleaFrame();
        m_logger = Logger.getLogger((Class)UIBrowseDimensionalBagFleaFrame.class);
    }
    
    private static class FleaContentLoadedListener implements EditableRenderableCollection.CollectionContentLoadedListener
    {
        private List m_list;
        private Window m_window;
        private float m_listOffset;
        
        private FleaContentLoadedListener(final List list, final Window window) {
            super();
            this.m_list = list;
            this.m_window = window;
        }
        
        @Override
        public void onContentLoaded() {
            XulorUtil.setWidgetInScreen(this.m_window);
            if (this.m_listOffset == -1.0f) {
                this.m_listOffset = this.m_list.getOffset();
            }
            else {
                this.m_list.setOffset(this.m_listOffset);
            }
        }
        
        public void setListOffset(final float listOffset) {
            this.m_listOffset = listOffset;
        }
    }
}
