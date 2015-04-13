package com.ankamagames.wakfu.client.sound;

import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.framework.fileFormat.xml.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class WakfuGroundSoundManager implements SoundFunctionsLibrary.GroundSoundProvider
{
    private IntObjectLightWeightMap<MultiGroundSound> m_map;
    private String m_currentPath;
    private float m_fightFspGain;
    
    public WakfuGroundSoundManager() {
        super();
        this.m_map = new IntObjectLightWeightMap<MultiGroundSound>();
        this.m_fightFspGain = 1.0f;
    }
    
    @Override
    public SoundFunctionsLibrary.GroundSoundData getSoundData(final byte groundType, final byte walkType) {
        final MultiGroundSound list = this.m_map.get(groundType);
        if (list == null) {
            return null;
        }
        return list.getGroundSoundData(walkType);
    }
    
    @Override
    public float getFightFspGain() {
        return this.m_fightFspGain;
    }
    
    public static SoundFunctionsLibrary.GroundSoundData createGroundSoundFromXML(final DocumentEntry xml) {
        final DocumentEntry paramId = xml.getParameterByName("id");
        if (paramId == null) {
            return null;
        }
        final SoundFunctionsLibrary.GroundSoundData param = new SoundFunctionsLibrary.GroundSoundData();
        param.setSoundId(paramId.getLongValue());
        final DocumentEntry paramGain = xml.getParameterByName("gain");
        if (paramGain != null) {
            param.setGain(paramGain.getIntValue() / 100.0f);
        }
        final DocumentEntry paramRollOff = xml.getParameterByName("rollOff");
        if (paramRollOff != null) {
            param.setRollOff(paramRollOff.getIntValue());
        }
        return param;
    }
    
    public void reload() throws Exception {
        this.loadFromXML(this.m_currentPath);
    }
    
    public void loadFromXML(final String path) throws Exception {
        this.m_currentPath = path;
        final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
        final XMLDocumentContainer container = new XMLDocumentContainer();
        accessor.open(path);
        accessor.read(container, new DocumentEntryParser[0]);
        accessor.close();
        this.m_map.clear();
        final DocumentEntry entry = container.getRootNode();
        final ArrayList<DocumentEntry> fightChildren = entry.getDirectChildrenByName("fightFspGain");
        if (fightChildren != null) {
            for (final DocumentEntry fightGain : fightChildren) {
                final DocumentEntry value = fightGain.getParameterByName("value");
                if (value == null) {
                    continue;
                }
                this.m_fightFspGain = value.getFloatValue();
                break;
            }
        }
        for (final DocumentEntry groundEntry : entry.getDirectChildrenByName("ground")) {
            int groundType = 0;
            final DocumentEntry groundTypeEntry = groundEntry.getParameterByName("type");
            if (groundTypeEntry != null) {
                groundType = groundTypeEntry.getIntValue();
            }
            final MultiGroundSound list = new MultiGroundSound();
            final DocumentEntry walkEntry = groundEntry.getChildByName("walk");
            for (final DocumentEntry soundEntry : walkEntry.getDirectChildrenByName("sound")) {
                final SoundFunctionsLibrary.GroundSoundData groundSound = createGroundSoundFromXML(soundEntry);
                if (groundSound != null) {
                    list.addGroundSound(groundSound, (byte)0);
                }
            }
            final DocumentEntry runEntry = groundEntry.getChildByName("run");
            for (final DocumentEntry soundEntry2 : runEntry.getDirectChildrenByName("sound")) {
                final SoundFunctionsLibrary.GroundSoundData groundSound2 = createGroundSoundFromXML(soundEntry2);
                if (groundSound2 != null) {
                    list.addGroundSound(groundSound2, (byte)1);
                }
            }
            this.m_map.put(groundType, list);
        }
    }
    
    private static class MultiGroundSound
    {
        private final ArrayList<SoundFunctionsLibrary.GroundSoundData> m_walkList;
        private int m_walkOffset;
        private final ArrayList<SoundFunctionsLibrary.GroundSoundData> m_runList;
        private int m_runOffset;
        
        private MultiGroundSound() {
            super();
            this.m_walkList = new ArrayList<SoundFunctionsLibrary.GroundSoundData>();
            this.m_runList = new ArrayList<SoundFunctionsLibrary.GroundSoundData>();
        }
        
        public void addGroundSound(final SoundFunctionsLibrary.GroundSoundData data, final byte type) {
            switch (type) {
                case 0: {
                    this.m_walkList.add(data);
                    this.m_walkOffset = this.m_walkList.size();
                    break;
                }
                case 1: {
                    this.m_runList.add(data);
                    this.m_runOffset = this.m_runList.size();
                    break;
                }
            }
        }
        
        public SoundFunctionsLibrary.GroundSoundData getGroundSoundData(final byte type) {
            switch (type) {
                case 0: {
                    if (this.m_walkList.size() == 0) {
                        return null;
                    }
                    if (this.m_walkOffset == this.m_walkList.size()) {
                        this.reshuffle(this.m_walkList);
                        this.m_walkOffset = 0;
                    }
                    final SoundFunctionsLibrary.GroundSoundData data = this.m_walkList.get(this.m_walkOffset);
                    ++this.m_walkOffset;
                    return data;
                }
                case 1: {
                    if (this.m_runList.size() == 0) {
                        return null;
                    }
                    if (this.m_runOffset == this.m_runList.size()) {
                        this.reshuffle(this.m_runList);
                        this.m_runOffset = 0;
                    }
                    final SoundFunctionsLibrary.GroundSoundData data = this.m_runList.get(this.m_runOffset);
                    ++this.m_runOffset;
                    return data;
                }
                default: {
                    return null;
                }
            }
        }
        
        private void reshuffle(final ArrayList<SoundFunctionsLibrary.GroundSoundData> list) {
            Collections.shuffle(list, MathHelper.getRandomGenerator());
        }
    }
}
