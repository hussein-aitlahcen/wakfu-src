package com.ankamagames.wakfu.client.ui.protocol.frame;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;
import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import com.ankamagames.wakfu.common.game.nation.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.alea.environment.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.wakfu.common.game.resource.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.common.game.groundType.*;
import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;

public class UISeedInteractionFrame extends UIAbstractItemUseInteractionFrame
{
    private static final Logger m_logger;
    private static final UISeedInteractionFrame m_instance;
    protected final ArrayList<FreeParticleSystem> m_systems;
    
    public UISeedInteractionFrame() {
        super();
        this.m_systems = new ArrayList<FreeParticleSystem>();
    }
    
    public static UISeedInteractionFrame getInstance() {
        return UISeedInteractionFrame.m_instance;
    }
    
    @Override
    protected void addToSelection(final Point3 target) {
        this.m_elementSelection.add(target.getX(), target.getY(), target.getZ());
    }
    
    @Override
    protected void refreshParticles() {
        if (!this.m_systems.isEmpty()) {
            return;
        }
        for (final int particleSystemId : this.getParticleSystems()) {
            final FreeParticleSystem freeParticleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(particleSystemId);
            if (freeParticleSystem != null) {
                this.m_systems.add(freeParticleSystem);
                freeParticleSystem.setTarget(this.m_isoWorldTarget);
                IsoParticleSystemManager.getInstance().addParticleSystem(freeParticleSystem);
            }
        }
    }
    
    @Override
    public void onSeedSucceed() {
        final SeedItemAction action = (SeedItemAction)this.m_item.getReferenceItem().getItemAction();
        final int resourceId = action.getResourceId();
        final ReferenceResource resource = ReferenceResourceManager.getInstance().getReferenceResource(resourceId);
        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventSeedPlanted(resourceId, resource.getResourceType()));
        WakfuGameEntity.getInstance().getLocalPlayer().getCraftHandler().onPlantationSuccess(action.getCraftId());
        final Point3 currentPosition = this.m_runningSeedAction.getTarget();
        if (this.canAffordSeedActionCount(1)) {
            this.refreshCursorDisplay(this.m_lastTarget, true);
        }
        if (this.hasSeedActionOnTarget(currentPosition)) {
            this.removeTargetFromSelection(currentPosition);
            this.removeSeedActionsOnTarget(currentPosition);
        }
    }
    
    @Override
    protected void sendItemActionRequest(final int seedPositionX, final int seedPositionY) {
        ((SeedItemAction)this.m_item.getReferenceItem().getItemAction()).sendRequest(this.m_item, seedPositionX, seedPositionY);
    }
    
    private boolean isLocalPlayerZoneSubscribed() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer);
    }
    
    @Override
    protected String getMouseInfoText() {
        if (this.isNoFeedbackMode()) {
            return "";
        }
        if (this.m_lastTarget == null) {
            return "";
        }
        if (!this.isLocalPlayerZoneSubscribed()) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.openText().addColor(Color.RED.getRGBtoHex());
            sb.append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed"));
            sb.closeText();
            return sb.finishAndToString();
        }
        final int percent = this.checkValidity(this.m_lastTarget);
        final String lawText = this.getLawText();
        return (percent == -1) ? "X" : (percent + " %" + ((lawText == null) ? "" : lawText));
    }
    
    private String getLawText() {
        final NationAlignement alignment = AbstractLawMRUAction.getCurrentNationAlignment();
        if (alignment == null) {
            return null;
        }
        final TextWidgetFormater sb = new TextWidgetFormater();
        final boolean leadToEnemyStateAction = this.leadToEnemyStateAction();
        if (leadToEnemyStateAction) {
            sb.openText().addColor(Color.RED.getRGBtoHex());
            sb.append("\n").append(WakfuTranslator.getInstance().getString("desc.mru.illegalAction"));
            sb.closeText();
        }
        final List<NationLaw> goodLaws = this.getTriggeredGoodLaws();
        if (alignment == NationAlignement.ALLIED && !goodLaws.isEmpty()) {
            sb.newLine().openText().addColor(Color.GREEN.getRGBtoHex());
            sb.b().append(WakfuTranslator.getInstance().getString("nation.law.mru.good"))._b();
            sb.closeText();
            AbstractLawMRUAction.listLaws(sb, goodLaws, Color.GREEN.getRGBtoHex(), leadToEnemyStateAction);
        }
        final List<NationLaw> badLaws = this.getTriggeredBadLaws();
        if (!badLaws.isEmpty()) {
            sb.newLine().openText().addColor(Color.RED.getRGBtoHex());
            sb.b().append(WakfuTranslator.getInstance().getString("nation.law.mru.bad"))._b();
            sb.closeText();
            AbstractLawMRUAction.listLaws(sb, badLaws, Color.RED.getRGBtoHex(), leadToEnemyStateAction);
        }
        return (sb.length() > 0) ? sb.finishAndToString() : null;
    }
    
    private boolean leadToEnemyStateAction() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (AbstractLawMRUAction.getCurrentNationAlignment() != NationAlignement.ENEMY || NationPvpHelper.isPvpActive(localPlayer.getCitizenComportment())) {
            return false;
        }
        final List<NationLaw> badLaws = this.getTriggeredBadLaws();
        return !badLaws.isEmpty();
    }
    
    private List<NationLaw> getTriggeredGoodLaws() {
        return (List<NationLaw>)NationLaw.getGoodLaws((List<NationLaw<NationLawEvent>>)this.getTriggeredLaws());
    }
    
    private List<NationLaw> getTriggeredBadLaws() {
        return (List<NationLaw>)NationLaw.getBadLaws((List<NationLaw<NationLawEvent>>)this.getTriggeredLaws());
    }
    
    private List<NationLaw> getTriggeredLaws() {
        final SeedItemAction action = (SeedItemAction)this.m_item.getReferenceItem().getItemAction();
        final int resourceId = action.getResourceId();
        final ReferenceResource reference = ReferenceResourceManager.getInstance().getReferenceResource(resourceId);
        if (reference == null) {
            return (List<NationLaw>)Collections.emptyList();
        }
        final Protector protector = ProtectorView.getInstance().getProtector();
        if (protector == null) {
            return (List<NationLaw>)Collections.emptyList();
        }
        final Nation nation = protector.getCurrentNation();
        if (nation == Nation.VOID_NATION) {
            return (List<NationLaw>)Collections.emptyList();
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ArrayList<WakfuEcosystemFamilyInfo> wakfuEcosystemFamilyInfos = new ArrayList<WakfuEcosystemFamilyInfo>();
        if (reference instanceof MonsterReferenceResource) {
            for (final int familyId : ((MonsterReferenceResource)reference).getMonsterFamilies()) {
                wakfuEcosystemFamilyInfos.add(WakfuMonsterZoneManager.getInstance().getMonsterFamilyInfo(familyId));
            }
        }
        else {
            wakfuEcosystemFamilyInfos.add(WakfuResourceZoneManager.getInstance().getResourceFamilyInfo(reference.getResourceType()));
        }
        final ArrayList<NationLaw> triggeredLaws = new ArrayList<NationLaw>();
        for (final WakfuEcosystemFamilyInfo wakfuEcosystemFamilyInfo : wakfuEcosystemFamilyInfos) {
            if (wakfuEcosystemFamilyInfo != null && wakfuEcosystemFamilyInfo.hasProtectorInterval() && !wakfuEcosystemFamilyInfo.isInProtectorInterval()) {
                final ProtectorWishLawEvent wishLawEvent = new ProtectorWishLawEvent(localPlayer);
                final int diffFromMax = wakfuEcosystemFamilyInfo.getProtectorIntervalDiffFromMax();
                if (diffFromMax == 0) {
                    continue;
                }
                ProtectorWishLawEvent.ActionType type;
                if (diffFromMax < 0) {
                    type = ProtectorWishLawEvent.ActionType.FOLLOWING;
                }
                else {
                    type = ProtectorWishLawEvent.ActionType.AGAINST;
                }
                wishLawEvent.setAction(type);
                triggeredLaws.addAll(wishLawEvent.getTriggeringLaws());
            }
        }
        return triggeredLaws;
    }
    
    private List<Integer> getParticleSystems() {
        if (this.leadToEnemyStateAction()) {
            return Collections.singletonList(800127);
        }
        final List<NationLaw> triggeredLaws = this.getTriggeredLaws();
        if (!triggeredLaws.isEmpty()) {
            int particleId;
            if (triggeredLaws.get(0).getBasePointsModification() > 0) {
                particleId = 800191;
            }
            else {
                particleId = 800192;
            }
            return Collections.singletonList(particleId);
        }
        return Collections.emptyList();
    }
    
    @Override
    protected int checkValidity(final Point3 target) {
        if (this.m_item == null) {
            return -1;
        }
        final Resource resource = ResourceManager.getInstance().getResource(target.getX(), target.getY());
        if (resource != null) {
            return -1;
        }
        final WakfuClientEnvironmentMap map = (WakfuClientEnvironmentMap)EnvironmentMapManager.getInstance().getMapFromCell(target.getX(), target.getY());
        if (map == null) {
            return -1;
        }
        final short groundType = map.getGroundTypeFromWorld(target.getX(), target.getY(), target.getZ());
        if (groundType == 0) {
            return -1;
        }
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!this.isLocalPlayerZoneSubscribed()) {
            return -1;
        }
        final DimensionalBagView visitingBag = player.getVisitingDimentionalBag();
        if (visitingBag != null && !visitingBag.canPlayerInteractWithContentInRoom(player, target.getX(), target.getY())) {
            return -1;
        }
        final SeedItemAction action = (SeedItemAction)this.m_item.getReferenceItem().getItemAction();
        final int resourceId = action.getResourceId();
        final ReferenceResource reference = ReferenceResourceManager.getInstance().getReferenceResource(resourceId);
        if (reference == null) {
            return -1;
        }
        if (reference instanceof MonsterReferenceResource) {
            if (TopologyMapManager.isMoboSterileOrNotWalkable(target.getX(), target.getY(), target.getZ())) {
                return -1;
            }
            if (!this.checkFamily((MonsterReferenceResource)reference)) {
                return -1;
            }
        }
        final GroundType gType = GroundManager.getInstance().getGroundType(groundType);
        if (gType == null) {
            return -1;
        }
        final WeatherInfo weatherInfo = WeatherInfoManager.getInstance().getCurrentWeather().getWeatherInfo();
        final double fertility = ResourceSproutHelper.getChancesToGrow(player, reference, gType, (weatherInfo == null) ? -1.0f : weatherInfo.getPrecipitations());
        return (int)((fertility <= 0.0) ? -1L : Math.round(fertility * 100.0));
    }
    
    private boolean checkFamily(final MonsterReferenceResource monsterReferenceResource) {
        if (HavenWorldManager.INSTANCE.hasHavenWorld()) {
            return true;
        }
        for (final int familyId : monsterReferenceResource.getMonsterFamilies()) {
            if (WakfuMonsterZoneManager.getInstance().getMonsterFamilyInfo(familyId) == null) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    protected void clearParticles() {
        for (final FreeParticleSystem freeParticleSystem : this.m_systems) {
            freeParticleSystem.kill();
        }
        this.m_systems.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)UISeedInteractionFrame.class);
        m_instance = new UISeedInteractionFrame();
    }
}
