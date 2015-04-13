package com.ankamagames.wakfu.client.alea.highlightingCells;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.baseImpl.graphics.alea.cellSelector.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import com.ankamagames.wakfu.client.core.effectArea.graphics.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.client.core.effectArea.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import gnu.trove.*;

public class StaticEffectAreaDisplayer implements CustomTextureHighlightingProvider
{
    protected static final Logger m_logger;
    private FightInfo m_fight;
    private static final float[] STATIC_ZONE_EFFECT_COLOR;
    private static final String PREFIX_SELECTION_NAME = "STATIC_EFFECT";
    private static final StaticEffectAreaDisplayer m_instance;
    private static int NEXT_SELECTION_ID;
    private final Stack<BasicEffectArea> m_effectAreaWaitingToBeAdd;
    private final HashMap<BasicEffectArea, ElementSelection> m_areaSelections;
    private final HashMap<BasicEffectArea, AnimatedElement> m_areaAnimations;
    private final TLongObjectHashMap<TIntArrayList> m_particles;
    private static final TIntProcedure REMOVE_PARTICLE_PROCEDURE;
    
    public static StaticEffectAreaDisplayer getInstance() {
        return StaticEffectAreaDisplayer.m_instance;
    }
    
    private static int getNextSelectionId() {
        if (StaticEffectAreaDisplayer.NEXT_SELECTION_ID == Integer.MAX_VALUE) {
            return 1;
        }
        return StaticEffectAreaDisplayer.NEXT_SELECTION_ID++;
    }
    
    private StaticEffectAreaDisplayer() {
        super();
        this.m_effectAreaWaitingToBeAdd = new Stack<BasicEffectArea>();
        this.m_areaSelections = new HashMap<BasicEffectArea, ElementSelection>();
        this.m_areaAnimations = new HashMap<BasicEffectArea, AnimatedElement>();
        this.m_particles = new TLongObjectHashMap<TIntArrayList>();
    }
    
    public void addStaticEffectArea(final BasicEffectArea area) {
        final GraphicalArea graphicalArea = ((GraphicalAreaProvider)area).getGraphicalArea();
        if (graphicalArea.hasAnimation()) {
            this.applyAnimation(area, graphicalArea);
        }
        if (this.m_particles.containsKey(area.getId())) {
            return;
        }
        if (graphicalArea.hasAPS()) {
            try {
                this.applyCentralParticle(area, graphicalArea);
            }
            catch (NumberFormatException e) {
                StaticEffectAreaDisplayer.m_logger.error((Object)("Aps au format \u00e9trange : '" + graphicalArea.getAPS() + "' pour l'area " + graphicalArea.getAnimationFile() + "-" + graphicalArea.getId()));
                return;
            }
        }
        final ElementSelection selection = this.createElementSelection(area, graphicalArea);
        if (selection == null) {
            if (!graphicalArea.hasCellAPS()) {
                return;
            }
            if (area.getArea() == null) {
                return;
            }
        }
        if (area.getArea() != null) {
            int areaX = area.getWorldCellX();
            int areaY = area.getWorldCellY();
            final short areaZ = area.getWorldCellAltitude();
            final EffectUser owner = area.getOwner();
            Iterable<int[]> pattern = null;
            if (area instanceof AbstractWarpEffectArea) {
                pattern = ((AbstractWarpEffectArea)area).getPattern();
                areaX = 0;
                areaY = 0;
            }
            else if (owner != null) {
                pattern = area.getArea().getCells(areaX, areaY, areaZ, owner.getWorldCellX(), owner.getWorldCellY(), owner.getWorldCellAltitude(), owner.getDirection());
                areaX = 0;
                areaY = 0;
            }
            else {
                pattern = area.getArea().getPattern();
            }
            if (this.m_fight != null) {
                final FightMap fightMap = this.m_fight.getFightMap();
                if (pattern != null) {
                    for (final int[] offset : pattern) {
                        final int x = areaX + offset[0];
                        final int y = areaY + offset[1];
                        if (fightMap.isInsideOrBorder(x, y)) {
                            final short z = fightMap.getCellHeight(x, y);
                            if (selection != null && !selection.contains(x, y, z)) {
                                selection.add(x, y, z);
                            }
                            if (!graphicalArea.hasCellAPS()) {
                                continue;
                            }
                            this.addCellParticle(graphicalArea, x, y, z);
                        }
                    }
                }
            }
            else {
                for (final int[] offset2 : pattern) {
                    final int x2 = areaX + offset2[0];
                    final int y2 = areaY + offset2[1];
                    final DisplayedScreenElement screenElement = DisplayedScreenWorld.getInstance().getNearesetWalkableElement(x2, y2, areaZ, ElementFilter.NOT_EMPTY);
                    if (screenElement == null) {
                        continue;
                    }
                    final short z = screenElement.getElement().getCellZ();
                    if (selection != null && !selection.contains(x2, y2, z)) {
                        selection.add(x2, y2, z);
                    }
                    if (!graphicalArea.hasCellAPS()) {
                        continue;
                    }
                    this.addCellParticle(graphicalArea, x2, y2, z);
                }
            }
        }
        else {
            StaticEffectAreaDisplayer.m_logger.error((Object)"area.getArea() is null");
        }
        if (selection != null) {
            this.m_areaSelections.put(area, selection);
        }
    }
    
    @Nullable
    private ElementSelection createElementSelection(final BasicEffectArea area, final GraphicalArea graphicalArea) {
        if (!graphicalArea.hasCellTexture()) {
            return null;
        }
        if (this.m_areaSelections.containsKey(area)) {
            return null;
        }
        String textureFilePath = null;
        try {
            textureFilePath = WakfuConfiguration.getInstance().getString("highLightGfxPath") + graphicalArea.getCellTextureFile() + ".tga";
        }
        catch (Exception e) {
            StaticEffectAreaDisplayer.m_logger.error((Object)"Exception", (Throwable)e);
        }
        if (textureFilePath != null) {
            final Texture texture = ElementSelection.createTexture(textureFilePath);
            return new ElementSelection("STATIC_EFFECT" + getNextSelectionId(), StaticEffectAreaDisplayer.STATIC_ZONE_EFFECT_COLOR, texture, HighLightTextureApplication.ISO);
        }
        StaticEffectAreaDisplayer.m_logger.error((Object)"Chemin vers la source nulle");
        return null;
    }
    
    private void applyCentralParticle(final BasicEffectArea area, final GraphicalArea graphicalArea) throws NumberFormatException {
        if (this.m_particles.contains(area.getId())) {
            return;
        }
        int apsId = Integer.parseInt(graphicalArea.getAPS());
        if (graphicalArea.hasSpecificAPSForCaster()) {
            final LocalPlayerCharacter lp = WakfuGameEntity.getInstance().getLocalPlayer();
            if (this.shouldUseCasterAps(area, lp)) {
                apsId = Integer.parseInt(graphicalArea.getSpecificAPSForCaster());
            }
            else if (this.shouldUseApsForAllies(area, lp)) {
                apsId = Integer.parseInt(graphicalArea.getSpecificAPSForAllies());
            }
        }
        final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(apsId, graphicalArea.getAPSLevel());
        final IsoWorldTarget target = graphicalArea.getAPSSpecificTarget();
        if (target != null) {
            system.setTarget(target);
        }
        else {
            system.setWorldPosition(area.getWorldCellX(), area.getWorldCellY(), area.getWorldCellAltitude());
        }
        this.addParticle(graphicalArea, system);
    }
    
    private boolean shouldUseCasterAps(final BasicEffectArea area, final LocalPlayerCharacter lp) {
        return area.getOwner() != null && area.getOwner() == lp;
    }
    
    private boolean shouldUseApsForAllies(final BasicEffectArea area, final LocalPlayerCharacter lp) {
        if (this.shouldUseCasterAps(area, lp)) {
            return false;
        }
        if (area instanceof FakeFighterEffectArea) {
            final boolean isSteamerRail = ((FakeFighterEffectArea)area).getUserDefinedId() == 6;
            return isSteamerRail && area.getTeamId() == lp.getTeamId();
        }
        return false;
    }
    
    private void applyAnimation(final BasicEffectArea area, final GraphicalArea graphicalArea) {
        if (this.m_areaAnimations.containsKey(area)) {
            return;
        }
        final int areaX = area.getWorldCellX();
        final int areaY = area.getWorldCellY();
        final short areaZ = area.getWorldCellAltitude();
        final EffectUser owner = area.getOwner();
        final boolean isBomb = area.getType() == EffectAreaType.BOMB.getTypeId();
        final boolean isBarrel = area.getType() == EffectAreaType.BARREL.getTypeId();
        final boolean isBombOrBarrel = isBomb || isBarrel;
        boolean previousMobileUsed = false;
        AnimatedInteractiveElement animatedElement;
        if (isBombOrBarrel && area instanceof MobileProvider) {
            final Mobile previousMobile = ((MobileProvider)area).getMobile();
            if (previousMobile != null) {
                animatedElement = previousMobile;
                previousMobileUsed = true;
            }
            else {
                animatedElement = new PathMobile(area.getId(), areaX, areaY, areaZ);
            }
            animatedElement.setWorldPosition(areaX, areaY, areaZ);
            final Mobile mobile = (Mobile)animatedElement;
            mobile.setFightId(this.m_fight.getId());
            mobile.setVisualHeight(graphicalArea.getVisualHeight());
            if (isBomb) {
                ((BombEffectArea)area).setMobile(mobile);
            }
            else if (isBarrel) {
                ((BarrelEffectArea)area).setMobile(mobile);
            }
        }
        else {
            animatedElement = new AnimatedInteractiveElement(area.getId(), (float)areaX, (float)areaY, (float)areaZ) {
                @Override
                public int getCurrentFightId() {
                    return StaticEffectAreaDisplayer.this.m_fight.getId();
                }
            };
            animatedElement.setVisualHeight(graphicalArea.getVisualHeight());
        }
        try {
            String gfxFile = WakfuConfiguration.getInstance().getString("ANMInteractiveElementPath");
            gfxFile = String.format(gfxFile, graphicalArea.getAnimationFile());
            animatedElement.load(gfxFile, true);
            if (isBarrel) {
                if (owner instanceof PlayerCharacter) {
                    ((Mobile)animatedElement).setCustomColor(1, BreedColorsManager.getInstance().getSkinColor((PlayerCharacter)owner).getCustomColor());
                }
                else if (owner instanceof NonPlayerCharacter) {
                    final MonsterSpecialGfx gfx = ((NonPlayerCharacter)owner).getBreedSpecialGfx();
                    gfx.foreachColor(new TObjectProcedure<MonsterSpecialGfx.Colors>() {
                        @Override
                        public boolean execute(final MonsterSpecialGfx.Colors color) {
                            if (color.m_partIndex == 1) {
                                MonsterSpecialGfxApplyer.applyColor((Mobile)animatedElement, color);
                            }
                            return true;
                        }
                    });
                }
            }
            animatedElement.setGfxId(FileHelper.getNameWithoutExt(graphicalArea.getAnimationFile()));
        }
        catch (PropertyException e) {
            StaticEffectAreaDisplayer.m_logger.error((Object)"", (Throwable)e);
        }
        catch (Exception e2) {
            StaticEffectAreaDisplayer.m_logger.error((Object)"", (Throwable)e2);
        }
        graphicalArea.setAnimatedElement(animatedElement);
        this.m_areaAnimations.put(area, animatedElement);
        if (isBombOrBarrel) {
            MobileManager.getInstance().addMobile((Mobile)animatedElement);
        }
        else {
            SimpleAnimatedElementManager.getInstance().addAnimatedElement(animatedElement);
        }
        if (!previousMobileUsed) {
            UIFightFrame.getInstance().hide(animatedElement);
            animatedElement.onAnmLoaded(new Runnable() {
                @Override
                public void run() {
                    if (animatedElement.containsAnimation("AnimOuverture")) {
                        animatedElement.setAnimation("AnimOuverture");
                    }
                }
            });
        }
    }
    
    private void addCellParticle(final GraphicalArea area, final int x, final int y, final short z) {
        int apsId;
        try {
            apsId = Integer.parseInt(area.getCellAPS());
        }
        catch (NumberFormatException e) {
            StaticEffectAreaDisplayer.m_logger.error((Object)("Aps au format \u00e9trange : '" + area.getCellAPS() + "' pour l'area " + area.getAnimationFile() + "-" + area.getId()));
            return;
        }
        final FreeParticleSystem particle = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(apsId);
        particle.setWorldPosition(x, y, z);
        this.addParticle(area, particle);
    }
    
    private void addParticle(final GraphicalArea area, final FreeParticleSystem particle) {
        if (particle == null) {
            return;
        }
        if (this.m_fight != null) {
            particle.setFightId(this.m_fight.getId());
        }
        IsoParticleSystemManager.getInstance().addParticleSystem(particle);
        TIntArrayList list = this.m_particles.get(area.getId());
        if (list == null) {
            list = new TIntArrayList();
            this.m_particles.put(area.getId(), list);
        }
        list.add(particle.getId());
    }
    
    public void update(final BasicEffectArea area) {
        switch (EffectAreaType.getTypeFromId(area.getType())) {
            case AURA:
            case BARREL:
            case BOMB: {
                final EffectContext context = area.getContext();
                if (context instanceof WakfuFightEffectContextInterface) {
                    final int fightId = ((WakfuFightEffectContextInterface)context).getFightId();
                    final FightInfo fight = FightManager.getInstance().getFightById(fightId);
                    if (fight != null) {
                        FightVisibilityManager.getInstance().onBasicEffectAreaTeleported(fight, area);
                    }
                    break;
                }
                break;
            }
        }
    }
    
    private AnimatedElement removeAreaAnimation(final BasicEffectArea area) {
        final Set<BasicEffectArea> areas = (Set<BasicEffectArea>)this.m_areaAnimations.keySet();
        for (final BasicEffectArea itArea : areas) {
            if (itArea.getId() == area.getId()) {
                return this.m_areaAnimations.remove(itArea);
            }
        }
        return null;
    }
    
    public void removeStaticEffectArea(final BasicEffectArea area) {
        if (area == null) {
            StaticEffectAreaDisplayer.m_logger.warn((Object)"On veut supprimer une area null");
            return;
        }
        final AnimatedElement animation = this.removeAreaAnimation(area);
        if (animation != null) {
            SimpleAnimatedElementManager.getInstance().removeAnimatedElement(animation);
            animation.dispose();
        }
        final ElementSelection selection = this.m_areaSelections.remove(area);
        if (selection != null) {
            selection.clear();
        }
        final long areaId = area.getId();
        final TIntArrayList tIntArrayList = this.m_particles.remove(areaId);
        if (tIntArrayList != null) {
            tIntArrayList.forEach(StaticEffectAreaDisplayer.REMOVE_PARTICLE_PROCEDURE);
        }
        for (final BasicEffectArea itArea : this.m_effectAreaWaitingToBeAdd) {
            if (itArea.getId() == areaId) {
                this.m_effectAreaWaitingToBeAdd.remove(itArea);
                break;
            }
        }
    }
    
    public void clear() {
        for (final AnimatedElement animation : this.m_areaAnimations.values()) {
            SimpleAnimatedElementManager.getInstance().removeAnimatedElement(animation);
            animation.dispose();
        }
        this.m_areaAnimations.clear();
        for (final ElementSelection selection : this.m_areaSelections.values()) {
            selection.clear();
        }
        this.m_areaSelections.clear();
        final TLongObjectIterator<TIntArrayList> iterator = this.m_particles.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            final TIntArrayList tIntArrayList = iterator.value();
            if (tIntArrayList != null) {
                tIntArrayList.forEach(StaticEffectAreaDisplayer.REMOVE_PARTICLE_PROCEDURE);
            }
        }
        this.m_particles.clear();
    }
    
    @Override
    public void update() {
        while (!this.m_effectAreaWaitingToBeAdd.empty()) {
            this.addStaticEffectArea(this.m_effectAreaWaitingToBeAdd.pop());
        }
    }
    
    public void pushStaticEffectArea(final BasicEffectArea area, final FightInfo fight) {
        this.m_effectAreaWaitingToBeAdd.push(area);
        this.m_fight = fight;
    }
    
    public FightInfo getFight() {
        return this.m_fight;
    }
    
    static {
        m_logger = Logger.getLogger((Class)StaticEffectAreaDisplayer.class);
        STATIC_ZONE_EFFECT_COLOR = new float[] { 1.0f, 1.0f, 1.0f, 0.9f };
        m_instance = new StaticEffectAreaDisplayer();
        StaticEffectAreaDisplayer.NEXT_SELECTION_ID = 1;
        REMOVE_PARTICLE_PROCEDURE = new TIntProcedure() {
            @Override
            public boolean execute(final int id) {
                IsoParticleSystemManager.getInstance().removeParticleSystem(id);
                return true;
            }
        };
    }
}
