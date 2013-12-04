
package sqhilltunnel;

import java.util.Date;


public class SqHillTunnel {

static int length = 4225;

    public static void main(String[] args) {
        
        boolean tunnel = true;
        
        Road r = new Road(length*2);
        Stats s = new Stats(length*2);
        r.setStats(s);
        long runtime = 3*60*60;
        long t = 0;
        if(tunnel)
        {
            r.setLaneChangeRestrictedArea(length,length*2);
        }
        RoadBMP rrli = new RoadBMP((int)(runtime*1.5/10),15,r.getLength());
        while(t < runtime || r.getCarsOnLanes() > 0)
        {
            r.itterate(t, runtime);
            if(t%10 == 0)
            {
                rrli.addRow(r);
            }
            if(t%60 == 0)
            {
                int h = (int)t/(60*60);
                int m = (int)t/60 - h*60;
                
                System.out.println(h + " hours " + m + " mins");
            }
            t++;
        }
        
        System.out.println("N cars: " + s.getNCars());
        System.out.println("Mean speed: " + s.getMeanSpeed());
        System.out.println("Median speed: " + s.getMedianSpeed());
        System.out.println("Mean Time: " + s.getMeanTime());
        System.out.println("N lane changes: " + r.getLaneChanges());
        if(tunnel)
        {
            rrli.save("tunnel");
        }
        else
        {
            rrli.save("control");
        }
    }

}
