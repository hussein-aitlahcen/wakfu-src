package com.ankamagames.baseImpl.graphics.script;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.particles.snow.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.keplerproject.luajava.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.particles.rain.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.*;
import com.ankamagames.framework.graphics.engine.fx.*;

public class EffectFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final EffectFunctionsLibrary m_instance;
    private IsoWorldScene m_worldScene;
    
    public static EffectFunctionsLibrary getInstance() {
        return EffectFunctionsLibrary.m_instance;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new CreateSnowEffect(luaState), new StopEffect(luaState), new CreateRainEffect(luaState), new CreateCameraShakeEffect(luaState), new CreateCameraSeaSicknessEffect(luaState), new CreateHeatPostProcess(luaState), new CreateBloomPostProcess(luaState), new CreateFullScreenSprite(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    public final void setWorldSence(final IsoWorldScene scene) {
        this.m_worldScene = scene;
    }
    
    public IsoWorldScene getWorldScene() {
        return this.m_worldScene;
    }
    
    private static IsoCamera getIsoCamera() {
        return EffectFunctionsLibrary.m_instance.m_worldScene.getIsoCamera();
    }
    
    @Override
    public final String getName() {
        return "Effect";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    private static void startCameraEffect(final int startDuration, final int midDuration, final int endDuration, final CameraEffect camEffect, final IsoCamera isoCamera) {
        camEffect.setCamera(isoCamera);
        if (midDuration == -1) {
            camEffect.start(new EaseInStrength(startDuration));
        }
        else {
            camEffect.start(new TimedStrength(startDuration, midDuration, endDuration));
        }
    }
    
    static {
        m_instance = new EffectFunctionsLibrary();
    }
    
    private static class CreateSnowEffect extends JavaFunctionEx
    {
        CreateSnowEffect(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "createSnowEffect";
        }
        
        @Override
        public String getDescription() {
            return "Active un effet de neige";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("numParticles", "Nombre de flocons ? l'?cran", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("numParticlesPerSpawn", "Nombre de flocons a r?g?n?rer", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("spawnFrequency", "On r?gen?re numParticlesPerSpawn toutes les spawnFrequency millisecondes", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("particleLifeTime", "Dur?e de vie d'un flocon", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("particleSize", "Taille d'un flocon", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("particleVelocity", "Vitesse d'un flocon", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("windSpeed", "Vitesse du vent", LuaScriptParameterType.NUMBER, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("effectId", "Id de l'effet", LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            int numParticles = 512;
            float numParticlesPerSpawn = 10.0f;
            float spawnFrequency = 300.0f;
            float particleLifeTime = 20000.0f;
            float particleSize = 3.0f;
            if (paramCount >= 1) {
                numParticles = this.getParamInt(0);
            }
            if (paramCount >= 2) {
                numParticlesPerSpawn = (float)this.getParamDouble(1);
            }
            if (paramCount >= 3) {
                spawnFrequency = (float)this.getParamDouble(2);
            }
            if (paramCount >= 4) {
                particleLifeTime = (float)this.getParamDouble(3);
            }
            if (paramCount >= 5) {
                particleSize = (float)this.getParamDouble(4);
            }
            final SnowParticleEffect snow = new SnowParticleEffect();
            snow.initialize(numParticles);
            snow.start(numParticlesPerSpawn, spawnFrequency, particleLifeTime, particleSize);
            snow.setCamera(getIsoCamera());
            snow.setBoundingBox(new Box(-18.0f, 18.0f, -18.0f, 18.0f, -18.0f, 18.0f));
            EffectManager.getInstance().addWorldEffect(snow);
            this.addReturnValue(snow.getId());
        }
    }
    
    private static class CreateRainEffect extends JavaFunctionEx
    {
        CreateRainEffect(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "createRainEffect";
        }
        
        @Override
        public String getDescription() {
            return "Active un effet de pluie";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("numParticles", "Nombre de gouttes ? l'?cran", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("numParticlesPerSpawn", "Nombre de gouttes ? g?n?rer", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("spawnFrequency", "On r?gen?re numParticlesPerSpawn toutes les spawnFrequency millisecondes", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("particleLifeTime", "Dur?e de vie d'une goutte", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("particleHeight", "Taille d'une goutte", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("particleVelocity", "Vitesse de chute des gouttes", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("particleAlpha", "Transparence des gouttes", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("particleAngle", "Angle (en radian) selon lequel tombe les gouttes", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("particleAngleRandom", "Al?atoire sur l'angle (en radian ? ajouter ? particleAngle)", LuaScriptParameterType.NUMBER, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("effectId", "Id de l'effet", LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            int numParticles = 512;
            float numParticlesPerSpawn = 128.0f;
            float spawnFrequency = 100.0f;
            float particleLifeTime = 300.0f;
            float particleHeight = 4.0f;
            float particleVelocity = -60.0f;
            float particleAlpha = 0.25f;
            float particleAngle = -1.2566371f;
            float particleAngleRandom = 0.15707964f;
            if (paramCount >= 1) {
                numParticles = this.getParamInt(0);
            }
            if (paramCount >= 2) {
                numParticlesPerSpawn = (float)this.getParamDouble(1);
            }
            if (paramCount >= 3) {
                spawnFrequency = (float)this.getParamDouble(2);
            }
            if (paramCount >= 4) {
                particleLifeTime = (float)this.getParamDouble(3);
            }
            if (paramCount >= 5) {
                particleHeight = (float)this.getParamDouble(4);
            }
            if (paramCount >= 6) {
                particleVelocity = (float)this.getParamDouble(5);
            }
            if (paramCount == 7) {
                particleAlpha = (float)this.getParamDouble(6);
            }
            if (paramCount == 8) {
                particleAngle = (float)this.getParamDouble(7);
            }
            if (paramCount == 9) {
                particleAngleRandom = (float)this.getParamDouble(8);
            }
            final RainParticleEffect rain = new RainParticleEffect();
            rain.initialize(numParticles);
            rain.setAngle(particleAngle, particleAngleRandom);
            rain.setColor(1.0f, 1.0f, 1.0f, particleAlpha, 0.0f);
            rain.start(numParticlesPerSpawn, spawnFrequency, particleLifeTime, particleHeight, particleVelocity, 0.0f);
            rain.setCamera(getIsoCamera());
            rain.setBoundingBox(new Box(-18.0f, 18.0f, -18.0f, 18.0f, -18.0f, 18.0f));
            EffectManager.getInstance().addWorldEffect(rain);
            this.addReturnValue(rain.getId());
        }
    }
    
    private static class CreateFullScreenSprite extends JavaFunctionEx
    {
        CreateFullScreenSprite(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "createFullScreenSprite";
        }
        
        @Override
        public String getDescription() {
            return "Affiche une image remplissant compl?tement l'?cran";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("startDuration", "Temps pendant lequel la transparence augmente progressivement (en ms)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("midDuration", "Dur?e de l'effet ? la transparence max (en ms)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("endDuration", "Temps pendant lequel la transparence diminue progressivement (en ms)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("red", "Teinte rouge (entre 0 et 1)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("green", "Teinte verte (entre 0 et 1)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("blue", "Teinte bleue (entre 0 et 1)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("alpha", "Transparence de l'image", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("textureFileName", "Fichier de l'image ? afficher", LuaScriptParameterType.STRING, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("effectId", "L'id de l'effet", LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            int startDuration = 1000;
            int midDuration = 2000;
            int endDuration = 1000;
            float red = 1.0f;
            float green = 1.0f;
            float blue = 1.0f;
            float alpha = 1.0f;
            String textureFileName = null;
            if (paramCount >= 1) {
                startDuration = (int)this.getParamDouble(0);
            }
            if (paramCount >= 2) {
                midDuration = (int)this.getParamDouble(1);
            }
            if (paramCount >= 3) {
                endDuration = (int)this.getParamDouble(2);
            }
            if (paramCount >= 4) {
                red = (float)this.getParamDouble(3);
            }
            if (paramCount >= 5) {
                green = (float)this.getParamDouble(4);
            }
            if (paramCount >= 6) {
                blue = (float)this.getParamDouble(5);
            }
            if (paramCount >= 7) {
                alpha = (float)this.getParamDouble(6);
            }
            if (paramCount >= 8) {
                textureFileName = this.getParamString(7);
            }
            final FullScreenSprite sprite = new FullScreenSprite();
            sprite.setTexture(textureFileName);
            sprite.setColor(red, green, blue, alpha);
            if (midDuration < 0) {
                sprite.start(new EaseInStrength(startDuration));
            }
            else {
                sprite.start(new TimedStrength(startDuration, midDuration, endDuration));
            }
            EffectManager.getInstance().addWorldEffect(sprite);
            this.addReturnValue(sprite.getId());
        }
    }
    
    private static class CreateCameraShakeEffect extends JavaFunctionEx
    {
        CreateCameraShakeEffect(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "createCameraShakeEffect";
        }
        
        @Override
        public String getDescription() {
            return "Active un effet de tremblement de la cam?ra";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("startDuration", "Temps pendant lequel la force augmente progressivement (en ms)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("midDuration", "Dur?e de l'effet ? la force max (en ms)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("endDuration", "Temps pendant lequel la force diminue progressivement (en ms)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("amplitude", "Amplitude max des mouvements", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("period", "P?riode du tremblement", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("direction", "Application de l'effet selon l'axe 'X', 'Y', ou 'BOTH' ('XY')", LuaScriptParameterType.STRING, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("effectId", "Id de l'efet", LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            int startDuration = 1000;
            int midDuration = 2000;
            int endDuration = 1000;
            float amplitude = 0.5f;
            float period = 77.0f;
            String direction = "BOTH";
            if (paramCount >= 1) {
                startDuration = (int)this.getParamDouble(0);
            }
            if (paramCount >= 2) {
                midDuration = (int)this.getParamDouble(1);
            }
            if (paramCount >= 3) {
                endDuration = (int)this.getParamDouble(2);
            }
            if (paramCount >= 4) {
                amplitude = (float)this.getParamDouble(3);
            }
            if (paramCount == 5) {
                period = (float)this.getParamDouble(4);
            }
            if (paramCount == 6) {
                direction = this.getParamString(5).toUpperCase();
            }
            final CameraEffectShake shake = new CameraEffectShake();
            shake.setDirection(direction);
            shake.setParams(period, amplitude);
            startCameraEffect(startDuration, midDuration, endDuration, shake, getIsoCamera());
            EffectManager.getInstance().addWorldEffect(shake);
            this.addReturnValue(shake.getId());
        }
    }
    
    private static class CreateCameraSeaSicknessEffect extends JavaFunctionEx
    {
        CreateCameraSeaSicknessEffect(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "createCameraSeaSicknessEffect";
        }
        
        @Override
        public String getDescription() {
            return "Active un effet de mal de mer";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("startDuration", "Temps pendant lequel la force augmente progressivement (en ms)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("midDuration", "Dur?e de l'effet ? la force max (en ms)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("endDuration", "Temps pendant lequel la force diminue progressivement (en ms)", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("amplitude", "Amplitude max des mouvements", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("period", "P?riode du tremblement", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("rotation", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("direction", "Application de l'effet selon l'axe 'X', 'Y', ou 'BOTH' ('XY')", LuaScriptParameterType.STRING, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("effectId", "Id de l'effet", LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            int startDuration = 0;
            int midDuration = -1;
            int endDuration = 0;
            float amplitude = 5.0f;
            float period = 1500.0f;
            float rotation = 0.025f;
            String direction = "BOTH";
            if (paramCount >= 1) {
                startDuration = (int)this.getParamDouble(0);
            }
            if (paramCount >= 2) {
                midDuration = (int)this.getParamDouble(1);
            }
            if (paramCount >= 3) {
                endDuration = (int)this.getParamDouble(2);
            }
            if (paramCount >= 4) {
                amplitude = (float)this.getParamDouble(3);
            }
            if (paramCount >= 5) {
                period = (float)this.getParamDouble(4);
            }
            if (paramCount >= 6) {
                rotation = (float)this.getParamDouble(6);
            }
            if (paramCount >= 7) {
                direction = this.getParamString(6).toUpperCase();
            }
            final CameraEffectSeaSickness seaSickness = new CameraEffectSeaSickness();
            seaSickness.setDirection(direction);
            seaSickness.setParams(period, amplitude, rotation);
            startCameraEffect(startDuration, midDuration, endDuration, seaSickness, getIsoCamera());
            EffectManager.getInstance().addWorldEffect(seaSickness);
            this.addReturnValue(seaSickness.getId());
        }
    }
    
    private static class CreateHeatPostProcess extends JavaFunctionEx
    {
        CreateHeatPostProcess(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "createHeatPostProcess";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("startDuration", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("midDuration", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("endDuration", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("strength", null, LuaScriptParameterType.NUMBER, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("effectId", null, LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            float startDuration = 1000.0f;
            float midDuration = 2000.0f;
            float endDuration = 1000.0f;
            float strength = 1.0f;
            if (paramCount >= 1) {
                startDuration = (float)this.getParamDouble(0);
            }
            if (paramCount >= 2) {
                midDuration = (float)this.getParamDouble(1);
            }
            if (paramCount >= 3) {
                endDuration = (float)this.getParamDouble(2);
            }
            if (paramCount == 4) {
                strength = (float)this.getParamDouble(3);
            }
            this.addReturnValue(FxConstants.POST_PROCESS_HEAT);
            throw new UnsupportedOperationException("TODO: post process Heat");
        }
    }
    
    private static class CreateBloomPostProcess extends JavaFunctionEx
    {
        CreateBloomPostProcess(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "createBloomPostProcess";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("startDuration", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("midDuration", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("endDuration", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("minValue", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("red", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("green", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("blue", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("alpha", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("blurX", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("blurY", null, LuaScriptParameterType.NUMBER, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("effectId", null, LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            float startDuration = 1000.0f;
            float midDuration = 2000.0f;
            float endDuration = 1000.0f;
            float minValue = 0.7f;
            float red = 0.3f;
            float green = 0.3f;
            float blue = 0.3f;
            float alpha = 1.0f;
            int blurX = 16;
            int blurY = 16;
            if (paramCount >= 1) {
                startDuration = (float)this.getParamDouble(0);
            }
            if (paramCount >= 2) {
                midDuration = (float)this.getParamDouble(1);
            }
            if (paramCount >= 3) {
                endDuration = (float)this.getParamDouble(2);
            }
            if (paramCount >= 4) {
                minValue = (float)this.getParamDouble(3);
            }
            if (paramCount >= 5) {
                red = (float)this.getParamDouble(4);
            }
            if (paramCount >= 6) {
                green = (float)this.getParamDouble(5);
            }
            if (paramCount >= 7) {
                blue = (float)this.getParamDouble(6);
            }
            if (paramCount >= 8) {
                alpha = (float)this.getParamDouble(7);
            }
            if (paramCount >= 9) {
                blurX = this.getParamInt(8);
            }
            if (paramCount == 10) {
                blurY = this.getParamInt(9);
            }
            this.addReturnValue(FxConstants.POST_PROCESS_BLOOM);
            throw new UnsupportedOperationException("TODO: post process Bloom");
        }
    }
    
    private static class StopEffect extends JavaFunctionEx
    {
        StopEffect(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "stopEffect";
        }
        
        @Override
        public String getDescription() {
            return "Arr?te l'execution d'un effet";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("effectId", "L'id de l'effet", LuaScriptParameterType.NUMBER, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int id = this.getParamInt(0);
            if (id == FxConstants.POST_PROCESS_BLOOM || id == FxConstants.POST_PROCESS_HEAT) {
                return;
            }
            final EffectBase worldEffect = EffectManager.getInstance().getWorldEffect(id);
            if (worldEffect != null) {
                worldEffect.activate(false);
                EffectManager.getInstance().removeWorldEffect(worldEffect);
                worldEffect.clear();
            }
        }
    }
}
