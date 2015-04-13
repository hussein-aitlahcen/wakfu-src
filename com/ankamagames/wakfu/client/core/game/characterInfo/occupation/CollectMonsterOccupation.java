package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;

public class CollectMonsterOccupation extends AbstractOccupation
{
    protected static final Logger m_logger;
    private final CollectAction m_skillAction;
    private final NonPlayerCharacter m_monster;
    private long m_estimatedTime;
    
    public CollectMonsterOccupation(final CollectAction skillAction, final NonPlayerCharacter monster) {
        super();
        this.m_skillAction = skillAction;
        this.m_monster = monster;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 6;
    }
    
    @Override
    public boolean isAllowed() {
        if (this.m_skillAction != null) {
            final SimpleCriterion criterion = this.m_skillAction.getCriterion();
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            assert localPlayer == this.m_localPlayer;
            if (!localPlayer.getCraftHandler().contains(this.m_skillAction.getCraftId()) || (criterion != null && !criterion.isValid(localPlayer, this.m_monster, null, null))) {
                CollectMonsterOccupation.m_logger.error((Object)("le joueur ne poss\u00e8de pas ce skill " + this.m_skillAction.getCraftId()));
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void begin() {
        QueueCollectManager.getInstance().deleteTimeOut();
        CollectMonsterOccupation.m_logger.info((Object)"On d\u00e9marre l'occupation de collecte d'un monstre");
        this.m_localPlayer.cancelCurrentOccupation(false, true);
        this.m_localPlayer.setCurrentInteractiveElement(this.m_monster.getActor());
        final ActionVisual actionVisual = ActionVisualManager.getInstance().get(this.m_skillAction.getVisualId());
        if (actionVisual != null) {
            ActionVisualHelper.applyActionVisual(this.m_localPlayer.getActor(), actionVisual);
        }
        this.m_localPlayer.setCurrentOccupation(this);
        this.m_localPlayer.getActionInProgress().startCollectMonster(this.m_skillAction, this.m_estimatedTime);
    }
    
    @Override
    public boolean cancel(final boolean fromServer, final boolean sendMessage) {
        CollectMonsterOccupation.m_logger.info((Object)("Annulation de la collecte d'un monstre, relai au serveur " + sendMessage));
        this.m_localPlayer.getActionInProgress().endAction();
        if (sendMessage) {
            final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
            netMsg.setModificationType((byte)3);
            netMsg.setOccupationType(this.getOccupationTypeId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
        }
        this.m_localPlayer.getActor().setAnimation("AnimStatique");
        this.m_localPlayer.releaseSkillInfo();
        QueueCollectManager.getInstance().clear();
        return true;
    }
    
    @Override
    public boolean finish() {
        QueueCollectManager.getInstance().clearCurrentCollectAction();
        CollectMonsterOccupation.m_logger.info((Object)"Fin de l'occupation de collecte d'un monstre");
        final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
        netMsg.setModificationType((byte)2);
        netMsg.setOccupationType((short)6);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
        final String animation = AnimationConstants.setAnimationEndSuffix(this.m_localPlayer.getActor().getAnimation(), true);
        this.m_localPlayer.getActor().setAnimation(animation);
        this.m_localPlayer.getActionInProgress().endAction();
        this.m_localPlayer.releaseSkillInfo();
        this.m_localPlayer.getCraftHandler().onCollectSuccess(this.m_skillAction.getCraftId(), this.m_skillAction.isDestructive());
        return true;
    }
    
    public void setEstimatedTime(final long estimatedTime) {
        this.m_estimatedTime = estimatedTime;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CollectMonsterOccupation.class);
    }
}
