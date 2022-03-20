import java.awt.Graphics;
import static java.awt.image.ImageObserver.WIDTH;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import java.util.concurrent.Semaphore;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Parking extends JPanel{
    int PlacesOccupees ;
    int capacite ; 
    ImageIcon image;
    boolean Place[] ;
    public  Semaphore semaphore;
    public ArrayList<Voiture> allCars;
    public HashSet<Voiture> infoVoitures = new HashSet<Voiture>();
    
//   Définie la capacité de notre Parking
    public Parking(int size){
        capacite = size;  // nbr des places
        this.PlacesOccupees = 0;
        this.allCars = new ArrayList();
        this.semaphore = new Semaphore(5,true);
//        Initialise  les Places 
        Place      =  new boolean[4];
        for(int i=0 ; i<Place.length;i++){
            Place[i]=false ; 
        }
    } 
// Vérifiez s'il existe une place libre pour une nouvelle voiture
    int places(){ 
        return (capacite - this.PlacesOccupees);
    }  
	
// Fonction permettant d'accéder à une voiture pour aller à l'intérieur d'un parking    
    boolean  accepter(Voiture car) {
// Vérifier si trouvé une place libre
     if  (places() > 0 ){           
                PlacesOccupees ++ ;// Si vous trouvez une place pour une voiture, augmentez le nombre de places occupées              
                this.PlaceDisponible(car);
		infoVoitures.add(car); 
		System.out.format("[Parking] :%s acceptee, il reste %d places \n", car.nom,places());
		System.out.println(infoVoitures);
		return (true) ; 
	    }else{
                System.out.format("Parking : %s refusee, il reste  %d places \n", car.nom,places());
                MoveVoiture(car,car.posx,car.posy);
                    return(false);
	   }
    }
// Fonction permet à les voitures du quitter le parking    
    void Quitter(Voiture car) {
// Décrémenter le nombre des places occupées dans le parking	 
         PlacesOccupees --; 
	 infoVoitures.remove(car);
	 System.out.format("Parking :[%s] est sortie, reste  %d places and posY = %d\n", car.nom, places(),car.posy);           
        switch (car.posy) {
            case 10 :
                    Place[0]=false;            
                   break;
            case 160:
                    Place[1]=false;          
                       break;
            case 330:
                    Place[2]=false;
                   break;            
            case 490:
                    Place[3]=false;
                      break;
             default :
                   Place[0]=false;                 
                 break;
        }
          this.moveRoundOutside(car,car.posx,car.posy);
    }
 
    // faire le tour dans le  parking
    public void MoveVoiture(Voiture car,int x,int y){        
           while(y > 70){
                y-=10;
                car.setLocation(x,y);
          try { 
                Thread.sleep(40);  
            } catch (InterruptedException ex) {
                Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
              if(car.side =="l"){               
                while(y > 0){
                  y-=10;
                  car.setLocation(x,y);
                  try { 
                        Thread.sleep(40);  
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
                    }
                  }                 
           }
            car.changeImage("images/c1.png"); // à droite
            car.setBounds(x, y, 150,141);
           while(x < 640){ 
            x+=10;
            car.setLocation(x,y);
            try { 
                Thread.sleep(40);  
            } catch (InterruptedException ex) {
                Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
          if(car.side == "l"){  
            x+=70;
            car.setLocation(x,y);
        }
        car.changeImage("images/c2.png");  // vers le bas
        while(y < 690){ 
            y += 10;
            car.setLocation(x,y);
             try { 
                Thread.sleep(40);  
            } catch (InterruptedException ex) {
                Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        car.changeImage("images/c4.png"); // à gauche
        car.setBounds(x, y, 150,141);        
        while(x > car.positionX){               
            x-=10;
            car.setLocation(x,y);
            try { 
                Thread.sleep(40);  
            }catch (InterruptedException ex) {
                Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
        car.changeImage("images/c3.png"); // vers le haut
        while(y > car.positionY){
            y -= 10;
            car.setLocation(x,y);
             try { 
                Thread.sleep(40);  
            } catch (InterruptedException ex) {
                Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        car.posx = x;
        car.posy = y;
    
    }
       
//    Fonction pour Définir A Background dans notre Panel
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        image = new ImageIcon("images/park1.png");
        image.paintIcon(this, g, WIDTH,WIDTH);
    }
// Vérifier si les les places sont vide
    public void PlaceDisponible(Voiture car) {
// Vérifie si la 1ér Place est vide 
            if(!Place[0]){
                Place[0] = true;
                this.moveDirect(car,car.posx,car.posy);
            }
// Vérifie si la 2éme Place est vide 
            else if(!Place[1]){
                Place[1] = true;   
                this.moveRoundInside(car, car.posx,car.posy,160);            
            }
// Vérifie si la 3éme Place est vide                 
            else if(!Place[2]){
                Place[2] = true;
                this.moveRoundInside(car, car.posx,car.posy,330);             
            }
// Vérifie si la 4éme Place est vide   
            else if(!Place[3]){
                 Place[3] = true;
                 this.moveRoundInside(car, car.posx,car.posy,490);              
            }else{               
                System.out.println("Tous Les Places ne sont pas Disponible !");
            }           
    }
    
    public void moveDirect(Voiture car,int x,int y){            
            while(y > 60){
                y-=10;
                car.setLocation(x,y);
                try { 
                      Thread.sleep(50);  
                  } catch (InterruptedException ex) {
                      Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
//            Up To Top
           if(car.side == "l"){               
              while(y > 0){
                    y-=10;
                    car.setLocation(x,y);
                    try { 
                          Thread.sleep(50);  
                        }catch (InterruptedException ex) {
                          Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
                        }
               }                 
           }
            car.changeImage("images/c1.png"); // à droite
            car.setBounds(x, y, 150,141);
          
            while(x < 975){  //  Voiture dans la 1ér place                   
                x+=10;
                car.setLocation(x,y);
            try { 
                  Thread.sleep(50);  
              } catch (InterruptedException ex) {
                  Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
              }
            }
            car.posx = x;
            car.posy = y;      
    }
    public void moveRoundInside(Voiture car,int x,int y,int Distance){
          while(y > 70){                
                y-=10;
                car.setLocation(x,y);
            try { 
                  Thread.sleep(50);  
              } catch (InterruptedException ex) {
                  Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
              }
            }
          //            Up To Top
              if(car.side =="l"){              
               while(y > 0){
                    y-=10;
                    car.setLocation(x,y);
                    try { 
                          Thread.sleep(50);  
                      } catch (InterruptedException ex) {
                          Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
                      }
                  }                
           }
            car.changeImage("images/c1.png"); // à droite
            car.setBounds(x,y, 150,141);
           while(x < 700){ // mettre la voiture au centre  
                x+=10;
                car.setLocation(x,y);
                try { 
                    Thread.sleep(50);  
                } catch (InterruptedException ex) {
                    Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
                }
        }           
         if(car.side == "l"){  
            x+=70;
            car.setLocation(x,y);
        }
           
        car.changeImage("images/c2.png");  // vers le bas      
        while(y < Distance){          
            y += 10;
            car.setLocation(x,y);
             try { 
                Thread.sleep(50);  
            } catch (InterruptedException ex) {
                Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
            }
        }            
        if(car.side == "r"){            
                   for(Voiture cl:this.allCars){           
              if(y == (int)cl.getLocation().getY()){                  
                  try {
                      Thread.sleep(500); 
                  } catch (InterruptedException ex) {
                      Logger.getLogger(Parking.class.getName()).log(Level.SEVERE, null, ex);
                  }
              }           
       }
      }
        car.changeImage("images/c1.png");
        car.setBounds(x,y ,150,141);    
       while(x < 975){ // voiture au centre                
                x+=10;
                car.setLocation(x,y);
          try { 
                Thread.sleep(50);  
            } catch (InterruptedException ex) {
                Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
            }
        }           
        car.posx = x;
        car.posy = y;   
    }
   

    public void moveRoundOutside(Voiture car, int x, int y) {          

       while(x > 650){  // pour La 1ér place
            x-=8; // la vitesse de mouvement 
            car.setLocation(x,y);
            try { 
                Thread.sleep(50);  
            } catch (InterruptedException ex) {
                Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
            }
        }            
        for(Voiture cl:this.allCars){
           
              if(y == (int)cl.getLocation().getY()){                  
                  try {
                      Thread.sleep(500);
                  } catch (InterruptedException ex) {
                      Logger.getLogger(Parking.class.getName()).log(Level.SEVERE, null, ex);
                  }
              }           
       }
        while(x > 1020){ // Sortir de la place dans le parking
            x-=10;
            car.setLocation(x,y);
            try { 
                Thread.sleep(50);  
            } catch (InterruptedException ex) {
                Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        car.changeImage("images/c2.png"); // Vers la bas
        while( y < 650){           
            y += 10;
            car.setLocation(x,y);
             try { 
                Thread.sleep(50);  
            } catch (InterruptedException ex) {
                Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
            }       
    }
      car.changeImage("images/c4.png");// à droite
      car.setBounds(x,y, 150,141);
       while(x >  car.positionX){
                 x-=10;
                 car.setLocation(x,y);
          try { 
                Thread.sleep(50);  
            } catch (InterruptedException ex) {
                Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
            }
        }           
        car.changeImage("images/c3.png");
        while(y > car.positionY){
                 y-=10;
                 car.setLocation(x,y);
          try { 
                Thread.sleep(50);  
            } catch (InterruptedException ex) {
                Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        car.posx = x;
        car.posy = y;
    }    
}
