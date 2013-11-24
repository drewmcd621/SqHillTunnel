
package sqhilltunnel;

import java.util.ArrayList;
import java.util.Random;


public class Car  implements Comparable<Car>
{
    //Constants
    private int acceleration = 6; //ft/s^2
    private double pSlowdown = 0; 
    private int carlength = 15; //ft
    //Variables
    private int distance; //ft
    private int newDist;
    private int maxSpeed; //ft/s
    private int curSpeed; //ft/s
    //Time
    private long startTime; //s
    private long endTime;  //s
    //Other
    private Road parent;
    int lane;
    private Random r;
    
    
    public Car(int maximumSpeed, Road parentRoad, int roadLane, long Now)
    {
        distance = carlength; //So that the whole car is on the sim road
        maxSpeed = maximumSpeed; 
        curSpeed = maximumSpeed; //Enter sim at max speed
        startTime = Now;
        lane = roadLane;
        parent = parentRoad;
        r = new Random();
    }
    
    public void Accelerate()
    {
        curSpeed += acceleration;
        
        if(curSpeed > maxSpeed) curSpeed = maxSpeed;
        
    }
    public void Slowdown()
    {
        int thislane = getCarPos(lane, false);
        int otherlane = getCarPos(1-lane,true);
        
    }
    private int getCarPos(int lanepos, boolean fuzzy)
    {
        ArrayList<Car> l = parent.getLane(lanepos);
        
        for(int i = l.size()-1; i >= 0; i --)
        {
            if(l.get(i).equals(this))
            {
                return i;
            }
            else if(l.get(i).distance >= this.distance && fuzzy)
            {
                return i;
            }
        }
        return -1;  
    }
    private int getOtherCarDistance(int cari, int carlane)
    {
        ArrayList<Car> l = parent.getLane(carlane);
        return l.get(cari).getRearDistance();
    }
    public void RandomSlow()
    {
        if(r.nextDouble() <= pSlowdown)
        {
            curSpeed -= acceleration;
        }
        
        if(curSpeed < 0) curSpeed = 0;
    }
    public void Move()
    {
        newDist = distance + curSpeed;
    }
    public void Update()
    {
        distance = newDist;
    }
    
    public int getDistance()
    {
        return distance;
    }
    public int getRearDistance()
    {
        return distance - carlength;
    }
    public int getCarLength()
    {
        return carlength;
    }
    //This seems counterintuative but it's so we can have the cars closest to the end of the sim at the front of arrays
    @Override
    public int compareTo(Car o) 
    {
        if(distance < o.distance)
        {
            return 1;
        }
        else if (distance > o.distance)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

}
