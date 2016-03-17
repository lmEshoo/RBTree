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

public class driveMe {
		public static void main(String[] args){
			RBT<Integer, Integer> redBlack=new RBT<Integer, Integer>();
			for(int i=0;i<50;i++) redBlack.add(i,i);
			for(int i=0;i<45;i++){
				if(i==44)
					System.out.println(redBlack.toPrettyString());
				redBlack.remove(i);
			}
			
			System.out.println(redBlack.lookup(47));
		}
}
