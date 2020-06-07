package FishHunt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Balle {
    private double posX;
    private double posY;
    private double largeur;
    private double hauteur;
    private double originalX;
    private double originalY;

    public Balle(double x, double y){

        this.largeur = 50;
        this.hauteur = 50;
        this.posX = x - largeur/2;
        this.posY = y - hauteur/2;
        this.originalX = x;
        this.originalY = y;
    }

    public void update(double dt){
        double diminution = dt*300;

            this.largeur -= diminution;
            this.hauteur -= diminution;

        if(posX <= originalX || posY <= originalY ) {
            this.posX += diminution;
            this.posY += diminution;
        }
    }

    public boolean isGone(){
        return (this.largeur<-75 || this.hauteur<-75);
    }

    public boolean testBalleTouche(Poissons other){
        if(this.largeur <= 0 || this.hauteur <= 0){
            if(testCollision(other)){
                return true;
            }
        }
        return false;
    }

    public boolean testCollision(Poissons other){
        double dx = this.posX - other.x;
        double dy = this.posY - other.y;
        double dCarre = dx * dx + dy * dy;
        return dCarre < (this.largeur + other.largeur) * (this.hauteur + other.hauteur);

    }

    public void draw(GraphicsContext context) {

        context.setFill(Color.BLACK);
        context.fillOval(posX, posY, largeur, hauteur);

    }
}
