package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.game.steam.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.webShop.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.framework.net.http.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.gift.*;
import com.ankamagames.wakfu.client.core.game.soap.auth.*;
import java.util.*;

public class UIWebShopFrame implements MessageFrame
{
    private static final UIWebShopFrame INSTANCE;
    protected static final Logger m_logger;
    private THashSet<String> m_loadedDialogs;
    private static final int SCROLL_INTERVAL = 5000;
    private static final int SCROLL_DURATION = 500;
    private static final int SCROLL_WAIT_AFTER_INTERACTION = 10000;
    private List m_bannerList;
    private int m_currentOffset;
    private long m_lastOffsetChange;
    private long m_lastUserOffsetChange;
    private WebShopSession m_session;
    private CarrouselSchedule m_runnable;
    private DialogUnloadListener m_dialogUnloadListener;
    private final Runnable m_refreshTime;
    private final Runnable m_refreshMoney;
    
    public static UIWebShopFrame getInstance() {
        return UIWebShopFrame.INSTANCE;
    }
    
    private UIWebShopFrame() {
        super();
        this.m_loadedDialogs = new THashSet<String>();
        this.m_refreshTime = new Runnable() {
            @Override
            public void run() {
                if (UIWebShopFrame.this.m_session != null) {
                    try {
                        UIWebShopFrame.this.m_session.refreshTimedAndStockArticles();
                    }
                    catch (Exception e) {
                        UIWebShopFrame.m_logger.warn((Object)e.getMessage(), (Throwable)e);
                    }
                }
            }
        };
        this.m_refreshMoney = new Runnable() {
            @Override
            public void run() {
                if (UIWebShopFrame.this.m_session != null) {
                    try {
                        UIWebShopFrame.this.m_session.refreshOgrines();
                    }
                    catch (Exception e) {
                        UIWebShopFrame.m_logger.warn((Object)e.getMessage(), (Throwable)e);
                    }
                }
            }
        };
    }
    
    public void openCloseShopDialog() {
        if (this.m_loadedDialogs.contains("webShopDialog")) {
            Xulor.getInstance().unload("webShopDialog");
        }
        else {
            this.openShopDialog();
        }
    }
    
    public void openShopDialog() {
        if (SteamClientContext.INSTANCE.isInit() && !SteamClientContext.INSTANCE.isOverlayEnabled()) {
            final MessageBoxData data = new MessageBoxData(102, 0, WakfuTranslator.getInstance().getString("steam.needOverlayForShop"), 2L);
            Xulor.getInstance().msgBox(data);
        }
        else {
            this.checkInitialization(true);
            Worker.getInstance().pushMessage(new UIMessage((short)18500));
        }
    }
    
    public void openArticleDialog(final Article article) {
        this.checkInitialization(false);
        Worker.getInstance().pushMessage(new UIArticleMessage((short)18501, article));
    }
    
    public void steamFinalizeTxn(final int orderId, final boolean authorized) {
        this.checkInitialization(false);
        final AbstractUIMessage message = new UIMessage((short)18510);
        message.setIntValue(orderId);
        message.setBooleanValue(authorized);
        Worker.getInstance().pushMessage(message);
    }
    
    public void requestLockForUI(final String dialogId) {
        this.checkInitialization(false);
        this.m_loadedDialogs.add(dialogId);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 18500: {
                if (!this.shopEnabled()) {
                    return false;
                }
                this.load("webShopDialog", "webShopDialog");
                this.initWebShop();
                return false;
            }
            case 18501: {
                if (!this.shopEnabled()) {
                    return false;
                }
                final UIArticleMessage msg = (UIArticleMessage)message;
                final Article article = msg.getArticle();
                final String id = this.buildArticleDialogId(article.getId());
                this.load(id, "webShopArticleDialog");
                PropertiesProvider.getInstance().setLocalPropertyValue("webShop.article", article, id);
                return false;
            }
            case 18502: {
                if (!this.shopEnabled()) {
                    return false;
                }
                final UIMessage msg2 = (UIMessage)message;
                final int index = msg2.getIntValue();
                this.setUserOffset(index);
                return false;
            }
            case 18503: {
                if (!this.shopEnabled()) {
                    return false;
                }
                if (SteamClientContext.INSTANCE.isInit()) {
                    final Category ogrinsCategory = this.m_session.getCategoryFromKey("ogrins");
                    if (ogrinsCategory != null) {
                        this.m_session.setCurrentCategory(ogrinsCategory);
                        this.m_session.searchArticles("");
                    }
                }
                else {
                    String url;
                    if (Partner.getCurrentPartner() == Partner.BIGPOINT) {
                        final String baseUrl = WakfuConfiguration.getInstance().getString("bigpointShopBuyOgrinesUrl", null);
                        url = String.format(baseUrl, WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage().toLowerCase());
                    }
                    else {
                        final String baseUrl = WakfuConfiguration.getInstance().getString("shopBuyOgrinesUrl", null);
                        url = String.format(baseUrl, WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage().toLowerCase());
                    }
                    BrowserManager.openUrlInBrowser(url);
                    ProcessScheduler.getInstance().remove(this.m_refreshMoney);
                    ProcessScheduler.getInstance().schedule(this.m_refreshMoney, 10000L, -1);
                }
                return false;
            }
            case 18510: {
                if (!this.shopEnabled()) {
                    return false;
                }
                final AbstractUIMessage msg3 = (UIMessage)message;
                final int orderId = msg3.getIntValue();
                final boolean authorized = msg3.getBooleanValue();
                this.m_session.steamFinalizeTxn(orderId, authorized);
                if (this.m_loadedDialogs.isEmpty()) {
                    WakfuGameEntity.getInstance().removeFrame(UIWebShopFrame.INSTANCE);
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean shopEnabled() {
        return SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.SHOP_ENABLED);
    }
    
    public WebShopSession getSession() {
        return this.m_session;
    }
    
    private void checkInitialization(final boolean shop) {
        if (!WakfuGameEntity.getInstance().hasFrame(this)) {
            WakfuGameEntity.getInstance().pushFrame(this);
        }
        else if (shop) {
            this.m_session.forceHome();
        }
    }
    
    private void load(final String id, final String path) {
        if (!this.m_loadedDialogs.contains(id)) {
            Xulor.getInstance().load(id, Dialogs.getDialogPath(path), 32768L, (short)10000);
            this.m_loadedDialogs.add(id);
        }
    }
    
    private void initWebShop() {
        final ElementMap elementMap = Xulor.getInstance().getEnvironment().getElementMap("webShopDialog");
        if (elementMap == null) {
            return;
        }
        this.m_bannerList = (List)elementMap.getElement("bannerList");
        if (this.m_bannerList == null) {
            return;
        }
        this.m_bannerList.setScrollMode(List.ListScrollMode.CIRCULAR);
        this.m_bannerList.setScrollOnMouseWheel(false);
        this.m_lastOffsetChange = System.currentTimeMillis();
        this.m_runnable = new CarrouselSchedule();
        ProcessScheduler.getInstance().schedule(this.m_runnable, 250L, -1);
    }
    
    private void setUserOffset(final int offset) {
        if (!this.m_runnable.m_terminated) {
            return;
        }
        final int previousIndex = (this.m_currentOffset % this.m_session.getCarrouselSize() + this.m_session.getCarrouselSize()) % this.m_session.getCarrouselSize();
        this.setListOffset(offset, previousIndex);
        this.m_lastUserOffsetChange = System.currentTimeMillis();
    }
    
    private void setListOffset(final int offset, final int previousOffset) {
        if (this.m_bannerList == null) {
            return;
        }
        this.m_bannerList.removeTweensOfType(ListOffsetTween.class);
        final ListOffsetTween tween = new ListOffsetTween(previousOffset, offset, this.m_bannerList, 0, 500, TweenFunction.PROGRESSIVE);
        tween.addTweenEventListener(new TweenEventListener() {
            @Override
            public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                if (e == TweenEvent.TWEEN_ENDED) {
                    UIWebShopFrame.this.m_currentOffset = (int)UIWebShopFrame.this.m_bannerList.getOffset();
                    final int carrouselSize = UIWebShopFrame.this.m_session.getCarrouselSize();
                    if (carrouselSize == 0) {
                        return;
                    }
                    final int index = (UIWebShopFrame.this.m_currentOffset % carrouselSize + carrouselSize) % carrouselSize;
                    UIWebShopFrame.this.m_session.setCurrentCarrousel(index);
                }
            }
        });
        this.m_bannerList.addTween(tween);
    }
    
    private String buildArticleDialogId(final int articleId) {
        return "webShopArticleDialog" + articleId;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    UIWebShopFrame.this.m_loadedDialogs.remove(id);
                    if ("webShopDialog".equals(id)) {
                        ProcessScheduler.getInstance().remove(UIWebShopFrame.this.m_runnable);
                    }
                    if (UIWebShopFrame.this.m_loadedDialogs.isEmpty()) {
                        WakfuGameEntity.getInstance().removeFrame(UIWebShopFrame.INSTANCE);
                    }
                }
            };
            (this.m_session = new WebShopSession()).init(new WebShopListener() {
                @Override
                public void onInitialize() {
                }
                
                @Override
                public void onHome() {
                    UIWebShopFrame.this.m_session.setCurrentCarrousel(0);
                    UIWebShopFrame.this.setListOffset(0, 0);
                }
                
                @Override
                public void onSearch() {
                }
            });
            PropertiesProvider.getInstance().setPropertyValue("webShop", this.m_session);
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().putActionClass("wakfu.webShop", WebShopDialogActions.class);
            ProcessScheduler.getInstance().schedule(this.m_refreshTime, 10000L, -1);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            for (final String dialogId : this.m_loadedDialogs) {
                Xulor.getInstance().unload(dialogId);
            }
            this.m_loadedDialogs.clear();
            Xulor.getInstance().removeActionClass("wakfu.webShop");
            PropertiesProvider.getInstance().removeProperty("webShop");
            final GiftInventoryRequestMessage netMessage = new GiftInventoryRequestMessage();
            netMessage.setLocale(WakfuTranslator.getInstance().getLanguage().getActualLocale());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
            AuthentificationManager.INSTANCE.clear();
            ProcessScheduler.getInstance().remove(this.m_refreshTime);
            ProcessScheduler.getInstance().remove(this.m_refreshMoney);
            this.m_session = null;
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        INSTANCE = new UIWebShopFrame();
        m_logger = Logger.getLogger((Class)UIWebShopFrame.class);
    }
    
    private class CarrouselSchedule implements Runnable
    {
        boolean m_terminated;
        
        CarrouselSchedule() {
            super();
            this.m_terminated = true;
        }
        
        @Override
        public void run() {
            final long now = System.currentTimeMillis();
            if (now - UIWebShopFrame.this.m_lastUserOffsetChange < 10000L) {
                this.m_terminated = true;
                return;
            }
            if (now - UIWebShopFrame.this.m_lastOffsetChange < 5000L) {
                this.m_terminated = true;
                return;
            }
            UIWebShopFrame.this.m_lastOffsetChange = now;
            UIWebShopFrame.this.setListOffset(UIWebShopFrame.this.m_currentOffset + 1, UIWebShopFrame.this.m_currentOffset);
            this.m_terminated = false;
        }
    }
}
