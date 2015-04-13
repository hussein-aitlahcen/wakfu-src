package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.renderer.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.craft.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import gnu.trove.*;

public class UIRecycleFrame implements MessageFrame, InventoryObserver, BagContainerListener
{
    protected static final Logger m_logger;
    private static final UIRecycleFrame m_instance;
    private static final byte MAX_RECYCLABLE_ITEMS_AT_ONCE = 8;
    private static final String PROGRESS_BAR = "progressBar";
    private static final String BUTTON = "startButton";
    private DialogUnloadListener m_dialogUnloadListener;
    WakfuClientMapInteractiveElement m_interactiveElement;
    private MobileStartPathListener m_listener;
    private ElementMap m_dialogElementMap;
    TLongObjectHashMap<IngredientView> m_itemsToRecycle;
    
    private UIRecycleFrame() {
        super();
        this.m_interactiveElement = null;
        this.m_itemsToRecycle = new TLongObjectHashMap<IngredientView>();
    }
    
    public void reinitialize() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        localPlayer.getEquipmentInventory().addObserver(this);
        final TLongObjectIterator<AbstractBag> it = localPlayer.getBags().getBagsIterator();
        while (it.hasNext()) {
            it.advance();
            it.value().addObserver(this);
        }
        this.cleanItems();
    }
    
    public void clean() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        localPlayer.getEquipmentInventory().removeObserver(this);
        final TLongObjectIterator<AbstractBag> it = localPlayer.getBags().getBagsIterator();
        while (it.hasNext()) {
            it.advance();
            it.value().removeObserver(this);
        }
        this.cleanItems();
    }
    
    private void cleanItems() {
        PropertiesProvider.getInstance().removeProperty("recycleStack");
        this.m_itemsToRecycle.clear();
    }
    
    public void highLightIngredientSlots(final boolean highlight) {
        final EditableRenderableCollection list = (EditableRenderableCollection)this.m_dialogElementMap.getElement("ingredientList");
        for (final RenderableContainer renderableContainer : list.getRenderables()) {
            final Widget container = (Widget)renderableContainer.getInnerElementMap().getElement("ingredientBackgroundContainer");
            container.setStyle(highlight ? "itemSelectedBackground" : "itemBackground");
        }
    }
    
    public boolean containsItem(final long uid) {
        return this.m_itemsToRecycle.containsKey(uid);
    }
    
    private long getUID(final IngredientView ingredient) {
        final TLongObjectIterator<IngredientView> it = this.m_itemsToRecycle.iterator();
        while (it.hasNext()) {
            it.advance();
            if (it.value().getReferenceItem().getId() == ingredient.getReferenceItem().getId()) {
                return it.key();
            }
        }
        return -1L;
    }
    
    void resetRecycledItemsView() {
        PropertiesProvider.getInstance().setPropertyValue("recycleStackSize", this.m_itemsToRecycle.size());
        PropertiesProvider.getInstance().setPropertyValue("recycleStack", this.m_itemsToRecycle.getValues());
    }
    
    @Override
    public void bagAdded(final AbstractBag bag) {
        bag.addObserver(this);
    }
    
    @Override
    public void bagRemoved(final AbstractBag bag) {
        bag.removeObserver(this);
    }
    
    @Override
    public void onInventoryEvent(final InventoryEvent event) {
        switch (event.getAction()) {
            case ITEM_REMOVED:
            case ITEM_REMOVED_AT: {
                final InventoryItemModifiedEvent e = (InventoryItemModifiedEvent)event;
                final InventoryContent item = e.getConcernedItem();
                this.m_itemsToRecycle.remove(item.getUniqueId());
                this.resetRecycledItemsView();
                break;
            }
            case ITEM_QUANTITY_MODIFIED: {
                final InventoryItemModifiedEvent e = (InventoryItemModifiedEvent)event;
                final Item item2 = (Item)e.getConcernedItem();
                final IngredientView view = this.m_itemsToRecycle.get(item2.getUniqueId());
                if (view != null && item2.getQuantity() < view.getQuantity()) {
                    this.m_itemsToRecycle.put(item2.getUniqueId(), new IngredientView(item2.getQuantity(), (ReferenceItem)item2.getReferenceItem()));
                    this.resetRecycledItemsView();
                    break;
                }
                break;
            }
            case CLEARED: {
                final TLongObjectIterator<IngredientView> it = this.m_itemsToRecycle.iterator();
                while (it.hasNext()) {
                    it.advance();
                    if (!possessesItem(it.key())) {
                        it.remove();
                    }
                }
                this.resetRecycledItemsView();
                break;
            }
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (UIFrameMouseKey.isKeyOrMouseMessage(message)) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (message.getId()) {
            case 16846: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final long itemUID = msg.getLongValue();
                if (this.m_itemsToRecycle.contains(itemUID)) {
                    this.m_itemsToRecycle.remove(itemUID);
                    this.resetRecycledItemsView();
                }
                this.highLightIngredientSlots(true);
                return false;
            }
            case 16845: {
                if (PropertiesProvider.getInstance().getBooleanProperty("recycleRunning")) {
                    return false;
                }
                final UIDropOutIngredientFromCraftMessage msg2 = (UIDropOutIngredientFromCraftMessage)message;
                final IngredientView ingredientView = msg2.getIngredientView();
                final long uid = this.getUID(ingredientView);
                final short quantity = msg2.getShortValue();
                final Item item = localPlayer.getBags().getItemFromInventories(uid);
                if (quantity == -1 || ingredientView.getQuantity() == 1) {
                    this.m_itemsToRecycle.remove(uid);
                }
                else {
                    this.m_itemsToRecycle.put(uid, new IngredientView((short)(ingredientView.getQuantity() - 1), ingredientView.getReferenceItem()));
                }
                PropertiesProvider.getInstance().firePropertyValueChanged(item, "usedInCurrentRecipe");
                this.resetRecycledItemsView();
                return false;
            }
            case 16844: {
                final UIItemMessage msg3 = (UIItemMessage)message;
                final Item item2 = msg3.getItem();
                if (!item2.getType().isRecyclable()) {
                    return false;
                }
                if (item2.getReferenceItem().hasItemProperty(ItemProperty.NOT_RECYCLABLE)) {
                    return false;
                }
                if (this.m_itemsToRecycle.size() >= 8) {
                    return false;
                }
                if (PropertiesProvider.getInstance().getBooleanProperty("recycleRunning")) {
                    return false;
                }
                final long itemId = item2.getUniqueId();
                if (!possessesItem(itemId)) {
                    return false;
                }
                final IngredientView ingredientView2 = this.m_itemsToRecycle.get(itemId);
                short quantity2 = msg3.getQuantity();
                if (ingredientView2 != null) {
                    if (quantity2 != 1) {
                        UIRecycleFrame.m_logger.error((Object)"on a encore un item de m\u00eame type dans la liste des ingr\u00e9dients !");
                        return false;
                    }
                    final int quantityToAdd = ingredientView2.getQuantity() + 1;
                    if (item2.getQuantity() < quantityToAdd) {
                        return false;
                    }
                    quantity2 = (short)quantityToAdd;
                }
                this.m_itemsToRecycle.put(itemId, new IngredientView((quantity2 == -1) ? item2.getQuantity() : quantity2, (ReferenceItem)item2.getReferenceItem()));
                PropertiesProvider.getInstance().firePropertyValueChanged(item2, "usedInCurrentRecipe");
                this.highLightIngredientSlots(false);
                this.resetRecycledItemsView();
                return false;
            }
            case 16840: {
                final UIStartCraftMessage msg4 = (UIStartCraftMessage)message;
                if (!this.m_itemsToRecycle.isEmpty()) {
                    final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.confirmRecycle"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                    messageBoxControler.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type == 8) {
                                final TLongShortHashMap items = new TLongShortHashMap();
                                final int[] totalQty = { 0 };
                                UIRecycleFrame.this.m_itemsToRecycle.forEachEntry(new TLongObjectProcedure<IngredientView>() {
                                    @Override
                                    public boolean execute(final long uid, final IngredientView item) {
                                        items.put(uid, item.getQuantity());
                                        final int[] val$totalQty = totalQty;
                                        final int n = 0;
                                        val$totalQty[n] += item.getQuantity();
                                        return true;
                                    }
                                });
                                final int duration = totalQty[0] * 5000;
                                final DisassembleOccupation occupation = new DisassembleOccupation(items, duration, UIRecycleFrame.this.m_interactiveElement.getId());
                                localPlayer.setCurrentOccupation(occupation);
                                occupation.begin();
                                final ProgressBar progressBar = msg4.getBar();
                                progressBar.setValue(0.0f);
                                PropertiesProvider.getInstance().setPropertyValue("recycleRunning", true);
                                progressBar.setTweenDuration(duration);
                                progressBar.setValue(1.0f);
                            }
                            else {
                                final ToggleButton button = msg4.getButton();
                                button.setSelected(false);
                            }
                        }
                    });
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private static boolean possessesItem(final long itemId) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return localPlayer.getEquipmentInventory().containsUniqueId(itemId) || localPlayer.getBags().contains(itemId);
    }
    
    public void onRecycleResult(final boolean success) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("recycleDialog");
        final ProgressBar progressBar = (ProgressBar)map.getElement("progressBar");
        final Widget button = (Widget)map.getElement("progressBar");
        final ParticleDecorator particleDecorator = new ParticleDecorator();
        particleDecorator.onCheckOut();
        particleDecorator.setAlignment(Alignment9.CENTER);
        particleDecorator.setLevel(1);
        particleDecorator.setFile(success ? "6001038.xps" : "6001039.xps");
        particleDecorator.setUseParentScissor(true);
        particleDecorator.setRemovable(false);
        button.getAppearance().add(particleDecorator);
        progressBar.setTweenDuration(0L);
        progressBar.setValue(0.0f);
        this.m_itemsToRecycle.clear();
        this.resetRecycledItemsView();
        PropertiesProvider.getInstance().setPropertyValue("recycleRunning", false);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
            this.m_listener = new MobileStartPathListener() {
                @Override
                public void pathStarted(final PathMobile mobile, final PathFindResult path) {
                    WakfuGameEntity.getInstance().removeFrame(UIRecycleFrame.getInstance());
                }
            };
            character.getActor().addStartPathListener(this.m_listener);
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("recycleDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIRecycleFrame.getInstance());
                        final AbstractOccupation occupation = character.getCurrentOccupation();
                        if (occupation instanceof DisassembleOccupation) {
                            character.cancelCurrentOccupation(false, true);
                        }
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            this.reinitialize();
            final EventDispatcher dialog = Xulor.getInstance().load("recycleDialog", Dialogs.getDialogPath("recycleDialog"), 32768L, (short)10000);
            if (dialog != null) {
                this.m_dialogElementMap = dialog.getElementMap();
            }
            Xulor.getInstance().putActionClass("wakfu.recycle", RecycleDialogActions.class);
            PropertiesProvider.getInstance().setPropertyValue("recycleRunning", false);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            WakfuGameEntity.getInstance().getLocalPlayer().getActor().removeStartListener(this.m_listener);
            this.m_listener = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("recycleDialog");
            Xulor.getInstance().removeActionClass("wakfu.recycle");
            this.clean();
            if (this.m_interactiveElement != null) {
                this.m_interactiveElement.setState((short)1);
                this.m_interactiveElement.notifyViews();
            }
        }
    }
    
    public void setInteractiveElement(final WakfuClientMapInteractiveElement interactiveElement) {
        this.m_interactiveElement = interactiveElement;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public static UIRecycleFrame getInstance() {
        return UIRecycleFrame.m_instance;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIRecycleFrame.class);
        m_instance = new UIRecycleFrame();
    }
}
