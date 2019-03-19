import java.io.*;
class Main {

  // String directory = System.getProperty("user.home");
  // String fileName = "sample.txt";
  // String absolutePath = directory + File.separator + fileName;

  String[] tables = {"bids", "items", "users"};


  public static void main(String[] args) {
    new Main();
  }

  public Main() {
    DBentry entry = read_file_from_Id("items", "000");
    System.out.println(entry.getStringRepresentation());
  }

  boolean inTablesArray(String string) {
    for (String s : tables) {
      if (s.equals(string)) {
        return true;
      }
    }
    return false;
  }

  //find entry given Id
  DBentry read_file_from_Id(String table, String Id) {
    DBentry entry = null;
    //check invalid type of return data
    if (!inTablesArray(table)) {
      System.out.println("ERROR: invalid table.");
      return null;
    }

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
      System.out.println("ERROR: No Id found.");
    } catch (FileNotFoundException e) {
      System.out.println(e);
    } catch (IOException e) {
      System.out.println(e);
    }
    return null;
  }
  //find entry given Name
  DBentry read_file_from_name(String table, String name) {
    DBentry entry = null;
    //check invalid type of return data
    if (!(table.equals("items") || table.equals("users"))) {
      System.out.println("ERROR: Only items and users have names");
      return null;
    }

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
      System.out.println(e);
    } catch (IOException e) {
      System.out.println(e);
    }
    return null;
  }
  //append entry to file
  void append_file(String table, String msg) {
    try(FileWriter file = new FileWriter("./"+table+".txt", true)) {
      file.append(msg);
      file.flush();
      file.close();
    } catch (IOException e) {
      System.out.println("write error: "+ e);
    }
  }

  // int num_entries(String table) {
  //   try(FileReader file = new FileReader("./"+table+".txt")) {
  //     BufferedReader inStream = new BufferedReader(file);
  //     String line;
  //     while((line = inStream.readLine()) != null) {
  // }

  // add_bid(String userName, String itemName) {
  //   Bid bid = new Bid()
  //   append_file("bids", bid.getStringRepresentation());
  // }
  //
  // add_item(Item item) {
  //   append_file("items", item.getStringRepresentation());
  // }
  // add_user(User user) {
  //   append_file("users", user.getStringRepresentation());
  // }


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
