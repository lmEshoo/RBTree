/**
 * CS 241: Data Structures and Algorithms II
 * Professor: Edwin Rodriguez
 *
 * Programming Assignment #4
 *
 * <RED BLACK TREE>
 *
 * Lini Mestar
 */
package edu.cpp.cs.cs241.prog_assgmnt_N;

import java.util.ArrayList;
enum Color{RED,BLACK,NIL};

public class RBT<K extends Comparable<K>, V> implements Tree<K,V> {
	private Node root=null;

	@Override
	public void add(K key, V value) {
		if(root==null){ //root insert
			root=new Node(key,value,Color.BLACK);
			root.leftChild=new Node(Color.BLACK);
			root.leftChild.parent=root;
			root.rightChild=new Node(Color.BLACK);
			root.rightChild.parent=root;
		}
		else{
			//find where to insert node
			Node nodeMe = insertHere(key); 
			//already in the tree update value
			if(!nodeMe.isLeaf() && nodeMe.key.equals(key)){
				nodeMe.value=value;
				return;
			}
			else{
				nodeMe.setInsertedNode(new Node(key,value,Color.RED));
				nodeMe.leftChild.parent = nodeMe;
				nodeMe.rightChild.parent=nodeMe;
				insertCase1(nodeMe);
			}//else
		}//else
	}//add(K key, V value)
	
	private void insertCase1(Node nodeMe){
		 if (nodeMe.parent == null) nodeMe.color = Color.BLACK;
		 else insertCase2(nodeMe);
	}//insertCase1(Node nodeMe)
	
	private void insertCase2(Node nodeMe){
		if(nodeMe.parent().color != Color.BLACK) insertCase3(nodeMe);
		else return;
	}//insertCase2(Node nodeMe)
	
	private void insertCase3(Node nodeMe){
		if(nodeMe.uncle().color==Color.RED){
			nodeMe.parent().color=Color.BLACK;
			nodeMe.uncle().color=Color.BLACK;
			nodeMe.grandParent().color=Color.RED;
			insertCase1(nodeMe.grandParent());
		}
		else insertCase4(nodeMe);
	}//insertCase3(Node nodeMe)
	
	private void insertCase4(Node nodeMe){
		if(nodeMe.parent().color ==Color.RED &&
				nodeMe.uncle().color==Color.BLACK && 
				IRN(nodeMe)){
			rotateRight(nodeMe.parent());
			insertCase5(nodeMe.rightChild);
		}
		else if(nodeMe.parent().color ==Color.RED &&
				nodeMe.uncle().color==Color.BLACK && 
				ILN(nodeMe)){
			rotateLeft(nodeMe.parent());
			insertCase5(nodeMe.leftChild);
		}
		else insertCase5(nodeMe);
	}//insertCase4(Node nodeMe)
	
	private void insertCase5(Node nodeMe){
		nodeMe.parent().color=Color.BLACK;
		nodeMe.grandParent().color=Color.RED;
		if(ERN(nodeMe) && 
				nodeMe.uncle().color==Color.BLACK) rotateLeft(nodeMe.grandParent());
		if(ELN(nodeMe) &&
				nodeMe.uncle().color==Color.BLACK) rotateRight(nodeMe.grandParent());
	}//insertCase5(Node nodeMe)
	
	@Override
	public V remove(K key) {
		Node nodeMe=root;
		boolean notFound=true;
		V tempVal=null;
		//find it!
		while(!nodeMe.isLeaf() && notFound){
			if(key.compareTo(nodeMe.key)==-1){
				if(nodeMe.leftChild!=null) nodeMe=nodeMe.leftChild;
			}
			else if(key.compareTo(nodeMe.key)==1){
				if(nodeMe.rightChild!=null) nodeMe=nodeMe.rightChild;
			}
			if(nodeMe.isLeaf()) break;
			else if(nodeMe.key.equals(key)){
				notFound=false;
				tempVal=nodeMe.value;
				removeCase1(nodeMe);
			}
		}//while
		return tempVal;
	}//remove(K key)
	
	private void removeCase1(Node nodeMe){
		if (!nodeMe.leftChild.isLeaf() && !nodeMe.rightChild.isLeaf()){
		        //get the key and value inOrderSuccessor and then delete it instead
		        Node successor = inOrderSuccessor(nodeMe);
		        nodeMe.key=successor.key;
		        nodeMe.value=successor.value;
		        nodeMe=successor;
		}
		//get the non leafy child
		Node child=null;		
		if(nodeMe.leftChild.isLeaf() || nodeMe.rightChild.isLeaf()){
		  		if(nodeMe.rightChild.isLeaf()) child=nodeMe.leftChild;
		  		else child=nodeMe.rightChild;
		  		if(nodeMe.color==Color.BLACK){
		  			nodeMe.color=child.color;
		  			if (nodeMe.parent != null) removeCase2(nodeMe);
		  		    else;
		  		}
		}//if
		replaceParent(nodeMe, child);
		root.color=Color.BLACK;
		if(root.isLeaf()) root=null;
	}//removeCase1(Node nodeMe)
	
	private void removeCase2(Node nodeMe){
		if(nodeMe.sibling().color==Color.RED){
			 nodeMe.parent.color=Color.RED;
			 nodeMe.sibling().color=Color.BLACK;
			if (nodeMe == nodeMe.parent.leftChild) rotateLeft(nodeMe.parent());
			else rotateRight(nodeMe.parent());
		}//if
		removeCase3(nodeMe);
	}//removeCase2(Node nodeMe)
	
	private void removeCase3(Node nodeMe){
		//recurse on the parent node
		if( nodeMe.parent().color==Color.BLACK &&
				nodeMe.sibling().color==Color.BLACK &&
				( nodeMe.sibling().leftChild.color==Color.BLACK && 
					nodeMe.sibling().rightChild.color==Color.BLACK )){
			nodeMe.sibling().color=Color.RED;
			if (nodeMe.parent.parent != null) removeCase2(nodeMe.parent);
		    else;
		}
		else if(nodeMe.parent().color==Color.RED &&
				nodeMe.sibling().color==Color.BLACK &&
				nodeMe.sibling().leftChild.color==Color.BLACK&&
				nodeMe.sibling().rightChild.color==Color.BLACK){
			nodeMe.sibling().color=Color.RED;
			nodeMe.parent().color=Color.BLACK;
		}
		else removeCase4(nodeMe);
	}//removeCase3(Node nodeMe)
	
	private void removeCase4(Node nodeMe){
		if(nodeMe.parent.leftChild==nodeMe &&
				nodeMe.sibling().color==Color.BLACK &&
				nodeMe.sibling().leftChild.color==Color.RED &&
				nodeMe.sibling().rightChild.color==Color.BLACK){
			nodeMe.sibling().color=Color.RED;
			nodeMe.leftChild.color=Color.BLACK;
			rotateLeft(nodeMe.sibling());
			removeCase5(nodeMe);
		}			
		if(nodeMe.parent.rightChild==nodeMe
				&&nodeMe.sibling().color==Color.BLACK
				&&nodeMe.sibling().rightChild.color==Color.RED
				&&nodeMe.sibling().leftChild.color==Color.BLACK){
			nodeMe.sibling().color=Color.RED;
			nodeMe.rightChild.color=Color.BLACK;
			rotateRight(nodeMe.sibling());
			removeCase5(nodeMe);
		}
		else removeCase5(nodeMe);
	}//removeCase4(Node nodeMe)
	
	private void removeCase5(Node nodeMe){
		if( nodeMe.parent.leftChild==nodeMe &&
				nodeMe.sibling().color==Color.BLACK &&
				ERN(nodeMe.sibling().rightChild) ){
			if(nodeMe.sibling().rightChild.color==Color.RED){
				nodeMe.sibling().color=nodeMe.parent().color;
				nodeMe.parent().color=Color.BLACK;
				nodeMe.sibling().rightChild.color=Color.BLACK;
				rotateLeft(nodeMe.parent());
			}
		}
		else if(nodeMe.parent.rightChild==nodeMe&&
				nodeMe.sibling().color==Color.BLACK &&
				ELN(nodeMe.sibling().leftChild)){
			if(nodeMe.leftChild.color==Color.RED){
				nodeMe.sibling().color=nodeMe.parent().color;
				nodeMe.parent().color=Color.BLACK;
				nodeMe.sibling().rightChild.color=Color.BLACK;
				nodeMe.sibling().color=Color.BLACK;
				rotateRight(nodeMe.parent);
			}//if
		}//else if
	}//removeCase5(Node nodeMe)
	
	//returns the in order successor of the given node
	private Node inOrderSuccessor(Node nodeMe){
		nodeMe=nodeMe.rightChild;
		if(nodeMe!= null) 
			while (!nodeMe.leftChild.isLeaf()) nodeMe=nodeMe.leftChild;
		return nodeMe;
	}//inOrderSuccessor(Node nodeMe)
	
	public V lookup(K key) {
		Node nodeMe=root;
		while(!nodeMe.isLeaf()){
			if(key.compareTo(nodeMe.key)==-1){
				if(nodeMe.leftChild!=null) nodeMe=nodeMe.leftChild;
			}
			else if(key.compareTo(nodeMe.key)>0){
				if(nodeMe.rightChild!=null) nodeMe=nodeMe.rightChild;
			}
			if(nodeMe.isLeaf()) break;
			else if (key.equals(nodeMe.key)) return nodeMe.value;
		}//while
		return null;
	}//lookup(K key)
	
	//returns the leaf where the new entry should be added.
	private Node insertHere(K key){
		Node nodeMe=root;
		while(!nodeMe.isLeaf()){
			if(!nodeMe.isLeaf()&&nodeMe.key.equals(key)) return nodeMe;
			else if(key.compareTo(nodeMe.key)>=0){
				if(nodeMe.rightChild!=null) nodeMe=nodeMe.rightChild;
			}
			else if(key.compareTo(nodeMe.key)==-1){
				if(nodeMe.leftChild!=null) nodeMe=nodeMe.leftChild;
			}
		}//while
		return nodeMe;
	}//insertHere(K key)
	
	//rotates right 
	private void rotateRight(Node nodeMe){
		Node left=nodeMe.leftChild;
		replaceParent(nodeMe,left);
		nodeMe.leftChild=left.rightChild;
		if(left.rightChild!=null) left.rightChild.parent=nodeMe;
		left.rightChild=nodeMe;
		nodeMe.parent=left;
	}//rotateRight(Node nodeMe)
	
	//rotates left 
	private void rotateLeft(Node nodeMe){
		Node right=nodeMe.rightChild;
		replaceParent(nodeMe,right);
		nodeMe.rightChild=right.leftChild;
		if(right.leftChild!=null) right.leftChild.parent=nodeMe;
		right.leftChild=nodeMe;
		nodeMe.parent=right;
	}//rotateLeft(Node nodeMe)
	
	//make newParent the new parent of childMe.
	private void replaceParent(Node childMe, Node newParent){
		if (childMe.parent == null) root = newParent;
	    else {
	        if (childMe != childMe.parent.leftChild) childMe.parent.rightChild = newParent;
	        else childMe.parent.leftChild = newParent;
	    }
	    if (newParent != null) newParent.parent = childMe.parent;
	}//replaceParent(Node childMe, Node newParent)
	
	//external right node
	private boolean ERN(Node nodeMe){
		return (nodeMe.parent().rightChild==nodeMe && 
				nodeMe.grandParent().rightChild==nodeMe.parent()) ? true : false;
	}//externalRightNode(Node nodeMe)
	
	//external Left Node
	private boolean ELN(Node nodeMe){
		return (nodeMe.parent().leftChild==nodeMe && 
				nodeMe.grandParent().leftChild== nodeMe.parent()) ? true : false;
	}//externalLeftNode(Node nodeMe)
	
	//is it an internal left node?
	private boolean ILN(Node nodeMe) {
		return (nodeMe.parent().rightChild==nodeMe &&
				nodeMe.grandParent().leftChild==nodeMe.parent()) ? true : false;
	}//internalLeftNode(Node nodeMe)
	
	//or...is it an internal right node?
	private boolean IRN(Node nodeMe){
		return (nodeMe.parent().leftChild==nodeMe && 
				nodeMe.grandParent().rightChild==nodeMe.parent()) ? true : false;	
	}//internalRightNode(Node NodeMe)
	
	private int size(Node nodeMe){ 
		  return (nodeMe == null) ? 0 : (size(nodeMe.leftChild) + 1 + size(nodeMe.rightChild)); 
	}//size(Node node)
	
	@Override
	public String toPrettyString() {

		ArrayList<String> list = new ArrayList<>();
		String pos,finalT = "";
		int tempSize = size(root);
		int index= 1,depth= 1,elemNum= 1,level = 1;

		while(index <= tempSize){
			
			pos = Integer.toBinaryString(index);
			Node nodeMe = root;
			
			for(int i = 1; i < pos.length(); i++){
				if(nodeMe != null)
					nodeMe = (pos.charAt(i) == '0') ? nodeMe.leftChild : nodeMe.rightChild;
			}
			
			if(nodeMe != null){
				String temp="";
				temp= (nodeMe.key != null)?
						nodeMe.value + "(" + nodeMe.color + ")" + " " : 
							"NIL" + "(" + nodeMe.color + ")";
				list.add(temp);
			}else{
				list.add("     ");
				tempSize++;
			}
			index++;
		}
		
		while(elemNum < tempSize){
			depth++;
			elemNum += (int)Math.pow(2, depth - 1);
		}

		while(level <= depth){
			int levelElem = (int)Math.pow(2, level - 1);
			if(level == 1){
				for(int i = 0; i < (int)Math.pow(2, depth - 1) / 2; i++)
					finalT += "       ";
			}else{
				for(int i = 0; i < ((int)Math.pow(2, depth - 1) - levelElem) / 2; i++)
					finalT += "       ";
			}
			for(int i = 0; i < levelElem; i++){
				if(levelElem - 1 + i < tempSize)
					finalT += list.get(levelElem - 1 + i) +" ";
			}
			finalT += "\n";
			level++;
		}
		return finalT;
	}//toPrettyString()
	
	private class Node{
		protected Color color;
		protected V value;
		protected K key;
		protected Node parent, leftChild, rightChild;
		
		public Node(K key, V value,Color colorMe){
			this.color=colorMe;
			this.value=value;
			this.key=key;
			this.leftChild=new Node(Color.BLACK);
			this.rightChild=new Node(Color.BLACK);
		}//Node(K key, V value,Color colorMe)
		
		//used for new nodes or NIL nodes for use in the toPrettyString()
		public Node(Color colorMe){
			this.color=colorMe;
			this.value=null;
			this.key=null;
		}//Node(Color nodeColor)
		
		public void setInsertedNode(Node insertMe){
			this.color=insertMe.color;
			this.value=insertMe.value;
			this.key=insertMe.key;
			this.leftChild=insertMe.leftChild;
			this.rightChild=insertMe.rightChild;
		}//setInsertedNode(Node insertMe)
		
		public Node grandParent(){
			return (this.parent!=null && this.parent.parent!=null) ? this.parent.parent : null;
		}//grandParent()
		
		public Node uncle(){
			return (this.parent().sibling()!=null) ? this.parent().sibling() : null;
		}//uncle()
		
		public Node parent(){
			return (this.parent!=null) ? this.parent : null;
		}//parent()
		
		public Node sibling(){
			  if (parent != null) return (this == parent.leftChild) ? parent.rightChild : parent.leftChild;
			  else return null;
		}//sibling()
		
		public boolean isLeaf(){
			return (this.color==Color.BLACK && this.value==null && this.key==null) ? true : false;
		}//isLeaf()
		
		public String toString(){
			if(this.key==null) return "NIL B   ";
			return (this.color==Color.BLACK) ? value+" B   " : value+" R   ";
		}//toString()
	}//Node
}
