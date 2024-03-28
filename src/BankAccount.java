public class BankAccount {

    private static int nextID;
    private int accountID;
    private String owner;
    private double balance;

    public BankAccount(String name) {
        this.owner = name;
        this.accountID = nextID;
        this.balance = 0;
        nextID++;
    }


    public void addMoney(double amount) {
        System.out.println("Adding " + amount);
        if (amount <= 0) {
            return;
        }
        balance += amount;
    }

    public void removeMoney(double amount) {
        if (amount >= 0 && amount <= this.balance) {
            this.balance -= amount;
        }
    }

    public void setAccountID(int id) {
        this.accountID = id;
    }

    public static int getNextID() {
        return nextID;
    }
    public static void setNextID(int id) {
        nextID = id;
    }

    public double getBalance() {
        return balance;
    }
    public String getOwner() { return owner; }
    public int getAccountID() {
        return accountID;
    }

    @Override
    public String toString() {
        return owner + ", ID: " + accountID;
    }
}
