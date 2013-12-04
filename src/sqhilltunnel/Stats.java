
package sqhilltunnel;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * Drew McDermott
 * adm75@pitt.edu
 */
public class Stats 
{
    
    public Stats(int distance)
    {
        dist = distance;
        carTime = new ArrayList<>();
    }
    
    private int dist;
    private ArrayList<Long> carTime;
    
    
    public void addCar(Car c)
    {
         carTime.add(c.getTotalTime());
    }
    public double getMeanTime()
    {
        double mean = 0;
        long total = 0;
        int num = 0;
        
        
        for(Long l : carTime)
        {
            total += l.longValue();
            num++;
        }
        
        mean = (double)total / (double)num; //Avg time per car (s)
        
        return mean;
    }
    
    public double getMeanSpeed()
    {
        double mean = getMeanTime();
        
        mean =  (double)dist / mean; //Avg speed (ft/s)
        
        return mean;
        
    }
    public double getMinSpeed()
    {
        Collections.sort(carTime);
        
        long min = carTime.get(carTime.size() - 1).longValue();
        
        double minS = (double)dist / (double)min;
        
        return minS;
    }
    public double getMaxSpeed()
    {
       Collections.sort(carTime);
        
        long max = carTime.get(0).longValue();
        
        double maxS = (double)dist / (double)max;
        
        return maxS;
    }
    public double getMedianSpeed()
    {
       Collections.sort(carTime);
        
        long med = carTime.get(carTime.size() / 2 - 1).longValue();
        
        double medS = (double)dist / (double)med;
        
        return medS;
    }
    public int getNCars()
    {
        return carTime.size();
    }

}
