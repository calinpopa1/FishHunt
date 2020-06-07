package FishHunt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

public class PoissonNormal extends Poissons {

    private Image[] images;
    private Image poisson;

    public PoissonNormal(int level){
        super(level);
        ay = 100;
        vx = calculVitesse(level);
        if(Math.random()<0.5){
            vy = -100;
        }else{
            vy = -200;
        }
        images = new Image[]{
                new Image("/images/fish/00.png"),
                new Image("/images/fish/01.png"),
                new Image("/images/fish/02.png"),
                new Image("/images/fish/03.png"),
                new Image("/images/fish/04.png"),
                new Image("/images/fish/05.png"),
                new Image("/images/fish/06.png"),
                new Image("/images/fish/07.png")
        };
        int random = (int) Math.floor(Math.random() * 7);
        poisson = images[random];


        int r = (int) (Math.random()*256);
        int g = (int) (Math.random()*256);
        int b = (int) (Math.random()*256);
        color = Color.rgb(r,g,b);
        poisson = ImageHelpers.colorize(poisson,color);

        if(!regardeDroite()){
            poisson = ImageHelpers.flop(poisson);
        }

    }



    public double calculVitesse(int level){
        if(regardeDroite()){
            return 100 * Math.pow(level,(double)(1/3)) + 200;
        }else{
            return -(100 * Math.pow(level,(double)(1/3)) + 200);
        }
    }


    @Override
    public void draw(GraphicsContext context){
        context.drawImage(poisson, this.x, this.y, largeur, hauteur);
    }
}
