package FishHunt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;


public class Controleur {

    Jeu jeu;
    Scorelist list;

    public Controleur(){}

    public void draw(GraphicsContext context){ jeu.draw(context); }

    public void startGame(){ jeu = new Jeu(); }

    public void gameOver(){jeu.gameOver();}

    public void levelUp(){ jeu.levelUp();}

    public void scoreUp(){ jeu.scoreUp();}

    public void nbrViesUp(){ jeu.nbrViesUp();}

    public void posCible(double x, double y){ jeu.posCible(x,y);}

    public void tireBalle(double x, double y){ jeu.tireBalle(x,y);}

    public void update(double deltaTime){ jeu.update(deltaTime); }

    public boolean isGameOver(){ return jeu.isGameOver();}

    public int getPoints(){ return jeu.getPoints();}

    public ListView<String> getList(Scorelist list) { return list.getList(); }

    public void ajoutScore(Scorelist list, String nom, boolean download){jeu.nouveauScore(list, nom, download);}


}
