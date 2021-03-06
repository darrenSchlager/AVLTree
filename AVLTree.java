/*
	Author: Darren Schlager
*/

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;

class Node 
{
	//instance variables
	int data;
	Node parent;
	Node left;
	Node right;
	int height;
	
	//constructor
	Node(int data) 
	{
		this.data = data;
		height = 1;
	}
	
	//if the node has any children, update the height to be the height of the tallest child + 1
	public void updateHeight() 
	{
		if(left!=null || right!=null) 
		{
			if(left==null) height=right.height+1; 
			else if(right==null) height=left.height+1; 
			else if(left.height>=right.height) height = left.height+1;
			else height = right.height+1;
		}
		else 
		{
			height = 1;
		}
	}
	
	//balance = height of left child - height of right child
	public int getBalance() 
	{
		if(right==null && left==null) return 0;
		else if(right==null) return left.height;
		else if(left==null) return right.height*-1;
		else return left.height-right.height;
	}
}


public class AVLTree 
{	
	//instance variables
	Node root;
	private int lengthOfLongestData = 0;	//used by print()
	
	//constructor
	AVLTree(int[] dataArray) 
	{
		if(dataArray!=null && dataArray.length>0) 
		{
			//insert a node for each of the remaining data items
			for(int i=0; i<dataArray.length; i++) 
			{
				insert(dataArray[i]);
			}
		}
	}
	
	//default constructor
	AVLTree() {}
	
	public boolean isEmpty()
	{
		return root==null;
	}
	
	//recursively insert a node
	public void insert(int data) 
	{
		//update 'lengthOfLongestData'
		int lengthOfData = (data+"").length();
		if(lengthOfData>lengthOfLongestData) lengthOfLongestData = lengthOfData;
		
		//create the root node
		if(root==null) 
		{
			System.out.println("[insert "+data+"]\n");
			root = new Node(data);
			print();
		}
		//recursively insert a node below the root
		else 
		{
			System.out.println("[insert "+data+"]\n");
			insert(root, data);
		}
	}
	
	//recursive helper function
	private void insert(Node currentNode, int data) 
	{
		//the new node belongs on the left
		if(data<currentNode.data) 
		{
			//no left child, insert the node
			if(currentNode.left==null) 
			{
				currentNode.left = new Node(data);
				currentNode.left.parent = currentNode;
				print();
			}
			//recursive call with the left child
			else 
			{
				insert(currentNode.left, data);
			}
		}
		//the new node belongs on the right
		else 
		{
			//no right child, insert the node
			if(currentNode.right==null) 
			{
				currentNode.right = new Node(data);
				currentNode.right.parent = currentNode;
				print();
			}
			//recursive call with the right child
			else 
			{
				insert(currentNode.right, data);
			}
		}
		
		//a node has been inserted below the current node so update the height
		currentNode.updateHeight();
		rebalanceIfNecessary(currentNode);
	}
	
	//recursively remove a node
	public void remove(int data)
	{
		System.out.println("[remove "+data+"]\n");
		if(root!=null) remove(root, data);
	}
	
	//recursive helper function
	private void remove(Node currentNode, int data)
	{
		//data item found
		if(currentNode.data == data)
		{
			//currentNode has two children
			if(currentNode.left!=null && currentNode.right!=null) 
			{
				//replace the data item contained within currentNode with the smallest data item contained within its right subtree and remove that node instead
				
				//enter the right subtree
				Node nodeToSwapWith = currentNode.right;
				
				if(nodeToSwapWith.left!=null) 
				{
					//find the smallest, copy its data item into nodeToRemove, remove the found node
					removeLeftmost(currentNode, nodeToSwapWith);
				}
				//the right child is the smallest
				else 
				{
					//copy its data item into currentNode
					currentNode.data = nodeToSwapWith.data;
					
					//it has no children, remove it
					if(nodeToSwapWith.right==null) 
					{
						currentNode.right = null;
					}
					//make its right child the new right child of currentNode
					else 
					{
						currentNode.right = nodeToSwapWith.right;
						currentNode.right.parent = currentNode;
					}
					print();
				}
				
				//a node has been removed below the current node so update the height
				currentNode.updateHeight();
				rebalanceIfNecessary(currentNode);
			}
			//currentNode has only a left child
			else if(currentNode.left!=null)
			{
				if(currentNode==root) 
				{
					//make the left child the new root
					root = currentNode.left;
					root.parent = null; 
					
					//a node has been removed below the current node so update the height
					root.updateHeight();
					rebalanceIfNecessary(root);
				}
				
				else if(currentNode.parent.left==currentNode) 
				{
					//make the left child the parents new left child
					currentNode.parent.left = currentNode.left;
					currentNode.parent.left.parent = currentNode.parent;
				}
				else 
				{
					//make the left child the parents new right child
					currentNode.parent.right = currentNode.left;
					currentNode.parent.right.parent = currentNode.parent;
				}
				print();
			}
			//currentNode has only a right child
			else if(currentNode.right!=null)
			{
				if(currentNode==root) 
				{
					//make the right child the new root
					root = currentNode.right;
					root.parent = null;
					
					//a node has been removed below the current node so update the height
					root.updateHeight();
					rebalanceIfNecessary(root);
				}
				else if(currentNode.parent.left==currentNode) 
				{
					//make the right child the parents new left child
					currentNode.parent.left = currentNode.right;
					currentNode.parent.left.parent = currentNode.parent;
				}
				else 
				{
					//make the right child the parents new right child
					currentNode.parent.right = currentNode.right;
					currentNode.parent.right.parent = currentNode.parent;
				}
				print();
			}
			//currentNode has no children
			else
			{
				//currentNode is the only node
				if(currentNode==root) root = null;
				//delete the parents left child
				else if(currentNode.parent.left==currentNode) currentNode.parent.left = null;
				//delete the parents right child
				else currentNode.parent.right = null;
				
				print();
			}
		}
		//recursive call with the left child
		else if(data<currentNode.data && currentNode.left!=null)
		{
			remove(currentNode.left, data);
			
			//a node has been removed below the current node so update the height
			currentNode.updateHeight();
			rebalanceIfNecessary(currentNode);
		}
		//recursive call with the right child
		else if(data>currentNode.data && currentNode.right!=null)
		{
			remove(currentNode.right, data);
			
			//a node has been removed below the current node so update the height
			currentNode.updateHeight();
			rebalanceIfNecessary(currentNode);
		}
	}
	
	//recursively find the smallest node, copy its data item into nodeToRemove, remove the found node
	private void removeLeftmost(Node currentNode, Node nodeToSwapWith)
	{
		//found the smallest node
		if(nodeToSwapWith.left.left==null) 
		{
			//copy its data item into currentNode, remove the found node
			currentNode.data = nodeToSwapWith.left.data;
			
			//it has no children, remove it
			if(nodeToSwapWith.left.right==null) 
			{
				nodeToSwapWith.left = null;
			}
			//make its right child the new right child of nodeToSwapWith
			else 
			{
				nodeToSwapWith.left = nodeToSwapWith.left.right;
				nodeToSwapWith.left.parent = nodeToSwapWith;
			}
			print();
		}
		//recursive call with the left child
		else
		{
			removeLeftmost(currentNode, nodeToSwapWith.left);
		}
		
		//a node has been removed below the current node so update the height
		nodeToSwapWith.updateHeight();		
		rebalanceIfNecessary(nodeToSwapWith);
	}
	
	//perform a rotation if the tree rooted at currentNode is unbalanced
	private void rebalanceIfNecessary(Node currentNode)
	{
		int balance = currentNode.getBalance();
		
		//right heavy
		if(balance<-1)
		{
			//right subtree is left heavy
			if(currentNode.right.getBalance()>0)
			{
				System.out.println(".right left rotation.\n");
				rotateRightLeft(currentNode);
				print();
			}
			else
			{
				System.out.println(".left rotation.\n");
				rotateLeft(currentNode);
				print();
			}
		}
		//left heavy
		else if(balance>1)
		{
			//left subtree is right heavy
			if(currentNode.left.getBalance()<0)
			{
				System.out.println(".left right rotation.\n");
				rotateLeftRight(currentNode);
				print();
			}
		 	else
			{
				System.out.println(".right rotation.\n");
				rotateRight(currentNode);
				print();
			}
		}
	}
	
	private void rotateLeft(Node currentNode) 
	{
		if(currentNode!=null && currentNode.right!=null) 
		{
			if(currentNode == root) 
			{
				//the right child becomes the new root
				Node oldRoot = root;
				root = root.right;
				root.parent = oldRoot.parent;
				
				//the old root makes the new root's left child its right child
				oldRoot.right = root.left;
				if(root.left!=null) root.left.parent = oldRoot;
				
				//the new root makes the old root its left child
				root.left = oldRoot;
				oldRoot.parent = root;
				
				oldRoot.updateHeight();
				root.updateHeight();
			}
			else 
			{
				Node parent  = currentNode.parent;
				
				//currentNode is the parents left node
				if(parent.left == currentNode) 
				{
					
					//the right child becomes the parents new left child
					parent.left = currentNode.right;
					if(currentNode.right!=null) currentNode.right.parent = parent;
					
					//the old left child makes the new left child's left child its right child
					currentNode.right = parent.left.left;
					if(parent.left!=null && parent.left.left!=null) parent.left.left.parent = currentNode;
					
					//the new left child makes the old left child its left child
					parent.left.left = currentNode;
					currentNode.parent = parent.left;
					
					parent.left.left.updateHeight();
					parent.left.updateHeight();
					
				}
				//currentNode is the parents right node
				else 
				{
					
					//the right child becomes the parents new right child
					parent.right = currentNode.right;
					if(currentNode.right!=null) currentNode.right.parent = parent;
					
					//the old right child makes the new right child's left child its right child
					currentNode.right = parent.right.left;
					if(parent.right!=null && parent.right.left!=null)parent.right.left.parent = currentNode;
					
					//the new right child makes the old right child its left child
					parent.right.left = currentNode;
					currentNode.parent = parent.right;
					
					parent.right.left.updateHeight();
					parent.right.updateHeight();
				}
				
			}
		} 
	}
	
	private void rotateRight(Node currentNode) 
	{
		if(currentNode!=null && currentNode.left!=null) 
		{
			if(currentNode == root) 
			{
				//the left child becomes the new root
				Node oldRoot = root;
				root = root.left;
				root.parent = oldRoot.parent;
				
				//the old root makes the new root's right child its left child
				oldRoot.left = root.right;
				if(root.right!=null) root.right.parent = oldRoot;
				
				//the new root makes the old root its right child
				root.right = oldRoot;
				oldRoot.parent = root;
				
				oldRoot.updateHeight();
				root.updateHeight();
			}
			else if(currentNode.parent!=null )
			{
				Node parent  = currentNode.parent;
				
				//currentNode is the parents left node
				if(parent.left == currentNode) {
					
					//the left child becomes the parents new left child
					parent.left = currentNode.left;
					if(currentNode.left!=null) currentNode.left.parent = parent;
					
					//the old left child makes the new left child's right child its left child
					currentNode.left = parent.left.right;
					if(parent.left!=null && parent.left.right!=null) parent.left.right.parent = currentNode;
					
					//the new left child makes the old left child its right child
					parent.left.right = currentNode;
					currentNode.parent = parent.left;
					
					parent.left.right.updateHeight();
					parent.left.updateHeight();
				}
				//currentNode is the parents right node
				else if(parent.right == currentNode) 
				{
					
					//the left child becomes the parents new right child
					parent.right = currentNode.left;
					if(currentNode.left!=null) currentNode.left.parent = parent;
					
					//the old right child makes the new right child's right child its left child
					currentNode.left = parent.right.right;
					if(parent.right!=null && parent.right.right!=null)parent.right.right.parent = currentNode;
					
					//the new right child makes the old right child its right child
					parent.right.right = currentNode;
					currentNode.parent = parent.right;
					
					parent.right.right.updateHeight();
					parent.right.updateHeight();
				}
				
			}
		}
	}
	
	//rotate the right subtree right before rotating left
	private void rotateRightLeft(Node currentNode)
	{
		rotateRight(currentNode.right);
		print();
		rotateLeft(currentNode);
	}
	
	//rotate the left subtree left before rotating right
	private void rotateLeftRight(Node currentNode)
	{
		rotateLeft(currentNode.left);
		print();
		rotateRight(currentNode);
	}
	
	//recursively find the nodes within the specified range
	public ArrayList<Integer> range(int key1, int key2)
	{
		ArrayList<Integer> values = new ArrayList<Integer>();
		getInRange(key1, key2, root, values);
		return values;
	}
	
	//recursive helper function
	private void getInRange(int key1, int key2, Node currentNode, ArrayList<Integer> values)
	{
		if(currentNode!=null)
		{
			if(key1<=currentNode.data && currentNode.data<=key2)
			{
				if(currentNode.left!=null) getInRange(key1, key2, currentNode.left, values);
				if(currentNode.right!=null) getInRange(key1, key2, currentNode.right, values);
				values.add(currentNode.data);
			}
			else if(currentNode.data<key1)
			{
				if(currentNode.left!=null) getInRange(key1, key2, currentNode.right, values);
			}
			else if(key2<currentNode.data)
			{
				if(currentNode.left!=null) getInRange(key1, key2, currentNode.left, values);
			}
			else 
			{
				values.add(currentNode.data);
			}	
		}
	}
	
	public void print() 
	{
		if(root!=null) 
		{
			//2d array, each row represents a level
			ArrayList< ArrayList<Integer> > levels = new ArrayList< ArrayList<Integer> >();
			
			//prepare the first level
			levels.add(new ArrayList<Integer>());
			
			//call recursive helper function
			printTree(root, levels, 0);
		}
	}
	
	private void printTree(Node currentNode, ArrayList< ArrayList<Integer> > levels, int levelIndex) 
	{
		//add the current node to the level
		levels.get(levelIndex).add(currentNode.data);
		
		//prepare for the next level
		if(levelIndex+1 == levels.size()) levels.add(new ArrayList<Integer>());
		
		//recursive call with the left child
		if(currentNode.left!=null) printTree(currentNode.left, levels, levelIndex+1);
		//no left child
		else levels.get(levelIndex+1).add(null);
		
		//recursive call with the right child
		if(currentNode.right!=null) printTree(currentNode.right, levels, levelIndex+1);
		//no right child
		else levels.get(levelIndex+1).add(null);
		
		//recursion has unwound back to the first call, print the result
		if(levelIndex==0) 
		{
			
			//holds true for a number and false for a null
			ArrayList<Boolean> lastLevel = new ArrayList<Boolean>();
			ArrayList<Boolean> currentLevel = new ArrayList<Boolean>();
			
			//first level
			int paddingAtStart = ( (int)Math.pow(2, (levels.size()-2))-1 ) * lengthOfLongestData;
			for(int l=0; l<paddingAtStart+2; l++) System.out.print(" ");
			System.out.print(levels.get(0).get(0)+"");
			lastLevel.add(true);
			System.out.println("\n");
			
			//second level through level n-1 since level n-1 is empty
			for(int i=1; i<levels.size()-1; i++) 
			{
				paddingAtStart = ( (int)Math.pow(2, (levels.size()-2-i))-1 ) * lengthOfLongestData;
				int paddingBetween = ( (int)Math.pow(2, (levels.size()-1-i))-1) * lengthOfLongestData;
				
				int column = 0; //will be incremented only when a number(true) is encountered in 'lastLevel'
				for(int j=0; j<lastLevel.size(); j++) 
				{
					if(j==0) for(int l=0; l<paddingAtStart+2; l++) System.out.print(" ");
					
					//number
					if(lastLevel.get(j)) 
					{

						//process the two children
						for(int k=0; k<2; k++) 
						{
							//null
							if(levels.get(i).get(column)==null) 
							{
								if(k==0) System.out.printf("%"+(lengthOfLongestData)+"c",'-');
								else System.out.printf("%-"+(lengthOfLongestData)+"c",'-');
								for(int l=0; l<paddingBetween; l++) System.out.print(" ");
								currentLevel.add(false);
							}
							//number
							else 
							{
								if(k==0) System.out.printf("%"+(lengthOfLongestData)+"s", levels.get(i).get(column));
								else System.out.printf("%-"+(lengthOfLongestData)+"s", levels.get(i).get(column));
								for(int l=0; l<paddingBetween; l++) System.out.print(" ");
								currentLevel.add(true);
							}
							column++;
						}
					}
					//null
					else 
					{
						System.out.printf("%"+(lengthOfLongestData)+"c",'-');
						for(int l=0; l<paddingBetween; l++) System.out.print(" ");
						System.out.printf("%-"+(lengthOfLongestData)+"c",'-');
						for(int l=0; l<paddingBetween; l++) System.out.print(" ");
						currentLevel.add(false);
						currentLevel.add(false);
					}
				}
				
				System.out.println("\n");
				lastLevel = currentLevel;
				currentLevel = new ArrayList<Boolean>();
			}
		}
		
	}
	
	public static void main(String[] args) 
	{
		AVLTree tree = new AVLTree();
		run(tree);
	}
	
	public static void run(AVLTree tree)
	{
		// used to retrieve keyboard input from the user
		Scanner keyboard = new Scanner(System.in);
		
		// print description of expeteted file contents
		System.out.println("AVLTree\n");
		System.out.println("Format your file as follows:");
		System.out.println("===============================================");
		System.out.println(" <integer 1> <integer 2> ... <integer n>");
		System.out.println("===============================================");
		
		Scanner file;
		boolean dataInputSuccessfully = false;
		System.out.println("Step 1: create tree and insert nodes");
		System.out.print("press RETURN to enter data manually\nfile path: ");
		String path = "dummy string";
		
		//insert nodes
		do {
			
			// get the file path from the user
			if(!path.equals("")) path = keyboard.nextLine();
			
			// enter data manually
			if(path.equals(""))
			{
				
				//get the input from the user
				System.out.print("insert node(s): ");
				String input = keyboard.nextLine();
				
				while(!input.equals(""))
				{
					try 
					{
						System.out.println();
						
						//preapare the received input for processing
						Scanner stringScanner = new Scanner(input);
						
						//insert each item
						while(stringScanner.hasNext())
						{
							tree.insert(stringScanner.nextInt());
						}
					}
					catch (Exception e)
					{
						System.out.println("That data is not formatted correctly.");
					}
					
					//get the next input from the user
					System.out.print("insert: ");
					input = keyboard.nextLine();
				}
				dataInputSuccessfully = true;
			}
			//enter data from file
			else
			{
				try 
				{
					// open the file
					file = new Scanner(new FileReader(path));
					
					try 
					{
						System.out.println();
						
						//insert each item
						while(file.hasNext())
						{
							tree.insert(file.nextInt());
							if(!file.hasNext()) dataInputSuccessfully = true;
						}
					}
					catch (Exception e)
					{
						System.out.println("That file is not formatted correctly.\nfile path: ");
					}
					
					//close the file
					file.close();
				} 
				catch (IOException e) // file
				{
					// invalid file
					System.out.print("That file does not exist.\nfile path: ");
				}
			}
			
		} while (!dataInputSuccessfully);
		
		System.out.println("=finished inserting data=\n");
		tree.print();
		
		//remove nodes
		System.out.println("Step 2: remove nodes");
		System.out.println("press RETURN to skip");
		boolean finishedRemoving = false;
		do
		{
			//get the nodes to remove from the user
			System.out.print("remove node(s): ");
			String input = keyboard.nextLine();
			
			while(!input.equals(""))
			{
				try
				{
					//prepare the received input for processing
					Scanner stringScanner = new Scanner(input);
					while(stringScanner.hasNext()) 
					{
						//remove each node
						int dataToRemove = stringScanner.nextInt();
						System.out.println();
						tree.remove(dataToRemove);
					}
				}
				catch (Exception e) 
				{
					System.out.println("That data is not formatted correctly.");	
				}
				
				if(!tree.isEmpty())
				{
					//get the next nodes from the user
					System.out.print("remove node(s): ");
					input = keyboard.nextLine();
				}
				else 
				{
					//exit loop
					input = "";
				}
			}
			finishedRemoving = true;
		} while(!finishedRemoving);
		
		
		System.out.println("\nStep 3: find nodes within a range");
		int key1 = -1;
		int key2 = -1;
		while(key1<0)
		{
			try 
			{
				System.out.print("Range lower bound: ");
				key1 = keyboard.nextInt();
			} 
			catch (Exception e) 
			{
				System.out.println("That data is not formatted correctly.");
				keyboard.next();
			}
		}
		while(key2<0)
		{
			try 
			{
				System.out.print("Range upper bound: ");
				key2 = keyboard.nextInt();
			} 
			catch (Exception e) 
			{
				System.out.println("That data is not formatted correctly.");	
				keyboard.next();
			}
		}
		
		ArrayList<Integer> rangeResult = tree.range(key1, key2);
		System.out.println();
		for(int i=0; i<rangeResult.size(); i++) System.out.print(rangeResult.get(i)+" ");
		System.out.println("\n\nexit\n");
	}
}