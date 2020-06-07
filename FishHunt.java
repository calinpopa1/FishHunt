package FishHunt;/*
Fait par **privé** et Calin Popa 
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class FishHunt extends Application {

    public static final int WIDTH =640, HEIGHT = 480;

    public static void main(String[] args){launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception{

        Pane root = new Pane();
        VBox accueilRoot = new VBox();
        VBox scoresRoot = new VBox();
        VBox ajoutRoot = new VBox();
        Scene accueil = new Scene(accueilRoot, WIDTH, HEIGHT);
        Scene sceneJeu = new Scene(root, WIDTH, HEIGHT);
        Scene sceneScores = new Scene(scoresRoot, WIDTH, HEIGHT);
        Scene sceneAjoutScores = new Scene(ajoutRoot, WIDTH, HEIGHT);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        GraphicsContext context = canvas.getGraphicsContext2D();

        Controleur controleur = new Controleur();
        Scorelist scoreList = new Scorelist();
        if(new File("meilleursScores.txt").exists()){
            controleur.startGame();         // Temporaire pour acceder a la methode
            controleur.ajoutScore(scoreList,"none",true);
        }

        // setup de la scene des meilleurs scores ---------------------------

        Text titre = new Text("Meilleurs scores");
        ArrayList<String> scoresTmp = new ArrayList<>(10);
        ListView<String> listTmp = new ListView<>();
        listTmp.getItems().setAll(scoresTmp);
        Button buttonMenuScores = new Button("Menu");

        TextField nouveauScoreTextField = new TextField();
        Text votreNom = new Text("Votre nom: ");
        Text aFaitPointsTmp = new Text("tmp");


        Button buttonAjouter = new Button("Ajouter!");


        HBox hboxNouveauScore = new HBox();
        HBox hboxNouveauScoreTmp = new HBox();

        buttonMenuScores.setOnAction((event) ->
        {

            if(scoresRoot.getChildren().contains(hboxNouveauScore) ||
                    scoresRoot.getChildren().contains(hboxNouveauScoreTmp)){

                scoresRoot.getChildren().remove(2);
            }
            primaryStage.setScene(accueil);

        });

        buttonAjouter.setOnAction((event) ->
        {
            controleur.ajoutScore(scoreList,nouveauScoreTextField.getText(),false);

            primaryStage.setScene(accueil);
        });

        titre.setStyle("-fx-font: 30 arial;");

        scoresRoot.setAlignment(Pos.CENTER);
        scoresRoot.setSpacing(10);
        scoresRoot.setPadding(new Insets(30,25,35,25));

        scoresRoot.getChildren().add(titre);
        scoresRoot.getChildren().add(listTmp);

        hboxNouveauScore.setAlignment(Pos.CENTER);
        hboxNouveauScore.setSpacing(10);

        hboxNouveauScore.getChildren().add(votreNom);
        hboxNouveauScore.getChildren().add(nouveauScoreTextField);
        hboxNouveauScore.getChildren().add(aFaitPointsTmp);
        hboxNouveauScore.getChildren().add(buttonAjouter);

        scoresRoot.getChildren().add(hboxNouveauScoreTmp);


        scoresRoot.getChildren().add(buttonMenuScores);


        // setup de la scene d'accueil -------------------------------------

        Image logoDuJeu = new Image("/images/logo.png");
        ImageView logoAccueil = new ImageView(logoDuJeu);
        Button buttonNouvellePartie = new Button("Nouvelle partie!");
        Button buttonMeilleursScores = new Button("Meilleurs Scores");


        buttonNouvellePartie.setOnAction((event) ->
                {
                    primaryStage.setScene(sceneJeu);
                    controleur.startGame();
                });

        buttonMeilleursScores.setOnAction((event) ->
                {

                    scoresRoot.getChildren().remove(1);
                    ListView<String> list;
                    list = controleur.getList(scoreList);
                    scoresRoot.getChildren().add(1,list);


                    if(!scoresRoot.getChildren().contains(buttonMenuScores)){
                        scoresRoot.getChildren().add(buttonMenuScores);
                    }

                    if(scoresRoot.getChildren().contains(hboxNouveauScoreTmp)
                    || scoresRoot.getChildren().contains(hboxNouveauScore)){
                        scoresRoot.getChildren().remove(2);
                    }

                    primaryStage.setScene(sceneScores);
                });

        accueilRoot.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.DARKBLUE,
                                CornerRadii.EMPTY,
                                Insets.EMPTY)));


        accueilRoot.setAlignment(Pos.CENTER);
        accueilRoot.setSpacing(10);
        accueilRoot.setPadding(new Insets(10));

        accueilRoot.getChildren().add(logoAccueil);
        accueilRoot.getChildren().add(buttonNouvellePartie);
        accueilRoot.getChildren().add(buttonMeilleursScores);



        // setup de la scene du jeu ----------------------------------------

        sceneJeu.setOnKeyPressed((value) -> {
            if (value.getCode() == KeyCode.H) {
                controleur.levelUp();
            }
            if (value.getCode() == KeyCode.J) {
                controleur.scoreUp();
            }
            if (value.getCode() == KeyCode.K) {
                controleur.nbrViesUp();
            }
            if (value.getCode() == KeyCode.L) {
                controleur.gameOver();
            }
        });


        sceneJeu.setOnMouseMoved((event) -> {
           controleur.posCible(event.getX(), event.getY());
        });

        sceneJeu.setOnMouseClicked((MouseEvent) -> {
            if(MouseEvent.getButton() == MouseButton.PRIMARY){
                controleur.tireBalle(MouseEvent.getX(), MouseEvent.getY());
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                if(primaryStage.getScene().equals(sceneJeu)) {
                    double deltaTime = (now - lastTime) * 1e-9;

                    controleur.update(deltaTime);
                    controleur.draw(context);

                    lastTime = now;

                    if(controleur.isGameOver()){


                        int points = controleur.getPoints();


                        if(scoresRoot.getChildren().contains(hboxNouveauScoreTmp) ||
                                scoresRoot.getChildren().contains(hboxNouveauScore)){
                            scoresRoot.getChildren().remove(2);
                        }
                        Text aFaitPoints = new Text("a fait "+points+" points!");
                        hboxNouveauScore.getChildren().remove(2);
                        hboxNouveauScore.getChildren().add(2,aFaitPoints);
                        scoresRoot.getChildren().add(2,hboxNouveauScore);


                        scoresRoot.getChildren().remove(1);
                        ListView<String> list;
                        list = controleur.getList(scoreList);
                        scoresRoot.getChildren().add(1,list);


                        primaryStage.setScene(sceneScores);

                    }
                }
            }
        };
        timer.start();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Fish Hunt");

        primaryStage.setScene(accueil);

        primaryStage.show();
    }

}
