package com.ankamagames.wakfu.client.core.game.soap.shop;

import org.apache.log4j.*;
import com.ankamagames.framework.net.soap.data.*;
import com.google.common.base.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.configuration.*;
import java.text.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import gnu.trove.*;
import java.util.*;
import org.jetbrains.annotations.*;

class ShopHelper
{
    private static final Logger m_logger;
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String DISPLAY_MODE = "displaymode";
    private static final String IMAGE = "image";
    private static final String CHILD = "child";
    private static final String LINK = "link";
    private static final String TYPE = "type";
    private static final String MODE = "mode";
    private static final String CATEGORIES = "categories";
    private static final String GONDOLAHEADS = "gondolaheads";
    private static final String HIGHLIGHTS = "hightlights";
    private static final String ARTICLES = "articles";
    public static final String WIP_SERVER_KEY = "WIP_";
    public static final String HIDDEN_CATEGORY = "hidden";
    
    static ArrayList<Article> createArticlesList(final Data d) {
        final ArrayList<Article> lists = new ArrayList<Article>();
        if (d.getDataType() == DataType.NIL) {
            return lists;
        }
        final MapData data = (MapData)d;
        final ArrayData articles = (ArrayData)data.getValue("articles");
        for (int i = 0, size = articles.size(); i < size; ++i) {
            final MapData articleData = (MapData)articles.getValue(i);
            final Article article = createArticle(articleData);
            if (article.isServerRestrictionOK()) {
                lists.add(article);
            }
        }
        return lists;
    }
    
    static ArrayList<Highlight> createHighlightsList(final Data d) {
        final ArrayList<Highlight> lists = new ArrayList<Highlight>();
        if (d.getDataType() == DataType.NIL) {
            return lists;
        }
        final MapData data = (MapData)d;
        final ArrayData highlights = (ArrayData)data.getValue("hightlights");
        for (int i = 0, size = highlights.size(); i < size; ++i) {
            final MapData categoryData = (MapData)highlights.getValue(i);
            final Optional<Highlight> highlight = createHighlight(categoryData);
            if (highlight.isPresent()) {
                lists.add((Highlight)highlight.get());
            }
        }
        return lists;
    }
    
    public static ArrayList<GondolaHead> createGondolaHeads(final MapData data) {
        final ArrayList<GondolaHead> lists = new ArrayList<GondolaHead>();
        final ArrayData gondolaHeads = (ArrayData)data.getValue("gondolaheads");
        for (int i = 0, size = gondolaHeads.size(); i < size; ++i) {
            final MapData categoryData = (MapData)gondolaHeads.getValue(i);
            lists.add(createGondolaHead(categoryData, false));
        }
        return lists;
    }
    
    static ArrayList<Category> createCategoryList(final MapData data) {
        final ArrayList<Category> lists = new ArrayList<Category>();
        final ArrayData categories = (ArrayData)data.getValue("categories");
        for (int i = 0, size = categories.size(); i < size; ++i) {
            final MapData categoryData = (MapData)categories.getValue(i);
            final Category category = createCategory(categoryData);
            if (category != null) {
                lists.add(category);
            }
        }
        return lists;
    }
    
    static GondolaHead createGondolaHead(final MapData root, final boolean highlights) {
        final int id = PrimitiveConverter.getInteger(root.getStringValue("id"));
        final String name = root.getStringValue("name");
        final String link = root.getStringValue("link");
        final String type = root.getStringValue("type");
        final ArrayList<ImageData> imageDataList = ImageData.extract((MapData)root.getValue("image"));
        return new GondolaHead(id, name, link, type, imageDataList, highlights ? 3 : 0);
    }
    
    static Optional<Highlight> createHighlight(final MapData root) {
        final int id = PrimitiveConverter.getInteger(root.getStringValue("id"));
        try {
            final String name = root.getStringValue("name");
            final String description = root.getStringValue("description");
            final String link = root.getStringValue("link");
            final String type = root.getStringValue("type");
            final String mode = root.getStringValue("mode");
            final ArrayList<ImageData> imageData = ImageData.extract((MapData)root.getValue("image"));
            final Data external = root.getValue("external");
            final Article article = (type.equals("ARTICLE") && external.getDataType() != DataType.NIL) ? createArticle((MapData)external) : null;
            final GondolaHead gondolaHead = (type.equals("GONDOLAHEAD") && external.getDataType() != DataType.NIL) ? createGondolaHead((MapData)external, true) : null;
            int categoryId;
            if (type.equals("CATEGORY")) {
                final MapData data = (MapData)external;
                categoryId = PrimitiveConverter.getInteger(data.getStringValue("id"));
            }
            else {
                categoryId = -1;
            }
            return (Optional<Highlight>)Optional.of((Object)new Highlight(id, name, description, link, type, mode, imageData, article, gondolaHead, categoryId));
        }
        catch (Exception e) {
            ShopHelper.m_logger.error((Object)("Highlight with id " + id + " can't be loaded"), (Throwable)e);
            return (Optional<Highlight>)Optional.absent();
        }
    }
    
    static Category createCategory(final MapData categoryRoot) {
        final int id = PrimitiveConverter.getInteger(categoryRoot.getStringValue("id"));
        final String name = categoryRoot.getStringValue("name");
        final String desc = categoryRoot.getStringValue("description");
        final Category.DisplayMode displayMode = Category.DisplayMode.getFromName(categoryRoot.getStringValue("displaymode"));
        final Data imageData = categoryRoot.getValue("image");
        final String image = (imageData.getDataType() == DataType.BOOLEAN) ? null : imageData.getStringValue();
        final String key = categoryRoot.getStringValue("key");
        final Category category = new Category(id, name, desc, image, displayMode, key);
        if (!isCategoryValid(category)) {
            return null;
        }
        final ArrayData children = (ArrayData)categoryRoot.getValue("child");
        if (children != null) {
            for (int i = 0, size = children.size(); i < size; ++i) {
                final MapData categoryData = (MapData)children.getValue(i);
                final Category child = createCategory(categoryData);
                if (child != null) {
                    category.addChild(child);
                }
            }
        }
        return category;
    }
    
    static boolean isCategoryValid(final Category category) {
        final String categoryKey = category.getKey();
        if (categoryKey == null) {
            return true;
        }
        if (categoryKey.startsWith("WIP_")) {
            final int serverId = WakfuGameEntity.getInstance().getServerId();
            final String serverKey = "WIP_" + serverId;
            return categoryKey.equals(serverKey);
        }
        return !categoryKey.equals("hidden");
    }
    
    public static Article createArticle(final MapData articleRoot) {
        final int id = PrimitiveConverter.getInteger(articleRoot.getStringValue("id"));
        final String name = extractString(articleRoot, "name");
        final String subtitle = extractString(articleRoot, "subtitle");
        final String desc = extractString(articleRoot, "description");
        final ArrayList<ImageData> images = ImageData.extract((MapData)articleRoot.getValue("image"));
        final float price = PrimitiveConverter.getFloat(extractString(articleRoot, "price"));
        final float originalPrice = PrimitiveConverter.getFloat(extractString(articleRoot, "original_price"));
        final Currency currency = Currency.getByApiName(extractString(articleRoot, "currency"));
        final int stock = PrimitiveConverter.getInteger(extractString(articleRoot, "stock"), -1);
        final GameDate endDate = extractDate(articleRoot, "enddate");
        final ArrayList<SubArticle> subArticles = new ArrayList<SubArticle>();
        final ArrayData references = (ArrayData)articleRoot.getValue("references");
        extractReferences(subArticles, references);
        final ArrayData promotionsArray = (ArrayData)articleRoot.getValue("promo");
        ArrayList<Promotion> promotions;
        if (promotionsArray != null) {
            promotions = new ArrayList<Promotion>();
            extractPromotions(promotions, promotionsArray);
        }
        else {
            promotions = null;
        }
        return new Article(id, name, desc, subtitle, price, originalPrice, currency, stock, subArticles, images, promotions, endDate);
    }
    
    private static GameDate extractDate(final MapData data, final String key) {
        final Data d = data.getValue(key);
        if (d.getDataType() == DataType.NIL) {
            return GameDate.getNullDate();
        }
        final String timeZoneString = SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.CALENDAR_TZ);
        final long deltaTime = SystemConfiguration.INSTANCE.getLongValue(SystemConfigurationType.CALENDAR_DELTA);
        final TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            final Date date = format.parse(d.getStringValue());
            final long time = date.getTime();
            return GameDate.fromJavaDate(new Date(time + timeZone.getRawOffset() + deltaTime));
        }
        catch (ParseException e) {
            return GameDate.getNullDate();
        }
    }
    
    private static String extractString(final MapData data, final String key) {
        final String value = data.getStringValue(key);
        if (value == null) {
            return "";
        }
        return TextUtils.unescapeHTML(value);
    }
    
    private static void extractPromotions(final Collection<Promotion> list, final ArrayData promotions) {
        for (int i = 0, size = promotions.size(); i < size; ++i) {
            final MapData mapData = (MapData)promotions.getValue(i);
            final String name = mapData.getStringValue("name");
            final String description = mapData.getStringValue("description");
            list.add(new Promotion(name, description));
        }
    }
    
    private static void extractReferences(final ArrayList<SubArticle> list, final ArrayData references) {
        for (int i = 0, size = references.size(); i < size; ++i) {
            final MapData reference = (MapData)references.getValue(i);
            final Data content = reference.getValue("content");
            final ArticleType type = ArticleType.getFromName(reference.getStringValue("type"));
            if (content != null) {
                switch (type) {
                    case ICE_GIFT:
                    case ICE_ITEM_GIFT: {
                        if (content.getDataType() == DataType.ARRAY) {
                            extractIceGiftItem(list, (ArrayData)content, type);
                            break;
                        }
                        break;
                    }
                    case VIRTUAL_SUBSCRIPTION_LEVEL: {
                        if (content.getDataType() == DataType.MAP) {
                            extractVirtualSubscriptionLevel(list, (MapData)content);
                            break;
                        }
                        break;
                    }
                    case ACCOUNT_STATUS: {
                        if (content.getDataType() == DataType.MAP) {
                            extractAccountStatusReference(list, (MapData)content);
                            break;
                        }
                        break;
                    }
                    case OGRINE: {
                        if (content.getDataType() == DataType.ARRAY) {
                            extractOrgineItem(list, (ArrayData)content);
                            break;
                        }
                        break;
                    }
                }
            }
        }
    }
    
    private static void extractAccountStatusReference(final Collection<SubArticle> list, final MapData content) {
        final SubArticle subArticle = new SubArticle(-1, ArticleType.ACCOUNT_STATUS);
        final String status = content.getStringValue("STATUS");
        final String type = content.getStringValue("TYPE");
        if (status != null) {
            subArticle.addData("STATUS", status);
        }
        if (type != null) {
            subArticle.addData("TYPE", type);
        }
        list.add(subArticle);
    }
    
    private static void extractVirtualSubscriptionLevel(final ArrayList<SubArticle> list, final MapData content) {
        final int contentId = PrimitiveConverter.getInteger(content.getStringValue("LEVEL"), -1);
        final SubArticle subArticle = new SubArticle(contentId, ArticleType.VIRTUAL_SUBSCRIPTION_LEVEL);
        subArticle.addData("server.id", content.getStringValue("SERVER"));
        list.add(subArticle);
    }
    
    private static void extractIceGiftItem(final ArrayList<SubArticle> list, final ArrayData content, final ArticleType type) {
        for (int i = 0, size = content.size(); i < size; ++i) {
            final MapData contentValue = (MapData)content.getValue(i);
            final int contentId = PrimitiveConverter.getInteger(contentValue.getStringValue("id"), -1);
            final SubArticle subArticle = new SubArticle(contentId, type);
            list.add(subArticle);
            final Data metaData = contentValue.getValue("metas");
            if (metaData != null && metaData.getDataType() == DataType.MAP) {
                final MapData metaMap = (MapData)metaData;
                metaMap.forEach(new TObjectObjectProcedure<String, Data>() {
                    @Override
                    public boolean execute(final String a, final Data b) {
                        subArticle.addData(a, b.getStringValue());
                        return true;
                    }
                });
            }
        }
    }
    
    private static void extractOrgineItem(final ArrayList<SubArticle> list, final ArrayData content) {
        for (int i = 0, size = content.size(); i < size; ++i) {
            final SubArticle subArticle = new SubArticle(-1, ArticleType.OGRINE);
            list.add(subArticle);
        }
    }
    
    @Nullable
    private static String extractImageUrl(final MapData articleRoot) {
        final Data imageData = articleRoot.getValue("image");
        final THashMap<Object, Data> imageMap = (THashMap<Object, Data>)imageData.getValue();
        final Collection<Data> values = imageMap.values();
        for (final Data data : values) {
            if (data.getDataType() == DataType.BOOLEAN) {
                continue;
            }
            return data.getStringValue();
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ShopHelper.class);
    }
}
