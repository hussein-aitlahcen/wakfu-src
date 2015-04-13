package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.core.game.exchangeMachine.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.exchangeMachine.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.exchangeMachine.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;
import java.util.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.util.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.pathfinder.*;

public class UIExchangeMachineFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static final UIExchangeMachineFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    private ElementMap m_dialogElementMap;
    private ExchangeMachineView m_exchangeMachineView;
    private boolean m_blockWorldInteractions;
    
    public static UIExchangeMachineFrame getInstance() {
        return UIExchangeMachineFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            if (this.m_blockWorldInteractions) {
                WakfuGameEntity.getInstance().removeFrame(UIWorldInteractionFrame.getInstance());
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("exchangeMachineDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIExchangeMachineFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            this.m_dialogElementMap = Xulor.getInstance().load("exchangeMachineDialog", Dialogs.getDialogPath("exchangeMachineDialog"), 1L, (short)10000).getElementMap();
            WakfuSoundManager.getInstance().playGUISound(600012L);
            Xulor.getInstance().putActionClass("wakfu.exchangeMachine", ExchangeMachineDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_dialogElementMap = null;
            this.m_exchangeMachineView.removeInventoryListeners();
            this.m_exchangeMachineView = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("exchangeMachineDialog");
            PropertiesProvider.getInstance().removeProperty("exchangeMachine");
            PropertiesProvider.getInstance().removeProperty("selectedExchange");
            WakfuSoundManager.getInstance().playGUISound(600013L);
            Xulor.getInstance().removeActionClass("wakfu.exchangeMachine");
            if (this.m_blockWorldInteractions) {
                WakfuGameEntity.getInstance().pushFrame(UIWorldInteractionFrame.getInstance());
            }
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19992: {
                return !this.m_blockWorldInteractions;
            }
            case 19341: {
                final UIExchangeMachineMessage uiExchangeMachineMessage = (UIExchangeMachineMessage)message;
                final long machineId = this.m_exchangeMachineView.getMachineId();
                final int exchangeId = uiExchangeMachineMessage.getExchangeView().getExchange().getId();
                final ExchangeMachineActivationMessage exchangeMachineActivationMessage = new ExchangeMachineActivationMessage(machineId, exchangeId);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(exchangeMachineActivationMessage);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void setExchangeMachineParameters(final IEExchangeParameter param, final String name) {
        this.setExchangeMachineParameters(0L, name, param, false, false);
    }
    
    public void setExchangeMachine(final ExchangeMachine machine) {
        this.setExchangeMachineParameters(machine.getId(), machine.getName(), machine.getParam(), true, true);
    }
    
    private void setExchangeMachineParameters(final long id, final String name, final IEExchangeParameter param, final boolean usable, final boolean blockWorldInteraction) {
        (this.m_exchangeMachineView = new ExchangeMachineView(id, name, usable, param)).addInventoryListeners();
        this.m_blockWorldInteractions = blockWorldInteraction;
        PropertiesProvider.getInstance().setPropertyValue("exchangeMachine", this.m_exchangeMachineView);
        WakfuGameEntity.getInstance().pushFrame(this);
    }
    
    private void highLightLackingIngredients(final RecipeView recipeView) {
        final StackList list = (StackList)this.m_dialogElementMap.getElement("recipeList");
        final RenderableContainer renderable = list.getRenderables().get(list.getItemIndex(recipeView));
        final List ingredients = (List)renderable.getInnerElementMap().getElement("ingredientList");
        for (final Object item : ingredients.getItems()) {
            final IngredientView ingredientView = (IngredientView)item;
            if (!ingredientView.isPossessed()) {
                final Image image = (Image)ingredients.getRenderableByOffset(ingredients.getItemIndex(ingredientView)).getInnerElementMap().getElement("icon");
                final Color c = new Color(1.0f, 0.5f, 0.5f, 1.0f);
                final Color c2 = image.getModulationColor();
                final AbstractTween t = new ModulationColorTween(c, c2, image, 0, 250, 3, TweenFunction.PROGRESSIVE);
                image.addTween(t);
            }
        }
    }
    
    public void onDespawn(final CraftInteractiveElement elem) {
        if (elem.getId() == this.m_exchangeMachineView.getMachineId()) {
            WakfuGameEntity.getInstance().removeFrame(this);
        }
    }
    
    public void pathStarted(final PathMobile mobile, final PathFindResult path) {
        WakfuGameEntity.getInstance().removeFrame(UICraftTableFrame.getInstance());
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIExchangeMachineFrame.class);
        m_instance = new UIExchangeMachineFrame();
    }
}
