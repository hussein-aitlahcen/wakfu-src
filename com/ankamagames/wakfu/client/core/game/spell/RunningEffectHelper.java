package com.ankamagames.wakfu.client.core.game.spell;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import gnu.trove.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.util.text.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class RunningEffectHelper
{
    private static final Logger m_logger;
    
    public static ArrayList<String> getStackedEffects(final Iterable<RunningEffect> runningEffects, final EffectContainer commonContainer) {
        boolean auto = true;
        final ArrayList<String> sb = new ArrayList<String>();
        if (commonContainer instanceof SpellLevel) {
            final SpellLevel spellLevel = (SpellLevel)commonContainer;
            if (!spellLevel.getSpell().isUseAutomaticDescription()) {
                auto = false;
                if (WakfuTranslator.getInstance().containsContentKey(5, spellLevel.getSpell().getId())) {
                    sb.add(WakfuTranslator.getInstance().getString(5, spellLevel.getSpell().getId(), new Object[0]));
                }
            }
        }
        if (auto && runningEffects != null) {
            final TObjectIntHashMap<String> descriptions = new TObjectIntHashMap<String>();
            final Collection<String> descOrdered = new ArrayList<String>();
            RunningEffect previous = null;
            int value = 0;
            for (final RunningEffect runningEffect : runningEffects) {
                final TextWidgetFormater builder = new TextWidgetFormater();
                final Effect genericEffect = runningEffect.getGenericEffect();
                String finalDesc = "";
                if (genericEffect != null) {
                    if (genericEffect instanceof WakfuEffect) {
                        short containerLevel = 0;
                        DefaultContainerWriter containerWriter;
                        if (runningEffect.getEffectContainer() instanceof StateClient) {
                            containerLevel = runningEffect.getEffectContainer().getLevel();
                            containerWriter = new RunningEffectContainerWriter(commonContainer, 0, containerLevel);
                        }
                        else if (runningEffect.getEffectContainer() instanceof SpellLevel) {
                            final SpellLevel spellLevel2 = runningEffect.getEffectContainer();
                            containerLevel = spellLevel2.getLevel();
                            containerWriter = new SpellWriter(spellLevel2);
                        }
                        else if (runningEffect.getEffectContainer() instanceof AbstractEffectArea) {
                            containerLevel = runningEffect.getEffectContainer().getLevel();
                            containerWriter = new RunningEffectContainerWriter(commonContainer, 0, containerLevel);
                        }
                        else {
                            containerWriter = new RunningEffectContainerWriter(commonContainer, 0, containerLevel);
                        }
                        final WakfuEffect effect = (WakfuEffect)genericEffect;
                        final float probability = effect.getExecutionProbability(containerLevel);
                        if (probability <= 0.0f) {
                            continue;
                        }
                        final WakfuEffect wakfuEffect = new DummyRunningEffectWakfuEffect(runningEffect);
                        if (wakfuEffect.getActionId() == -1) {
                            continue;
                        }
                        final EffectWriter effectWriter = CastableDescriptionGenerator.getEffectWriter(wakfuEffect.getActionId());
                        final int error = effectWriter.writeEffect(builder, wakfuEffect, containerWriter);
                        if (error == -1) {
                            continue;
                        }
                        String desc = builder.finishAndToString();
                        desc = containerWriter.onEffectAdded(desc, effect);
                        desc = DefaultContainerWriter.parseArithmetic(desc);
                        if (runningEffect.getEffectContainer() instanceof SpellLevel) {
                            desc = SpellWriter.formatSpellAttributes(runningEffect.getEffectContainer(), containerLevel, desc);
                        }
                        finalDesc = desc;
                    }
                }
                else {
                    if (previous != null && previous.getId() != runningEffect.getId()) {
                        writeRunningEffect(builder, previous.getGenericEffect().getEffectId(), previous.getId(), value, getElementFromRunningEffect(previous.getGenericEffect()));
                        value = 0;
                    }
                    previous = runningEffect;
                    value += runningEffect.getValue();
                    finalDesc = builder.finishAndToString();
                }
                if (!finalDesc.isEmpty()) {
                    if (!descriptions.contains(finalDesc)) {
                        descOrdered.add(finalDesc);
                    }
                    descriptions.adjustOrPutValue(finalDesc, 1, 1);
                }
            }
            for (String desc2 : descOrdered) {
                final int count = descriptions.get(desc2);
                if (count > 1) {
                    desc2 = desc2 + " (x" + count + ')';
                }
                sb.add(desc2);
            }
            if (previous != null) {
                final TextWidgetFormater builder2 = new TextWidgetFormater();
                writeRunningEffect(builder2, previous.getGenericEffect().getEffectId(), previous.getId(), value, getElementFromRunningEffect(previous.getGenericEffect()));
                sb.add(builder2.finishAndToString());
            }
        }
        return sb;
    }
    
    private static Elements getElementFromRunningEffect(final Effect effect) {
        return RunningEffectConstants.getInstance().getObjectFromId(effect.getActionId()).getElement();
    }
    
    private static void writeRunningEffect(final TextWidgetFormater stringBuilder, final int effectId, final int actionId, final int value, final Elements element) {
        String s = null;
        if (WakfuTranslator.getInstance().containsContentKey(33, effectId)) {
            final String effectText = WakfuTranslator.getInstance().getString(33, effectId, new Object[0]);
            s = StringFormatter.format(effectText, value);
        }
        if (s == null || s.isEmpty()) {
            final String effectText = WakfuTranslator.getInstance().getStringWithoutFormat(10, actionId);
            s = StringFormatter.format(effectText, value);
        }
        if (s != null && !s.isEmpty()) {
            stringBuilder.append(s);
            if (element != null) {
                try {
                    final String elementUrl = String.format(WakfuConfiguration.getInstance().getString("elementsSmallIconsPath"), element.name());
                    stringBuilder.append(" ").append(TextUtils.getImageTag(elementUrl, -1, -1, null));
                }
                catch (PropertyException e) {
                    RunningEffectHelper.m_logger.error((Object)e.toString());
                }
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)RunningEffectHelper.class);
    }
}
