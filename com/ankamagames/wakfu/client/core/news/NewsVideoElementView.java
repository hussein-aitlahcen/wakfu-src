package com.ankamagames.wakfu.client.core.news;

import org.apache.log4j.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.video.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.framework.fileFormat.news.*;
import java.net.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.core.event.*;
import java.util.*;

public class NewsVideoElementView extends NewsElementView
{
    private static final Logger m_logger;
    public static final String VIDEO_PLAYING_FIELD = "videoPlaying";
    public static final String VIDEO_MUTED_FIELD = "videoMuted";
    public static final String VIDEO_SOUND_VOLUME_VALUE_FIELD = "videoSoundVolumeValue";
    public static final String VIDEO_SOUND_VOLUME_TEXT_FIELD = "videoSoundVolumeText";
    public static final String VIDEO_PROGRESSION_VALUE_FIELD = "videoProgressionValue";
    public static final String VIDEO_CURRENT_TIME_TEXT_FIELD = "videoCurrentTimeText";
    public static final String VIDEO_TOTAL_TIME_TEXT_FIELD = "videoTotalTimeText";
    public static final String AVAILABLE_QUALITIES_FIELD = "availableQualities";
    public static final String SELECTED_QUALITY_FIELD = "selectedQuality";
    public static final String[] FIELDS;
    private VideoWidget m_videoWidget;
    private float m_currentVideoValue;
    private ArrayList<VideoQualityView> m_availableQualities;
    private EventListener m_timeChangedListener;
    private VideoQualityView m_selectedVideoQuality;
    private Image m_image;
    
    public NewsVideoElementView(final NewsElement newsElement) {
        super(newsElement);
        this.m_availableQualities = new ArrayList<VideoQualityView>();
        this.m_timeChangedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                NewsVideoElementView.this.m_currentVideoValue = (long)((ValueChangedEvent)event).getValue();
                PropertiesProvider.getInstance().firePropertyValueChanged(NewsVideoElementView.this, "videoProgressionValue", "videoCurrentTimeText", "videoTotalTimeText");
                return false;
            }
        };
        final int[] availableQualities = ((VideoElement)newsElement).getAvailableQualities();
        for (int i = 0; i < availableQualities.length; ++i) {
            this.m_availableQualities.add(new VideoQualityView(availableQualities[i]));
        }
        Collections.sort(this.m_availableQualities, new Comparator<VideoQualityView>() {
            @Override
            public int compare(final VideoQualityView o1, final VideoQualityView o2) {
                return o2.getQuality() - o1.getQuality();
            }
        });
        this.m_selectedVideoQuality = this.m_availableQualities.get(this.m_availableQualities.size() - 1);
    }
    
    @Override
    public String[] getFields() {
        return NewsVideoElementView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("videoPlaying")) {
            return this.isVideoPlaying();
        }
        if (fieldName.equals("videoProgressionValue")) {
            if (!this.m_videoWidget.isInitialized()) {
                return 0.0f;
            }
            return this.m_currentVideoValue / this.m_videoWidget.getVideoDuration();
        }
        else if (fieldName.equals("videoCurrentTimeText")) {
            if (!this.m_videoWidget.isInitialized()) {
                return formatTime(0L);
            }
            return formatTime((long)this.m_currentVideoValue);
        }
        else if (fieldName.equals("videoTotalTimeText")) {
            if (!this.m_videoWidget.isInitialized()) {
                return formatTime(0L);
            }
            return formatTime(this.m_videoWidget.getVideoDuration());
        }
        else {
            if (fieldName.equals("videoSoundVolumeValue")) {
                return this.m_videoWidget.getVolume();
            }
            if (fieldName.equals("videoSoundVolumeText")) {
                return (int)(this.m_videoWidget.getVolume() / 1.0f * 100.0f) + "%";
            }
            if (fieldName.equals("videoMuted")) {
                return this.isMuted();
            }
            if (fieldName.equals("availableQualities")) {
                return this.m_availableQualities;
            }
            if (fieldName.equals("selectedQuality")) {
                return this.m_selectedVideoQuality;
            }
            return null;
        }
    }
    
    public boolean isVideoPlaying() {
        return this.m_videoWidget.isInitialized() && !this.m_videoWidget.isPaused();
    }
    
    public static String formatTime(long time) {
        time /= 1000L;
        final long minutes = time / 60L;
        final long seconds = time - 60L * minutes;
        return String.format("%d:%s", minutes, (seconds > 9L) ? seconds : ("0" + seconds));
    }
    
    @Override
    public void togglePlayPauseVideo() {
        if (!this.m_videoWidget.isInitialized()) {
            if (this.m_image != null) {
                this.m_image.setVisible(false);
            }
            this.playVideo();
            this.m_videoWidget.addVideoEventListener(new VideoEventListener() {
                @Override
                public void onVideoOutput() {
                }
                
                @Override
                public void onVideoEnd() {
                    ProcessScheduler.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            UIAuthentificationFrame.getInstance().getNewsDisplayer().resetCurrentNew();
                        }
                    });
                }
                
                @Override
                public void onVideoStopped() {
                }
                
                @Override
                public void onBuffering(final float buffering) {
                }
            });
            return;
        }
        this.m_videoWidget.setPaused(!this.m_videoWidget.isPaused());
    }
    
    @Override
    public Container build(final Container newsContainer, final NewsDisplayer newsDisplayer) {
        final Container container = super.build(newsContainer, newsDisplayer);
        final VideoElement ve = (VideoElement)this.m_newsElement;
        this.addVideo(container, ve);
        final NewsImage prePlayImage = ve.getPrePlayImage();
        if (prePlayImage != null) {
            try {
                final URL url = prePlayImage.getFile().toURL();
                final Pixmap pixmap = new Pixmap(TextureLoader.getInstance().loadTexture(url));
                final PixmapElement pixmapElement = new PixmapElement();
                pixmapElement.onCheckOut();
                pixmapElement.setPixmap(pixmap);
                (this.m_image = new Image()).onCheckOut();
                this.m_image.setNonBlocking(true);
                this.m_image.setExpandable(false);
                this.m_image.add(pixmapElement);
                this.m_image.onChildrenAdded();
                this.m_image.computeMinSize();
                this.m_image.setSizeToPrefSize();
                final StaticLayoutData sld3 = new StaticLayoutData();
                sld3.onCheckOut();
                sld3.setSize(new Dimension(100.0f, 100.0f));
                sld3.setAlign(Alignment17.CENTER);
                this.m_image.setLayoutData(sld3);
                container.add(this.m_image);
                container.onChildrenAdded();
            }
            catch (MalformedURLException e) {
                NewsVideoElementView.m_logger.warn((Object)("URL malform\u00e9e : \"" + prePlayImage.getFile() + "\""));
            }
        }
        container.onChildrenAdded();
        return container;
    }
    
    private void addVideo(final Container container, final VideoElement ve) {
        if (this.m_videoWidget != null) {
            this.m_videoWidget.destroySelfFromParent();
        }
        (this.m_videoWidget = VideoWidget.checkOut()).setKeepRatio(false);
        final URL stream = ve.getStream(this.m_selectedVideoQuality.getQuality());
        if (!URLUtils.urlExists(stream)) {
            NewsVideoElementView.m_logger.error((Object)("Impossible de retrouver la vid\u00e9o dans cette qualit\u00e9 " + this.m_selectedVideoQuality));
            return;
        }
        this.m_videoWidget.setVideoPath(stream.toExternalForm());
        container.add(this.m_videoWidget);
        container.onChildrenAdded();
        this.m_videoWidget.setPrefSize(new Dimension(ve.getArea().width(), ve.getArea().height()));
        this.m_videoWidget.addEventListener(Events.VALUE_CHANGED, this.m_timeChangedListener, false);
        final StaticLayoutData sld2 = new StaticLayoutData();
        sld2.onCheckOut();
        sld2.setSize(new Dimension(ve.getArea().width(), ve.getArea().height()));
        sld2.setAlign(Alignment17.CENTER);
        this.m_videoWidget.setLayoutData(sld2);
    }
    
    public VideoWidget getVideoWidget() {
        return this.m_videoWidget;
    }
    
    @Override
    public void destroyAllWidgets() {
        super.destroyAllWidgets();
    }
    
    public void setVideoVolume(final float videoVolume) {
        this.m_videoWidget.setVolume(videoVolume);
    }
    
    public void seek(final float positionInPercent) {
        this.m_videoWidget.seek(positionInPercent);
    }
    
    public void setSelectedQuality(final Container newsContainer, final int value) {
        final boolean videoPlaying = this.isVideoPlaying();
        for (final VideoQualityView videoQualityView : this.m_availableQualities) {
            if (videoQualityView.getQuality() == value) {
                this.m_selectedVideoQuality = videoQualityView;
            }
        }
        this.addVideo(newsContainer, (VideoElement)this.m_newsElement);
        if (videoPlaying) {
            this.playVideo();
        }
    }
    
    private void playVideo() {
        this.m_videoWidget.play();
    }
    
    public boolean isMuted() {
        return this.m_videoWidget.isMuted();
    }
    
    public void setMuted(final boolean muted) {
        this.m_videoWidget.mute(muted);
    }
    
    static {
        m_logger = Logger.getLogger((Class)NewsVideoElementView.class);
        FIELDS = new String[] { "videoPlaying", "videoMuted", "videoSoundVolumeValue", "videoSoundVolumeText", "videoProgressionValue", "videoCurrentTimeText", "videoTotalTimeText", "availableQualities", "selectedQuality" };
    }
}
