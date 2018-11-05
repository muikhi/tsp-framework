package tsp;

import java.util.ArrayList;

public class Carre_plus_LS {
	
	/**
	 * Dans cette classe on combine le découpage en carré de 4 de l'espace géographique avec l'algorithme de local search
	 * On commence par créer une solution où toutes les villes sont réparties par carrés. On ajoute d'abord toutes les villes situées 
	 * dans le carré 1 puis toutes celles dans le carré 2 puis 3, puis 4.
	 * On utilise ensuite le même algo de local search que dans la classe Local_Search à la différence qu'on ajoute la contrainte qu'on 
	 * ne fait des swaps entre les villes uniquement si les deux villes appartiennent au même carré.
	 * On selectionne ensuite le meilleur individu de cette population.
	 */
	
		private static Instance m_instance;
		private static int nbIndividu;

		public Carre_plus_LS () {
		}
		
		/**
		 * On découpe géographiquement la carte des villes en 4 carrés de taille identique
		 * @param index_ville, un entier
		 * @param instance de type Instance
		 * @return le numéro de carré dans lequel se situe la ville d'indexe : indexe_ville pour une instance donnée
		 * @throws Exception
		 */

		
		public static int Numero_carre_4 (int index_ville, Instance instance)throws Exception{
			m_instance = instance;
			double coordonne_x=m_instance.getX(index_ville);
			double coordonne_y=m_instance.getY(index_ville);
			int res;
			if(coordonne_x<= m_instance.getMaxX()/2) {
				if(coordonne_y <= m_instance.getMaxY()/2) {
					res = 1;
				}
				else {
						res=2;
					}
			}
				else {
						if(coordonne_y<=m_instance.getMaxY()/2) {
							res = 4;
						}
					else {
						res = 3;
					}
				}
			
			return res;
		}
		
		/**Pour un découpage en 4 carrés
		 * on crée un tableau qui associe à la case d'indice i la ville numéro i et qui stock son numéro de carré
		 * @param instance de type Instance
		 * @return un tableau composé de toutes les villes auquelles on a associé un numéro de carré
		 * @throws Exception
		 */
		public static int[] tableau_ville_4(Instance m_instance) throws Exception{
			int[] tab = new int[m_instance.getNbCities()];
			for(int i=0; i<m_instance.getNbCities(); i++) {
				tab[i]=Numero_carre_4(i, m_instance);
				}
			return tab;
		}
		
		/**On crée une population composée de nbIndividu, chaque individu doit respecter le découpage en carrés géographiques de 4
		 * On met d'abord toutes les villes d'un même carré ( ici le 1) puis toutes les villes du carré 2 
		 * en respectant l'ordre 1,2,3,4 on obtient un individu. Sur cet individu on effectue 2000 permutations de villes 
		 * uniquement si elles sont dans le même carré et si cette permutation améliore la solution
		 * @param instance
		 * @param nbIndividu
		 * @return une population composée d'individus de type Solution
		 * @throws Exception
		 */
		
		public static ArrayList<Solution> Algo_Local_Search(Instance instance, int nbIndividu, int nb_repetition) throws Exception {
			ArrayList<Solution> population = new ArrayList<Solution>(nbIndividu);
			m_instance = instance; 	 	  		  		    		 	
			Solution m_solution = new Solution(m_instance); 	 	  		  		    		 	
			
			//On crée individu de départ qui respecte bien la classification par carré
			m_solution.setCityPosition(0,0);     	 	  		  		    		 	
			int position =1;
			int[] tableau_ville = tableau_ville_4(m_instance);
			for(int i=1; i<m_instance.getNbCities(); i++) {
				if(tableau_ville[i]==1) {
					m_solution.setCityPosition(i,position);     	 	  		  		    		 	
					position++;
				}
			}
			for(int i=1; i<m_instance.getNbCities(); i++) {
				if(tableau_ville[i]==2) {
					m_solution.setCityPosition(i,position);     	 	  		  		    		 	
					position++;
				}
			}
			for(int i=1; i<m_instance.getNbCities(); i++) {
				if(tableau_ville[i]==3) {
					m_solution.setCityPosition(i,position);     	 	  		  		    		 	
					position++;
				}
			}
			for(int i=1; i<m_instance.getNbCities(); i++) {

				if(tableau_ville[i]==4) {
					m_solution.setCityPosition(i,position);     	 	  		  		    		 	
					position++;
				}
			}
			
			// on va creer nbIndividu differents en utilisant un algo local search
			for(int j=0; j<nbIndividu; j++) { 
				Solution indiv=m_solution;
				double distance_optim = m_solution.evaluate();
				for(int m=0; m<nb_repetition; m++) { 		  		    		 	
					int pos_a_echanger1 = (int)(Math.random()*m_instance.getNbCities());
					int pos_a_echanger2 = (int)(Math.random()*m_instance.getNbCities()); 	 	  		  		    		 	
					int index_city1 = indiv.getCity(pos_a_echanger1); 	 	  		  		    		 	
					int index_city2 = indiv.getCity(pos_a_echanger2);
					if(tableau_ville[index_city1]==tableau_ville[index_city2]) { // les deux villes appartiennent au même carré
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
				}  	  		  		    		 	
				//on ajoute notre indidu à la population
				population.add(j, indiv);
			}
			return population;
		}
		
		

}
