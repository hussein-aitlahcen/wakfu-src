package com.ankamagames.wakfu.client.ui.protocol.frame.helpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.client.alea.adviser.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.text.flying.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import gnu.trove.*;

public class EmoteIconHelper
{
    private static final Logger m_logger;
    private static final TLongObjectHashMap<SmileyWidget> SMILEYS;
    private static final int FLYING_EMOTE_DURATION = 2000;
    
    public static void cleanAllSmileys() {
        final TLongObjectIterator<SmileyWidget> it = EmoteIconHelper.SMILEYS.iterator();
        while (it.hasNext()) {
            it.advance();
            final SmileyWidget smileyWidget = it.value();
            if (smileyWidget != null && smileyWidget.getElementMap() != null) {
                smileyWidget.unloadWidget();
            }
        }
        EmoteIconHelper.SMILEYS.clear();
    }
    
    public static void cleanSmiley(final long sourceId) {
        final SmileyWidget smiley = EmoteIconHelper.SMILEYS.remove(sourceId);
        if (smiley != null) {
            smiley.unloadWidget();
        }
    }
    
    public static boolean prepareSmileyWidget(final int id, final CharacterInfo character) {
        final String animName = SmileyEnum.getSmileyFromId(id).getAnimation();
        final SmileyWidget smileyWidget = getOrCreateSmileyWidget(character, animName);
        if (smileyWidget == null) {
            return false;
        }
        prepareSmileyWidget(character, smileyWidget);
        return true;
    }
    
    private static SmileyWidget getOrCreateSmileyWidget(final CharacterInfo character, final String animName) {
        final long characterId = character.getId();
        SmileyWidget smileyWidget = EmoteIconHelper.SMILEYS.get(characterId);
        if (smileyWidget != null) {
            assert characterId == ((Mobile)smileyWidget.getTarget()).getId() : "Le mobile de " + character.getName() + " ne correspond pas \u00e0 l'ancien";
            try {
                smileyWidget.resetElapsedLifeTime();
                smileyWidget.setAnimation(animName);
                return smileyWidget;
            }
            catch (Exception e) {
                smileyWidget.unloadWidget();
                EmoteIconHelper.SMILEYS.remove(characterId);
                EmoteIconHelper.m_logger.warn((Object)e);
                return null;
            }
        }
        try {
            final String widgetId = WakfuSmileyUtils.getNewWakfuSmileyId();
            smileyWidget = WakfuSmileyUtils.loadSmiley(widgetId);
            smileyWidget.initialize(widgetId, animName, false);
            EmoteIconHelper.SMILEYS.put(characterId, smileyWidget);
        }
        catch (Exception e) {
            EmoteIconHelper.m_logger.warn((Object)e);
            return null;
        }
        return smileyWidget;
    }
    
    private static void prepareSmileyWidget(final CharacterInfo character, final SmileyWidget smileyWidget) {
        final AnimatedElement animatedElement = character.getSmiley();
        assert character.getBreed() instanceof MonsterBreed;
        applyCharacterColorsOnSmiley(character, animatedElement.getAnmInstance());
        smileyWidget.setDuration(2000);
        smileyWidget.setTarget(character.getActor());
        smileyWidget.validateAdviser();
        smileyWidget.setYOffset((int)(character.getActor().getVisualHeight() * 10.0f) + 10);
        smileyWidget.setSmileyIsVisible(true);
        smileyWidget.validateAdviser();
        final AnimatedElementViewer viewer = smileyWidget.getAnimatedElementViewer();
        viewer.setAnimatedElement(animatedElement);
        viewer.getAnimatedElement().addAnimationEndedListener(new AnimationEndedListener() {
            @Override
            public void animationEnded(final AnimatedElement element) {
                element.removeAnimationEndedListener(this);
                EmoteIconHelper.cleanSmiley(character.getId());
            }
        });
    }
    
    public static boolean displayEmoteIconOnMonster(final int id, final long sourceId, final int familyId, final CharacterInfo character) {
        assert character != null;
        final Breed characterBreed = character.getBreed();
        String emoteIconUrl;
        try {
            final int breedFamilyId = getBreedFamilyId(familyId, character);
            emoteIconUrl = WakfuConfiguration.getInstance().getBreedEmoteIconUrl(SmileyEnum.formatEmoteIconId(id, characterBreed.getBreedId()));
            if (emoteIconUrl == null) {
                emoteIconUrl = getEmoteIconUrlForFamily(id, breedFamilyId);
            }
        }
        catch (Exception e) {
            EmoteIconHelper.m_logger.error((Object)"Exception", (Throwable)e);
            return false;
        }
        return emoteIconUrl != null && applyEmoteIconOnMobile(sourceId, characterBreed, emoteIconUrl);
    }
    
    public static boolean displayEmoteIconOnMobile(final int id, final long mobileId, final int familyId) {
        if (familyId == 1) {
            EmoteIconHelper.m_logger.error((Object)("character inconnu " + mobileId));
            return false;
        }
        final String emoteIconUrl = getEmoteIconUrlForFamily(id, familyId);
        return applyEmoteIconOnMobile(mobileId, null, emoteIconUrl);
    }
    
    private static boolean applyEmoteIconOnMobile(final long sourceId, final Breed characterBreed, final String emoteIconUrl) {
        final Mobile mobile = MobileManager.getInstance().getMobile(sourceId);
        if (mobile == null || !mobile.isVisible()) {
            return false;
        }
        final HashSet<Adviser> advisers = AdviserManager.getInstance().getAdvisers(mobile);
        final byte height = (characterBreed == null) ? ((byte)mobile.getHeight()) : characterBreed.getHeight();
        final FlyingImage emoteIconWidget = new FlyingImage(emoteIconUrl, 32, 32, new FlyingImage.StaticFlyingImageDeformer(), 2000);
        emoteIconWidget.setTarget(mobile);
        emoteIconWidget.setYOffset((int)(height * 10.0f + emoteIconWidget.getYOffset() + 10.0f));
        emoteIconWidget.setWaitingTime(Math.max(1, (advisers != null) ? (advisers.size() * 2000 / 2) : 0));
        AdviserManager.getInstance().addAdviser(emoteIconWidget);
        return true;
    }
    
    private static int getBreedFamilyId(final int familyId, final CharacterInfo character) {
        if (familyId != -1) {
            return familyId;
        }
        final MonsterFamily family = MonsterFamilyManager.getInstance().getMonsterFamily(character.getBreed().getFamilyId());
        return character.isActiveProperty(WorldPropertyType.NPC) ? 0 : family.getParentFamilyId();
    }
    
    private static String getEmoteIconUrlForFamily(final int id, final int breedFamilyId) {
        return WakfuConfiguration.getInstance().getEmoteIconUrl(SmileyEnum.formatEmoteIconId(id, breedFamilyId));
    }
    
    public static void applyCharacterColorsOnSmiley(final CharacterInfo character, final AnmInstance anmInstance) {
        final CharacterActor characterActor = character.getActor();
        applycustomColor(anmInstance, characterActor, 1);
        applycustomColor(anmInstance, characterActor, 2);
        applycustomColor(anmInstance, characterActor, 8);
        anmInstance.forceUpdate();
    }
    
    private static void applycustomColor(final AnmInstance anmInstance, final CharacterActor characterActor, final int index) {
        final float[] color = characterActor.getCustomColor(index);
        anmInstance.addCustomColor(index, color);
    }
    
    static {
        m_logger = Logger.getLogger((Class)EmoteIconHelper.class);
        SMILEYS = new TLongObjectHashMap<SmileyWidget>();
    }
}
