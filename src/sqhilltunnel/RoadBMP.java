
package sqhilltunnel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.color.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 *
 * Drew McDermott
 * adm75@pitt.edu
 */
public class RoadBMP 
{
    BufferedImage  image;
    
    int imgWidth;
    int bWidth;
    int curRow;
    int maxRow;

    
    public RoadBMP(int timeRows, int blockWidth, int roadWidth)
    {
        maxRow = timeRows;
        bWidth = blockWidth;
        imgWidth = (int)((double)roadWidth/(double)blockWidth) + 1;
        curRow = 0;

        
        image = new BufferedImage(imgWidth, maxRow, BufferedImage.TYPE_INT_RGB);
        
     }
    public int addRow(Road r)
    {
        
        if(curRow >= maxRow) return 0;
        
        int cr = 0;
        int cl = 0;
        Color c;
        
        boolean left;
        boolean right;
        
        ArrayList<Car> RL = r.getLane(0);
        ArrayList<Car> LL = r.getLane(1);
        
        int x;
        for(int d =r.getLength(); d >= 0 ; d-= bWidth)
        {
            x = (d / bWidth);
            if(cl >= LL.size())
            {
                left = false;
            }
            else if(LL.get(cl).getDistance() > d)
            {
                //Car in left lane
                left = true;
                cl++;
            }
            else
            {
                left = false;
            }
            
            if(cr >= RL.size())
            {
                right = false;
            }
            else if(RL.get(cr).getDistance() > d)
            {
                //Car in left lane
                right = true;
                cr++;
            }
            else
            {
                right = false;
            }
            
            if(right && left)
            {
                c = Color.green;
            }
            else if(left)
            {
                c = Color.blue;
            }
            else if(right)
            {
                c = Color.red;
            }
            else
            {
                c = Color.black;
            }
            
            image.setRGB(x, curRow, c.getRGB());

        }
        curRow ++;
        
        return maxRow - curRow;
    }
    public void save(String name) 
    {
        try {
            File out = new File(name + ".png");
            ImageIO.write(image, "png", out);
        } catch (IOException ex) {
            Logger.getLogger(RoadBMP.class.getName()).log(Level.SEVERE, null, ex);
        }
        curRow = 0;
    }

       
    
    

}
