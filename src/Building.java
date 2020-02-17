import java.awt.*;
import java.util.Vector;

public class Building {
    Home[] homes;
    boolean ban; //封锁

    public Building(int floors){
        homes=new Home[floors];
        for(int i=0;i<floors;i++){
            homes[i]=new Home();
            Main.allhome.add(homes[i]);
        }
    }

    public int get_ill_count(){
        int c=0;
        for(Home h:homes){
            c+=h.get_ill_count_check();
        }
        return c;
    }

    public void check_ban(){
        int c=get_ill_count();
        if (Constant.ill_count>500 && Math.random()<c/3d){
            ban=true;
            for(Home h:homes){
                h.ban=true;
            }
        }
    }

    public void step(){
        check_ban();
    }

    public void draw(Graphics2D g, Rect rect){
        Color rawcol=g.getColor();
        if(ban)
            g.setColor(Color.red);
        else
            g.setColor(Constant.tyblue);

        g.drawRect((int)rect.x,(int)rect.y,(int)rect.w,(int)rect.h);
        double dw=rect.w/Math.ceil(Math.sqrt(35))-10,dh=rect.h/Math.ceil(Math.sqrt(35))-10;
        int len=(int)Math.ceil(Math.sqrt(homes.length));
        double offset=(rect.w/len-dw)/2;
        for(int i=0;i<homes.length;i++){
            homes[i].draw(g, new Rect((offset*2+dw)*(i%len)+offset/2+rect.x,(offset*2+dh)*(i/len)+offset/2+rect.y,dw,dh));
        }

        g.setColor(rawcol);
    }
}
