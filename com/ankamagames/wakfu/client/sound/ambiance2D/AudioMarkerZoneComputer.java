package com.ankamagames.wakfu.client.sound.ambiance2D;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import java.util.*;
import gnu.trove.*;

public class AudioMarkerZoneComputer
{
    private static final TIntHashSet m_registeredEvents;
    private static final TObjectProcedure<AudioMarker> LAUNCH_PROCEDURE;
    
    public static void execute() {
        if (!LocalPartitionManager.getInstance().isInit()) {
            return;
        }
        final IntIntLightWeightMap scores = createScores();
        final TIntIterator it = AudioMarkerZoneComputer.m_registeredEvents.iterator();
        while (it.hasNext()) {
            final int soundEventId = it.next();
            if (!scores.contains(soundEventId)) {
                WakfuSoundManager.getInstance().onEvent(new AudioMarkerSoundEvent(null, AudioMarkerType.getFromId(soundEventId), (short)0));
            }
            it.remove();
        }
        for (int i = 0, size = scores.size(); i < size; ++i) {
            AudioMarkerZoneComputer.m_registeredEvents.add(scores.getQuickKey(i));
            WakfuSoundManager.getInstance().onEvent(new AudioMarkerSoundEvent(null, AudioMarkerType.getFromId(scores.getQuickKey(i)), (short)scores.getQuickValue(i)));
        }
    }
    
    private static IntIntLightWeightMap createScores() {
        final IntIntLightWeightMap scores = new IntIntLightWeightMap();
        foreachVisibleAudioMarkerInPartitions(new TObjectProcedure<AudioMarker>() {
            @Override
            public boolean execute(final AudioMarker marker) {
                scores.put(marker.getAudioMarkerType(), scores.get(marker.getAudioMarkerType()) + 1);
                return true;
            }
        });
        return scores;
    }
    
    public static void launchLocalizedAudioMarkers() {
        if (!LocalPartitionManager.getInstance().isInit()) {
            return;
        }
        foreachVisibleAudioMarkerInPartitions(AudioMarkerZoneComputer.LAUNCH_PROCEDURE);
    }
    
    private static void foreachVisibleAudioMarkerInPartitions(final TObjectProcedure<AudioMarker> procedure) {
        final ArrayList<ClientMapInteractiveElement> elements = LocalPartitionManager.getInstance().getInteractiveElementsFromPartitions();
        for (int i = 0, size = elements.size(); i < size; ++i) {
            final ClientMapInteractiveElement element = elements.get(i);
            if (element instanceof AudioMarker) {
                final AudioMarker audioMarker = (AudioMarker)element;
                if (audioMarker.isVisible()) {
                    procedure.execute(audioMarker);
                }
            }
        }
    }
    
    static {
        m_registeredEvents = new TIntHashSet();
        LAUNCH_PROCEDURE = new TObjectProcedure<AudioMarker>() {
            @Override
            public boolean execute(final AudioMarker marker) {
                marker.launchLocalizedAudioMarkerEvent();
                return true;
            }
        };
    }
}
