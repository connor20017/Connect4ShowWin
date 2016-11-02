package connect4showwin;
import java.awt.*;

public class Piece {
    private Color color;
    public int value=(int)(Math.random()*5+1);
    
    Piece(Color _color)
    {
        
        color = _color;

    }
    Color getColor()
    {
        return (color);
    }
   
  
}
