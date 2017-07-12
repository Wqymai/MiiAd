package com.mg.d.c;

import java.io.Serializable;


public class a implements Serializable,Cloneable {

//    private String id;
//
//    private String name;
//
//    private String title;
//
//    private String desc;
//
//    private String image;
//
//    private String pkName;
//
//    private String category;
//
//    private long size;
//
//    private int type;                   //广告点击后续操作类型    1、apk下载     2、web网页  4、H5
//    private String page;//H5代码（type=4,值有效）
//
//    private String icon;
//
//    private String url;
//
//    private b reportBean;
//
//    private String apkFilePath;
//
//
//    private int pt;                     //1: banner，2: 全屏， 3: 半屏， 4: 信息流，5：广告墙 ，6：push
//
//    private int et;                     //广告过期时间，单位 秒，超过过期时间请重新获取广告。为0则该广告不能做缓存。
//
//    private int displayTime;            //广告展示时间
//
//    private int delayTime;              //插屏广告延迟展示时间
//
//    private boolean hasJumpButton;      //是否有跳过按钮
//
//    private int jumpFunction;           //点击跳过按钮对应的功能
//
//    public int getFlag() {
//        return flag;
//    }
//
//    public void setFlag(int flag) {
//        this.flag = flag;
//    }
//
//    private int toggleSence;            //广告的触发场景
//
//    private int flag;       //点击广告是跳转还是关闭
//    private String bp;//banner广告的位置
//
//    private String downx;
//    private String downy;
//    private String upx;
//    private String upy;
//    private String clickid;
//
//    public String getClickid() {
//        return clickid;
//    }
//
//    public void setClickid(String clickid) {
//        this.clickid = clickid;
//    }
//
//    public String getBp() {
//        return bp;
//    }
//
//    public void setBp(String bp) {
//        this.bp = bp;
//    }
//
//    public int getToggleSence() {
//        return toggleSence;
//    }
//
//    public void setToggleSence(int toggleSence) {
//        this.toggleSence = toggleSence;
//    }
//
//    public int getDisplayTime() {
//        return displayTime;
//    }
//
//    public void setDisplayTime(int displayTime) {
//        this.displayTime = displayTime;
//    }
//
//    public int getDelayTime() {
//        return delayTime;
//    }
//
//    public void setDelayTime(int delayTime) {
//        this.delayTime = delayTime;
//    }
//
//    public boolean isHasJumpButton() {
//        return hasJumpButton;
//    }
//
//    public void setHasJumpButton(boolean hasJumpButton) {
//        this.hasJumpButton = hasJumpButton;
//    }
//
//    public int getJumpFunction() {
//        return jumpFunction;
//    }
//
//    public void setJumpFunction(int jumpFunction) {
//        this.jumpFunction = jumpFunction;
//    }
//
//    public int getPt() {
//        return pt;
//    }
//
//    public void setPt(int pt) {
//        this.pt = pt;
//    }
//
//    public int getEt() {
//        return et;
//    }
//
//    public void setEt(int et) {
//        this.et = et;
//    }
//
//    public String getApkFilePath() {
//        return apkFilePath;
//    }
//
//    public void setApkFilePath(String apkFilePath) {
//        this.apkFilePath = apkFilePath;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDesc() {
//        return desc;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public String getPkName() {
//        return pkName;
//    }
//
//    public void setPkName(String pkName) {
//        this.pkName = pkName;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public long getSize() {
//        return size;
//    }
//
//    public void setSize(long size) {
//        this.size = size;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public String getIcon() {
//        return icon;
//    }
//
//    public void setIcon(String icon) {
//        this.icon = icon;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public b getReportBean() {
//        return reportBean;
//    }
//
//    public void setReportBean(b reportBean) {
//        this.reportBean = reportBean;
//    }
//    public String getPage() {
//        return page;
//    }
//
//    public void setPage(String page) {
//        this.page = page;
//    }
//
//    public String getDownx() {
//        return downx;
//    }
//
//    public void setDownx(String downx) {
//        this.downx = downx;
//    }
//
//    public String getDowny() {
//        return downy;
//    }
//
//    public void setDowny(String downy) {
//        this.downy = downy;
//    }
//
//    public String getUpx() {
//        return upx;
//    }
//
//    public void setUpx(String upx) {
//        this.upx = upx;
//    }
//
//    public String getUpy() {
//        return upy;
//    }
//
//    public void setUpy(String upy) {
//        this.upy = upy;
//    }
//
//    @Override
//    public String toString() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("\n name="+name+"\n");
//        sb.append("title="+title+"\n");
//        sb.append("icon="+icon+"\n");
//        sb.append("img="+image+"\n");
//        sb.append("url="+url + "\n");
//        sb.append("pt="+pt+"\n");
//        sb.append("et="+et+"\n");
//        sb.append("displayTime="+displayTime+"\n");
//        sb.append("delayTime="+delayTime+"\n");
//        sb.append("hasJumpButton="+hasJumpButton+"\n");
//        sb.append("jumpFunction="+jumpFunction+"\n");
//        sb.append("type="+type+"\n");
//        sb.append("flag="+flag);
//
//        return sb.toString();
//    }
//
//    @Override
//    public Object clone() {
//        a adModel = null;
//        try{
//            adModel = (a)super.clone();
//        }catch(CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
//        return adModel;
//    }

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
    private int n;
    private int o;
    private String p;
    private String q;
    private String r;
    private String s;
    private String t;
    private String u;
    private String v;

    public final String a()
    {
        return this.v;
    }

    public final void a(String paramString)
    {
        this.v = paramString;
    }

    public final String b()
    {
        return this.u;
    }

    public final void b(String paramString)
    {
        this.u = paramString;
    }

    public final String c()
    {
        return this.t;
    }

    public final void c(String paramString)
    {
        this.t = paramString;
    }

    public final void a(int paramInt)
    {
        this.n = paramInt;
    }

    public final void b(int paramInt)
    {
        this.o = paramInt;
    }

    public final String d()
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

    public final String e()
    {
        return this.b;
    }

    public final void f(String paramString)
    {
        this.b = paramString;
    }

    public final String f()
    {
        return this.c;
    }

    public final void g(String paramString)
    {
        this.c = paramString;
    }

    public final String g()
    {
        return this.d;
    }

    public final void h(String paramString)
    {
        this.d = paramString;
    }

    public final String h()
    {
        return this.e;
    }

    public final void i(String paramString)
    {
        this.e = paramString;
    }

    public final String i()
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

    public final int j()
    {
        return this.h;
    }

    public final void c(int paramInt)
    {
        this.h = paramInt;
    }

    public final String k()
    {
        return this.j;
    }

    public final void l(String paramString)
    {
        this.j = paramString;
    }

    public final String l()
    {
        return this.k;
    }

    public final void m(String paramString)
    {
        this.k = paramString;
    }

    public final b m()
    {
        return this.l;
    }

    public final void a(b paramb)
    {
        this.l = paramb;
    }

    public final String n()
    {
        return this.i;
    }

    public final void n(String paramString)
    {
        this.i = paramString;
    }

    public final String o()
    {
        return this.p;
    }

    public final void o(String paramString)
    {
        this.p = paramString;
    }

    public final String p()
    {
        return this.q;
    }

    public final void p(String paramString)
    {
        this.q = paramString;
    }

    public final String q()
    {
        return this.r;
    }

    public final void q(String paramString)
    {
        this.r = paramString;
    }

    public final String r()
    {
        return this.s;
    }

    public final void r(String paramString)
    {
        this.s = paramString;
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
        localStringBuffer.append("pt=" + this.n + "\n");
        localStringBuffer.append("et=" + this.o + "\n");
        localStringBuffer.append("displayTime=" + 0 + "\n");
        localStringBuffer.append("delayTime=" + 0);
        localStringBuffer.append("hasJumpButton=" + false);
        localStringBuffer.append("jumpFunction=" + 0);
        localStringBuffer.append("toggleSence=" + 0);
        localStringBuffer.append("flag=" + 0);
        localStringBuffer.append("bp=" + null);
        localStringBuffer.append("downx=" + this.p);
        localStringBuffer.append("downy=" + this.q);
        localStringBuffer.append("upx=" + this.r);
        localStringBuffer.append("upy=" + this.s);
        localStringBuffer.append("clickid=" + this.t);
        localStringBuffer.append("deeplink=" + this.u);
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
