package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUBecameFightSpectatorAction extends AbstractMRUAction implements MobileEndPathListener
{
    protected Point3 m_pathDestination;
    
    @Override
    public MRUActions tag() {
        return MRUActions.ATTEND_FIGHT;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        final Point3 nearestCell = this.findNearestExternalFightCell();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        this.m_pathDestination = nearestCell;
        if (!localPlayer.cancelCurrentOccupation(false, true)) {
            return;
        }
        localPlayer.getActor().addEndPositionListener(this);
        if (!localPlayer.moveTo(this.m_pathDestination, false, true)) {
            localPlayer.getActor().removeEndPositionListener(this);
            final Point3 coordinates = localPlayer.getActor().getWorldCoordinates();
            if (Math.abs(coordinates.getX() - this.m_pathDestination.getX()) <= 1 && Math.abs(coordinates.getY() - this.m_pathDestination.getY()) <= 1) {
                this.setDirectionTowardFight();
                this.sendFightAttendRequest();
                return;
            }
            this.displayNoPathToBorderChatErrorMessage();
        }
    }
    
    private void sendFightAttendRequest() {
        final int fightId = ((CharacterInfo)this.m_source).getCurrentFightId();
        if (FightVisibilityManager.getInstance().getParticipatingFight() == fightId) {
            return;
        }
        final BecameFightSpectatorRequestMessage request = new BecameFightSpectatorRequestMessage();
        request.setFightId(fightId);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request);
    }
    
    public Point3 findNearestExternalFightCell() {
        final CharacterInfo sourceInfo = (CharacterInfo)this.m_source;
        final FightInfo fight = FightManager.getInstance().getFightById(sourceInfo.getCurrentFightId());
        if (fight == null) {
            return null;
        }
        final Point3 nearestBorderCell = this.findNearestBorderCell(fight);
        return nearestBorderCell;
    }
    
    private Point3 findNearestBorderCell(final FightInfo fight) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Point3 playerPosition = localPlayer.getPosition();
        final Point3 res = new Point3(playerPosition);
        int nearestDistance = Integer.MAX_VALUE;
        final FightMap fightMap = fight.getFightMap();
        final int minX = fightMap.getMinX();
        final int minY = fightMap.getMinY();
        final Point3 temporaryForLoop = new Point3();
        for (int x = minX; x < minX + fightMap.getWidth(); ++x) {
            for (int y = minY; y < minY + fightMap.getHeight(); ++y) {
                if (fightMap.isBorder(x, y)) {
                    final short z = fightMap.getCellHeight(x, y);
                    final int dist = playerPosition.getDistance(x, y, z);
                    if (dist < nearestDistance) {
                        temporaryForLoop.set(x, y, z);
                        this.findNotInFightCellCloseTo(fight, temporaryForLoop);
                        if (!temporaryForLoop.equals(x, y)) {
                            if (TopologyMapManager.isWalkable(temporaryForLoop.getX(), temporaryForLoop.getY(), temporaryForLoop.getZ())) {
                                nearestDistance = dist;
                                res.set(temporaryForLoop);
                                if (nearestDistance == 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return res;
    }
    
    private Point3 findNotInFightCellCloseTo(final FightInfo fight, final Point3 borderCell) {
        final FightMap fightMap = fight.getFightMap();
        if (!fightMap.isInsideOrBorder(borderCell.getX() + 1, borderCell.getY())) {
            borderCell.add(1, 0, 0);
        }
        else if (!fightMap.isInsideOrBorder(borderCell.getX(), borderCell.getY() + 1)) {
            borderCell.add(0, 1, 0);
        }
        else if (!fightMap.isInsideOrBorder(borderCell.getX() - 1, borderCell.getY())) {
            borderCell.sub(1, 0, 0);
        }
        else if (!fightMap.isInsideOrBorder(borderCell.getX(), borderCell.getY() - 1)) {
            borderCell.sub(0, 1, 0);
        }
        return borderCell;
    }
    
    private void displayNoPathToBorderChatErrorMessage() {
        final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("fight.no.path.to.border"));
        chatMessage.setPipeDestination(3);
        ChatManager.getInstance().pushMessage(chatMessage);
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        if (this.m_pathDestination.getX() != x || this.m_pathDestination.getY() != y) {
            return;
        }
        this.setDirectionTowardFight();
        this.sendFightAttendRequest();
    }
    
    private void setDirectionTowardFight() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterInfo sourceInfo = (CharacterInfo)this.m_source;
        final FightInfo fight = FightManager.getInstance().getFightById(sourceInfo.getCurrentFightId());
        if (fight != null) {
            final Direction8 direction = localPlayer.getPosition().getDirectionTo(fight.getFightMap().getApproximateBubbleCenter());
            if (direction != null) {
                localPlayer.getActor().setDirectionWithNotification(direction);
            }
        }
    }
    
    @Override
    public boolean isRunnable() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return !localPlayer.isOnFight() && !ClientTradeHelper.INSTANCE.isTradeRunning() && localPlayer.getCurrentOrObservedFight() == null && this.m_source instanceof CharacterInfo && ((CharacterInfo)this.m_source).isOnFight() && !((CharacterInfo)this.m_source).isDead();
    }
    
    @Override
    public String getTranslatorKey() {
        return "becameFightSpectator";
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUBecameFightSpectatorAction();
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.SPECTATOR.m_id;
    }
}
