package FishHunt;

import javafx.scene.control.ListView;

import java.util.ArrayList;

public class Scorelist {

    private ListView<String> list;
    private ArrayList<String> score;

    public Scorelist(){
        this.score = new ArrayList<>(10);
        this.list  = new ListView<>();
        list.getItems().setAll(score);
    }

    public ListView<String> getList(){
        return this.list;
    }

    public void setList(ListView<String> list){
        this.list = list;
    }
}
