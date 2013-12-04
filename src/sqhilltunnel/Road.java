
package sqhilltunnel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;


public class Road 
{

    
    int length;
    
    ArrayList<Car> LeftLane = new ArrayList<>();
    ArrayList<Car> RightLane = new ArrayList<>();
    
    Random r;
    long nextCar = 0;
    int LCRstart = -2;
    int LCRend = -1;
    int iniDist = 100;
    
    //Settings
    double CarsPerSecond = 0.626416666666667;
    int meanSpeed = 87;
    int stdSpeed = 11;
    boolean laneChange = true;
    
    //Stats
    int cars = 0;
    int laneChanges = 0;
    double totSpeed = 0;
    Stats roadStats;
    
    
    public Road(int lengthOfRoad)
    {
        length = lengthOfRoad;
        r = new Random();
        nextCar = 0;
    }
    public void setStats(Stats s)
    {
        roadStats = s;
    }    
    public void itterate(long Now, long End)
    {
       
        //Sort arrays on distance:  Farthest = First
       Collections.sort(LeftLane);
       Collections.sort(RightLane);
       
       ArrayList<Car> temp = new ArrayList<>();
       
       temp.addAll(LeftLane);
       temp.addAll(RightLane);
       //Collections.sort(temp);
       
       for(Car c : temp)
       {
           //Update c
           c.Accelerate();
           c.Slowdown();
           c.RandomSlow();
           c.Move();
       }

       for(int i = 0; i < RightLane.size(); i++)
       {
           Car rc = RightLane.get(i);
           rc.Update();
           if(rc.getDistance() >= length)
           {
               
               removeCar(rc, Now);
               roadStats.addCar(rc);
               i--;
           }
       }
       
       for(int i = 0; i < LeftLane.size(); i++)
       {
           Car lc = LeftLane.get(i);
           lc.Update();
           if(lc.getDistance() >= length)
           {
               removeCar(lc, Now);
               i--;
           }
       }
      
       
       //Add cars
       if(Now >= nextCar && Now < End)
       {
           if(addCar(Now));
           {

           }
       }
       
      

       
       
        
    }
    public boolean addCar(long Now)
    {
        int speed = (int)(r.nextGaussian()*stdSpeed + meanSpeed);
        
        if(speed < stdSpeed) speed = stdSpeed;
        
        int lane = getLaneToAddCar();
        Car c;
        if(lane == -1)
        {
            //No possible location to add car
            nextCar = Now +1;
            return false;
        }
        else if(lane == 0)
        {
            c = new Car(speed, this, lane, Now);
            RightLane.add(RightLane.size(), c);
        }
        else
        {
            c = new Car(speed, this, lane, Now);
            LeftLane.add(LeftLane.size(), c);
        }
      
        getNextArrival(Now); //Calc next arrival
        return true;
    }
    public int getCarsOnLanes()
    {
        return LeftLane.size() + RightLane.size();
    }

       
    private int getLaneToAddCar()
    {
        boolean right = false;
        boolean left = false;
        Car firstRight = null;
        Car firstLeft = null;
        if(RightLane.size() > 0)
        {
            firstRight = RightLane.get(RightLane.size() - 1);
        }
        else
        {
            right = true;
        }
        if(LeftLane.size() > 0)
        {
            firstLeft = LeftLane.get(LeftLane.size() - 1);
        }
        else
        {
            left = true;
        }

        if(!left)
        {
            if(firstLeft.getRearDistance() > firstLeft.getCarLength())
            {
                left = true;
            }
        }
        if(!right)
        {
            if(firstRight.getRearDistance() > firstRight.getCarLength())
            {
               right = true;
            }
        }
        
        if(left && right)
        {
            return r.nextInt(2); //Between 0 and 1
        }
        else if(left || right)
        {
            if(right)
            {
                return 0;
            }
            else
            {
                return 1;
            }
        }
        else
        {
            return -1;
        }
    }
        
    
    public void removeCar(Car c, long Now)
    {
        c.setEndTime(Now);
        long time = c.getTotalTime();
        double avgSpeed = (double)length / (double)time;
        //System.out.println(avgSpeed);
        totSpeed += avgSpeed;
        cars ++;
        //Before removing do stats here
        
        RightLane.remove(c);
        LeftLane.remove(c);
    }
    
    private void getNextArrival(long Now)
    {
        double ttc = getPoisson(r, CarsPerSecond);
        nextCar = Now + 1 + (int)Math.round(ttc) ;

    }
    
    private int getPoisson(Random r, double lambda) 
    { 
            double L = Math.exp(-lambda); 
            int k = 0; 
            double p = 1.0; 
            do { 
                k++; 
                p = p * r.nextDouble(); 
            } while (p > L); 

            return k -1; 
    }
    
    public int getNumCars()
    {
        return cars;
    }
    public double getAvgSpeed()
    {
        return totSpeed / cars;
    }
    public int getLaneChanges()
    {
        return laneChanges;
    }
    public ArrayList<Car> getLane(int lane)
    {
        if(lane == 0)
        {
            return RightLane;
        }
        else
        {
            return LeftLane;
        }
    }
    public boolean canChangeLanes(Car c)
    {
        int d = c.getDistance();
        
        return (d > iniDist) && (d < LCRstart || d > LCRend) && laneChange;
        
    }
    public void setLaneChangeRestrictedArea(int start, int end)
    {
        LCRstart = start;
        LCRend = end;
    }
    public void changeLanes(Car c, int newLane)
    {
        //Remove existing car
        RightLane.remove(c);
        LeftLane.remove(c);
        
        if(newLane == 0)
        {
            RightLane.add(c);
            Collections.sort(RightLane);
        }
        else
        {
            LeftLane.add(c);
           Collections.sort(LeftLane);
        }
        laneChanges ++;
    }
    public int getLength()
    {
        return length;
    }
      

}
