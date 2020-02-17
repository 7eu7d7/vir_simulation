import java.awt.*;
import java.util.Vector;

public class SuperMarket extends Home{
    int clean_rate;

    @Override
    public void make_ill() {
        //System.out.println(get_ill_count()+"/"+peos.size());
        super.make_ill();
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

            g.fillOval(Constant.randint(0,(int)rect.w-(int)(rect.w*0.05))+(int)rect.x,
                    Constant.randint(0,(int)rect.h-(int)(rect.h*0.05))+(int)rect.y,
                    (int)(rect.w*0.05),(int)(rect.w*0.05));
        }

        g.setColor(rawcol);
    }
}
