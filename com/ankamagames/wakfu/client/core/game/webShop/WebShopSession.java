package com.ankamagames.wakfu.client.core.game.webShop;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.soap.account.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.gift.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.game.steam.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.game.soap.shop.*;
import gnu.trove.*;

public class WebShopSession extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String READY = "ready";
    public static final String MODE = "mode";
    public static final String CARROUSEL = "carrousel";
    public static final String ROOT_CATEGORIES = "rootCategories";
    public static final String CATEGORIES = "categories";
    public static final String CURRENT_CATEGORY = "currentCategory";
    public static final String PARENT_CATEGORY = "parentCategory";
    public static final String HOME_SUB_CATEGORIES = "homeSubCategories";
    public static final String ARTICLES = "articles";
    public static final String HOME_ARTICLES = "homeArticles";
    public static final String HIGHLIGHTS = "highlights";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String TOTAL_PAGES = "totalPages";
    public static final String CURRENT_CARROUSEL = "currentCarrousel";
    private static final String WALLET = "wallet";
    private static final String PAGES_DESCRIPTION = "pagesDescription";
    public static final int PAGE_SIZE = 12;
    private final Object m_mutex;
    private static final int NONE = 1;
    private static final int INITIALIZING = 2;
    private static final int INITIALIZED = 3;
    private boolean m_processing;
    private Mode m_mode;
    private ContentType m_contentType;
    private final ArrayList<WebShopListener> m_listeners;
    private final TByteIntHashMap m_wallet;
    private final ArrayList<Category> m_rootCategories;
    private final ArrayList<Category> m_categories;
    private final ArrayList<Highlight> m_carrousel;
    private final ArrayList<Article> m_homeArticles;
    private final TIntArrayList m_rootCategoriesIds;
    private final ArrayList<GondolaHead> m_homeSubCategories;
    private final ArrayList<Highlight> m_highlights;
    private Category m_currentCategory;
    private GondolaHead m_currentGondolaHead;
    private Highlight m_currentCarrouselHighlight;
    private final ArrayList<Article> m_articles;
    private int m_currentPage;
    private int m_totalPages;
    private String m_currentSearch;
    private int m_state;
    private int m_walletState;
    private int m_categoriesState;
    private final ArrayList<WalletEntry> m_walletEntries;
    private final TIntObjectHashMap<ArticlesProvider> m_providers;
    
    public WebShopSession() {
        super();
        this.m_mutex = new Object();
        this.m_listeners = new ArrayList<WebShopListener>();
        this.m_wallet = new TByteIntHashMap();
        this.m_rootCategories = new ArrayList<Category>();
        this.m_categories = new ArrayList<Category>();
        this.m_carrousel = new ArrayList<Highlight>();
        this.m_homeArticles = new ArrayList<Article>();
        this.m_rootCategoriesIds = new TIntArrayList();
        this.m_homeSubCategories = new ArrayList<GondolaHead>();
        this.m_highlights = new ArrayList<Highlight>();
        this.m_articles = new ArrayList<Article>();
        this.m_currentPage = 0;
        this.m_totalPages = 0;
        this.m_currentSearch = null;
        this.m_state = 1;
        this.m_walletState = 1;
        this.m_categoriesState = 1;
        this.m_walletEntries = new ArrayList<WalletEntry>();
        this.m_providers = new TIntObjectHashMap<ArticlesProvider>();
    }
    
    public void init(final WebShopListener listener) {
        synchronized (this.m_mutex) {
            if (this.m_state == 3) {
                listener.onInitialize();
            }
            else {
                this.m_listeners.add(listener);
            }
            this.m_mode = Mode.LOADING;
            this.m_contentType = ContentType.CATEGORY;
            if (this.m_state != 1) {
                return;
            }
            WebShopSession.m_logger.info((Object)"WebSession Init");
            this.m_state = 2;
            this.m_categoriesState = 2;
            this.m_walletState = 2;
            for (final Currency currency : Currency.values()) {
                if (!currency.isHardCurrency()) {
                    if (currency != Currency.KRZ || SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.SHOP_ENABLE_KROSZ)) {
                        this.m_walletEntries.add(new WalletEntry(currency, this));
                    }
                }
            }
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "wallet");
            this.home(true, true);
            MoneyLoader.INSTANCE.getMoney(new MoneyListener() {
                @Override
                public void onMoney(final int ogrins, final int krosz) {
                    synchronized (WebShopSession.this.m_mutex) {
                        WebShopSession.m_logger.info((Object)"WebSession : money init");
                        WebShopSession.this.updateWallet(ogrins, krosz);
                        WebShopSession.this.m_walletState = 3;
                        WebShopSession.this.checkInitialization();
                    }
                }
                
                @Override
                public void onError() {
                    WebShopSession.m_logger.warn((Object)"Erreur \u00e0 la r\u00e9cup\u00e9ration du wallet.");
                    synchronized (WebShopSession.this.m_mutex) {
                        WebShopSession.this.m_walletState = 1;
                    }
                }
            });
        }
    }
    
    private void home(final boolean withCategories, final boolean withInit) {
        HomeLoader.INSTANCE.getHome(new HomeListener() {
            @Override
            public void onHome(final ArrayList<Category> categories, final ArrayList<Highlight> carrousel, final ArrayList<Highlight> images, final ArrayList<GondolaHead> main, final ArrayList<Article> articles) {
                synchronized (WebShopSession.this.m_mutex) {
                    WebShopSession.this.m_mode = Mode.HOME;
                    if (withCategories) {
                        this.initCategories(categories);
                    }
                    this.initHomeSubcategories(main);
                    this.initCarrousel(carrousel);
                    this.initHomeHighlights(images);
                    this.initHomeArticles(articles);
                    if (withInit) {
                        WebShopSession.this.checkInitialization();
                    }
                    WebShopSession.this.m_currentCategory = null;
                    for (int i = 0, size = WebShopSession.this.m_listeners.size(); i < size; ++i) {
                        final WebShopListener listener = WebShopSession.this.m_listeners.get(i);
                        listener.onHome();
                    }
                    PropertiesProvider.getInstance().firePropertyValueChanged(WebShopSession.this, "mode", "currentCategory", "parentCategory");
                }
            }
            
            private void initHomeHighlights(final ArrayList<Highlight> images) {
                WebShopSession.this.m_highlights.clear();
                WebShopSession.this.m_highlights.addAll(images);
                PropertiesProvider.getInstance().firePropertyValueChanged(WebShopSession.this, "highlights");
            }
            
            private void initHomeArticles(final ArrayList<Article> articles) {
                WebShopSession.this.m_homeArticles.clear();
                for (int i = 0, size = Math.min(6, articles.size()); i < size; ++i) {
                    WebShopSession.this.m_homeArticles.add(articles.get(i));
                }
                PropertiesProvider.getInstance().firePropertyValueChanged(WebShopSession.this, "homeArticles");
            }
            
            private void initHomeSubcategories(final ArrayList<GondolaHead> main) {
                WebShopSession.this.m_homeSubCategories.clear();
                WebShopSession.this.m_homeSubCategories.addAll(main);
                PropertiesProvider.getInstance().firePropertyValueChanged(WebShopSession.this, "homeSubCategories");
            }
            
            private void initCarrousel(final ArrayList<Highlight> carrousel) {
                WebShopSession.this.m_carrousel.clear();
                WebShopSession.this.m_carrousel.addAll(carrousel);
                PropertiesProvider.getInstance().firePropertyValueChanged(WebShopSession.this, "carrousel");
            }
            
            private void initCategories(final ArrayList<Category> categories) {
                final TObjectProcedure<Category> proc = new TObjectProcedure<Category>() {
                    @Override
                    public boolean execute(final Category object) {
                        WebShopSession.this.m_categories.add(object);
                        return true;
                    }
                };
                WebShopSession.m_logger.info((Object)"WebSession : categories init");
                WebShopSession.this.m_rootCategories.clear();
                WebShopSession.this.m_rootCategories.addAll(categories);
                WebShopSession.this.m_rootCategoriesIds.clear();
                for (int i = 0, size = categories.size(); i < size; ++i) {
                    final Category category = categories.get(i);
                    category.forEachChild(proc);
                    WebShopSession.this.m_rootCategoriesIds.add(category.getId());
                }
                WebShopSession.this.m_categoriesState = 3;
                PropertiesProvider.getInstance().firePropertyValueChanged(WebShopSession.this, "categories", "rootCategories", "parentCategory");
            }
            
            @Override
            public void onError() {
                WebShopSession.m_logger.warn((Object)"Probl\u00e8me \u00e0 la r\u00e9cup\u00e9ration du home du shop.");
                synchronized (WebShopSession.this.m_mutex) {
                    WebShopSession.this.m_categoriesState = 1;
                }
            }
        });
    }
    
    private void checkInitialization() {
        synchronized (this.m_mutex) {
            if (this.m_state != 3 && this.m_categoriesState == 3 && this.m_walletState == 3) {
                this.m_state = 3;
                for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
                    final WebShopListener webShopListener = this.m_listeners.get(i);
                    webShopListener.onInitialize();
                }
            }
        }
    }
    
    public int getCarrouselSize() {
        return this.m_carrousel.size();
    }
    
    public int getCarrouselHighlightIndex(final Highlight highlight) {
        return this.m_carrousel.indexOf(highlight);
    }
    
    public void setCurrentCarrousel(final int index) {
        if (index < 0 || index >= this.m_carrousel.size()) {
            this.m_currentCarrouselHighlight = null;
        }
        else {
            this.m_currentCarrouselHighlight = this.m_carrousel.get(index);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentCarrousel");
    }
    
    void updateWallet(final int ogrins, final int krozs) {
        this.m_wallet.put(Currency.OGR.getId(), ogrins);
        this.m_wallet.put(Currency.KRZ.getId(), krozs);
        for (int i = 0, size = this.m_walletEntries.size(); i < size; ++i) {
            final WalletEntry entry = this.m_walletEntries.get(i);
            entry.updateValue();
        }
    }
    
    int getWalletAmount(final Currency currency) {
        return this.m_wallet.get(currency.getId());
    }
    
    public Category getCurrentCategory() {
        return this.m_currentCategory;
    }
    
    public void setCurrentCategory(final Category category) {
        this.m_currentCategory = category;
        if (this.m_currentCategory != null) {
            this.m_contentType = ContentType.CATEGORY;
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentCategory");
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "parentCategory");
        this.resetPreloading();
    }
    
    public void setCurrentGondolaHead(final GondolaHead gondolaHead) {
        this.m_currentGondolaHead = gondolaHead;
        if (this.m_currentGondolaHead != null) {
            this.m_contentType = ContentType.GONDOLA_HEAD;
        }
        this.resetPreloading();
    }
    
    public Category getCategoryFromId(final int categoryId) {
        if (this.m_categoriesState != 3) {
            return null;
        }
        for (final Category category : this.m_categories) {
            if (category.getId() == categoryId) {
                return category;
            }
        }
        return null;
    }
    
    public Category getCategoryFromKey(final String key) {
        if (key == null || this.m_categoriesState != 3) {
            return null;
        }
        for (final Category category : this.m_categories) {
            if (key.equals(category.getKey())) {
                return category;
            }
        }
        return null;
    }
    
    public void searchArticles(final String search) {
        this.searchArticles(search, 1);
    }
    
    public void firstPage() {
        if (this.m_currentPage == 1) {
            return;
        }
        this.searchArticles(this.m_currentSearch, 1);
    }
    
    public void previousPage() {
        if (this.m_currentPage <= 1) {
            return;
        }
        this.searchArticles(this.m_currentSearch, this.m_currentPage - 1);
    }
    
    public void nextPage() {
        if (this.m_currentPage == this.m_totalPages) {
            return;
        }
        this.searchArticles(this.m_currentSearch, this.m_currentPage + 1);
    }
    
    public void lastPage() {
        if (this.m_currentPage == this.m_totalPages) {
            return;
        }
        this.searchArticles(this.m_currentSearch, this.m_totalPages);
    }
    
    public void quickBuy(final Article article) {
        this.getProcessLock();
        QuickBuyLoader.INSTANCE.quickBuy(article.getId(), 1, new QuickBuyListener() {
            @Override
            public void onPartnerQuickBuy() {
                WebShopSession.this.releaseProcessLock();
            }
            
            @Override
            public void onQuickBuy(final int ogrins, final int krozs) {
                String purchaseConfirmKey = "shop.purchaseSuccessWarning";
                if (article.containsType(ArticleType.VIRTUAL_SUBSCRIPTION_LEVEL)) {
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new RefreshSubscriptionRequestMessage());
                }
                else if (article.containsType(ArticleType.ACCOUNT_STATUS)) {
                    final ArrayList<SubArticle> subArticles = article.getSubArticles();
                    if (!subArticles.isEmpty()) {
                        final SubArticle subArticle = subArticles.get(0);
                        if ("WKCHARACTERS".equals(subArticle.getMetaStringValue("STATUS"))) {
                            final Message msg = new RefreshAdditionalCharacterSlotsRequestMessage();
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
                            purchaseConfirmKey = "shop.purchaseCharacterSlotSuccessWarning";
                        }
                        else if ("WKVAULTUP".equals(subArticle.getMetaStringValue("STATUS"))) {
                            final Message msg = new RefreshVaultUpgradeRequestMessage();
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
                            purchaseConfirmKey = "shop.purchaseVaultUpgradeSuccessWarning";
                        }
                    }
                }
                article.buyStock();
                WebShopSession.this.updateWallet(ogrins, krozs);
                final GiftInventoryRequestMessage netMessage = new GiftInventoryRequestMessage();
                netMessage.setLocale(WakfuTranslator.getInstance().getLanguage().getActualLocale());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                WebShopSession.this.releaseProcessLock();
                Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString(purchaseConfirmKey), WakfuMessageBoxConstants.getMessageBoxIconUrl(7), 515L, 102, 1);
            }
            
            @Override
            public void onError(final QuickBuyError error) {
                WebShopSession.this.releaseProcessLock();
            }
        });
    }
    
    public void steamFinalizeTxn(final int orderId, final boolean authorized) {
        if (!SteamClientContext.INSTANCE.isInit()) {
            return;
        }
        this.getProcessLock();
        PartnerFinalizeTxnLoader.INSTANCE.partnerFinalizeTxn(orderId, authorized, new PartnerFinalizeTxnListener() {
            @Override
            public void onPartnerFinalizeTxn(final int ogrins, final int krozs) {
                WebShopSession.this.updateWallet(ogrins, krozs);
                WebShopSession.this.releaseProcessLock();
            }
            
            @Override
            public void onError(final PartnerFinalizeTxnError error) {
                WebShopSession.this.releaseProcessLock();
            }
        });
    }
    
    private void searchArticles(final String currentSearch, final int currentPage) {
        if (this.m_processing) {
            return;
        }
        if (!StringUtils.equals(this.m_currentSearch, currentSearch, true)) {
            this.resetPreloading();
        }
        this.m_currentSearch = currentSearch;
        this.m_currentPage = currentPage;
        if (this.m_contentType == ContentType.CATEGORY && this.m_currentCategory == null && StringUtils.isEmptyOrNull(currentSearch)) {
            this.home();
        }
        else {
            this.ensureProvidersAreLoaded();
            this.searchArticles();
        }
    }
    
    public void searchArticles(final int page, final int size, final ArticlesSearchListener listener) {
        this.getProcessLock();
        ArticlesSearchLoader.INSTANCE.getArticlesSearch(this.m_rootCategoriesIds, "", page, size, new ArticlesSearchListener() {
            @Override
            public void onArticlesSearch(final ArrayList<Article> articles, final int count) {
                WebShopSession.this.releaseProcessLock();
                listener.onArticlesSearch(articles, count);
            }
            
            @Override
            public void onError() {
                WebShopSession.this.releaseProcessLock();
            }
        });
    }
    
    private void ensureProvidersAreLoaded() {
        final int[] arr$;
        final int[] keys = arr$ = this.m_providers.keys();
        for (final int key : arr$) {
            if (key < this.m_currentPage - 1 || key > this.m_currentPage + 1) {
                this.m_providers.remove(key);
            }
        }
        this.ensureProviderIsLoaded(this.m_currentPage);
        if (this.m_totalPages == 0 || this.m_currentPage < this.m_totalPages) {
            this.ensureProviderIsLoaded(this.m_currentPage + 1);
        }
        if (this.m_totalPages == 0) {
            return;
        }
        if (this.m_currentPage > 1) {
            this.ensureProviderIsLoaded(this.m_currentPage - 1);
        }
    }
    
    private void ensureProviderIsLoaded(final int page) {
        if (this.m_providers.get(page) != null) {
            return;
        }
        if (this.m_contentType == ContentType.CATEGORY) {
            if (StringUtils.isEmptyOrNull(this.m_currentSearch)) {
                this.m_providers.put(page, new ArticleListProvider(this.m_currentCategory.getId(), page, null));
            }
            else {
                this.m_providers.put(page, new ArticleSearchProvider(this.m_rootCategoriesIds, this.m_currentSearch, page, null));
            }
        }
        else if (this.m_contentType == ContentType.GONDOLA_HEAD) {
            this.m_providers.put(page, new GondolaHeadListArticlesProvider(this.m_currentGondolaHead.getId(), 12, page, null));
        }
    }
    
    public void forceHome() {
        this.home();
        this.m_mode = Mode.HOME;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "mode");
    }
    
    public void home() {
        this.home(false, false);
    }
    
    private void searchArticles() {
        final ArticlesProvider provider = this.m_providers.get(this.m_currentPage);
        if (provider.isReady()) {
            this.setArticles(provider.getArticles(), provider.getCount(), false);
        }
        else {
            this.getProcessLock();
            provider.setListener(new ArticlesListener() {
                @Override
                public void onArticlesList(final ArrayList<Article> articles, final int count) {
                    WebShopSession.this.setArticles(articles, count, true);
                }
                
                @Override
                public void onError() {
                    WebShopSession.this.releaseProcessLock();
                }
            });
        }
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            final WebShopListener listener = this.m_listeners.get(i);
            listener.onSearch();
        }
    }
    
    private void resetPreloading() {
        final TIntObjectIterator<ArticlesProvider> it = this.m_providers.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().clean();
        }
        this.m_providers.clear();
    }
    
    private void setArticles(final List<Article> articles, final int count, final boolean releaseLock) {
        this.m_mode = Mode.ARTICLES;
        this.m_articles.clear();
        this.m_articles.addAll(articles);
        this.m_totalPages = (int)Math.ceil(count / 12.0f);
        if (releaseLock) {
            this.releaseProcessLock();
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "articles", "currentPage", "totalPages", "pagesDescription", "mode");
    }
    
    private void getProcessLock() {
        this.m_processing = true;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "ready");
    }
    
    void releaseProcessLock() {
        this.m_processing = false;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "ready");
    }
    
    public boolean isInitialized() {
        return this.m_state == 3;
    }
    
    @Override
    public String[] getFields() {
        return WebShopSession.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("wallet")) {
            return this.m_walletEntries;
        }
        if (fieldName.equals("mode")) {
            return this.m_mode.getId();
        }
        if (fieldName.equals("categories")) {
            return this.m_categories;
        }
        if (fieldName.equals("currentCarrousel")) {
            return this.m_currentCarrouselHighlight;
        }
        if (fieldName.equals("homeArticles")) {
            return this.m_homeArticles;
        }
        if (fieldName.equals("highlights")) {
            return this.m_highlights;
        }
        if (fieldName.equals("homeSubCategories")) {
            return this.m_homeSubCategories;
        }
        if (fieldName.equals("carrousel")) {
            return this.m_carrousel;
        }
        if (fieldName.equals("rootCategories")) {
            return this.m_rootCategories;
        }
        if (fieldName.equals("currentCategory")) {
            return this.m_currentCategory;
        }
        if (fieldName.equals("parentCategory")) {
            return (this.m_currentCategory != null) ? this.m_currentCategory.getParent() : null;
        }
        if (fieldName.equals("articles")) {
            return this.m_articles;
        }
        if (fieldName.equals("currentPage")) {
            return this.m_currentPage;
        }
        if (fieldName.equals("totalPages")) {
            return this.m_totalPages;
        }
        if (fieldName.equals("ready")) {
            return !this.m_processing;
        }
        if (!fieldName.equals("pagesDescription")) {
            return null;
        }
        if (this.m_totalPages != 0) {
            return this.m_currentPage + "/" + this.m_totalPages;
        }
        return WakfuTranslator.getInstance().getString("marketBoard.noResults");
    }
    
    private void updateArticle(final int articleId, final GameDateConst date, final int stocks) {
        for (int i = 0, size = this.m_articles.size(); i < size; ++i) {
            final Article article = this.m_articles.get(i);
            if (article.getId() == articleId) {
                article.update(date, stocks);
            }
        }
        for (int i = 0, size = this.m_homeArticles.size(); i < size; ++i) {
            final Article article = this.m_homeArticles.get(i);
            if (article.getId() == articleId) {
                article.update(date, stocks);
            }
        }
    }
    
    public void refreshOgrines() {
        MoneyLoader.INSTANCE.getMoney(new MoneyListener() {
            @Override
            public void onMoney(final int ogrins, final int krosz) {
                WebShopSession.this.updateWallet(ogrins, krosz);
            }
            
            @Override
            public void onError() {
            }
        });
    }
    
    public void refreshTimedAndStockArticles() {
        final TIntArrayList list = new TIntArrayList();
        for (int i = 0, size = this.m_articles.size(); i < size; ++i) {
            final Article article = this.m_articles.get(i);
            if (article.isTimed() || article.getStock() != -1) {
                list.add(article.getId());
            }
        }
        for (int i = 0, size = this.m_homeArticles.size(); i < size; ++i) {
            final Article article = this.m_homeArticles.get(i);
            if (article.isTimed() || article.getStock() != -1) {
                list.add(article.getId());
            }
        }
        if (list.isEmpty()) {
            return;
        }
        ArticlesIdsLoader.INSTANCE.getArticlesByIds(list, new ArticlesIdsListener() {
            @Override
            public void onArticlesIds(final ArrayList<Article> articles) {
                for (int i = 0, size = articles.size(); i < size; ++i) {
                    final Article article = articles.get(i);
                    WebShopSession.this.updateArticle(article.getId(), article.getEndDate(), article.getStock());
                }
            }
            
            @Override
            public void onError() {
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)WebShopSession.class);
    }
    
    public enum ContentType
    {
        CATEGORY, 
        GONDOLA_HEAD;
    }
    
    public enum Mode
    {
        LOADING((byte)0), 
        HOME((byte)1), 
        ARTICLES((byte)2);
        
        private final byte m_id;
        
        private Mode(final byte id) {
            this.m_id = id;
        }
        
        public byte getId() {
            return this.m_id;
        }
    }
    
    private abstract static class ArticlesProvider
    {
        private int m_currentPage;
        protected ArticlesListener m_listener;
        
        protected ArticlesProvider(final int currentPage, final ArticlesListener listener) {
            super();
            this.m_currentPage = currentPage;
            this.m_listener = listener;
        }
        
        private int getCurrentPage() {
            return this.m_currentPage;
        }
        
        private void setCurrentPage(final int currentPage) {
            this.m_currentPage = currentPage;
        }
        
        private void setListener(final ArticlesListener listener) {
            this.m_listener = listener;
        }
        
        public abstract ArrayList<Article> getArticles();
        
        public abstract int getCount();
        
        public abstract boolean isReady();
        
        public void clean() {
            this.m_listener = null;
        }
    }
    
    protected static class ArticleListProvider extends ArticlesProvider
    {
        private final ArrayList<Article> m_myArticles;
        private int m_count;
        private boolean m_isReady;
        
        protected ArticleListProvider(final int categoryId, final int currentPage, final ArticlesListener listener) {
            super(currentPage, listener);
            this.m_myArticles = new ArrayList<Article>();
            this.m_isReady = false;
            ArticlesListLoader.INSTANCE.getArticlesList(categoryId, currentPage, 12, new ArticlesListListener() {
                @Override
                public void onArticlesList(final ArrayList<Article> articles, final int count) {
                    ArticleListProvider.this.m_myArticles.addAll(articles);
                    ArticleListProvider.this.m_count = count;
                    ArticleListProvider.this.m_isReady = true;
                    if (ArticleListProvider.this.m_listener != null) {
                        ArticleListProvider.this.m_listener.onArticlesList(ArticleListProvider.this.m_myArticles, count);
                    }
                }
                
                @Override
                public void onError() {
                    ArticleListProvider.this.m_isReady = true;
                    if (ArticleListProvider.this.m_listener != null) {
                        ArticleListProvider.this.m_listener.onError();
                    }
                }
            });
        }
        
        @Override
        public ArrayList<Article> getArticles() {
            return this.m_myArticles;
        }
        
        @Override
        public int getCount() {
            return this.m_count;
        }
        
        @Override
        public boolean isReady() {
            return this.m_isReady;
        }
    }
    
    protected static class ArticleSearchProvider extends ArticlesProvider
    {
        private final ArrayList<Article> m_myArticles;
        private int m_count;
        private boolean m_isReady;
        
        protected ArticleSearchProvider(final TIntArrayList categoryId, final String search, final int currentPage, final ArticlesListener listener) {
            super(currentPage, listener);
            this.m_myArticles = new ArrayList<Article>();
            this.m_isReady = false;
            ArticlesSearchLoader.INSTANCE.getArticlesSearch(categoryId, search, currentPage, 12, new ArticlesSearchListener() {
                @Override
                public void onArticlesSearch(final ArrayList<Article> articles, final int count) {
                    ArticleSearchProvider.this.m_myArticles.addAll(articles);
                    ArticleSearchProvider.this.m_count = count;
                    ArticleSearchProvider.this.m_isReady = true;
                    if (ArticleSearchProvider.this.m_listener != null) {
                        ArticleSearchProvider.this.m_listener.onArticlesList(articles, count);
                    }
                }
                
                @Override
                public void onError() {
                    ArticleSearchProvider.this.m_isReady = true;
                    if (ArticleSearchProvider.this.m_listener != null) {
                        ArticleSearchProvider.this.m_listener.onError();
                    }
                }
            });
        }
        
        @Override
        public ArrayList<Article> getArticles() {
            return this.m_myArticles;
        }
        
        @Override
        public int getCount() {
            return this.m_count;
        }
        
        @Override
        public boolean isReady() {
            return this.m_isReady;
        }
    }
    
    public static class GondolaHeadListArticlesProvider extends ArticlesProvider
    {
        private final ArrayList<Article> m_myArticles;
        private final int m_maxArticles;
        private int m_count;
        private boolean m_isReady;
        
        public GondolaHeadListArticlesProvider(final int gondolaId, final int maxArticles, final int currentPage, final ArticlesListener listener) {
            super(currentPage, listener);
            this.m_myArticles = new ArrayList<Article>();
            this.m_isReady = false;
            this.m_maxArticles = maxArticles;
            GondolaHeadArticlesLoader.INSTANCE.getArticlesList(gondolaId, currentPage, maxArticles, new ArticlesListListener() {
                @Override
                public void onArticlesList(final ArrayList<Article> articles, final int count) {
                    GondolaHeadListArticlesProvider.this.m_myArticles.addAll(articles);
                    GondolaHeadListArticlesProvider.this.m_count = count;
                    GondolaHeadListArticlesProvider.this.m_isReady = true;
                    if (GondolaHeadListArticlesProvider.this.m_listener != null) {
                        GondolaHeadListArticlesProvider.this.m_listener.onArticlesList(articles, count);
                    }
                }
                
                @Override
                public void onError() {
                    GondolaHeadListArticlesProvider.this.m_isReady = true;
                    if (GondolaHeadListArticlesProvider.this.m_listener != null) {
                        GondolaHeadListArticlesProvider.this.m_listener.onError();
                    }
                }
            });
        }
        
        @Override
        public ArrayList<Article> getArticles() {
            return this.m_myArticles;
        }
        
        @Override
        public int getCount() {
            return this.m_count;
        }
        
        @Override
        public boolean isReady() {
            return this.m_isReady;
        }
    }
    
    public interface ArticlesListener
    {
        void onArticlesList(ArrayList<Article> p0, int p1);
        
        void onError();
    }
}
