package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.io.*;
import com.ankamagames.wakfu.common.binaryStorage.*;
import java.util.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;

public final class ScenarioBinaryStorable extends BinaryStorable
{
    private static final short CURRENT_VERSION = 1;
    private int m_id;
    private byte m_type;
    private byte m_userType;
    private boolean m_autoTrigger;
    @BinaryStorageIndex(name = "is_challenge")
    private boolean m_isChallenge;
    private boolean m_isChaos;
    private short m_duration;
    private GameDateConst m_expirationDate;
    private short m_minUsers;
    private short m_maxUsers;
    private String m_joinCriterion;
    private String m_rewardEligibilityCriterion;
    private String[] m_varMapping;
    private String m_params;
    private final ArrayList<ActionGroupStorable> m_actionGroups;
    private final ArrayList<RewardStorable> m_rewards;
    
    public ScenarioBinaryStorable() {
        super((short)1);
        this.m_actionGroups = new ArrayList<ActionGroupStorable>(2);
        this.m_rewards = new ArrayList<RewardStorable>(2);
    }
    
    public void addActionGroup(final ActionGroupStorable group) {
        this.m_actionGroups.add(group);
    }
    
    public void addReward(final RewardStorable reward) {
        this.m_rewards.add(reward);
    }
    
    @Override
    public void build(final ByteBuffer bb, final int id, final short version) {
        this.setGlobalId(id);
        if (version == 1) {
            this.m_id = bb.getInt();
            this.m_type = bb.get();
            this.m_userType = bb.get();
            this.m_autoTrigger = (bb.get() == 1);
            this.m_isChallenge = (bb.get() == 1);
            this.m_isChaos = (bb.get() == 1);
            this.m_duration = bb.getShort();
            this.m_minUsers = bb.getShort();
            this.m_maxUsers = bb.getShort();
            final long time = bb.getLong();
            if (time != 0L) {
                this.m_expirationDate = GameDate.fromLong(time);
            }
            else {
                this.m_expirationDate = null;
            }
            try {
                final byte[] bytes = new byte[bb.getInt()];
                bb.get(bytes);
                this.m_params = new String(bytes, "UTF-8").intern();
            }
            catch (UnsupportedEncodingException e) {
                ScenarioBinaryStorable.m_logger.error((Object)"Exception", (Throwable)e);
            }
            try {
                final int mappingLen = bb.getInt();
                this.m_varMapping = new String[mappingLen];
                for (int i = 0; i < mappingLen; ++i) {
                    final byte[] bytes2 = new byte[bb.getInt()];
                    bb.get(bytes2);
                    this.m_varMapping[i] = new String(bytes2, "UTF-8").intern();
                }
            }
            catch (UnsupportedEncodingException e) {
                ScenarioBinaryStorable.m_logger.error((Object)"Exception", (Throwable)e);
            }
            try {
                final byte[] bytes = new byte[bb.getInt()];
                bb.get(bytes);
                this.m_joinCriterion = new String(bytes, "UTF-8").intern();
                final byte[] rewardEligibilityCriterion = new byte[bb.getInt()];
                bb.get(rewardEligibilityCriterion);
                this.m_rewardEligibilityCriterion = new String(rewardEligibilityCriterion, "UTF-8").intern();
            }
            catch (UnsupportedEncodingException e) {
                ScenarioBinaryStorable.m_logger.error((Object)"Exception", (Throwable)e);
            }
            for (int glen = bb.getInt(), i = 0; i < glen; ++i) {
                final byte[] gdata = new byte[bb.getInt()];
                bb.get(gdata);
                final ActionGroupStorable g = new ActionGroupStorable();
                g.unserialise(gdata);
                this.m_actionGroups.add(g);
            }
            for (int rlen = bb.getInt(), j = 0; j < rlen; ++j) {
                final byte[] rdata = new byte[bb.getInt()];
                bb.get(rdata);
                final RewardStorable r = new RewardStorable();
                r.unserialise(rdata);
                this.m_rewards.add(r);
            }
        }
        else {
            ScenarioBinaryStorable.m_logger.error((Object)"Tentative de d\u00e9s\u00e9rialisation d'un objet avec une version non prise en charge");
        }
    }
    
    @Override
    public BinaryStorable createInstance() {
        return new ScenarioBinaryStorable();
    }
    
    public ActionGroupStorable createNewActionGroup() {
        return new ActionGroupStorable();
    }
    
    public RewardStorable createNewReward() {
        return new RewardStorable();
    }
    
    public ArrayList<ActionGroupStorable> getActionGroups() {
        return this.m_actionGroups;
    }
    
    @Override
    public byte[] getBinaryData() {
        final ArrayList<byte[]> binGroups = new ArrayList<byte[]>(this.m_actionGroups.size());
        final ArrayList<byte[]> binRewars = new ArrayList<byte[]>(this.m_rewards.size());
        int presize = 0;
        presize += 4;
        try {
            for (final String var : this.m_varMapping) {
                presize += 4 + var.getBytes("UTF-8").length;
            }
        }
        catch (Exception e) {
            ScenarioBinaryStorable.m_logger.error((Object)"Exception", (Throwable)e);
        }
        try {
            presize += 8;
            presize += ((this.m_joinCriterion != null) ? this.m_joinCriterion.getBytes("UTF-8").length : 0);
            presize += ((this.m_rewardEligibilityCriterion != null) ? this.m_rewardEligibilityCriterion.getBytes("UTF-8").length : 0);
        }
        catch (Exception e) {
            ScenarioBinaryStorable.m_logger.error((Object)"Exception", (Throwable)e);
        }
        for (final ActionGroupStorable g : this.m_actionGroups) {
            final byte[] bin = g.serialize();
            presize += bin.length + 4;
            binGroups.add(bin);
        }
        for (final RewardStorable r : this.m_rewards) {
            final byte[] bin = r.serialize();
            presize += bin.length + 4;
            binRewars.add(bin);
        }
        try {
            presize += 4;
            presize += ((this.m_params != null) ? this.m_params.getBytes("UTF-8").length : 0);
        }
        catch (Exception e) {
            ScenarioBinaryStorable.m_logger.error((Object)"Exception", (Throwable)e);
        }
        final ByteBuffer bb = ByteBuffer.allocate(31 + presize);
        bb.putInt(this.m_id);
        bb.put(this.m_type);
        bb.put(this.m_userType);
        bb.put((byte)(this.m_autoTrigger ? 1 : 0));
        bb.put((byte)(this.m_isChallenge ? 1 : 0));
        bb.put((byte)(this.m_isChaos ? 1 : 0));
        bb.putShort(this.m_duration);
        bb.putShort(this.m_minUsers);
        bb.putShort(this.m_maxUsers);
        if (this.m_expirationDate != null) {
            bb.putLong(this.m_expirationDate.toLong());
        }
        else {
            bb.putLong(0L);
        }
        try {
            if (this.m_params != null) {
                final byte[] bytes = this.m_params.getBytes("UTF-8");
                bb.putInt(bytes.length);
                bb.put(bytes);
            }
            else {
                bb.putInt(0);
            }
        }
        catch (Exception e2) {
            ScenarioBinaryStorable.m_logger.error((Object)"Exception", (Throwable)e2);
        }
        try {
            bb.putInt(this.m_varMapping.length);
            for (final String var2 : this.m_varMapping) {
                final byte[] bytes2 = var2.getBytes("UTF-8");
                bb.putInt(bytes2.length);
                bb.put(bytes2);
            }
        }
        catch (Exception e2) {
            ScenarioBinaryStorable.m_logger.error((Object)"Exception", (Throwable)e2);
        }
        try {
            if (this.m_joinCriterion != null) {
                final byte[] bytes = this.m_joinCriterion.getBytes("UTF-8");
                bb.putInt(bytes.length);
                bb.put(bytes);
            }
            else {
                bb.putInt(0);
            }
            if (this.m_rewardEligibilityCriterion != null) {
                final byte[] bytes = this.m_rewardEligibilityCriterion.getBytes("UTF-8");
                bb.putInt(bytes.length);
                bb.put(bytes);
            }
            else {
                bb.putInt(0);
            }
        }
        catch (Exception e2) {
            ScenarioBinaryStorable.m_logger.error((Object)"Exception", (Throwable)e2);
        }
        bb.putInt(binGroups.size());
        for (final byte[] gdata : binGroups) {
            bb.putInt(gdata.length);
            bb.put(gdata);
        }
        bb.putInt(binRewars.size());
        for (final byte[] rdata : binRewars) {
            bb.putInt(rdata.length);
            bb.put(rdata);
        }
        return bb.array();
    }
    
    @Override
    public int getBinaryType() {
        return WakfuBinaryStorableType.SCENARIO.getId();
    }
    
    public short getDuration() {
        return this.m_duration;
    }
    
    public GameDateConst getExpirationDate() {
        return this.m_expirationDate;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public ArrayList<RewardStorable> getRewards() {
        return this.m_rewards;
    }
    
    public byte getType() {
        return this.m_type;
    }
    
    public byte getUserType() {
        return this.m_userType;
    }
    
    public String[] getVarMapping() {
        return this.m_varMapping;
    }
    
    public String getJoinCriterion() {
        return this.m_joinCriterion;
    }
    
    public String getRewardEligibilityCriterion() {
        return this.m_rewardEligibilityCriterion;
    }
    
    public boolean isAutoTrigger() {
        return this.m_autoTrigger;
    }
    
    public boolean isChallenge() {
        return this.m_isChallenge;
    }
    
    public boolean isChaos() {
        return this.m_isChaos;
    }
    
    public void setAutoTrigger(final boolean autoTrigger) {
        this.m_autoTrigger = autoTrigger;
    }
    
    public void setChallenge(final boolean challenge) {
        this.m_isChallenge = challenge;
    }
    
    public void setChaos(final boolean chaos) {
        this.m_isChaos = chaos;
    }
    
    public void setDuration(final short duration) {
        this.m_duration = duration;
    }
    
    public void setMinUsers(final short minUsers) {
        this.m_minUsers = minUsers;
    }
    
    public short getMinUsers() {
        return this.m_minUsers;
    }
    
    public short getMaxUsers() {
        return this.m_maxUsers;
    }
    
    public void setMaxUsers(final short maxUsers) {
        this.m_maxUsers = maxUsers;
    }
    
    public void setExpirationDate(final Date expirationDate) {
        this.m_expirationDate = ((expirationDate != null) ? GameDate.fromJavaDate(expirationDate) : null);
    }
    
    public void setId(final int id) {
        this.m_id = id;
    }
    
    public void setType(final byte type) {
        this.m_type = type;
    }
    
    public void setUserType(final byte userType) {
        this.m_userType = userType;
    }
    
    public void setVarMapping(final String[] varMapping) {
        this.m_varMapping = varMapping;
    }
    
    public void setJoinCriterion(final String joinCriterion) {
        this.m_joinCriterion = joinCriterion;
    }
    
    public void setRewardEligibilityCriterion(final String rewardEligibilityCriterion) {
        this.m_rewardEligibilityCriterion = rewardEligibilityCriterion;
    }
    
    public void setParams(final String params) {
        this.m_params = params;
    }
    
    public String getParams() {
        return this.m_params;
    }
    
    public static class ActionGroupStorable
    {
        private int m_id;
        private int m_jaugeMaxValue;
        private boolean m_isChallengeGoal;
        private boolean m_isCountDownJauge;
        private String m_targetPosition;
        private String m_jaugeVarName;
        
        public final int getId() {
            return this.m_id;
        }
        
        public final int getJaugeMaxValue() {
            return this.m_jaugeMaxValue;
        }
        
        public final String getJaugeVarName() {
            return this.m_jaugeVarName;
        }
        
        public final String getTargetPosition() {
            return this.m_targetPosition;
        }
        
        public final boolean isChallengeGoal() {
            return this.m_isChallengeGoal;
        }
        
        public final boolean isCountDownJauge() {
            return this.m_isCountDownJauge;
        }
        
        public final byte[] serialize() {
            ByteBuffer bb = null;
            try {
                final byte[] targetPosition = (this.m_targetPosition == null) ? new byte[0] : this.m_targetPosition.getBytes("UTF-8");
                final byte[] jaugeVarName = (this.m_jaugeVarName == null) ? new byte[0] : this.m_jaugeVarName.getBytes("UTF-8");
                bb = ByteBuffer.allocate(9 + targetPosition.length + 4 + 1 + 4 + jaugeVarName.length);
                bb.putInt(this.m_id);
                bb.put((byte)(this.m_isChallengeGoal ? 1 : 0));
                bb.putInt(targetPosition.length);
                bb.put(targetPosition);
                bb.put((byte)(this.m_isCountDownJauge ? 1 : 0));
                bb.putInt(this.m_jaugeMaxValue);
                bb.putInt(jaugeVarName.length);
                bb.put(jaugeVarName);
            }
            catch (UnsupportedEncodingException e) {
                ScenarioBinaryStorable.m_logger.error((Object)"Exception", (Throwable)e);
            }
            return bb.array();
        }
        
        public final void setChallengeGoal(final boolean challengeGoal) {
            this.m_isChallengeGoal = challengeGoal;
        }
        
        public final void setCountDownJauge(final boolean countDownJauge) {
            this.m_isCountDownJauge = countDownJauge;
        }
        
        public final void setId(final int id) {
            this.m_id = id;
        }
        
        public final void setJaugeMaxValue(final int jaugeMaxValue) {
            this.m_jaugeMaxValue = jaugeMaxValue;
        }
        
        public final void setJaugeVarName(final String jaugeVarName) {
            this.m_jaugeVarName = jaugeVarName;
        }
        
        public final void setTargetPosition(final String targetPosition) {
            this.m_targetPosition = targetPosition;
        }
        
        public final void unserialise(final byte[] data) {
            final ByteBuffer bb = ByteBuffer.wrap(data);
            this.m_id = bb.getInt();
            this.m_isChallengeGoal = (bb.get() == 1);
            try {
                final byte[] targetPosition = new byte[bb.getInt()];
                bb.get(targetPosition);
                this.m_targetPosition = new String(targetPosition, "UTF-8").intern();
                this.m_isCountDownJauge = (bb.get() == 1);
                this.m_jaugeMaxValue = bb.getInt();
                final byte[] jaugeVarName = new byte[bb.getInt()];
                bb.get(jaugeVarName);
                this.m_jaugeVarName = new String(jaugeVarName, "UTF-8").intern();
            }
            catch (UnsupportedEncodingException e) {
                ScenarioBinaryStorable.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
    }
    
    public static class RewardStorable
    {
        private int m_id;
        private int m_gfx;
        private int m_itemId;
        private short m_itemQty;
        private int m_xp;
        private int m_kama;
        private int m_guildPoints;
        private String m_criterion;
        private boolean m_success;
        private byte m_order;
        
        public final String getCriterion() {
            return this.m_criterion;
        }
        
        public final int getGfx() {
            return this.m_gfx;
        }
        
        public final int getId() {
            return this.m_id;
        }
        
        public final byte getOrder() {
            return this.m_order;
        }
        
        public final boolean isSuccess() {
            return this.m_success;
        }
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public short getItemQty() {
            return this.m_itemQty;
        }
        
        public int getXp() {
            return this.m_xp;
        }
        
        public int getKama() {
            return this.m_kama;
        }
        
        public int getGuildPoints() {
            return this.m_guildPoints;
        }
        
        public final byte[] serialize() {
            final byte[] cdata = StringUtils.toUTF8(this.m_criterion);
            final int presize = cdata.length;
            final ByteBuffer bb = ByteBuffer.allocate(32 + presize);
            bb.putInt(this.m_id);
            bb.put(this.m_order);
            bb.putInt(this.m_gfx);
            bb.putInt(cdata.length);
            bb.put(cdata);
            bb.put((byte)(this.m_success ? 1 : 0));
            bb.putInt(this.m_itemId);
            bb.putShort(this.m_itemQty);
            bb.putInt(this.m_xp);
            bb.putInt(this.m_kama);
            bb.putInt(this.m_guildPoints);
            return bb.array();
        }
        
        public final void setCriterion(final String criterion) {
            this.m_criterion = criterion;
        }
        
        public final void setGfx(final int gfx) {
            this.m_gfx = gfx;
        }
        
        public final void setId(final int id) {
            this.m_id = id;
        }
        
        public void setOrder(final byte order) {
            this.m_order = order;
        }
        
        public final void setSuccess(final boolean success) {
            this.m_success = success;
        }
        
        public void setItemId(final int itemId) {
            this.m_itemId = itemId;
        }
        
        public void setItemQty(final short itemQty) {
            this.m_itemQty = itemQty;
        }
        
        public void setXp(final int xp) {
            this.m_xp = xp;
        }
        
        public void setKama(final int kama) {
            this.m_kama = kama;
        }
        
        public void setGuildPoints(final int guildPoints) {
            this.m_guildPoints = guildPoints;
        }
        
        public final void unserialise(final byte[] data) {
            final ByteBuffer bb = ByteBuffer.wrap(data);
            this.m_id = bb.getInt();
            this.m_order = bb.get();
            this.m_gfx = bb.getInt();
            final byte[] cdata = new byte[bb.getInt()];
            bb.get(cdata);
            try {
                this.m_criterion = new String(cdata, "UTF-8").intern();
            }
            catch (UnsupportedEncodingException e) {
                ScenarioBinaryStorable.m_logger.error((Object)"Exception", (Throwable)e);
            }
            this.m_success = (bb.get() == 1);
            this.m_itemId = bb.getInt();
            this.m_itemQty = bb.getShort();
            this.m_xp = bb.getInt();
            this.m_kama = bb.getInt();
            this.m_guildPoints = bb.getInt();
        }
    }
}
