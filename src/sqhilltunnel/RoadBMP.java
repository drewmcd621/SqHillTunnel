
package sqhilltunnel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.color.*;
import java.io.File;
import java.io.IOException;
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
    int lane;
    
    public RoadBMP(int timeRows, int blockWidth, int roadWidth, int roadLane)
    {
        maxRow = timeRows;
        bWidth = blockWidth;
        imgWidth = (int)((double)roadWidth/(double)blockWidth) + 1;
        curRow = 0;
        lane = roadLane;
        
        image = new BufferedImage(imgWidth, maxRow, BufferedImage.TYPE_INT_RGB);
        
     }
    public int addRow(Road r)
    {
        
        if(curRow >= maxRow) return 0;
        
        int ci = 0;
        int x;
        for(int d =r.getLength(); d >= 0 ; d-= bWidth)
        {
            x = (imgWidth-1) - (d / bWidth);
            if(ci >= r.getLane(lane).size())
            {
                image.setRGB(x, curRow, Color.black.getRGB());
            }
            else if(r.getLane(lane).get(ci).getDistance() > d)
            {
                image.setRGB(x, curRow, Color.white.getRGB());
                ci++;
            }
            else
            {
                image.setRGB(x, curRow, Color.black.getRGB());
            }
        }
        curRow ++;
        
        return maxRow - curRow;
    }
    public void save() 
    {
        try {
            File out = new File("saved.png");
            ImageIO.write(image, "png", out);
        } catch (IOException ex) {
            Logger.getLogger(RoadBMP.class.getName()).log(Level.SEVERE, null, ex);
        }
        curRow = 0;
    }

       
    
    

}
