
package sqhilltunnel;

import java.util.Date;


public class SqHillTunnel {

static int length = 4225;

    public static void main(String[] args) {
        
        boolean tunnel = false;
        
        Road r = new Road(length*2);
        Stats s = new Stats(length*2);
        r.setStats(s);
        long runTime = 6*60*60;
        long setupTime = 60*60;
        long totalTime = setupTime + runTime;
        int PNGintv = 10;
        long t = 0;
        if(tunnel)
        {
            r.setLaneChangeRestrictedArea(length,length*2);
        }
        while(t < setupTime)
        {
            r.itterate(t, totalTime);
            t++;
        }
        s.Reset();
        RoadBMP rrli = new RoadBMP((int)(runTime/PNGintv),15,r.getLength());
        while(t < totalTime)
        {
            r.itterate(t, totalTime);
            if(t%PNGintv == 0)
            {
                rrli.addRow(r);
            }
            if(t%60 == 0)
            {
                long tr = t - setupTime;
                int h = (int)tr/(60*60);
                int m = (int)tr/60 - h*60;
                
                System.out.println(h + " hours " + m + " mins");
            }
            t++;
        }
        
        System.out.println("N cars: " + s.getNCars());
        System.out.println("Cars entered: " + r.getNumCars());
        System.out.println("Mean speed: " + s.getMeanSpeed());
        System.out.println("Median speed: " + s.getMedianSpeed());
        System.out.println("Mean Time: " + s.getMeanTime());
        System.out.println("Min Speed: " + s.getMinSpeed());
        System.out.println("Max Speed: " + s.getMaxSpeed());
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
