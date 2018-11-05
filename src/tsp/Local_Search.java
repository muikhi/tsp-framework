package tsp;
import java.util.ArrayList;

public class Local_Search {


	private static Instance m_instance;
	

	public Local_Search (){	
	}
	
	/**
	 * On commence par créer la solution la plus simple en mettant tous les individus dans l'ordre de leur indice 
	 * (ville 1 en 1er puis ville 2, ...). En partant de cet individu on va créer nbIndividu différents. Pour cela on fait 2000
	 * swaps aléatoires entre 2 villes et on fait ce swap uniquement si ce swap améliore la solution. 
	 * On obtient une population 
	 * On va ensuite appeler dans le TSP solver le meilleur individu de cette population. 
	 * 
	 * @param instance
	 * @param nbIndividu
	 * @return une population composée d'individus de type Solution
	 * @throws Exception
	 * 
	 */
	
	public static ArrayList<Solution> Algo_Local_Search(Instance instance, int nbIndividu) throws Exception {
		ArrayList<Solution> population = new ArrayList<Solution>(nbIndividu);
		m_instance = instance; 	 	  		  		    		 	
		Solution m_solution = new Solution(m_instance); 	 	  		  		    		 	
		
		//solution la plus simple, toutes les villes dans l'ordre
		for (int i=0; i < m_instance.getNbCities(); i++)   { 	 	  		  		    		 	
			m_solution.setCityPosition(i, i);     	 	  		  		    		 	
			}  	 	  		  		    		 	
		m_solution.setCityPosition(0, m_instance.getNbCities()); 	 	  		  		    		 	
		
		// on va creer nbIndividu differents en utilisant un algo local search
		for(int j=0; j<nbIndividu; j++) { 
			Solution indiv=m_solution;
			double distance_optim = m_solution.evaluate();
			for(int m=0; m<2000; m++) { 		  		    		 	
				int pos_a_echanger1 = (int)(Math.random()*m_instance.getNbCities());
				int pos_a_echanger2 = (int)(Math.random()*m_instance.getNbCities()); 	 	  		  		    		 	
				int index_city1 = indiv.getCity(pos_a_echanger1); 	 	  		  		    		 	
				int index_city2 = indiv.getCity(pos_a_echanger2); 	 	  		  		    		 	 	  		  		    		 	
				indiv.setCityPosition(index_city1, pos_a_echanger2);  	 	  		  		    		 	
				indiv.setCityPosition(index_city2, pos_a_echanger1); 	 	  		  		    		 	
				if (indiv.evaluate() >= distance_optim) { //l'ancienne solution est meilleure ou égale, on y revient	 	  		  		    		 	
					indiv.setCityPosition(index_city1, pos_a_echanger1);  	 	  		  		    		 	
					indiv.setCityPosition(index_city2, pos_a_echanger2); 	 	 	  		  		    		 	
				} 
					else { 	//la nouvelle solution est meilleure, on la conserve 
						distance_optim=indiv.evaluate(); 	 	  		  		    		 	
						int index_ville_depart = indiv.getCity(0); 	 	  		  		    		 	
						indiv.setCityPosition(index_ville_depart, m_instance.getNbCities());  		  		    		 	
					} 	 	  		  		    		 		 	  		  		    		  	 	  		  		    		 	 	  		  		    		 	
			}  	  		  		    		 	
			//on ajoute notre indidu à la population
			population.add(j, indiv);
		}
		return population;
	}
	
}