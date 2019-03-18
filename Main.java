import java.io.*;
class Main {

  // String directory = System.getProperty("user.home");
  // String fileName = "sample.txt";
  // String absolutePath = directory + File.separator + fileName;

  //{id, name}
  String itemsFilePath = "./items.txt";
  //{id, user, item}
  String bidsFilePath = "./bids.txt";



  public static void main(String[] args) {
    new Main();
  }

  public Main() {
    DBentry entry = read_file("items", "000");
    entry.print();
  }

  void write_file(String fileLocation, String msg) {
    System.out.println("writing file");
    try(FileWriter file = new FileWriter(fileLocation, true)) {
        file.append(msg);
        file.flush();
        file.close();
    } catch (IOException e) {
        System.out.println("write error: "+ e);
    }
}
  //add readType parameter for different reads
  DBentry read_file(String type, String Id) {
    DBentry entry;
    //check invalid type of return data
    if (!(type.equals("bids") || type.equals("items"))) {
      System.out.println("ERROR: invalid type.");
      return null;
    }

    try(FileReader file = new FileReader("./"+type+".txt")) {
      BufferedReader inStream = new BufferedReader(file);
      String line;
      while((line = inStream.readLine()) != null) {
        //check each line for matching ID
        if (line.substring(0,3).equals(Id)) {
          //create bid object
          if (type.equals("bids")) {
            entry = new Bid(line.substring(0,3),line.substring(3,6),line.substring(6,9));
          } else {
            entry = new Item(line.substring(0,3),line.substring(3,line.length()));
          }
          return entry;
        }
        // System.out.println(line);
      }
    } catch (FileNotFoundException e) {
      System.out.println(e);
    } catch (IOException e) {
      System.out.println(e);
    }
    return null;
  }


}

abstract class DBentry {
  public String Id;
  public abstract void print();
}

class Bid extends DBentry {
  public String userId;
  public String itemId;

  public Bid(String Id, String userId, String itemId) {
    this.Id = Id;
    this.userId = userId;
    this.itemId = itemId;
  }

  public void print() {
    System.out.println(Id + userId + itemId);
  }
}
class Item extends DBentry {
  public String name;

  public Item(String Id, String name) {
    this.Id = Id;
    this.name = name;
  }

  public void print() {
    System.out.println(Id + name);
  }
}
