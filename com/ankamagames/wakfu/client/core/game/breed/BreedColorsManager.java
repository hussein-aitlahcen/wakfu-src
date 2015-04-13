package com.ankamagames.wakfu.client.core.game.breed;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.colors.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class BreedColorsManager
{
    private static final Logger m_logger;
    public static final float COLOR_VARIATION_STEP = 0.03f;
    public static final byte COLOR_VARIATION_MIN_FACTOR = -4;
    public static final byte COLOR_VARIATION_MAX_FACTOR = 4;
    public static final byte COLOR_VARIATIONS_SIZE = 9;
    private final IntObjectLightWeightMap<String> m_breedAps;
    private final IntObjectLightWeightMap<BreedColorSet> m_breedMaleColors;
    private final IntObjectLightWeightMap<BreedColorSet> m_breedFemaleColors;
    private static final BreedColorsManager m_instance;
    
    private BreedColorsManager() {
        super();
        this.m_breedAps = new IntObjectLightWeightMap<String>();
        this.m_breedMaleColors = new IntObjectLightWeightMap<BreedColorSet>();
        this.m_breedFemaleColors = new IntObjectLightWeightMap<BreedColorSet>();
    }
    
    public static BreedColorsManager getInstance() {
        return BreedColorsManager.m_instance;
    }
    
    public void addAps(final int breedId, final int apsId) {
        this.m_breedAps.put(breedId, apsId + ".xps");
    }
    
    @Nullable
    public String getApsFileName(final int breedId) {
        return this.m_breedAps.get(breedId);
    }
    
    public void addBreedColor(final int breedId, final byte sex, final BreedColorSet breedColorSet) {
        this.getColorMap(sex).put(breedId, breedColorSet);
    }
    
    private IntObjectLightWeightMap<BreedColorSet> getColorMap(final byte sex) {
        return (sex == 0) ? this.m_breedMaleColors : this.m_breedFemaleColors;
    }
    
    @Nullable
    BreedColorSet getBreedColorSet(final int breedId, final byte sex) {
        return this.getColorMap(sex).get(breedId);
    }
    
    @Nullable
    public CharacterColor getSkinColor(final int breedId, final int colorIndex, final byte colorFactor, final byte sex) {
        final BreedColorSet set = this.getBreedColorSet(breedId, sex);
        return (set == null) ? null : createNewColorWithFactor((byte)colorIndex, set.getSkinColor((byte)colorIndex), colorFactor);
    }
    
    @Nullable
    public final CharacterColor getSkinColor(final PlayerCharacter character) {
        return this.getSkinColor(character.getBreedId(), character.getSkinColorIndex(), character.getSkinColorFactor(), character.getSex());
    }
    
    @Nullable
    public CharacterColor getHairColor(final int breedId, final int colorIndex, final byte colorFactor, final byte sex) {
        final BreedColorSet set = this.getBreedColorSet(breedId, sex);
        return (set == null) ? null : createNewColorWithFactor((byte)colorIndex, set.getHairColor((byte)colorIndex), colorFactor);
    }
    
    @Nullable
    public final CharacterColor getHairColor(final PlayerCharacter character) {
        return this.getHairColor(character.getBreedId(), character.getHairColorIndex(), character.getHairColorFactor(), character.getSex());
    }
    
    @Nullable
    public CharacterColor getPupilColor(final int breedId, final int colorIndex, final byte sex) {
        final BreedColorSet set = this.getBreedColorSet(breedId, sex);
        return (set == null) ? null : createNewColorWithFactor((byte)colorIndex, set.getPupilColor((byte)colorIndex), (byte)0);
    }
    
    @Nullable
    public final CharacterColor getPupilColor(final PlayerCharacter character) {
        return this.getPupilColor(character.getBreedId(), character.getPupilColorIndex(), character.getSex());
    }
    
    @Nullable
    public CharacterColor[] getSkinColors(final int breedId, final byte sex) {
        final BreedColorSet set = this.getBreedColorSet(breedId, sex);
        return (CharacterColor[])((set == null) ? null : set.getSkinColors());
    }
    
    @Nullable
    public CharacterColor[] getHairColors(final int breedId, final byte sex) {
        final BreedColorSet set = this.getBreedColorSet(breedId, sex);
        return (CharacterColor[])((set != null) ? set.getHairColors() : null);
    }
    
    @Nullable
    public CharacterColor[] getPupilColors(final int breedId, final byte sex) {
        final BreedColorSet set = this.getBreedColorSet(breedId, sex);
        return (CharacterColor[])((set == null) ? null : set.getPupilColors());
    }
    
    @Nullable
    public CharacterColor[] getSecondarySkinColors(final int breedId, final byte colorId, final byte sex) {
        return this.getSecondaryColors(this.getSkinColors(breedId, sex), colorId);
    }
    
    @Nullable
    public CharacterColor[] getSecondaryHairColors(final int breedId, final byte colorId, final byte sex) {
        return this.getSecondaryColors(this.getHairColors(breedId, sex), colorId);
    }
    
    @Nullable
    public ObjectPair<Byte, Byte> getDefaultSkinColorIndex(final int breedId, final byte sex) {
        final ObjectPair<Byte, Byte> objectPair = new ObjectPair<Byte, Byte>();
        final BreedColorSet bcs = this.getBreedColorSet(breedId, sex);
        if (bcs == null) {
            return null;
        }
        objectPair.setFirst(bcs.getDefaultSkinColorIndex());
        objectPair.setSecond(bcs.getDefaultSkinColorFactor());
        return objectPair;
    }
    
    @Nullable
    public ObjectPair<Byte, Byte> getDefaultHairColorIndex(final int breedId, final byte sex) {
        final ObjectPair<Byte, Byte> objectPair = new ObjectPair<Byte, Byte>();
        final BreedColorSet bcs = this.getBreedColorSet(breedId, sex);
        if (bcs == null) {
            return null;
        }
        objectPair.setFirst(bcs.getDefaultHairColorIndex());
        objectPair.setSecond(bcs.getDefaultHairColorFactor());
        return objectPair;
    }
    
    @Nullable
    public byte getDefaultPupilColorIndex(final int breedId, final byte sex) {
        final BreedColorSet set = this.getBreedColorSet(breedId, sex);
        return (byte)((set == null) ? 0 : set.getDefaultPupilColorIndex());
    }
    
    public String getHairStyle(final int breedId, final boolean isMale, final int index) {
        assert breedId >= 0;
        int finalIndex = index;
        final int hairStyleMaxIndex = this.getHairStylesCount((short)breedId, isMale) - 1;
        if (finalIndex > hairStyleMaxIndex) {
            finalIndex = hairStyleMaxIndex;
        }
        else if (finalIndex < 0) {
            finalIndex = 0;
        }
        return getStyle("20", breedId, isMale, finalIndex);
    }
    
    public String getDressStyle(final int breedId, final boolean isMale, final int index) {
        assert breedId >= 0;
        int finalIndex = index;
        final int dressStyleMaxIndex = this.getDressStylesCount((short)breedId, isMale) - 1;
        if (finalIndex > dressStyleMaxIndex) {
            finalIndex = dressStyleMaxIndex;
        }
        else if (finalIndex < 0) {
            finalIndex = 0;
        }
        return getStyle("21", breedId, isMale, finalIndex);
    }
    
    public int getSkinColorsCount(final short breedId, final byte sex) {
        final BreedColorSet set = this.getBreedColorSet(breedId, sex);
        return set.getSkinColors().length;
    }
    
    public int getHairColorsCount(final short breedId, final byte sex) {
        final BreedColorSet set = this.getBreedColorSet(breedId, sex);
        return set.getHairColors().length;
    }
    
    public int getPupilColorsCount(final short breedId, final byte sex) {
        final BreedColorSet set = this.getBreedColorSet(breedId, sex);
        return set.getPupilColors().length;
    }
    
    public int getDressStylesCount(final short breedId, final boolean isMale) {
        if (breedId == AvatarBreed.SRAM.getBreedId() && isMale) {
            return 1;
        }
        if (breedId == AvatarBreed.STEAMER.getBreedId()) {
            return 1;
        }
        if (breedId == AvatarBreed.SOUL.getBreedId()) {
            return 3;
        }
        return 5;
    }
    
    public int getHairStylesCount(final short breedId, final boolean isMale) {
        if (breedId == AvatarBreed.ELIOTROPE.getBreedId()) {
            return 1;
        }
        return 3;
    }
    
    private static String getStyle(final String prefix, final int breedId, final boolean isMale, final int index) {
        final StringBuilder sb = new StringBuilder(9);
        sb.append(prefix);
        if (breedId < 10) {
            sb.append('0');
        }
        sb.append(breedId);
        sb.append(isMale ? '0' : '1');
        sb.append("000");
        sb.append(index + 1);
        return sb.toString();
    }
    
    @Nullable
    private CharacterColor[] getSecondaryColors(final CharacterColor[] colors, final byte colorId) {
        try {
            return getModifiedColors(colors[colorId]);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    @Nullable
    private static CharacterColor[] getModifiedColors(final CharacterColor baseColor) {
        final CharacterColor[] colors = new CharacterColor[9];
        int j = 0;
        for (int i = -4; i <= 4; ++i) {
            colors[j] = createNewColorWithFactor((byte)baseColor.getIndex(), baseColor, (byte)i);
            ++j;
        }
        return colors;
    }
    
    @Nullable
    private static CharacterColor createNewColorWithFactor(final byte baseIndex, final CharacterColor color, final byte colorFactor) {
        if (color == null) {
            return null;
        }
        final CharacterColor c = new CharacterColor(baseIndex, MathHelper.clamp(color.getRed() + 0.03f * colorFactor, 0.0f, 1.0f), MathHelper.clamp(color.getGreen() + 0.03f * colorFactor, 0.0f, 1.0f), MathHelper.clamp(color.getBlue() + 0.03f * colorFactor, 0.0f, 1.0f));
        c.setFactor(colorFactor);
        return c;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BreedColorsManager.class);
        m_instance = new BreedColorsManager();
    }
}
