package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.core.proxyGroup.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.proxy.*;
import com.ankamagames.wakfu.client.ui.protocol.message.server.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import java.util.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.dispatch.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.google.common.collect.*;
import com.google.common.base.*;

public class UIServerChoiceFrame implements MessageFrame
{
    public static final UIServerChoiceFrame INSTANCE;
    private static WakfuServerView m_selectedServer;
    private final Runnable m_refreshServerListProcess;
    private boolean m_selectedServerDirty;
    private List m_list;
    private ListScroller m_scroller;
    private final Collection<WakfuServerView> m_serverViews;
    private boolean m_serverFiltered;
    
    public UIServerChoiceFrame() {
        super();
        this.m_refreshServerListProcess = new Runnable() {
            @Override
            public void run() {
                final Message request = new ClientProxiesRequestMessage();
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request);
            }
        };
        this.m_serverViews = new ArrayList<WakfuServerView>();
        this.m_serverFiltered = true;
    }
    
    public static void onSelectServer(final WakfuServerView server, final long forcedAccountId, final String keyLabel) {
        UIServerChoiceFrame.m_selectedServer = server;
        WakfuProgressMonitorManager.getInstance().getProgressMonitor(true).beginTask(WakfuTranslator.getInstance().getString(keyLabel), 0);
        requestToken(forcedAccountId);
    }
    
    private static void requestToken(final long forcedAccountId) {
        final WakfuAuthenticationTokenRequestMessage tokenRequestMessage = new WakfuAuthenticationTokenRequestMessage();
        tokenRequestMessage.setServerId(UIServerChoiceFrame.m_selectedServer.getProxy().getId());
        tokenRequestMessage.setAccountId(forcedAccountId);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(tokenRequestMessage);
    }
    
    public static void connectToServer() {
        NetCharacterChoiceFrame.getInstance().enableLoadUI(false);
        WakfuGameEntity.getInstance().removeFrame(UIServerChoiceFrame.INSTANCE);
        final Proxy proxy = UIServerChoiceFrame.m_selectedServer.getProxy();
        final ProxyGroup wakfuProxyGroup = new WakfuProxyGroup(proxy.getId(), proxy.getName(), proxy.getCommunity());
        for (final int port : proxy.getPorts()) {
            wakfuProxyGroup.addProxy(new ProxyAddress(proxy.getAddress(), port));
        }
        WakfuGameEntity.getInstance().disconnectFromServer("Dispatch");
        WakfuGameEntity.getInstance().setServerId(UIServerChoiceFrame.m_selectedServer.getWorldInfo().getServerId());
        WakfuGameEntity.getInstance().setProxyGroup(wakfuProxyGroup);
        WakfuGameEntity.getInstance().setCurrentLoginPhase(LoginPhase.CONNECTION);
        WakfuGameEntity.getInstance().connect();
    }
    
    public void setSelectedServerDirty(final boolean selectedServerDirty) {
        this.m_selectedServerDirty = selectedServerDirty;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16490: {
                final UIServerReferenceMessage msg = (UIServerReferenceMessage)message;
                final WakfuServerView reference = msg.getServerReference();
                final long forcedAccountId = msg.getForcedAccountId();
                onSelectServer(reference, forcedAccountId, "loadingWaitingWorldNotFull.progress");
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public void setScrollMode(final ListScroller.ScrollMode mode) {
        this.m_scroller.setScrollMode(mode);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            ProcessScheduler.getInstance().schedule(this.m_refreshServerListProcess, 10000L);
            MobileManager.getInstance().removeAllMobiles();
            Xulor.getInstance().load("serverChoiceDialog", Dialogs.getDialogPath("serverChoiceDialog"), 8192L, (short)10000);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("serverChoiceDialog");
            (this.m_list = (List)map.getElement("list")).addListContentListener(new EditableRenderableCollection.CollectionContentLoadedListener() {
                @Override
                public void onContentLoaded() {
                    final WakfuServerView wakfuServerView = (WakfuServerView)PropertiesProvider.getInstance().getObjectProperty("serverChoice.selectedServerReference");
                    if (wakfuServerView != null) {
                        UIServerChoiceFrame.this.m_list.setListOffset(MathHelper.clamp(UIServerChoiceFrame.this.m_list.getSelectedOffsetByValue(wakfuServerView) - 3, 0, UIServerChoiceFrame.this.m_list.size() - 3));
                    }
                    UIServerChoiceFrame.this.m_list.removeListContentLoadListener(this);
                }
            });
            (this.m_scroller = new ListScroller(this.m_list)).start();
            this.m_serverFiltered = true;
            Xulor.getInstance().putActionClass("wakfu.serverChoice", ServerChoiceDialogActions.class);
            this.activateSteamTween();
            UISystemBarFrame.getInstance().reloadMenuBarDialog();
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            UIServerChoiceFrame.m_selectedServer = null;
            this.disactivateSteamTween();
            this.m_selectedServerDirty = false;
            PropertiesProvider.getInstance().removeProperty("serverChoice.serverReferencesList");
            PropertiesProvider.getInstance().removeProperty("serverChoice.selectedServerReference");
            ProcessScheduler.getInstance().remove(this.m_refreshServerListProcess);
            if (WakfuGameEntity.getInstance().getCurrentLoginPhase() == LoginPhase.DISPATCH) {
                WakfuGameEntity.getInstance().disconnectFromServer("Dispatch");
            }
            Xulor.getInstance().unload("serverChoiceDialog");
            this.m_scroller.stop();
            this.m_list = null;
            this.m_serverFiltered = true;
            Xulor.getInstance().removeActionClass("wakfu.serverChoice");
        }
    }
    
    private void activateSteamTween() {
        final Image image = (Image)Xulor.getInstance().getEnvironment().getElementMap("serverChoiceDialog").getElement("steamLinkImage");
        if (image == null || !image.isVisible()) {
            return;
        }
        final Color c = new Color(Color.BLACK.get());
        final Color c2 = new Color(1.0f, 0.78f, 0.15f, 1.0f);
        final DecoratorAppearance appearance = image.getAppearance();
        final AbstractTween t = new ModulationColorTween(c, c2, appearance, 0, 3000, -1, TweenFunction.PROGRESSIVE);
        appearance.addTween(t);
        final ImageScaleTween ist = new ImageScaleTween(0.9f, 1.0f, image, 0, 1500, TweenFunction.FULL_TO_NULL, image.getImageMesh(), -1);
        image.addTween(ist);
    }
    
    private void disactivateSteamTween() {
        final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap("serverChoiceDialog");
        if (elementMap == null) {
            return;
        }
        final Image image = (Image)elementMap.getElement("steamLinkImage");
        if (image == null || !image.isVisible()) {
            return;
        }
        final DecoratorAppearance appearance = image.getAppearance();
        appearance.removeTweensOfType(ModulationColorTween.class);
        image.removeTweensOfType(ImageScaleTween.class);
    }
    
    public void reloadServerList(final Optional<ArrayList<WakfuServerView>> optionalServers) {
        if (optionalServers.isPresent()) {
            this.m_serverViews.clear();
            this.m_serverViews.addAll((Collection<? extends WakfuServerView>)optionalServers.get());
        }
        final Ordering<WakfuServerView> byServerIds = (Ordering<WakfuServerView>)Ordering.natural().onResultOf((Function)new Order());
        final ImmutableList<WakfuServerView> filteredServers = (ImmutableList<WakfuServerView>)FluentIterable.from((Iterable)this.m_serverViews).filter((Predicate)new VisibleOnly()).toSortedList((Comparator)byServerIds);
        final int serverId = WakfuClientInstance.getInstance().getGamePreferences().getIntValue(WakfuKeyPreferenceStoreEnum.LAST_SERVER_ID_KEY);
        for (final WakfuServerView serverView : filteredServers) {
            final WorldInfo worldInfo = serverView.getWorldInfo();
            final int currentServerId = worldInfo.getServerId();
            if (!this.m_selectedServerDirty && currentServerId == serverId) {
                PropertiesProvider.getInstance().setPropertyValue("serverChoice.selectedServerReference", serverView);
            }
        }
        PropertiesProvider.getInstance().setPropertyValue("serverChoice.serverReferencesList", filteredServers);
    }
    
    public boolean isServerFiltered() {
        return this.m_serverFiltered;
    }
    
    public void toggleServerFiltered() {
        this.m_serverFiltered = !this.m_serverFiltered;
        this.reloadServerList((Optional<ArrayList<WakfuServerView>>)Optional.absent());
    }
    
    static {
        INSTANCE = new UIServerChoiceFrame();
    }
    
    private static class VisibleOnly implements Predicate<WakfuServerView>
    {
        public boolean apply(final WakfuServerView input) {
            final WorldInfo info = input.getWorldInfo();
            if (!WorldInfoHelper.isPartnerValid(info, Partner.getCurrentPartner())) {
                return false;
            }
            if (!UIServerChoiceFrame.INSTANCE.isServerFiltered()) {
                return true;
            }
            final ArrayList<Community> validCommunities = (ArrayList<Community>)Lists.newArrayList((Object[])new Community[] { DispatchAccountInformation.getCommunity() });
            return !WorldInfoHelper.getCommunities(info, (Predicate<Community>)Predicates.in((Collection)validCommunities)).isEmpty();
        }
    }
    
    private static class Order implements Function<WakfuServerView, Integer>
    {
        public Integer apply(final WakfuServerView input) {
            return input.getProxy().getOrder();
        }
    }
}
