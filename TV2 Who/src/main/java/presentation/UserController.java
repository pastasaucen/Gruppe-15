package presentation;

import domain.ITV2WhoUI;
import domain.SystemAdministrator;
import domain.TV2Who;
import domain.UserType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;

public class UserController extends BorderPane {

    @FXML
    BorderPane borderPane;

    Text header;

    ITV2WhoUI tv2Who = TV2Who.getInstance();

    public UserController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user.fxml"));
        fxmlLoader.setRoot(this);

        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * layout and action for create user
     */
    public void createUser(){
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0,0,0,50));
        grid.setHgap(10);
        grid.setVgap(50);

        Text nameText = new Text("Navn:");
        TextField nameField = new TextField();
        nameField.setPrefWidth(300);
        nameField.setPromptText("Navn");
        VBox name = new VBox();
        name.getChildren().addAll(nameText, nameField);
        Text emailText = new Text("E-mail:");
        TextField emailField = new TextField();
        emailField.setPrefWidth(300);
        emailField.setPromptText("E-mail");
        VBox email = new VBox();
        email.getChildren().addAll(emailText,emailField);
        Text codewordText = new Text("Kodeord");
        TextField codewordField = new TextField();
        codewordField.setPrefWidth(300);
        codewordField.setPromptText("Kodeord");
        VBox codeword = new VBox();
        codeword.getChildren().addAll(codewordText, codewordField);
        RadioButton systemAdministrator = new RadioButton();
        Text sAText = new Text("System Administrator");
        HBox sA = new HBox();
        sA.getChildren().addAll(systemAdministrator, sAText);
        RadioButton producer = new RadioButton();
        Text prText = new Text("Producer");
        HBox pr = new HBox();
        pr.getChildren().addAll(producer,prText);
        RadioButton rDUser = new RadioButton();
        Text rDUText = new Text("Redaktion Danmark Bruger");
        HBox rDU = new HBox();
        rDU.getChildren().addAll(rDUser, rDUText);
        RadioButton editor = new RadioButton();
        Text edText = new Text("Redaktør");
        HBox ed = new HBox();
        ed.getChildren().addAll(editor, edText);
        Button create = new Button ("Opret Bruger");
        VBox type = new VBox();
        type.getChildren().addAll(sA, pr, rDU, ed);

        ToggleGroup radios = new ToggleGroup();
        systemAdministrator.setToggleGroup(radios);
        producer.setToggleGroup(radios);
        rDUser.setToggleGroup(radios);
        editor.setToggleGroup(radios);
        Text warning = new Text("");
        warning.setFill(Color.RED);


        grid.add(name,1,1);
        grid.add(email,1,2);
        grid.add(codeword,1,3);
        grid.add(create,1,4);
        grid.add(warning,1,5);
        grid.add(type, 3,2);

        setHeader("OPRET LOGIN");
        borderPane.setCenter(grid);

        create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name = nameField.getText();
                String email = emailField.getText();
                String codeword = codewordField.getText();
                UserType userType = null;
                if(name.isBlank() || email.isBlank() ||codeword.isBlank()){
                    warning.setText("Udfyld alle felter");
                    return;
                } else if(systemAdministrator.isSelected()){
                    userType = UserType.SYSTEMADMINISTRATOR;
                } else if(producer.isSelected()){
                    userType = UserType.PRODUCER;
                } else if(rDUser.isSelected()){
                    userType = UserType.RDUSER;
                }else if(editor.isSelected()){
                    userType = UserType.EDITOR;
                } else if (userType == null) {
                    warning.setText("Vælg en bruger type");
                    return;
                }

                SystemAdministrator administrator = (SystemAdministrator) tv2Who.getCurrentUser();
                administrator.createUser(name, email, userType, codeword);

                Stage stage = new Stage();
                stage.setResizable(false);
                BorderPane borderPane = new BorderPane();
                borderPane.setPrefSize(500,500);
                Scene scene = new Scene(borderPane);

                Text text = new Text("BRUGEREN " + email + "\nER BLEVET OPRETTET");
                borderPane.setCenter(text);


                HBox hbox = new HBox();
                Button ok = new Button("OK");
                hbox.getChildren().add(ok);
                hbox.setAlignment(Pos.TOP_CENTER);
                hbox.setPrefHeight(150);
                hbox.setSpacing(100);
                borderPane.setBottom(hbox);
                stage.setScene(scene);
                stage.show();

                ok.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        stage.close();
                    }
                });

                createUser();
            }
        });


    }

    /**
     * Header for user.fxml
     * @param string
     */
    private void setHeader(String string){
        header = new Text(string);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setFont(Font.font(25));
        borderPane.setTop(header);
        borderPane.setAlignment(header, Pos.CENTER);
    }

    public void userStart(){
        borderPane.setCenter(null);
        setHeader("BRUGER HÅNDTERING");
    }
}
