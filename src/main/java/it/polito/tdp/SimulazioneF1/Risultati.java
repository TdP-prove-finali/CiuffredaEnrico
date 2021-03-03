/**
 * Sample Skeleton for 'SceneRisultati.fxml' Controller Class
 */

package it.polito.tdp.SimulazioneF1;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.model.Gara;
import it.polito.tdp.model.Model;
import it.polito.tdp.model.Pilota;
import it.polito.tdp.model.StampaPilota;
import it.polito.tdp.model.StampaScuderia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Risultati {
	private Model model;
	private Stage stage;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxClassifica"
    private ComboBox<String> boxClassifica; // Value injected by FXMLLoader

    @FXML // fx:id="boxGara"
    private ComboBox<Gara> boxGara; // Value injected by FXMLLoader

    @FXML // fx:id="boxClassificaGara"
    private ComboBox<String> boxClassificaGara; // Value injected by FXMLLoader

    @FXML // fx:id="txtClassifica"
    private TextArea txtClassifica; // Value injected by FXMLLoader

    @FXML // fx:id="txtGara"
    private TextArea txtGara; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML
    void nuovaSimulazione(ActionEvent event) throws Exception {
    	CambiaScena.goToSimulazione(stage);
    }
    
    //aggiornamento output quando viene cambiata la gara scelta
    @FXML
    void CambiaClassificaGara(ActionEvent event) {
    	Gara gara=boxGara.getValue();
    	String selezionato=boxClassificaGara.getValue();
    	int i=1;
    	if(selezionato.equals("CLASSIFICA FINALE GARA")) {
    		txtGara.setText("VINCITORE DEL GRAN PREMIO: ");
    		for(Pilota p: model.risultatiGare().get(gara).getClassificaFinale().values()) {
    			if(i>1) {
    			txtGara.appendText(String.valueOf(i)+"°  ");
    			}
    			txtGara.appendText(p.toString()+"\n");
    			i++;
    			}
    		txtGara.appendText("MIGLIOR TEMPO: "+ model.risultatiGare().get(gara).getPoleman().getPilota().toString()+" "+ model.risultatiGare().get(gara).getPoleman().getTempo().toString());
    		}    		  
    	if(selezionato.equals("GRIGLIA DI PARTENZA")) {
    		 {
    	    		txtGara.setText("POLE POSITION: ");
    	    		for(Pilota p: model.risultatiGare().get(gara).getPosizioniIniziali().values()) {
    	    			if(i>1) {
    	    			txtGara.appendText(String.valueOf(i)+"°  ");
    	    			}
    	    			txtGara.appendText(p.toString()+"\n");
    	    			i++;
    	    			}
    	    		}   
    	}
    }
    
    //aggiornamento output quando viene cambiato il tipo di classifica da visualizzare
    @FXML
    void GaraSelezionata(ActionEvent event) {
    	Gara gara=boxGara.getValue();
    	String selezionato=boxClassificaGara.getValue();
    	int i=1;
    	if(selezionato.equals("CLASSIFICA FINALE GARA")) {
    		txtGara.setText("VINCITORE DEL GRAN PREMIO: ");
    		for(Pilota p: model.risultatiGare().get(gara).getClassificaFinale().values()) {
    			if(i>1) {
    			txtGara.appendText(String.valueOf(i)+"°  ");
    			}
    			txtGara.appendText(p.toString()+"\n");
    			i++;
    			}
    		txtGara.appendText("MIGLIOR TEMPO: "+ model.risultatiGare().get(gara).getPoleman().getPilota().toString()+" "+ model.risultatiGare().get(gara).getPoleman().getTempo().toString());
    		}    		  
    	if(selezionato.equals("GRIGLIA DI PARTENZA")) {
    		 {
    	    		txtGara.setText("POLE POSITION: ");
    	    		for(Pilota p: model.risultatiGare().get(gara).getPosizioniIniziali().values()) {
    	    			if(i>1) {
    	    			txtGara.appendText(String.valueOf(i)+"°  ");
    	    			}
    	    			txtGara.appendText(p.toString()+"\n");
    	    			i++;
    	    			}
    	    		}   
    	}
    }
    
    
    //aggiornamento output quando viene cambiata la classifica generale da visualizzare
    @FXML
    void cambiaClassifica(ActionEvent event) {
    	String selezionato=boxClassifica.getValue();
    	if(selezionato.equals("CLASSIFICA PILOTI")) {
    		int i=1;
    		txtClassifica.setText("VINCITORE DEL TITOLO: ");
        		for(StampaPilota p: model.classificaGeneralePilota()) {
        			if(i>1) {
        			txtClassifica.appendText(String.valueOf(i)+"°  ");
        			}
        			txtClassifica.appendText(p.toString()+"\n");
        			i++;
        			}
    	}
    	if(selezionato.equals("CLASSIFICA COSTRUTTORI")) {
    		int i=1;
    		txtClassifica.setText("VINCITORE DEL TITOLO: ");
        		for(StampaScuderia s: model.classificaGeneraleScuderia()) {
        			if(i>1) {
        			txtClassifica.appendText(String.valueOf(i)+"°  ");
        			}
        			txtClassifica.appendText(s.toString()+"\n");
        			i++;
        			}
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxClassifica != null : "fx:id=\"boxClassifica\" was not injected: check your FXML file 'SceneRisultati.fxml'.";
        assert boxGara != null : "fx:id=\"boxGara\" was not injected: check your FXML file 'SceneRisultati.fxml'.";
        assert boxClassificaGara != null : "fx:id=\"boxClassificaGara\" was not injected: check your FXML file 'SceneRisultati.fxml'.";
        assert txtClassifica != null : "fx:id=\"txtClassifica\" was not injected: check your FXML file 'SceneRisultati.fxml'.";
        assert txtGara != null : "fx:id=\"txtGara\" was not injected: check your FXML file 'SceneRisultati.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'SceneRisultati.fxml'.";
    }
    
    //inizializzo i box e stampo la classifica piloti e la classifica della prima gara
    public void setModel(Model model) {
    	this.model=model;
    	boxClassifica.getItems().add("CLASSIFICA PILOTI");
    	boxClassifica.getItems().add("CLASSIFICA COSTRUTTORI");
    	boxClassifica.setValue("CLASSIFICA PILOTI");
		int i=1;
		txtClassifica.setText("VINCITORE DEL TITOLO: ");
    		for(StampaPilota p: model.classificaGeneralePilota()) {
    			if(i>1) {
    			txtClassifica.appendText(String.valueOf(i)+"°  ");
    			}
    			txtClassifica.appendText(p.toString()+"\n");
    			i++;
    			}
    	boxClassificaGara.getItems().add("GRIGLIA DI PARTENZA");
    	boxClassificaGara.getItems().add("CLASSIFICA FINALE GARA");
    	boxClassificaGara.setValue("GRIGLIA DI PARTENZA");
    	for(Gara g: model.risultatiGare().keySet()) {
    		boxGara.getItems().add(g);
    	}
    	boxGara.setValue(boxGara.getItems().get(0));
    	txtGara.setText(model.risultatiGare().get(boxGara.getItems().get(0)).getPosizioniIniziali().toString());
    	i=1; 		  
    	    		txtGara.setText("POLE POSITION: ");
    	    		for(Pilota p: model.risultatiGare().get(boxGara.getItems().get(0)).getPosizioniIniziali().values()) {
    	    			if(i>1) {
    	    			txtGara.appendText(String.valueOf(i)+"°  ");
    	    			}
    	    			txtGara.appendText(p.toString()+"\n");
    	    			i++;
    	    			}
    }
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    @FXML
    void goToRentalsData(ActionEvent event) throws Exception {
    	CambiaScena.goToSimulazione(this.stage);
    }
}
