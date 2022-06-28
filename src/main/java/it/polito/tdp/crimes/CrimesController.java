/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.db.EventsDao;
import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CrimesController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo...\n");
    	if(boxAnno.getValue()==null||boxCategoria.getValue()==null) {
    		txtResult.appendText("\nSelezionare anno e categoria");
    	}
    	else {
    		model.creaGrafo(boxAnno.getValue(),boxCategoria.getValue());
    		txtResult.appendText("\nGRAFO CREATO! \nVERTICI "+model.nVert()+" ARCHI "+model.nArch());
    		List<Adiacenza> massimi =new ArrayList<>(model.massimi());
    		Collections.sort(massimi);
    		for(Adiacenza a: massimi) {
    			txtResult.appendText("\n"+a.toString());
    		}
    		boxArco.getItems().clear();
    		boxArco.getItems().addAll(massimi);
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso...\n");
    	if(boxAnno.getValue()==null||boxCategoria.getValue()==null) {
    		txtResult.appendText("\nSelezionare anno e categoria");
    	}
    	else if(model.nVert()==0) {
    		txtResult.appendText("\nCrea arco");
    	}
    	else if(boxArco.getValue()==null) {
    		txtResult.appendText("\nSelezionare arco");
    	}
    	else {
    		List<String> cammino=model.cercaCammino(boxArco.getValue());
    		txtResult.appendText("\nCAMMINO TROVATO");
    		for(String s:cammino) {
    			txtResult.appendText("\n"+s);
    			
    		}
    		txtResult.appendText("\nPESO: "+model.getPesoTot());
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	EventsDao dao=new EventsDao();
    	boxCategoria.getItems().addAll(dao.categorie());
    	boxAnno.getItems().addAll(dao.anni());
    }
}
