import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.*;
import java.util.HashMap;

public class BankApp extends Application {

    public void saveBank(Bank bank, String filename) {
        try {
            bank.saveToFile(new DataOutputStream(new FileOutputStream(filename)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Bank loadBank(String filename) {
        try {
            return Bank.loadBank(new DataInputStream(new FileInputStream(filename)));
        } catch (FileNotFoundException e) {
            return new Bank("TD");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(Stage primaryStage) {
        Bank td = loadBank("Bank.txt");
        HashMap<Integer, BankAccount> accounts = td.getAccounts();

        Pane main = new Pane();
        Scene scene = new Scene(main, 500, 300);
        primaryStage.setTitle(td.getInstitution() + " Bank");

        ListView<String> accountsList = new ListView<>();
        accountsList.relocate(10,30);
        accountsList.setPrefSize(205,200);

        refreshList(accounts, td, accountsList);

        TextField accountName = new TextField();
        accountName.relocate(10,270);
        accountName.setPrefSize(130,10);

        Button addAccount = new Button("Create");
        addAccount.setPrefSize(65,10);
        addAccount.relocate(150,270);
        addAccount.setStyle("-fx-text-fill: #ffffff; -fx-background-color: #0f9608;");

        addAccount.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                BankAccount newAccount = new BankAccount(accountName.getText());
                td.addAccount(newAccount);
                refreshList(accounts, td, accountsList);
                saveBank(td,"Bank.txt");
            }
        });

        Button deleteAccount = new Button("Delete");
        deleteAccount.setPrefSize(65,10);
        deleteAccount.relocate(115,239);
        deleteAccount.setStyle("-fx-background-color: #b00d07; -fx-text-fill: #ffffff");

        Label accountslabel = new Label("Accounts");
        accountslabel.relocate(10,10);
        accountslabel.setPrefSize(50,10);

        Label infolabel = new Label("Account Info");
        infolabel.relocate(230,10);
        infolabel.setPrefSize(80,10);

        ListView<String> infolist = new ListView<>();
        infolist.relocate(230,30);
        infolist.setPrefSize(255,200);
        String[] infos = new String[3];

        deleteAccount.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                String selected = accountsList.getSelectionModel().getSelectedItem();
                BankAccount account = getAccountFromString(selected,td);
                td.removeAccount(account.getAccountID());
                saveBank(td,"Bank.txt");
                refreshList(accounts,td,accountsList);
                if (infos[0].equals("Account Owner: " + account.getOwner())) {
                    infolist.setItems(FXCollections.observableArrayList(new String[0]));
                }
            }
        });

        Button viewAccount = new Button("View Account");
        viewAccount.relocate(10,240);
        viewAccount.setPrefSize(95,10);
        viewAccount.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                String selected = accountsList.getSelectionModel().getSelectedItem();
                BankAccount account = getAccountFromString(selected,td);
                infos[0] = "Account Owner: " + account.getOwner();
                infos[1] = "Account ID: " + account.getAccountID();
                infos[2] = "Account Balance: $" + String.format("%.2f",account.getBalance());
                infolist.setItems(FXCollections.observableArrayList(infos));
            }
        });

        TextField money = new TextField();
        money.relocate(230,240);
        money.setPrefSize(70,10);

        Button addMoney = new Button("Add Funds");
        addMoney.relocate(305,240);
        addMoney.setPrefSize(80,10);
        addMoney.setStyle("-fx-background-color: #0f9608");

        Button removeMoney = new Button("Remove Funds");
        removeMoney.relocate(390,240);
        removeMoney.setPrefSize(95,10);
        removeMoney.setStyle("-fx-background-color: #b00d07");

        addMoney.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                String selected = accountsList.getSelectionModel().getSelectedItem();
                BankAccount account = getAccountFromString(selected,td);
                account.addMoney(Double.parseDouble(money.getText()));
                infos[0] = "Account Owner: " + account.getOwner();
                infos[1] = "Account ID: " + account.getAccountID();
                infos[2] = "Account Balance: $" + String.format("%.2f",account.getBalance());
                infolist.setItems(FXCollections.observableArrayList(infos));
                saveBank(td,"Bank.txt");
            }
        });

        removeMoney.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                String selected = accountsList.getSelectionModel().getSelectedItem();
                BankAccount account = getAccountFromString(selected,td);
                account.removeMoney(Double.parseDouble(money.getText()));
                infos[0] = "Account Owner: " + account.getOwner();
                infos[1] = "Account ID: " + account.getAccountID();
                infos[2] = "Account Balance: $" + String.format("%.2f",account.getBalance());
                infolist.setItems(FXCollections.observableArrayList(infos));
                saveBank(td,"Bank.txt");
            }
        });

        main.getChildren().addAll(infolabel,accountslabel,accountsList,viewAccount,accountName,addAccount,deleteAccount,infolist,money,removeMoney,addMoney);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void refreshList(HashMap<Integer, BankAccount> accounts, Bank td, ListView<String> accountsList) {
        String[] accountStrings = new String[accounts.size()];
        int i = 0;
        for (int accountID : accounts.keySet()) {
            BankAccount account = td.getAccount(accountID);
            accountStrings[i] = account.toString();
            i++;
        }
        accountsList.setItems(FXCollections.observableArrayList(accountStrings));
    }

    public BankAccount getAccountFromString(String str, Bank bank) {
        HashMap<Integer, BankAccount> accounts = bank.getAccounts();
        for (int accountID : accounts.keySet()) {
            BankAccount account = bank.getAccount(accountID);
            if (account.toString().equals(str)) {
                return account;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
