package com.ankamagames.wakfu.common.account.admin;

import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.google.common.base.*;
import io.netty.buffer.*;
import java.util.*;
import com.google.common.primitives.*;

public class AdminUtils
{
    public static final Comparator<Admin> ADMIN_COMPARATOR;
    public static final Comparator<Right> RIGHT_COMPARATOR;
    
    public static AdminRightsGroup getAdminGroup(final Optional<Admin> admin, final int serverId) {
        final Optional<Right> right = (Optional<Right>)(admin.isPresent() ? ((Admin)admin.get()).getRight(serverId) : Optional.absent());
        return right.isPresent() ? ((Right)right.get()).getRight() : AdminRightsGroup.NONE;
    }
    
    public static byte[] serialize(final Admin admin) {
        final ByteBuf buf = Unpooled.buffer();
        buf.writeLong(admin.getAccountId());
        final String name = admin.getAdminName();
        final byte[] bName = name.getBytes(Charsets.UTF_8);
        buf.writeInt(bName.length);
        buf.writeBytes(bName);
        final List<Right> rights = admin.getRights();
        buf.writeInt(rights.size());
        for (final Right right : rights) {
            buf.writeInt(right.getServerId());
            buf.writeInt(right.getRight().getId());
        }
        return buf.array();
    }
    
    public static Admin unSerialize(final byte[] data) {
        final ByteBuf buf = Unpooled.wrappedBuffer(data);
        final long accountId = buf.readLong();
        final byte[] bName = new byte[buf.readInt()];
        buf.readBytes(bName);
        final String name = new String(bName, Charsets.UTF_8);
        final Admin admin = new Admin(accountId, name);
        for (int i = 0, size = buf.readInt(); i < size; ++i) {
            final Right right = new Right(buf.readInt(), AdminRightsGroup.getRightsGroupFromId(buf.readInt()));
            admin.addRights(right);
        }
        return admin;
    }
    
    static {
        ADMIN_COMPARATOR = new AdminComparator();
        RIGHT_COMPARATOR = new RightsComparator();
    }
    
    private static class AdminComparator implements Comparator<Admin>
    {
        @Override
        public int compare(final Admin o1, final Admin t1) {
            return Longs.compare(o1.getAccountId(), t1.getAccountId());
        }
    }
    
    private static class RightsComparator implements Comparator<Right>
    {
        @Override
        public int compare(final Right o1, final Right t1) {
            return Ints.compare(o1.getServerId(), t1.getServerId());
        }
    }
}
