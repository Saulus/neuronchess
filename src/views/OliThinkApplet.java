package views;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JApplet;
import javax.swing.JPanel;

/**
 * @author Oliver Brausch 25.11.2009
 */
public class OliThinkApplet extends JPanel implements MouseListener, MouseMotionListener {
   private static final long serialVersionUID = 1L;
   static int ix = 0;
   static int iy = 0;
   static OliPiece thisPiece = null;
   static Container cpane;
   static OliThinkApplet applet;
   
   public void init() {
      applet = this;
      //cpane = getContentPane();
      cpane.setLayout(null);
      
      TimerTask engine = new TimerTask() {
         public void run() {
            //OliThink.main(new String[]{});
         }
      };
      new Timer().schedule(engine, 0L);
      //OliThink.receiveCommand("time 3000");
   }
   
   public void stop() {
      //OliThink.receiveCommand("quit");
   }

   public synchronized static void parsePos(String pos) {
      cpane.removeAll();
      thisPiece = null;
      
      int col = 7, row = 0;
      for (int i = pos.length() - 1; i >= 0; i--) {
         char s = pos.charAt(i);
         if (s == '/') {
            row++;
            col = 7;
         } else if (s >= '1' && s <= '8') {
            col -= s - '0';
         } else {
            applet.activatePiece(new OliPiece(s, col--, row));
         }
      }
      for (col = 0; col < 8; col ++)
         for (row = 0; row < 8; row ++)
            cpane.add(new OliSquare(col, row));
      applet.repaint();
   }
   

   public synchronized static void engineMove(int fromcol, int fromrow, int tocol, int torow) {
      int fromx = fromcol*70;
      int fromy = (7-fromrow)*70;
      int tox = tocol*70;
      int toy = (7-torow)*70;

      OliPiece p = findPiece(fromcol, fromrow);
      for (int i = 1; i < 100; i++) try {
         int x = fromx + i*(tox-fromx)/100;
         int y = fromy + i*(toy-fromy)/100;
         p.setBounds(x, y);
         Thread.sleep(1L);
      } catch (InterruptedException ie) {
      }
      p.setBounds(tox, toy);
   }
   
   private synchronized static OliPiece findPiece(int col, int row) {
      Component[] comps = cpane.getComponents();
      for (Component comp : comps) {
         if (comp instanceof OliPiece && ((OliPiece)comp).isThere(col, row))
            return (OliPiece)comp;
      }
      return null;
   }
      
   private synchronized void activatePiece(OliPiece p) {
      p.addMouseListener(this);
      p.addMouseMotionListener(this);
      cpane.add(p);      
      p.setOpaque(false);
   }
   
   public void mouseClicked(MouseEvent arg0) {
   }

   public void mouseEntered(MouseEvent arg0) {
   }

   public void mouseExited(MouseEvent arg0) {
   }

   public void mousePressed(MouseEvent arg0) {
      thisPiece = (OliPiece) arg0.getSource();
      ix = thisPiece.getX();
      iy = thisPiece.getY();
   }

   public void mouseReleased(MouseEvent arg0) {
      if (thisPiece == null) thisPiece = (OliPiece) arg0.getSource();
      int col = thisPiece.col;
      int row = thisPiece.row;
      int type = thisPiece.type;
      thisPiece = null;
      int newcol = (ix+34)/70;
      int newrow = 7-(iy+34)/70;

      if (newcol != col || newrow != row) {
         String move = String.valueOf((char)('a' + col)) + (char)('1' + row)
               + (char)('a' + newcol) + (char)('1' + newrow);
         
         if ((type == 'p' || type == 'P') && (newrow == 7 || newrow == 0)) move += "q";
         //OliThink.receiveCommand(move);
      }
   }
   
   public void mouseDragged(MouseEvent arg0) {
      if (thisPiece == null) thisPiece = (OliPiece) arg0.getSource();
      arg0.translatePoint(ix, iy);
      ix = arg0.getX() - 35;
      iy = arg0.getY() - 35;
      thisPiece.setBounds(ix, iy);
      this.repaint();
   }

   public void mouseMoved(MouseEvent arg0) {
   }
} 
