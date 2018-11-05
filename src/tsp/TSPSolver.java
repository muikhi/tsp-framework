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
		long startTime = System.currentTimeMillis();
		long spentTime = 0;
		m_solution.print(System.err);
		     	 	  		  		    		 			
		//local search
		/**ArrayList<Solution> population = new ArrayList<Solution>();
		population = Local_Search.Algo_Local_Search(m_instance, 20000);
		m_solution = Genetique.Meilleur_Individu(population);
		*/
		
		//Découpage en carré puis local search en respectant les carrés
		/**ArrayList<Solution> population = new ArrayList<Solution>();
		population = Carre_plus_LS.Algo_Local_Search(m_instance, 20000, 10000);
		m_solution = Genetique.Meilleur_Individu(population);
		*/
		
		ArrayList<Solution> population = new ArrayList<Solution>();
		ArrayList<Solution> selection = new ArrayList<Solution>(); 
		ArrayList<Solution> pop_croisee = new ArrayList<Solution>(); 
		ArrayList<Solution> pop_rassemblee = new ArrayList<Solution>(); 
		ArrayList<Solution> pop_complete = new ArrayList<Solution>(); 
		ArrayList<Solution> pop_mute = new ArrayList<Solution>(); 
		ArrayList<Solution> pop_sans_doublons = new ArrayList<Solution>(); 
		
		//genetique avec population créée par local search
		population = Local_Search.Algo_Local_Search(m_instance, 500);

		do
		{
			selection = Genetique.Selectionner_Meilleur_Pop(population);
			pop_croisee = Genetique.CroisementPop(selection, m_instance);
			pop_rassemblee= Genetique.Rassemblement_Pop(selection, pop_croisee);
			pop_complete = Genetique.Completer_Pop(pop_rassemblee, population, m_instance);
			pop_mute=Genetique.MutationPop(pop_complete, m_instance);
			pop_sans_doublons=Genetique.Modifier_doublon(pop_mute, m_instance);
			population = pop_sans_doublons;	
			m_solution=Genetique.Meilleur_Individu(population);
  	 		m_solution.setCityPosition(m_solution.getCity(0), m_instance.getNbCities()); // on doit avoir 1er ville = dernière ville

			spentTime = System.currentTimeMillis() - startTime; 
			
		} while(spentTime < (m_timeLimit * 1000 - 100) ); 
		
		
		//genetique avec population créée aléatoirement
		population = Genetique.CreerPopulation(m_instance,100);

		do
		{
			selection = Genetique.Selectionner_Meilleur_Pop(population);
			pop_croisee = Genetique.CroisementPop(selection, m_instance);
			pop_rassemblee= Genetique.Rassemblement_Pop(selection, pop_croisee);
			pop_complete = Genetique.Completer_Pop(pop_rassemblee, population, m_instance);
			pop_mute= Genetique.MutationPop(pop_complete, m_instance);
			pop_sans_doublons= Genetique.Modifier_doublon(pop_mute, m_instance);
			population = pop_sans_doublons;	
			m_solution= Genetique.Meilleur_Individu(population);
			m_solution.setCityPosition(m_solution.getCity(0), m_instance.getNbCities());
			spentTime = System.currentTimeMillis() - startTime; 
			
		} while(spentTime < (m_timeLimit * 1000 - 100) ); 
		
		//Genetique avec les 4 carrés
		population = Genetique_carres_4.CreerPopulation_Carre_4(m_instance, 500);
		do
		{
			selection = Genetique_carres_4.Selectionner_Meilleur_Pop(population);
			int point_de_croisement= Genetique_carres_4.get_point_de_croisement_4(m_instance);
			pop_croisee = Genetique_carres_4.CroisementPop(selection, point_de_croisement);
			pop_rassemblee= Genetique_carres_4.Rassemblement_Pop(selection, pop_croisee);
			pop_complete = Genetique_carres_4.Completer_Pop_4(pop_rassemblee, population);
			pop_mute=Genetique_carres_4.MutationPop_4(pop_complete);
			pop_sans_doublons=Genetique_carres_4.Modifier_doublon_4(pop_mute);
			population = pop_sans_doublons;	
			m_solution=Genetique_carres_4.Meilleur_Individu(population);
			m_solution.setCityPosition(m_solution.getCity(0), m_instance.getNbCities());
			spentTime = System.currentTimeMillis() - startTime; 
			
		} while(spentTime < (m_timeLimit * 1000 - 100) ); 
		
		
		//Genetique avec les 16 carrés
		population = Genetique_carres_16.CreerPopulation_Carre_16(m_instance, 100, Genetique_carres_16.tableau_ville_16(m_instance));
		do
		{
			selection = Genetique_carres_16.Selectionner_Meilleur_Pop(population);
			pop_croisee = Genetique_carres_16.CroisementPop(selection, (int)m_instance.getNbCities()/2);
			pop_rassemblee= Genetique_carres_16.Rassemblement_Pop(selection, pop_croisee);
			pop_complete = Genetique_carres_16.Completer_Pop_16(pop_rassemblee, population);
			pop_mute=Genetique_carres_16.MutationPop_16(pop_complete);
			pop_sans_doublons=Genetique_carres_16.Modifier_doublon_16(pop_mute);
			population = pop_sans_doublons;	
			m_solution=Genetique_carres_16.Meilleur_Individu(population);
			spentTime = System.currentTimeMillis() - startTime; 
			
		} while(spentTime < (m_timeLimit * 1000 - 100) ); 
	
		
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
