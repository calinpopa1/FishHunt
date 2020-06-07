package FishHunt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bulles extends Entity{

    private double r;

    public Bulles(double x){

        this.x = x;
        this.y = FishHunt.HEIGHT + 50;
        this.r = (Math.random()*30)+10;
        vy = -((Math.random()*100)+350);
    }

    public boolean isOut(){
        if(this.y<-50) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(Color.rgb(0,0,255,0.4));
        context.fillOval(this.x,this.y,this.r,this.r);
    }
}
