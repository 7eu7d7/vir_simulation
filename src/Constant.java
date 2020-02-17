import java.awt.*;

public class Constant {
    public static double ill_rate=0.1;
    public static int time;
    public static int[] spy_times={3*24, 10*24};
    public static double die_rate=0.15;
    public static double recv_rate=0.18;

    public static int[][] building_count={{3000,25,35},{7000,5,7}};
    public static int[] home_count={1,6};
    public static int[] hos_count={20,100,500};
    public static int sup_count=100;

    public static double base_shop_rate=4f/24;
    public static double low_shop_rate=1f/72;
    public static double down_rate=0.5;
    public static double up_rate=0.9;
    public static double move_down_rate=0.6;
    public static double prot_up_rate=0.5;
    public static double tohosp_rate=0.2;


    public static int die_count;
    public static int ill_count;
    public static int recv_count;

    public static Color tyblue=new Color(0x66, 0xcc, 0xff);

    public static int randint(int low,int high){
        return (int)(low+Math.random()*(high-low+1));
    }

    public static double random(double low,double high){
        return (low+Math.random()*(high-low));
    }
}
