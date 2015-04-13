package com.ankamagames.wakfu.client.core.havenWorld;

import gnu.trove.*;

public class HavenWorldSoundLibrary
{
    public static final HavenWorldSoundLibrary INSTANCE;
    private final TIntIntHashMap m_buildingSounds;
    private final TIntIntHashMap m_patchSounds;
    
    public HavenWorldSoundLibrary() {
        super();
        this.m_buildingSounds = new TIntIntHashMap();
        this.m_patchSounds = new TIntIntHashMap();
    }
    
    public void registerBuilding(final int buildingRefId, final int soundId) {
        this.m_buildingSounds.put(buildingRefId, soundId);
    }
    
    public void registerPatch(final int patchId, final int soundId) {
        this.m_patchSounds.put(patchId, soundId);
    }
    
    public int getBuildingSound(final int buildingRefId) {
        return this.m_buildingSounds.get(buildingRefId);
    }
    
    public int getPatchSound(final int patchId) {
        return this.m_patchSounds.get(patchId);
    }
    
    static {
        INSTANCE = new HavenWorldSoundLibrary();
    }
}
