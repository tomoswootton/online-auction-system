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
    // testing();
  }

  void testing() {
    clearAllTables();

    //invalid inputs
    addItem("");
    addItem("clo,ck");
    addUser("");
    addUser("tom,os");

    addItem("clock");
    addUser("tomos");
    addBid("","clock","23");
    addBid("tomos","","23");
    addBid("tomos","clock","");
    addBid("tom,os","clock","23");
    addBid("tomos","cl,ock","23");
    addBid("tomos","clock","2,3");
    addBid("tomos","clock","23");

    //test new bid must be > highest
    addBid("tomos", "clock", "100");
    addBid("tomos", "clock", "99");

    //pulling from tables
    Item item = (Item) getEntryFromName(itemsTable, "");
    item = (Item) getEntryFromName(itemsTable, "not-in-table");
    item = (Item) getEntryFromName(itemsTable, "clock");
    System.out.println(item.getDbTuple());

    User user = (User) getEntryFromName(usersTable, "");
    user = (User) getEntryFromName(usersTable, "not-in-table");
    user = (User) getEntryFromName(usersTable, "tomos");
    System.out.println(user.getDbTuple());

    item = (Item) getEntryFromName(itemsTable, "");
    System.out.println(getBidsForItem(item));
    item = (Item) getEntryFromName(itemsTable, "not-in-table");
    System.out.println(getBidsForItem(item));
    item = (Item) getEntryFromName(itemsTable, "clock");
    System.out.println(getBidsForItem(item));

    user = (User) getEntryFromName(usersTable, "");
    System.out.println(getBidsForUser(user));
    user = (User) getEntryFromName(usersTable, "not-in-table");
    System.out.println(getBidsForUser(user));
    user = (User) getEntryFromName(usersTable, "tomos");
    System.out.println(getBidsForUser(user));



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
  public DBentry getEntryFromId(String table, String Id) { //returns entry object for given Id

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
            entry = new Item(fieldFromEntry(line,0),fieldFromEntry(line,1));
          } else if (table.equals(usersTable)){
            entry = new User(fieldFromEntry(line,0),fieldFromEntry(line,1));
        }
        return entry;
        }
      }
      System.out.println("ERROR: No Id="+Id+" found in "+table+".");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid table given.: "+table, e);
    } catch (IOException e) {
      System.out.println(e);
    }
    return null;
  }
  public DBentry getEntryFromName(String table, String name) { //returns entry object for given name
    DBentry entry = null;
    try(FileReader file = new FileReader(table)) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching name
        if (fieldFromEntry(line,1).equals(name)) {
          //create bid object
          if (table.equals(itemsTable)){
            entry = new Item(fieldFromEntry(line,0),fieldFromEntry(line,1));
          } else if (table.equals(usersTable)){
            entry = new User(fieldFromEntry(line,0),fieldFromEntry(line,1));
          }
        return entry;
        }
      }
      System.out.println("ERROR: No name="+name+" found in "+table+".");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid table given.", e);
    } catch (IOException e) {
      System.out.println(e);
    }
    return null;
  }
  public ArrayList<String> getBidsForItem(Item item) { //returns list of bids for item
    if (item == null) {
      return null;
    }
    ArrayList<String> output = new ArrayList<String>();
    try(FileReader file = new FileReader(bidsTable)) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching itemId
        if (fieldFromEntry(line,2).equals(item.Id)) {
          output.add(fieldFromEntry(line,3));
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
    if (user == null) {
      return null;
    }
    ArrayList<String> output = new ArrayList<String>();
    try(FileReader file = new FileReader(bidsTable)) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching userId
        if (fieldFromEntry(line,1).equals(user.Id)) {
          output.add(fieldFromEntry(line,3));
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
        throw new IllegalArgumentException("ERROR: Invalid input length.");
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
    if (checkForCommaInString(userName) || checkForCommaInString(itemName) || checkForCommaInString(value) || userName.equals("") || itemName.equals("") || value.equals("")) {
      System.out.println("ERROR: Invalid input");
      return;
    }
    //first find currenct highest bid for item
    Item item = (Item) getEntryFromName(itemsTable, itemName);

    //error if item does not exist
    if (item == null) {
      System.out.println("ERROR: No item by the name "+itemName+".");
      return;
    }

    //check highest bid
    String highest_bid = item.getHighestBid();
    if(item.stringNumericValueCompare(highest_bid,value)) {
      System.out.println("\nBid denied, lower than current bid: "+highest_bid);
      return;
    }

    //construct bid data structure from given userName and itemName
    Bid bid = new Bid(newId(bidsTable),getEntryFromName(usersTable, userName).Id, getEntryFromName(itemsTable, itemName).Id, value);
    //append to storage file
    appendFile(bidsTable, bid.getDbTuple());
    System.out.println("Bid success, "+userName+" is new highest bidder on "+itemName+" with bid "+value);

  }
  public void addItem(String name) {
    //check input for invalid characters
    if (checkForCommaInString(name) || name.equals("")) {
      System.out.println("ERROR: Invalid input");
      return;
    }
    //contruct item
    Item item = new Item(newId(itemsTable),name);
    //append to storage
    appendFile(itemsTable, item.getDbTuple());
    System.out.println("Item "+name+" added.");
  }
  public void addUser(String name) {
    //check input for invalid characters
    if (checkForCommaInString(name) || name.equals("")) {
      System.out.println("ERROR: Invalid input");
      return;
    }
    //contruct item
    User user = new User(newId(usersTable), name);
    //append to storage
    appendFile(usersTable, user.getDbTuple());
    System.out.println("User "+name+" added.");
  }


abstract class DBentry {
  public String Id;
  public abstract String getDbTuple();
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
  public String name;
  public Item(String Id, String name) {
    this.Id = Id;
    this.name = name;
  }
  public String getDbTuple() {
    return Id +","+ name;
  }
  public String getHighestBid() {
    String highest_bid = "0";
    //find highest bid
    try(FileReader file = new FileReader(bidsTable)) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching itemId and compare value with highest_bid
        // System.out.println(highest_bid, )
        if (fieldFromEntry(line,2).equals(Id) && stringNumericValueCompare(fieldFromEntry(line,3),highest_bid)) {
          //set new highest bid
          highest_bid = fieldFromEntry(line,3);
        }
      }
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid table given.", e);
    } catch (IOException e) {
      System.out.println(e);
    }
    return highest_bid;
  }
  boolean stringNumericValueCompare(String str1, String str2) {
    if (Integer.parseInt(str1) > Integer.parseInt(str2)) {
      return true;
    }
    return false;
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
}
