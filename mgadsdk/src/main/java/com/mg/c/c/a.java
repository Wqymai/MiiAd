package com.mg.c.c;

import java.io.Serializable;


public class a implements Serializable,Cloneable {



    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;
    private int h;
    private String i;
    private String j;
    private String k;
    private b l;
    private String m;
    private String n;
    private String o;
    private String p;
    private String q;
    private String r;
    private String s;
    private String t;
    private int u;

    public final int a()
    {
        return this.u;
    }

    public final void a(int paramInt)
    {
        this.u = paramInt;
    }

    public final String b()
    {
        return this.t;
    }

    public final void a(String paramString)
    {
        this.t = paramString;
    }

    public final String c()
    {
        return this.s;
    }

    public final void b(String paramString)
    {
        this.s = paramString;
    }

    public final String d()
    {
        return this.r;
    }

    public final void c(String paramString)
    {
        this.r = paramString;
    }

    public final String e()
    {
        return this.m;
    }

    public final void d(String paramString)
    {
        this.m = paramString;
    }

    public final void e(String paramString)
    {
        this.a = paramString;
    }

    public final String f()
    {
        return this.b;
    }

    public final void f(String paramString)
    {
        this.b = paramString;
    }

    public final String g()
    {
        return this.c;
    }

    public final void g(String paramString)
    {
        this.c = paramString;
    }

    public final String h()
    {
        return this.d;
    }

    public final void h(String paramString)
    {
        this.d = paramString;
    }

    public final String i()
    {
        return this.e;
    }

    public final void i(String paramString)
    {
        this.e = paramString;
    }

    public final String j()
    {
        return this.f;
    }

    public final void j(String paramString)
    {
        this.f = paramString;
    }

    public final void k(String paramString)
    {
        this.g = paramString;
    }

    public final int k()
    {
        return this.h;
    }

    public final void b(int paramInt)
    {
        this.h = paramInt;
    }

    public final String l()
    {
        return this.j;
    }

    public final void l(String paramString)
    {
        this.j = paramString;
    }

    public final String m()
    {
        return this.k;
    }

    public final void m(String paramString)
    {
        this.k = paramString;
    }

    public final b n()
    {
        return this.l;
    }

    public final void a(b paramb)
    {
        this.l = paramb;
    }

    public final String o()
    {
        return this.i;
    }

    public final void n(String paramString)
    {
        this.i = paramString;
    }

    public final String p()
    {
        return this.n;
    }

    public final void o(String paramString)
    {
        this.n = paramString;
    }

    public final String q()
    {
        return this.o;
    }

    public final void p(String paramString)
    {
        this.o = paramString;
    }

    public final String r()
    {
        return this.p;
    }

    public final void q(String paramString)
    {
        this.p = paramString;
    }

    public final String s()
    {
        return this.q;
    }

    public final void r(String paramString)
    {
        this.q = paramString;
    }

    public final String toString()
    {
        StringBuffer localStringBuffer;
        (localStringBuffer = new StringBuffer()).append("id=" + this.a + "\n");
        localStringBuffer.append("name=" + this.b + "\n");
        localStringBuffer.append("title=" + this.c + "\n");
        localStringBuffer.append("desc=" + this.d + "\n");
        localStringBuffer.append("image=" + this.e + "\n");
        localStringBuffer.append("pkName=" + this.f + "\n");
        localStringBuffer.append("category=" + this.g + "\n");
        localStringBuffer.append("size=" + 0L + "\n");
        localStringBuffer.append("type=" + this.h + "\n");
        localStringBuffer.append("page=" + this.i + "\n");
        localStringBuffer.append("icon=" + this.j + "\n");
        localStringBuffer.append("url=" + this.k + "\n");
        localStringBuffer.append("reportBean=" + this.l + "\n");
        localStringBuffer.append("apkFilePath=" + this.m + "\n");
        localStringBuffer.append("pt=" + 0 + "\n");
        localStringBuffer.append("et=" + 0 + "\n");
        localStringBuffer.append("displayTime=" + 0 + "\n");
        localStringBuffer.append("delayTime=" + 0);
        localStringBuffer.append("hasJumpButton=" + false);
        localStringBuffer.append("jumpFunction=" + 0);
        localStringBuffer.append("toggleSence=" + 0);
        localStringBuffer.append("flag=" + 0);
        localStringBuffer.append("bp=" + null);
        localStringBuffer.append("downx=" + this.n);
        localStringBuffer.append("downy=" + this.o);
        localStringBuffer.append("upx=" + this.p);
        localStringBuffer.append("upy=" + this.q);
        localStringBuffer.append("clickid=" + this.r);
        localStringBuffer.append("deeplink=" + this.s);
        return localStringBuffer.toString();
    }

    public final Object clone()
    {
        a locala = null;
        try
        {
            locala = (a)super.clone();
        }
        catch (CloneNotSupportedException localCloneNotSupportedException2)
        {
            CloneNotSupportedException localCloneNotSupportedException1;
            (localCloneNotSupportedException1 = localCloneNotSupportedException2).printStackTrace();
        }
        return locala;
    }

}
