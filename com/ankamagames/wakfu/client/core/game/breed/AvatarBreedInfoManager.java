package com.ankamagames.wakfu.client.core.game.breed;

import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class AvatarBreedInfoManager
{
    private static final AvatarBreedInfoManager m_instance;
    private final IntObjectLightWeightMap<AvatarBreedInfo> m_breedInfos;
    private AvatarBreedInfo[] m_breedInfosArray;
    
    public AvatarBreedInfoManager() {
        super();
        this.m_breedInfos = new IntObjectLightWeightMap<AvatarBreedInfo>();
    }
    
    public static AvatarBreedInfoManager getInstance() {
        return AvatarBreedInfoManager.m_instance;
    }
    
    private static AvatarBreed[] enabledBreeds() {
        return AvatarBreedConstants.getEnabledBreeds();
    }
    
    private static AvatarBreed[] enabledBreedsForTest() {
        return AvatarBreedConstants.getEnabledBreeds();
    }
    
    public final void initialize() {
        this.m_breedInfosArray = null;
        this.m_breedInfos.clear();
        for (final AvatarBreed avatarBreed : enabledBreeds()) {
            final AvatarBreedInfo breedInfo = new AvatarBreedInfo(avatarBreed);
            this.m_breedInfos.put(avatarBreed.getBreedId(), breedInfo);
        }
    }
    
    public final AvatarBreedInfo getBreedInfo(final int breedId) {
        return this.m_breedInfos.get(breedId);
    }
    
    public final AvatarBreedInfo[] getBreedInfos() {
        if (this.m_breedInfosArray == null) {
            final ArrayList<AvatarBreedInfo> avatarBreeds = new ArrayList<AvatarBreedInfo>(this.m_breedInfos.size());
            for (int i = 0, size = this.m_breedInfos.size(); i < size; ++i) {
                final AvatarBreedInfo breedInfo = this.m_breedInfos.getQuickValue(i);
                if (breedInfo.getBreed() != AvatarBreed.COMMON && breedInfo.getBreed() != AvatarBreed.NONE) {
                    avatarBreeds.add(breedInfo);
                }
            }
            Collections.sort(avatarBreeds);
            this.m_breedInfosArray = avatarBreeds.toArray(new AvatarBreedInfo[avatarBreeds.size()]);
        }
        return this.m_breedInfosArray;
    }
    
    public final AvatarBreed getRandomBreedForTest() {
        return enabledBreedsForTest()[MathHelper.random(enabledBreedsForTest().length)];
    }
    
    static {
        m_instance = new AvatarBreedInfoManager();
    }
}
