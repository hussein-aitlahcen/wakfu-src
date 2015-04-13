package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class LinkMobileList extends List<Data>
{
    private static final Logger m_logger;
    
    @Override
    protected void onAdding(final CharacterActor actor, final Data data) {
    }
    
    @Override
    public void clear(final CharacterActor actor) {
        final Data current = this.getLast();
        this.m_stack.clear();
        this.onRemoved(current, current, actor);
    }
    
    @Override
    public void onRemoved(final Data current, final Data removed, final CharacterActor actor) {
        if (removed.m_linked == null) {
            LinkMobileList.m_logger.warn((Object)"On essaye de d\u00e9sappliquer un mobile qui n'a pas \u00e9t\u00e9 appliqu\u00e9 correctement");
        }
        actor.unlinkChildMobile(removed.m_linked);
        MobileManager.getInstance().removeMobile(removed.m_linked.getId());
        removed.m_linked = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)LinkMobileList.class);
    }
    
    public static class Data extends AdditionnalData
    {
        public final String m_gfxId;
        public final String m_anim;
        public Actor m_linked;
        
        public Data(final WakfuRunningEffect effect, final String gfxId, final String anim) {
            super(((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect());
            this.m_gfxId = gfxId;
            this.m_anim = anim;
            this.m_linked = null;
        }
        
        private Data(final Data dataToCopy) {
            super(dataToCopy.m_effect);
            this.m_gfxId = dataToCopy.m_gfxId;
            this.m_anim = dataToCopy.m_anim;
            this.m_linked = null;
        }
        
        private static Direction8 getDirectionFrom(final String animName) {
            if (animName == null) {
                return null;
            }
            if (animName.charAt(1) == '_') {
                try {
                    final int dir = Integer.parseInt("" + animName.charAt(0));
                    return Direction8.getDirectionFromIndex(dir);
                }
                catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        }
        
        @Override
        public void apply(final CharacterActor actor) {
            final Actor linked = new Actor(GUIDGenerator.getGUID()) {
                @Override
                public void onInventoryEvent(final InventoryEvent event) {
                }
                
                @Override
                public int getIconId() {
                    return -1;
                }
                
                @Override
                public String getFormatedOverheadText() {
                    return "";
                }
            };
            linked.setGfx(this.m_gfxId);
            final Direction8 direction = getDirectionFrom(this.m_anim);
            final boolean useParentDirection = direction == null;
            if (useParentDirection) {
                linked.setDirection(actor.getDirection());
            }
            else {
                linked.setDirection(direction);
            }
            linked.setAnimation("AnimStatique");
            linked.setDeltaZ(LayerOrder.MOBILE.getDeltaZ());
            linked.setMovementSelector(NoneMovementSelector.getInstance());
            linked.setWorldPosition(actor.getWorldCellX(), actor.getWorldCellY(), actor.getWorldCellAltitude());
            MobileManager.getInstance().addMobile(linked);
            final boolean useParentAnimation = this.m_anim == null;
            if (!useParentAnimation) {
                linked.setAnimation(useParentDirection ? this.m_anim : this.m_anim.substring(2));
            }
            actor.linkMobile(linked, useParentAnimation, useParentAnimation, useParentDirection);
            this.m_linked = linked;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }
            final Data data = (Data)o;
            Label_0072: {
                if (this.m_anim != null) {
                    if (this.m_anim.equals(data.m_anim)) {
                        break Label_0072;
                    }
                }
                else if (data.m_anim == null) {
                    break Label_0072;
                }
                return false;
            }
            if (this.m_gfxId != null) {
                if (this.m_gfxId.equals(data.m_gfxId)) {
                    return true;
                }
            }
            else if (data.m_gfxId == null) {
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + ((this.m_gfxId != null) ? this.m_gfxId.hashCode() : 0);
            result = 31 * result + ((this.m_anim != null) ? this.m_anim.hashCode() : 0);
            return result;
        }
        
        @Override
        public Data duplicateForNewList() {
            return new Data(this);
        }
    }
}
