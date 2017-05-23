package edu.wit.cs.comp3370;

import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

/* Adds floating point numbers with varying precision 
 * 
 * Wentworth Institute of Technology
 * COMP 3370
 * Lab Assignment 2
 * 
 * Rachel Palmer
 */


public class LAB2 {
	
	/**
	 * Builds a heap based on given array using the pullup method (starting from index 1. Further description below).
	 * After the heap is built, a while loop will begin to add all numbers in the heap from smallest to largest
	 * in order to reduce rounding error. int k acts as a faux index count. Since arrays cannot have elements
	 * removed or added dynamically, any "removed" elements are moved to the end of the array, and k prevents
	 * the rest of the method from accessing those elements. Inside the loop, the minimum elements to be summed
	 * will be a[0] and one of a[0]'s children. The smallest child is recorded, and the sum is stored into a[0],
	 * replacing the original value. The child that was summed swaps positions with the end of the array, and k
	 * is reduced so that that element will be ignored (as if it were removed). Then, pushdown is called on 
	 * the index that once had the smallest child. If it is larger than its' children, then it is pushed down
	 * into the heap. pushdown is also called on a[0] since the summed number may also be bigger than its children.
	 * After the two pushdown's, the heap's min to max order is restored, and the loop repeats. By the time k=0,
	 * a[0] will hold the sum of all numbers in the heap.
	 * 
	 * @param a		defines the array given; will become a heap
	 * @return		a[0] which contains the sum of all numbers in the heap
	 */
	public static float heapAdd(float[] a) {
		//build heap
		for(int x = 1; x<=a.length-1; x++){ 
			pullup(a, x);
		}
		
		//add heap
		int k = a.length - 1; //k represents the max index and wont allow the program to access indices larger than it as the array "shrinks"
		while(k > 0){ 
			int minChild = 0;
			if(2 <= k){ //if k is 1, then lchild is the only one left
				if(a[1] < a[2])
					minChild = 1;
				else
					minChild = 2;
				}
			else{
				minChild = 1;
			}
			a[0] = a[0] + a[minChild];
			float temp = a[minChild];
			a[minChild] = a[k];
			a[k] = temp; //switched values to get rid of old value that was summed
			k--;
			pushdown(a, minChild, k);
			pushdown(a, 0, k);
		}
		if(a.length == 0){ //this if statement is only necessary for ChartMaker to run properly
			return 0;
		}
		else{
			return a[0]; 
		}
	}
	
	/**
	 * Takes the array/heap and a given index, and pulls the element a node above, if it is smaller than its
	 * parent. If the parent and child are switched, then pullup is called recursively on parent index, since it
	 * now contains a new number. It only calls pullup again if the index is not 0, since index 0 cannot be
	 * pulled up any further.
	 * @param a		array/heap to be sorted
	 * @param i		index of array element to check for possible pullup
	 * @return		the array with element in index i in the correct location
	 */
	public static float[] pullup(float[] a, int i){
		int parenti = (i-1)/2;
		if(a[i] < a[parenti]){ 
			float temp = a[parenti];
			a[parenti] = a[i]; 
			a[i] = temp; 
			if(parenti != 0)
				pullup(a, parenti);
		}
		return a;
	}
	
	/**
	 * Takes the array/heap at the given index, and pushes the element a node below, if it is larger than its
	 * smallest child. If the parent and child are switched, then pushdown is called on the index that once had
	 * the smallest value in it (and now contains the larger value). This way, it can be pushed down further if
	 * it continues to be larger than the children. k checks to make sure that pushdown will not try to add values
	 * into indices that don't exist.
	 * @param a		array/heap to be sorted
	 * @param i		index of array element to check for possible pushdown
	 * @param k		represents the faux size of the heap and prevents values from being sorted into ignored or nonexistent indices.
	 * @return		the array with element in index i in the correct location
	 */
	public static float[] pushdown(float[] a, int i, int k){
		int lchild = 2*i + 1;
		int rchild = 2*i + 2;
		int mini = i;
		if(lchild <= k){
			if(rchild <= k){ //if the rchild is valid
				if(a[lchild] <= a[rchild] && a[lchild] < a[mini])
					mini = lchild;
				else if(a[rchild] <= a[lchild] && a[rchild] < a[mini])
					mini = rchild;
			}
			else{ //if rchild is not valid
				if(a[lchild] < a[mini])
					mini = lchild;
			}
			if(mini != i){
				float temp = a[mini];
				a[mini] = a[i];
				a[i] = temp;
				pushdown(a, mini, k); 
			}
		}
		return a;
	}

	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	// sum an array of floats sequentially - high rounding error
	public static float seqAdd(float[] a) {
		float ret = 0;
		
		for (int i = 0; i < a.length; i++)
			ret += a[i];
		
		return ret;
	}

	// sort an array of floats and then sum sequentially - medium rounding error
	public static float sortAdd(float[] a) {
		Arrays.sort(a);
		return seqAdd(a);
	}

	// scan linearly through an array for two minimum values,
	// remove them, and put their sum back in the array. repeat.
	// minimized rounding error
	public static float min2ScanAdd(float[] a) {
		int min1, min2;
		float tmp;
		
		if (a.length == 0) return 0;
		
		for (int i = 0, end = a.length; i < a.length - 1; i++, end--) {
			
			if (a[0] < a[1]) { min1 = 0; min2 = 1; }	// initialize
			else { min1 = 1; min2 = 0; }
			
			for (int j = 2; j < end; j++) {		// find two min indices
				if (a[min1] > a[j]) { min2 = min1; min1 = j; }
				else if (a[min2] > a[j]) { min2 = j; }
			}
			
			tmp = a[min1] + a[min2];	// add together
			if (min1<min2) {			// put into first slot of array
				a[min1] = tmp;			// fill second slot from end of array
				a[min2] = a[end-1];
			}
			else {
				a[min2] = tmp;
				a[min1] = a[end-1];
			}
		}
		
		return a[0];
	}

	// read floats from a Scanner
	// returns an array of the floats read
	private static float[] getFloats(Scanner s) {
		ArrayList<Float> a = new ArrayList<Float>();

		while (s.hasNextFloat()) {
			float f = s.nextFloat();
			if (f >= 0)
				a.add(f);
		}
		return toFloatArray(a);
	}

	// copies an ArrayList to an array
	private static float[] toFloatArray(ArrayList<Float> a) {
		float[] ret = new float[a.size()];
		for(int i = 0; i < ret.length; i++)
			ret[i] = a.get(i);
		return ret;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		System.out.printf("Enter the adding algorithm to use ([h]eap, [m]in2scan, se[q], [s]ort): ");
		char algo = s.next().charAt(0);

		System.out.printf("Enter the non-negative floats that you would like summed, followed by a non-numeric input: ");
		float[] values = getFloats(s);
		float sum = 0;

		s.close();

		if (values.length == 0) {
			System.out.println("You must enter at least one value");
			System.exit(0);
		}
		else if (values.length == 1) {
			System.out.println("Sum is " + values[0]);
			System.exit(0);
			
		}
		
		switch (algo) {
		case 'h':
			sum = heapAdd(values);
			break;
		case 'm':
			sum = min2ScanAdd(values);
			break;
		case 'q':
			sum = seqAdd(values);
			break;
		case 's':
			sum = sortAdd(values);
			break;
		default:
			System.out.println("Invalid adding algorithm");
			System.exit(0);
			break;
		}

		System.out.printf("Sum is %f\n", sum);		

	}

}
