package com.ankamagames.wakfu.client.core.game.gift;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class GiftPackage extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String TITLE_FIELD = "title";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String GIFTS = "gifts";
    public static final String ICON_URL = "iconUrl";
    public static final String GUID = "guid";
    public static final String[] FIELDS;
    private String m_titleGuid;
    private String m_messageGuid;
    private final ArrayList<Gift> m_gifts;
    private byte m_contentType;
    
    public GiftPackage() {
        super();
        this.m_contentType = 1;
        this.m_gifts = new ArrayList<Gift>();
    }
    
    public void fromRaw(final RawGiftPackage rawGiftPackage) {
        this.m_titleGuid = rawGiftPackage.title;
        this.m_messageGuid = rawGiftPackage.message;
        final ArrayList<RawGiftPackage.Content> contents = rawGiftPackage.contents;
        for (final RawGiftPackage.Content content : contents) {
            final RawGiftItem rawGift = content.giftItem;
            Gift gift;
            try {
                gift = GiftItem.fromRaw(rawGift);
            }
            catch (GiftException e) {
                GiftPackage.m_logger.error((Object)("Impossible de cr\u00e9er le gift " + rawGift), (Throwable)e);
                gift = new FakeGift();
            }
            this.m_gifts.add(gift);
        }
    }
    
    @Override
    public String[] getFields() {
        return GiftPackage.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("title")) {
            return this.m_titleGuid;
        }
        if (fieldName.equals("description")) {
            return this.m_messageGuid;
        }
        if (fieldName.equals("gifts")) {
            return this.m_gifts;
        }
        if (fieldName.equals("iconUrl")) {
            return WakfuConfiguration.getInstance().getIconUrl("giftTypeIconPath", "defaultIconPath", this.m_contentType);
        }
        if (fieldName.equals("guid")) {
            return this.m_titleGuid;
        }
        return null;
    }
    
    public boolean consume(final Gift gift, final boolean all) {
        final int index = this.m_gifts.indexOf(gift);
        if (index == -1) {
            return false;
        }
        if (gift.consume(all)) {
            this.m_gifts.remove(gift);
        }
        if (this.m_gifts.size() != 0) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this, "gifts");
        }
        return this.m_gifts.size() == 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GiftPackage.class);
        FIELDS = new String[] { "title", "description", "gifts" };
    }
}
