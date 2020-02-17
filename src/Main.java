import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

public class Main {
    public static Vector<Building> allbui=new Vector<>();
    public static Vector<Home> allhome=new Vector<>();
    public static Vector<People> allpeo=new Vector<>();
    public static Vector<SuperMarket> allsup=new Vector<>();
    public static Vector<Hospital> allhos=new Vector<>();

    public static int ill_init=10;
    public static int ill_up=100;

    public static int record_len=24*120;

    public static void main(String[] args){

        int[] px=new int[record_len];
        int[] py=new int[record_len];

        init();
        int psize=allpeo.size();
        System.out.println("总人数： "+psize);
        for(Constant.time=0;Constant.time<record_len;Constant.time++){
            step();
            System.out.println(Constant.time);
            System.out.println("患病人数： "+Constant.ill_count);
            System.out.println("死亡人数： "+Constant.die_count);
            System.out.println("治愈人数： "+Constant.recv_count);
            System.out.println();
            px[Constant.time]=(int)(100+((double)Constant.time/record_len)*(4096-200));
            py[Constant.time]=(int)(4000-((double)Constant.ill_count/psize)*(4096-200));
            //drawFrame(Constant.time); //绘制当前帧
        }


        int width = 4096, height = 4096;
        //创建图片对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        //基于图片对象打开绘图
        Graphics2D graphics = image.createGraphics();
        //绘图逻辑 START （基于业务逻辑进行绘图处理）……

        graphics.setColor(Color.white);
        graphics.fillRect(0,0,4096,4096);

        graphics.setColor(Color.black);
        graphics.setStroke(new BasicStroke(5f));
        for(int i=0;i<record_len-1;i++){
            graphics.drawLine(px[i],py[i],px[i+1],py[i+1]);
        }

        /*graphics.setColor(Color.red);
        for(int i=0;i<record_len-2;i++){
            graphics.drawLine(px[i],py[i]-py[i+1],px[i+1],py[i+2]-py[i+1]);
        }*/

        // 绘图逻辑 END
        //处理绘图
        graphics.dispose();
        //将绘制好的图片写入到图片
        try {
            ImageIO.write(image, "png", new File("pred.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void init(){
        for(int[] arr:Constant.building_count){
            for(int i=0;i<arr[0];i++)
                allbui.add(new Building(Constant.randint(arr[1],arr[2])));
        }

        for(int i=0;i<Constant.hos_count[0];i++)
            allhos.add(new Hospital(Constant.randint(Constant.hos_count[1], Constant.hos_count[2])));

        for(int i=0;i<Constant.sup_count;i++)
            allsup.add(new SuperMarket());

        for(int i=0;i<ill_init;i++){
           allpeo.get((int)(Math.random()*allpeo.size())).ill_time=1;
        }

        Collections.shuffle(allbui);
    }

    public static void step(){
        for(People p: allpeo){
            if(p.die || p.inhos || p.home_now.ban)
                continue;

            if(Math.random()<p.move_rate){
                p.moveTo(allhome.get((int)(Math.random()*allhome.size())));
            }else {
                p.goBack();
            }

            //System.out.println(Math.max(Constant.base_shop_rate*p.move_rate,Constant.low_shop_rate));
            if(Math.random()<Math.max(Constant.base_shop_rate*p.move_rate,Constant.low_shop_rate)){
                p.moveTo(allsup.get((int)(Math.random()*allsup.size())));
            }else {
                p.goBack();
            }

            if(p.ill_time>0 && Constant.time-p.ill_time>p.syp_time+5 && Math.random()<Constant.tohosp_rate){
                //System.out.println("fuck");
                p.moveTo(allhos.get((int)(Math.random()*allhos.size())));
            }

        }

        if(Constant.ill_count>ill_up){
            for(People p: allpeo) {
                p.rate_down();
                p.prot_up();
            }
            ill_up*=1.4;
        }

        for(Building b:allbui)
            b.step();

        for(SuperMarket b:allsup)
            b.step();

        for(Home b:allhome)
            b.step();

        for(Home b:allhos)
            b.step();
    }

    public static void drawFrame(int count){
        int width = 4096*4, height = 4096*4;
        int imgw=4096+500, imgh=4096+500;
        //创建图片对象
        BufferedImage image = new BufferedImage(imgw, imgh, BufferedImage.TYPE_4BYTE_ABGR);
        //基于图片对象打开绘图
        Graphics2D graphics = image.createGraphics();
        graphics.setStroke(new BasicStroke(5f));
        //绘图逻辑 START （基于业务逻辑进行绘图处理）……

        graphics.setColor(Color.black);
        graphics.fillRect(0,0,width,height);

        Rect rect=new Rect(width*0.01,height*0.01,width*0.98,height*0.98);
        int len=(int)Math.ceil(Math.sqrt(allbui.size()));
        double dw=rect.w/len-50,dh=rect.h/len-50;
        System.out.println(dh);


        double len_sup=Math.ceil((double) allsup.size()/len),
               len_hos=Math.ceil((double) allhos.size()/len);
        double sclen=(width+ len_sup*(dw+50) + len_hos*(dw+50)+1000 -imgw);

        double offset=count*500;
        offset=((int)(Math.floor(offset/(sclen*height/500)))%2==1)?(sclen*height/500)-offset%(int)(sclen*height/500):offset%(int)(sclen*height/500);
        System.out.println(offset);
        double offx=(Math.floor(offset/sclen)%2==0?offset%sclen:sclen-offset%sclen);
        double offy=Math.floor(offset/sclen)*500;
        rect.x-=offx;
        rect.y-=offy;

        //绘图
        graphics.setColor(Constant.tyblue);
        for(int i=0;i<allbui.size();i++){
            double px=(50+dw)*(i%len)+25+rect.x;
            double py=(50+dh)*(i/len)+25+rect.y;
            if(px>imgw||py>imgh||px+dw<0||py+dh<0)
                continue;
            allbui.get(i).draw(graphics, new Rect(px,py,dw,dh));
        }

        graphics.setColor(Color.GREEN);
        for(int i=0;i<allsup.size();i++){
            double px=(50+dw)*(i/len)+25+rect.x+width;
            double py=(50+dh)*(i%len)+25+rect.y;
            if(px>imgw||py>imgh||px+dw<0||py+dh<0)
                continue;
            allsup.get(i).draw(graphics, new Rect(px,py,dw,dh));
        }

        graphics.setColor(Color.orange);
        for(int i=0;i<allhos.size();i++){
            double px=(50+dw)*(i/len)+25+rect.x+width+len_sup*(dw+50);
            double py=(50+dh)*(i%len)+25+rect.y;
            if(px>imgw||py>imgh||px+dw<0||py+dh<0)
                continue;
            allhos.get(i).draw(graphics, new Rect(px,py,dw,dh));
        }

        // 绘图逻辑 END
        //处理绘图
        graphics.dispose();
        //将绘制好的图片写入到图片
        try {
            ImageIO.write(image, "png", new File("frames/frame"+count+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
