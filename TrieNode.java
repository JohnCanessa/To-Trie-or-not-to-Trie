import java.util.TreeMap;

/*
 * Node for our Trie.
 */
public class TrieNode {

  // **** members ****
  public char ch;
  TreeMap<Character, TrieNode> children;
  boolean end;

  String word;
  TrieNode prev;

  /*
   * Constructor with character.
   */
  public TrieNode(char ch) {
    this.ch = ch;
    this.children = new TreeMap<Character, TrieNode>();
    this.end = false;

    this.prev = null;
    this.word = "";
  }

  /*
   * Constructor without character.
   */
  public TrieNode() {
    this.children = new TreeMap<Character, TrieNode>();
    this.end = false;

    this.prev = null;
    this.word = "";
  }

  /*
   * Gets the child node associated with this character.
   */
  public TrieNode getChild(char ch) {
    if (this.children != null) {
      return this.children.get(ch);
    } else {
      return null;
    }
  }

  /*
   * Add child to children.
   */
  public void addChild(TrieNode child) {
    children.put(child.ch, child);
  }

  /*
   * Returns a string of the specified node.
   */
  public String toString() {

    // **** for efficiency ****
    StringBuilder sb = new StringBuilder();

    // **** ****
    sb.append("ch: " + this.ch);
    sb.append(" word ==>" + this.word + "<==");
    sb.append(" endOfWord: " + this.end);

    // **** return a string ****
    return sb.toString();
  }

}