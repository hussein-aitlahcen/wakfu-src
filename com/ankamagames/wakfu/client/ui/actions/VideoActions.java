package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.component.*;

@XulorActionsTag
public class VideoActions
{
    public static final String PACKAGE = "wakfu.video";
    
    public static void play(final Event e, final VideoWidget videoWidget, final Button playButton) {
        if (!videoWidget.isInitialized() || videoWidget.isPaused()) {
            playButton.setText(WakfuTranslator.getInstance().getString("dialog.video.pause"));
            videoWidget.play();
            videoWidget.setPaused(false);
        }
        else {
            playButton.setText(WakfuTranslator.getInstance().getString("dialog.video.play"));
            videoWidget.setPaused(true);
        }
    }
    
    public static void updateSlider(final ValueChangedEvent e, final VideoWidget videoWidget, final Slider timeSlider) {
        final float videoDuration = videoWidget.getVideoDuration();
        final float newPosition = (long)e.getValue();
        timeSlider.setValue(newPosition / videoDuration);
    }
    
    public static void seek(final MouseEvent e, final VideoWidget videoWidget, final Slider slider) {
        videoWidget.setPaused(true);
        if (e.getButton() != 0) {
            final float sliderPosition = slider.getValue();
            videoWidget.seek(sliderPosition);
            final ValueChangedEvent changedEvent = new ValueChangedEvent(slider);
            changedEvent.setValue((long)(sliderPosition * videoWidget.getVideoDuration()));
            updateSlider(changedEvent, videoWidget, slider);
            videoWidget.play();
            videoWidget.setPaused(false);
        }
    }
}
