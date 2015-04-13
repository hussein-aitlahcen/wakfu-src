package com.ankamagames.wakfu.client.core.script;

import com.ankamagames.framework.script.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.graphics.core.partitions.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import org.keplerproject.luajava.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.wakfu.client.core.weather.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;

public class AmbianceFunctionLibrary extends JavaFunctionsLibrary
{
    private static final AmbianceFunctionLibrary m_instance;
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new SetEnvironmentParticleSystem(luaState), new SetWeather(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return new JavaFunctionEx[0];
    }
    
    public static AmbianceFunctionLibrary getInstance() {
        return AmbianceFunctionLibrary.m_instance;
    }
    
    @Override
    public final String getName() {
        return "Ambiance";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    static {
        m_instance = new AmbianceFunctionLibrary();
    }
    
    private static class SetEnvironmentParticleSystem extends JavaFunctionEx
    {
        SetEnvironmentParticleSystem(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "setEnvironmentAps";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("APS Id", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("level", null, LuaScriptParameterType.NUMBER, false) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int apsId = this.getParamInt(0);
            final int level = this.getParamInt(1);
            final TLongArrayList partitions = new TLongArrayList();
            final List<LocalPartition> localPartitionList = ((AbstractLocalPartitionManager<LocalPartition>)LocalPartitionManager.getInstance()).getAllLocalPartitions();
            final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
            for (int i = 0, size = localPartitionList.size(); i < size; ++i) {
                final LocalPartition localPartition = localPartitionList.get(i);
                final long key = MathHelper.getLongFromTwoInt(localPartition.getX(), localPartition.getY());
                if (!partitions.contains(key)) {
                    partitions.add(key);
                    final int x = localPartition.getX() * 18;
                    final int y = localPartition.getY() * 18;
                    final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(apsId, level);
                    system.setPosition(x, y, TopologyMapManager.getNearestZ(x, y, player.getPosition().getZ()));
                    IsoParticleSystemManager.getInstance().addParticleSystem(system);
                }
            }
            LocalPartitionManager.getInstance().addPartitionListener(new ApsPartitionListener(apsId, level, partitions));
        }
        
        private static class ApsPartitionListener implements PartitionChangedListener<PathMobile, LocalPartition>
        {
            final TLongArrayList partitions;
            private final int apsId;
            private final int level;
            
            ApsPartitionListener(final int i, final int lvl, final TLongArrayList parts) {
                super();
                this.apsId = i;
                this.partitions = parts;
                this.level = lvl;
            }
            
            @Override
            public void partitionChanged(final PathMobile isoWorldTarget, final LocalPartition oldPartition, final LocalPartition newPartition) {
                final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
                final List<LocalPartition> localPartitionList = ((AbstractLocalPartitionManager<LocalPartition>)LocalPartitionManager.getInstance()).getAllLocalPartitions();
                for (int i = 0, size = localPartitionList.size(); i < size; ++i) {
                    final LocalPartition localPartition = localPartitionList.get(i);
                    final long key = MathHelper.getLongFromTwoInt(localPartition.getX(), localPartition.getY());
                    if (!this.partitions.contains(key)) {
                        this.partitions.add(key);
                        final int x = localPartition.getX() * 18;
                        final int y = localPartition.getY() * 18;
                        final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(this.apsId, this.level);
                        system.setPosition(x, y, TopologyMapManager.getNearestZ(x, y, player.getPosition().getZ()));
                        IsoParticleSystemManager.getInstance().addParticleSystem(system);
                    }
                }
            }
        }
    }
    
    private static class SetWeather extends JavaFunctionEx
    {
        SetWeather(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "setWeather";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("temperature", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("rainIntensity", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("wind", null, LuaScriptParameterType.NUMBER, false) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final float temperature = (float)this.getParamDouble(0);
            final float rain = (float)this.getParamDouble(1);
            final float wind = (float)this.getParamDouble(2);
            final WeatherInfo info = new WeatherInfo();
            info.setWeather(temperature, temperature, temperature, 0.0f, new float[] { temperature }, rain, 0, 0.0f, wind, 0.0f);
            WeatherInfoManager.getInstance().updateCurrentWeather(info);
            WeatherEffectManager.INSTANCE.changeTo(info.getWeatherType(), 100);
            WeatherEffectManager.INSTANCE.changeWindStrength(info.getWindForce());
        }
    }
}
