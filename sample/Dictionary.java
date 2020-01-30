package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

//***********************************
// Gabriel Urbaitis
//
// The Dictionary class is used for
// reading in and storing all delimited
// words in OpenEnglishWordList.txt
// longer than 3 letters.
//***********************************
public class Dictionary
{
  private HashSet<String> set;

  public Dictionary()
  {
    set = new HashSet<String>();
    readInDictionary();
  }
  //***********************************
  //Input: None
  //Returns set, a hashset of all
  //delimited words longer than 3 in
  //the text file
  //Returns the words in the dictionary
  //***********************************
  public HashSet<String> getSet()
  {
    return set;
  }
  //***********************************
  //Input: None
  //Returns void
  //Scans the text file and assigns
  //a string to each set of delimited
  //text. If the string is longer than
  //3 characters it adds it to a hashset
  //for storage.
  //***********************************
  private void readInDictionary()
  {
    Scanner file = null;
    try
    {
      file = new Scanner(new File("src/OpenEnglishWordList.txt"));
      while (file.hasNext())
      {
        String current = file.next().trim();
        if (current.length() < 3) continue; //don't take words less than the 3 letter minimum
        set.add(current);
      }
    } catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }
}
