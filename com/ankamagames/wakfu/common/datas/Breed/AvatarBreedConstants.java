package com.ankamagames.wakfu.common.datas.Breed;

import java.util.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.configuration.*;

public class AvatarBreedConstants
{
    private static final AvatarBreed[] DEFAULT_ENABLED_BREEDS;
    private static AvatarBreed[] ENABLED_BREEDS;
    
    public static void initBreeds() {
        final TIntHashSet authorizedCharactersClasses = SystemConfiguration.INSTANCE.getIntHashSet(SystemConfigurationType.AUTHORIZED_CHARACTER_CLASS);
        if (authorizedCharactersClasses.isEmpty()) {
            AvatarBreedConstants.ENABLED_BREEDS = new AvatarBreed[AvatarBreedConstants.DEFAULT_ENABLED_BREEDS.length];
            System.arraycopy(AvatarBreedConstants.DEFAULT_ENABLED_BREEDS, 0, AvatarBreedConstants.ENABLED_BREEDS, 0, AvatarBreedConstants.DEFAULT_ENABLED_BREEDS.length);
        }
        else {
            final ArrayList<AvatarBreed> authorizedClasses = new ArrayList<AvatarBreed>();
            authorizedCharactersClasses.forEach(new TIntProcedure() {
                @Override
                public boolean execute(final int value) {
                    final AvatarBreed avatarBreed = AvatarBreed.getBreedFromId(value);
                    if (avatarBreed != null) {
                        authorizedClasses.add(avatarBreed);
                    }
                    return true;
                }
            });
            AvatarBreedConstants.ENABLED_BREEDS = authorizedClasses.toArray(new AvatarBreed[authorizedClasses.size()]);
        }
    }
    
    public static AvatarBreed[] getEnabledBreeds() {
        return AvatarBreedConstants.ENABLED_BREEDS;
    }
    
    public static boolean isBreedEnabled(final AvatarBreed breed) {
        return breed != null && isBreedEnabled(breed.getBreedId());
    }
    
    public static boolean isBreedEnabled(final int breedId) {
        for (int i = 0; i < AvatarBreedConstants.ENABLED_BREEDS.length; ++i) {
            if (AvatarBreedConstants.ENABLED_BREEDS[i].getBreedId() == breedId) {
                return AvatarBreedConstants.ENABLED_BREEDS[i].isInitialized();
            }
        }
        return false;
    }
    
    static {
        DEFAULT_ENABLED_BREEDS = new AvatarBreed[] { AvatarBreed.IOP, AvatarBreed.CRA, AvatarBreed.ECAFLIP, AvatarBreed.FECA, AvatarBreed.SACRIER, AvatarBreed.ENIRIPSA, AvatarBreed.SRAM, AvatarBreed.PANDAWA, AvatarBreed.XELOR, AvatarBreed.OSAMODAS, AvatarBreed.SADIDA, AvatarBreed.ENUTROF, AvatarBreed.ROUBLARD, AvatarBreed.ZOBAL, AvatarBreed.STEAMER, AvatarBreed.SOUL, AvatarBreed.ELIOTROPE };
        AvatarBreedConstants.ENABLED_BREEDS = null;
        initBreeds();
        SystemConfiguration.INSTANCE.addListener(new SystemConfigurationListener() {
            @Override
            public void onLoad() {
                AvatarBreedConstants.initBreeds();
            }
        });
    }
}
