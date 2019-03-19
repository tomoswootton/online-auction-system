import java.io.*;
class Main {

  String[] tables = {"bids", "items", "users"};

  public static void main(String[] args) {
    new Main();
  }

  public Main() {
    // DBentry entry = readIdFromName("items", "000");
    // System.out.println(entry.getStringRepresentation());
    listTable("items");
    addItem("moose");
    listTable("items");

  }
  /*
    DB
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
  //find entry given Id
  DBentry readNameFromId(String table, String Id) {
    DBentry entry = null;

    try(FileReader file = new FileReader("./"+table+".txt")) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching ID
        if (line.substring(0,3).equals(Id)) {
          //create bid object
          if (table.equals("bids")) {
            entry = new Bid(line.substring(0,3),line.substring(3,6),line.substring(6,9));
          } else if (table.equals("items")){
            entry = new Item(line.substring(0,3),line.substring(3,line.length()));
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
  //find entry given Name
  DBentry readIdFromName(String table, String name) {
    DBentry entry = null;
    //check invalid type of return data

    try(FileReader file = new FileReader("./"+table+".txt")) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching ID
        if (line.substring(3,line.length()).equals(name)) {
          //create bid object
          if (table.equals("items")){
            entry = new Item(line.substring(0,3),line.substring(3,line.length()));
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
  //append entry to file
  void appendFile(String table, String msg) {
    try(FileWriter file = new FileWriter("./"+table+".txt", true)) {
      file.append(msg);
      file.flush();
      file.close();
    } catch (IOException e) {
      System.out.println("write error: "+ e);
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

  /*
    Misc
  */
  String makeIdFromNumEntries(int numEntries) {
    String numS = Integer.toString(numEntries);
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


  void addBid(String userName, String itemName) {
    //construct bid data structure from given userName and itemName
    Bid bid = new Bid(makeIdFromNumEntries(numEntries("bids")),readIdFromName("users", userName).Id, readIdFromName("items", itemName).Id);

    System.out.println("Id: "+ bid.Id);
    System.out.println("userId: "+ bid.userId);
    System.out.println("itemId: "+ bid.itemId);
    //append to storage file
    appendFile("bids", bid.getStringRepresentation());
  }
  void addItem(String name) {
    //contruct item
    Item item = new Item(makeIdFromNumEntries(numEntries("items")), name);
    //append to storage
    appendFile("items", item.getStringRepresentation());
  }
  void addUser(String name) {
    //contruct item
    User user = new User(makeIdFromNumEntries(numEntries("users")), name);
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

  public Bid(String Id, String userId, String itemId) {
    this.Id = Id;
    this.userId = userId;
    this.itemId = itemId;
  }
  public String getStringRepresentation() {
    return Id + userId + itemId;
  }
}

class Item extends DBentry {
  public String name;

  public Item(String Id, String name) {
    this.Id = Id;
    this.name = name;
  }
  public String getStringRepresentation() {
    return Id + name;
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
