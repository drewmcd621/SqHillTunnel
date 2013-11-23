
package sqhilltunnel;


public class Car  implements Comparable<Car>
{
    private int distance;
    
    public void Accelerate()
    {
        
    }
    public void Slowdown()
    {
        
    }
    public void RandomSlow()
    {
        
    }
    public void Move()
    {
        
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
