package FishHunt;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

public class Jeu {
    public static final int WIDTH =640, HEIGHT = 480;
    private int scoreCapture;
    private int scoreCaptureLevel;
    private int nbrVies;
    private int level;

    private boolean showLevel;
    private boolean retourScores;
    private boolean premierScore;

    private ArrayList<Poissons> poissons;
    private ArrayList<Balle> balle;
    private ArrayList<Bulles> bulles;
    private Cible cible;

    private Image score;

    private FileWriter fw;
    private ArrayList<String> scores;
    private ArrayList<String> scoresTrie = new ArrayList<>();
    private ListView<String> listJeu = new ListView<>();

    private Timer timerJeu;
    private Timer timerLevel;

    private File file;


    public Jeu() {
        cible = new Cible(Math.floor(WIDTH/2), Math.floor(HEIGHT/2));
        poissons = new ArrayList<>();
        balle = new ArrayList<>();
        bulles = new ArrayList<>();
        file = new File("meilleursScores.txt");
        showLevel = true;
        retourScores = false;
        scoreCapture = 0;
        scoreCaptureLevel = 0;
        nbrVies = 3;
        level = 1;

        score = new Image("/images/fish/00.png");

        timerJeu = new Timer();
        timerLevel = new Timer();

        afficheLevel();
        startNouveauPoissonNorm(level);
        startNouvellesBulles();

    }

    public void gameOver(){
        nbrVies = 0;
        TimerTask changeDeScene = new TimerTask(){
            @Override
            public void run(){
                retourScores = true;
            }
        };
        timerJeu.schedule(changeDeScene, 3000);
    }

    public void nouveauScore(Scorelist scoreList, String nom, boolean download){

        try {
            scores = new ArrayList<>();
            if(!file.exists()) {

                fw = new FileWriter(file);
                premierScore = true;
                BufferedWriter writer = new BufferedWriter(fw);
                String out;
                out = ""+scoreCapture+" "+nom;
                writer.append(out);
                writer.close();
            }


            if(!premierScore) {
                fw = new FileWriter(file,true);
                FileReader fr = new FileReader(file);
                BufferedReader reader = new BufferedReader(fr);
                BufferedWriter writer = new BufferedWriter(fw);
                String out;
                if(download){
                    writer.close();
                }else {
                    out = "" + "\n" + scoreCapture + " " + nom;
                    writer.append(out);
                    writer.close();
                }

                String in;
                while((in = reader.readLine()) != null) {
                    scores.add(in);
                }

                ArrayList<String> scoresNum = new ArrayList<>();
                String concaTmp = "";
                for(int i=0;i< scores.size();i++){
                    scoresNum.add("");
                    for(int j=0;j<scores.get(i).length();j++){
                        if(scores.get(i).charAt(j) == ' '){
                            break;
                        }
                        concaTmp = scoresNum.get(i);
                        scoresNum.set(i, ""+concaTmp+scores.get(i).charAt(j));
                        concaTmp = "";
                    }
                }

                Collections.sort(scoresNum, Collections.reverseOrder());
                for(int t=0; t<scoresNum.size();t++){
                    if((t+1) != scoresNum.size()){
                        if(scoresNum.get(t+1).length() > scoresNum.get(t).length()){
                            String tempString = scoresNum.get(t);
                            scoresNum.set(t,scoresNum.get(t+1));
                            scoresNum.set(t+1,tempString);
                            tempString = "";
                        }
                    }
                }

                String scoreTmp = "";
                for(int s =0; s< scoresNum.size(); s++){
                    for(int n =0; n<scores.size();n++) {

                        for(int b =0; b<scores.get(n).length();b++){
                            if((scores.get(n).charAt(b) == ' ')){
                                break;
                            }else{
                                scoreTmp += scores.get(n).charAt(b);
                            }
                        }

                        if(Integer.valueOf(scoreTmp) == Integer.valueOf(scoresNum.get(s))){
                            String tempString = scores.get(s);
                            scores.set(s,scores.get(n));
                            scores.set(n,tempString);
                            scoreTmp = "";
                            break;
                        }
                        scoreTmp = "";

                    }
                    if(s == 9){
                        break;
                    }
                }
                for(int f =0; f< scores.size();f++){
                    scoresTrie.add(scores.get(f));
                    if(f == 9){
                        break;
                    }
                }
                scores.clear();

            }



            String nomTrouve ="";
            String scoreTrouve ="";
            if(premierScore){
                scores.add("#1 - "+nom+" - "+scoreCapture);
            }else{
                for(int z =0;z<scoresTrie.size();z++){
                    scores.add("");
                    for(int k = 0; k<scoresTrie.get(z).length();k++){
                        if(scoresTrie.get(z).charAt(k) == ' '){
                            for(int h = k+1;h<scoresTrie.get(z).length();h++){
                                nomTrouve += scoresTrie.get(z).charAt(h);
                            }
                            break;
                        }
                        scoreTrouve += scoresTrie.get(z).charAt(k);
                    }
                    scores.set(z,"#"+(z+1)+" - "+nomTrouve+" - "+scoreTrouve);
                    nomTrouve = "";
                    scoreTrouve = "";
                }
                if(scores.size()<10){
                    for(int w = 0;w< 10 - scores.size();w++){
                        scores.add("");
                    }
                }
                scoresTrie.clear();
            }

            listJeu.getItems().setAll(scores);
            scoreList.setList(listJeu);

            premierScore = false;
            scores.clear();
        } catch (IOException e) {
            System.out.println("Erreur à l’écriture du fichier");
        }

    }


    public boolean isGameOver(){
        if(retourScores){
            retourScores = false;
            return true;
        }else{
            return false;
        }
    }

    public int getPoints(){
        return scoreCapture;
    }

    public void afficheLevel(){
        TimerTask setShowLevelFalse = new TimerTask(){
            @Override
            public void run(){
                showLevel = false;
            }
        };
        timerLevel.schedule(setShowLevelFalse, 3000);
    }

    /**
     *
     * @param level niveau actuel
     */
    public void startNouveauPoissonNorm(int level){
        TimerTask nouveauPoissonNorm = new TimerTask(){
            @Override
            public void run(){
                poissons.add(new PoissonNormal(level));
            }
        };
        timerJeu.schedule(nouveauPoissonNorm, 3000, 3000);
    }

    /**
     * Commence la création des poissons spéciaux (crabes et étoiles)
     * @param level niveau actuel
     */
    public void startNouveauPoissonSpec(int level){
            TimerTask nouveauPoissonSpec = new TimerTask() {
                @Override
                public void run() {
                    double randomSpec = Math.random();
                    if(randomSpec<0.5) {
                        PoissonCrabe crabe = new PoissonCrabe(level);
                        poissons.add(crabe);
                        crabeMarche(crabe);
                    }else{
                        poissons.add(new PoissonEtoile(level));
                    }
                }
            };
            timerJeu.schedule(nouveauPoissonSpec, 3000, 5000);
    }

    public void startNouvellesBulles(){
        TimerTask nouvelleBulle = new TimerTask() {
            @Override
            public void run() {

                double baseX;
                double baseXInterval;
                for(int b =0;b<3;b++){
                    baseX = (Math.random()*WIDTH);
                    for(int p =0;p<5;p++){
                        baseXInterval = (Math.random()*40) + baseX-20;
                        bulles.add(new Bulles(baseXInterval));
                    }
                }
            }
        };
        timerJeu.schedule(nouvelleBulle, 0, 3000);
    }

    /**
     * Contrôle les crabes
     * @param crabe crabe qui sera controlé pour avancer et reculer
     */
    public void crabeMarche(PoissonCrabe crabe){
        Timer timerCrabe = new Timer();
        TimerTask crabeRecule = new TimerTask(){
            @Override
            public void run() {
                crabe.inverseDirection();
            }
        };
        TimerTask crabeAvance = new TimerTask(){
            @Override
            public void run() {
                crabe.inverseDirection();
            }
        };
        timerCrabe.schedule(crabeRecule,500,750);
        timerCrabe.schedule(crabeAvance, 750, 750);

    }

    public void levelUp(){
        level++;
    }

    public void scoreUp(){
        scoreCapture++;
        scoreCaptureLevel++;
    }

    public void nbrViesUp(){
        if(nbrVies < 3) {
            nbrVies++;
        }
    }

    public void nbrViesDown(){
        nbrVies--;
    }

    /**
     * Trouve la cible
     * @param x position en x
     * @param y position en y
     */
    public void posCible(double x, double y){
        cible.posCible(x,y);
    }

    /**
     * Créer une balle
     * @param x position en x
     * @param y position en y
     */
    public void tireBalle(double x, double y){
        balle.add(new Balle(x,y));
    }

    /**
     * À chaque tour, on recalcule si une balle touche un poisson ou non,
     * si un poisson est sorti de l'ecran, si des bulles sont sorties de
     * l'ecran, si on a augmenter de niveau et si le nombre de vies est
     * tombée a 0 ou non
     */
    public void update(double dt){

        if(!balle.isEmpty()){
            ArrayList<Integer> ballesARetirer = new ArrayList<Integer>();
            for (int f = 0; f < balle.size(); f++) {
                balle.get(f).update(dt);

                if(balle.get(f).isGone()){
                    ballesARetirer.add(f);
                }

            }
            for(int r=0;r< ballesARetirer.size();r++){
                balle.remove((int)ballesARetirer.get(r));
            }
            ballesARetirer.clear();
        }

        if(!poissons.isEmpty()) {
            ArrayList<Integer> poissonsARetirer = new ArrayList<Integer>();
            for (int p = 0; p < poissons.size(); p++) {
                poissons.get(p).update(dt);
                if (poissons.get(p).isOut()) {
                    nbrViesDown();
                    poissonsARetirer.add(p);
                }
            }
            for(int r=0;r< poissonsARetirer.size();r++){
                poissons.remove((int)poissonsARetirer.get(r));
            }
            poissonsARetirer.clear();
        }

        if(!poissons.isEmpty()) {
            ArrayList<Integer> poissonsARetirer = new ArrayList<Integer>();
            for (int p = 0; p < poissons.size(); p++) {
                for (int b = 0; b < balle.size(); b++) {
                    if (balle.get(b).testBalleTouche(poissons.get(p))) {
                        scoreUp();
                        poissonsARetirer.add(p);
                        continue;
                    }
                }
            }
            for(int r=0;r< poissonsARetirer.size();r++){
                poissons.remove((int)poissonsARetirer.get(r));
            }
            poissonsARetirer.clear();

        }

        if(!bulles.isEmpty()){
            ArrayList<Integer> bullesARetirer = new ArrayList<Integer>();
            for (int f = 0; f < bulles.size(); f++) {
                bulles.get(f).update(dt);

                if(bulles.get(f).isOut()){
                    bullesARetirer.add(f);
                }

            }
            for(int r=0;r< (bullesARetirer.size()/2);r++){
                bulles.remove((int)bullesARetirer.get(r));
            }
            bullesARetirer.clear();
        }


        if(nbrVies == 0){
            gameOver();
        }

        if(scoreCaptureLevel == 5){
            showLevel = true;
            scoreCaptureLevel -= 5;
            levelUp();
            timerJeu.cancel();
            timerJeu = new Timer();
            afficheLevel();
            startNouveauPoissonNorm(level);
            startNouveauPoissonSpec(level);
            startNouvellesBulles();
        }

    }

    /**
     * S'occupe de dessiner tout ce qu'il y a dans la scene de jeu
     * @param context
     */
    public void draw(GraphicsContext context){
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);
        if(!bulles.isEmpty()) {
            for (int j = 0; j < bulles.size(); j++) {
                bulles.get(j).draw(context);
            }
        }

        if(!poissons.isEmpty()) {
            for (int i = 0; i < poissons.size(); i++) {
                poissons.get(i).draw(context);
            }
        }

        if(!balle.isEmpty()) {
            for (int j = 0; j < balle.size(); j++) {
                balle.get(j).draw(context);
            }
        }

        if(showLevel){
            context.setFill(Color.WHITE);
            context.setFont(new Font(""+Font.getDefault(), 50));
            context.fillText("Level "+level,Math.floor(WIDTH/2 +10) - 80,Math.floor(HEIGHT/2));
        }

        context.setFont(new Font(""+Font.getDefault(), 30));
        context.setFill(Color.WHITE);
        context.fillText(""+scoreCapture,320,50);

        double iconVieW = 30;
        double iconVieH = 30;
        double iconVieY = 80;

        switch(nbrVies){
            case 3:
                context.drawImage(score, Math.floor(WIDTH / 2 +10) - iconVieW/2 - 45, iconVieY, iconVieW, iconVieH);
                context.drawImage(score, Math.floor(WIDTH / 2 +10)- iconVieH/2, iconVieY, iconVieW, iconVieH);
                context.drawImage(score, Math.floor(WIDTH / 2 +10)- iconVieH/2 + 45, iconVieY, iconVieH, iconVieH);
                break;

            case 2:
                context.drawImage(score, Math.floor(WIDTH / 2 +10) - iconVieH/2 - 45, iconVieY, iconVieH, iconVieH);
                context.drawImage(score, Math.floor(WIDTH / 2 +10) - iconVieH/2, iconVieY, iconVieH, iconVieH);
                break;

            case 1:
                context.drawImage(score, Math.floor(WIDTH / 2 +10) - iconVieH - 45, iconVieY, iconVieH, iconVieH);
                break;

            case 0:
                context.setFill(Color.RED);
                context.setFont(new Font(""+Font.getDefault(), 60));
                context.fillText("GAME OVER",Math.floor(WIDTH/3) - 40,Math.floor(HEIGHT/2));
                break;

            default: break;
        }

        cible.draw(context);
    }
}
