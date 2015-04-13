package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.action.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.characterInfo.action.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;

public class MRUMonsterAction extends AbstractMRUAction implements MobileEndPathListener
{
    private AbstractClientMonsterAction m_action;
    private boolean m_busy;
    public static final int ENABLED = 0;
    public static final int IS_NOT_SUBSCRIBED = 1;
    private int m_disabledReason;
    
    MRUMonsterAction() {
        super();
    }
    
    public MRUMonsterAction(final AbstractClientMonsterAction monsterAction) {
        super();
        this.m_action = monsterAction;
    }
    
    @Override
    public void initFromSource(final Object source) {
        super.initFromSource(source);
        this.m_busy = ((BasicCharacterInfo)this.m_source).isActiveProperty(WorldPropertyType.BUSY);
    }
    
    @Override
    public boolean isEnabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (this.m_action.getTypeId() == MonsterActionConstants.MANAGE_HAVEN_WORLD.getId() && !WakfuAccountPermissionContext.SUBSCRIBER.hasPermission(localPlayer)) {
            this.m_disabledReason = 1;
            return false;
        }
        if (this.m_action.getTypeId() == MonsterActionConstants.START_DIALOG.getId() && !WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer)) {
            this.m_disabledReason = 1;
            return false;
        }
        this.m_disabledReason = 0;
        return true;
    }
    
    @Override
    public boolean isRunnable() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return !localPlayer.isActiveProperty(WorldPropertyType.MONSTER_ACTION_DISABLED) && this.m_source instanceof NonPlayerCharacter && !((NonPlayerCharacter)this.m_source).isDead() && !((NonPlayerCharacter)this.m_source).hasProperty(WorldPropertyType.NPC_NO_MONSTER_ACTION) && this.m_action.isRunnable((NonPlayerCharacter)this.m_source);
    }
    
    @Override
    public void run() {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        player.getActor().addEndPositionListener(this);
        final int distance = player.getPosition().getDistance(((NonPlayerCharacter)this.m_source).getPosition());
        final boolean useDiagonal = distance > 2;
        if (!this.m_action.isMovePlayer() || (distance <= 1 && player.getActor().getCurrentPath() == null)) {
            this.runAction();
        }
        else {
            final List<Point3> dests = new ArrayList<Point3>();
            final Point3 target = ((NonPlayerCharacter)this.m_source).getPosition();
            dests.add(new Point3(target.getX() + 1, target.getY(), target.getZ()));
            dests.add(new Point3(target.getX() - 1, target.getY(), target.getZ()));
            dests.add(new Point3(target.getX(), target.getY() + 1, target.getZ()));
            dests.add(new Point3(target.getX(), target.getY() - 1, target.getZ()));
            if (!player.moveTo(false, useDiagonal, dests)) {
                player.getActor().removeEndPositionListener(this);
                ErrorsMessageTranslator.getInstance().pushMessage(2, 3, new Object[0]);
            }
        }
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        this.runAction();
    }
    
    private void runAction() {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterActor actor = player.getActor();
        final NonPlayerCharacter npc = (NonPlayerCharacter)this.m_source;
        actor.removeEndPositionListener(this);
        final int distance = player.getPosition().getDistance(npc.getPosition());
        if (this.m_action.isMovePlayer() && distance > 1) {
            return;
        }
        this.m_action.run(npc);
    }
    
    @Override
    protected int getGFXId() {
        return this.m_action.getVisual().getMruGfx();
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor((!MonsterActionConstants.getFromId(this.m_action.getTypeId()).isCanBeTriggeredWhenBusy() && this.m_busy) ? MRUMonsterAction.NOK_TOOLTIP_COLOR : MRUMonsterAction.DEFAULT_TOOLTIP_COLOR);
        sb.append(WakfuTranslator.getInstance().getString("desc.mru." + this.m_action.getVisual().getMruLabelKey()));
        sb._b();
        if (!this.isEnabled()) {
            switch (this.m_disabledReason) {
                case 1: {
                    sb.newLine().addColor(MRUMonsterAction.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed"));
                    break;
                }
            }
        }
        return sb.finishAndToString();
    }
    
    @Override
    public String getTranslatorKey() {
        return null;
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.MONSTER_ACTION;
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUMonsterAction(this.m_action);
    }
    
    @Override
    public String toString() {
        return "MRUMonsterAction{m_action=" + this.m_action + ", m_busy=" + this.m_busy + '}';
    }
}
