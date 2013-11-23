
package sqhilltunnel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;


public class Road 
{
    PriorityQueue<Car> LeftLane;
    PriorityQueue<Car> RightLane;
    
    public Road()
    {
        LeftLane = new PriorityQueue<>();
        RightLane = new PriorityQueue<>();
    }
    
    public void itterate()
    {
        PriorityQueue<Car> temp = new PriorityQueue<>();
        temp.addAll(LeftLane);
        temp.addAll(RightLane);
        
        Car c = temp.poll();
        //Go through each car
        while(c != null)
        {
            
            c = temp.poll();
        }
        
    }
        
      

}
