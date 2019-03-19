import java.io.*;
import java.util.*;
class Main {

  String bidsTable = "./bids.txt";
  String usersTable = "./users.txt";
  String itemsTable = "./items.txt";
  public static void main(String[] args) {
    new Main();
  }

  public Main() {
    // DBentry entry = getEntryFromName("items", "000");
    // System.out.println(entry.getStringRepresentation());
    // addBid("tomos", "clock", "093");
    Item item = (Item) getEntryFromId("items","555");
    ArrayList<String> bids = getBidsForItem(item);
    System.out.print(bids);

    User user = new User("002","tester");
    System.out.println(getBidsForUser(user));

    clearAllTables();
  }
  /*
    DB tools
  */
  void tableClear(String table) {
    try(FileWriter file = new FileWriter("./"+table+".txt",false)) {} catch (IOException e){}
  }
  void clearAllTables() {
    tableClear("bids");
    tableClear("items");
    tableClear("users");
  }

  /*
    DB get
  */
  void listTable(String table) {
    try(FileReader file = new FileReader("./"+table+".txt")) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        System.out.println(line);
        }
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid table given.", e);
    } catch (IOException e) {
      System.out.println(e);
    }
  }
  int numEntries(String table) {
    String line;
    int lineCount = 0;
    try(FileReader file = new FileReader("./"+table+".txt")) {
      BufferedReader inStream = new BufferedReader(file);
      while((line = inStream.readLine()) != null) {
        lineCount ++;
      }
    } catch (FileNotFoundException e) {
      System.out.println("ERROR: invalid table.");
      System.out.println(e);
    } catch (IOException e) {
      System.out.println(e);
    }
    return lineCount;
  }
  DBentry getEntryFromId(String table, String Id) { //returns entry object for given Id

    DBentry entry = null;

    try(FileReader file = new FileReader("./"+table+".txt")) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching ID
        if (line.substring(0,3).equals(Id)) {
          //create bid object
          if (table.equals("bids")) {
            entry = new Bid(line.substring(0,3),line.substring(3,6),line.substring(6,9),line.substring(9,12));
          } else if (table.equals("items")){
            entry = new Item(line.substring(0,3),line.substring(3,6),line.substring(6,line.length()));
          } else if (table.equals("users")){
            entry = new User(line.substring(0,3),line.substring(3,line.length()));
        }
        return entry;
        }
      }
      System.out.println("ERROR: No Id found in table.");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid table given.", e);
    } catch (IOException e) {
      System.out.println(e);
    }
    return null;
  }
  DBentry getEntryFromName(String table, String name) { //returns entry object for given name
    DBentry entry = null;
    try(FileReader file = new FileReader("./"+table+".txt")) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching ID
        if (line.substring(3,line.length()).equals(name)) {
          //create bid object
          if (table.equals("items")){
            entry = new Item(line.substring(0,3),line.substring(3,6),line.substring(6,line.length()));
          } else if (table.equals("users")){
            entry = new User(line.substring(0,3),line.substring(3,line.length()));
          }
        return entry;
        }
      }
      System.out.println("ERROR: No name found.");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid table given.", e);
    } catch (IOException e) {
      System.out.println(e);
    }
    return null;
  }
  ArrayList<String> getBidsForItem(Item item) { //returns list of bids for item
    ArrayList<String> output = new ArrayList<String>();
    try(FileReader file = new FileReader(bidsTable)) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching itemId
        if (line.substring(3,6).equals(item.Id)) {
          output.add(line);
          }
        }
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid table given.", e);
    } catch (IOException e) {
      System.out.println(e);
    }
    return output;
  }
  ArrayList<String> getBidsForUser(User user) { //returns list of bids for item
    ArrayList<String> output = new ArrayList<String>();
    try(FileReader file = new FileReader(bidsTable)) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching itemId
        if (line.substring(6,9).equals(user.Id)) {
          output.add(line);
          }
        }
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid table given.", e);
    } catch (IOException e) {
      System.out.println(e);
    }
    return output;
  }
  /*
    DB set
  */
  String newId(String table) {
    //count num entries in table, return new id value
    String numS = Integer.toString(numEntries(table));
    switch(numS.length()) {
      case 1:
        numS = "00" + numS;
        break;
      case 2:
        numS = "0" + numS;
        break;
      case 3 :
        break;
      default:
        throw new IllegalArgumentException("Invalid input length.");
    }
    return numS;
  }
  void appendFile(String table, String msg) {
    try(FileWriter file = new FileWriter("./"+table+".txt", true)) {
      file.append(msg+"\n");
      file.flush();
      file.close();
    } catch (IOException e) {
      System.out.println("write error: "+ e);
    }
  }
  void addBid(String userName, String itemName, String value) {
    //first find currenct highest bid for item
    DBentry entry = getEntryFromName("items", itemName);
    Item item = (Item) entry;

    System.out.println(item.getStringRepresentation());
    if(item.stringValueLarger(item.getHighestBid(),value)) {
      System.out.println("Bid denied, lower than current bid: "+item.highest_bid);
      return;
    }
    System.out.println(item.getStringRepresentation());

    //construct bid data structure from given userName and itemName
    Bid bid = new Bid(newId("bids"),getEntryFromName("users", userName).Id, getEntryFromName("items", itemName).Id, value);
    //append to storage file
    appendFile("bids", bid.getStringRepresentation());
  }
  void addItem(String name) {
    //contruct item
    Item item = new Item(newId("items"),"000", name);
    //append to storage
    appendFile("items", item.getStringRepresentation());
  }
  void addUser(String name) {
    //contruct item
    User user = new User(newId("users"), name);
    //append to storage
    appendFile("users", user.getStringRepresentation());
  }

}

abstract class DBentry {
  public String Id;
  public abstract String getStringRepresentation();

}

class Bid extends DBentry {
  public String userId;
  public String itemId;
  public String value;

  public Bid(String Id, String userId, String itemId, String value ) {
    this.Id = Id;
    this.userId = userId;
    this.itemId = itemId;
    this.value = value;
  }

  public String getStringRepresentation() {
    return Id + userId + itemId + value;
  }
}

class Item extends DBentry {
  public String highest_bid = "000";
  public String name;

  String bidsTable = "./bids.txt";
  String usersTable = "./users.txt";
  String itemsTable = "./items.txt";

  public Item(String Id, String highest_bid, String name) {
    this.Id = Id;
    this.name = name;
  }
  public String getStringRepresentation() {
    return Id + highest_bid + name;
  }
  boolean stringValueLarger(String str1, String str2) {
    if (Integer.parseInt(str1) > Integer.parseInt(str2)) {
      return true;
    }
    return false;
  }
  public String getHighestBid() {
    //find highest bid
    try(FileReader file = new FileReader(bidsTable)) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching itemId and compare value with highest_bid
        if (line.substring(6,9).equals(this.Id) && stringValueLarger(line.substring(9,12),highest_bid)) {
          //set new highest bid
          highest_bid = line.substring(9,12);
        }
      }
      this.highest_bid = highest_bid;
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid table given.", e);
    } catch (IOException e) {
      System.out.println(e);
    }
    return this.highest_bid;
  }
}

  class User extends DBentry {
    public String name;

    public User(String Id, String name) {
      this.Id = Id;
      this.name = name;
    }
    public String getStringRepresentation() {
      return Id + name;
    }
}
