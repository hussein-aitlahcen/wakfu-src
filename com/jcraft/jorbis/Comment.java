package com.jcraft.jorbis;

import com.jcraft.jogg.*;

public class Comment
{
    private byte[][] user_comments;
    private byte[] vendor;
    
    public void init() {
        this.user_comments = null;
        this.vendor = null;
    }
    
    private static boolean tagcompare(final byte[] s1, final byte[] s2, final int n) {
        for (int c = 0; c < n; ++c) {
            byte u1 = s1[c];
            byte u2 = s2[c];
            if (90 >= u1 && u1 >= 65) {
                u1 = (byte)(u1 - 65 + 97);
            }
            if (90 >= u2 && u2 >= 65) {
                u2 = (byte)(u2 - 65 + 97);
            }
            if (u1 != u2) {
                return false;
            }
        }
        return true;
    }
    
    public String query(final String tag) {
        return this.query(tag, 0);
    }
    
    String query(final String tag, final int count) {
        final int foo = this.query(tag.getBytes(), count);
        if (foo == -1) {
            return null;
        }
        final byte[] comment = this.user_comments[foo];
        for (int i = 0; i < comment.length; ++i) {
            if (comment[i] == 61) {
                return new String(comment, i + 1, comment.length - (i + 1));
            }
        }
        return null;
    }
    
    private int query(final byte[] tag, final int count) {
        if (this.user_comments == null) {
            return -1;
        }
        int i = 0;
        int found = 0;
        final int fulltaglen = tag.length + 1;
        final byte[] fulltag = new byte[fulltaglen];
        System.arraycopy(tag, 0, fulltag, 0, tag.length);
        fulltag[tag.length] = 61;
        for (i = 0; i < this.user_comments.length; ++i) {
            if (tagcompare(this.user_comments[i], fulltag, fulltaglen)) {
                if (count == found) {
                    return i;
                }
                ++found;
            }
        }
        return -1;
    }
    
    int unpack(final Buffer opb) {
        final int vendorlen = opb.read(32);
        if (vendorlen < 0) {
            this.clear();
            return -1;
        }
        opb.read(this.vendor = new byte[vendorlen], vendorlen);
        final int comments = opb.read(32);
        if (comments < 0) {
            this.clear();
            return -1;
        }
        if (comments == 0) {
            this.user_comments = null;
        }
        else {
            this.user_comments = new byte[comments][];
            for (int i = 0; i < comments; ++i) {
                final int len = opb.read(32);
                if (len < 0) {
                    this.clear();
                    return -1;
                }
                opb.read(this.user_comments[i] = new byte[len], len);
            }
        }
        if (opb.read(1) != 1) {
            this.clear();
            return -1;
        }
        return 0;
    }
    
    void clear() {
        this.user_comments = null;
        this.vendor = null;
    }
    
    public boolean hasVendor() {
        return this.vendor != null;
    }
    
    @Override
    public String toString() {
        String foo = "Vendor: " + new String(this.vendor, 0, this.vendor.length);
        for (int i = 0; i < this.user_comments.length; ++i) {
            foo = foo + "\nComment: " + new String(this.user_comments[i], 0, this.user_comments[i].length);
        }
        foo += '\n';
        return foo;
    }
}
