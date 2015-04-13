package com.ankamagames.framework.fileFormat.news;

import gnu.trove.*;
import java.net.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;

public class VideoElement extends NewsElement
{
    private final TIntObjectHashMap<URL> m_streamsPerQuality;
    private NewsImage m_prePlayImage;
    private boolean m_fullscreenAllowed;
    
    public VideoElement(@NotNull final Rect area) {
        super(NewsElementType.VIDEO, area);
        this.m_streamsPerQuality = new TIntObjectHashMap<URL>();
        this.m_prePlayImage = null;
        this.m_fullscreenAllowed = true;
    }
    
    public boolean isFullscreenAllowed() {
        return this.m_fullscreenAllowed;
    }
    
    public NewsImage getPrePlayImage() {
        return this.m_prePlayImage;
    }
    
    public URL getStream(final int quality) {
        return this.m_streamsPerQuality.get(quality);
    }
    
    public int getStreamsCount() {
        return this.m_streamsPerQuality.size();
    }
    
    public int[] getAvailableQualities() {
        return this.m_streamsPerQuality.keys();
    }
    
    void registerStream(@NotNull final URL streamUrl, final int quality) {
        this.m_streamsPerQuality.put(quality, streamUrl);
    }
    
    void setPrePlayImage(final NewsImage prePlayImage) {
        this.m_prePlayImage = prePlayImage;
    }
    
    void setFullscreenAllowed(final boolean fullscreenAllowed) {
        this.m_fullscreenAllowed = fullscreenAllowed;
    }
}
