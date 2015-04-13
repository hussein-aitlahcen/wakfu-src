package com.ankamagames.wakfu.common.game.aptitude;

import com.ankamagames.wakfu.common.datas.Breed.*;
import org.apache.commons.lang3.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public class AptitudeHelper
{
    private static final int[] SOUL_APTITUDES;
    
    public static int[] getBreedId(final int aptitudeId, final int breedId) {
        final AvatarBreed aptitudeBreed = AvatarBreed.getBreedFromId(breedId);
        if (aptitudeBreed != AvatarBreed.COMMON) {
            return new int[] { breedId };
        }
        if (ArrayUtils.contains(AptitudeHelper.SOUL_APTITUDES, aptitudeId)) {
            return new int[] { AvatarBreed.COMMON.getBreedId() };
        }
        final TIntArrayList breeds = new TIntArrayList();
        for (final AvatarBreed breed : AvatarBreed.values()) {
            if (breed != AvatarBreed.COMMON && breed != AvatarBreed.NONE && breed != AvatarBreed.SOUL) {
                breeds.add(breed.getBreedId());
            }
        }
        return breeds.toNativeArray();
    }
    
    public static AptitudeType getAptitudeType(final int[] breedIds, final FighterCharacteristicType characteristicType) {
        final boolean isCommon = ArrayUtils.contains(breedIds, AvatarBreed.COMMON.getBreedId()) || breedIds.length > 1;
        return (isCommon && characteristicType != null) ? AptitudeType.COMMON : AptitudeType.SPELL;
    }
    
    static {
        SOUL_APTITUDES = new int[] { 3, 4, 167, 168, 169, 170 };
    }
}
