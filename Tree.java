package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode,
 * with fields for tag/text, first child and sibling.
 * 
 */
public class Tree {

	/**
	 * Root node
	 */
	TagNode root = null;

	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;

	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc
	 *            Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}

	/**
	 * Builds the DOM tree from input HTML file. The root of the tree is stored
	 * in the root field.
	 */
	public void build() {
		String temp;
		TagNode current = null;
		Stack<TagNode> stack = new Stack<TagNode>();

		while(sc.hasNextLine()){
			temp = sc.nextLine();
			if(tagCheck(temp, "start")){
				temp = temp.replace("<" , "");
				temp = temp.replace(">" , "");
				if(root == null){
					root = new TagNode(temp, null, null);
					stack.push(root);
					current = root;
					continue;
				}
				if(current.firstChild == null){
					current.firstChild = new TagNode(temp, null, null);
					stack.push(current.firstChild);
					current = stack.peek();
					continue;
				}
				current = current.firstChild;
				while(current.sibling!=null){
					current = current.sibling;
				}
				current.sibling = new TagNode(temp, null, null);
				stack.push(current.sibling);
				current = stack.peek();
				continue;
			}
			if(tagCheck(temp, "ending")){
				current = stack.pop();
				if(stack.isEmpty())
					continue;
				current = stack.peek();
				continue;
			}
			if(!tagCheck(temp, "start") && !tagCheck(temp, "ending")){
				if(current.firstChild == null){
					current.firstChild = new TagNode(temp, null, null);
					continue;
				}
				else{
					current = current.firstChild;
					while(current.sibling!=null){
						current = current.sibling;
					}
					current.sibling = new TagNode(temp, null, null);
					current = stack.peek();
					continue;
				}
			}
		}
	}

	private boolean tagCheck(String temp1, String temp2){
		if(temp2.equals("start")){
			if(temp1.startsWith("<") && temp1.endsWith(">") && !temp1.contains("/"))
				return true;
			return false;
		}
		if(temp2.equals("ending")){
			if(temp1.startsWith("</") && temp1.endsWith(">"))
				return true;
			return false;
		}
		return true;
	}
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag
	 *            Old tag
	 * @param newTag
	 *            Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		TagNode curr = root;
		replace(curr, oldTag, newTag);

	}
	private void replace(TagNode curr, String oldTag, String newTag){
		if(curr.tag.equals(oldTag)){
			curr.tag = newTag;
		}
		if(curr.firstChild == null && curr.sibling == null){
			return;
		}
		if(curr.sibling != null){
			replace(curr.sibling, oldTag, newTag);
		}
		if(curr.firstChild != null){
			replace(curr.firstChild, oldTag, newTag);
		}

	}

	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The
	 * boldface (b) tag appears directly under the td tag of every column of
	 * this row.
	 * 
	 * @param row
	 *            Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		ArrayList<TagNode> list = new ArrayList<TagNode>();
		boldHelp(root, list, "tr");
		if(list.isEmpty() || list.size() < row)
			return;
		TagNode ptr = list.get(0);
		for(int i = 1; i < row; i++){
			ptr = ptr.sibling;
		}
		ptr = ptr.firstChild;
		TagNode temp;
		while(ptr != null){
			temp = new TagNode("b", ptr.firstChild, null);
			temp.firstChild = ptr.firstChild;
			ptr.firstChild = temp;
			ptr = ptr.sibling;
		}
	}


	private void boldHelp(TagNode root, ArrayList<TagNode> list, String info) {
		if (root == null) {
			return;
		}
		boldHelp(root.firstChild, list, info);
		if(root.tag.equals(info))
			list.add(root);
		boldHelp(root.sibling, list, info);
	}


	public void removeTag(String tag) {
		if(tag.equals("ol")|| tag.equals("ul")){
			Stack<TagNode> stack= new Stack<TagNode>();
			stack=tagSearch(stack,root,tag);
			while(!stack.isEmpty()){
				TagNode temp=stack.pop();
				temp=temp.firstChild;
				while(temp!=null){
					if(temp.tag.equals("ol") || temp.tag.equals("ul"));
					temp.tag="p";
					temp=temp.sibling;
				}
			}
		}
		Stack<TagNode> stack=new Stack<TagNode>();
		stack=tagSearch(stack,root,tag);
		while(!stack.isEmpty()){
			adder(stack.pop());

		}
	}


	private Stack<TagNode> tagSearch(Stack<TagNode>stack, TagNode root, String tag){
		if(root==null)
			return null;
		if(root.tag.equals(tag)){
			stack.push(root);
		}

		tagSearch(stack,root.sibling,tag);
		tagSearch(stack,root.firstChild,tag);
		return stack;
	}


	private void adder(TagNode temp){
		TagNode curr=temp.sibling;
		TagNode begin=temp.firstChild;
		TagNode last=begin;
		while(last.sibling!=null){
			last=last.sibling;
		}
		last.sibling=curr;
		temp.sibling=begin.sibling;
		temp.tag=begin.tag;
		temp.firstChild=begin.firstChild;
	}

	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 *
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		addTag(root, word.toLowerCase(), tag);
	}

	private void addTag(TagNode root, String word, String tag) {
		if(root == null)
			return;
		addTag(root.firstChild, word, tag);
		addTag(root.sibling, word, tag);
		if(root.firstChild == null) {
			while(root.tag.toLowerCase().contains(word)){
				String[] arr = root.tag.split(" ");
				Boolean bool = false;
				String temp = "";
				StringBuilder strings = new StringBuilder(root.tag.length());
				int counter = 0;
				for(counter=0; counter<arr.length; counter++) {
					if(arr[counter].toLowerCase().matches(word+"[.?!;:]?")) {
						temp = arr[counter];
						bool = true;
						for(int i=counter+1; i<arr.length; i++) strings.append(arr[i]+" ");
						break;
					}
				}
				if(!bool)
					return;
				String endOfString = strings.toString().trim();
				if(counter == 0) {
					root.firstChild = new TagNode(temp, null, null);
					root.tag = tag;
					if(!endOfString.equals("")) { 
						root.sibling = new TagNode(endOfString, null, root.sibling);
						root = root.sibling;
					}
				}
				else{
					TagNode temporary = new TagNode(temp, null, null);
					TagNode toTag = new TagNode(tag, temporary, root.sibling);
					root.sibling = toTag;
					root.tag = root.tag.replaceFirst(" "+temp, "");
					if(!endOfString.equals("")) {
						root.tag = root.tag.replace(endOfString, "");
						toTag.sibling = new TagNode(endOfString, null, toTag.sibling);
						root = toTag.sibling;
					}
				}
			} 
		}
	}

	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 *
	 * @return HTML string, including new lines.
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}

	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode curr = root; curr != null; curr = curr.sibling) {
			if (curr.firstChild == null) {
				sb.append(curr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(curr.tag);
				sb.append(">\n");
				getHTML(curr.firstChild, sb);
				sb.append("</");
				sb.append(curr.tag);
				sb.append(">\n");
			}
		}
	}

}
