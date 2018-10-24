package tsp;

import java.awt.List;

import java.io.IOException;
import java.util.ArrayList;

public class Population {
	private static ArrayList<Solution> populations;
	private static Instance m_instance;
	private static Solution m_solution;
	private static long m_timeLimit;
	private static int nbIndividu;

	public Population (Instance m_instance, long m_timeLimit, int nbIndividu) {
		this.m_instance =m_instance;
		this.m_timeLimit=m_timeLimit;
		this.nbIndividu=nbIndividu;
	}
	
	/** 
	 * Créer une population avec un nombre d'individu définis, un temps de création d'un individu défini
	 * et une instance définie
	 */
	public static ArrayList<Solution> Miracle(Instance instance, int nbIndividu) throws Exception {
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
			
			for(int m=0; m<1000; m++) {
			 	 	  		  		    		 	
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
			System.out.println("avec une evaluation de " + indiv.evaluate() +" on ajoute ce nouvel individu" + toString(indiv));
			System.out.println(" ");
		}
		for(Solution indiv1 : population) {
			System.out.println(indiv1.evaluate());
		}
		
		return population;
		
	}
	
	public static ArrayList<Solution> CreerPopulation(Instance instance, long timeLimit, int nbIndividu ) throws Exception {
		ArrayList<Solution> population = new ArrayList<Solution>(nbIndividu);
		m_instance = instance; 	 	  		  		    		 	
		Solution m_solution = new Solution(m_instance); 	 	  		  		    		 	
		m_timeLimit = timeLimit;
		
		//solution la plus simple, toutes les villes dans l'ordre
		for (int i=0; i < m_instance.getNbCities(); i++)   { 	 	  		  		    		 	
			m_solution.setCityPosition(i, i);     	 	  		  		    		 	
			}  	 	  		  		    		 	
		m_solution.setCityPosition(0, m_instance.getNbCities()); 	 	  		  		    		 	
		
		
		
		// on va creer nbIndividu differents en utilisant un algo local search
		for(int j=0; j<nbIndividu; j++) { 
			Solution indiv=m_solution.copy();
			double distance_optim = m_solution.evaluate();
			//System.out.println("individu de départ avec une evaluation de "+m_solution.evaluate()+toString(m_solution));
			//while(spentTime < (m_timeLimit * 1000 - 100) ){ 
			for(int m=0; m<1000; m++) {
			 	 	  		  		    		 	
				int pos_a_echanger1 = (int)(Math.random()*m_instance.getNbCities());
				int pos_a_echanger2 = (int)(Math.random()*m_instance.getNbCities()); 	 	  		  		    		 	
				int index_city1 = indiv.getCity(pos_a_echanger1); 	 	  		  		    		 	
				int index_city2 = indiv.getCity(pos_a_echanger2); 	 	  		  		    		 	 	  		  		    		 	
				indiv.setCityPosition(index_city1, pos_a_echanger2);  	 	  		  		    		 	
				indiv.setCityPosition(index_city2, pos_a_echanger1); 	 	  		  		    		 	
				//System.out.println("l'évaluation de l'individu est : "+indiv.evaluate()); 	 	  		  		    		 	
				if (indiv.evaluate() >= distance_optim) { //l'ancienne solution est meilleure ou égale, on y revient	 	  		  		    		 	
					indiv.setCityPosition(index_city1, pos_a_echanger1);  	 	  		  		    		 	
					indiv.setCityPosition(index_city2, pos_a_echanger2); 	 	 	  		  		    		 	
				} 
					else { 	//la nouvelle solution est meilleure, on la conserve 
						//System.out.println("l'ancienne distance parcouru était de :" + distance_optim+ "la nouvelle est de : " + indiv.evaluate());
						distance_optim=indiv.evaluate(); 	 	  		  		    		 	
						int index_ville_depart = indiv.getCity(0); 	 	  		  		    		 	
						indiv.setCityPosition(index_ville_depart, m_instance.getNbCities());
						//System.out.println("on met la ville d'indice : "+ index_city1+ " en position " + pos_a_echanger2);
						//System.out.println("on met la ville d'indice : "+ index_city2+ " en position " + pos_a_echanger1);
						 	 	  		  		    		 	
					} 	 	  		  		    		 		 	  		  		    		  	 	  		  		    		 	 	  		  		    		 	
				
			}  	  		  		    		 	
			//on ajoute notre indidu à la population
			population.add(j, indiv);
			System.out.println("avec une evaluation de " + indiv.evaluate() +" on ajoute ce nouvel individu" + toString(indiv));
			System.out.println(" ");
		}
		for(Solution indiv1 : population) {
			System.out.println(indiv1.evaluate());
		}
		
		return population;
	}
	
	
	/**éliminer les moins bons individus et garder uniquement les meilleurs en fonction de leur évaluation 
	 * par rapport au temps de parcours total
	 * 
	 * étape de selection : on calcule la distance moyenne et on selectionne tous les individus en-dessous de cette moyenne
	 */
	
	public static ArrayList<Solution> SelectionnerPop(ArrayList<Solution> population) throws Exception { 	 	  		  		    		 	
		double moyenne=0;
		double evaluation =0;
		//on calul la distance moyenne de parcours 
		
		for(Solution individu : population) {
			evaluation = individu.evaluate();
			moyenne = moyenne + evaluation;
		}
		int nbIndividu= population.size();
		moyenne = moyenne/nbIndividu;
		System.out.println("la distance moyenne est :" + moyenne);
		
		//on selectionne les individus qui se situe en dessous de cette moyenne
		ArrayList<Solution> selection = new ArrayList<Solution>();
		System.out.println("il y a:" + population.size() + " individus dans la population");
		for(Solution individu : population) {
			if(individu.evaluate()<=moyenne) {
				selection.add(individu);
				System.out.println("on selection cet individu : "+toString(individu));
			}	 
		}
	
		return selection;
	}
	
	
	/**
	 * Etape de croisement des individus précédemment sélectionnés pour obtenir une nouvelle population. 
	 * Deux parents sont donc choisis pour appliquer un opérateur 
	 * de croisement afin d’obtenir un descendant (nouvel individu). technique de « crossover en un point ». 
	 * On recopie une partie du parent 1 et une partie du parent 2 pour obtenir 
	 * un nouvel individu. Le point de séparation des parents est appelé point de croisement. 
	 * Il faut cependant faire attention à ne pas visiter plusieurs fois la même ville (on ne recopie 
	 * pas les villes déjà visitées), 
	 * et à ne pas oublier de ville (on rajoute à la fin les villes non prises en compte)
	 */
	
	public static ArrayList<Solution> CroisementPop (ArrayList<Solution> selection ) throws Exception {
		ArrayList<Solution> pop_croisee = new ArrayList<Solution>(nbIndividu);
		int nbVilles = m_instance.getNbCities();
		Solution nouvel_individu = new Solution(m_instance); 	 
  	 	for(int i=0; i<selection.size()-1; i=i+2) { //(i<nbIndividu/4) on divise par 4 parce qu'on veut prendre que la premiere moitié de la selection qui a déjà été divisé par deux 
  	 		Solution parent1 = selection.get(i);
  	 		Solution parent2 = selection.get(i+1);
  	 		int point_de_separation = Math.round(nbVilles/2); //arbitrairement on sépare au milieu en prenant la partie entière pour être sur d'avoir un entier
  	 		System.out.println(" ");
  	 		System.out.println("coisement population");
  	 		System.out.println("le parent 1 avec une evaluation de : " + parent1.evaluate() + toString(parent1));
  	 		System.out.println("le parent 2 avec une evaluation de : " + parent2.evaluate() + toString(parent2));

  	 		
  	 		//on selectionne toutes les villes de parent1 situées avant le point de séparation et on les ajoute au nouvel individu
  	 		for(int j=0; j<300; j++) { //point_de_separation au lieux de 10
  	 			int indexCity = parent1.getCity(j);
  	 			nouvel_individu.setCityPosition(indexCity, j); // on recopie ville d'indice j de parents 1 à la position j de nouvel individu
  	 			//System.out.println("le nouvel individu visite la ville : "+indexCity+"en position : " + j);
  	 			
  	 		}
  	 		System.out.println(" ");
  	 		// on recopie toutes les villes non visitées par nouvel_individu de parents2 dans nouvel_individu
	 		int index_position_non_occupee_nouvel_individu = 300;

  	 		for(int k=0; k<nbVilles; k++) {
  	 			int indexCityParent2 = parent2.getCity(k);

  	 			if(contains(nouvel_individu,indexCityParent2)==false) {
  	 				System.out.println("Individu a visité la ville d'indice: "+indexCityParent2 + " ?" +contains(nouvel_individu,indexCityParent2));
  	 				nouvel_individu.setCityPosition( indexCityParent2, index_position_non_occupee_nouvel_individu);
  	 				System.out.println("on ajoute ville d'index " +indexCityParent2+" en position " + index_position_non_occupee_nouvel_individu);
  	 				index_position_non_occupee_nouvel_individu ++;
  	 			}
	 				
  	 		}
  	 		nouvel_individu.setCityPosition(nouvel_individu.getCity(0), nbVilles); // on doit avoir 1er ville = dernière ville
  	 		pop_croisee.add(nouvel_individu); //on ajoute ce descendant à notre solution
  	 	}
  	 	for(Solution ind : pop_croisee) {
	 			System.out.println("on ajoute le descendant suivant avec une evaluation de : " + ind.evaluate() + toString(ind));
	 		}
  	 	return selection;  
	}
	public static Solution Meilleur_Individu (ArrayList<Solution> population) throws Exception {
		Solution meilleur_ind = population.get(0);
		double plus_courte_distance = meilleur_ind.evaluate();
		for(Solution s: population) {
			if(s.evaluate()<plus_courte_distance) {
				meilleur_ind=s;
			}
		}
		return meilleur_ind;
				
	}
	
	/**
	 * Si l'individu créé contient déjà la ville d'indice i return true, 
	 * sinon return false
	 * @throws Exception 
	 */
	public static boolean contains(Solution individu, int i) throws Exception {
		boolean res = false;
		for(int j=0; j < m_instance.getNbCities(); j++) {
			if(individu.getCity(j)==i) {
				res = true;
			}
		}
		return res;
	}
	

	public static String toString(Solution individu) {
		String res ="";
		for (int i = 1; i <=m_instance.getNbCities() ; i++) {
			try {
				res = res +"-" + individu.getCity(i);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}
	
	public static void main(String[] args) throws Exception {
		
		ArrayList<Solution> population = new ArrayList<Solution>();
		ArrayList<Solution> selection = new ArrayList<Solution>(); 
		ArrayList<Solution> pop_croisee = new ArrayList<Solution>(); 

		// génétique
		Instance inst = new Instance("instances/d657.tsp", 0);
		population = CreerPopulation(inst, (long)0.1, 50);
		selection = SelectionnerPop(population);
		pop_croisee = CroisementPop(selection);
		Solution solution = Meilleur_Individu(pop_croisee);
		System.out.println(solution.evaluate() + toString(solution));
		
		//local search
		/*population = Miracle(inst, 1000);
		Solution solution = Meilleur_Individu(population);
		System.out.println(solution.evaluate() + toString(solution));*/

	}
}

	
