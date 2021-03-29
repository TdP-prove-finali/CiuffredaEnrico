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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
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

    @FXML
    private CheckBox checkScambio;
    
    @FXML // fx:id="boxPilota1"
    private ComboBox<Pilota> boxPilota1; // Value injected by FXMLLoader

    @FXML // fx:id="boxPilota2"
    private ComboBox<Pilota> boxPilota2; // Value injected by FXMLLoader

    @FXML
    private CheckBox checkInserimento;
    
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

    @FXML
    private CheckBox checkInvestimento;
    
    @FXML // fx:id="boxImporto"
    private TextField txtImporto; // Value injected by FXMLLoader

    @FXML // fx:id="boxScuderia"
    private ComboBox<Scuderia> boxScuderia; // Value injected by FXMLLoader

    @FXML // fx:id="boxPioggia"
    private ComboBox<String> boxPioggia; // Value injected by FXMLLoader

    @FXML
    private TextArea txtErrore;
    
    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader
    
    @FXML
    void AttivaInserisciPilota(ActionEvent event) {
    	boolean selezionato=checkInserimento.isSelected();
    	if(selezionato==true) {
    		txtNome.setDisable(false);
    		txtCognome.setDisable(false);
    		txtNumero.setDisable(false);
    		txtPunteggio.setDisable(false);
    		boxPilotaEliminare.setDisable(false);
    	}
    	if(selezionato==false) {
    		txtNome.setDisable(true);
    		txtCognome.setDisable(true);
    		txtNumero.setDisable(true);
    		txtPunteggio.setDisable(true);
    		boxPilotaEliminare.setDisable(true);
    	}
    }

    @FXML
    void AttivaInvestimento(ActionEvent event) {
    	boolean selezionato=checkInvestimento.isSelected();
    	if(selezionato==true) {
    		txtImporto.setDisable(false);
    		boxScuderia.setDisable(false);
    	}
    	if(selezionato==false) {
    		txtImporto.setDisable(true);
    		boxScuderia.setDisable(true);
    	}

    }

    @FXML
    void AttivaScambio(ActionEvent event) {
    	boolean selezionato=checkScambio.isSelected();
    	if(selezionato==true) {
    		boxPilota1.setDisable(false);
    		boxPilota2.setDisable(false);
    	}
    	if(selezionato==false) {
    		boxPilota1.setDisable(true);
    		boxPilota2.setDisable(true);
    	}
    }
    
    //controllo degli errori e inizio simulazione
	@FXML
    void Simula(ActionEvent event) throws Exception {
    	Integer numeroGare=boxGare.getValue();
    	model.caricaGare(numeroGare);
    	boolean selezionatoscambio=false;
    	boolean selezionatoaggiungi=false;
    	boolean selezionatoinvesti=false;
    	Pilota Pilota1=null;
    	Pilota Pilota2=null;
    	String nome="";
    	String cognome="";
    	Integer numero=0;
    	Integer punteggio=0;
    	Pilota aggiungere=null;
    	Pilota elimina=null;
    	Integer importo=0;
    	Scuderia scuderia=null;
    	
    	//scambioPilota
    	selezionatoscambio=checkScambio.isSelected();
    	if(selezionatoscambio==true) {
    	Pilota1=boxPilota1.getValue();
    	Pilota2=boxPilota2.getValue();  
    	}
    	//AggiungiPilotaEliminaPilota
    	selezionatoaggiungi=checkInserimento.isSelected();
    	if(selezionatoaggiungi==true) {
    	nome=txtNome.getText();
    	cognome=txtCognome.getText();
    	//controllo se il numero è giusto
    	String numeros=txtNumero.getText();
    	numero=0;
		try {
			if(!numeros.equals("")) {
		numero=Integer.parseInt(numeros);
			}
		}
		catch(NumberFormatException e ) {
			txtErrore.setText("errore inserimento numero");
			return;
		}
		//controllo se il valore abilità pilota è giusto
		String punteggios=txtPunteggio.getText();
    	punteggio=0;
		try {
			if(!punteggios.equals("")) {
		punteggio=Integer.parseInt(punteggios);
			}
		}
		catch(NumberFormatException e ) {
			txtErrore.setText("errore inserimento punteggio");
			return;
		}
		if(cognome.equals("") || cognome.matches(".*\\d.*")) {
			txtErrore.setText("inserisci un cognome valido");
			return;
		}
		if(nome.equals("") || nome.matches(".*\\d.*")) {
			txtErrore.setText("inserisci un nome valido");
			return;
		}
		if(numero<1 || numero>99) {
			txtErrore.setText("inserisci un numero compreso tra 1 e 99");
			return;
		}
		boolean numerooccupato=false;
		for(Pilota p: model.getPilotiMap().values())
		{
			if(p.getNumber()==numero && p.getNumber()!=boxPilotaEliminare.getValue().getNumber()) {
				numerooccupato=true;
			}
		}
		if(numerooccupato==true) {
			txtErrore.setText("inserisci un altro numero poichè questo è occupato");
			return;
		}
		if(punteggio<1 || punteggio>99) {
			txtErrore.setText("inserisci un punteggio compreso tra 1 e 99");
			return;
		}
    	elimina=boxPilotaEliminare.getValue();
    	//creo identificativo pilota 
    	if(cognome.length()>=3) {
    		String cognomesub=cognome.replace(" ", "");
        		aggiungere=new Pilota(850,cognome,numero,cognomesub.substring(0, 3).toUpperCase(),cognome,nome,LocalDate.of(1999, 2, 14),"Italian",punteggio,0,0,elimina.getScuderia());
    	}
    	else if((cognome.length()+nome.length())>=3) {
    		String cognomesub=cognome.replace(" ", "");
    		String nomesub=nome.replace(" ", "");
    		Integer cognomelenght=cognome.length();
    		String codice=cognomesub.substring(0, cognomelenght).toUpperCase();
    		codice=codice+nomesub.substring(0, 3-cognomelenght).toUpperCase();
    		aggiungere=new Pilota(850,cognome,numero,codice,cognome,nome,LocalDate.of(1999, 2, 14),"Italian",punteggio,0,0,elimina.getScuderia());
	    }
    	else if((cognome.length()+nome.length())<3) {
    		String codice=cognome+nome+"1";
    	    aggiungere=new Pilota(850,cognome,numero,codice,cognome,nome,LocalDate.of(1999, 2, 14),"Italian",punteggio,0,0,elimina.getScuderia());
	    }
    	}
    	//miglioraMacchina
    	selezionatoinvesti=checkInvestimento.isSelected();
    	if(selezionatoinvesti==true) {
    	String importos=txtImporto.getText();
    	importo=0;
		try {
			if(!importos.equals("")) {
		importo=Integer.parseInt(importos);
			}
		}
		catch(NumberFormatException e ) {
			txtErrore.setText("errore inserimento importo");
			return;
		}
		if(importo<0 || importo>999)
		{
			txtErrore.setText("inserisci un importo compreso tra 0 e 999");
			return;
		}
		scuderia=boxScuderia.getValue();
    	}
    	//Scambiare i piloti
		if(selezionatoscambio==true) {
	  		model.Scambio(Pilota1,Pilota2);
		}
    	//Aggiungi i piloti
		if(selezionatoaggiungi==true) {
	    	model.AggiungiElimina(aggiungere, elimina);
		}
		//Investo nella scuderia
		if(selezionatoinvesti==true) {
			model.MiglioraScuderia(scuderia, importo);	
		}
    	model.iniziaSimulazione(boxPioggia.getValue());
    	CambiaScena.goToRisultati(stage, model);

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxGare != null : "fx:id=\"boxGare\" was not injected: check your FXML file 'Scene.fxml'.";
        assert checkScambio != null : "fx:id=\"checkScambio\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxPilota1 != null : "fx:id=\"boxPilota1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxPilota2 != null : "fx:id=\"boxPilota2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert checkInserimento != null : "fx:id=\"checkInserimento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNome != null : "fx:id=\"txtNome\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCognome != null : "fx:id=\"txtCognome\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNumero != null : "fx:id=\"txtNumero\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtPunteggio != null : "fx:id=\"txtPunteggio\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxPilotaEliminare != null : "fx:id=\"boxPilotaEliminare\" was not injected: check your FXML file 'Scene.fxml'.";
        assert checkInvestimento != null : "fx:id=\"checkInvestimento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtImporto != null : "fx:id=\"boxImporto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxScuderia != null : "fx:id=\"boxScuderia\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxPioggia != null : "fx:id=\"boxPioggia\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtErrore != null : "fx:id=\"txtErrore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    //inizializzo i box
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
    	boxPilota1.setValue(boxPilota1.getItems().get(0));
    	boxPilota2.setValue(boxPilota2.getItems().get(1));
    	boxPilotaEliminare.setValue(boxPilotaEliminare.getItems().get(0));
    	boxScuderia.setValue(boxScuderia.getItems().get(8));
    }
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    @FXML
    void goToRentalsData(ActionEvent event) throws Exception {
    	CambiaScena.goToRisultati(this.stage, this.model);
    }
}
