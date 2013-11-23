
package sqhilltunnel;

import java.util.Random;


public class Car  implements Comparable<Car>
{
    //Constants
    private int acceleration = 6; //ft/s^2
    private double pSlowdown = 0; 
    //Variables
    private int distance; //ft
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
        distance = 0;
        maxSpeed = maximumSpeed; 
        curSpeed = maximumSpeed; //Enter sim at max speed
        startTime = Now;
        lane = roadLane;
        r = new Random();
    }
    
    public void Accelerate()
    {
        curSpeed += acceleration;
        
        if(curSpeed > maxSpeed) curSpeed = maxSpeed;
        
    }
    public void Slowdown()
    {
        
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
        distance += curSpeed;
    }
    
    public int getDistance()
    {
        return distance;
    }

    @Override
    public int compareTo(Car o) 
    {
        if(distance > o.distance)
        {
            return 1;
        }
        else if (distance < o.distance)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

}
