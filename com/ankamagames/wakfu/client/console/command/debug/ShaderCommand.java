package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.shaders.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers.*;
import com.ankamagames.framework.graphics.engine.fx.*;

public class ShaderCommand implements Command
{
    private static final Logger m_logger;
    private static final THashMap<String, ShaderEffect> m_postProcesses;
    
    private static String getShaderPath() {
        try {
            return WakfuConfiguration.getInstance().getString("shadersPath");
        }
        catch (PropertyException e) {
            ShaderCommand.m_logger.error((Object)"", (Throwable)e);
            return "";
        }
    }
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (args.size() == 3) {
            final String name = args.get(2);
            ShaderEffect effect = ShaderCommand.m_postProcesses.get(name);
            if (effect == null) {
                effect = new ShaderEffect(getShaderPath() + name + ".cgfx", "test");
                effect.setCamera(WakfuClientInstance.getInstance().getWorldScene().getIsoCamera());
                effect.activate(true);
                effect.start(new EaseInStrength(4000));
                ShaderCommand.m_postProcesses.put(name, effect);
                EffectManager.getInstance().addWorldEffect(effect);
            }
            else {
                effect.stop(500);
                ShaderCommand.m_postProcesses.remove(name);
            }
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ShaderCommand.class);
        m_postProcesses = new THashMap<String, ShaderEffect>();
    }
}
