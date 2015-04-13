package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import java.util.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import java.nio.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.framework.graphics.engine.*;

public class SitOccupation extends AbstractOccupation
{
    private static final Logger m_logger;
    private static final boolean DEBUG = false;
    private static final int[] LINKAGE;
    private final CharacterInfo m_user;
    private Stool m_stool;
    private AnimationEndedListener m_finishListener;
    private AnimationEndedListener m_begin;
    private AnimationEndedListener m_finish;
    
    public SitOccupation(final CharacterInfo user, final Stool stool) {
        super();
        this.m_user = user;
        this.m_stool = stool;
    }
    
    public SitOccupation(final CharacterInfo user) {
        super();
        this.m_user = user;
        this.m_stool = null;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 16;
    }
    
    @Override
    public boolean isAllowed() {
        return true;
    }
    
    public void apply(final Stool stool) {
        if (this.m_stool != null) {
            return;
        }
        this.m_stool = stool;
        final CharacterActor actor = this.m_user.getActor();
        this.sitActor(actor);
        this.onActorSit(actor);
    }
    
    @Override
    public void begin() {
        if (this.m_user == this.m_localPlayer) {
            UIMRUFrame.getInstance().closeCurrentMRU();
        }
        final CharacterActor actor = this.m_user.getActor();
        this.m_user.setCurrentOccupation(this);
        actor.stopMoving();
        this.sitActor(actor);
        actor.addAnimationEndedListener(this.m_begin = new AnimationEndedListener() {
            @Override
            public void animationEnded(final AnimatedElement element) {
                actor.removeAnimationEndedListener(this);
                SitOccupation.this.onActorSit(actor);
                actor.update(null, 0);
                SitOccupation.this.m_begin = null;
            }
        });
    }
    
    @Override
    public boolean cancel(final boolean fromServer, final boolean sendMessage) {
        final CharacterActor actor = this.m_user.getActor();
        if (this.m_begin != null) {
            if (!fromServer) {
                return false;
            }
            this.m_begin.animationEnded(actor);
            this.finish();
        }
        if (this.m_finish != null) {
            if (!fromServer) {
                return false;
            }
            this.m_finish.animationEnded(actor);
        }
        this.resetActor(this.m_user.getActor(), this.getStoolView());
        if (this.m_user == this.m_localPlayer) {
            this.sendMessage((byte)3);
        }
        return true;
    }
    
    public final void askFinishOccupation(final AnimationEndedListener finishListener) {
        if (this.m_user == this.m_localPlayer) {
            this.m_finishListener = finishListener;
            this.sendMessage((byte)2);
        }
    }
    
    @Override
    public boolean finish() {
        final CharacterActor actor = this.m_user.getActor();
        if (actor.getMovementSelector() == SimpleMovementSelector.getInstance()) {
            return true;
        }
        final WakfuClientInteractiveAnimatedElementSceneView stoolview = this.getStoolView();
        if (stoolview != null) {
            stoolview.setVisible(false);
        }
        Direction8 stoolDirection;
        if (this.m_stool != null) {
            actor.setAnimation(this.m_stool.getAnimationName() + "-Fin");
            stoolDirection = (this.m_stool.comeFromFront() ? this.m_stool.getDirection() : this.m_stool.getDirection().opposite());
        }
        else {
            stoolDirection = Direction8.SOUTH_EAST;
        }
        final Point3 stoolPos = this.m_stool.getPosition();
        actor.setWorldPosition(stoolPos.getX() + stoolDirection.m_x, stoolPos.getY() + stoolDirection.m_y, stoolPos.getZ());
        this.m_finish = new AnimationEndedListener() {
            @Override
            public void animationEnded(final AnimatedElement element) {
                stoolview.setVisible(true);
                actor.removeAnimationEndedListener(this);
                SitOccupation.this.resetActor(actor, stoolview);
                actor.setAnimation(actor.getStaticAnimationKey());
                actor.setDirection(stoolDirection);
                if (SitOccupation.this.m_finishListener != null) {
                    SitOccupation.this.m_finishListener.animationEnded(element);
                }
                SitOccupation.this.m_finishListener = null;
                SitOccupation.this.m_finish = null;
                SitOccupation.this.m_user.setCurrentOccupation(null);
            }
        };
        actor.addStartPathListener(new MobileStartPathListener() {
            @Override
            public void pathStarted(final PathMobile mobile, final PathFindResult path) {
                if (SitOccupation.this.m_finish != null) {
                    SitOccupation.this.m_finish.animationEnded(actor);
                }
                actor.removeStartListener(this);
            }
        });
        actor.addAnimationEndedListener(this.m_finish);
        return false;
    }
    
    private void sendMessage(final byte modificationType) {
        final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
        netMsg.setModificationType(modificationType);
        netMsg.setOccupationType((short)16);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
    }
    
    private static void prepareActorForSitting(final CharacterActor actor, final Direction8 stoolDirection, final String animStatic) {
        actor.setDirection(stoolDirection);
        actor.setAnimationSuffix("-Assis");
        actor.setAnimation(animStatic + "-Debut");
        actor.setStaticAnimationKey(animStatic);
    }
    
    public void resetActor(final CharacterActor actor, final WakfuClientInteractiveAnimatedElementSceneView stoolview) {
        this.m_user.setCurrentInteractiveElement(null);
        Direction8 stoolDirection = Direction8.SOUTH_EAST;
        if (stoolview != null) {
            final Anm stoolAnm = stoolview.getAnm();
            if (stoolAnm != null) {
                actor.removeParts(stoolAnm, SitOccupation.LINKAGE);
            }
            stoolview.setVisible(true);
            stoolDirection = (this.m_stool.comeFromFront() ? stoolview.getDirection() : stoolview.getDirection().opposite());
        }
        actor.setStaticAnimationKey("AnimStatique");
        actor.setAnimationSuffix(null);
        actor.setMovementSelector(SimpleMovementSelector.getInstance());
        this.m_stool.removeCharacter();
        final Point3 stoolPos = this.m_stool.getPosition();
        final int x = stoolPos.getX() + stoolDirection.m_x;
        final int y = stoolPos.getY() + stoolDirection.m_y;
        final short stoolZ = stoolPos.getZ();
        short z = TopologyMapManager.getNearestWalkableZ(x, y, stoolZ);
        if (z == -32768) {
            z = stoolZ;
        }
        actor.setWorldPosition(x, y, z);
    }
    
    private WakfuClientInteractiveAnimatedElementSceneView getStoolView() {
        if (this.m_stool == null) {
            SitOccupation.m_logger.error((Object)("pas de banc pour le player: " + this.m_user.toString()));
            return null;
        }
        final Collection<ClientInteractiveElementView> views = this.m_stool.getViews();
        if (views.isEmpty()) {
            return null;
        }
        return views.iterator().next();
    }
    
    public boolean actorInTransition(final PlayerCharacter player) {
        final String animation = player.getActor().getAnimation();
        final String stoolAnimName = this.m_stool.getAnimationName();
        return animation.equals(stoolAnimName + "-Debut") || animation.equals(stoolAnimName + "-Fin");
    }
    
    private void sitActor(final CharacterActor actor) {
        final WakfuClientInteractiveAnimatedElementSceneView stoolview = this.getStoolView();
        this.m_user.setCurrentInteractiveElement(stoolview);
        final Direction8 stoolDirection = stoolview.getDirection();
        final String animationName = this.m_stool.getAnimationName();
        final Direction8 direction = this.m_stool.comeFromFront() ? stoolDirection.opposite() : stoolDirection;
        prepareActorForSitting(actor, direction, animationName);
        actor.setMovementSelector(NoneMovementSelector.getInstance());
        final Point3 position = this.m_stool.getPosition();
        final int dx = direction.m_x;
        final int dy = direction.m_y;
        actor.setWorldPosition(position.getX() - dx, position.getY() - dy, position.getZ());
        this.insertStoolInActor();
        stoolview.setVisible(false);
    }
    
    public final void insertStoolInActor() {
        final WakfuClientInteractiveAnimatedElementSceneView stoolview = this.getStoolView();
        if (stoolview == null) {
            SitOccupation.m_logger.error((Object)("pas de vue pour le banc pour assoir " + this.m_user.toString()));
            return;
        }
        final CharacterActor actor = this.m_user.getActor();
        actor.applyParts(stoolview.getAnm(), SitOccupation.LINKAGE);
    }
    
    private void onActorSit(final CharacterActor actor) {
        actor.setDirection(this.m_stool.getDirection());
        actor.setWorldPosition(this.m_stool.getPosition().getX(), this.m_stool.getPosition().getY(), this.m_stool.getPosition().getZ());
        actor.setAnimation(actor.getStaticAnimationKey());
    }
    
    @Override
    public boolean canPlayEmote() {
        return this.m_begin == null && this.m_finish == null;
    }
    
    public void build(final byte[] rawData) {
        final ByteBuffer bb = ByteBuffer.wrap(rawData);
        final long stoolId = bb.getLong();
        if (this.m_stool != null && this.m_stool.getId() != stoolId) {
            SitOccupation.m_logger.error((Object)"stoolId diff\u00e9rent ");
            this.m_stool = null;
        }
        if (this.m_stool != null) {
            return;
        }
        try {
            final Stool stool = (Stool)LocalPartitionManager.getInstance().getInteractiveElement(stoolId);
            if (stool != null) {
                this.apply(stool);
            }
        }
        catch (Exception e) {
            SitOccupation.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SitOccupation.class);
        LINKAGE = new int[] { Engine.getPartName("Stool") };
    }
}
