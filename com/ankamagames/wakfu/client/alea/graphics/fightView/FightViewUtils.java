package com.ankamagames.wakfu.client.alea.graphics.fightView;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.baseImpl.graphics.isometric.lights.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.console.command.display.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import java.util.*;

final class FightViewUtils
{
    private static final Logger m_logger;
    private static final float TRANSPARENT_FIGHTER_ALPHA = 0.6f;
    private static final TIntObjectHashMap<AnimatedInteractiveElement> m_fightRepresentations0;
    private static final TIntObjectHashMap<AnimatedInteractiveElement> m_fightRepresentations1;
    
    static void showFight(final FightInfo fightInfo) {
        if (fightInfo == null) {
            return;
        }
        for (final CharacterInfo characterInfo : fightInfo.getFighters()) {
            showCharacter(characterInfo);
        }
        final BasicEffectAreaManager effectAreaManager = fightInfo.getEffectAreaManager();
        if (effectAreaManager != null) {
            for (final BasicEffectArea basicEffectArea : effectAreaManager.getActiveEffectAreas()) {
                showEffectAreaIfNecessary(fightInfo, basicEffectArea);
            }
            refreshEffectAreaDisplayer();
        }
        final InOutCellLightModifier lightModifier = fightInfo.getCellLightModifier();
        if (lightModifier != null) {
            lightModifier.fadeIn();
        }
    }
    
    static void hideFight(final FightInfo fightInfo) {
        changeFightVisibility(fightInfo, VisibilityType.HIDE);
    }
    
    private static void changeFightVisibility(final FightInfo fightInfo, final VisibilityType type) {
        if (fightInfo == null) {
            return;
        }
        changeFightersVisibility(fightInfo, type);
        hideFightEffectAreas(fightInfo);
        fadeOutLightModifier(fightInfo);
    }
    
    private static Point3 findNearestFighterPosition(final FightInfo fightInfo, final Point3 pos) {
        final FightMap fightMap = fightInfo.getFightMap();
        int shortestDistance = Integer.MAX_VALUE;
        Point3 nearestValidPosition = null;
        for (final BasicFighter fighter : fightInfo.getFighters()) {
            final Point3 position = fighter.getPosition();
            if (position == null) {
                FightViewUtils.m_logger.error((Object)"fighter sans position");
            }
            else {
                if (!fightMap.isInside(position.getX(), position.getY())) {
                    continue;
                }
                final int distance = pos.getDistance(position);
                if (distance >= shortestDistance) {
                    continue;
                }
                shortestDistance = distance;
                nearestValidPosition = position;
            }
        }
        return nearestValidPosition;
    }
    
    public static void addFightRepresentation(final ExternalFightInfo fightInfo) {
        if (FightViewUtils.m_fightRepresentations0.containsKey(fightInfo.getId())) {
            return;
        }
        final Point3 center = findApproximateBubbleCenter(fightInfo);
        if (center == null) {
            FightViewUtils.m_logger.error((Object)("pas de cellule trouv\u00e9e pour repr\u00e9senter le combat " + fightInfo + " center=" + fightInfo.getFightMap().getApproximateBubbleCenter()));
            return;
        }
        final AnimatedInteractiveElement sword0 = FightRepresentationHelper.createRepresentation(fightInfo, 0, center);
        FightViewUtils.m_fightRepresentations0.put(fightInfo.getId(), sword0);
        SimpleAnimatedElementManager.getInstance().addAnimatedElement(sword0);
        final AnimatedInteractiveElement sword = FightRepresentationHelper.createRepresentation(fightInfo, 1, center);
        FightViewUtils.m_fightRepresentations1.put(fightInfo.getId(), sword);
        SimpleAnimatedElementManager.getInstance().addAnimatedElement(sword);
    }
    
    private static Point3 findApproximateBubbleCenter(final ExternalFightInfo fightInfo) {
        final Point3 center = fightInfo.getFightMap().getApproximateBubbleCenter();
        center.setZ(TopologyMapManager.getNearestZ(center.getX(), center.getY(), center.getZ()));
        try {
            if (!TopologyMapManager.isWalkable(center.getX(), center.getY(), center.getZ())) {
                return findNearestFighterPosition(fightInfo, center);
            }
        }
        catch (Exception e) {
            FightViewUtils.m_logger.warn((Object)("La map topolologique pour " + center + " n'est pas charg\u00e9e"), (Throwable)e);
        }
        return center;
    }
    
    public static void removeFightRepresentation(final FightInfo fightInfo) {
        final AnimatedElement red = FightViewUtils.m_fightRepresentations1.remove(fightInfo.getId());
        if (red != null) {
            SimpleAnimatedElementManager.getInstance().removeAnimatedElement(red);
        }
        final AnimatedElement blue = FightViewUtils.m_fightRepresentations0.remove(fightInfo.getId());
        if (blue != null) {
            SimpleAnimatedElementManager.getInstance().removeAnimatedElement(blue);
        }
    }
    
    static void removeAllFightRepresentations() {
        final TObjectProcedure<AnimatedInteractiveElement> procedure = new TObjectProcedure<AnimatedInteractiveElement>() {
            @Override
            public boolean execute(final AnimatedInteractiveElement object) {
                SimpleAnimatedElementManager.getInstance().removeAnimatedElement(object);
                return true;
            }
        };
        FightViewUtils.m_fightRepresentations0.forEachValue(procedure);
        FightViewUtils.m_fightRepresentations1.forEachValue(procedure);
        FightViewUtils.m_fightRepresentations0.clear();
        FightViewUtils.m_fightRepresentations1.clear();
    }
    
    public static void changeFightersVisibility(final FightInfo fightInfo, final VisibilityType type) {
        switch (type) {
            case HIDE: {
                hideCharacters(fightInfo.getFighters());
                break;
            }
            case VEIL: {
                veilCharacters(fightInfo.getFighters());
                break;
            }
        }
    }
    
    public static void removeFightersTeamCircle(final FightInfo fight) {
        final Collection<CharacterInfo> fighters = fight.getFighters();
        for (final CharacterInfo fighter : fighters) {
            fighter.getActor().clearTeamParticleSystem();
            fighter.getActor().clearDirectionParticleSystem();
        }
    }
    
    public static void hideFightEffectAreas(final FightInfo fightInfo) {
        final BasicEffectAreaManager effectAreaManager = fightInfo.getEffectAreaManager();
        if (fightInfo instanceof Fight) {
            StaticEffectAreaDisplayer.getInstance().removeStaticEffectArea(((Fight)fightInfo).getBattlegroundBorderEffectArea());
        }
        if (effectAreaManager != null) {
            for (final BasicEffectArea basicEffectArea : effectAreaManager.getActiveEffectAreas()) {
                hideEffectArea(basicEffectArea);
            }
            refreshEffectAreaDisplayer();
        }
        StaticEffectAreaDisplayer.getInstance().update();
    }
    
    public static void showFightEffectAreas(final FightInfo fightInfo) {
        final BasicEffectAreaManager effectAreaManager = fightInfo.getEffectAreaManager();
        final Fight fight = (Fight)fightInfo;
        final AbstractBattlegroundBorderEffectArea area = fight.getBattlegroundBorderEffectArea();
        StaticEffectAreaDisplayer.getInstance().pushStaticEffectArea(area, fightInfo);
        if (effectAreaManager != null) {
            for (final BasicEffectArea basicEffectArea : effectAreaManager.getActiveEffectAreas()) {
                showEffectAreaIfNecessary(fightInfo, basicEffectArea);
            }
        }
        refreshEffectAreaDisplayer();
        StaticEffectAreaDisplayer.getInstance().update();
    }
    
    public static void fadeOutLightModifier(final FightInfo fightInfo) {
        final InOutCellLightModifier lightModifier = fightInfo.getCellLightModifier();
        if (lightModifier != null) {
            lightModifier.fadeOut();
        }
    }
    
    public static void fadeInLightModifier(final FightInfo fightInfo) {
        final InOutCellLightModifier lightModifier = fightInfo.getCellLightModifier();
        if (lightModifier != null) {
            lightModifier.fadeIn();
        }
    }
    
    private static void hideCharacters(final Collection<CharacterInfo> fighters) {
        for (final CharacterInfo fighter : fighters) {
            hideCharacter(fighter);
        }
    }
    
    private static void veilCharacters(final Collection<CharacterInfo> fighters) {
        for (final CharacterInfo fighter : fighters) {
            veilCharacter(fighter);
        }
    }
    
    static void showCharacter(final CharacterInfo character) {
        final CharacterActor actor = character.getActor();
        actor.setDesiredAlpha(actor.getOriginalAlpha());
        if (character.isInvisibleForLocalPlayer()) {
            return;
        }
        actor.setVisible(true);
    }
    
    static void veilCharacter(final CharacterInfo character) {
        character.getActor().enableAlphaMask(false);
        character.getActor().setDesiredAlpha(0.6f);
        if (character.isInvisibleForLocalPlayer()) {
            return;
        }
        character.getActor().setVisible(true);
    }
    
    static void hideCharacter(final CharacterInfo character) {
        character.getActor().setVisible(false);
        character.getActor().enableAlphaMask(false);
    }
    
    static void refreshEffectAreaDisplayer() {
        ((WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene()).addHighlightCellProvidersToUpdate(StaticEffectAreaDisplayer.getInstance());
    }
    
    static void showEffectAreaIfNecessary(final FightInfo fightInfo, final BasicEffectArea area) {
        if (fightInfo.shouldDisplayAreaForLocalPlayer(area)) {
            StaticEffectAreaDisplayer.getInstance().pushStaticEffectArea(area, fightInfo);
        }
    }
    
    static void hideEffectArea(final BasicEffectArea effectArea) {
        StaticEffectAreaDisplayer.getInstance().removeStaticEffectArea(effectArea);
    }
    
    public static void activateFightObservationView(final FightInfo fightInfo) {
        FightVisibilityManager.getInstance().setZoomFactorBeforeFight(WakfuClientInstance.getInstance().getWorldScene().getIsoCamera().getZoomFactor());
        final AleaIsoCamera camera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
        final Fight fight = (Fight)fightInfo;
        final CharacterInfo currentFighter = fight.getTimeline().getCurrentFighter();
        if (currentFighter != null) {
            camera.setTrackingTarget(currentFighter.getActor());
            camera.alignOnTrackingTarget();
        }
        camera.lockMaskKey(true);
        IsoSceneLightManager.INSTANCE.addLightingModifier((LitSceneModifier)fightInfo.getCellLightModifier());
    }
    
    public static void deactivateFightObservationView(final FightInfo fightInfo) {
        WakfuClientInstance.getInstance().getWorldScene().setDesiredZoomFactor(FightVisibilityManager.getInstance().getZoomFactorBeforeFight());
        final AleaIsoCamera camera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
        camera.setTrackingTarget(WakfuGameEntity.getInstance().getLocalPlayer().getActor());
        camera.lockMaskKey(false);
        AlphaMaskCommand.applyAlphaMaskOnFight(false);
        HideFightOccluders.hideFightOccluders(0);
        IsoSceneLightManager.INSTANCE.removeLightingModifier((LitSceneModifier)fightInfo.getCellLightModifier());
    }
    
    public static void updateFloorItemsViews() {
        FloorItemManager.getInstance().foreachFloorItem(new TObjectProcedure<FloorItem>() {
            @Override
            public boolean execute(final FloorItem floorItem) {
                floorItem.getFloorItemInteractiveElement().notifyViews();
                return true;
            }
        });
    }
    
    public static void getFightRepresentations(final ArrayList<AnimatedInteractiveElement> elements) {
        final TObjectProcedure<AnimatedInteractiveElement> procedure = new TObjectProcedure<AnimatedInteractiveElement>() {
            @Override
            public boolean execute(final AnimatedInteractiveElement object) {
                elements.add(object);
                return true;
            }
        };
        FightViewUtils.m_fightRepresentations0.forEachValue(procedure);
        FightViewUtils.m_fightRepresentations1.forEachValue(procedure);
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightViewUtils.class);
        m_fightRepresentations0 = new TIntObjectHashMap<AnimatedInteractiveElement>();
        m_fightRepresentations1 = new TIntObjectHashMap<AnimatedInteractiveElement>();
    }
}
