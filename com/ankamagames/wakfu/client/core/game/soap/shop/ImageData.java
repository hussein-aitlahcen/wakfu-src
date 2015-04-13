package com.ankamagames.wakfu.client.core.game.soap.shop;

import java.util.*;
import gnu.trove.*;
import com.ankamagames.framework.net.soap.data.*;
import com.ankamagames.framework.java.util.*;
import java.util.regex.*;

public class ImageData
{
    private static Pattern PATTERN;
    private final int m_width;
    private final int m_height;
    private final String m_url;
    
    private ImageData(final int width, final int height, final String url) {
        super();
        this.m_width = width;
        this.m_height = height;
        this.m_url = url;
    }
    
    public int getWidth() {
        return this.m_width;
    }
    
    public int getHeight() {
        return this.m_height;
    }
    
    public String getUrl() {
        return this.m_url;
    }
    
    public static ArrayList<ImageData> extract(final MapData data) {
        final ArrayList<ImageData> images = new ArrayList<ImageData>();
        data.forEach(new TObjectObjectProcedure<String, Data>() {
            @Override
            public boolean execute(final String a, final Data b) {
                final Matcher matcher = ImageData.PATTERN.matcher(a);
                if (matcher.matches()) {
                    if (b.getDataType() == DataType.BOOLEAN) {
                        return true;
                    }
                    final int width = PrimitiveConverter.getInteger(matcher.group(1));
                    final int height = PrimitiveConverter.getInteger(matcher.group(2));
                    final String url = b.getStringValue();
                    images.add(new ImageData(width, height, url, null));
                }
                return true;
            }
        });
        return images;
    }
    
    static {
        ImageData.PATTERN = Pattern.compile("([0-9]+)_([0-9]+)");
    }
}
