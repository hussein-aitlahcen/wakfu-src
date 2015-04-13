package com.ankamagames.framework.fileFormat.news;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.translator.*;
import org.json.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import java.net.*;
import java.text.*;

class NewsJSONParser
{
    public static final Logger m_logger;
    private static final String KEY_CHANNEL_LANGUAGE = "language";
    private static final String KEY_CHANNEL_PUBLICATION_DATE = "pub_date";
    private static final String KEY_CHANNEL_ITEMS = "items";
    private static final String KEY_ITEM_GUID = "guid";
    private static final String KEY_ITEM_PUBLICATION_DATE = "pub_date";
    private static final String KEY_ITEM_TITLE = "title";
    private static final String KEY_ITEM_DESCRIPTION = "description";
    private static final String KEY_ITEM_LINK = "link";
    private static final String KEY_ITEM_CONTEXTS = "context";
    private static final String KEY_ITEM_PRIORITY = "priority";
    private static final String KEY_ITEM_TYPE = "type";
    private static final String KEY_ITEM_TEXTFIELDS = "annotations";
    private static final String KEY_ELEMENT_TIMING = "timing";
    private static final String KEY_TEXTFIELD_AREA = "area";
    private static final String KEY_TEXTFIELD_TEXT = "text";
    private static final String KEY_TEXTFIELD_TRIGGERING_AREA = "mouse_area";
    private static final String KEY_TEXTFIELD_BACKGROUND = "background";
    private static final String KEY_TEXTFIELD_BACKGROUND_OVER = "background_over";
    private static final String KEY_TEXTFIELD_LINK = "link";
    private static final String KEY_BACKGROUND_COLOR = "color";
    private static final String KEY_BACKGROUND_IMAGE = "image";
    private static final String KEY_IMAGE_SRC = "source";
    private static final String KEY_IMAGE_GUID = "guid";
    private static final String KEY_IMAGE_LAST_MODIFIED = "last_modif";
    private static final String KEY_IMAGE_BACKGROUND_IMAGE = "image";
    private static final String KEY_VIDEO_AREA = "area";
    private static final String KEY_VIDEO_STREAMS = "source";
    private static final String KEY_VIDEO_BACKGROUND = "background";
    private static final String KEY_VIDEO_PRE_IMAGE = "image";
    private static final String KEY_VIDEO_FULLSCREEN_ALLOWED = "allow_fullscreen";
    private static final String KEY_VIDEOSTREAM_URL = "url";
    private static final String KEY_VIDEOSTREAM_QUALITY = "quality";
    private static final SimpleDateFormat[] DATE_FORMATS;
    
    public static NewsChannel parse(@NotNull final String document, @NotNull final NewsImageFactory imageFactory) throws JSONException {
        final JSONObject mainObject = new JSONObject(document);
        final String rawLanguage = mainObject.getString("language");
        final Language language = Language.getLanguage(rawLanguage);
        final String rawPublicationDate = mainObject.getString("pub_date");
        final Date publicationDate = parseDate(rawPublicationDate);
        final NewsChannel channel = new NewsChannel(language, publicationDate);
        final JSONArray items = mainObject.getJSONArray("items");
        for (int i = 0; i < items.length(); ++i) {
            try {
                final JSONObject itemObject = items.getJSONObject(i);
                final NewsItem item = parseItem(itemObject, imageFactory);
                if (item != null) {
                    channel.addItem(item);
                }
            }
            catch (JSONException e) {
                NewsJSONParser.m_logger.error((Object)"Error while parsing item", (Throwable)e);
            }
        }
        return channel;
    }
    
    @Nullable
    private static NewsImage parseImage(@Nullable final JSONObject imageObject, @NotNull final NewsImageFactory imageFactory) throws JSONException {
        if (imageObject == null) {
            return null;
        }
        final String guid = imageObject.getString("guid");
        final URL src = parseUrl(imageObject.getString("source"));
        final long lastModification = imageObject.getLong("last_modif");
        return imageFactory.createImage(src, guid, lastModification);
    }
    
    @Nullable
    private static NewsItem parseItem(@NotNull final JSONObject itemObject, @NotNull final NewsImageFactory imageFactory) throws JSONException {
        final String guid = itemObject.getString("guid");
        final NewsItemType type = parseEnum(itemObject.getString("type"), NewsItemType.class);
        final Date publicationDate = parseDate(itemObject.optString("pub_date"));
        final String title = itemObject.optString("title");
        final String description = itemObject.optString("description", null);
        final URL link = parseUrl(itemObject.optString("link", null));
        final int priority = itemObject.optInt("priority", 0);
        final JSONArray contexts = itemObject.getJSONArray("context");
        final NewsItem item = new NewsItem(guid);
        item.setPublicationDate(publicationDate);
        item.setTitle(title);
        item.setDescription(description);
        item.setLink(link);
        item.setPriority(priority);
        for (int i = 0; i < contexts.length(); ++i) {
            final NewsContext newsContext = parseEnum(contexts.optString(i), NewsContext.class);
            if (newsContext != null) {
                item.addContext(newsContext);
            }
        }
        final JSONArray textfieldsArray = itemObject.optJSONArray("annotations");
        parseTextFields(item, textfieldsArray, imageFactory);
        if (type == NewsItemType.VIDEO) {
            final NewsElementBackground background = parseBackground(itemObject.optJSONObject("background"), imageFactory);
            item.setBackground(background);
            final Rect videoArea = parseRect(itemObject.getJSONArray("area"));
            if (videoArea == null) {
                NewsJSONParser.m_logger.error((Object)("Unable to find a valid position for the video (key 'area' : " + itemObject));
                return null;
            }
            final NewsElementTiming timing = parseTiming(itemObject.optJSONArray("timing"));
            final NewsImage prePlayImage = parseImage(itemObject.optJSONObject("image"), imageFactory);
            final boolean fullScreenAllowed = itemObject.optBoolean("allow_fullscreen", true);
            final VideoElement videoElement = new VideoElement(videoArea);
            videoElement.setTiming(timing);
            videoElement.setFullscreenAllowed(fullScreenAllowed);
            videoElement.setPrePlayImage(prePlayImage);
            final JSONArray streamsArray = itemObject.getJSONArray("source");
            parseStreams(videoElement, streamsArray);
            if (videoElement.getStreamsCount() == 0) {
                NewsJSONParser.m_logger.error((Object)("No valid stream found for video. Bypassing element " + itemObject));
                return null;
            }
            item.addElement(videoElement);
        }
        else if (type == NewsItemType.IMAGE) {
            final NewsImage newsImage = parseImage(itemObject.getJSONObject("image"), imageFactory);
            item.setBackground(new NewsElementBackground(null, newsImage));
        }
        return item;
    }
    
    private static void parseStreams(@NotNull final VideoElement videoElement, @Nullable final JSONArray streamsArray) {
        if (streamsArray == null) {
            return;
        }
        for (int i = 0; i < streamsArray.length(); ++i) {
            try {
                final JSONObject streamObject = streamsArray.getJSONObject(i);
                final URL url = parseUrl(streamObject.getString("url"));
                if (url == null) {
                    NewsJSONParser.m_logger.error((Object)("Unknown url for stream " + streamObject));
                }
                else {
                    final int quality = streamObject.getInt("quality");
                    videoElement.registerStream(url, quality);
                }
            }
            catch (JSONException e) {
                NewsJSONParser.m_logger.error((Object)("Malformed video streams : " + streamsArray), (Throwable)e);
            }
        }
    }
    
    private static void parseTextFields(@NotNull final NewsItem item, @Nullable final JSONArray textfieldsArray, @NotNull final NewsImageFactory imageFactory) {
        if (textfieldsArray == null) {
            return;
        }
        for (int i = 0; i < textfieldsArray.length(); ++i) {
            try {
                final JSONObject textObject = textfieldsArray.getJSONObject(i);
                final Rect area = parseRect(textObject.getJSONArray("area"));
                if (area == null) {
                    NewsJSONParser.m_logger.error((Object)("Unknown area for textfield " + textObject));
                }
                else {
                    final String text = textObject.optString("text");
                    final Rect triggeringArea = parseRect(textObject.optJSONArray("mouse_area"));
                    final NewsElementBackground background = parseBackground(textObject.optJSONObject("background"), imageFactory);
                    final NewsElementBackground backgroundOver = parseBackground(textObject.optJSONObject("background_over"), imageFactory);
                    final URL link = parseUrl(textObject.optString("link"));
                    final NewsElementTiming timing = parseTiming(textObject.optJSONArray("timing"));
                    final TextElement textElement = new TextElement(area, text);
                    textElement.setBackground(background);
                    textElement.setBackgroundOver(backgroundOver);
                    textElement.setLink(link);
                    textElement.setTiming(timing);
                    textElement.setTriggeringArea(triggeringArea);
                    item.addElement(textElement);
                }
            }
            catch (JSONException e) {
                NewsJSONParser.m_logger.error((Object)("Malformed annotations at index " + i + " in " + textfieldsArray), (Throwable)e);
            }
        }
    }
    
    @NotNull
    private static NewsElementTiming parseTiming(final JSONArray timingObject) {
        if (timingObject == null) {
            return NewsElementTiming.ALWAYS;
        }
        final int length = timingObject.length();
        if (length < 2) {
            NewsJSONParser.m_logger.error((Object)("Malformed timing : " + timingObject.toString()));
            return NewsElementTiming.ALWAYS;
        }
        try {
            return NewsElementTiming.create(timingObject.getInt(0), timingObject.getInt(1));
        }
        catch (JSONException e) {
            NewsJSONParser.m_logger.error((Object)("Malformed timing : " + timingObject.toString()), (Throwable)e);
            return NewsElementTiming.ALWAYS;
        }
    }
    
    @Nullable
    private static Color parseColor(final String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        if (!s.startsWith("#")) {
            NewsJSONParser.m_logger.error((Object)("Malformated color '" + s + "'"));
            return null;
        }
        final int length = s.length();
        if (length != 7 && length != 9) {
            NewsJSONParser.m_logger.error((Object)("Malformated color '" + s + "'"));
            return null;
        }
        return Color.getRGBAFromHex(s);
    }
    
    @Nullable
    private static NewsElementBackground parseBackground(final JSONObject backgroundObject, @NotNull final NewsImageFactory imageFactory) throws JSONException {
        if (backgroundObject == null) {
            return null;
        }
        final Color color = parseColor(backgroundObject.optString("color"));
        final NewsImage image = parseImage(backgroundObject.optJSONObject("image"), imageFactory);
        return (color == null && image == null) ? null : new NewsElementBackground(color, image);
    }
    
    @Nullable
    private static <E extends Enum<E>> E parseEnum(final String s, final Class<E> enumClass) {
        if (s == null) {
            return null;
        }
        E value = null;
        try {
            value = Enum.valueOf(enumClass, s);
        }
        catch (Exception ex) {}
        if (value == null) {
            try {
                value = Enum.valueOf(enumClass, s.toUpperCase(Locale.US));
            }
            catch (Exception ex2) {}
        }
        return value;
    }
    
    @Nullable
    private static Rect parseRect(final JSONArray arrayOfValues) throws JSONException {
        if (arrayOfValues == null) {
            return null;
        }
        if (arrayOfValues.length() < 4) {
            NewsJSONParser.m_logger.error((Object)("Malformed JSON Rect : 4 values expected, " + arrayOfValues.length() + " found : " + arrayOfValues));
            return null;
        }
        final int x = arrayOfValues.getInt(0);
        final int y = arrayOfValues.getInt(1);
        final int width = arrayOfValues.getInt(2);
        final int height = arrayOfValues.getInt(3);
        return new Rect(x, x + width, y, y + height);
    }
    
    @Nullable
    private static URL parseUrl(final String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        try {
            return new URL(s);
        }
        catch (MalformedURLException e) {
            NewsJSONParser.m_logger.error((Object)("Error while parsing news items : " + e.getMessage() + " '" + s + '\''));
            return null;
        }
    }
    
    @Nullable
    private static Date parseDate(final String s) {
        if (s == null) {
            return null;
        }
        for (int i = 0; i < NewsJSONParser.DATE_FORMATS.length; ++i) {
            try {
                final Date date = NewsJSONParser.DATE_FORMATS[i].parse(s);
                if (date != null) {
                    return date;
                }
            }
            catch (ParseException ex) {}
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NewsJSONParser.class);
        DATE_FORMATS = new SimpleDateFormat[] { new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.US), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US), new SimpleDateFormat("yyyy-MM-dd", Locale.US) };
    }
}
