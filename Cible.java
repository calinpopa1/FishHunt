package FishHunt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Cible {

    private double posX;
    private double posY;
    private Image imageCible;
    private double largeur;
    private double hauteur;

    public Cible(double posX, double posY){
        this.posX = posX;
        this.posY = posY;
        this.largeur = 50;
        this.hauteur = 50;
        imageCible = new Image("/images/cible.png");
    }

    public void posCible(double x, double y){
        this.posX = x - largeur/2;
        this.posY = y - hauteur/2;
    }


    public void draw(GraphicsContext context) {

        context.drawImage(imageCible, posX, posY, largeur, hauteur);
    }
}
