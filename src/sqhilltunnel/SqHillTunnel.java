
package sqhilltunnel;


public class SqHillTunnel {


    public static void main(String[] args) {
        Road r = new Road();
        long runtime = 60*60;
        long t = 0;
        while(t < runtime)
        {
            r.itterate(t);
            t++;
        }
        System.out.println(r.getNumCars());
    }

}
