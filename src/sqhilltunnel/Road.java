
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
       
       //Add cars
       if(Now >= nextCar)
       {
           addCar(Now);
           cars++;
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
        getNextArrival(Now); //Calc next arrival
        
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
        
      

}
