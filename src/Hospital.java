import java.awt.*;
import java.util.Vector;

public class Hospital extends Home{
    int size;

    public Hospital(int size){
        this.size=size;
    }

    @Override
    public void check_recover_die(){
        //System.out.println(peos.size()+"/"+size);
        for(int i=0;i<peos.size();i++){
            People p=peos.get(i);
            if(p.ill_time>0 && (Constant.time-p.ill_time)-p.syp_time>4*24){
                if(Math.random()<Constant.die_rate*0.6) {
                    p.die();
                    //System.out.println("die");
                    i--;
                }
                else if (Math.random()<Constant.recv_rate*1.2) {
                    i--;
                    p.recover();
                }
            }
        }
    }

    @Override
    public boolean addPeople(People peo) {
        if(peos.size()>=size)
            return false;
        peo.inhos=true;
        return super.addPeople(peo);
    }

    @Override
    public void step() {
        check_recover_die();
    }

    public void draw(Graphics2D g, Rect rect){
        g.drawRect((int)rect.x,(int)rect.y,(int)rect.w,(int)rect.h);
        Color rawcol=g.getColor();

        int idx=0;
        int len=(int)Math.ceil(Math.sqrt(peos.size()));
        double dw=(rect.w*0.9)/len;
        double dh=(rect.h*0.9)/len;

        for (People p:peos){
            if(p.ill_time>p.syp_time)
                g.setColor(Color.red);
            else if(p.ill_time>0)
                g.setColor(Color.yellow);
            else
                g.setColor(Constant.tyblue);

            g.fillOval((int)(rect.w*0.05)+ (int)((idx%len)*dw) +(int)rect.x,
                    (int)(rect.h*0.05)+ (int)((idx/len)*dh)+(int)rect.y,
                    (int)(rect.w*0.05),(int)(rect.w*0.05));
            idx++;
        }

        g.setColor(rawcol);
    }
}
