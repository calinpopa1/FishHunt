package FishHunt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Entity {

    protected double largeur, hauteur;
    protected double x, y;
    protected double vx, vy;
    protected double ax, ay;
    protected Color color;

    /**
     * Met à jour la position et la vitesse du Entity et s'assure qu'il
     * demeure dans l'écran
     * @param dt Temps écoulé depuis la dernière mise à jour en secondes
     */
    public void update(double dt) {
        vx += dt * ax;
        vy += dt * ay;
        x += dt * vx;
        y += dt * vy;

        /*
        // Force à rester dans les bornes des cotés de l'écran
        if (x + largeur > FishHunt.WIDTH || x < 0) {
            vx *= -0.9;
        }


        x = Math.min(x, FishHunt.WIDTH - largeur);
        x = Math.max(x, 0);
        y = Math.min(y, FishHunt.HEIGHT - hauteur);
        y = Math.max(y, 0);
        */

    }


    public abstract void draw(GraphicsContext context);


}
