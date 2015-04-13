package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.client.ui.mru.MRUDecorators.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.text.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.util.text.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.common.game.nation.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public abstract class AbstractLawMRUAction extends AbstractMRUAction
{
    @Override
    public ArrayList<ParticleDecorator> getParticleDecorators() {
        final ArrayList<ParticleDecorator> particleDecorators = new ArrayList<ParticleDecorator>();
        final Protector protector = ProtectorView.getInstance().getProtector();
        if (protector == null) {
            return particleDecorators;
        }
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        if (player.getCitizenComportment().isPvpEnemy()) {
            return particleDecorators;
        }
        final NationAlignement alignment = getCurrentNationAlignment();
        if (alignment == NationAlignement.ENEMY && player.getCitizenComportment().isOutlaw()) {
            return particleDecorators;
        }
        final boolean hasProbablyGood = !this.getProbablyTriggeredGoodLaws().isEmpty();
        final boolean hasProbablyBad = !this.getProbablyTriggeredBadLaws().isEmpty();
        final boolean hasGood = !this.getTriggeredGoodLaws().isEmpty();
        final boolean hasBad = !this.getTriggeredBadLaws().isEmpty();
        if (alignment == NationAlignement.ENEMY && !NationPvpHelper.isPvpActive(player.getCitizenComportment())) {
            if (this.leadToEnemyStateAction()) {
                particleDecorators.add(new MRUCriminalParticleDecorator("6001020.xps"));
            }
        }
        else if ((hasProbablyGood && hasProbablyBad) || (hasGood && hasBad) || (hasProbablyGood && hasBad) || (hasProbablyBad && hasGood)) {
            particleDecorators.add(new MRUCriminalParticleDecorator("800224.xps"));
        }
        else if (hasGood) {
            particleDecorators.add(new MRUCriminalParticleDecorator("800193.xps"));
        }
        else if (hasBad) {
            particleDecorators.add(new MRUCriminalParticleDecorator("800194.xps"));
        }
        else if (hasProbablyGood) {
            particleDecorators.add(new MRUCriminalParticleDecorator("800222.xps"));
        }
        else if (hasProbablyBad) {
            particleDecorators.add(new MRUCriminalParticleDecorator("800223.xps"));
        }
        return particleDecorators;
    }
    
    @Nullable
    @Override
    public String getComplementaryTooltip() {
        final Protector protector = ProtectorView.getInstance().getProtector();
        if (protector == null) {
            return null;
        }
        final NationAlignement alignment = getCurrentNationAlignment();
        if (alignment == NationAlignement.ENEMY && WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().isOutlaw()) {
            return null;
        }
        final TextWidgetFormater sb = new TextWidgetFormater();
        final boolean leadToEnemyAction = this.leadToEnemyStateAction();
        if (leadToEnemyAction) {
            sb.openText().addColor(AbstractLawMRUAction.NOK_TOOLTIP_COLOR);
            sb.append("\n").append(WakfuTranslator.getInstance().getString("desc.mru.illegalAction"));
            sb.closeText();
        }
        final List<NationLaw> goodLaws = this.getTriggeredGoodLaws();
        if (!goodLaws.isEmpty()) {
            sb.newLine().openText().addColor(AbstractLawMRUAction.OK_TOOLTIP_COLOR);
            sb.b().append(WakfuTranslator.getInstance().getString("nation.law.mru.good"))._b();
            sb.closeText();
            listLaws(sb, goodLaws, AbstractLawMRUAction.OK_TOOLTIP_COLOR, leadToEnemyAction);
        }
        final List<NationLaw> badLaws = this.getTriggeredBadLaws();
        if (!badLaws.isEmpty()) {
            sb.newLine().openText().addColor(AbstractLawMRUAction.NOK_TOOLTIP_COLOR);
            sb.b().append(WakfuTranslator.getInstance().getString("nation.law.mru.bad"))._b();
            sb.closeText();
            listLaws(sb, badLaws, AbstractLawMRUAction.NOK_TOOLTIP_COLOR, leadToEnemyAction);
        }
        final List<NationLaw> probablyGoodLaws = this.getProbablyTriggeredGoodLaws();
        if (!probablyGoodLaws.isEmpty()) {
            sb.newLine().openText().addColor("9ed34b");
            sb.b().append(WakfuTranslator.getInstance().getString("nation.law.mru.probablyGood"))._b();
            sb.closeText();
            listLaws(sb, probablyGoodLaws, "9ed34b", leadToEnemyAction);
        }
        final List<NationLaw> probablyBadLaws = this.getProbablyTriggeredBadLaws();
        if (!probablyBadLaws.isEmpty()) {
            sb.newLine().openText().addColor("f48140");
            sb.b().append(WakfuTranslator.getInstance().getString("nation.law.mru.probablyBad"))._b();
            sb.closeText();
            listLaws(sb, probablyBadLaws, "f48140", leadToEnemyAction);
        }
        return (sb.length() > 0) ? sb.finishAndToString() : null;
    }
    
    public static void listLaws(final TextWidgetFormater sb, final Iterable<NationLaw> laws, final String color, final boolean leadToEnemyAction) {
        for (final NationLaw nationLaw : laws) {
            final boolean good = nationLaw.getBasePointsModification() > 0;
            sb.newLine().openText();
            sb.addColor(color);
            sb.append("    " + WakfuTranslator.getInstance().getString(97, (int)nationLaw.getId(), new Object[0]));
            if (leadToEnemyAction) {
                sb.closeText();
                return;
            }
            sb.b().append(" (" + (good ? "+" : "") + nationLaw.getPercentPointsModification() + "%/" + (good ? "+" : "") + nationLaw.getBasePointsModification());
            try {
                sb.append(TextUtils.getImageTag(WakfuTextImageProvider._getIconUrl((byte)9), -1, -1, "north", "citizenScore") + ')');
            }
            catch (PropertyException e) {
                AbstractLawMRUAction.m_logger.error((Object)"erreur au chargement de l'icon de citizen");
            }
            sb._b().closeText();
        }
    }
    
    protected boolean leadToEnemyStateAction() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (getCurrentNationAlignment() != NationAlignement.ENEMY || NationPvpHelper.isPvpActive(localPlayer.getCitizenComportment())) {
            return false;
        }
        final List<NationLaw> badLaws = this.getTriggeredBadLaws();
        final List<NationLaw> probablyBadLaws = this.getProbablyTriggeredBadLaws();
        return !badLaws.isEmpty() || !probablyBadLaws.isEmpty();
    }
    
    @Nullable
    public static NationAlignement getCurrentNationAlignment() {
        final Protector protector = ProtectorView.getInstance().getProtector();
        if (protector == null) {
            return null;
        }
        final Nation nation = protector.getCurrentNation();
        if (nation == Nation.VOID_NATION) {
            return null;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CitizenComportment comp = localPlayer.getCitizenComportment();
        return comp.getNation().getDiplomacyManager().getAlignment(nation.getNationId());
    }
    
    private static boolean protectorHasNation() {
        final Protector protector = ProtectorView.getInstance().getProtector();
        return protector != null && protector.hasNation();
    }
    
    private List<NationLaw> getTriggeredGoodLaws() {
        if (!protectorHasNation()) {
            return (List<NationLaw>)Collections.emptyList();
        }
        return (List<NationLaw>)NationLaw.getGoodLaws((List<NationLaw<NationLawEvent>>)this.getTriggeredLaws());
    }
    
    private List<NationLaw> getProbablyTriggeredGoodLaws() {
        return (List<NationLaw>)NationLaw.getGoodLaws((List<NationLaw<NationLawEvent>>)this.getProbablyTriggeredLaws());
    }
    
    private List<NationLaw> getTriggeredBadLaws() {
        if (!protectorHasNation()) {
            return (List<NationLaw>)Collections.emptyList();
        }
        return (List<NationLaw>)NationLaw.getBadLaws((List<NationLaw<NationLawEvent>>)this.getTriggeredLaws());
    }
    
    private List<NationLaw> getProbablyTriggeredBadLaws() {
        return (List<NationLaw>)NationLaw.getBadLaws((List<NationLaw<NationLawEvent>>)this.getProbablyTriggeredLaws());
    }
    
    @Nullable
    public abstract List<NationLaw> getTriggeredLaws();
    
    @Nullable
    public abstract List<NationLaw> getProbablyTriggeredLaws();
}
