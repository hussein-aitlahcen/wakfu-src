package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public class ChangeAnimStaticList extends List<Data>
{
    private static final Logger m_logger;
    private String m_defaultValue;
    
    @Override
    protected void onAdding(final CharacterActor actor, final Data data) {
        if (this.isEmpty()) {
            assert this.m_defaultValue == null;
            this.m_defaultValue = actor.getStaticAnimationKey();
        }
    }
    
    @Override
    public void onRemoved(final Data current, final Data removed, final CharacterActor actor) {
        if (actor.getAnmInstance() == null) {
            ChangeAnimStaticList.m_logger.error((Object)"Impossible de desappliquer le changement d'anim statique ");
            return;
        }
        actor.onAnmLoaded(new Runnable() {
            @Override
            public void run() {
                if (ChangeAnimStaticList.this.isEmpty()) {
                    actor.setStaticAnimationKey(ChangeAnimStaticList.this.m_defaultValue);
                    actor.setAnimation(ChangeAnimStaticList.this.m_defaultValue);
                    ChangeAnimStaticList.this.m_defaultValue = null;
                    actor.onAppearanceChangedExternally();
                }
                else {
                    final Data next = ChangeAnimStaticList.this.getLast();
                    if (!next.equals(current)) {
                        next.apply(actor);
                    }
                }
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChangeAnimStaticList.class);
    }
    
    public static class Data extends AdditionnalData
    {
        public final String m_animStaticKey;
        
        public Data(final WakfuRunningEffect effect, final String animStaticKey) {
            super(((RunningEffect<WakfuEffect, EC>)effect).getGenericEffect());
            this.m_animStaticKey = animStaticKey;
        }
        
        private Data(final Data dataToCopy) {
            super(dataToCopy.m_effect);
            this.m_animStaticKey = dataToCopy.m_animStaticKey;
        }
        
        @Override
        public Data duplicateForNewList() {
            return new Data(this);
        }
        
        @Override
        public void apply(final CharacterActor actor) {
            if (actor.getAnmInstance() == null) {
                ChangeAnimStaticList.m_logger.error((Object)("Impossible d'appliquer le changement d'anim statique " + this.m_animStaticKey));
                return;
            }
            actor.onAnmLoaded(new Runnable() {
                @Override
                public void run() {
                    if (actor.containsAnimation(Data.this.m_animStaticKey)) {
                        actor.setStaticAnimationKey(Data.this.m_animStaticKey);
                        actor.setAnimation(Data.this.m_animStaticKey);
                        actor.onAppearanceChangedExternally();
                    }
                }
            });
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
            if (this.m_animStaticKey != null) {
                if (this.m_animStaticKey.equals(data.m_animStaticKey)) {
                    return true;
                }
            }
            else if (data.m_animStaticKey == null) {
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + ((this.m_animStaticKey != null) ? this.m_animStaticKey.hashCode() : 0);
            return result;
        }
    }
}
