package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.client.core.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.colors.*;
import org.jetbrains.annotations.*;
import java.util.*;

public class AvatarSmileyView extends ImmutableFieldProvider
{
    public static final String SMILEY_FIELD = "smiley";
    public static final String ANIM_NAME = "animName";
    private final Definition m_definition;
    private AnimatedElementWithDirection m_smiley;
    private static final Map<Definition, AvatarSmileyView> VIEWS;
    
    public static AvatarSmileyView getView(final short breedId, final byte sex, final int smileyEnumId) {
        final Definition definition = new Definition(breedId, sex, smileyEnumId);
        AvatarSmileyView view = AvatarSmileyView.VIEWS.get(definition);
        if (view == null) {
            AvatarSmileyView.VIEWS.put(definition, view = new AvatarSmileyView(definition));
        }
        return view;
    }
    
    private AvatarSmileyView(final Definition definition) {
        super();
        this.m_definition = definition;
        this.init();
    }
    
    private void init() {
        final SmileyEnum smileyType = SmileyEnum.getSmileyFromId(this.m_definition.getSmileyEnumId());
        this.m_smiley = getSmiley((smileyType != null) ? smileyType : SmileyEnum.HAPPY_SMILEYS, this.m_definition.getBreedId(), this.m_definition.getSex());
    }
    
    protected static String getSmileyGfxFileName(final short breedId, final byte sex) {
        return "Smiley_" + breedId + sex;
    }
    
    @Nullable
    private static AnimatedElementWithDirection getSmiley(@NotNull final SmileyEnum smileyType, final short breedId, final byte sex) {
        final AnimatedElementWithDirection smiley = new AnimatedElementWithDirection(GUIDGenerator.getGUID());
        final String fileName = getSmileyGfxFileName(breedId, sex);
        try {
            smiley.load(String.format(WakfuConfiguration.getInstance().getString("playerGfxPath"), fileName), true);
        }
        catch (IOException e) {
            return null;
        }
        catch (PropertyException e2) {
            return null;
        }
        smiley.setGfxId(fileName);
        smiley.setAnimation(smileyType.getAnimation());
        smiley.setAnimationSpeed(0.0f);
        final BreedColorsManager breedColorsManager = BreedColorsManager.getInstance();
        final ObjectPair<Byte, Byte> defaultHairColorIndex = breedColorsManager.getDefaultHairColorIndex(breedId, sex);
        final ObjectPair<Byte, Byte> defaultSkinColorIndex = breedColorsManager.getDefaultSkinColorIndex(breedId, sex);
        final byte defaultPupilColorIndex = breedColorsManager.getDefaultPupilColorIndex(breedId, sex);
        final AnmInstance anmInstance = smiley.getAnmInstance();
        if (defaultHairColorIndex != null) {
            final CharacterColor hairColor = breedColorsManager.getHairColor(breedId, defaultHairColorIndex.getFirst(), defaultHairColorIndex.getSecond(), sex);
            if (hairColor != null) {
                final float[] color = hairColor.getCustomColor();
                anmInstance.addCustomColor(2, color);
            }
        }
        if (defaultSkinColorIndex != null) {
            final CharacterColor skinColor = breedColorsManager.getSkinColor(breedId, defaultSkinColorIndex.getFirst(), defaultSkinColorIndex.getSecond(), sex);
            if (skinColor != null) {
                final float[] color = skinColor.getCustomColor();
                anmInstance.addCustomColor(1, color);
            }
        }
        final CharacterColor pupilColor = breedColorsManager.getPupilColor(breedId, defaultPupilColorIndex, sex);
        if (pupilColor != null) {
            final float[] color = pupilColor.getCustomColor();
            anmInstance.addCustomColor(8, color);
        }
        anmInstance.forceUpdate();
        return smiley;
    }
    
    @Override
    public String[] getFields() {
        return AvatarSmileyView.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("smiley")) {
            return this.m_smiley;
        }
        if (fieldName.equals("animName")) {
            return (this.m_smiley == null) ? null : this.m_smiley.getAnimation();
        }
        return null;
    }
    
    public int getSmileyEnumId() {
        return this.m_definition.getSmileyEnumId();
    }
    
    static {
        VIEWS = new HashMap<Definition, AvatarSmileyView>();
    }
    
    private static class Definition
    {
        private final short m_breedId;
        private final byte m_sex;
        private final int m_smileyEnumId;
        
        protected Definition(final short breedId, final byte sex, final int smileyEnumId) {
            super();
            this.m_breedId = breedId;
            this.m_sex = sex;
            this.m_smileyEnumId = smileyEnumId;
        }
        
        public short getBreedId() {
            return this.m_breedId;
        }
        
        public byte getSex() {
            return this.m_sex;
        }
        
        public int getSmileyEnumId() {
            return this.m_smileyEnumId;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Definition that = (Definition)o;
            return this.m_breedId == that.m_breedId && this.m_sex == that.m_sex && this.m_smileyEnumId == that.m_smileyEnumId;
        }
        
        @Override
        public int hashCode() {
            int result = this.m_breedId;
            result = 31 * result + this.m_sex;
            result = 31 * result + this.m_smileyEnumId;
            return result;
        }
        
        @Override
        public String toString() {
            return "Definition{m_breedId=" + this.m_breedId + ", m_sex=" + this.m_sex + ", m_smileyEnumId=" + this.m_smileyEnumId + '}';
        }
    }
}
