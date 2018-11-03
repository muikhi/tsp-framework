package tsp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecuitSimule extends TSPSolver {

	private Solution m_solution ;
	private Instance m_instance ; 
	private long m_timeLimit ;

	
	
	public RecuitSimule(Instance instance, long timeLimit) {
		super(instance, timeLimit);
		
		// TODO Auto-generated constructor stub
	}
	
	
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
	
	/** modifie la copie de la solution m_solution avec une methode de recuit simule */
	
	public void solve() throws Exception {	
	
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
			for (int i = j; i<= pos_init ; j++) {
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
	Double[] distances_array = distances.toArray(new Double[hm.size()]);
	double distance_min = getMin(distances_array) ;
	m_sol = hm.get(distance_min);
	System.out.println(m_sol);
	 
	}
	
	
	// TODO
	// Code a loop base on time here
	 spentTime = System.currentTimeMillis() - startTime;
}while(spentTime < (m_timeLimit * 1000 - 100) );
	
	
	 
/*	
	//On echange la position de la ville j avec la ville a sa suite
	for (int j = 2; j<m_instance.getNbCities(); j++) { 
	int temp = m_sol.getCity(j);
	m_sol.setCityPosition(j-1, j);
	m_sol.setCityPosition(temp, j-1  );
	double dist_j = m_sol.evaluate();  
	if (distance_optim >dist_j) {
		 hm.put(dist_j, m_sol);		// on stocke la solution et sa distance quand elle est minimale
		 distance_optim = dist_j ;
		}
	}
	System.out.println(hm.toString()); 
*/	
	
	 
	
	
	


/*
	//TEST 2 : ON CHOISIT UNE VILLE QUI SERA PLACEE AU MEILLEUR ENDROIT EN 
	//PARCOURANT LA TOURNEE 
	//on choisit ARBITRAIREMENT la ville 1 pour la changer de place	 		  		    		 	

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
	
	/*
	
/*	
	// TEST 1 AVEC SWAPS ALEATOIRES
	 
	 
// Example of a time loop
long startTime = System.currentTimeMillis();
long spentTime = 0;

// Le sommet  i est inséré en position i dans la tournée

for (int i=0; i < m_instance.getNbCities(); i++)   { 	 	  		  		    		 	
	m_solution.setCityPosition(i, i);     	 	  		  		    		 	
	} 
m_solution.setCityPosition(0, m_instance.getNbCities());
double distance_optim = m_solution.evaluate() ; 

	do
{
	int pos_a_echanger1 = (int)(Math.random()*m_instance.getNbCities()) ;
	int pos_a_echanger2 = (int)(Math.random()*m_instance.getNbCities() );
	int index_city1 = m_solution.getCity(pos_a_echanger1);
	int index_city2 = m_solution.getCity(pos_a_echanger2);
	System.out.println("pos_a_echanger1 " + pos_a_echanger1);
	System.out.println("pos_a_echanger2 " + pos_a_echanger2);
	System.out.println("index_city1 " + index_city1);
	System.out.println("index_city2 " + index_city2);

	m_solution.setCityPosition(index_city1, pos_a_echanger2); 
	m_solution.setCityPosition(index_city2, pos_a_echanger1);
	
	
	if (m_solution.evaluate() > distance_optim) {
		m_solution.setCityPosition(index_city1, pos_a_echanger1); 
		m_solution.setCityPosition(index_city2, pos_a_echanger2);
		
	}
	else {
		distance_optim=m_solution.evaluate();
		int index_ville_depart = m_solution.getCity(0);
		m_solution.setCityPosition(index_ville_depart, m_instance.getNbCities());
	}
	
	
	
	// TODO
	// Code a loop base on time here
	spentTime = System.currentTimeMillis() - startTime;
}while(spentTime < (m_timeLimit * 1000 - 100) );

*/
	
	}	
	
	
	
	
	
	
	
	
	
	
	
}
