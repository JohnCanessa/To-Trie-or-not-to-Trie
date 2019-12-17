import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;

/*
 * Trie class.
 */
public class Trie {

  // **** root for our trie ****
  private TrieNode root;

  // **** ****
  private Stack<String> tempStack = new Stack<String>();

  /*
   * Constructor empty Trie.
   */
  public Trie() {
    root = new TrieNode();
    tempStack = new Stack<String>();
  }

  /*
   * Constructor using an array list.
   */
  public Trie(ArrayList<String> al) {

    // **** ****
    root = new TrieNode();
    tempStack = new Stack<String>();

    // **** populate the Trie ****
    for (int i = 0; i < al.size(); i++) {
      insert(al.get(i));
    }
  }

  /*
   * Insert single (complete) word into Trie.
   */
  public void insert(String str) {

    // **** check if this word is a duplicate ****
    if (search(str)) {
      return;
    }

    // **** used in the loop ****
    TrieNode current = root;
    StringBuilder sb = new StringBuilder();

    // **** traverse the characters in the string ****
    for (int i = 0; i < str.length(); i++) {

      // **** get the current character ****
      char ch = str.charAt(i);

      // **** add character to string builder ****
      sb.append(ch);

      // **** get the child node for this character ****
      TrieNode node = current.children.get(ch);

      // **** the character is NOT in the Trie ****
      if (node == null) {

        // **** create node for this character ****
        node = new TrieNode();

        // **** set the node ****
        node.ch = ch;
        node.prev = current;
        node.word = sb.toString();

        // **** ****
        current.children.put(ch, node);
      }

      // **** update current for next pass ****
      current = node;

    }

    // **** set end of word ****
    current.end = true;
  }

  /*
   * Search for the specified string in the Trie. Returns true is full word is
   * found. Returns false if full word is NOT found.
   */
  public boolean search(String str) {

    // **** start at the root of the trie ****
    TrieNode current = root;

    // **** traverse the characters in the string looking them up in the trie ****
    for (int i = 0; i < str.length(); i++) {

      // **** get the current character ****
      char ch = str.charAt(i);

      // **** get the child node for this character ****
      TrieNode node = current.children.get(ch);

      // **** check if this characters was NOT found ****
      if (node == null)
        return false;

      // **** ****
      current = node;
    }

    // **** return if this string is a full word or not ****
    return current.end;
  }

  /*
   * Return the root node of the Trie.
   */
  public TrieNode getRoot() {
    return this.root;
  }

  /*
   * Display the contents of the trie. This is a recursive call.
   */
  public void display(TrieNode current, String str) {

    // **** check if end of word ****
    if (current.end)
      System.out.println(str);

    // **** get the set of child entries ****
    Set<Entry<Character, TrieNode>> set = current.children.entrySet();

    // **** iterate through the entries ****
    Iterator<Entry<Character, TrieNode>> it = set.iterator();
    while (it.hasNext()) {

      // **** get the next entry ****
      Entry<Character, TrieNode> entry = it.next();

      // **** get the character for this entry ****
      char ch = entry.getKey();

      // **** append the character to the string ****
      if (ch != '\000')
        str += ch;

      // **** process the next entry ****
      display(entry.getValue(), str);
    }
  }

  /*
   * Print the contents of the Trie.
   */
  public void printTrie() {

    // **** to load the stack ****
    printTrie(getRoot(), "");

    // **** dump stack ****
    while (!tempStack.isEmpty())
      System.out.println(tempStack.remove(tempStack.size() - 1));

  }

  /*
   * Print contents of the Trie. Recursive call.
   */
  public void printTrie(TrieNode node, String s) {

    // **** ****
    String strSoFar = s;

    // **** ****
    if (node.ch != '\000')
      strSoFar += String.valueOf(node.ch);

    // **** ****
    if (node.end) {

      // **** make copy of this string (to print in ascending order) ****
      tempStack.push(strSoFar);

      // **** ****
      return;

    } else {

      Stack<TrieNode> stack = new Stack<TrieNode>();
      Iterator<TrieNode> itr = node.children.values().iterator();

      // **** ****
      while (itr.hasNext()) {
        stack.add(itr.next());
      }

      // **** display words in ascending order ****
      while (!stack.empty()) {
        TrieNode t = stack.pop();
        printTrie(t, strSoFar);
      }

    }

  }

  /*
   * Get all words with the specified prefix starting at the specified node. This
   * is a recursive call. Should be a private method.
   */
  public List<String> getAll(String prefix, TrieNode node) {

    // **** new list ****
    List<String> al = new ArrayList<>();

    // **** add this prefix to the list ****
    if (node.end) {
      al.add(prefix);
    }

    // **** loop once per character ****
    for (Map.Entry<Character, TrieNode> child : node.children.entrySet()) {

      // **** add character to the prefix ****
      List<String> subSuffix = getAll(prefix + child.getKey(), child.getValue());

      // **** add subfix ****
      al.addAll(subSuffix);
    }

    // **** ****
    return al;
  }

  /*
   * Return a list with all words that start with the specified prefix. If the
   * prefix is ""; then all words in the trie are returned.
   */
  public List<String> returnAllChildren(String prefix) {

    // **** ****
    List<String> al = new ArrayList<>();

    // **** start at the root node of the trie ****
    TrieNode current = root;

    // **** ****
    for (int i = 0; i < prefix.length(); i++) {

      // **** get the current character from the prefix ****
      char ch = prefix.charAt(i);

      // **** get the child node for this character ****
      TrieNode node = current.children.get(ch);

      // **** check if there is no such character (prefix) ****
      if (node == null) {
        return al;
      }

      // **** to continue loop with this node ****
      current = node;
    }

    // **** get all words starting at this node with the specified prefix ****
    return getAll(prefix, current);
  }

  /*
   * Return the node of the last character in the specified string.
   */
  public TrieNode getLastNode(String str) {

    // **** perform sanity checks ****
    if (str.length() == 0)
      return null;

    // **** current node ****
    TrieNode current = root;

    // **** traverse the string ****
    for (int i = 0; i < str.length(); i++) {

      // **** get the current char ****
      char ch = str.charAt(i);

      // **** get trie node associated with this character ****
      TrieNode child = current.children.get(ch);

      // **** something is wrong ****
      if (child == null)
        return null;

      // **** to continue in loop ****
      current = child;
    }

    // **** return node associated with last character of the string ****
    return current;
  }

  /*
   * Dump contents of trie. This is a recursive call.
   */
  public void dump(TrieNode node, String str) {

    // **** end condition ****
    if (node == null)
      return;

    // **** print complete word ****
    if (node.end) {
      System.out.println(str);
    }

    // **** get set of children ****
    Set<Entry<Character, TrieNode>> set = node.children.entrySet();

    // **** loop for all children ****
    for (Entry<Character, TrieNode> entry : set) {
      char ch = entry.getKey();
      dump(entry.getValue(), str + ch);
    }
  }

  /*
   * Traverse the trie collecting all strings from end nodes.
   */
  public void genList(TrieNode root, List<String> list) {

    // **** ****
    Set<Entry<Character, TrieNode>> set = root.children.entrySet();

    // **** ****
    if (set == null) {
      return;
    }

    // **** ****
    for (Entry<Character, TrieNode> entry : set) {

      // **** add word to the list ****
      if (entry.getValue().end) {
        list.add(entry.getValue().word);
      }

      // **** ****
      genList(entry.getValue(), list);
    }

  }

  /*
   * Delete the specified (complete) word from the trie. Returns without message
   * if word is not found.
   */
  public void delete(String word) {

    // **** start at the root of the trie ****
    TrieNode current = this.getRoot();

    // **** get to the last node for this word ****
    for (char ch : word.toCharArray()) {

      // **** get the child node for the current character in current node ****
      TrieNode child = current.children.get(ch);

      // **** check if child not found ****
      if (child == null) {
        return;
      }

      // **** get ready for the next character ****
      current = child;
    }

    // **** check if this node is NOT an end node ****
    if (!current.end) {
      return;
    }

    // **** this is no longer going to be a word ****
    current.end = false;

    // **** loop deleting nodes (as needed) ****
    TrieNode prev = current.prev;
    while (current != root) {

      // **** reference previous node ****
      prev = current.prev;

      // *** get number of children ****
      int numChildren = current.children.size();

      // **** node part of multiple words ****
      if (numChildren != 0) {
        break;
      }

      // **** character to delete ****
      char ch = current.ch;

      // **** remove character from previous node ****
      prev.children.remove(ch);

      // **** set current to our previous node ****
      current = prev;

      // **** check if this is an end of word ****
      if (current.end)
        break;
    }

    // **** delete character from root node (if needed) ****
    if (current == root) {
      char ch = word.charAt(0);
      current.children.remove(ch);
    }

  }

}
