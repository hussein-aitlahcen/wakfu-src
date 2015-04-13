package com.ankamagames.wakfu.client.core.game.soap.shop;

import java.util.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.framework.net.download.*;
import java.io.*;

public class ImageDataHelper
{
    public static ImageData findImageData(final ArrayList<ImageData> list, final int width, final int height) {
        for (int i = 0, size = list.size(); i < size; ++i) {
            final ImageData data = list.get(i);
            if (data.getWidth() == width && data.getHeight() == height) {
                return data;
            }
        }
        return list.isEmpty() ? null : list.get(0);
    }
    
    public static DownloadableFieldProvider load(final ImageData imageData, final DownloadableFieldProviderListener listener, final String field) {
        final File file = WebShopFileHelper.getCachedFilePathFromRemoteUrl(imageData.getUrl());
        final String cachedFileUrl = WebShopFileHelper.fileToURL(file);
        final DownloadableFieldProvider provider = new DownloadableFieldProvider(null, cachedFileUrl, field, listener);
        WebShopDataDownloader.INSTANCE.add(imageData.getUrl(), provider);
        return provider;
    }
}
