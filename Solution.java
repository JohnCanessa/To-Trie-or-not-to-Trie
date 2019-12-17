import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/*
 * Will implement a Trie with some add ons to see if we can solve a challenge.
 * I do not believe that a simple Trie would do the trick.
 * Perhaps by adding some features we could get close.
 */
public class Solution {

  // **** open scanner ****
  static Scanner sc = new Scanner(System.in);

  /*
   *
   */
  public static class InputStreamConsumer extends Thread {

    private InputStream is;

    public InputStreamConsumer(InputStream is) {
      this.is = is;
    }

    @Override
    public void run() {

      try {
        int value = -1;
        while ((value = is.read()) != -1) {
          System.out.print((char) value);
        }
      } catch (IOException exp) {
        exp.printStackTrace();
      }

    }

  }

  /*
   * Check word as you type. In progress. Will revisit using JNA.
   */
  static void checkAsYouType(Trie trie) throws IOException, InterruptedException {

    // **** ****
    // ProcessBuilder pb = new ProcessBuilder("cmd", "/C", "dir");
    ProcessBuilder pb = new ProcessBuilder("cmd", "/C");

    // **** ****
    pb.redirectError();

    // **** start new process ****
    Process p = pb.start();

    // **** ****
    InputStream is = p.getInputStream();
    InputStreamConsumer isc = new InputStreamConsumer(is);

    // **** ****
    isc.start();

    // **** wait until process exits ****
    int exitCode = p.waitFor();

    // **** ****
    isc.join();

    // **** ****
    System.out.println("checkAsYouType <<< process terminated exitCode: " + exitCode);
  }

  /*
   * Loop prompting user to delete complete words from the trie. Words are deleted
   * from the trie one at a time.
   */
  static void deleteWords(Trie trie) {

    // **** ****
    boolean done = false;

    // **** loop until done ****
    while (!done) {

      // **** generate and display list of trie words ****
      List<String> list = new ArrayList<String>();
      trie.genList(trie.getRoot(), list);
      System.out.println("deleteWords <<< list: " + list.toString());

      // **** prompt user and get word to delete ****
      System.out.print("deleteWords >>> word to delete [single <Enter> to exit]: ");
      String word = sc.nextLine();

      // // ???? ????
      // System.out.println("deleteWords <<< word ==>" + word + "<==");

      // **** check if we are done ****
      if (word.length() == 0) {
        done = true;
        continue;
      }

      // **** delete this word from trie ****
      trie.delete(word);

    }

  }

  /*
   * Delete words from the trie using a recursive approach.
   */
  static void deleteWordsRec(Trie trie) {

  }

  /*
   * Check word using Trie.
   */
  static void checkWord(ArrayList<String> al) {

    // **** create Trie with the specified list of words ****
    Trie trie = new Trie(al);

    // **** delete words from the trie ****
    deleteWords(trie);
    // deleteWordsRec(trie);

    // **** ****
    boolean done = false;
    String string = "";
    TrieNode lastNode = trie.getRoot();

    // **** loop until done ****
    while (!done) {

      // **** generate and display list of trie words ****
      List<String> list = new ArrayList<String>();
      trie.genList(trie.getRoot(), list);
      System.out.println("checkWord <<< list: " + list.toString());

      // **** prompt for the next character ****
      System.out.println("checkWord <<< ['-' remove last char, '=' clear word or '*' exit]");
      System.out.print("checkWord >>> SINGLE character followed by <Enter>: " + string);

      // **** get input from the user ****
      String str = sc.nextLine();

      // **** check if no characters were entered ****
      if (str.length() == 0)
        continue;

      // **** check if user entered: '*' (exit) ****
      if (str.charAt(0) == '*') {
        done = true;
        continue;
      }

      // **** check if user entered '=' (clear current word) ****
      if (str.charAt(0) == '=') {
        string = "";
        lastNode = trie.getRoot();
        continue;
      }

      // **** check if user entered '-' (clear last character) ****
      if ((string.length() != 0) && (str.charAt(0) == '-')) {
        string = string.substring(0, string.length() - 1);
        lastNode = lastNode.prev;
        if (lastNode.end) {
          System.out.println("checkWord <<< string ==>" + string + "<== is a complete word");
        }
        continue;
      }

      // **** extract first character only (just in case) ****
      char ch = str.charAt(0);

      // **** look for the character in the last node ****
      TrieNode node = lastNode.children.get(ch);

      // **** character NOT found ****
      if (node == null) {
        System.out.println("checkWord <<< string ==>" + string + ch + "<== not found !!!");
      }

      // **** character found ****
      else {

        // **** update the last node in the trie ****
        lastNode = node;

        // **** append character to string ****
        string += ch;

        // **** check if we have a complete word ****
        if (lastNode.end) {
          System.out.println("checkWord <<< string ==>" + string + "<== is a complete word");
        }
      }
    }

  }

  /*
   * Slide a window over a gene computing its total health.
   */
  static int slidingWindow(ArrayList<String> al, ArrayList<Integer> ghal) {

    // **** total health for the specified gene ****
    int totalHealth = 0;

    // **** read string with start, end and gene to be processed ****
    String string = sc.nextLine();

    // // ???? ????
    // System.out.println("slidingWindow <<< string ==>" + string + "<==");

    // **** split string ****
    String[] strings = string.split(" ");

    // **** ****
    int start = Integer.parseInt(strings[0]);
    int end = Integer.parseInt(strings[1]);
    String gene = strings[2];

    // **** populate trie with necessary genes ****
    Trie trie = new Trie();
    for (int i = start; i <= end; i++)
      trie.insert(al.get(i));

    // // ???? generate and display list of trie words ????
    // List<String> list = new ArrayList<String>();
    // trie.genList(trie.getRoot(), list);
    // System.out.println("slidingWindow <<< list: " + list.toString());

    // **** populate hash map that associates genes with their health benefit ****
    HashMap<String, Integer> ghhm = new HashMap<String, Integer>();
    for (int i = start; i <= end; i++) {
      String str = al.get(i);
      if (!ghhm.containsKey(str)) {
        ghhm.put(str, ghal.get(i));
      } else {
        int val = ghhm.get(str);
        val += ghal.get(i);
        ghhm.put(str, val);
      }
    }

    // **** process string ****
    int sw = 0;
    int ew = 1;
    while ((sw < ew) && (sw < gene.length()) && (ew <= gene.length())) {

      // **** fill window ****
      String window = gene.substring(sw, ew);

      // **** look up window in trie ****
      boolean found = trie.search(window);

      // **** ****
      if (found) {

        // ***** increment total health as needed ****
        totalHealth += ghhm.get(window);

        // **** adjust window ****
        sw++;
      } else {

        // **** get node for last character in the window ****
        TrieNode node = trie.getLastNode(window);

        // **** not end of word ****
        if ((node != null) && (!node.end)) {
          ew++;
        } else {
          sw++;
        }
      }

      // **** window must contain at least one character ****
      if (sw == ew)
        ew++;

    }

    // **** return total health for the specified gene ****
    return totalHealth;
  }

  /*
   * Factorial recursive example.
   */
  static int factorial(int n) {

    // **** base case ****
    if (n <= 0)
      return 1;

    // **** recursive case ****
    return n * factorial(n - 1);
  }

  /*
   * Sum of first n integers recursive example.
   */
  static int sum(int n) {

    // **** base case ****
    if (n <= 0)
      return 0;

    // **** recursive case ****
    return n + sum(n - 1);
  }

  /*
   * Reverse word.
   */
  static void reverse(String word, Stack<Character> stack) {

    // **** base case ****
    if (word.length() == 0) {
      return;
    }

    // **** ****
    stack.push(word.charAt(0));

    // **** recursive case ****
    reverse(word.substring(1, word.length()), stack);

    // **** ****
    System.out.print(stack.pop());
  }

  /*
   * Test scaffolding.
   */
  public static void main(String[] args) throws IOException, InterruptedException {

    // // ???? start reverse string test ????
    // Stack<Character> stack = new Stack<Character>();
    // String word = "car";
    // System.out.print("main <<< word: " + word + " reverse: ");
    // reverse(word, stack);
    // // ???? end reverse string test ????

    // // ???? start factorial test ????
    // System.out.print("main <<< f: ");
    // int f = sc.nextInt();
    // System.out.println("main <<< factorial: " + factorial(f));
    // System.exit(-1);
    // // ???? end factorial test ????

    // // ???? start factorial test ????
    // System.out.print("main <<< s: ");
    // int s = sc.nextInt();
    // System.out.println("main <<< sum: " + sum(s));
    // System.exit(-1);
    // // ???? end factorial test ????

    // **** list of words that may be used in a trie ****
    ArrayList<String> al = new ArrayList<String>();

    // **** get the total number of words to read ****
    int n = sc.nextInt();
    sc.nextLine();

    // **** read the words into a string ****
    String string = sc.nextLine();

    // **** split the words ****
    String[] strings = string.split(" ");

    // **** add the words into the array list ****
    for (int i = 0; i < n; i++) {
      al.add(strings[i]);
    }

    // **** check if we did NOT read all the words into the array list ****
    if (n != al.size()) {
      System.err.println("main <<< UNEXPECTED n: " + n + " != al.size: " + al.size());
      System.exit(-1);
    }

    // **** check word as you type one character at a time****
    checkWord(al);

    // // **** read health values ****
    // string = sc.nextLine();

    // // // ???? ????
    // // System.out.println("main <<< string ==>" + string + "<==");

    // // **** split the health values ****
    // String[] val = string.split(" ");

    // // **** load the gene health array list ****
    // ArrayList<Integer> ghal = new ArrayList<Integer>();
    // for (int i = 0; i < al.size(); i++) {
    // ghal.add(Integer.parseInt(val[i]));
    // }

    // // // ???? ????
    // // System.out.println("main <<< ghal: " + ghal.toString());

    // // **** get the number of test cases ****
    // int t = sc.nextInt();
    // sc.nextLine();

    // // // ???? ????
    // // System.out.println("main <<< t: " + t);

    // // **** ****
    // int minHealth = Integer.MAX_VALUE;
    // int maxHealth = Integer.MIN_VALUE;

    // // **** loop through the test cases ****
    // for (int i = 0; i < t; i++) {

    // // **** check prefix and remove as found ****
    // int totalHealth = slidingWindow(al, ghal);

    // // // ???? ????
    // // System.out.println("main <<< totalHealth: " + totalHealth);

    // // **** update the min health ****
    // if (totalHealth < minHealth) {
    // minHealth = totalHealth;
    // }

    // // **** update the max health ****
    // if (totalHealth > maxHealth) {
    // maxHealth = totalHealth;
    // }

    // }

    // // **** display min and max health ****
    // System.out.println(minHealth + " " + maxHealth);

    // **** close scanner ****
    sc.close();
  }

}