package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class AddParticleCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (args.size() == 3) {
            try {
                boolean attach = false;
                String s = args.get(2);
                if (s.startsWith("@")) {
                    s = s.substring(1);
                    attach = true;
                }
                final int particleId = Integer.valueOf(s);
                if (attach) {
                    final FreeParticleSystem particleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(particleId);
                    final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                    particleSystem.setTarget(localPlayer.getActor());
                    IsoParticleSystemManager.getInstance().addParticleSystem(particleSystem);
                }
                else {
                    final CellParticleSystem particleSystem2 = IsoParticleSystemFactory.getInstance().getCellParticleSystem(particleId);
                    final Point3 position = WakfuGameEntity.getInstance().getLocalPlayer().getPosition();
                    particleSystem2.setPosition(position.getX(), position.getY(), position.getZ());
                    IsoParticleSystemManager.getInstance().addParticleSystem(particleSystem2);
                }
            }
            catch (Exception e) {
                Logger.getLogger((Class)AddParticleCommand.class).error((Object)("Impossible d'ajouter la particule " + args.get(1)), (Throwable)e);
            }
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
