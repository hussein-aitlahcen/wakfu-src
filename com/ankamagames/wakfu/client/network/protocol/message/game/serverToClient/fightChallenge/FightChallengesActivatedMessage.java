package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fightChallenge;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public class FightChallengesActivatedMessage extends InputOnlyProxyMessage
{
    private final TIntArrayList m_challenges;
    private final TIntArrayList m_dropLevel;
    private final TIntArrayList m_xpLevel;
    
    public FightChallengesActivatedMessage() {
        super();
        this.m_challenges = new TIntArrayList();
        this.m_dropLevel = new TIntArrayList();
        this.m_xpLevel = new TIntArrayList();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        for (int i = 0, size = buffer.get(); i < size; ++i) {
            this.m_challenges.add(buffer.getInt());
            this.m_dropLevel.add(buffer.getInt());
            this.m_xpLevel.add(buffer.getInt());
        }
        return true;
    }
    
    public TIntArrayList getChallenges() {
        return this.m_challenges;
    }
    
    public TIntArrayList getDropLevel() {
        return this.m_dropLevel;
    }
    
    public TIntArrayList getXpLevel() {
        return this.m_xpLevel;
    }
    
    @Override
    public int getId() {
        return 15510;
    }
}
