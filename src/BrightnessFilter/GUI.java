package BrightnessFilter;


import javafx.scene.control.Label;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javafx.scene.control.TextField;
import javafx.scene.control.Control;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.CheckBox;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Hyperlink;
import javafx.geometry.Orientation;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class GUI extends Application {
    
	// Initializam main ca obiect general, acesta fiind utilizat pentru procesarea efectiva a imaginii selectate.
    Main main = null;
	// ImageView pentru afisarea imaginii selectate, respectiv modificate.
    ImageView myImageView;
    // Se defineste pentru selectarea imaginilor.
    FileChooser fileChooser = new FileChooser();
    File file = null;
    BufferedImage bufferedImage = null;
    // Vom utiliza obiectul de tip Image pentru citirea imaginii.
    Image image = null;
    // Nivelul de luminozitate adaugat.
	Integer level = 0;
	// Variabila pentru definirea acceptarii termenilor.
	Integer accepted = 0;
	// Element de tip ProgressBar pentru afisarea progresului.
    ProgressBar bar = new ProgressBar(0);
    // String utilizat pentru calea destinatie a imaginii modificate.
    String destination;
    // Raportul survenit dupa completarea studiului.
    String raport = "";

    public static int getIntFromTextField(TextField textField) {
        String text = textField.getText();
        return Integer.parseInt(text);
    }
    
    // Primul stage utilizat. 
    @Override
    public void start(Stage primaryStage) throws IOException {

        VBox start = new VBox();
        // Element de tip toolbar, utilizat de asemenea in cel de-al doilea ecran.
        ToolBar toolBar = new ToolBar();
        
        ImageView raport_image = new ImageView();
        
        // Definire caracteristici.
        start.setSpacing(30);
        start.setPadding(new Insets(100, 50, 50, 50));
        start.setStyle("-fx-background-color: white");
        
        // Indicator de progres.
        ProgressIndicator progress = new ProgressIndicator();

        // Buton grafic ce va duce din primul in al doilea ecran. 
        Button button_start = new Button();
        Image imageStart = new Image(getClass().getResourceAsStream("start.png"));
        button_start.setGraphic(new ImageView(imageStart));

    	Button btnLoad = new Button("Load");
    	btnLoad.setStyle("-fx-base: coral;");
        btnLoad.setOnAction(btnLoadEventListener);
        toolBar.getItems().add(btnLoad);
        
        // Buton colorat pentru adaugarea filtrului la imagine, acesta declansand procesarea imaginii.
        Button filter = new Button("Add filter");  
        filter.setStyle("-fx-base: coral;");
        toolBar.getItems().add(filter);
        filter.setOnAction(btnLevelEventListener);  

        // Buton ce salveaza imaginea procesata la adresa introdusa.
        Button dest = new Button("Save to path");  
        dest.setStyle("-fx-base: coral;");
        toolBar.getItems().add(dest);
        dest.setOnAction(btnDestEventListener);        
        
        // Buton ce va duce in al treilea ecran, cel utilizat pentru chestionar.
    	Button btnInf = new Button("Survey");
    	btnInf.setStyle("-fx-base: coral;");
        toolBar.getItems().add(btnInf);

        // Buton utilizat pentru intoarcerea la ecranul anterior.
    	Button back = new Button("Go back");
    	back.setStyle("-fx-base: coral;");

        // Buton utilizat pentru trimiterea chestionarului.
        Button submit = new Button("Submit report");  
        submit.setStyle("-fx-base: coral;");
        submit.setOnAction(p -> {System.out.println(raport); raport = "";}); 
        
        VBox rootBox = new VBox(toolBar);
        rootBox.setSpacing(30);
        rootBox.setPadding(new Insets(100, 50, 50, 50));     
        rootBox.setStyle("-fx-background-color: white");
        
        VBox rootBox2 = new VBox();
        rootBox2.setSpacing(30);
        rootBox2.setStyle("-fx-background-color: white");
        
        // Label pentru numele aplicatiei.
    	Label label = new Label("Brightness Filter");
    	label.setFont(new Font("Arial", 24));
    	label.setTextFill(Color.color(0, 1, 1));
   
        
        myImageView = new ImageView();        
        myImageView.setFitHeight(500);
        myImageView.setFitWidth(500);       
      
        // Element de tip checkbox, utilizat in primul ecran, pentru confirmarea termenilor.
        CheckBox checkbox = new CheckBox("I confirm I read the provided information.");
        checkbox.setTextFill(Color.color(0, 1, 1));
        checkbox.selectedProperty().addListener(
      	      (ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
      	         progress.setProgress(0.3);
      	         accepted = 1;
         		 bar.setProgress(0.3);
      	      });
    	
        // Elementul de tip hiperlink va fi utilizat in primul ecran pentru citirea documentatiei.
        Hyperlink hyperlink = new Hyperlink("https://docs.oracle.com/javase/8/javafx/get-started-tutorial/jfx-overview.htm");
        hyperlink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	 getHostServices().showDocument(hyperlink.getText());
            }
        });
        
        // Label de tip grafic, utilizat in cel de-al treilea ecran.
        Label graphic_label = new Label("Brightness filter");
        Image img =  new Image(getClass().getResourceAsStream("sun.png"));
        ImageView view = new ImageView(img);
        view.setFitHeight(80);
        view.setPreserveRatio(true);
        graphic_label.setGraphic(view);
        Font font = Font.font("Brush Script MT", FontWeight.BOLD, FontPosture.REGULAR, 25);
        // Definire proprietati ale fontului labelului.
        graphic_label.setFont(font);
        graphic_label.setTextFill(Color.CORAL);
        graphic_label.setTranslateX(500);
        graphic_label.setTranslateY(0);

        // Elemente de tip Text Fielt, utilizate atat pentru introducerea caii destinatie, cat si pentru 
        // introducerea nivelului de luminozitate adaugat.
        TextField textfield=new TextField();  
        textfield.setMaxWidth(300);
        textfield.setPromptText("Level of brightness");

        TextField textfield_dest=new TextField();  
        textfield_dest.setMaxWidth(300);
        textfield_dest.setPromptText("File destination path");
                
        // Listenere pentru text field-urile utilizate.
        try {
	        textfield.textProperty().addListener((observable, oldValue, newValue) -> {
			level=Integer.parseInt(textfield.getText());
	        	});}
        catch (Exception e) {
        	System.out.println(e);
        }
        
        try {
        	textfield_dest.textProperty().addListener((observable, oldValue, newValue) -> {
    		destination=textfield_dest.getText();
            	});}
            catch (Exception e) {
            	System.out.println(e);
            }
                   
        // Label si grup de butoane de tip radio, utilizate pentru chestionarul din al treilea ecran.
    	Label survey_label = new Label("What is the reason for using this app?");
    	ToggleGroup grp = new ToggleGroup();
        RadioButton radio1 = new RadioButton("Work");
        radio1.setToggleGroup(grp);
        RadioButton radio2 = new RadioButton("Curiosity");
        radio2.setToggleGroup(grp);
        RadioButton radio3 = new RadioButton("By mistake");
        radio3.setToggleGroup(grp);
        
        // Label si grup de butoane de tip radio, utilizate pentru chestionarul din al treilea ecran.
    	Label survey_label2 = new Label("You enjoyed the app overall:");
        ToggleGroup grp2 = new ToggleGroup();
        ToggleButton toggle1 = new ToggleButton("yes");
        toggle1.setToggleGroup(grp2);
        ToggleButton toggle2 = new ToggleButton("no");
        toggle2.setToggleGroup(grp2);
        
        // Listener utilizat pentru radio buttons.
        grp.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
        @Override
        public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1)
            {
            RadioButton sel = (RadioButton)t1.getToggleGroup().getSelectedToggle(); 
            if (sel == radio1){
            	// Se va adauga raspunsul la raportul de tip string.
            	raport += "What is the reason for using this app? Work";
            }
            if (sel == radio2){
            	raport += "What is the reason for using this app? Curiosity.";
            }
            if (sel == radio3){
            	raport += "What is the reason for using this app? By mistake.";
            }
            }
        });
    	     
        // Listener utilizat pentru raport.
        grp2.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
        @Override
        public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1)
            {
        	ToggleButton sel = (ToggleButton)t1.getToggleGroup().getSelectedToggle(); 
        	// Se va adauga raspunsul la raportul de tip string, generandu-se o imagine pe baza raspunsului.
            if (sel == toggle1){
            	raport += "\nYou enjoyed the app overall: Yes";
                Image imageGoodRaport = new Image(getClass().getResourceAsStream("thanks.jpg"));
                raport_image.setImage(imageGoodRaport);
            }
            if (sel == toggle2){
            	raport += "\nYou enjoyed the app overall: No";
                Image imageBadRaport = new Image(getClass().getResourceAsStream("sorry.jpg"));
                raport_image.setImage(imageBadRaport);
            }
            }
        });
        
        // Elementul de tip lista orizontala va fi utilizat pentru afisarea instructiunilor folosirii aplicatiei.
        ListView<String> list_horizontal = new ListView<String>();
        ObservableList<String> list = FXCollections.observableArrayList("Instructions for using this app: ", "1. Confirm reading theory", "2. Load image", "3. Set the brightness level", "4. Apply filter",
          "5. Save to a given path");
        
        list_horizontal.setItems(list);
        list_horizontal.setPrefWidth(400);
        list_horizontal.setPrefHeight(100);
        list_horizontal.setOrientation(Orientation.HORIZONTAL);
        ListView<String> list_vertical = new ListView<>();
        
        // Elementul de tip lista simpla va fi utilizat pentru afisarea unor atentionari referitoare la aplicatie.
        ObservableList<String> listv = FXCollections.observableArrayList("Important: ", "1. If the level is too low/high, it will adjust automatically", "2. BMP images are supported", "3. The image will be automatically saved at the old path");
        list_vertical.setItems(listv);
        list_vertical.setPrefWidth(400);
        list_vertical.setPrefHeight(50);
        list_vertical.setOrientation(Orientation.VERTICAL);
        list_vertical.setPrefWidth(100);
        list_vertical.setPrefHeight(300);
        
        
        // Element de tip scroll bar, utilizat in cel de-al doilea ecran.
        final ScrollBar scroll = new ScrollBar();
        scroll.setLayoutX(0);
        scroll.setMin(0);
        scroll.setOrientation(Orientation.HORIZONTAL);
            
        start.getChildren().addAll(hyperlink,checkbox,progress,button_start);
        rootBox.getChildren().addAll(label,  btnLoad,textfield, textfield_dest, filter, dest, btnInf, bar, myImageView, scroll);
        rootBox.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        rootBox2.getChildren().addAll(graphic_label,back, survey_label, radio1, radio2, radio3,survey_label2, toggle1, toggle2, submit, list_horizontal,list_vertical, raport_image);

        // Definirea a trei ecrane, fiecare avand cate o radacina.
        Scene scene_start = new Scene(start, 1000, 1000);
        Scene scene_survey = new Scene(rootBox2, 800, 1000);
        Scene scene_process = new Scene(rootBox, 800, 1000);
        
        // In cazul apasarii butonului de start, se va verifica daca a fost bifat checkbox-ul iar in acel caz, se va trece la 
        // urmatorul ecran.
        button_start.setOnAction(e -> {if (accepted == 1) primaryStage.setScene(scene_process);});
    	btnInf.setOnAction(e -> primaryStage.setScene(scene_survey));
    	back.setOnAction(e -> primaryStage.setScene(scene_process));
    	
    	scroll.valueProperty().addListener(new ChangeListener<Number>() {
    	    public void changed(ObservableValue<? extends Number> ov,
    	        Number oldv, Number newv) {
    	            rootBox.setLayoutX(-newv.doubleValue());
    	        }
    	});
    	
        primaryStage.setTitle("Brightness Filter");
        primaryStage.setScene(scene_start);    
        primaryStage.show();
    }
 
    // Event listener pentru butonul load, care va utiliza selectorul de fisiere definit anterior
    // pentru selectarea unei imagini de tip BMP. 
    EventHandler<ActionEvent> btnLoadEventListener
    = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent t) {            
            FileChooser.ExtensionFilter extFilterBMP = new FileChooser.ExtensionFilter("BMP files (*.bmp)", "*.BMP");
            fileChooser.getExtensionFilters().addAll(extFilterBMP);
            file = fileChooser.showOpenDialog(null);
                      
				try {
					bufferedImage = ImageIO.read(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                image = SwingFXUtils.toFXImage(bufferedImage, null);
              
                myImageView.setImage(image);
        		bar.setProgress(0.7);
        }
    };
    
    // Event listener pentru butonul de destinatie, care va salva imaginea la adresa data.
    EventHandler<ActionEvent> btnDestEventListener
    = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent t) {
        		try {
					main.saveImage(destination);
				} catch (IOException e) {
					e.printStackTrace();
				}
        		// Indicatorul de progres va arata acum procesul ca fiind complet.
        		bar.setProgress(1);
        }
    };
    
    // Event listener pentru butonul de adaugare a nivelului de luminozitate peste imaginea incarcata.
    EventHandler<ActionEvent> btnLevelEventListener
    = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent t) {
            try {
         	 System.out.println(file.getAbsolutePath());
         	 // Se instantiaza un obiect de tipul main, aceasta fiind clasa ce se ocupa de procesare
         	 // pasand adresa fisierului si nivelul de luminozitate adauat.
				main = new Main(file.getAbsolutePath(), level);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}            
             String path = file.getAbsolutePath() ;
             try {
            	 // Se proceseaza imaginea incarcata.
					main.processImage(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
             try {
            	 // Se salveaza imaginea la calea data ca parametru.
 				main.saveImage(path);
 			} catch (IOException e) {
 				e.printStackTrace();
 			}
             // Se afiseaza imaginea noua, procesata, in locul celei vechi.
             Image new_image = new Image(Paths.get(path).toUri().toString());
     		bar.setProgress(0.8);
             myImageView.setImage(new_image);
        }
          
    };
    
    public static void main(String[] args) {
        launch(args);
    }
    
}