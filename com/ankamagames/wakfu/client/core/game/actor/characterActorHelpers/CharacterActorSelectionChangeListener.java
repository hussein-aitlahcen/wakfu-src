package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.ui.protocol.message.overHeadInfos.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.group.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.colors.*;
import com.ankamagames.framework.java.util.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.datas.group.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.datas.Breed.*;

public final class CharacterActorSelectionChangeListener implements InteractiveElementSelectionChangeListener<Mobile>
{
    private static final Logger m_logger;
    private static final DelayedFightPreview m_preview;
    private final CharacterActor m_actor;
    
    public CharacterActorSelectionChangeListener(final CharacterActor actor) {
        super();
        assert actor != null;
        this.m_actor = actor;
    }
    
    @Override
    public void selectionChanged(final Mobile mobile, final boolean selected) {
        assert mobile == this.m_actor;
        if (isSelectingCellForInteraction()) {
            return;
        }
        if (selected) {
            select(this.m_actor);
        }
        else {
            unselect(this.m_actor);
        }
    }
    
    public static void select(final CharacterActor actor) {
        final Widget widget = MasterRootContainer.getInstance().getMouseOver();
        if (widget != null && widget != MasterRootContainer.getInstance()) {
            return;
        }
        final CharacterInfo characterInfo = actor.getCharacterInfo();
        if (characterInfo == null) {
            return;
        }
        if (characterInfo.isInvisibleForLocalPlayer()) {
            return;
        }
        if (characterInfo.hasProperty(FightPropertyType.DISPLAYED_LIKE_A_DECORATION)) {
            return;
        }
        final UIShowOverHeadInfosMessage msg = new UIShowOverHeadInfosMessage(actor, actor.getTextOffset());
        actor.addStartPathListener(new MobileStartPathListener() {
            @Override
            public void pathStarted(final PathMobile mobile, final PathFindResult path) {
                UIOverHeadInfosFrame.getInstance().hideOverHead(actor);
                actor.removeStartListener(this);
            }
        });
        ApsOnVoodoolTarget.INSTANCE.applyApsOnVoodoolTarget(true, actor.getCharacterInfo());
        prepareMessage(characterInfo, msg);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void prepareMessage(final CharacterInfo characterInfo, final UIShowOverHeadInfosMessage msg) {
        final TextWidgetFormater[] additionnalText = { new TextWidgetFormater(), new TextWidgetFormater() };
        final boolean extended = characterInfo.getActor().displayExtendedInfos();
        CharacterInfo overHeadInfosCharacter;
        CharacterInfo statesCharacter;
        CharacterInfo iconCharacter;
        if (characterInfo.isActiveProperty(FightPropertyType.IS_A_COPY_OF_HIS_CONTROLLER)) {
            overHeadInfosCharacter = characterInfo.getOriginalController();
            statesCharacter = characterInfo.getOriginalController();
            iconCharacter = characterInfo.getOriginalController();
        }
        else {
            statesCharacter = characterInfo;
            overHeadInfosCharacter = characterInfo;
            if (characterInfo.getBreedId() == 1550) {
                iconCharacter = characterInfo.getController();
            }
            else {
                iconCharacter = characterInfo;
            }
        }
        String iconUrl = null;
        if (!iconCharacter.isDead() && !iconCharacter.isNotEcosystemNpc()) {
            if (iconCharacter instanceof NonPlayerCharacter) {
                selectNonPlayerCharacters(extended, iconCharacter, additionnalText, msg);
            }
            iconUrl = getIconUrl(iconCharacter);
            setBreedIconUrl(msg, iconCharacter);
            highlightCharacterInFight(characterInfo, true);
        }
        final int localFightId = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFightId();
        final int selectCharacterFightId = characterInfo.getCurrentFightId();
        final boolean selectedCharacterIsInMyFight = localFightId == selectCharacterFightId;
        final Fight currentFight = characterInfo.getCurrentFight();
        if (selectedCharacterIsInMyFight && currentFight != null && currentFight.getModel().isArcadeType() && iconCharacter instanceof NonPlayerCharacter && iconCharacter.isActiveProperty(WorldPropertyType.IS_ARCADE_WAVE_NPC)) {
            additionnalText[1].newLine().openText().addColor(Color.GOLD.getRGBtoHex());
            additionnalText[1].append(WakfuTranslator.getInstance().getString("arcadeDungeon.monsterScoreBonus", ((NonPlayerCharacter)characterInfo).getArcadeScore()));
            additionnalText[1].closeText();
        }
        displayVoodooParticles(characterInfo, selectedCharacterIsInMyFight);
        final boolean selectedCharacterIsInExternalFight = characterInfo.isOnFight() && !selectedCharacterIsInMyFight;
        if (selectedCharacterIsInExternalFight) {
            additionnalText[1].newLine().append(WakfuTranslator.getInstance().getString("desc.inFight"));
        }
        final boolean selectedCharacterFightIsNotObserved = selectedCharacterIsInExternalFight && FightVisibilityManager.getInstance().getParticipatingFight() != selectCharacterFightId;
        if (selectedCharacterFightIsNotObserved) {
            displayFightPreview(FightManager.getInstance().getFightById(selectCharacterFightId));
        }
        if (currentFight != null && !HoodedMonsterFightEventListener.isVisuallyHooded(statesCharacter)) {
            final String desc = statesCharacter.getStatesIconDescription();
            if (desc != null && desc.length() > 0) {
                additionnalText[1].newLine().append(desc);
            }
        }
        if (msg.getOverHeadInfos().isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(additionnalText[0].finishAndToString());
            sb.append(getOverHeadInfos(overHeadInfosCharacter, extended));
            sb.append(additionnalText[1].finishAndToString());
            msg.addInfo(sb.toString(), iconUrl);
        }
    }
    
    public static void setBreedIconUrl(final UIShowOverHeadInfosMessage msg, final CharacterInfo character) {
        final String popupIconFileName = getPopupIconFileName(character);
        String breedIconUrl = null;
        if (popupIconFileName != null) {
            try {
                breedIconUrl = String.format(WakfuConfiguration.getInstance().getString("popupIconPath"), popupIconFileName);
            }
            catch (PropertyException e) {
                CharacterActorSelectionChangeListener.m_logger.error((Object)e.getMessage(), (Throwable)e);
            }
        }
        msg.setBreedIconUrl(breedIconUrl);
    }
    
    private static String getPopupIconFileName(final CharacterInfo iconCharacter) {
        if (iconCharacter instanceof PlayerCharacter) {
            return "breed" + iconCharacter.getBreedId();
        }
        String popupIconFileName = null;
        if (iconCharacter instanceof NonPlayerCharacter) {
            final Protector protector = (Protector)iconCharacter.getProtector();
            final MonsterBreed monsterBreed = MonsterBreedManager.getInstance().getBreedFromId(iconCharacter.getBreedId());
            final MonsterFamily family = MonsterFamilyManager.getInstance().getMonsterFamily(monsterBreed.getFamilyId());
            final int parentFamilyId = (iconCharacter.isActiveProperty(WorldPropertyType.NPC) && protector == null) ? 0 : family.getParentFamilyId();
            DescribedNonPlayerCharacterType describedNonPlayerCharacterType = DescribedNonPlayerCharacterType.MOB;
            if (parentFamilyId == 0) {
                describedNonPlayerCharacterType = DescribedNonPlayerCharacterType.PNJ;
            }
            if (protector != null) {
                describedNonPlayerCharacterType = DescribedNonPlayerCharacterType.PROTECTOR;
            }
            if (describedNonPlayerCharacterType == DescribedNonPlayerCharacterType.MOB) {
                popupIconFileName = "monsters";
            }
            else if (describedNonPlayerCharacterType == DescribedNonPlayerCharacterType.HOODED_MONSTER) {
                popupIconFileName = "hooded";
            }
        }
        return popupIconFileName;
    }
    
    private static void displayVoodooParticles(final CharacterInfo characterInfo, final boolean selectedCharacterIsInMyFight) {
        if (selectedCharacterIsInMyFight && !characterInfo.isDead() && characterInfo.isNotEcosystemNpc()) {
            for (final RunningEffect effect : characterInfo.getRunningEffectManager()) {
                final EffectUser target = effect.getTarget();
                if (target != null && effect.getGenericEffect() != null && effect.getGenericEffect().getEffectId() == 25274) {
                    final CellParticleSystem particleSystem = IsoParticleSystemFactory.getInstance().getCellParticleSystem(78900);
                    particleSystem.setPosition(target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude());
                    particleSystem.setDuration(2000);
                    IsoParticleSystemManager.getInstance().addParticleSystem(particleSystem);
                }
            }
        }
    }
    
    public static String getOverHeadInfos(CharacterInfo info, final boolean extendedInfos) {
        final TextWidgetFormater buffer = new TextWidgetFormater();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final boolean isDouble = info.getBreedId() == 1550;
        buffer.b();
        if (info.getCurrentFight() != null) {
            buffer.append(info.getControllerName());
            final int characteristicValue = info.getCharacteristicValue(FighterCharacteristicType.HP) + info.getCharacteristicValue(FighterCharacteristicType.VIRTUAL_HP);
            buffer.append(" (").append(WakfuTranslator.getInstance().getString("hp.var", characteristicValue)).append(")");
            final float healResist = info.getFinalHealResist();
            if (healResist > 1.0f) {
                buffer.newLine().append(WakfuTranslator.getInstance().getString("desc.healRes", (int)healResist));
            }
        }
        else {
            if (extendedInfos) {
                buffer.append("[").append(info.getId()).append("] ");
            }
            if (info instanceof NonPlayerCharacter && (info.isDead() || info.isActiveProperty(WorldPropertyType.DEAD))) {
                buffer.append(WakfuTranslator.getInstance().getString("npc.corpse"));
            }
            buffer.append(info.getControllerName());
            if (extendedInfos) {
                buffer.append(" (").append(info.getWorldCellX()).append(";").append(info.getWorldCellY()).append(") ");
            }
            if (info.isLocalPlayer()) {
                buffer.append(" (").append(WakfuTranslator.getInstance().getString("levelShort.custom", info.getLevel())).append(")");
            }
        }
        if (displaySummonerName(info, isDouble)) {
            buffer.append("\n").append(WakfuTranslator.getInstance().getString("owners.summoning", info.getController().getName()));
        }
        if (info instanceof NonPlayerCharacter && info.isActiveProperty(WorldPropertyType.COMPANION)) {
            buffer.append("\n").append(WakfuTranslator.getInstance().getString("companionOwnedBy", info.getController().getName()));
        }
        if (info instanceof NonPlayerCharacter && !info.isDead() && !info.isActiveProperty(WorldPropertyType.DEAD) && !isDouble) {
            buffer.append("\n(").append(WakfuTranslator.getInstance().getString("levelShort.custom", info.getLevel()));
            buffer.append(")");
        }
        if (info.isOffPlay()) {
            buffer.append(" ").append(WakfuTranslator.getInstance().getString("koMessage", info.getCharacteristicValue(FighterCharacteristicType.KO_TIME_BEFORE_DEATH)));
        }
        buffer._b();
        if (localPlayer.getCurrentFight() != null && info.getCurrentFightId() == localPlayer.getCurrentFightId() && info.getId() != localPlayer.getId()) {
            displaySpecificBreedInfo(buffer, info, localPlayer);
        }
        if (isDouble) {
            info = info.getController();
        }
        final CitizenComportment myComp = localPlayer.getCitizenComportment();
        if (info instanceof NonPlayerCharacter) {
            final Protector protector = (Protector)info.getProtector();
            if (protector != null) {
                final Nation nation = protector.getCurrentNation();
                final int nationId = nation.getNationId();
                final TextWidgetFormater formater = new TextWidgetFormater();
                formater.openText();
                formater.addColor(DiplomacyColorHelper.getColor(myComp, nation).getRGBtoHex());
                formater.append(WakfuTranslator.getInstance().getString(39, nationId, new Object[0])).closeText();
                buffer.append("\n").append(formater.toString());
            }
        }
        else if (info instanceof PlayerCharacter) {
            final PlayerCharacter pc = (PlayerCharacter)info;
            final byte tempoGender = StringFormatter.getGender();
            StringFormatter.setGender(pc.getSex());
            final String title = pc.getCurrentTitleDescription();
            StringFormatter.setGender(tempoGender);
            if (title != null) {
                buffer.newLine().i().append(title)._i();
            }
            if (pc.isInGuild() && pc.isInGuild()) {
                final ClientGuildInformationHandler handler = pc.getGuildHandler();
                buffer.newLine().i().append(handler.getName())._i();
            }
        }
        return buffer.finishAndToString();
    }
    
    private static boolean displaySummonerName(final CharacterInfo info, final boolean isDouble) {
        return info.isSummoned() && !isDouble && info.getController() != info;
    }
    
    private static void displaySpecificBreedInfo(final TextWidgetFormater buffer, final CharacterInfo info, final LocalPlayerCharacter localPlayer) {
        if (localPlayer.getBreed() == AvatarBreed.SACRIER) {
            displaySacrierInfo(buffer, info, localPlayer);
            return;
        }
        if (localPlayer.getBreed() == AvatarBreed.SRAM) {
            displaySramInfo(buffer, info);
        }
    }
    
    private static void displaySramInfo(final TextWidgetFormater buffer, final CharacterInfo info) {
        final FighterCharacteristic infoHp = info.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP);
        final FighterCharacteristic infoHp2 = info.getCharacteristic((CharacteristicType)FighterCharacteristicType.VIRTUAL_HP);
        final float infoLifePerc = (infoHp.value() + infoHp2.value()) / (infoHp.max() + infoHp2.max());
        buffer.newLine().openText();
        if (0.35f > infoLifePerc) {
            buffer.addColor(Color.GREEN.getRGBtoHex());
        }
        else {
            buffer.addColor(Color.GRAY.getRGBtoHex());
        }
        buffer.append(WakfuTranslator.getInstance().getString("hasBledDryBonus", (int)(infoLifePerc * 100.0f))).closeText();
    }
    
    private static void displaySacrierInfo(final TextWidgetFormater buffer, final CharacterInfo info, final LocalPlayerCharacter localPlayer) {
        final FighterCharacteristic infoHp = info.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP);
        final FighterCharacteristic infoHp2 = info.getCharacteristic((CharacteristicType)FighterCharacteristicType.VIRTUAL_HP);
        final float infoLifePerc = (infoHp.value() + infoHp2.value()) / (infoHp.max() + infoHp2.max());
        final FighterCharacteristic localHp = localPlayer.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP);
        final FighterCharacteristic localHp2 = localPlayer.getCharacteristic((CharacteristicType)FighterCharacteristicType.VIRTUAL_HP);
        final float localLifePerc = (localHp.value() + localHp2.value()) / (localHp.max() + localHp2.max());
        if (localLifePerc < infoLifePerc) {
            buffer.newLine().openText().addColor(Color.GREEN.getRGBtoHex()).append(WakfuTranslator.getInstance().getString("hasMoreLife")).closeText();
        }
    }
    
    public static void unselect(final CharacterActor actor) {
        UIOverHeadInfosFrame.getInstance().hideOverHead(actor);
        final CharacterInfo characterInfo = actor.getCharacterInfo();
        if (characterInfo == null) {
            return;
        }
        if (characterInfo instanceof NonPlayerCharacter) {
            final NPCGroupInformation groupInformation = NPCGroupInformationManager.getInstance().getGroupInformation(((NonPlayerCharacter)characterInfo).getGroupId());
            if (!characterInfo.isOnFight() && groupInformation != null) {
                for (final NPCGroupInformation.NPCInformation npcInfo : groupInformation.getMembersInformations()) {
                    final CharacterInfo npc = CharacterInfoManager.getInstance().getCharacter(npcInfo.getId());
                    if (npc != null) {
                        npc.getActor().resetColor();
                    }
                }
            }
        }
        ApsOnVoodoolTarget.INSTANCE.cleanCellParticleSystem();
        removeFightPreview();
        highlightCharacterInFight(characterInfo, false);
    }
    
    private static void selectNonPlayerCharacters(final boolean extended, final CharacterInfo characterInfo, final TextWidgetFormater[] additionnalText, final UIShowOverHeadInfosMessage msg) {
        final NPCGroupInformation groupInformation = NPCGroupInformationManager.getInstance().getGroupInformation(((NonPlayerCharacter)characterInfo).getGroupId());
        if (!characterInfo.isOnFight() && groupInformation != null) {
            final int totalLevel = groupInformation.getRealGroupLevel();
            if (groupInformation.getMembersInformations().size() > 1) {
                setTotalLevel(msg, totalLevel);
            }
            if (extended) {
                writeId(msg, groupInformation.getId());
                final ProtectorBase protector = characterInfo.getProtector();
                if (protector != null) {
                    writeProtectorId(msg, protector.getId());
                }
            }
            final List<NPCGroupInformation.NPCInformation> informations = groupInformation.getMembersInformations();
            for (int size = informations.size(), i = 0; i < size; ++i) {
                final NPCGroupInformation.NPCInformation npcInfo = informations.get(i);
                selectNPC(extended, characterInfo, msg, npcInfo);
            }
            writeSyntheticLevel(additionnalText, totalLevel);
        }
        else if (groupInformation == null) {
            CharacterActorSelectionChangeListener.m_logger.info((Object)("On ne dispose pas d'information sur le groupe du NPC id=" + characterInfo.getId() + " : \u00e9trange"));
        }
        if (extended && characterInfo.isOnFight()) {
            writeFightInfo(characterInfo, additionnalText);
        }
    }
    
    public static void setTotalLevel(final UIShowOverHeadInfosMessage msg, final int totalLevel) {
        msg.setTitle(new TextWidgetFormater().b().append(WakfuTranslator.getInstance().getString("levelShort.custom", totalLevel))._b().finishAndToString());
    }
    
    private static void writeFightInfo(final CharacterInfo characterInfo, final TextWidgetFormater[] additionnalText) {
        final TextWidgetFormater sb = additionnalText[1];
        sb.newLine().u().append("        ")._u().newLine();
        sb.openText().addColor("999999").append("cheap fight debug panel :").newLine();
        sb.append("summoned=").append(characterInfo.isSummoned()).newLine();
        sb.append(characterInfo.aggroToString()).closeText();
    }
    
    private static void writeSyntheticLevel(final TextWidgetFormater[] additionnalText, final double totalLevel) {
    }
    
    private static void writeLevel(final UIShowOverHeadInfosMessage msg, final double totalLevel) {
        msg.addInfo(String.format("<b>Group level: %.2f</b>", totalLevel));
    }
    
    private static void writeId(final UIShowOverHeadInfosMessage msg, final long groupId) {
        msg.addInfo(String.format("<b>Group ID: %d</b>", groupId));
    }
    
    private static void writeProtectorId(final UIShowOverHeadInfosMessage msg, final int protectorId) {
        msg.addInfo(String.format("<b>Protector ID: %d</b>", protectorId));
    }
    
    public static void selectNPC(final boolean extended, final CharacterInfo characterInfo, final UIShowOverHeadInfosMessage msg, final NPCGroupInformation.NPCInformation npcInfo) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        final MonsterBreed monsterBreed = MonsterBreedManager.getInstance().getBreedFromId(npcInfo.getReferenceId());
        final MonsterFamily family = MonsterFamilyManager.getInstance().getMonsterFamily(monsterBreed.getFamilyId());
        final Protector protector = (Protector)characterInfo.getProtector();
        final int parentFamilyId = (characterInfo.isActiveProperty(WorldPropertyType.NPC) && protector == null) ? 0 : family.getParentFamilyId();
        DescribedNonPlayerCharacterType describedNonPlayerCharacterType = DescribedNonPlayerCharacterType.MOB;
        if (parentFamilyId == 0) {
            describedNonPlayerCharacterType = DescribedNonPlayerCharacterType.PNJ;
        }
        if (protector != null) {
            describedNonPlayerCharacterType = DescribedNonPlayerCharacterType.PROTECTOR;
        }
        if (HoodedMonsterFightEventListener.isVisuallyHooded(characterInfo)) {
            describedNonPlayerCharacterType = DescribedNonPlayerCharacterType.HOODED_MONSTER;
        }
        if (npcInfo.getId() == characterInfo.getId() && describedNonPlayerCharacterType != DescribedNonPlayerCharacterType.PNJ) {
            sb.b();
        }
        sb.append(npcInfo.toString(extended, describedNonPlayerCharacterType));
        if (npcInfo.getId() == characterInfo.getId() && describedNonPlayerCharacterType != DescribedNonPlayerCharacterType.PNJ) {
            sb._b();
        }
        GroupDifficultyColor color = GetGroupDifficultyColor(npcInfo, monsterBreed);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (protector != null) {
            if (protector.getNpcId() == characterInfo.getId()) {
                color = GroupDifficultyColor.EASY;
            }
            final Nation nation = protector.getCurrentNation();
            final int nationId = nation.getNationId();
            final CitizenComportment comp = localPlayer.getCitizenComportment();
            final Nation localNation = comp.getNation();
            sb.newLine().openText();
            sb.addColor(DiplomacyColorHelper.getColor(comp, nation).getRGBtoHex());
            sb.append(WakfuTranslator.getInstance().getString(39, nationId, new Object[0])).closeText();
        }
        String iconUrl = null;
        if (parentFamilyId != 0) {
            iconUrl = color.getIconURL(parentFamilyId);
            if (!URLUtils.urlExists(iconUrl)) {
                iconUrl = color.getIconURL(0);
            }
        }
        final ArrayList<String> overIconUrl = new ArrayList<String>();
        final Protector zoneProtector = ProtectorView.getInstance().getProtector();
        if (zoneProtector != null) {
            final ProtectorEcosystemHandler handler = zoneProtector.getEcosystemHandler();
            if (handler.isProtectedMonsterFamily(family.getFamilyId())) {
                overIconUrl.add(WakfuConfiguration.getInstance().getIconUrl("ecosystemProtectedIconPath", "defaultIconPath", new Object[0]));
                msg.setBreedIconColor(Color.RED);
            }
        }
        if (localPlayer.getBreed() == AvatarBreed.OSAMODAS && monsterBreed.containsBaseFightProperties(FightPropertyType.CAN_BE_SEDUCED)) {
            overIconUrl.add(WakfuConfiguration.getInstance().getIconUrl("osamodasMonsterIconPath", "defaultIconPath", new Object[0]));
        }
        msg.addInfo(sb.finishAndToString(), iconUrl, overIconUrl);
        final NonPlayerCharacter npc = (NonPlayerCharacter)CharacterInfoManager.getInstance().getCharacter(npcInfo.getId());
        if (npc != null) {
            MobileColorizeHelper.onHover(npc.getActor());
        }
    }
    
    private static GroupDifficultyColor GetGroupDifficultyColor(final NPCGroupInformation.NPCInformation npcInfo, final MonsterBreed monsterBreed) {
        switch (monsterBreed.getRank()) {
            case FAMILY_LEADER: {
                return GroupDifficultyColor.KING;
            }
            case LITTLE_GOD: {
                return GroupDifficultyColor.CELEST;
            }
            default: {
                return GroupDifficultyColor.getGroupDifficultyColor(WakfuGameEntity.getInstance().getLocalPlayer().getLevel(), npcInfo.getLevel());
            }
        }
    }
    
    public static String getIconUrl(final CharacterInfo character) {
        if (!(character instanceof PlayerCharacter)) {
            return null;
        }
        final PlayerCharacter playerCharacter = (PlayerCharacter)character;
        final long guildId = playerCharacter.getGuildId();
        if (guildId <= 0L) {
            return null;
        }
        final GuildBlazon blazon = new GuildBlazon(playerCharacter.getGuildHandler().getBlazon());
        final GuildBannerData data = new GuildBannerData(blazon.getShapeId(), blazon.getSymbolId(), GuildBannerColor.getInstance().getColor(blazon.getSymbolColor()), GuildBannerColor.getInstance().getColor(blazon.getShapeColor()));
        GuildBannerGenerator.getInstance().getGuildBannerTexture(data);
        data.cleanUp();
        return String.valueOf(data.getTextureName());
    }
    
    private static void highlightCharacterInFight(final CharacterInfo characterInfo, final boolean activated) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Fight currentFight = localPlayer.getCurrentFight();
        if (currentFight != null && localPlayer.getCurrentFightId() == characterInfo.getCurrentFightId()) {
            currentFight.getTimeline().highlightFighter(characterInfo, activated);
        }
    }
    
    private static boolean isSelectingCellForInteraction() {
        return UIFightItemUseInteractionFrame.getInstance().isFrameAdded() || UIFightSpellCastAndItemUseInteractionFrame.getInstance().isFrameAdded() || UIFightSpellCastInteractionFrame.getInstance().isFrameAdded();
    }
    
    private static void displayFightPreview(final FightInfo fightInfo) {
        CharacterActorSelectionChangeListener.m_preview.removeFightPreview();
        CharacterActorSelectionChangeListener.m_preview.setFightInfo(fightInfo);
        final long clockDelay = (long)(WakfuClientInstance.getInstance().getGamePreferences().getFloatValue(WakfuKeyPreferenceStoreEnum.OVER_HEAD_DELAY_KEY) * 1000.0f);
        MessageScheduler.getInstance().addClock(CharacterActorSelectionChangeListener.m_preview, clockDelay, 0, 1);
    }
    
    private static void removeFightPreview() {
        CharacterActorSelectionChangeListener.m_preview.removeFightPreview();
    }
    
    static {
        m_logger = Logger.getLogger((Class)CharacterActorSelectionChangeListener.class);
        m_preview = new DelayedFightPreview();
    }
    
    public enum DescribedNonPlayerCharacterType
    {
        PNJ, 
        MOB, 
        PROTECTOR, 
        HOODED_MONSTER;
    }
}
