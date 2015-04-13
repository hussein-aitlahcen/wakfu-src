package com.ankamagames.wakfu.client.core.nation;

import java.util.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import gnu.trove.*;

public class NationDiplomacyView extends NationFieldProvider
{
    public static final String GOVERNOR_NAME = "governorName";
    public static final String ALLIES = "allies";
    public static final String ENEMIES = "ennemies";
    public static final String IS_LOCAL_ALLY = "isLocalAlly";
    public static final String IS_LOCAl_ENEMY = "isLocalEnemy";
    public static final String IS_WAITING_FOR_ALLIANCE_RESULT = "isWaitingForAllianceResult";
    public static final String IS_WAITING_FOR_ALLIANCE_ANSWER = "isWaitingForAllianceAnswer";
    public static final String HAS_ALREADY_AN_ALLY = "hasAlreadyAnAlly";
    public static final String CAN_INVITE_ANOTHER_NATION = "canInviteAnotherNation";
    public static final String[] FIELDS;
    private ArrayList<NationFieldProvider> m_alliesViews;
    private ArrayList<NationFieldProvider> m_enemiesViews;
    private boolean m_waitingForAllianceResult;
    private boolean m_waitingForAllianceAnswer;
    private boolean m_hasAnAlly;
    private boolean m_canInviteAnotherNation;
    
    public NationDiplomacyView(final int nationId) {
        super(nationId);
        this.m_alliesViews = new ArrayList<NationFieldProvider>();
        this.m_enemiesViews = new ArrayList<NationFieldProvider>();
        this.initialize();
    }
    
    private void initialize() {
        final Nation nation = NationManager.INSTANCE.getNationById(this.m_nationId);
        this.m_hasAnAlly = false;
        this.m_canInviteAnotherNation = true;
        final TIntObjectIterator<Nation> it = NationManager.INSTANCE.realNationIterator(this.m_nationId);
        while (it.hasNext()) {
            it.advance();
            final int nationId = it.key();
            switch (nation.getDiplomacyManager().getAlignment(nationId)) {
                case ALLIED: {
                    this.m_alliesViews.add(new NationFieldProvider(nationId));
                    this.m_hasAnAlly = true;
                    continue;
                }
                case ENEMY: {
                    this.m_enemiesViews.add(new NationFieldProvider(nationId));
                    continue;
                }
            }
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return;
        }
        final Nation myNation = localPlayer.getCitizenComportment().getNation();
        final NationAlignement alignmentWaitingResult = myNation.getDiplomacyManager().getRequest(this.m_nationId);
        this.m_waitingForAllianceResult = (alignmentWaitingResult == NationAlignement.ALLIED);
        final NationAlignement alignmentToAnswer = nation.getDiplomacyManager().getRequest(myNation.getNationId());
        this.m_waitingForAllianceAnswer = (alignmentToAnswer == NationAlignement.ALLIED);
        final TIntObjectIterator<Nation> it2 = NationManager.INSTANCE.realNationIterator(myNation.getNationId());
        while (it2.hasNext()) {
            it2.advance();
            if (myNation.getDiplomacyManager().getAlignment(it2.key()) == NationAlignement.ALLIED) {
                this.m_canInviteAnotherNation = false;
                break;
            }
        }
    }
    
    @Override
    public String[] getFields() {
        return NationDiplomacyView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("governorName")) {
            final GovernmentInfo governor = NationManager.INSTANCE.getNationById(this.m_nationId).getGovernment().getGovernor();
            return (governor == null) ? WakfuTranslator.getInstance().getString("nation.noGovernorDesc") : governor.getName();
        }
        if (fieldName.equals("allies")) {
            return this.m_alliesViews;
        }
        if (fieldName.equals("ennemies")) {
            return this.m_enemiesViews;
        }
        if (fieldName.equals("isLocalAlly")) {
            return this.getLocalAlignment() == NationAlignement.ALLIED;
        }
        if (fieldName.equals("isLocalEnemy")) {
            return this.getLocalAlignment() == NationAlignement.ENEMY;
        }
        if (fieldName.equals("isWaitingForAllianceResult")) {
            return this.m_waitingForAllianceResult;
        }
        if (fieldName.equals("isWaitingForAllianceAnswer")) {
            return this.m_waitingForAllianceAnswer;
        }
        if (fieldName.equals("hasAlreadyAnAlly")) {
            return this.m_hasAnAlly;
        }
        if (fieldName.equals("canInviteAnotherNation")) {
            return this.m_canInviteAnotherNation;
        }
        return super.getFieldValue(fieldName);
    }
    
    public boolean isWaitingForAllianceAnswer() {
        return this.m_waitingForAllianceAnswer;
    }
    
    public NationAlignement getLocalAlignment() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return (localPlayer == null) ? NationAlignement.ENEMY : localPlayer.getCitizenComportment().getNation().getDiplomacyManager().getAlignment(this.m_nationId);
    }
    
    static {
        FIELDS = new String[] { "governorName", "allies", "ennemies", "isLocalAlly", "isLocalEnemy", "isWaitingForAllianceResult", "isWaitingForAllianceAnswer", "hasAlreadyAnAlly", "canInviteAnotherNation" };
    }
}
