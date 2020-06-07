package FishHunt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Poissons extends Entity {

    private boolean regardeDroite;
    private double hauteurMin = (double) 4/5 * FishHunt.HEIGHT;
    private double hauteurMax = (double) 1/5 * FishHunt.HEIGHT;
    private int hauteurRange = (int) (hauteurMax - hauteurMin + 1);

    public Poissons(int level){

        this.largeur = 100;
        this.hauteur = 100;
        this.ax = 0;
        this.y = (int) (Math.random() * hauteurRange + hauteurMin);
        if(Math.random()<0.5){
            this.x = -5- this.largeur;
            this.regardeDroite = true;
        }else{
            this.x = FishHunt.WIDTH + this.largeur;
            this.regardeDroite = false;
        }

    }

    public boolean regardeDroite(){
        return regardeDroite;
    }

    public boolean isOut(){
        if (this.x + largeur > FishHunt.WIDTH){
            if(regardeDroite()) {
                return true;
            }
        }else{
            if(x < 0){
                if(!regardeDroite()){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void draw(GraphicsContext context) {}

}
