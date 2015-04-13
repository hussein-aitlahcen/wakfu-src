package com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter;

import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.regex.*;
import com.ankamagames.wakfu.common.datas.*;

public class SpellWriter extends DefaultContainerWriter<AbstractSpellLevel<? extends AbstractSpell>>
{
    public SpellWriter(final AbstractSpellLevel<? extends AbstractSpell> container) {
        this(container, null, null, CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY);
    }
    
    public SpellWriter(final AbstractSpellLevel<? extends AbstractSpell> container, @Nullable final ArrayList<String> validCriterion, @Nullable final ArrayList<String> invalidCriterion, final CastableDescriptionGenerator.DescriptionMode mode) {
        super(container, ((AbstractSpell)container.getSpell()).getId(), container.getLevel(), CastableDescriptionGenerator.m_utilityDelegate.useAutomaticDescription(container), validCriterion, invalidCriterion, mode, CastableDescriptionGenerator.SPELL_FREE_DESCRIPTION_TRANSLATION_TYPE);
    }
    
    @Override
    public void onContainerBegin(@NotNull final ArrayList<String> descriptions) {
    }
    
    @Override
    public String onEffectAdded(@NotNull String effectDescription, @NotNull final WakfuEffect effect) {
        if (((AbstractSpellLevel)this.m_container).getSpell().getBreedId() == AvatarBreed.SADIDA.getBreedId()) {
            String name = null;
            final TargetValidator<EffectUser> userTargetValidator = effect.getTargetValidator();
            if (userTargetValidator.isConditionActive(34359738368L)) {
                name = "puppet";
            }
            else if (userTargetValidator.isConditionActive(137438953472L)) {
                name = "totem";
            }
            else if (userTargetValidator.isConditionActive(68719476736L) || userTargetValidator.isConditionActive(274877906944L)) {
                name = "enemy";
            }
            if (name != null) {
                effectDescription = CastableDescriptionGenerator.m_utilityDelegate.getSpellTargetIcon(CastableDescriptionGenerator.m_twfFactory.createNew(), name).append(effectDescription).finishAndToString();
            }
        }
        else if (((AbstractSpellLevel)this.m_container).getSpell().getBreedId() == AvatarBreed.SACRIER.getBreedId()) {
            String name = null;
            final TargetValidator<EffectUser> userTargetValidator = effect.getTargetValidator();
            if (userTargetValidator.isConditionActive(2L)) {
                name = "caster";
            }
            if (name != null) {
                effectDescription = CastableDescriptionGenerator.m_utilityDelegate.getSpellTargetIcon(CastableDescriptionGenerator.m_twfFactory.createNew(), name).append(effectDescription).finishAndToString();
            }
        }
        effectDescription = formatSpellAttributes((AbstractSpellLevel)this.m_container, this.getLevel(), effectDescription);
        if (((AbstractSpellLevel)this.m_container).getSpell().isPassive() && ((AbstractSpellLevel)this.m_container).getLevel() == 0) {
            final String[] split = StringUtils.split(effectDescription, '\n');
            final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
            for (int i = 0; i < split.length; ++i) {
                if (split[i].length() > 0) {
                    sb.openText().addColor("b0b0b0").append(split[i]).closeText().newLine();
                }
            }
            return sb.finishAndToString();
        }
        return effectDescription;
    }
    
    public static String formatSpellAttributes(final AbstractSpellLevel spellLevel, final short spellLevelLevel, String desc) {
        Matcher spellMatcher = CastableDescriptionGenerator.SPELL_PATTERN.matcher(desc.trim());
        final BasicCharacterInfo lp = CastableDescriptionGenerator.m_utilityDelegate.getCaster();
        final Object effectContext = CastableDescriptionGenerator.m_utilityDelegate.getCasterEffectContext();
        while (spellMatcher.find()) {
            final String value = spellMatcher.group(1);
            final char charAt0 = value.charAt(0);
            switch (charAt0) {
                case 'a': {
                    final char charAt = value.charAt(1);
                    switch (charAt) {
                        case 'p': {
                            desc = desc.substring(0, spellMatcher.start()) + spellLevel.getSpell().getActionPoints(spellLevel, lp, null, effectContext) + desc.substring(spellMatcher.end());
                            spellMatcher = CastableDescriptionGenerator.SPELL_PATTERN.matcher(desc.trim());
                            continue;
                        }
                    }
                    continue;
                }
                case 'm': {
                    final char charAt = value.charAt(1);
                    switch (charAt) {
                        case 'p': {
                            desc = desc.substring(0, spellMatcher.start()) + spellLevel.getSpell().getMovementPoints(spellLevel, lp, null, effectContext) + desc.substring(spellMatcher.end());
                            spellMatcher = CastableDescriptionGenerator.SPELL_PATTERN.matcher(desc.trim());
                            continue;
                        }
                        case 'r': {
                            desc = desc.substring(0, spellMatcher.start()) + spellLevel.getSpell().getRangeMin(spellLevel, lp, null, effectContext) + desc.substring(spellMatcher.end());
                            spellMatcher = CastableDescriptionGenerator.SPELL_PATTERN.matcher(desc.trim());
                            continue;
                        }
                    }
                    continue;
                }
                case 'M': {
                    final char charAt = value.charAt(1);
                    switch (charAt) {
                        case 'r': {
                            desc = desc.substring(0, spellMatcher.start()) + spellLevel.getSpell().getRangeMax(spellLevel, lp, null, effectContext) + desc.substring(spellMatcher.end());
                            spellMatcher = CastableDescriptionGenerator.SPELL_PATTERN.matcher(desc.trim());
                            continue;
                        }
                    }
                    continue;
                }
                case 'w': {
                    final char charAt = value.charAt(1);
                    switch (charAt) {
                        case 'p': {
                            desc = desc.substring(0, spellMatcher.start()) + spellLevel.getSpell().getWakfuPoints(spellLevel, lp, null, effectContext) + desc.substring(spellMatcher.end());
                            spellMatcher = CastableDescriptionGenerator.SPELL_PATTERN.matcher(desc.trim());
                            continue;
                        }
                    }
                    continue;
                }
                case 'e': {
                    final char charAt = value.charAt(1);
                    switch (charAt) {
                        case 'l': {
                            final Elements element = Elements.getElementFromId(spellLevel.getSpell().getElementId());
                            final String elementText = CastableDescriptionGenerator.addElement(element);
                            desc = desc.substring(0, spellMatcher.start()) + elementText + desc.substring(spellMatcher.end());
                            spellMatcher = CastableDescriptionGenerator.SPELL_PATTERN.matcher(desc.trim());
                            continue;
                        }
                    }
                    continue;
                }
                case 'c': {
                    final char charAt = value.charAt(1);
                    switch (charAt) {
                        case 'h': {
                            desc = desc.substring(0, spellMatcher.start()) + CastableDescriptionGenerator.m_utilityDelegate.getChrageIcon() + desc.substring(spellMatcher.end());
                            spellMatcher = CastableDescriptionGenerator.SPELL_PATTERN.matcher(desc.trim());
                            continue;
                        }
                    }
                    continue;
                }
                case 'l': {
                    final char charAt = value.charAt(1);
                    switch (charAt) {
                        case 'v': {
                            desc = desc.substring(0, spellMatcher.start()) + lp.getLevel() + desc.substring(spellMatcher.end());
                            spellMatcher = CastableDescriptionGenerator.SPELL_PATTERN.matcher(desc.trim());
                            continue;
                        }
                    }
                    continue;
                }
            }
        }
        return desc;
    }
}
