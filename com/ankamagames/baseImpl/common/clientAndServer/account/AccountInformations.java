package com.ankamagames.baseImpl.common.clientAndServer.account;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class AccountInformations extends BinarSerial
{
    private static final Logger m_logger;
    public static boolean ANTI_ADDICTION_ENABLED;
    private long m_account_id;
    private String m_accountNickName;
    private Community m_accountCommunity;
    private int m_subscriptionLevel;
    private int m_antiAddictionLevel;
    private byte m_additionalSlots;
    private byte m_vaultUpgrades;
    private long m_accountExpirationDate;
    private long m_banEndDate;
    private byte[][] m_accountConnectedIp;
    private long[] m_accountConnectedDate;
    private long m_accountCreationDate;
    private long m_baseTimeElapsed;
    private long m_sessionStartTime;
    private AccountData m_accountData;
    private int[] m_adminRights;
    private final Collection<AccountInformationsListener> m_listeners;
    protected final BinarSerialPart PUBLIC_INFORMATIONS;
    protected final BinarSerialPart PRIVATE_INFORMATIONS;
    
    public AccountInformations() {
        super();
        this.m_accountConnectedIp = new byte[3][];
        this.m_accountConnectedDate = new long[3];
        this.m_adminRights = new int[AdminRightHelper.getMaskArraySize()];
        this.m_listeners = new ArrayList<AccountInformationsListener>();
        this.PUBLIC_INFORMATIONS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong(AccountInformations.this.m_account_id);
                buffer.putInt(AccountInformations.this.m_subscriptionLevel);
                buffer.putInt(AccountInformations.this.m_antiAddictionLevel);
                buffer.putLong(AccountInformations.this.m_accountExpirationDate);
                for (final int right : AccountInformations.this.m_adminRights) {
                    buffer.putInt(right);
                }
                final byte[] nickName = StringUtils.toUTF8(AccountInformations.this.m_accountNickName);
                buffer.put((byte)nickName.length);
                buffer.put(nickName);
                buffer.putInt(AccountInformations.this.m_accountCommunity.getId());
                AccountInformations.this.m_accountData.serialize(buffer);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                AccountInformations.this.m_account_id = buffer.getLong();
                AccountInformations.this.m_subscriptionLevel = buffer.getInt();
                AccountInformations.this.m_antiAddictionLevel = buffer.getInt();
                AccountInformations.this.m_accountExpirationDate = buffer.getLong();
                for (int i = 0; i < AdminRightHelper.getMaskArraySize(); ++i) {
                    AccountInformations.this.m_adminRights[i] = buffer.getInt();
                }
                final byte[] nickName = new byte[buffer.get() & 0xFF];
                buffer.get(nickName);
                AccountInformations.this.m_accountNickName = StringUtils.fromUTF8(nickName);
                AccountInformations.this.m_accountCommunity = Community.getFromId(buffer.getInt());
                AccountInformations.this.m_accountData.unserialize(buffer);
            }
            
            @Override
            public int expectedSize() {
                int size = 0;
                size += 24 + 4 * AdminRightHelper.getMaskArraySize();
                final byte[] nickName = StringUtils.toUTF8(AccountInformations.this.m_accountNickName);
                size += 1 + nickName.length;
                size += 4;
                size += AccountInformations.this.m_accountData.serializedSize();
                return size;
            }
        };
        this.PRIVATE_INFORMATIONS = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("Non impl\u00e9ment\u00e9");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                throw new UnsupportedOperationException("Non impl\u00e9ment\u00e9");
            }
        };
        this.m_accountData = new AccountData();
        this.m_sessionStartTime = System.currentTimeMillis();
    }
    
    public int getSubscriptionLevel() {
        return this.m_subscriptionLevel;
    }
    
    public void setSubscriptionLevel(final int subscriptionLevel) {
        this.m_subscriptionLevel = subscriptionLevel;
    }
    
    public int getAntiAddictionLevel() {
        return this.m_antiAddictionLevel;
    }
    
    public byte getAdditionalSlots() {
        return this.m_additionalSlots;
    }
    
    public byte getVaultUpgrades() {
        return this.m_vaultUpgrades;
    }
    
    public long getAccountExpirationDate() {
        return this.m_accountExpirationDate;
    }
    
    public void setAccountExpirationDate(final long expirationDate) {
        this.m_accountExpirationDate = expirationDate;
        this.onSetAccountExpirationDate();
    }
    
    private void onSetAccountExpirationDate() {
        for (final AccountInformationsListener listener : this.m_listeners) {
            try {
                listener.accountExpirationDateChanged(this);
            }
            catch (Exception e) {
                AccountInformations.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    public long getBanEndDate() {
        return this.m_banEndDate;
    }
    
    public void setBanEndDate(final long banEndDate) {
        this.m_banEndDate = banEndDate;
    }
    
    public void setAccountCreationDate(final long accountCreationDate) {
        this.m_accountCreationDate = accountCreationDate;
    }
    
    public long getAccountCreationDate() {
        return this.m_accountCreationDate;
    }
    
    public final void setAccountConnectedInfo(final int i, final byte[] ip, final long date) {
        if (i < 0 || i >= 3) {
            throw new IllegalArgumentException("On ne conserve que 3 derni\u00e8res IP utilis\u00e9es");
        }
        this.m_accountConnectedIp[i] = ip;
        this.m_accountConnectedDate[i] = date;
    }
    
    public byte[] getAccountConnectedIp(final int i) {
        if (i < 0 || i >= 3) {
            throw new IllegalArgumentException("On ne conserve que 3 derni\u00e8res IP utilis\u00e9es");
        }
        return this.m_accountConnectedIp[i];
    }
    
    public long getAccountConnectedDate(final int i) {
        if (i < 0 || i >= 3) {
            throw new IllegalArgumentException("On ne conserve que 3 derni\u00e8res IP utilis\u00e9es");
        }
        return this.m_accountConnectedDate[i];
    }
    
    public void updateAccountConnectedInfo(final byte[] currentIp, final long currentDate) {
        if (currentIp[0] == this.m_accountConnectedIp[0][0] && currentIp[1] == this.m_accountConnectedIp[0][1] && currentIp[2] == this.m_accountConnectedIp[0][2] && currentIp[3] == this.m_accountConnectedIp[0][3]) {
            this.m_accountConnectedDate[0] = currentDate;
        }
        else {
            this.m_accountConnectedIp[2] = this.m_accountConnectedIp[1];
            this.m_accountConnectedIp[1] = this.m_accountConnectedIp[0];
            this.m_accountConnectedIp[0] = currentIp;
            this.m_accountConnectedDate[2] = this.m_accountConnectedDate[1];
            this.m_accountConnectedDate[1] = this.m_accountConnectedDate[0];
            this.m_accountConnectedDate[0] = currentDate;
        }
    }
    
    public long getTotalTimeElapsed() {
        return this.m_baseTimeElapsed + System.currentTimeMillis() - this.m_sessionStartTime;
    }
    
    public void setBaseTimeElapsed(final long baseTotalTimeElapsed) {
        this.m_baseTimeElapsed = baseTotalTimeElapsed;
    }
    
    public String getAccountNickName() {
        return this.m_accountNickName;
    }
    
    public void setAccountNickName(final String accountNickName) {
        this.m_accountNickName = accountNickName;
    }
    
    public Community getAccountCommunity() {
        return this.m_accountCommunity;
    }
    
    public void setAccountCommunity(final Community accountCommunity) {
        this.m_accountCommunity = accountCommunity;
    }
    
    public long getAccountId() {
        return this.m_account_id;
    }
    
    public void setAccount_id(final long account_id) {
        this.m_account_id = account_id;
    }
    
    public int[] getAdminRights() {
        return this.m_adminRights;
    }
    
    public void setAdminRights(final int[] adminRights) {
        this.m_adminRights = adminRights;
    }
    
    public void buildAccountData(final Map<String, Long> accountData) {
        this.m_antiAddictionLevel = (AccountInformations.ANTI_ADDICTION_ENABLED ? -1 : 0);
        for (final Map.Entry<String, Long> accountDataEntry : accountData.entrySet()) {
            final AccountDataFlagType accountDataFlagType = AccountDataFlagType.fromWebRepresentation(accountDataEntry.getKey());
            if (accountDataFlagType == null) {
                continue;
            }
            this.m_accountData.addFlag(new AccountDataFlag(accountDataFlagType, accountDataEntry.getValue()));
            if (AccountInformations.ANTI_ADDICTION_ENABLED && accountDataFlagType == AccountDataFlagType.ANTI_ADDICTION) {
                this.m_antiAddictionLevel = MathHelper.ensureInt(accountDataEntry.getValue());
            }
            else if (accountDataFlagType == AccountDataFlagType.CHARACTER_SLOTS) {
                this.m_additionalSlots = MathHelper.ensureByte(accountDataEntry.getValue());
            }
            else {
                if (accountDataFlagType != AccountDataFlagType.VAULT_UPGRADE) {
                    continue;
                }
                this.m_vaultUpgrades = MathHelper.ensureByte(accountDataEntry.getValue());
            }
        }
    }
    
    public AccountData getAccountData() {
        return this.m_accountData;
    }
    
    @Override
    public BinarSerialPart[] partsEnumeration() {
        return new BinarSerialPart[] { this.PUBLIC_INFORMATIONS, this.PRIVATE_INFORMATIONS };
    }
    
    public byte[] serializeForClient() {
        return this.build(this.PUBLIC_INFORMATIONS);
    }
    
    public byte[] serializeForAdmin() {
        return this.build(this.PRIVATE_INFORMATIONS);
    }
    
    public void addListener(final AccountInformationsListener listener) {
        if (this.m_listeners.contains(listener)) {
            return;
        }
        this.m_listeners.add(listener);
    }
    
    public void removeListener(final AccountInformationsListener listener) {
        this.m_listeners.remove(listener);
    }
    
    @Override
    public String toString() {
        return "AccountInformations{m_account_id=" + this.m_account_id + ", m_accountNickName='" + this.m_accountNickName + '\'' + ", m_accountCommunity=" + this.m_accountCommunity + ", m_subscriptionLevel=" + this.m_subscriptionLevel + ", m_antiAddictionLevel=" + this.m_antiAddictionLevel + ", m_additionalSlots=" + this.m_additionalSlots + ", m_vaultUpgrades=" + this.m_vaultUpgrades + ", m_accountExpirationDate=" + this.m_accountExpirationDate + ", m_banEndDate=" + this.m_banEndDate + ", m_accountConnectedIp=" + Arrays.toString(this.m_accountConnectedIp) + ", m_accountConnectedDate=" + Arrays.toString(this.m_accountConnectedDate) + ", m_baseTimeElapsed=" + this.m_baseTimeElapsed + ", m_sessionStartTime=" + this.m_sessionStartTime + ", m_adminRights=" + Arrays.toString(this.m_adminRights) + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)AccountInformations.class);
    }
}
