package com.ankamagames.baseImpl.graphics.game.interactiveElement;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.script.*;

public abstract class ClientInteractiveAnimatedElementSceneView extends AnimatedInteractiveElement implements ClientInteractiveElementView, AnimationEndedListener
{
    public static final short NOT_YET_INIT_STATE = Short.MIN_VALUE;
    private boolean m_canBeDeleted;
    private static final String ANIMTRANS = "AnimTrans_";
    private static final String ANIMSTATIQUE = "AnimStatique_";
    private static final String FUNCTION = "Func_";
    private static final String END = "End";
    private MonitoredPool m_pool;
    protected ClientMapInteractiveElement m_interactiveElement;
    private int m_viewModelId;
    private int m_gfx;
    protected short m_state;
    private boolean m_isOccluder;
    
    public ClientInteractiveAnimatedElementSceneView() {
        super();
        this.m_canBeDeleted = false;
        this.m_state = -32768;
    }
    
    protected void setPool(final MonitoredPool pool) {
        this.m_pool = pool;
    }
    
    @Override
    public long getId() {
        return this.m_interactiveElement.getId();
    }
    
    @Override
    public int getViewModelId() {
        return this.m_viewModelId;
    }
    
    @Override
    public void setViewModelId(final int viewModelId) {
        this.m_viewModelId = viewModelId;
    }
    
    public int getViewGfxId() {
        return this.m_gfx;
    }
    
    @Override
    public void setViewGfxId(final int gfx) {
        this.m_gfx = gfx;
    }
    
    @Override
    public void setColor(final int color) {
        final Color c = new Color(color);
        this.m_color[0] = c.getBlue();
        this.m_color[1] = c.getGreen();
        this.m_color[2] = c.getRed();
        final float[] color2 = this.m_color;
        final int n = 3;
        final float alpha = c.getAlpha();
        color2[n] = alpha;
        this.m_originalAlpha = alpha;
        this.m_desiredAlpha = alpha;
    }
    
    @Override
    public byte getViewHeight() {
        return (byte)this.getHeight();
    }
    
    @Override
    public void setViewHeight(final byte height) {
        this.setVisualHeight(height);
    }
    
    @Override
    public void setBehindMobile(final boolean behind) {
        final LayerOrder order = behind ? LayerOrder.IE : LayerOrder.IE;
        this.setDeltaZ(order.getDeltaZ());
    }
    
    @Override
    public void onCheckIn() {
        this.m_viewModelId = 0;
        this.m_gfx = 0;
        this.m_interactiveElement = null;
        this.m_pool = null;
        this.m_state = -32768;
        this.m_canBeDeleted = false;
        this.dispose();
    }
    
    @Override
    public void onCheckOut() {
        this.m_isOccluder = true;
    }
    
    @Override
    public void release() {
        if (this.m_pool != null) {
            try {
                this.m_pool.returnObject(this);
            }
            catch (Exception e) {
                ClientInteractiveAnimatedElementSceneView.m_logger.error((Object)"Exception lev\u00e9e du retour d'un objet dans son pool : ", (Throwable)e);
            }
        }
        else {
            ClientInteractiveAnimatedElementSceneView.m_logger.error((Object)"L'objet ne peut retourner dans un pool, il n'a pas \u00e9t\u00e9 initialis\u00e9");
        }
    }
    
    @Override
    public ClientMapInteractiveElement getInteractiveElement() {
        return this.m_interactiveElement;
    }
    
    @Override
    public void setInteractiveElement(@NotNull final ClientMapInteractiveElement element) {
        this.m_interactiveElement = element;
    }
    
    protected void selectAnimation(final byte state, final boolean useSpecificTransition, final TransitionModel transitionModel, final Direction8 direction) {
        final boolean changeState = this.m_state != state;
        final boolean changeDirection = this.m_direction != direction;
        if (!changeState && !changeDirection && transitionModel != TransitionModel.FORCE_TRANS) {
            final String currentAnimation = this.getAnimation();
            if (currentAnimation != null && !currentAnimation.equals("AnimStatique")) {
                return;
            }
        }
        this.setDirection(direction);
        String anim;
        if ((!changeState && changeDirection) || transitionModel == TransitionModel.FORCE_NO_TRANS) {
            anim = "AnimStatique_";
        }
        else {
            anim = this.getTransAnim(useSpecificTransition, changeState);
        }
        if (anim != null) {
            this.setAnimation(anim + state);
        }
        this.setStaticAnimationKey("AnimStatique_" + state);
        this.forceReloadAnimation();
    }
    
    private String getTransAnim(final boolean useSpecificTransition, final boolean changeState) {
        if (this.m_state == -32768) {
            return "AnimStatique_";
        }
        if (!useSpecificTransition) {
            return "AnimTrans_";
        }
        if (!changeState) {
            return null;
        }
        return "AnimTrans_" + this.m_state + "_";
    }
    
    public boolean canBeDeleted() {
        return this.m_canBeDeleted;
    }
    
    public void setCanBeDeleted(final boolean canBeDeleted) {
        this.m_canBeDeleted = canBeDeleted;
    }
    
    @Override
    public void animationEnded(final AnimatedElement element) {
        this.m_interactiveElement.onViewUpdated(this);
    }
    
    public boolean isOccluder() {
        return this.m_isOccluder;
    }
    
    public void setOccluder(final boolean occluder) {
        this.m_isOccluder = occluder;
    }
    
    public abstract JavaFunctionsLibrary[] getLibraries();
}
