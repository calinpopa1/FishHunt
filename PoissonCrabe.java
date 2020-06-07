package FishHunt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PoissonCrabe extends Poissons {

    private Image crabe;

    public PoissonCrabe(int level){
        super(level);
        this.ay = 0;
        vx = 1.3 * calculVitesse(level);

        crabe = new Image("/images/crabe.png");
    }

    public double calculVitesse(int level){
        if(regardeDroite()){
            return 100 * Math.pow(level,(double)(1/3)) + 200;
        }else{
            return -(100 * Math.pow(level,(double)(1/3)) + 200);
        }
    }

    public void inverseDirection(){
            vx = -vx;
    }

    @Override
    public void draw(GraphicsContext context){
        context.drawImage(crabe, this.x, this.y, largeur, hauteur);
    }
}
