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

public interface Tree<K extends Comparable<K>,V> {

	  public void add(K key, V value);
	  public V remove(K key);
	  public V lookup(K key);
	  public String toPrettyString();

}
