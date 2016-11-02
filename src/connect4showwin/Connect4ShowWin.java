/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4showwin;

import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.GradientPaint;

public class Connect4ShowWin extends JFrame implements Runnable {
    static final int XBORDER = 20;
    static final int YBORDER = 20;
    static final int YTITLE = 25;
    static final int WINDOW_WIDTH = 840;
    static final int WINDOW_HEIGHT = 865;    
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Graphics2D g;
    int score;
    boolean player1Turn;
    final static int NUM_ROWS = 8;
    final static int NUM_COLUMNS = 8;  
    Piece board[][] = new Piece[NUM_ROWS][NUM_COLUMNS];
    int mostRecentRow;
    int mostRecentCol;
    WinInfo winInfo;
    int NUM_CONNECT_WIN = 4;    
 
   
    public static void main(String[] args) {
        Connect4ShowWin frame = new Connect4ShowWin();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public Connect4ShowWin() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton() || e.BUTTON3 == e.getButton() ) {
                    
                    int ydelta = getHeight2()/NUM_ROWS;
                    int xdelta = getWidth2()/NUM_COLUMNS;

                    int zcol = 0;
                    int zcolLoc = xdelta;
                    for (int i=0;i<NUM_COLUMNS;i++)
                    {
                        if (zcolLoc*i < e.getX()-getX(0))
                            zcol = i;
                    } 
                    
                    for (int i=NUM_ROWS-1;i>=0;i--)
                    {
                        if (board[i][zcol] == null)
                        {
                            if (player1Turn)
                                board[i][zcol] = new Piece(Color.red);            
                            else
                                board[i][zcol] = new Piece(Color.black);
                            player1Turn = !player1Turn;
                            mostRecentRow = i;
                            mostRecentCol = zcol;
                    
                            break;
                        }
                    }
                       
                    
                }

                if (e.BUTTON3 == e.getButton()) {

                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {

        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {

        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_UP == e.getKeyCode()) {
                } else if (e.VK_DOWN == e.getKeyCode()) {
                } else if (e.VK_LEFT == e.getKeyCode()) {
                } else if (e.VK_RIGHT == e.getKeyCode()) {
                } else if (e.VK_ESCAPE == e.getKeyCode()) {
                    reset();
                }
                repaint();
            }
        });
        init();
        start();
    }
    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }
////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
//fill background
        
        g.setColor(Color.cyan);
        g.fillRect(0, 0, xsize, ysize);

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
//fill border
        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }

//          g.drawString(""+board.value, getX(alienXPos[i])-7 ,getYNormal(alienYPos[i]));
         
        
        
//Calculate the width and height of each board square.
        int ydelta = getHeight2()/NUM_ROWS;
        int xdelta = getWidth2()/NUM_COLUMNS;

        if (winInfo.getWin())
        {
            if (winInfo.getWinDirection() == WinInfo.WinDirection.HORIZONTAL)
            {
                for (int i=0;i<NUM_CONNECT_WIN;i++)
                {
                    g.setColor(Color.gray);
                    g.fillRect(getX((winInfo.getColumn()+i)*xdelta), getY(winInfo.getRow()*ydelta), xdelta, ydelta);
                }
            }
            if (winInfo.getWinDirection() == WinInfo.WinDirection.VERTICAL)
            {
                for (int i=0;i<NUM_CONNECT_WIN;i++)
                {
                    g.setColor(Color.gray);
                    g.fillRect(getX(winInfo.getColumn()*xdelta), getY((winInfo.getRow()+i)*ydelta), xdelta, ydelta);
                }
            }
            if (winInfo.getWinDirection() == WinInfo.WinDirection.DIAGONAL_DOWN)
            {
                for (int i=0;i<NUM_CONNECT_WIN;i++)
                {
                    g.setColor(Color.gray);
                    g.fillRect(getX((winInfo.getColumn()+i)*xdelta), getY((winInfo.getRow()+i)*ydelta), xdelta, ydelta);
                }
            }   
            if (winInfo.getWinDirection() == WinInfo.WinDirection.DIAGONAL_UP)
            {
                for (int i=0;i<NUM_CONNECT_WIN;i++)
                {
                    g.setColor(Color.gray);
                    g.fillRect(getX((winInfo.getColumn()+i)*xdelta), getY((winInfo.getRow()-i)*ydelta), xdelta, ydelta);
                }
            }               
        }        

 //draw grid
        g.setColor(Color.black);
        for (int zi = 1;zi<NUM_ROWS;zi++)
        {
            g.drawLine(getX(0),getY(zi*ydelta),
                    getX(getWidth2()),getY(zi*ydelta));
        }
        for (int zi = 1;zi<NUM_COLUMNS;zi++)
        {
            g.drawLine(getX(zi*xdelta),getY(0),
                    getX(zi*xdelta),getY(getHeight2()));
        }
        
       
//Draw the piece.        
        for (int zi = 0;zi<NUM_ROWS;zi++)
        {
            for (int zx = 0;zx<NUM_COLUMNS;zx++)
            {
                if (board[zi][zx] != null)
                {
                    g.setColor(board[zi][zx].getColor()); 
//                    g.fillOval(getX(zx*xdelta),getY(zi*ydelta),xdelta,ydelta);
                    
                  drawdiamond(getX(zx*xdelta+50), getY(zi*ydelta+50),0,50,50);
  g.setColor(Color.white);
            g.setFont(new Font("Arial Black",Font.PLAIN,30));
            g.drawString(""+board[zi][zx].value, getX(zx*xdelta+30),getY(zi*ydelta+50));
                }
                   
            }
        }

               g.setColor(Color.black);
            g.setFont(new Font("Arial Black",Font.PLAIN,20));
            g.drawString(""+score, 20 ,50);

        gOld.drawImage(image, 0, 0, null);
    }

////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = .1;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {


        player1Turn = true;
        for (int zi = 0;zi<NUM_ROWS;zi++)
        {
            for (int zx = 0;zx<NUM_COLUMNS;zx++)
            {
                board[zi][zx] = null;
            }
        }
      score=0;
        winInfo = new WinInfo();
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {

        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }

            reset();

        }

        checkWinWholeBoard();    
    }
    

    public boolean checkWinWholeBoard()
    {
        int numConsecutive = 0;
        Color colorMatch = null;

        int zzcol = 0;
        int zzrow = 0;        
        
        winInfo.initWin();
//Check for horizontal win.        
        for (int row=0;row<NUM_ROWS;row++)
        {
            for (int col=0;col<NUM_COLUMNS;col++)
            {

                if (board[row][col] == null)
                {        
                    numConsecutive = 0;
                    colorMatch = null;
                }
                else if (board[row][col].getColor() == colorMatch)
                {   
                    numConsecutive++;                    
                    
                    if (numConsecutive == NUM_CONNECT_WIN)
                    {
                        winInfo.setWinInfo(true,zzrow,zzcol,WinInfo.WinDirection.HORIZONTAL); 
                       
                        return (true);
                    }
                }
                else if (board[row][col].getColor() != colorMatch)
                {                                   
                    numConsecutive = 1;
                    zzrow = row;
                    zzcol = col;
                    colorMatch = board[row][col].getColor();
                }        
             
            }            
            colorMatch = null;
            numConsecutive = 0;  
            
        }
          if (numConsecutive == NUM_CONNECT_WIN)
             // score+=value;
              
//Check for vertical win.       
        colorMatch = null;
        numConsecutive = 0;        
        
        for (int col=0;col<NUM_COLUMNS;col++)
        {
            for (int row=0;row<NUM_ROWS;row++)
            {

                if (board[row][col] == null)
                {        
                    numConsecutive = 0;
                    colorMatch = null;
                }
                else if (board[row][col].getColor() == colorMatch)
                {   
                    numConsecutive++;                    
                    
                    if (numConsecutive == NUM_CONNECT_WIN)
                    {
                        winInfo.setWinInfo(true,zzrow,zzcol,WinInfo.WinDirection.VERTICAL);                        
                        return (true);
                    }
                }
                else if (board[row][col].getColor() != colorMatch)
                {                                   
                    numConsecutive = 1;
                    zzrow = row;
                    zzcol = col;
                    colorMatch = board[row][col].getColor();
                }        
                             
                
     
            }            
            colorMatch = null;
            numConsecutive = 0;        
        }    
        
//Check for diagonal win to the right and up.
        colorMatch = null;
        numConsecutive = 0;        
        for (int zrow=0;zrow<NUM_ROWS;zrow++)
        {
            int row = zrow;
            for (int col=0;row>=0;col++)
            {

                if (board[row][col] == null)
                {        
                    numConsecutive = 0;
                    colorMatch = null;
                }
                else if (board[row][col].getColor() == colorMatch)
                {   
                    numConsecutive++;                    
                    
                    if (numConsecutive == NUM_CONNECT_WIN)
                    {
                        winInfo.setWinInfo(true,zzrow,zzcol,WinInfo.WinDirection.DIAGONAL_UP);                        
                        return (true);
                    }
                }
                else if (board[row][col].getColor() != colorMatch)
                {                                   
                    numConsecutive = 1;
                    zzrow = row;
                    zzcol = col;
                    colorMatch = board[row][col].getColor();
                }        
             
                
               
                row--;
            }
            colorMatch = null;
            numConsecutive = 0;        

        }
        
        colorMatch = null;
        numConsecutive = 0;        
        for (int zcol=1;zcol<NUM_COLUMNS;zcol++)
        {
            int col = zcol;
            for (int row=NUM_ROWS-1;col<NUM_COLUMNS;row--)
            {              

                if (board[row][col] == null)
                {        
                    numConsecutive = 0;
                    colorMatch = null;
                }
                else if (board[row][col].getColor() == colorMatch)
                {   
                    numConsecutive++;                    
                    
                    if (numConsecutive == NUM_CONNECT_WIN)
                    {
                        winInfo.setWinInfo(true,zzrow,zzcol,WinInfo.WinDirection.DIAGONAL_UP);                        
                        return (true);
                    }
                }
                else if (board[row][col].getColor() != colorMatch)
                {                                   
                    numConsecutive = 1;
                    zzrow = row;
                    zzcol = col;
                    colorMatch = board[row][col].getColor();
                }        
             
     
                col++;
            }
            colorMatch = null;
            numConsecutive = 0;        

        }
        
//Check for diagonal win to the right and down.
        colorMatch = null;
        numConsecutive = 0;        
        for (int zrow=NUM_ROWS-1;zrow>=0;zrow--)
        {
            int row = zrow;
            for (int col=0;row<NUM_ROWS;col++)
            {

                if (board[row][col] == null)
                {        
                    numConsecutive = 0;
                    colorMatch = null;
                }
                else if (board[row][col].getColor() == colorMatch)
                {   
                    numConsecutive++;                    
                    
                    if (numConsecutive == NUM_CONNECT_WIN)
                    {
                        winInfo.setWinInfo(true,zzrow,zzcol,WinInfo.WinDirection.DIAGONAL_DOWN);                        
                        return (true);
                    }
                }
                else if (board[row][col].getColor() != colorMatch)
                {                                   
                    numConsecutive = 1;
                    zzrow = row;
                    zzcol = col;
                    colorMatch = board[row][col].getColor();
                }        
              
     
                row++;
            }
            colorMatch = null;
            numConsecutive = 0;        

        }
        
        colorMatch = null;
        numConsecutive = 0;        
        for (int acol=1;acol<NUM_COLUMNS;acol++)
        {
            int col = acol;
            for (int row=0;col<NUM_COLUMNS;row++)
            {              

                if (board[row][col] == null)
                {        
                    numConsecutive = 0;
                    colorMatch = null;
                }
                else if (board[row][col].getColor() == colorMatch)
                {   
                    numConsecutive++;                    
                    
                    if (numConsecutive == NUM_CONNECT_WIN)
                    {
                        winInfo.setWinInfo(true,zzrow,zzcol,WinInfo.WinDirection.DIAGONAL_DOWN);                        
                        return (true);
                    }
                }
                else if (board[row][col].getColor() != colorMatch)
                {                                   
                    numConsecutive = 1;
                    zzrow = row;
                    zzcol = col;
                    colorMatch = board[row][col].getColor();
                }        
                   
                
                col++;
            }
            colorMatch = null;
            numConsecutive = 0;        

        }
             
        return (false);
    }   
    ////////////////////////////////////////////////////////////////////////////
    public void drawdiamond(int xpos,int ypos,double rot,double xscale,double yscale)
    {
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );
        
           int xval[] = {1,0,-1,0};
           int yval[] = {0,1,0,-1};
        g.fillPolygon(xval,yval,xval.length);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }

////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
/////////////////////////////////////////////////////////////////////////
    public int getX(int x) {
        return (x + XBORDER);
    }

    public int getY(int y) {
        return (y + YBORDER + YTITLE);
    }
    public int getYNormal(int y) {
        return (-y + YBORDER + YTITLE+getHeight2());
    }

    public int getWidth2() {
        return (xsize - getX(0) - XBORDER);
    }

    public int getHeight2() {
        return (ysize - getY(0) - YBORDER);
    }
}
                            