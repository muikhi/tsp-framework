package tsp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecuitSimule  {

	// -----------------------------
	// ----- ATTRIBUTS -------------   
	// -----------------------------
	
	/** The Solution that will be returned by the program. */ 
	private Solution m_solution;
	
	/** The Instance of the problem. */
	private Instance m_instance;

	/** Time given to solve the problem. */
	private long m_timeLimit;

	
	// -----------------------------
	// ----- CONSTRUCTOR -----------
	// -----------------------------
	/**
	 * Crée un objet de la classe RecuitSimule pour les données du problème en paramètre  (instance)
	 * @param instance , les donnees du probleme
	 * @param timeLimit, la limite temporelle en secondes
	 * @throws Exception
	 */
	public RecuitSimule(Instance instance, long timeLimit) throws Exception {
		m_instance = instance ;
		m_timeLimit = timeLimit ; 
		m_solution = new Solution (instance);
		
		//solution de base : la ville i est en position i
		for (int i=0; i < m_instance.getNbCities(); i++)   { 	 	  		  		    		 	
		m_solution.setCityPosition(i, i);     	 	  		  		    		 	
		} 
		m_solution.setCityPosition(0, m_instance.getNbCities());
		
	}
	
	// -----------------------------
	// ----- GETTERS  --------------
	// -----------------------------
	
	/**
	 * Donne l'index du tableau donné pour lequel la valeur est minimale
	 * @param distances_array
	 * @return l'index de la valeur minimale du tableau 
	 */
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
	
	/**
	 * Donne la position de la ville d'index indexCity donné
	 * @param indexCity
	 * @param m_sol
	 * @return la position de la ville d'index donné
	 * @throws Exception
	 */
	
	public int getPos(int indexCity, Solution m_sol) throws Exception {
		int pos = 0 ;
		while (m_sol.getCity(pos)!= indexCity && pos< m_instance.getNbCities() ) {
			pos++;
		}
		return pos ;
	}
	
	
	// -----------------------------
	// ----- METHOD ---------------
	// -----------------------------
	//TEST 2 : CORRECTION DU TEST 1 POUR PLACER CHAQUE VILLE V AU MEILLEUR ENDROIT
	
	/**
	 * Retourne la solution du probleme associe a l'instance par une methode de recuit simule
	 * en placant progressivement chaque ville a l'endroit qui rend la distance minimale.
	 * @param m_instance
	 * @return la solution du probleme avec une methode de recuit simule 
	 * @throws Exception
	 */
	public Solution solveRS(Instance m_instance) throws Exception {	
	
	this.m_solution.print(System.err);
	
	long startTime = System.currentTimeMillis();
	long spentTime = 0;
	
	// On va modifier la copie de la solution initiale 
	Solution m_sol = this.m_solution.copy();
	
	do
	{
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
				m_sol.setCityPosition(val_a_placer, i);
				val_a_placer = temp ; 
				i--;	
			}
			hm.put(m_sol.evaluate(), m_sol);
		}
		
	}
	
	//On choisit la meilleure solution, cad qu'on fixe la ville a la position ou sa distance est minimale
	Set<Double> distances = hm.keySet() ;
	System.out.println("distances : " + distances);
	Double[] distances_array = distances.toArray(new Double[hm.size()]);
	double distance_min = getMin(distances_array) ;
	m_sol = hm.get(distance_min);
	System.out.println("solution retenue : " + m_sol);
	 
	}
	
	// Code a loop base on time here
	 spentTime = System.currentTimeMillis() - startTime;
}while(spentTime < (m_timeLimit * 1000 - 100) );
	
	this.m_solution = m_sol ;
	System.out.println("La solution finale est : " + this.m_solution);
	return this.m_solution ; 
	}	

	public static void main (String []args) throws Exception {
	Instance inst = new Instance("instances/d657.tsp", 0);	
	RecuitSimule rs = new RecuitSimule (inst, 60) ; 
	rs.solveRS(inst) ; 
		
	}	
		
/*	
	//TEST 1 : ON CHOISIT UNE VILLE QUI SERA PLACEE AU MEILLEUR ENDROIT EN PARCOURANT LA TOURNEE 
	//on choisit de deplacer ARBITRAIREMENT la ville 1	 		  		    		 	

	double[] distances_iter = new double[m_instance.getNbCities()] ; // on cree un tableau qui enregistre le evaluate pour
	//chaque iteration, on choisira la meilleure distance entre 1 et nb de villes-1 
	// distances_iter[i] represente le evaluate quand la ville1 est mise en position i
		distances_iter[1] = distance_optim ;
	
		//on calcule la distance dans le cas ou la ville 1 est à la position 2,3,...,n-1
		for (int i = 1 ; i<m_instance.getNbCities()-1; i++) { 
			m_solution.setCityPosition(1, i+1); 
			int pos = i+1;
			for (int j = i+1; j<m_instance.getNbCities()-1; j++){ //on decale les villes suivantes de la 1
				m_solution.setCityPosition(j, j+1);
				}
			m_solution.setCityPosition(656, 1); //la ville 656 se retrouve en pos 1
			m_solution.setCityPosition(0, 0);
			m_solution.setCityPosition(0,m_instance.getNbCities());// attention la premiere et la derniere ville doivent etre les memes
			distances_iter[i] = m_solution.evaluate() ; 	
			System.out.println(" la distance quand la ville 1 est en pos "+ pos + " est "+ m_solution.evaluate() );		 		  		    		 	
		}
		// on met la ville a l'index de la ou la distance est min
		m_solution.setCityPosition(1, getMin(distances_iter));
		
		if (getMin(distances_iter)==0) {
			m_solution.setCityPosition(1,m_instance.getNbCities());
			m_solution.setCityPosition(0,1);
			for (int i=2; i < m_instance.getNbCities(); i++)   { 	 	  		  		    		 	
				m_solution.setCityPosition(i, i);     	 	  		  		    		 	
				} 
			
		}else if (getMin(distances_iter)>1 ){
			m_solution.setCityPosition(0,m_instance.getNbCities());
			m_solution.setCityPosition(0,0);
			m_solution.setCityPosition(656, 1);
			for (int i = getMin(distances_iter) ; i<m_instance.getNbCities()-2; i++) { 	 		 		  		    		 	
				m_solution.setCityPosition(1 + i, i+2);
			}
		}else { // la ville 1 est en pos 1 donc aucun decalage
			for (int i=0; i < m_instance.getNbCities(); i++)   { 	 	  		  		    		 	
				m_solution.setCityPosition(i, i);     	 	  		  		    		 	
				} 
			m_solution.setCityPosition(0, m_instance.getNbCities());
		System.out.println("distance optimale : " +m_solution.evaluate());	
		}
*/ 
}	