package com.jcraft.jogg;

public class Packet
{
    public byte[] packet_base;
    public int packet;
    public int bytes;
    public int b_o_s;
    public int e_o_s;
    public long granulepos;
    public long packetno;
    
    void clear() {
        this.packet = 0;
        this.bytes = 0;
        this.b_o_s = 0;
        this.e_o_s = 0;
        this.packet_base = null;
        this.granulepos = 0L;
        this.packetno = 0L;
    }
    
    public void reinit() {
        this.clear();
    }
}
