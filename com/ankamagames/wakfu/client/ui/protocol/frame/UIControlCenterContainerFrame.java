package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.pvp.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.console.command.display.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.*;

public class UIControlCenterContainerFrame implements MessageFrame
{
    private static final UIControlCenterContainerFrame m_instance;
    private Window m_worldAndFightBarWindow;
    private static final byte MINIMAP_UPDATE_MESSAGE_ID = 0;
    private PopupElement m_popup;
    private PopupClientImpl m_popupClientImpl;
    private WakfuGameCalendar.SunStatutChangeListener m_sunStatusChangeListener;
    
    public UIControlCenterContainerFrame() {
        super();
        this.m_popupClientImpl = new PopupClientImpl();
    }
    
    public static UIControlCenterContainerFrame getInstance() {
        return UIControlCenterContainerFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (message instanceof ClockMessage) {
            final int subId = ((ClockMessage)message).getSubId();
            switch (subId) {
                case 0: {
                    MiniMapManager.getInstance().updateCenterFromCamera();
                    break;
                }
            }
            return false;
        }
        switch (message.getId()) {
            case 16606: {
                final UIMessage msg = (UIMessage)message;
                this.setMiniMapVisibility(msg.getBooleanValue(), false);
                return false;
            }
            case 16407: {
                final boolean displayed = PropertiesProvider.getInstance().getBooleanProperty("player.displayStates");
                PropertiesProvider.getInstance().setPropertyValue("player.displayStates", !displayed);
                return false;
            }
            case 16402: {
                WakfuGameEntity.getInstance().pushFrame(UIOptionsFrame.getInstance());
                return false;
            }
            case 19305: {
                if (!WakfuGameEntity.getInstance().hasFrame(UISpellsPageFrame.getInstance())) {
                    WakfuGameEntity.getInstance().pushFrame(UISpellsPageFrame.getInstance());
                }
                return false;
            }
            case 19420: {
                this.togglePvp();
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void togglePvp() {
        final CitizenComportment comportment = WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment();
        if (comportment.getPvpState().isActive()) {
            final String msgText = WakfuTranslator.getInstance().getString("pvp.disactivationWarning", 100);
            final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 24L);
            final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
            controler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new TogglePvpRequestMessage());
                    }
                }
            });
        }
        else {
            final String msgText = WakfuTranslator.getInstance().getString("pvp.activationWarning");
            final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 24L);
            final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
            controler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new TogglePvpRequestMessage());
                    }
                }
            });
        }
    }
    
    public void updateCitizenScore(final int score, final int delta) {
        if (delta == 0) {
            return;
        }
        final Widget image = (Image)this.getElementMap().getElement("citizenContainer");
        final ParticleDecorator particleDecorator = new ParticleDecorator();
        particleDecorator.onCheckOut();
        particleDecorator.setAlignment(Alignment9.CENTER);
        particleDecorator.setLevel(1);
        particleDecorator.setFile((delta > 0) ? "800195.xps" : "800196.xps");
        particleDecorator.setX((int)particleDecorator.getPosition().getX());
        particleDecorator.setY((int)particleDecorator.getPosition().getY() - 5);
        particleDecorator.setTimeToLive(1000);
        image.getAppearance().add(particleDecorator);
    }
    
    public void setDirectionButtonsEnabled(final boolean enabled) {
        final ElementMap map = this.getElementMap();
        if (map != null) {
            Widget w = (Widget)map.getElement("nwButton");
            if (w != null) {
                w.setEnabled(enabled);
            }
            w = (Widget)map.getElement("neButton");
            if (w != null) {
                w.setEnabled(enabled);
            }
            w = (Widget)map.getElement("swButton");
            if (w != null) {
                w.setEnabled(enabled);
            }
            w = (Widget)map.getElement("seButton");
            if (w != null) {
                w.setEnabled(enabled);
            }
        }
    }
    
    @Override
    public long getId() {
        return 10L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    private ElementMap getElementMap() {
        return Xulor.getInstance().getEnvironment().getElementMap("worldAndFightBarDialog");
    }
    
    public Window getWindow() {
        return this.m_worldAndFightBarWindow;
    }
    
    public void highLightCharacterInformationButton() {
        final Widget button = (Widget)this.getElementMap().getElement("fightInfoBtn");
        if (button == null) {
            return;
        }
        HighlightUIHelpers.highlightWidgetUntilClick(button);
    }
    
    public void highLightOsamodasSymbiotButton() {
        final Widget button = (Widget)this.getElementMap().getElement("symbiotBtn");
        if (button == null) {
            return;
        }
        HighlightUIHelpers.highlightWidgetUntilClick(button);
    }
    
    public void highLightCommunityButton() {
        final Widget widget = (Widget)this.getElementMap().getElement("communityButton");
        if (widget == null) {
            return;
        }
        HighlightUIHelpers.highlightWidgetUntilClick(widget);
    }
    
    public void highLightSpellsButton() {
        final Widget button = (Widget)this.getElementMap().getElement("spellButton");
        if (button == null) {
            return;
        }
        HighlightUIHelpers.highlightWidgetUntilClick(button);
    }
    
    public void highlightShortcut(final ShortCutBarType type, final short offset) {
        String listId = null;
        switch (type) {
            case FIGHT: {
                listId = "spellsShortcutList";
                break;
            }
            case WORLD: {
                listId = "itemsShortcutList";
                break;
            }
        }
        if (listId == null) {
            return;
        }
        final List list = (List)this.getElementMap().getElement(listId);
        if (list == null) {
            return;
        }
        final RenderableContainer renderable = list.getRenderableByOffset(offset);
        if (renderable == null) {
            return;
        }
        final Widget borderContainer = (Widget)renderable.getInnerElementMap().getElement("borderContainer");
        if (borderContainer == null) {
            return;
        }
        final Color c = new Color(0.531f, 0.812f, 0.835f, 1.0f);
        final Color c2 = new Color(Color.WHITE.get());
        final AbstractTween t = new ModulationColorTween(c2, c, borderContainer.getAppearance(), 0, 500, 6, TweenFunction.PROGRESSIVE);
        borderContainer.getAppearance().addTween(t);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            PropertiesProvider.getInstance().setPropertyValue("isInFight", false);
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            localPlayer.getShortcutBarManager().updateMainShortcutBar();
            PropertiesProvider.getInstance().setPropertyValue("challengeDetailsVisible", false);
            PropertiesProvider.getInstance().setPropertyValue("chat.enableInteractions", !localPlayer.hasProperty(WorldPropertyType.CHAT_UI_INTERACTION_DISABLED));
            final boolean forceOpen = localPlayer.hasProperty(WorldPropertyType.FOLLOW_ACHIEVEMENT_UI_FORCE_OPENED);
            PropertiesProvider.getInstance().setPropertyValue("followedAchievements.forceOpen", forceOpen);
            AchievementUIHelper.displayFollowedAchievements();
            this.initializeMinimap();
            this.initSundial();
            WakfuGameEntity.getInstance().pushFrame(UIPopupFrame.getInstance());
            WakfuGameEntity.getInstance().pushFrame(UISplitStackFrame.getInstance());
            this.m_worldAndFightBarWindow = (Window)Xulor.getInstance().load("worldAndFightBarDialog", Dialogs.getDialogPath("worldAndFightBarDialog"), 270336L, (short)10005);
            Xulor.getInstance().putActionClass("wakfu.controlCenter", ControlCenterDialogActions.class);
            PropertiesProvider.getInstance().setPropertyValue("controlCenter.forcePopup", true);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.cleanSunDial();
            this.cleanMinimap();
            Xulor.getInstance().unload("worldAndFightBarDialog");
            this.m_worldAndFightBarWindow = null;
            Xulor.getInstance().removeActionClass("wakfu.controlCenter");
            WakfuGameEntity.getInstance().removeFrame(UIPopupFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UISplitStackFrame.getInstance());
        }
    }
    
    private void initializeMinimap() {
        MiniMapManager.getInstance().initProperty();
        Xulor.getInstance().load("minimapPopup", Dialogs.getDialogPath("minimapPopup"), 8200L, (short)10000);
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("minimapPopup");
        if (map != null) {
            this.m_popup = (PopupElement)map.getElement("popup");
        }
        final EventDispatcher minimapDialog = Xulor.getInstance().load("minimapDialog", Dialogs.getDialogPath("minimapDialog"), 40960L, (short)10000);
        this.m_popupClientImpl.setMap(minimapDialog.getElementMap());
        final MapOverlay minimapWidget = (MapOverlay)minimapDialog.getElementMap().getElement("navigator");
        MiniMapManager.getInstance().setWidget(minimapWidget);
        MiniMapManager.getInstance().getLandMarkHandler().selectAllFilters();
        MessageScheduler.getInstance().addClock(this, 200L, 0, -1);
        final WakfuGamePreferences wgp = WakfuClientInstance.getInstance().getGamePreferences();
        final boolean enableMinimap = wgp.getBooleanValue(WakfuKeyPreferenceStoreEnum.MINIMAP_ENABLE);
        EnableMiniMapCommand.enableMiniMap(enableMinimap);
    }
    
    private void cleanMinimap() {
        MessageScheduler.getInstance().removeAllClocks(this, 0);
        Xulor.getInstance().unload("minimapDialog");
        Xulor.getInstance().unload("minimapPopup");
        this.m_popupClientImpl.setMap(null);
        this.m_popup = null;
        MiniMapManager.getInstance().setWidget(null);
    }
    
    public final void displayMiniMapPopup(final MapItemEvent e) {
        final String name = e.getDisplayableMapPoint().getName();
        if (name == null || name.length() == 0) {
            return;
        }
        PropertiesProvider.getInstance().setPropertyValue("minimapPopupDescription", name);
        final MapOverlay map = e.getTarget();
        this.m_popupClientImpl.setDisplayX(e.getMeshX() + map.getScreenX() - e.getMeshWidth() / 2);
        this.m_popupClientImpl.setDisplayY(e.getMeshY() + map.getScreenY() - e.getMeshHeight() / 2);
        this.m_popupClientImpl.setWidth(e.getMeshWidth());
        this.m_popupClientImpl.setHeight(e.getMeshHeight());
        this.m_popup.show(this.m_popupClientImpl);
    }
    
    public void setMiniMapVisibility(final boolean visible, final boolean force) {
        if (!force && visible != EnableMiniMapCommand.isEnableMiniMap()) {
            return;
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("minimapDialog");
        if (map != null) {
            final Widget w = (Widget)map.getElement("window");
            if (w != null) {
                w.setVisible(visible);
            }
        }
    }
    
    public void selectEmoteButton(final boolean select) {
        final ElementMap map = this.getElementMap();
        if (map == null) {
            return;
        }
        final EventDispatcher eventDispatcher = map.getElement("emoteButton");
        if (eventDispatcher == null) {
            return;
        }
        ((ToggleButton)eventDispatcher).setSelected(select);
    }
    
    private void initSundial() {
        if (!Xulor.getInstance().isLoaded("sundialDialog")) {
            Xulor.getInstance().load("sundialDialog", Dialogs.getDialogPath("sundialDialog"), 8320L, (short)10000);
        }
    }
    
    public void cleanSunDial() {
        Xulor.getInstance().unload("sundialDialog");
    }
    
    public void setSundialVisible(final boolean visible) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("sundialDialog");
        if (map == null) {
            return;
        }
        final Widget widget = (Widget)map.getElement("mainContainer");
        if (widget == null) {
            return;
        }
        widget.setVisible(visible);
    }
    
    static {
        m_instance = new UIControlCenterContainerFrame();
    }
}
