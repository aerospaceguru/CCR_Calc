package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class Controller {
	
    double rho_cu = 1.72e-8;
    double rho_al = 2.65e-8;
    double rho = 0;
    double csa1;
    double csa2;
    double efficiency = 0.85;
    double powerFactor = 0.97;
	double secondaryLossesPerM;
	double secondaryLosses;
	double primaryLosses;
	double lampPlusSecondaryLoad;
    double ccrLoad;
    double voltage;
    double ccrSize = 0;
    double utilisation;

	
	public TextField lr1;
	public TextField lr2;
	public TextField lr3;
	public TextField qty1;
	public TextField qty2;
	public TextField qty3;
	
	public TextField tplBox;
	public TextField tslBox;
	
	public ComboBox<String> currentDrop;
	public ObservableList<String> currentList = FXCollections.observableArrayList("6.6", "12");
	
	public ComboBox<String> cableDrop;
	public ObservableList<String> cableList = FXCollections.observableArrayList("Copper", "Aluminium");
	
	public ComboBox<String> primarySizeDrop;
	public ObservableList<String> primarySizeList = FXCollections.observableArrayList("6", "8");
	
	public ComboBox<String> secondarySizeDrop;
	public ObservableList<String> secondarySizeList = FXCollections.observableArrayList("2.5", "4");
	
	public Label totalLoadBox;
	public Label maxVoltageBox;
	public Label ccrSizeBox; 
	public Label utilisationBox;
	
	@FXML
	private Button calculate;
	@FXML
	private Button reset;
	

	
	public void initialize() {

		currentDrop.setItems(currentList);
		cableDrop.setItems(cableList);
		primarySizeDrop.setItems(primarySizeList);
		secondarySizeDrop.setItems(secondarySizeList);

}
	
	public void calculate(ActionEvent event) {
			
        String load1 = lr1.getText();
        double lamp1 = Double.parseDouble(load1);
        
        String load2 = lr2.getText();
        double lamp2 = Double.parseDouble(load2);
        
        String load3 = lr3.getText();
        double lamp3 = Double.parseDouble(load3);
        
        String quant1 = qty1.getText();
        double quantity1 = Double.parseDouble(quant1);
        
        String quant2 = qty2.getText();
        double quantity2 = Double.parseDouble(quant2);
        
        String quant3 = qty3.getText();
        double quantity3 = Double.parseDouble(quant3);
		
        String tpl1 = tplBox.getText();
        double lp = Double.parseDouble(tpl1);
        
        String tsl1 = tslBox.getText();
        double ls = Double.parseDouble(tsl1);
        
        String current1 = currentDrop.getValue();
        double I = Double.parseDouble(current1);
        
        String cable = cableDrop.getValue();
        if (cable == "Copper") {rho = rho_cu;}
        else if (cable == "Aluminium" ) {rho = rho_al;}
        
        String cableSizeP = primarySizeDrop.getValue();
        csa1 = Double.parseDouble(cableSizeP);
      
        String cableSizeS = secondarySizeDrop.getValue();
        csa2 = Double.parseDouble(cableSizeS);
        
        double R1 = rho * (1 / (csa2 / 1000000));            // Resistance per metre secondary
        double R2 = rho * (1 / (csa1 / 1000000));            // Resistance per metre primary
        secondaryLossesPerM = Math.pow(I, 2) * R1;        	 // Secondary losses per metre
        secondaryLosses = secondaryLossesPerM * ls;
        primaryLosses = Math.pow(I, 2) * R2 * lp;
        
        lampPlusSecondaryLoad = (1.1*((lamp1*quantity1 + lamp2*quantity2 + lamp3*quantity3) + secondaryLosses) / efficiency * powerFactor); // Added 10% for transformer losses
        

        ccrLoad = 1.05 * (lampPlusSecondaryLoad + primaryLosses); // 5% added for lamp and regulator losses
        ccrLoad = Math.round(ccrLoad * 100.0)/100.0;

        voltage = (1.05*(lampPlusSecondaryLoad + primaryLosses))/I;
        voltage = Math.round(voltage*100.0)/100.0;
        
        if(ccrLoad <= 0.9*2500){
            ccrSize = 2.5;
        }
        else if(ccrLoad >= 0.9*2500 && ccrLoad <=0.9*4000){
            ccrSize = 4;
        }
        else if(ccrLoad > 0.9*4000 && ccrLoad <=0.9*7500){
            ccrSize = 7.5;
        }
        else if(ccrLoad > 0.9*7500 && ccrLoad <=0.9*10000){
            ccrSize = (int)10;
        }
        else if(ccrLoad > 0.9*10000 && ccrLoad <=0.9*15000){
            ccrSize = (int)15;
        }
        else if(ccrLoad > 0.9*15000 && ccrLoad <=0.9*20000){
            ccrSize = (int)20;
        }
        else if(ccrLoad > 0.9*20000 && ccrLoad <=0.9*25000){
            ccrSize = (int)25;
        }
        else if(ccrLoad > 0.9*25000 && ccrLoad <=0.9*30000){
            ccrSize = (int)30;
        }
        else if(ccrLoad > 0.9*30000){
            ccrLoad = 0;
            ccrSize = 0;
            voltage = 0;
        }

        utilisation = 100*(ccrLoad/1000)/ccrSize;
        utilisation = Math.round(utilisation *100.0)/100.0;
        

        displayCCR(ccrLoad);
        displayVol(voltage);
        displaySize(ccrSize);
        displayUtil(utilisation);

        if(ccrLoad == 0 && voltage == 0 && ccrSize == 0 && utilisation == 0){
            String error = "Error!";
            displayError(error);
        }
        
	}
	
    private void displayCCR(double ccrLoad) {
    	totalLoadBox.setText("" + ccrLoad);
    }
    private void displayVol(double vol) {
    	maxVoltageBox.setText("" + vol);
    }
    private void displaySize(double size) {
    	ccrSizeBox.setText("" + size);
    }
    private void displayUtil(double util) {
    	utilisationBox.setText("" + util);
    }

    private void displayError(String error){

    	totalLoadBox.setText(error);
    	maxVoltageBox.setText(error);
    	ccrSizeBox.setText(error);
    	utilisationBox.setText(error);

    }
}
