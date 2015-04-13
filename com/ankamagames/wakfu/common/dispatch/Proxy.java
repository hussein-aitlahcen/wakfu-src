package com.ankamagames.wakfu.common.dispatch;

import com.google.gson.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;
import java.util.*;

public class Proxy
{
    @SerializedName("id")
    private int m_id;
    @SerializedName("name")
    private String m_name;
    @SerializedName("community")
    private Community m_community;
    @SerializedName("address")
    private String m_address;
    @SerializedName("ports")
    private int[] m_ports;
    private byte m_order;
    
    public int getId() {
        return this.m_id;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public Community getCommunity() {
        return this.m_community;
    }
    
    public String getAddress() {
        return this.m_address;
    }
    
    public int[] getPorts() {
        return this.m_ports.clone();
    }
    
    public int getOrder() {
        return this.m_order;
    }
    
    public void setOrder(final byte order) {
        this.m_order = order;
    }
    
    public byte[] build() {
        final ByteArray bb = new ByteArray();
        bb.putInt(this.m_id);
        final byte[] utfName = StringUtils.toUTF8(this.m_name);
        bb.putInt(utfName.length);
        bb.put(utfName);
        bb.putInt(this.m_community.getId());
        final byte[] utfAddress = StringUtils.toUTF8(this.m_address);
        bb.putInt(utfAddress.length);
        bb.put(utfAddress);
        bb.putInt(this.m_ports.length);
        for (int i = 0, length = this.m_ports.length; i < length; ++i) {
            final int port = this.m_ports[i];
            bb.putInt(port);
        }
        bb.put(this.m_order);
        return bb.toArray();
    }
    
    public static Proxy fromBuild(final ByteBuffer bb) {
        final int id = bb.getInt();
        final byte[] utfName = new byte[bb.getInt()];
        bb.get(utfName);
        final String name = StringUtils.fromUTF8(utfName);
        final Community community = Community.getFromId(bb.getInt());
        final byte[] utfAddress = new byte[bb.getInt()];
        bb.get(utfAddress);
        final String address = StringUtils.fromUTF8(utfAddress);
        final int[] ports = new int[bb.getInt()];
        for (int i = 0, length = ports.length; i < length; ++i) {
            ports[i] = bb.getInt();
        }
        final byte order = bb.get();
        final Proxy proxy = new Proxy();
        proxy.m_id = id;
        proxy.m_name = name;
        proxy.m_community = community;
        proxy.m_address = address;
        proxy.m_ports = ports;
        proxy.m_order = order;
        return proxy;
    }
    
    @Override
    public String toString() {
        return "Proxy{m_id=" + this.m_id + ", m_name='" + this.m_name + '\'' + ", m_community=" + this.m_community + ", m_address='" + this.m_address + '\'' + ", m_ports=" + Arrays.toString(this.m_ports) + ", m_order=" + this.m_order + '}';
    }
}
