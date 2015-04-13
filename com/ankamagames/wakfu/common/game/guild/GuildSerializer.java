package com.ankamagames.wakfu.common.game.guild;

import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.utils.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public final class GuildSerializer
{
    public static byte[] serializeGuild(final Guild guild) {
        final ByteArray bb = new ByteArray();
        bb.putLong(guild.getId());
        bb.putShort(guild.getLevel());
        bb.putLong(guild.getBlazon());
        bb.putInt(guild.getNationId());
        final byte[] nameUtf = StringUtils.toUTF8(guild.getName());
        bb.putInt(nameUtf.length);
        bb.put(nameUtf);
        final byte[] descUtf = StringUtils.toUTF8(guild.getDescription());
        bb.putInt(descUtf.length);
        bb.put(descUtf);
        final byte[] msgUtf = StringUtils.toUTF8(guild.getMessage());
        bb.putInt(msgUtf.length);
        bb.put(msgUtf);
        bb.putInt(guild.getCurrentGuildPoints());
        bb.putInt(guild.getTotalGuildPoints());
        bb.putInt(guild.getWeeklyPointsLimit());
        bb.putInt(guild.getEarnedPointsWeekly());
        bb.putInt(guild.getLastEarningPointWeek());
        bb.putInt(guild.rankSize());
        guild.forEachRank(new TObjectProcedure<GuildRank>() {
            @Override
            public boolean execute(final GuildRank object) {
                bb.put(GuildSerializer.serializeRank(object));
                return true;
            }
        });
        final ArrayList<byte[]> membersToSerialize = new ArrayList<byte[]>();
        guild.forEachMember(new TObjectProcedure<GuildMember>() {
            @Override
            public boolean execute(final GuildMember object) {
                if (membersToSerialize.size() <= 200 || object.getRank() == guild.getBestRank()) {
                    membersToSerialize.add(GuildSerializer.serializeMember(object));
                }
                return true;
            }
        });
        bb.putInt(membersToSerialize.size());
        for (int i = 0; i < membersToSerialize.size(); ++i) {
            bb.put(membersToSerialize.get(i));
        }
        bb.putInt(guild.bonusSize());
        guild.forEachBonus(new TObjectProcedure<GuildBonus>() {
            @Override
            public boolean execute(final GuildBonus bonus) {
                bb.put(GuildSerializer.serializeBonus(bonus));
                return true;
            }
        });
        return bb.toArray();
    }
    
    public static Guild unSerializeGuild(final ByteBuffer bb) {
        final Guild guild = new GuildModel();
        unSerializeGuild(bb, guild);
        return guild;
    }
    
    public static void unSerializeGuild(final ByteBuffer bb, final Guild guild) {
        final GuildModel model = (GuildModel)guild;
        model.setId(bb.getLong());
        model.setLevel(bb.getShort());
        model.setBlazon(bb.getLong());
        model.setNationId(bb.getInt());
        final byte[] nameUtf = new byte[bb.getInt()];
        bb.get(nameUtf);
        model.setName(StringUtils.fromUTF8(nameUtf));
        final byte[] descUtf = new byte[bb.getInt()];
        bb.get(descUtf);
        model.setDescription(StringUtils.fromUTF8(descUtf));
        final byte[] msgUtf = new byte[bb.getInt()];
        bb.get(msgUtf);
        model.setMessage(StringUtils.fromUTF8(msgUtf));
        model.setCurrentGuildPoints(bb.getInt());
        model.setTotalGuildPoints(bb.getInt());
        model.setWeeklyPointsLimit(bb.getInt());
        model.setEarnedPointsWeekly(bb.getInt());
        model.setLastEarningPointWeek(bb.getInt());
        for (int i = 0, size = bb.getInt(); i < size; ++i) {
            model.addRank(unSerializeRank(bb));
        }
        for (int i = 0, size = bb.getInt(); i < size; ++i) {
            model.addMember(unSerializeMember(bb));
        }
        for (int i = 0, size = bb.getInt(); i < size; ++i) {
            model.addBonus(unserializeBonus(bb));
        }
    }
    
    public static byte[] serializeRank(final GuildRank rank) {
        final byte[] rankNameUtf = StringUtils.toUTF8(rank.getName());
        final ByteBuffer bb = ByteBuffer.allocate(12 + rankNameUtf.length + 8 + 2);
        bb.putLong(rank.getId());
        bb.putInt(rankNameUtf.length);
        bb.put(rankNameUtf);
        bb.putLong(rank.getAuthorisations());
        bb.putShort(rank.getPosition());
        return bb.array();
    }
    
    public static GuildRank unSerializeRank(final ByteBuffer bb) {
        final GuildRankModel rank = new GuildRankModel(bb.getLong());
        final byte[] rankNameUtf = new byte[bb.getInt()];
        bb.get(rankNameUtf);
        rank.setName(StringUtils.fromUTF8(rankNameUtf));
        rank.setAuthorisations(bb.getLong());
        rank.setPosition(bb.getShort());
        return rank;
    }
    
    public static byte[] serializeBonus(final GuildBonus bonus) {
        final ByteBuffer bb = ByteBuffer.allocate(20);
        bb.putInt(bonus.getBonusId());
        bb.putLong(bonus.getActivationDate().toLong());
        bb.putLong(bonus.getBuyDate().toLong());
        return bb.array();
    }
    
    public static GuildBonus unserializeBonus(final ByteBuffer bb) {
        final int id = bb.getInt();
        final long activationDate = bb.getLong();
        final long buyDate = bb.getLong();
        return new GuildBonusBuilder().setBonusId(id).setBuyDate(GameDate.fromLong(buyDate)).setActivationDate(GameDate.fromLong(activationDate)).createGuildBonus();
    }
    
    public static byte[] serializeMember(final GuildMember member) {
        final byte[] utfName = StringUtils.toUTF8(member.getName());
        final ByteBuffer bb = ByteBuffer.allocate(41 + utfName.length);
        bb.putLong(member.getId());
        bb.putInt(member.getGuildPoints());
        bb.putLong(member.getRank());
        bb.putLong(member.getXp());
        bb.put((byte)(member.isConnected() ? 1 : 0));
        bb.put(member.getSmiley());
        bb.put(member.getSex());
        bb.putShort(member.getBreedId());
        bb.putInt(member.getNationId());
        bb.putInt(utfName.length);
        bb.put(utfName);
        return bb.array();
    }
    
    public static GuildMember unSerializeMember(final ByteBuffer bb) {
        final GuildMemberModel member = new GuildMemberModel(bb.getLong());
        member.setGuildPoints(bb.getInt());
        member.setRank(bb.getLong());
        member.setXp(bb.getLong());
        member.setConnected(bb.get() == 1);
        member.setSmiley(bb.get());
        member.setSex(bb.get());
        member.setBreedId(bb.getShort());
        member.setNationId(bb.getInt());
        final byte[] utfName = new byte[bb.getInt()];
        bb.get(utfName);
        member.setName(StringUtils.fromUTF8(utfName));
        return member;
    }
}
