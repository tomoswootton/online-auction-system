import java.io.IOException;
import java.util.Scanner;

class OnlineAuctionSystem {
  Scanner scanner;
  DB db;

  public static void main(String[] args) {
    new OnlineAuctionSystem();
  }

  public OnlineAuctionSystem() {
    scanner = new Scanner(System.in);
    db = new DB();
    System.out.println("Welcome to the auction!\n");
    while(true) {
      menu();
    }
  }


  void menu() {
    System.out.println("\nType the number of the function you would like to call:\n\n" +"1:Add User\n2:Add Item\n3:Add Bid");
    System.out.println("4:List Table\n5:Clear Table\n6:Clear All Tables\n");

    String choice = scanner.nextLine();
    String name = null;
    String table = null;
    switch (choice) {
      case "1":
        System.out.println("name:");
        name = scanner.nextLine();
        db.addUser(name);
        break;
      case "2":
        System.out.println("name:");
        name = scanner.nextLine();
        db.addItem(name);
        break;
      case "3":
        System.out.println("User name:");
        String userName = scanner.nextLine();
        System.out.println("Item name:");
        String itemName = scanner.nextLine();
        System.out.println("Value:");
        String value = scanner.nextLine();
        db.addBid(userName, itemName, value);
        break;
      case "4":
        System.out.println("Which one?\n1:Users\n2:Items\n3:Bids\n4:All");
        table = scanner.nextLine();
        if (table.equals("1")) {
          System.out.println("\nUsers table:");
          db.listTable(db.usersTable);
        } else if (table.equals("2")) {
          System.out.println("\nItems table:");
          db.listTable(db.itemsTable);
        } else if (table.equals("3")) {
          System.out.println("\nBids table:");
          db.listTable(db.bidsTable);
        } else if (table.equals("4")) {
          System.out.println("\nUsers table:");
          db.listTable(db.usersTable);
          System.out.println("\nItems table:");
          db.listTable(db.itemsTable);
          System.out.println("\nBids table:");
          db.listTable(db.bidsTable);
        } else {
          System.out.println("Invalid table choice");
        }
        break;
      case "5":
        System.out.println("Which one?\n1:Users\n2:Items\n3:Bids");
        table = scanner.nextLine();
        if (table.equals("1")) {
          db.tableClear(db.usersTable);
          System.out.println("\nUsers table cleared.");
        } else if (table.equals("2")) {
          db.tableClear(db.itemsTable);
          System.out.println("\nItemss table cleared.");
        } else if (table.equals("3")) {
          db.tableClear(db.bidsTable);
          System.out.println("\nBids table cleared.");
        }
        break;
      case "6":
        db.clearAllTables();
        System.out.println("\nAll tables cleared.");
        break;
      default:
        System.out.println("Invalid menu choice.");
        break;

    }

  }
}
