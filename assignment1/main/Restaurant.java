/**
 * 
 */
package main;

import entities.*;
import sharedRegions.*;
import genclass.*;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 *
 */
public class Restaurant {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Chef chef; // Reference to the Chef thread
		Waiter waiter; // Reference to the Waiter thread
		Student[] student = new Student[SimulPar.N]; // Array of references to the Student threads
		Bar bar; // Reference to the Bar thread
		GeneralRepository repository; // Reference to the GeneralRepository thread
		Kitchen kitchen; // Reference to the Kitchen thread
		Table table; // Reference to the Table thread

		GenericIO.writelnString("The Restaurant");

		repository = new GeneralRepository("log");
		kitchen = new Kitchen(repository);
		table = new Table(repository);
		bar = new Bar(repository,table);
		chef = new Chef("Chef", kitchen, bar, repository);
		waiter = new Waiter("Waiter", table, bar, kitchen, repository);
		for (int i = 0; i < SimulPar.N; i++) {
			student[i] = new Student("Stu" + i, i, table, bar, repository);
		}

		chef.start();
		waiter.start();
		for (int i = 0; i < SimulPar.N; i++) {
			student[i].start();
		}

		for (int i = 0; i < SimulPar.N; i++) {
			student[i].start();
		}

		for (int i = 0; i < SimulPar.N; i++) {
			try {
				student[i].join();
			} catch (InterruptedException e) {
			}
			GenericIO.writelnString("The Student " + (i) + " just terminated");
		}

		try {
			chef.join();
		} catch (InterruptedException e) {
		}
		GenericIO.writelnString("The Chef just terminated");

		try {
			waiter.join();
		} catch (InterruptedException e) {
		}
		GenericIO.writelnString("The Waiter just terminated");
	    GenericIO.writelnString ();

		
	}

}
