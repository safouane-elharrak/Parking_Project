import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Voiture extends JLabel implements Runnable { 
        String nom; 
	Parking park;
        ImageIcon carImg ; // pour les images des voitures
        int posx;
        int posy;
        int positionY;
        int positionX;
        String side; // Les cotes de voiture 
        public static Thread MesVoitures[];

    public Voiture(String name, Parking park,int x,int y){
        this.nom    = name; 
	this.park   = park; 
        this.posx   = x;
        this.posy   = y ;
        this.positionY = y;
        this.positionX = x;
        this.carImg = new ImageIcon("images/c3.png");
        this.setIcon(carImg);
        Dimension size = this.getPreferredSize();
        this.setBounds(posx, posy, size.width,size.height);
       }
      public boolean rentrer() throws InterruptedException{
          // pour accepter l'entrer au parking s'il est des places disponible
	if(this.park.accepter(this)){
            return true;
        }else{            
            return false;
        }
      }
      public void changeImage(String name){         
        this.setIcon(new ImageIcon(name));
      }
      
      public String toString(){         
          return "Le nom de la voiture est : "+this.nom + " posX = "+posx + " posY = "+this.posy + " le cote de la voiture : "+this.side;
      }
      
      public void run(){ 
	 System.out.println(this);
	try {  // demande ou obtunue  l'acces a parking
	    while(true){
                Thread.sleep((long)  (500* Math.random())); // reste 500 ms dans le parking
                System.out.format("[%s]: Je demande a rentrer  \n", this.nom);
		this.park.semaphore.acquire();
		if(!(this.rentrer())){                    
                     System.out.format("[%s]  : La voiture veut entrer a l'interieur  \n", this.nom);
                }
                this.park.semaphore.release();
		Thread.sleep((long)  (500* Math.random()));
                if(this.park.infoVoitures.contains(this)){
		System.out.format("[%s]: Je demande a sortir  \n", this.nom);
                this.park.semaphore.acquire(); // permet de demander une permission ou le thread apland ce bloc
                this.park.Quitter(this);
                this.park.semaphore.release(); // libere la permission                
                }               
	    }            
          }catch(InterruptedException e){              
              System.out.println(e.getMessage());
          }
        }
        
    public static void main(String[] args) {
        int TailleParking=4;
        JFrame frame     = new JFrame("L'animation pour le stationnement des voitures"); // partie de l'animation
	Parking  parking = new Parking(TailleParking);
        frame.setContentPane(parking);
        parking.setLayout(null);
        frame.setSize(1540,824); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	int nbVoitures = 4 ; 
        String CarName = "Voiture";
        JButton startAnimation = new JButton("Demarrer l'animation");
        parking.add(startAnimation);
        startAnimation.setFont(new Font("Serif",Font.ITALIC,30));
        startAnimation.setVisible(true);
        startAnimation.setBounds(325, 400, 300, 100); // pour le button de démarrer
        startAnimation.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startAnimation.setBackground(Color.BLUE);
        startAnimation.setForeground(Color.WHITE);
        startAnimation.setBorder(null); 
//        Créez six threads pour notre exemple, car nous avons juste besoin de six voitures. Un thread pour une voiture.
        Thread MesVoitures[][] = new Thread[3][2];
        int x ;
        int y = 150;
	for (int i = 0; i< 3; i++){
            x = 60;
            for(int j=0;j<2;j++){                
            Voiture  car =  new Voiture(CarName +" " + i+j,parking,x,y);
            if(j == 0){
                car.side = "l";                 
            }else{                
                car.side = "r";
            }
            MesVoitures[i][j] =  new Thread(car);        
            parking.add(car);       
            parking.allCars.add(car);
            x   = x + 80;
            }           
            y   = y + 160;            
        }               
//      Créer Action Listener pour le bouton
        ActionListener actionListenerForButoon = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            for (int i = 0; i< 3; i++){
                 for(int j=0;j<2;j++){
                         MesVoitures[i][j].start();
                 }
            }
          }
       };
      startAnimation.addActionListener(actionListenerForButoon);
    }
}

        
