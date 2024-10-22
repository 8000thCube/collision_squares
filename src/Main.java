package collision_squares;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
public class Main{
	static final double SCALE_FACTOR=0.95,DEFAULT_SCALE=1.5;
	static final int SCREEN_WIDTH=Toolkit.getDefaultToolkit().getScreenSize().width,SCREEN_HEIGHT=Toolkit.getDefaultToolkit().getScreenSize().height;
	static double Scale=1.0/DEFAULT_SCALE;
	static AffineTransform MainTransform=new AffineTransform();
	static JFrame Frame=new JFrame();
	static void initializeWindow(){
		BufferedImage bq=new BufferedImage(0x40,0x40,BufferedImage.TYPE_INT_RGB);
		Graphics2D gq=bq.createGraphics();
		JFrame jq=Frame;
		gq.setColor(Color.WHITE);
		gq.fillRect(0,0,0x40,0x40);
		jq.setIconImage(bq);
		jq.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}
		});
		jq.addComponentListener(new ComponentAdapter(){public void componentResized(ComponentEvent e){
			double dq=((double)Frame.getWidth())/SCREEN_WIDTH,dw=((double)Frame.getHeight())/SCREEN_HEIGHT,de=1.0/Scale;
			Scale=Math.sqrt(dq*dq+dw*dw);
			MainTransform.scale(Scale*de,Scale*de);
		}});
		jq.setSize(SCREEN_WIDTH*2/3,SCREEN_HEIGHT*2/3);
		jq.setLocation(SCREEN_WIDTH/6,SCREEN_HEIGHT/6);
		jq.setVisible(true);
	}public static void main(String args[]){
		//AffineTransform aq=MainTransform;
		initializeWindow();
		
		while(true){
			/*if(wq.checkKey(KeyEvent.VK_CONTROL)){
				if(wq.checkKey(KeyEvent.VK_MINUS)){
					Scale*=SCALE_FACTOR;
					aq.scale(SCALE_FACTOR,SCALE_FACTOR);
				}if(wq.checkKey(KeyEvent.VK_EQUALS)){
					Scale/=SCALE_FACTOR;
					aq.scale(1.0/SCALE_FACTOR,1.0/SCALE_FACTOR);
				}if(wq.checkKey(KeyEvent.VK_RIGHT)){aq.translate(2.0/aq.getScaleX(),0.0);}
				if(wq.checkKey(KeyEvent.VK_LEFT)){aq.translate(-2.0/aq.getScaleX(),0.0);}
				if(wq.checkKey(KeyEvent.VK_DOWN)){aq.translate(0.0,2.0/aq.getScaleY());}
				if(wq.checkKey(KeyEvent.VK_UP)){aq.translate(0.0,-2.0/aq.getScaleY());}
				if(wq.checkKey(KeyEvent.VK_R)){
					Scale=DEFAULT_SCALE;
					aq.setToScale(DEFAULT_SCALE,DEFAULT_SCALE);
				}
			}*/
			Frame.repaint();
			try{Thread.sleep(30);}catch(InterruptedException exception){exception.printStackTrace();}
		}
	}/* according to a google ai: 
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class CheckKeyPress {

    public static void main(String[] args) {
        try {
            Robot robot = new Robot();

            // Check if the 'A' key is currently pressed
            boolean isAPressed = (robot.getKeyState(KeyEvent.VK_A) & 0x80) != 0;

            if (isAPressed) {
                System.out.println("The 'A' key is pressed.");
            } else {
                System.out.println("The 'A' key is not pressed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
	 is how to get key state*/
}
