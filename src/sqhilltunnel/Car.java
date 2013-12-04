
package sqhilltunnel;

import java.util.ArrayList;
import java.util.Random;


public class Car  implements Comparable<Car>
{
    //Constants
    private int acceleration = 6; //ft/s^2
    private double pSlowdown = 0.25; 
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
    private boolean changeLane = false;
    
    
    public Car(int maximumSpeed, Road parentRoad, int roadLane, long Now)
    {
        distance = carlength; //So that the whole car is on the sim road
        maxSpeed = maximumSpeed; 

        startTime = Now;
        lane = roadLane;
        parent = parentRoad;
        r = new Random();
        curSpeed = maximumSpeed; //Enter sim at max speed
        Slowdown(); //Then slowdown for car in front
    }
    
    public void Accelerate()
    {
        curSpeed += acceleration;
        
        if(curSpeed > maxSpeed) curSpeed = maxSpeed;
        
    }
    public void Slowdown()
    {
        changeLane = false;
        int thislane = getCarPos(lane, false);
        int otherlane = getCarPos(1-lane,true);
        int thold = 0;
        
        //Get car in front of me
        Car front = getOtherCar(thislane - 1, lane);

        
        int maxs = curSpeed;
        int maxlc = -1;
        if(front != null)
        {
            maxs = front.getRearDistance() - getDistance() - 1; 
            if(maxs > curSpeed)
            {
                maxs = curSpeed;
            }
        }
        //Can't go negative
        if(maxs < 0)
        {
            maxs =  0;
        }
        //Check if in a restricted lane change area
        if(parent.canChangeLanes(this))
        {
            //Get car in front of me in other lane
            Car otherfront = getOtherCar(otherlane, 1-lane);
            //Get car behind me in other lane
            Car otherbehind = getOtherCar(otherlane+1, 1-lane);
                       
            //Check if there is room to change lanes
            if(otherbehind == null)
            {
                if(otherfront == null)
                {
                    maxlc = curSpeed + thold + 1;
                }
                else
                {
                    //Get max speed if changing lanes
                    maxlc = Math.min(otherfront.getRearDistance() - this.distance - 1, curSpeed);
                }
            }
            else if((otherbehind.getDistance() + otherbehind.curSpeed) < this.getRearDistance())
            {
               if(otherfront == null)
                {
                    maxlc = curSpeed + thold +1;
                }
                else
                {
                    //Get max speed if changing lanes
                    maxlc = Math.min(otherfront.getRearDistance() - this.distance - 1, curSpeed);
                }
            }
             //Prioritize staying in lane if equal speed
            
            
        }
        
         if(maxlc > (maxs + thold))
         {
                //Change lanes
                changeLane = true;
                curSpeed = Math.min(maxlc, curSpeed);
         } 
         else
         {
             changeLane = false;
             curSpeed = maxs;
         }  
         
         curSpeed = Math.min(maxSpeed, curSpeed);
         curSpeed = Math.max(curSpeed, 0);
        
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
    private Car getOtherCar(int cari, int carlane)
    {
        
        ArrayList<Car> l = parent.getLane(carlane);
        if(cari >= 0 && cari < l.size())
        {
        return l.get(cari);
        }
        else
        {
            return null;
        }
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
        if(changeLane)
        {
            parent.changeLanes(this, 1-lane);
            lane = 1 - lane;
            changeLane = false;
        }
        distance = newDist;
 
    }
    public void setEndTime(long end)
    {
        endTime = end;
    }
    public long getTotalTime()
    {
        return endTime - startTime;
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
