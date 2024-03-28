import java.io.*;
import java.util.HashMap;


public class Bank {

    private String institution;
    HashMap<Integer,BankAccount> accounts;

    public Bank(String institution) {
        this.institution = institution;
        accounts = new HashMap<>();
    }

    public void addAccount(BankAccount account) {
        accounts.put(account.getAccountID(),account);
    }

    public void removeAccount(int accountID) {
        accounts.remove(accountID);
    }

    public BankAccount getAccount(int accountID) {
        if (accounts.get(accountID) != null) {
            return accounts.get(accountID);
        }
        return null;
    }

    public HashMap<Integer,BankAccount> getAccounts() {
        return accounts;
    }

    public void saveToFile(DataOutputStream output) throws IOException,FileNotFoundException {
        output.writeUTF(institution);
        output.writeInt(accounts.size());
        output.writeInt(BankAccount.getNextID());

        for (int accountID : accounts.keySet()) {
            BankAccount account = getAccount(accountID);
            saveBankAccount(account,output);
        }
    }

    public void saveBankAccount(BankAccount account, DataOutputStream output) throws IOException, FileNotFoundException {
        output.writeUTF(account.getOwner());
        output.writeInt(account.getAccountID());
        output.writeDouble(account.getBalance());
    }

    public static Bank loadBank(DataInputStream input) throws IOException, FileNotFoundException {
        Bank newBank = new Bank(input.readUTF());
        int numberAccounts = input.readInt();
        BankAccount.setNextID(input.readInt());

        for (int i = 0; i < numberAccounts; i++) {
            BankAccount newAccount = new BankAccount(input.readUTF());
            newAccount.setAccountID(input.readInt());
            newAccount.addMoney(input.readDouble());
            newBank.addAccount(newAccount);
        }
        return newBank;
    }

    public String getInstitution() {
        return institution;
    }
}
