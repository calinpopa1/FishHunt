package FishHunt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PoissonEtoile extends Poissons {

    private Image etoile;

    public PoissonEtoile(int level){
        super(level);
        this.ay = 0;

        vx = calculVitesse(level);
        etoile = new Image("/images/star.png");
    }

    /**
     *
     * @param level niveau actuel
     * @return vitesse calculée à partir du niveau
     */
    public double calculVitesse(int level){
        if(regardeDroite()){
            return 100 * Math.pow(level,(double)(1/3)) + 200;
        }else{
            return -(100 * Math.pow(level,(double)(1/3)) + 200);
        }
    }

    public void update(double dt){
        super.update(dt);
        this.y = Math.floor(50*Math.sin(this.x*dt));
    }

    @Override
    public void draw(GraphicsContext context){

        context.drawImage(etoile, this.x, this.y, largeur, hauteur);
    }
}
