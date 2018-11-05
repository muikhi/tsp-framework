package tsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * This class is the place where you should enter your code and from which you can create your own objects.
 * 
 * The method you must implement is solve(). This method is called by the programmer after loading the data.
 * 
 * The TSPSolver object is created by the Main class.
 * The other objects that are created in Main can be accessed through the following TSPSolver attributes: 
 * 	- #m_instance :  the Instance object which contains the problem data
 * 	- #m_solution : the Solution object to modify. This object will store the result of the program.
 * 	- #m_timeLimit : the maximum time limit (in seconds) given to the program.
 *  
 * @author Damien Prot, Fabien Lehuede, Axel Grimault
 * @version 2017
 * 
 */
// morgane
//sese
//claire
public class TSPSolver {

	// -----------------------------
	// ----- ATTRIBUTS -------------   
	// -----------------------------

	/**
	 * The Solution that will be returned by the program.
	 */
	private Solution m_solution;
	

	/** The Instance of the problem. */
	private Instance m_instance;

	/** Time given to solve the problem. */
	private long m_timeLimit;

	
	// -----------------------------
	// ----- CONSTRUCTOR -----------
	// -----------------------------

	/**
	 * Creates an object of the class Solution for the problem data loaded in Instance
	 * @param instance the instance of the problem
	 * @param timeLimit the time limit in seconds
	 */
	public TSPSolver(Instance instance, long timeLimit) {
		m_instance = instance;
		m_solution = new Solution(m_instance);
		m_timeLimit = timeLimit;
	}

	// -----------------------------
	// ----- METHODS ---------------
	// -----------------------------

	/**
	 * **TODO** Modify this method to solve the problem.
	 * 
	 * Do not print text on the standard output (eg. using `System.out.print()` or `System.out.println()`).
	 * This output is dedicated to the result analyzer that will be used to evaluate your code on multiple instances.
	 * 
	 * You can print using the error output (`System.err.print()` or `System.err.println()`).
	 * 
	 * When your algorithm terminates, make sure the attribute #m_solution in this class points to the solution you want to return.
	 * 
	 * You have to make sure that your algorithm does not take more time than the time limit #m_timeLimit.
	 * 
	 * @throws Exception may return some error, in particular if some vertices index are wrong.
	 */
	public void solve() throws Exception
	{
	
		m_solution.print(System.err);
			 	
		    		
		
		//TEST 3 : CORRECTION DU TEST 2 POUR PLACER CHAQUE VILLE V AU MEILLEUR ENDROIT
		long startTime = System.currentTimeMillis();
		long spentTime = 0;
		do
		{

		//solution de base : la ville i est en position i
		for (int i=0; i < m_instance.getNbCities(); i++)   { 	 	  		  		    		 	
		m_solution.setCityPosition(i, i);     	 	  		  		    		 	
		} 
		m_solution.setCityPosition(0, m_instance.getNbCities());
		double distance_optim = m_solution.evaluate() ;
		
		
		// On va modifier la copie de la solution initiale
		Solution m_sol = m_solution.copy();
		
		//On va modifier la position de la ville v parmi toutes les villes à fixer 
		List<Integer> villes_a_fixer = new ArrayList<Integer>() ; 
		for (int v=1; v<= m_instance.getNbCities(); v++) {
			villes_a_fixer.add(v);
		}
		
			
		//On va faire une boucle pour tester toutes les positions pour toutes les villes non déjà fixees
		
		while (villes_a_fixer != null) {
		
			//Ville à fixer prise dans l'ordre croissant
			int v = villes_a_fixer.remove(0) ; 
		
			
			//On va tester toutes les positions j de la ville v 
		
			//On cree une HashMap pour stocker l'association distance - solution associee pour chaque ville a fixer
			Map<Double, Solution > hm = new HashMap<>();
		
			for (int j = 1; j<= m_instance.getNbCities()-1; j++ ) {
				int pos_init = getPos(v, m_sol) ;
			
				if (pos_init >= j ) {
					//on insere v en position i et on decale le reste
					for (int i = j; i<= pos_init ; i++) {
						int val_a_placer = v ;
						int temp = m_sol.getCity(i);
						m_sol.setCityPosition(val_a_placer, i);
						val_a_placer = temp ; 
						i++ ; 
						} 
					hm.put(m_sol.evaluate(), m_sol);
					
					
				
			}else { //j< pos_init 
				//on insere v en position i et on decale le reste
				for (int i = m_instance.getNbCities()-1; i> pos_init ; i++) {
					int val_a_placer = v ;
					int temp = m_sol.getCity(i);
					val_a_placer = temp ; 
					i--;	
				}
				hm.put(m_sol.evaluate(), m_sol);
			}
			
		}
		
		//On choisit la meilleure solution, cad qu'on fixe la ville a la position ou sa distance est minimale
		Set<Double> distances = hm.keySet() ; 
		System.out.println(distances);
		Double[] distances_array = distances.toArray(new Double[hm.size()]);
		double distance_min = getMin(distances_array) ;
		m_sol = hm.get(distance_min);
		System.out.println(m_sol);
		 m_solution = m_sol ;
		}
		
		
		// TODO
		// Code a loop base on time here
		 spentTime = System.currentTimeMillis() - startTime;
	}while(spentTime < (m_timeLimit * 1000 - 100) );
		
		
		 
	}

	
	// -----------------------------
	// ----- GETTERS / SETTERS -----
	// -----------------------------
	
		/** @return l'index de la valeur min du tableau */
		public int getMin(Double[] distances_array) {
			int ind = 0 ;
			double min = 300000 ; // une grande constante du probleme pour la distance maximale
			for (int i=1; i<distances_array.length; i++) {
				if (distances_array[i]<min) {
					min = distances_array[i];
					ind = i ;
				}
			}
			return ind ;
		}
		/** @param m_sol 
		 * @return la position de la ville  
		 * @throws Exception */
		public int getPos(int indexCity, Solution m_sol) throws Exception {
			int pos = 0 ;
			while (m_sol.getCity(pos)!= indexCity && pos< m_instance.getNbCities() ) {
				pos++;
			}
			return pos ;
		}
		

	/** @return the problem Solution */
	public Solution getSolution() {
		return m_solution;
	}

	/** @return problem data */
	public Instance getInstance() {
		return m_instance;
	}

	/** @return Time given to solve the problem */
	public long getTimeLimit() {
		return m_timeLimit;
	}

	/**
	 * Initializes the problem solution with a new Solution object (the old one will be deleted).
	 * @param solution : new solution
	 */
	public void setSolution(Solution solution) {
		this.m_solution = solution;
	}

	/**
	 * Sets the problem data
	 * @param instance the Instance object which contains the data.
	 */
	public void setInstance(Instance instance) {
		this.m_instance = instance;
	}

	/**
	 * Sets the time limit (in seconds).
	 * @param time time given to solve the problem
	 */
	public void setTimeLimit(long time) {
		this.m_timeLimit = time;
	}

}
