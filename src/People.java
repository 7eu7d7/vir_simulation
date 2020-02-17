public class People {
    double move_down_rate; //移动意向降低概率
    double move_rate; //移动意向
    double prot_rate=1; //保护意识
    int ill_time=-1; //得病时间
    int syp_time=-1; //潜伏期
    Home home;
    Home home_now;

    boolean inhos;
    boolean die;

    public People(double mr){
        move_rate=mr;
        syp_time=Constant.randint(Constant.spy_times[0], Constant.spy_times[1]);
        Main.allpeo.add(this);
    }

    public void getIll(){
        ill_time=Constant.time;
        Constant.ill_count++;
    }

    public void rate_down(){
        if(Math.random()<Constant.move_down_rate){
            move_rate*=Constant.down_rate;
        }
    }

    public void prot_up(){
        if(Math.random()<Constant.prot_up_rate){
            prot_rate*=Constant.up_rate;
        }
    }

    public void die(){
        home.peos.remove(this);
        home_now.peos.remove(this);
        die=true;
        Constant.die_count++;
    }

    public void recover(){
        ill_time=-1;
        inhos=false;
        goBack();
        Constant.recv_count++;
    }

    public void moveTo(Home ho){
        if(ho.ban || home_now.ban) {
            //System.out.println("fuck");
            return;
        }
        Home home_last=home_now;
        if(ho.addPeople(this))
            home_last.peos.remove(this);
    }

    public void goBack(){
        if(home_now.ban)
            return;
        home_now.peos.remove(this);
        home.addPeople(this);
    }

}
