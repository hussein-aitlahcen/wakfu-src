package com.ankamagames.wakfu.client.sound;

import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.fileFormat.xml.*;
import com.ankamagames.framework.fileFormat.document.*;
import gnu.trove.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class WakfuBarkManager implements SoundFunctionsLibrary.BarkProvider
{
    private final TLongObjectHashMap<MultiBark> m_multiBarksMap;
    private String m_currentPath;
    
    public WakfuBarkManager() {
        super();
        this.m_multiBarksMap = new TLongObjectHashMap<MultiBark>();
        this.m_currentPath = null;
    }
    
    private SoundFunctionsLibrary.BarkData getData(final ObservedSource elem, final int breedId, final int type) {
        if (!(elem instanceof CharacterActor)) {
            return null;
        }
        final CharacterActor actor = (CharacterActor)elem;
        final Breed breed = actor.getCharacterInfo().getBreed();
        long id = 910L;
        if (breed instanceof AvatarBreed) {
            id = id * 10000L + ((breedId != -1) ? breedId : breed.getBreedId());
            id = id * 10L + actor.getCharacterInfo().getSex();
        }
        else {
            id = id * 100000L + ((breedId != -1) ? breedId : breed.getBreedId());
        }
        id = id * 100L + type;
        final MultiBark multiBark = this.m_multiBarksMap.get(id);
        if (multiBark == null) {
            return null;
        }
        return multiBark.getBarkData();
    }
    
    @Override
    public SoundFunctionsLibrary.BarkData getSoundId(final ObservedSource elem, final int type) {
        return this.getData(elem, -1, type);
    }
    
    @Override
    public SoundFunctionsLibrary.BarkData getSoundId(final ObservedSource elem, final int type, final int breed) {
        return this.getData(elem, breed, type);
    }
    
    public static SoundFunctionsLibrary.BarkData createBarkFromXML(final DocumentEntry xml) {
        final DocumentEntry paramId = xml.getParameterByName("id");
        if (paramId == null) {
            return null;
        }
        final SoundFunctionsLibrary.BarkData param = new SoundFunctionsLibrary.BarkData();
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
        this.m_multiBarksMap.clear();
        final DocumentEntry entry = container.getRootNode();
        long prefix = 0L;
        final DocumentEntry prefixParameter = entry.getParameterByName("prefix");
        if (prefixParameter != null) {
            prefix = prefixParameter.getLongValue();
        }
        for (final DocumentEntry barkEntry : entry.getDirectChildrenByName("bark")) {
            int barkType = 0;
            final DocumentEntry barkTypeEntry = barkEntry.getParameterByName("type");
            if (barkTypeEntry != null) {
                barkType = barkTypeEntry.getIntValue();
            }
            for (final DocumentEntry skinEntry : barkEntry.getDirectChildrenByName("skin")) {
                final TIntArrayList skins = new TIntArrayList();
                final DocumentEntry skinValueParam = skinEntry.getParameterByName("value");
                if (skinValueParam != null) {
                    final String skinValue = skinValueParam.getStringValue();
                    final String[] arr$;
                    final String[] skinIds = arr$ = skinValue.split(",");
                    for (final String skinId : arr$) {
                        final int skinIdInt = PrimitiveConverter.getInteger(skinId.trim(), -1);
                        if (skinIdInt != -1) {
                            skins.add(skinIdInt);
                        }
                    }
                }
                final MultiBark multiBark = new MultiBark();
                for (final DocumentEntry soundEntry : skinEntry.getDirectChildrenByName("sound")) {
                    final SoundFunctionsLibrary.BarkData bark = createBarkFromXML(soundEntry);
                    if (bark != null) {
                        multiBark.addBark(bark);
                    }
                }
                for (int i = 0, size = skins.size(); i < size; ++i) {
                    long id = prefix;
                    id = id * 100000L + skins.get(i);
                    id = id * 100L + barkType;
                    this.m_multiBarksMap.put(id, multiBark);
                }
            }
        }
        this.m_multiBarksMap.compact();
    }
    
    private static class MultiBark
    {
        private ArrayList<SoundFunctionsLibrary.BarkData> m_list;
        private int m_offset;
        
        private MultiBark() {
            super();
            this.m_list = new ArrayList<SoundFunctionsLibrary.BarkData>();
        }
        
        public void addBark(final SoundFunctionsLibrary.BarkData data) {
            this.m_list.add(data);
            this.m_offset = this.m_list.size();
        }
        
        public SoundFunctionsLibrary.BarkData getBarkData() {
            if (this.m_list.size() == 0) {
                return null;
            }
            if (this.m_offset == this.m_list.size()) {
                this.reshuffle();
                this.m_offset = 0;
            }
            final SoundFunctionsLibrary.BarkData data = this.m_list.get(this.m_offset);
            ++this.m_offset;
            return data;
        }
        
        private void reshuffle() {
            Collections.shuffle(this.m_list, MathHelper.getRandomGenerator());
        }
    }
}
