package com.ankamagames.wakfu.client.core.world.havenWorld;

import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.alea.sound.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import java.util.*;

public class HavenWorldSound
{
    public static final HavenWorldSound INSTANCE;
    public static final int HAVEN_WORLD_SIDOA_PRAYING_ID = 2147483646;
    private static final ArrayList<SoundDefinition> SOUND_DEFS;
    private static final TIntObjectHashMap<SoundData> DATA;
    
    private static byte getRelativeX(final int x) {
        final int mapX = PartitionConstants.getPartitionXFromCellX(x);
        final int relativeX = x - mapX * 18;
        return (byte)relativeX;
    }
    
    private static byte getRelativeY(final int y) {
        final int mapY = PartitionConstants.getPartitionYFromCellY(y);
        final int relativeY = y - mapY * 18;
        return (byte)relativeY;
    }
    
    public void initialize() {
        HavenWorldSound.DATA.forEachValue(new TObjectProcedure<SoundData>() {
            @Override
            public boolean execute(final SoundData object) {
                SoundBank.getInstance().add(object);
                return true;
            }
        });
    }
    
    public void addAllSoundsToMap(final ClientEnvironmentMap map) {
        final SoundDef[] soundData = map.getSoundData();
        final ArrayList<SoundDef> soundDefs = new ArrayList<SoundDef>();
        if (soundData != null) {
            soundDefs.addAll(Arrays.asList(soundData));
        }
        for (int i = 0, size = HavenWorldSound.SOUND_DEFS.size(); i < size; ++i) {
            final SoundDefinition def = HavenWorldSound.SOUND_DEFS.get(i);
            if (map.isInMap(def.m_x, def.m_y)) {
                final SoundDef soundDef = new SoundDef(getRelativeX(def.m_x), getRelativeY(def.m_y), (short)0, def.m_soundId);
                soundDefs.add(soundDef);
            }
        }
        map.setSoundData(soundDefs.toArray(new SoundDef[soundDefs.size()]));
    }
    
    static {
        INSTANCE = new HavenWorldSound();
        SOUND_DEFS = new ArrayList<SoundDefinition>();
        (DATA = new TIntObjectHashMap<SoundData>()).put(2147483646, new SoundData(2147483646, 70500023L, 0.05f, true, true, (short)10, (short)25, 1.0f, 0, 0));
        HavenWorldSound.SOUND_DEFS.add(new SoundDefinition(2147483646, 0, -48));
    }
    
    private static class SoundDefinition
    {
        private int m_x;
        private int m_y;
        private int m_soundId;
        
        private SoundDefinition(final int soundId, final int x, final int y) {
            super();
            this.m_soundId = soundId;
            this.m_x = x;
            this.m_y = y;
        }
    }
}
