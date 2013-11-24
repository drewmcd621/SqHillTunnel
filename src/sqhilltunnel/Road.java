
package sqhilltunnel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;


public class Road 
{

    
    int length = 4225*3;
    
    ArrayList<Car> LeftLane = new ArrayList<>();
    ArrayList<Car> RightLane = new ArrayList<>();
    
    Random r;
    long nextCar = 0;
    double CarsPerSecond = 0.626416666666667;
    int meanSpeed = 71;
    int stdSpeed = 19;
    
    //Stats
    int cars = 0;
    
    public Road()
    {
        r = new Random();
        nextCar = 0;
    }
    
    public void itterate(long Now)
    {
       int total = LeftLane.size() + RightLane.size();
       int left = 0;
       int right = 0;
       int leftDist = 0;
       int rightDist = 0;
       Car c;
       for(int i = 0; i < total; i ++)
       {
           if(left >= LeftLane.size()) //End of left lane grab right hand side
           {
               c = RightLane.get(right);
               right++;
           }
           else if(right >= RightLane.size()) //End of right lane grab left hand side
           {
               c = LeftLane.get(left);
               left ++;
           }
           else if(LeftLane.get(left).compareTo(RightLane.get(right)) < 0) //Left lane is >= right lane, pick left
           {
               c = LeftLane.get(left);
               left ++;
           }
           else //Right lane > left lane, pick right
           {
               c = RightLane.get(right);
               right++;
           }
           //Update c
           c.Accelerate();
           c.Slowdown();
           c.RandomSlow();
           c.Move();
           //Remove cars
           if(c.getDistance() >= length)
           {
               removeCar(c);
               total --;
           }
           
       }
       for(Car rc : RightLane)
       {
           rc.Update();
       }
       for(Car lc : LeftLane)
       {
           lc.Update();
       }
       
       //Add cars
       if(Now >= nextCar)
       {
           if(addCar(Now));
           {
            cars++;
           }
       }
       
       //Sort arrays on distance:  Farthest = First
       Collections.sort(LeftLane);
       Collections.sort(RightLane);
       
       
        
    }
    public boolean addCar(long Now)
    {
        int speed = (int)(r.nextGaussian()*stdSpeed + meanSpeed);
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
        c.Slowdown(); //Reduce speed due to cars in front
        getNextArrival(Now); //Calc next arrival
        return true;
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
            if(firstLeft.getRearDistance() >= firstLeft.getCarLength())
            {
                left = true;
            }
        }
        if(!right)
        {
            if(firstRight.getRearDistance() >= firstRight.getCarLength())
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
    public void removeCar(Car c)
    {
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
        
      

}
