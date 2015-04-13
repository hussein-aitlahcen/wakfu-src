package com.ankamagames.wakfu.client.alea.ambiance;

import com.ankamagames.framework.graphics.image.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers.*;
import com.ankamagames.baseImpl.graphics.alea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.particles.rain.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.*;
import com.ankamagames.wakfu.common.game.weather.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.weather.*;

public class AmbianceManagerFake
{
    private static final Color PINK_FLASH;
    public static final AmbianceManagerFake INSTANCE;
    private final TIntArrayList m_effectIds;
    
    private AmbianceManagerFake() {
        super();
        this.m_effectIds = new TIntArrayList();
    }
    
    public void clear() {
        for (int i = 0, size = this.m_effectIds.size(); i < size; ++i) {
            Shaders.removeEffect(this.m_effectIds.getQuick(i));
        }
        this.m_effectIds.clear();
    }
    
    public void stopAmbiance(final int ambianceId) {
        this.clear();
    }
    
    public void reset() {
        this.clear();
    }
    
    public void playAmbiance(final int ambianceId) {
        final GameDateConst date = BaseGameDateProvider.INSTANCE.getDate();
        switch (ambianceId) {
            case 48: {
                this.applyMoonLight();
            }
            case 49: {
                this.applyFogAmbiance();
            }
            case 50: {
                this.applyThunder(ThunderEffect.DEFAULT_FLASH_COLOR);
            }
            case 54: {
                Shaders.applyPast();
            }
            case 55: {
                this.applyBlizzard(new StrengthHandler(1.0f));
            }
            case 56: {
                this.applyBlizzard(new PseudoRandomStrength(25, date.toLong()));
            }
            case 57: {
                this.applySirocco(new PseudoRandomStrength(PseudoRandomStrength.generateValues(10, 0.3f, 1.0f), 10, date.toLong()));
            }
            default: {
                final short worldId = MapManagerHelper.getWorldId();
                switch (worldId) {
                    case 98:
                    case 99:
                    case 101:
                    case 115:
                    case 141: {
                        this.applyIncarnamAmbiance();
                        return;
                    }
                    case 575:
                    case 576: {
                        this.applySun(new StrengthHandler(1.0f));
                        return;
                    }
                    case 237:
                    case 240:
                    case 243:
                    case 247:
                    case 248:
                    case 249:
                    case 250:
                    case 251:
                    case 252:
                    case 253:
                    case 254:
                    case 255:
                    case 256:
                    case 257:
                    case 258:
                    case 259:
                    case 301:
                    case 302: {
                        this.applyMineAmbiance();
                        return;
                    }
                    case 210:
                    case 213:
                    case 214:
                    case 215:
                    case 216:
                    case 217: {
                        this.applyShukruteAmbiance();
                        return;
                    }
                    case 323: {
                        this.applyFogAmbiance();
                        return;
                    }
                    case 327: {
                        this.applyFogAmbiance();
                        this.applyThunder(AmbianceManagerFake.PINK_FLASH);
                        return;
                    }
                    case 336:
                    case 339: {
                        this.applyThunder(AmbianceManagerFake.PINK_FLASH);
                        return;
                    }
                    case 370:
                    case 371: {
                        this.applyStorm();
                        return;
                    }
                    case 524: {
                        this.applyFogAmbiance();
                        return;
                    }
                    case 802: {
                        this.applyHallucination(new StrengthHandler(1.0f));
                        return;
                    }
                    default: {
                        return;
                    }
                }
                break;
            }
        }
    }
    
    private void applyFogAmbiance() {
        this.m_effectIds.add(Shaders.applyFog());
    }
    
    private void applyMoonLight() {
        this.m_effectIds.add(Shaders.applyMoonLight());
    }
    
    private void applyIncarnamAmbiance() {
        this.m_effectIds.add(Shaders.applyIncarnam());
    }
    
    private void applyZinitAmbiance() {
        Shaders.applyZinit();
    }
    
    private void applyMineAmbiance() {
        Shaders.applyMine();
    }
    
    private void applyShukruteAmbiance() {
        this.m_effectIds.add(Shaders.applyShukrute());
        final RainParticleEffect worldEffect = new RainParticleEffect();
        worldEffect.setCamera(this.getWorldScene().getIsoCamera());
        worldEffect.setBoundingBox(new Box(-18.0f, 18.0f, -18.0f, 18.0f, -18.0f, 18.0f));
        worldEffect.initialize(1000);
        worldEffect.setColor(1.0f, 1.0f, 0.0f, 1.0f, 0.1f);
        worldEffect.start(10.0f, 500.0f, 5000.0f, 0.4f, 2.0f, 0.02f);
        worldEffect.setNumUsableParticles(100);
        EffectManager.getInstance().addWorldEffect(worldEffect);
        this.m_effectIds.add(worldEffect.getId());
    }
    
    private void applyThunder(final Color flashColor) {
        final ThunderEffect thunderEffect = new ThunderEffect();
        thunderEffect.setColor(flashColor);
        thunderEffect.activate(true);
        thunderEffect.setCamera(this.getWorldScene().getIsoCamera());
        EffectManager.getInstance().addWorldEffect(thunderEffect);
        this.m_effectIds.add(thunderEffect.getId());
    }
    
    private void applyBlizzard(final StrengthHandler strengthHandler) {
        final Effect effect = new BlizzardEffect();
        effect.activate(true);
        effect.start(strengthHandler);
        effect.setCamera(this.getWorldScene().getIsoCamera());
        EffectManager.getInstance().addWorldEffect(effect);
        this.m_effectIds.add(Shaders.applyBlizzard(strengthHandler));
    }
    
    private void applyHallucination(final StrengthHandler strengthHandler) {
        this.m_effectIds.add(Shaders.applyHallucination(strengthHandler));
    }
    
    private void applySirocco(final StrengthHandler strengthHandler) {
        this.m_effectIds.add(Shaders.applySirocco(strengthHandler));
    }
    
    private void applySun(final StrengthHandler strengthHandler) {
        final Effect effect = new SunEffect();
        effect.activate(true);
        effect.start(strengthHandler);
        effect.setCamera(this.getWorldScene().getIsoCamera());
        EffectManager.getInstance().addWorldEffect(effect);
    }
    
    private void applyStorm() {
        WeatherEffectManager.INSTANCE.changeTo(Weather.STORM);
        WeatherEffectManager.INSTANCE.changeWindStrength(0.9f);
    }
    
    private AleaWorldSceneWithParallax getWorldScene() {
        return WakfuClientInstance.getInstance().getWorldScene();
    }
    
    private void applyGloomy(final StrengthHandler strengthHandler) {
        final GloomyEffect GloomyEffect = new GloomyEffect();
        GloomyEffect.activate(true);
        GloomyEffect.start(strengthHandler);
        GloomyEffect.setCamera(this.getWorldScene().getIsoCamera());
        EffectManager.getInstance().addWorldEffect(GloomyEffect);
        this.m_effectIds.add(Shaders.applyGloomy(strengthHandler));
    }
    
    static {
        PINK_FLASH = new Color(0.8f, 0.4f, 0.5f, 0.8f);
        INSTANCE = new AmbianceManagerFake();
    }
}
