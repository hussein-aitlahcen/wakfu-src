package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.graphics.engine.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.skill.*;

public class CollectOccupation extends AbstractOccupation
{
    protected static final Logger m_logger;
    private final Resource m_resource;
    private final CollectAction m_action;
    private boolean m_waitForOthers;
    private long m_estimatedTime;
    private byte m_usedSlots;
    private AnimatedElementWithDirection m_consumableElement;
    
    public CollectOccupation(final CollectAction action, final Resource resource) {
        super();
        this.m_resource = resource;
        this.m_action = action;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 3;
    }
    
    @Override
    public boolean isAllowed() {
        if (this.m_action.getCraftId() != 0 && !this.m_localPlayer.getCraftHandler().contains(this.m_action.getCraftId())) {
            CollectOccupation.m_logger.error((Object)("le joueur ne poss\u00e8de pas ce skill " + this.m_action));
            return false;
        }
        return true;
    }
    
    @Override
    public void begin() {
        this.begin(0.0);
    }
    
    public void begin(final double initialProgress) {
        QueueCollectManager.getInstance().deleteTimeOut();
        if (this.m_localPlayer.getCurrentOccupation() != this) {
            CollectOccupation.m_logger.info((Object)("On d\u00e9marre l'occupation de collecte sur la ressource " + this.m_resource));
            this.m_localPlayer.cancelCurrentOccupation(false, true);
            this.m_localPlayer.setCurrentInteractiveElement(this.m_resource);
            final ClientConsumableInfo consumableInfo = this.m_action.getConsumableInfo();
            if (consumableInfo.hasConsumableGfx()) {
                this.startConsumableAnimation();
            }
            final CharacterActor actor = this.m_localPlayer.getActor();
            actor.setVisualAnimation(this.m_action.getVisualId(), false);
            this.m_localPlayer.setCurrentOccupation(this);
        }
        else {
            CollectOccupation.m_logger.info((Object)("[COLLECT_DEBUG] On red\u00e9marre une occupation de collecte plut\u00f4t que de la mettre \u00e0 jour. " + ExceptionFormatter.currentStackTrace(6)));
        }
        this.update(initialProgress);
    }
    
    private void startConsumableAnimation() {
        try {
            final ClientConsumableInfo consumableInfo = this.m_action.getConsumableInfo();
            this.m_consumableElement = new AnimatedElementWithDirection(GUIDGenerator.getGUID(), this.m_resource.getWorldCellX(), this.m_resource.getWorldCellY(), this.m_resource.getWorldCellAltitude());
            if (this.isFishing()) {
                this.m_consumableElement.load(String.format(WakfuConfiguration.getInstance().getString("ANMEquipmentPath"), "AnimHamecon"), true);
                final Anm anm = AnmManager.getInstance().loadAnmFile(String.format(WakfuConfiguration.getInstance().getString("ANMEquipmentPath"), consumableInfo.getConsumableGfxId()), true);
                this.m_consumableElement.applyParts(anm, Engine.getPartName("Accessoire"));
            }
            else {
                this.m_consumableElement.load(String.format(WakfuConfiguration.getInstance().getString("ANMEquipmentPath"), consumableInfo.getConsumableGfxId()), true);
            }
            this.m_consumableElement.setDeltaZ(this.m_resource.getDeltaZ() + 1);
            this.m_consumableElement.setGfxId(String.valueOf(consumableInfo.getConsumableGfxId()));
            this.m_consumableElement.setAnimation("AnimStatique-Debut");
            SimpleAnimatedElementManager.getInstance().addAnimatedElement(this.m_consumableElement);
        }
        catch (IOException e) {
            CollectOccupation.m_logger.error((Object)"Impossible de charger le fichier d'animation", (Throwable)e);
        }
        catch (PropertyException e2) {
            CollectOccupation.m_logger.error((Object)"Impossible de r\u00e9cup\u00e9rer la propri\u00e9t\u00e9 pour le chargement d'animation", (Throwable)e2);
        }
    }
    
    private void endConsumableAnimation() {
        this.m_consumableElement.setAnimation("AnimStatique-Fin");
        this.m_consumableElement.addAnimationEndedListener(new AnimationEndedListener() {
            @Override
            public void animationEnded(final AnimatedElement element) {
                CollectOccupation.this.m_consumableElement.removeAnimationEndedListener(this);
                SimpleAnimatedElementManager.getInstance().removeAnimatedElement(CollectOccupation.this.m_consumableElement);
                CollectOccupation.this.m_consumableElement = null;
            }
        });
    }
    
    public void update(final double progress) {
        this.m_localPlayer.getActionInProgress().startCollect(this.m_resource, this.m_action, this.m_estimatedTime, this.m_usedSlots, this.m_waitForOthers, progress);
    }
    
    @Override
    public boolean cancel(final boolean fromServer, final boolean sendMessage) {
        CollectOccupation.m_logger.info((Object)("Annulation de la collecte, relai au serveur " + sendMessage));
        this.m_localPlayer.getActionInProgress().endAction();
        if (sendMessage) {
            final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
            netMsg.setModificationType((byte)3);
            netMsg.setOccupationType(this.getOccupationTypeId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
        }
        final CharacterActor actor = this.m_localPlayer.getActor();
        actor.setVisualAnimation(this.m_action.getVisualId(), true);
        this.m_localPlayer.releaseSkillInfo();
        if (this.m_consumableElement != null) {
            this.endConsumableAnimation();
        }
        QueueCollectManager.getInstance().clear();
        return true;
    }
    
    @Override
    public boolean finish() {
        QueueCollectManager.getInstance().clearCurrentCollectAction();
        CollectOccupation.m_logger.info((Object)"Fin de l'occupation de collecte");
        final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
        netMsg.setModificationType((byte)2);
        netMsg.setOccupationType((short)3);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
        final CharacterActor actor = this.m_localPlayer.getActor();
        actor.setVisualAnimation(this.m_action.getVisualId(), true);
        this.m_localPlayer.getActionInProgress().endAction();
        this.m_localPlayer.releaseSkillInfo();
        if (this.m_consumableElement != null) {
            this.endConsumableAnimation();
        }
        this.m_localPlayer.getCraftHandler().onCollectSuccess(this.m_action.getCraftId(), this.m_action.isDestructive());
        return true;
    }
    
    public void setWaitForOthers(final boolean waitForOthers) {
        this.m_waitForOthers = waitForOthers;
    }
    
    public boolean isWaitingForOthers() {
        return this.m_waitForOthers;
    }
    
    public void setEstimatedTime(final long estimatedTime) {
        this.m_estimatedTime = estimatedTime;
    }
    
    public void setUsedSlots(final byte usedSlots) {
        this.m_usedSlots = usedSlots;
    }
    
    public long getEstimatedTime() {
        return this.m_estimatedTime;
    }
    
    private boolean isFishing() {
        final ActionVisual visual = ActionVisualManager.getInstance().get(this.m_action.getVisualId());
        return visual != null && visual.getAnimLink().contains("AnimMetier-Peche");
    }
    
    static {
        m_logger = Logger.getLogger((Class)CollectOccupation.class);
    }
}
