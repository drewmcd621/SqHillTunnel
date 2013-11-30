
package sqhilltunnel;


public class SqHillTunnel {


    public static void main(String[] args) {
        Road r = new Road();
        long runtime = 60*60;
        long t = 0;
        RoadBMP rrli = new RoadBMP((int)(runtime/5),15,r.getLength(),0);
        while(t < runtime || r.getCarsOnLanes() > 0)
        {
            r.itterate(t, runtime);
            if(t%5 == 0)
            {
                rrli.addRow(r);
            }
            t++;
        }
        rrli.save();
        System.out.println(r.getNumCars());
        System.out.println(r.getAvgSpeed());
        System.out.println(r.getLaneChanges());
    }

}
