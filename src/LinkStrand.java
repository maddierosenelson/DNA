import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

public class LinkStrand implements IDnaStrand, Iterator<String> {
	private int myAppends;
	private long mySize;
	private Node myHead;
	private Node myTail;
	private Node current;

	public class Node {
		private String value;
		private Node next;

		public Node(String myString) {
			value = myString;
			next = null;

		}

		public Node() {
			value = null;
			next = null;
		}
	}

	public LinkStrand() {
		initialize("");
	}

	public LinkStrand(String s) {
		initialize(s);
	}

	public void initialize(String source) {
		myHead = new Node(source);
		current = myTail = myHead;
		myAppends = 0;
		mySize = source.length();
	}

	public IDnaStrand cutAndSplice(String enzyme, String splicee) {
		int pos = 0;
		int start = 0;
		StringBuilder search = new StringBuilder(this.toString());
		boolean first = true;
		LinkStrand ret = null;
		while ((pos = search.indexOf(enzyme, pos)) >= 0) {
			if (first) {
				ret = new LinkStrand(search.substring(start, pos));
				first = false;
			} else {
				ret.append(search.substring(start, pos));
			}
			start = pos + enzyme.length();
			ret.append(splicee);
			pos++;
		}

		if (start < search.length()) {
			// NOTE: This is an important special case! If the enzyme
			// is never found, return an empty String.
			if (ret == null) {
				ret = new LinkStrand("");
			} else {
				ret.append(search.substring(start));
			}
		}
		return ret;
	}

	public long size() {
		return mySize;
	}

	public String strandInfo() {
		return this.getClass().getName();
	}

	public String getStats() {
		return String.format("# append calls = %d", myAppends);
	}

	public String toString() {
		StringBuilder myString = new StringBuilder();
		Node current = myHead;
		while (current != null) {
			myString.append(current.value);
			current = current.next;
		}
		return myString.toString();
	}

	public void append(IDnaStrand dna) {
		if (dna instanceof LinkStrand) {
			LinkStrand lsDNA = (LinkStrand) dna;
			// The node after this.myTail becomes the first node of the DNA
			// strand
			// and the last node of the DNA strand you are appending becomes the
			// new tail.
			this.myTail.next = lsDNA.myHead;
			this.myTail = lsDNA.myTail;
			myAppends++;
			mySize += lsDNA.size();
		} else {
			this.append(dna.toString());
		}
	}

	public void append(String dna) {
		// Creates a LinkStrand with the String dna, so the other append method
		// can be used.
		append(new LinkStrand(dna));
	}

	public IDnaStrand reverse() {
		Stack<String> myStack = new Stack<String>();
		Node current = myHead;
		HashMap<String, String> revMap = new HashMap<String, String>();
		while (current != null) {
			// If the String of the current node has not been reversed,
			// it is reversed in the following if loop.
			if (!revMap.containsKey(current.value)) {
				StringBuilder curString = new StringBuilder(current.value);
				revMap.put(current.value, curString.reverse().toString());
			}
			myStack.push(revMap.get(current.value));
			current = current.next;
		}
		// myStrand is created so the first node is equal to the first value on
		// the
		// Stack and we do not have an empty node at the beginning of the
		// LinkStrand.
		LinkStrand myStrand = new LinkStrand(myStack.pop());
		while (!myStack.isEmpty()) {
			myStrand.append(myStack.pop());
		}
		return myStrand;
	}

	public String next() {
		String str = current.value;
		current = current.next;
		return str;
	}

	public boolean hasNext() {
		return current != null;
	}
}