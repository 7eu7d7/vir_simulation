import java.awt.*;
import java.util.Vector;

public class Home {
    Vector<People> peos=new Vector<>();
    boolean ban;

    public Home(){
        int count=Constant.randint(Constant.home_count[0], Constant.home_count[1]);
        for(int i=0;i<count;i++)
            initPeople(new People(Constant.random(4f/24,12f/24)));
    }

    public void initPeople(People peo){
        peos.add(peo);
        peo.home=this;
        peo.home_now=this;
    }

    public boolean addPeople(People peo){
        peos.add(peo);
        peo.home_now=this;
        return true;
    }

    public void make_ill(){
        int count=get_ill_count();

        for(People p:peos){
            if(p.ill_time==-1){
                for(int i=0;i<count;i++){
                    if(Math.random()<Constant.ill_rate*((double) count/peos.size())*p.prot_rate){ //患病概率=基础患病率*(患病人数/总人数)*保护意识
                        p.getIll();
                        break;
                    }
                }
            }
        }
    }

    public int get_ill_count(){
        int count=0;
        for(People p:peos){
            if(p.ill_time>0)
                count++;
        }
        return count;
    }

    public int get_ill_count_check(){
        int count=0;
        for(People p:peos){
            if(p.ill_time>0 && (Constant.time-p.ill_time)>p.syp_time+5)
                count++;
        }
        return count;
    }

    public void check_recover_die(){
        for(int i=0;i<peos.size();i++){
            People p=peos.get(i);
            if(p.ill_time>0 && (Constant.time-p.ill_time)-p.syp_time>4*24){
                if(Math.random()<Constant.die_rate) {
                    p.die();
                    i--;
                }
                else if (Math.random()<Constant.recv_rate) {
                    i--;
                    p.recover();
                }
            }
        }
    }

    public void step(){
        make_ill();
        check_recover_die();
    }

    public void draw(Graphics2D g, Rect rect){
        g.drawRect((int)rect.x,(int)rect.y,(int)rect.w,(int)rect.h);
        Color rawcol=g.getColor();

        for (People p:peos){
            if(p.ill_time>p.syp_time)
                g.setColor(Color.red);
            else if(p.ill_time>0)
                g.setColor(Color.yellow);
            else
                g.setColor(Constant.tyblue);

            g.fillOval(Constant.randint(0,(int)rect.w-(int)(rect.w*0.2))+(int)rect.x,
                    Constant.randint(0,(int)rect.h-(int)(rect.h*0.2))+(int)rect.y,
                    (int)(rect.w*0.2),(int)(rect.w*0.2));
        }

        g.setColor(rawcol);
    }
}
