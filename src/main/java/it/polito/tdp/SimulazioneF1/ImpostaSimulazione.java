/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.SimulazioneF1;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import it.polito.tdp.model.Gara;
import it.polito.tdp.model.Model;
import it.polito.tdp.model.Pilota;
import it.polito.tdp.model.Scuderia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ImpostaSimulazione {
	private Model model;
	private Stage stage;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxGare"
    private ComboBox<Integer> boxGare; // Value injected by FXMLLoader

    @FXML // fx:id="boxPilota1"
    private ComboBox<Pilota> boxPilota1; // Value injected by FXMLLoader

    @FXML // fx:id="boxPilota2"
    private ComboBox<Pilota> boxPilota2; // Value injected by FXMLLoader

    @FXML // fx:id="txtNome"
    private TextField txtNome; // Value injected by FXMLLoader

    @FXML // fx:id="txtCognome"
    private TextField txtCognome; // Value injected by FXMLLoader

    @FXML // fx:id="txtNumero"
    private TextField txtNumero; // Value injected by FXMLLoader

    @FXML // fx:id="txtPunteggio"
    private TextField txtPunteggio; // Value injected by FXMLLoader

    @FXML // fx:id="boxPilotaEliminare"
    private ComboBox<Pilota> boxPilotaEliminare; // Value injected by FXMLLoader

    @FXML // fx:id="boxImporto"
    private TextField txtImporto; // Value injected by FXMLLoader

    @FXML // fx:id="boxScuderia"
    private ComboBox<Scuderia> boxScuderia; // Value injected by FXMLLoader

    @FXML // fx:id="boxPioggia"
    private ComboBox<String> boxPioggia; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader
    
    
	@FXML
    void Simula(ActionEvent event) throws Exception {
    	Integer numeroGare=boxGare.getValue();
    	model.caricaGare(numeroGare);
    	//scambioPilota
    	Pilota Pilota1=boxPilota1.getValue();
    	Pilota Pilota2=boxPilota2.getValue();
    	if(Pilota1==null && Pilota2==null) {
    	}
    	else if(Pilota1==null && Pilota2!=null) {
    		System.out.println("Non hai scelto un pilota da scambiare");
    		return;
    	}
    	else if(Pilota2==null && Pilota1!=null) {
    		System.out.println("Non hai scelto un pilota da scambiare");
    		return;
    	}
    	else if(!Pilota1.equals(Pilota2)) {
    		model.Scambio(Pilota1,Pilota2);
    	}
    	//AggiungiPilotaEliminaPilota
    	String nome=txtNome.getText();
    	String cognome=txtCognome.getText();
    	String numeros=txtNumero.getText();
    	Integer numero=0;
		try {
			if(!numeros.equals("")) {
		numero=Integer.parseInt(numeros);
			}
		}
		catch(NumberFormatException e ) {
			System.out.println("errore inserimento numero");
			return;
		}
		String punteggios=txtNumero.getText();
    	Integer punteggio=0;
		try {
			if(!punteggios.equals("")) {
		punteggio=Integer.parseInt(punteggios);
			}
		}
		catch(NumberFormatException e ) {
			System.out.println("errore inserimento punteggio");
			return;
		}
    	Pilota elimina=boxPilotaEliminare.getValue();
    	
    	if (elimina==null || cognome.equals("") || nome.equals("") || numero==0 || punteggio==0) {
    	
    	}
    	else {
        Pilota aggiungere=null;
    	if(cognome.length()>=3) {
    		String cognomesub=cognome.replace(" ", "");
        		aggiungere=new Pilota(850,cognome,numero,cognomesub.substring(0, 3).toUpperCase(),cognome,nome,LocalDate.of(1999, 2, 14),"Italian",punteggio,0,0,elimina.getScuderia());
    	}
    	if((cognome.length()+nome.length())>=3) {
    		String cognomesub=cognome.replace(" ", "");
    		String nomesub=nome.replace(" ", "");
    		Integer cognomelenght=cognome.length();
    		String codice=cognomesub.substring(0, cognomelenght).toUpperCase();
    		codice=codice+nomesub.substring(0, 3-cognomelenght).toUpperCase();
    		aggiungere=new Pilota(850,cognome,numero,codice,cognome,nome,LocalDate.of(1999, 2, 14),"Italian",punteggio,0,0,elimina.getScuderia());
	    }
    	if((cognome.length()+nome.length())<3) {
    		String codice=cognome+nome+"1";
    	    aggiungere=new Pilota(850,cognome,numero,codice,cognome,nome,LocalDate.of(1999, 2, 14),"Italian",punteggio,0,0,elimina.getScuderia());
	    }
    	model.AggiungiElimina(aggiungere, elimina);
    	}
    	//miglioraMacchina
    	String importos=txtImporto.getText();
    	Integer importo=0;
		try {
			if(!importos.equals("")) {
		importo=Integer.parseInt(importos);
			}
		}
		catch(NumberFormatException e ) {
			System.out.println("errore inserimento importo");
			return;
		}
		Scuderia scuderia=boxScuderia.getValue();
		if(scuderia==null) {
			
		}
		else {
			model.MiglioraScuderia(scuderia, importo);
		}
    	model.iniziaSimulazione();
    	CambiaScena.goToRisultati(stage, model);
    	//System.out.println(model.risultatiGare());
    	System.out.println(model.classificaGeneralePilota());
    	System.out.println(model.classificaGeneraleScuderia());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxGare != null : "fx:id=\"boxGare\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxPilota1 != null : "fx:id=\"boxPilota1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxPilota2 != null : "fx:id=\"boxPilota2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNome != null : "fx:id=\"txtNome\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCognome != null : "fx:id=\"txtCognome\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNumero != null : "fx:id=\"txtNumero\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtPunteggio != null : "fx:id=\"txtPunteggio\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxPilotaEliminare != null : "fx:id=\"boxPilotaEliminare\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtImporto != null : "fx:id=\"boxImporto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxScuderia != null : "fx:id=\"boxScuderia\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxPioggia != null : "fx:id=\"boxPioggia\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model=model;
    	boxPioggia.getItems().add("NO");
    	boxPioggia.getItems().add("SI");
    	boxPioggia.setValue("NO");
    	for(int i=10;i<25;i++) {
    		boxGare.getItems().add(i);
    	}
    	boxGare.setValue(24);
    	boxPilota1.getItems().addAll(model.getPilotiMap().values());
    	boxPilota2.getItems().addAll(model.getPilotiMap().values());
    	boxPilotaEliminare.getItems().addAll(model.getPilotiMap().values());
    	boxScuderia.getItems().addAll(model.getScuderieMap().values());
    	boxPilota1.setPromptText("NESSUNO SCAMBIO");
    	boxPilota1.setValue(null);
    	boxPilota2.setPromptText("NESSUNO SCAMBIO");
    	boxPilota2.setValue(null);
    	boxPilotaEliminare.setPromptText("NON AGGIUNGO PILOTA");
    	boxPilotaEliminare.setValue(null);
    	boxScuderia.setPromptText("NON MIGLIORO SCUDERIA");
    	boxScuderia.setValue(null);
    }
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    @FXML
    void goToRentalsData(ActionEvent event) throws Exception {
    	CambiaScena.goToRisultati(this.stage, this.model);
    }
}
