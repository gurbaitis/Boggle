package sample;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

//***********************************
// Gabriel Urbaitis
//
// The Board class handles all of
//the logic associated with creating
//a board of a given size, filled
//with random letters, no more than
//4 of each and with increased
//probability of setting a U next to
//a Q. It contains methods for running
//through all possible legal
//combinations of letters on the board.
//***********************************
public class Board
{
  private int size;
  private Character[][] board;
  private HashSet<String> validWords = new HashSet<String>();
  private boolean[][] visited;
  private boolean firstTime = true;
  private Dictionary dictionary;

  public Board(int size)
  {
    this.size = size;
    board = new Character[size][size];
    createNewBoard();
    dictionary = new Dictionary();
    visited = new boolean[size][size];
    findMatches();
  }
  //***********************************
  //Input: None
  //Returns board, a 2 dimensional
  //Character array representing
  //the board
  //Returns the board
  //***********************************
  public Character[][] getBoard()
  {
    return board;
  }
  //***********************************
  //Input: None
  //Returns dictionary, a data structure
  //with the list of english words
  //Returns the dictionary
  //***********************************
  public Dictionary getDictionary()
  {
    return dictionary;
  }
  //***********************************
  //Input: None
  //Returns validWords, a hashset of all
  //possible legal words inputted by the
  //player.
  //Returns the valid words of the board
  //***********************************
  public HashSet<String> getValidWords()
  {
    return validWords;
  }
  //***********************************
  //Input: None
  //Returns void
  //Randomly selects letters from the
  //alphabet and adds them to a 1
  //dimensional array, with the
  //stipulation that no letter may be
  //selected more than 4 times and that
  //U has a 50% chance of appearing next
  //to Q. Creates a 2 dimensional array
  //and stores each of the letters to
  //represent the board.
  //***********************************
  private void createNewBoard()
  {
    HashMap<Character, Integer> letterCounter = new HashMap<>(26);
    letterCounter.put('a', 0);
    letterCounter.put('b', 0);
    letterCounter.put('c', 0);
    letterCounter.put('d', 0);
    letterCounter.put('e', 0);
    letterCounter.put('f', 0);
    letterCounter.put('g', 0);
    letterCounter.put('h', 0);
    letterCounter.put('i', 0);
    letterCounter.put('j', 0);
    letterCounter.put('k', 0);
    letterCounter.put('l', 0);
    letterCounter.put('m', 0);
    letterCounter.put('n', 0);
    letterCounter.put('o', 0);
    letterCounter.put('p', 0);
    letterCounter.put('q', 0);
    letterCounter.put('r', 0);
    letterCounter.put('s', 0);
    letterCounter.put('t', 0);
    letterCounter.put('u', 0);
    letterCounter.put('v', 0);
    letterCounter.put('w', 0);
    letterCounter.put('x', 0);
    letterCounter.put('y', 0);
    letterCounter.put('z', 0);
    final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    Random r = new Random();
    boolean needsAssignment;
    Character[] toPutInBoard = new Character[size * size];
    for (int h = 0; h < toPutInBoard.length; h++)
    {
      needsAssignment = true;
      while (needsAssignment)
      {
        Character newChar = alphabet.charAt(r.nextInt(alphabet.length()));
        if (letterCounter.get(newChar) < 4)
        {
          if (newChar == 'Q' && h < (toPutInBoard.length - 1))
          {
            letterCounter.replace(newChar, letterCounter.get(newChar), letterCounter.get(newChar) + 1);
            toPutInBoard[h] = newChar;
            if (r.nextInt(2) == 1)//50-50 chance of adding a U next to the Q
            {
              Character u = 'U';
              letterCounter.replace(u, letterCounter.get(u), letterCounter.get(u) + 1);
              toPutInBoard[h + 1] = u;
              h++;
            }
            needsAssignment = false;
          } else
          {
            letterCounter.replace(newChar, letterCounter.get(newChar), letterCounter.get(newChar) + 1);
            toPutInBoard[h] = newChar;
            needsAssignment = false;
          }
        } else
        {
          letterCounter.replace(newChar, letterCounter.get(newChar), letterCounter.get(newChar) + 1);
        }
      }
    }
    int h = 0;
    for (int i = 0; i < size; i++)
    {
      for (int j = 0; j < size; j++)
      {
        board[i][j] = toPutInBoard[h];
        h++;
      }

    }
  }
  //***********************************
  //Input: String beginning, the current
  //combination of letters whose
  //validity is being tested.
  //HashSet<String> startingSet, the
  //set to filter from
  //Returns a HashSet<String> of all
  //the strings in the starting set,
  //that begin with the inputted string.
  //Filters down the inputted set to
  //only the strings in the set that
  //begin with the inputted string.
  //***********************************
  private HashSet<String> filter(String beginning, HashSet<String> startingSet)
  {
    HashSet<String> endingSet = new HashSet<>();
    for (String word : startingSet)
    {
      if (word.startsWith(beginning))
      {
        endingSet.add(word);
      }
    }
    return endingSet;
  }
  //***********************************
  //Input: ints x and y, the coordinates
  //to begin the search at. String toCheck,
  //the current combination of letters
  //leading up to the coordinate.
  //HashSet<String> wordsLeft, the
  //words left in the dictionary that
  //begin with the combination.
  //Returns void
  //Adds the letter at the current
  //spot on the board to the combination
  //being assessed. Filters the remaining
  //words in the dictionary to only ones
  //starting with the new combination.
  //If there are still words starting
  //with that combination, checks to
  //see if the current combination is a
  //word and then recursively runs
  //the same sequence on all neighbors
  //that have not been visited yet with
  //the new combination and smaller set
  //of words left. After all neighbors
  //have been assesed, sets the coordinate
  //to not visited so it can be assessed by
  //calls going in other directions.
  //***********************************
  private void findLocalMatches(int x, int y, String toCheck, HashSet<String> wordsLeft)
  {
    visited[x][y] = true;
    if (toCheck.length() == 1 && firstTime)
    {
      firstTime = false;
    } else
    {
      toCheck = toCheck + board[x][y];
    }
    HashSet<String> possibleWords = filter(toCheck, wordsLeft);
    if (possibleWords.size() != 0)
    {
      if (possibleWords.contains(toCheck))
      {
        validWords.add(toCheck);
      }
      if ((x + 1) < size && (y + 1) < size && !visited[x + 1][y + 1])
      {
        findLocalMatches(x + 1, y + 1, toCheck, possibleWords);
      }
      if ((x + 1) < size && !visited[x + 1][y])
      {
        findLocalMatches(x + 1, y, toCheck, possibleWords);
      }
      if ((x + 1) < size && (y - 1) >= 0 && !visited[x + 1][y - 1])
      {
        findLocalMatches(x + 1, y - 1, toCheck, possibleWords);
      }
      if ((y - 1) >= 0 && !visited[x][y - 1])
      {
        findLocalMatches(x, y - 1, toCheck, possibleWords);
      }
      if ((x - 1) >= 0 && (y - 1) >= 0 && !visited[x - 1][y - 1])
      {
        findLocalMatches(x - 1, y - 1, toCheck, possibleWords);
      }
      if ((x - 1) >= 0 && !visited[x - 1][y])
      {
        findLocalMatches(x - 1, y, toCheck, possibleWords);
      }
      if ((x - 1) >= 0 && (y + 1) < size && !visited[x - 1][y + 1])
      {
        findLocalMatches(x - 1, y + 1, toCheck, possibleWords);
      }
      if ((y + 1) < size && !visited[x][y + 1])
      {
        findLocalMatches(x, y + 1, toCheck, possibleWords);
      }
    }
    visited[x][y] = false;
  }
  //***********************************
  //Input: None
  //Returns void
  //Runs through the entire board and
  //finds valid words using combinations
  //of adjacent characters started at
  //the current index.
  //***********************************
  private void findMatches()
  {
    if (board != null)
    {
      for (int i = 0; i < size; i++)
      {
        for (int j = 0; j < size; j++)
        {
          findLocalMatches(i, j, board[i][j].toString(), dictionary.getSet());
          firstTime = true;
        }
      }
    }
  }

}
