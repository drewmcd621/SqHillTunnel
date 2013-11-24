
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
    double secPerCar = 1.59638153518691;
    int meanSpeed = 71;
    int stdSpeed = 19;
    
    public Road()
    {
        r = new Random();
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
           if(LeftLane.get(left).compareTo(RightLane.get(right)) < 0) //Left lane is >= right lane, pick left
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
           }
           
       }
       
       //Add cars
       if(Now >= nextCar)
       {
           addCar(Now);
       }
       
       //Sort arrays on distance:  Farthest = First
       Collections.sort(LeftLane);
       Collections.sort(RightLane);
       
       
        
    }
    public void addCar(long Now)
    {
        int speed = (int)(r.nextGaussian()*stdSpeed + meanSpeed);
        int lane;
        Car c;
        if(r.nextBoolean())
        {
            lane = 0; //right
            c = new Car(speed, this, lane, Now);
            RightLane.add(RightLane.size(), c);
        }
        else
        {
            lane = 1; //left
            c = new Car(speed, this, lane, Now);
            LeftLane.add(LeftLane.size(), c);
        }
        c.Slowdown(); //Reduce speed due to cars in front
        
    }
    public void removeCar(Car c)
    {
        //Before removing do stats here
        RightLane.remove(c);
        LeftLane.remove(c);
    }
    
    private void getNextArrival(long Now)
    {
        int ttc = getPoisson(r, secPerCar);
        nextCar = Now + ttc;
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

            return k - 1; 
    } 
        
      

}
