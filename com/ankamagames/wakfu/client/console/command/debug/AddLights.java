package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.graphics.engine.light.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class AddLights implements Command
{
    private static final Logger m_logger;
    ArrayList<IsoLightSource> lights;
    
    public AddLights() {
        super();
        this.lights = new ArrayList<IsoLightSource>();
    }
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (args.size() != 3) {
            if (this.lights.size() != 0) {
                for (int i = 0; i < this.lights.size(); ++i) {
                    IsoSceneLightManager.INSTANCE.removeLight(this.lights.get(i).getId());
                }
                this.lights.clear();
            }
            return;
        }
        try {
            final int nb = Integer.valueOf(args.get(2));
            final Point3 position = WakfuGameEntity.getInstance().getLocalPlayer().getPosition();
            for (int j = 0; j < nb; ++j) {
                final L l = new L(new DefaultIsoWorldTarget(position.getX(), position.getY(), position.getZ()), MathHelper.random(-0.5f, 0.5f), MathHelper.random(-0.5f, 0.5f));
                l.setSaturation(MathHelper.random(0.3f, 0.8f), MathHelper.random(0.3f, 0.8f), MathHelper.random(0.3f, 0.8f));
                l.setEnabled(true);
                l.setRange(3.0f);
                this.lights.add(l);
                IsoSceneLightManager.INSTANCE.addLight(l);
            }
        }
        catch (Exception e) {
            AddLights.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddLights.class);
    }
    
    static class L extends IsoLightSource
    {
        float speedX;
        float speedY;
        boolean flipX;
        boolean flipY;
        float x;
        float y;
        
        L(final IsoWorldTarget target, final float speedX, final float speedY) {
            super(target);
            this.speedX = speedX;
            this.speedY = speedY;
            this.x = target.getWorldCellX();
            this.y = target.getWorldCellY();
        }
        
        @Override
        public void update(final int deltaTime) {
            super.update(deltaTime);
            final IsoWorldTarget target = this.getTarget();
            target.setWorldPosition(target.getWorldX() + this.speedX, target.getWorldY() + this.speedY);
            final int BOUND = 12;
            if (target.getWorldCellX() > this.x + BOUND) {
                this.speedX = -this.speedX;
            }
            if (target.getWorldCellX() < this.x - BOUND) {
                this.speedX = -this.speedX;
            }
            if (target.getWorldCellY() > this.y + BOUND) {
                this.speedY = -this.speedY;
            }
            if (target.getWorldCellY() < this.y - BOUND) {
                this.speedY = -this.speedY;
            }
        }
    }
}
