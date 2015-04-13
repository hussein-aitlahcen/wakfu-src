package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;

public class UIBrowseFleaFrame extends UIAbstractBrowseFleaFrame implements MessageFrame
{
    private static UIBrowseFleaFrame m_instance;
    protected static final Logger m_logger;
    private EditableRenderableCollection.CollectionContentLoadedListener m_collectionContentLoadedListener;
    private List m_list;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIBrowseFleaFrame getInstance() {
        return UIBrowseFleaFrame.m_instance;
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
                    if (id.equals("dimensionalBagFleaDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIBrowseFleaFrame.getInstance());
                        WakfuGameEntity.getInstance().getLocalPlayer().cancelCurrentOccupation(false, true);
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            this.init();
            final Widget w = (Widget)Xulor.getInstance().load("dimensionalBagFleaDialog", Dialogs.getDialogPath("dimensionalBagFleaDialog"), 32769L, (short)10000);
            this.m_list = (List)w.getElementMap().getElement("itemsList");
            final Window window = (Window)w.getElementMap().getElement("fleaWindow");
            this.m_collectionContentLoadedListener = new EditableRenderableCollection.CollectionContentLoadedListener() {
                @Override
                public void onContentLoaded() {
                    XulorUtil.setWidgetInScreen(window);
                }
            };
            this.m_list.addListContentListener(this.m_collectionContentLoadedListener);
            WakfuGameEntity.getInstance().removeFrame(UIWorldInteractionFrame.getInstance());
            WakfuGameEntity.getInstance().getNetworkEntity().pushFrame(NetFleaFrame.getInstance());
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            this.m_list.removeListContentLoadListener(this.m_collectionContentLoadedListener);
            this.m_list = null;
            this.m_collectionContentLoadedListener = null;
            this.clean();
            Xulor.getInstance().unload("dimensionalBagFleaDialog");
            Xulor.getInstance().unload("confirmFleaPurchaseDialog");
            WakfuGameEntity.getInstance().pushFrame(UIWorldInteractionFrame.getInstance());
            WakfuGameEntity.getInstance().getNetworkEntity().removeFrame(NetFleaFrame.getInstance());
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void addDestructionListener(final Object source, final AbstractOccupation occupation) {
        UIBrowseFleaFrame.m_destructionListener = new InteractiveElementDestructionListener() {
            @Override
            public void onInteractiveElementDestruction(final ClientInteractiveAnimatedElementSceneView element) {
                if (element.getInteractiveElement().equals(source)) {
                    if (WakfuGameEntity.getInstance().hasFrame(UIBrowseFleaFrame.getInstance())) {
                        WakfuGameEntity.getInstance().removeFrame(UIBrowseFleaFrame.getInstance());
                    }
                    occupation.cancel(false, false);
                    AnimatedElementSceneViewManager.getInstance().removeDestructionListener(this);
                }
            }
        };
        AnimatedElementSceneViewManager.getInstance().addDestructionListener(UIBrowseFleaFrame.m_destructionListener);
    }
    
    static {
        UIBrowseFleaFrame.m_instance = new UIBrowseFleaFrame();
        m_logger = Logger.getLogger((Class)UIBrowseFleaFrame.class);
    }
}
