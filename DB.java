import java.io.*;
import java.util.*;
class DB {

  public String bidsTable = "./bids.txt";
  public String usersTable = "./users.txt";
  public String itemsTable = "./items.txt";
  public static void main(String[] args) {
    new DB();
  }

  public DB() {
    // addUser("tomos");
    // addUser("elliott");
    // addItem("clock");
    // addBid("tomos", "clock", "093");
    // addBid("tomos", "clock", "093");
    System.out.println(checkForCommaInString("tomos"));
    System.out.println(checkForCommaInString("tomo,s"));

    // DBentry entry = getEntryFromId(itemsTable, "0");
    // Item item = (Item) entry;
    // System.out.println(item.name);


    // Item item = (Item) getEntryFromId("items","555");
    // ArrayList<String> bids = getBidsForItem(item);
    // System.out.print(bids);
    //
    // User user = new User("002","tester");
    // System.out.println(getBidsForUser(user));

  }
  /*
    DB tools
  */
  public void tableClear(String table) {
    try(FileWriter file = new FileWriter(table,false)) {} catch (IOException e){}
  }
  public void clearAllTables() {
    tableClear(bidsTable);
    tableClear(itemsTable);
    tableClear(usersTable);
  }
  String fieldFromEntry(String entry, int field) {
    // 0:ID, 1:Name, 2:itemId, 3:value
    return entry.split(",")[field];
  }
  boolean checkForCommaInString(String string) {
    if(string.indexOf(",") == -1) {
      return false;
    }
    return true;
  }

  /*
    DB get
  */
  public void listTable(String table) {
    try(FileReader file = new FileReader(table)) {
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
    try(FileReader file = new FileReader(table)) {
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

    try(FileReader file = new FileReader(table)) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching ID
        if (fieldFromEntry(line,0).equals(Id)) {
          //create bid object
          if (table.equals(bidsTable)) {
            entry = new Bid(fieldFromEntry(line,0),fieldFromEntry(line,1),fieldFromEntry(line,2),fieldFromEntry(line,3));
          } else if (table.equals(itemsTable)){
            entry = new Item(fieldFromEntry(line,0),fieldFromEntry(line,1),fieldFromEntry(line,2));
          } else if (table.equals(usersTable)){
            entry = new User(fieldFromEntry(line,0),fieldFromEntry(line,1));
        }
        return entry;
        }
      }
      System.out.println("ERROR: No Id found in table.");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid table given.: "+table, e);
    } catch (IOException e) {
      System.out.println(e);
    }
    return null;
  }
  DBentry getEntryFromName(String table, String name) { //returns entry object for given name
    DBentry entry = null;
    try(FileReader file = new FileReader(table)) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching name
        if (fieldFromEntry(line,1).equals(name)) {
          //create bid object
          if (table.equals(itemsTable)){
            entry = new Item(fieldFromEntry(line,0),fieldFromEntry(line,1),fieldFromEntry(line,2));
          } else if (table.equals(usersTable)){
            entry = new User(fieldFromEntry(line,0),fieldFromEntry(line,1));
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
  public ArrayList<String> getBidsForItem(Item item) { //returns list of bids for item
    ArrayList<String> output = new ArrayList<String>();
    try(FileReader file = new FileReader(bidsTable)) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching itemId
        if (fieldFromEntry(line,2).equals(item.Id)) {
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
  public ArrayList<String> getBidsForUser(User user) { //returns list of bids for item
    ArrayList<String> output = new ArrayList<String>();
    try(FileReader file = new FileReader(bidsTable)) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching userId
        if (fieldFromEntry(line,1).equals(user.Id)) {
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
    try(FileWriter file = new FileWriter(table, true)) {
      file.append(msg+"\n");
      file.flush();
      file.close();
    } catch (IOException e) {
      System.out.println("write error: "+ e);
    }
  }
  public void addBid(String userName, String itemName, String value) {
    //check input for invalid characters
    if (checkForCommaInString(userName) || checkForCommaInString(itemName) || checkForCommaInString(value)) {
      System.out.println("Failed write: Invalid input");
      return;
    }
    //first find currenct highest bid for item
    DBentry entry = getEntryFromName(itemsTable, itemName);

    //error if item does not exist
    if (entry == null) {
      System.out.println("ERROR: No item by the name "+itemName+".");
      return;
    }

    Item item = (Item) entry;

    //check highest bid
    if(item.stringNumericValueCompare(item.getHighestBid(),value)) {
      System.out.println("Bid denied, lower than current bid: "+item.highest_bid);
      return;
    }

    //construct bid data structure from given userName and itemName
    Bid bid = new Bid(newId(bidsTable),getEntryFromName(usersTable, userName).Id, getEntryFromName(itemsTable, itemName).Id, value);
    //append to storage file
    appendFile(bidsTable, bid.getDbTuple());
  }
  public void addItem(String name) {
    //check input for invalid characters
    if (checkForCommaInString(name)) {
      System.out.println("Failed write: Invalid input");
      return;
    }
    //contruct item
    Item item = new Item(newId(itemsTable),name,"0");
    //append to storage
    appendFile(itemsTable, item.getDbTuple());
  }
  public void addUser(String name) {
    //check input for invalid characters
    if (checkForCommaInString(name)) {
      System.out.println("Failed write: Invalid input");
      return;
    }
    //contruct item
    User user = new User(newId(usersTable), name);
    //append to storage
    appendFile(usersTable, user.getDbTuple());
  }

}

abstract class DBentry {
  public String Id;
  public abstract String getDbTuple();
  boolean stringNumericValueCompare(String str1, String str2) {
    if (Integer.parseInt(str1) > Integer.parseInt(str2)) {
      return true;
    }
    return false;
  }
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

  public String getDbTuple() {
    return Id +","+ userId +","+ itemId +","+ value;
  }
}

class Item extends DBentry {
  public String highest_bid = "000";
  public String name;

  String bidsTable = "./bids.txt";
  String usersTable = "./users.txt";
  String itemsTable = "./items.txt";

  public Item(String Id, String name, String highest_bid) {
    this.Id = Id;
    this.name = name;
  }
  public String getDbTuple() {
    return Id +","+ name +","+ highest_bid;
  }
  public String getHighestBid() {
    //find highest bid
    try(FileReader file = new FileReader(bidsTable)) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching itemId and compare value with highest_bid
        if (line.substring(6,9).equals(this.Id) && stringNumericValueCompare(line.substring(9,12),highest_bid)) {
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
    public String getDbTuple() {
      return Id +","+ name;
    }
}
