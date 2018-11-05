package tsp;
import java.util.ArrayList;


public class Genetique {

	
	/**On utilise ici la méthode génétique avec la création d'une population de façon soit aléatoire soit en utilisant 
	 * un algo de local search
	 * PLusieurs étapes sont à respecter :
	 * 1- Création d'une population comportant n individus
	 * 2- Selection des meilleurs individus (tout ceux au-dessus de la moyenne)
	 * 3- Croisement de la population parmis les meilleurs individu on croise deux individus pour donner deux enfants
	 * 4- Rassemblement des enfants et des meilleurs individu initiaux
	 * 5- Mutationaléatoire au sein de la population parmis les individus les moins bons (on ne modifie jamais 
	 * les meilleurs individus pour être sur de ne pas perdre une "bonne solution")
	 * 6- On complète ou diminue la population pour avoir exactement le même nb d'individu qu'au début. 
	 * Si la population est plus petite que la pop initiale on la complete avec des individu issue de mutation de la pop initiale
	 * Si la population est plus grande on retire les plus mauvais individu de la population
	 * Si elle a la même taille on ne fait rien
	 * 7- Supression des individus en doublons et on les remplace par des individus selectionnés parmis les meilleurs qui ont ensuite été mutés
	 * On recommance ces étapes tant qu'on est encore dans le temps imparti
	 * 
	 * Les fonctions sont :
	 * ArrayList<Solution> CreerPopulation(Instance instance, int nbIndividu )
	 * ArrayList<Solution> Selectionner_Meilleur_Pop(ArrayList<Solution> population)
	 * ArrayList<Solution> Selectionner_Mauvaise_Pop(ArrayList<Solution> population)
	 * ArrayList<Solution> CroisementPop (ArrayList<Solution> selection )
	 * ArrayList<Solution> Rassemblement_Pop (ArrayList<Solution> population1, ArrayList<Solution> population2 )
	 * Solution Mutation (Solution individu)
	 * ArrayList<Solution> MutationPop (ArrayList<Solution> population )
	 * Solution Meilleur_Individu (ArrayList<Solution> population)
	 * ArrayList<Solution> Completer_Pop(ArrayList<Solution> population1, ArrayList<Solution> population2)
	 * ArrayList<Solution> Modifier_doublon(ArrayList<Solution> population)
	 * boolean contains(Solution individu, int i, int position)
	 * String toString(Solution individu)
	 */
	
	
	private static Instance m_instance;
	private static int nbIndividu;

	public Genetique (Instance m_instance, int nbIndividu) {
		Genetique.m_instance =m_instance;
		Genetique.nbIndividu=nbIndividu;
	}
	
	/**
	 * On crée une population composée de nbIndividu. On commence par créer l'individu le plus simple en mettant toutes
	 * les villes dans l'ordre de leur indice. On fait ensuite 300 échanges aléatoire entre deux villes côte à côte en partant 
	 * de cette solution simple. On recommence jusqu'a obtenir le nombre d'individu souhaité.
	 * @param instance
	 * @param nbIndividu
	 * @return une population composée de nbIndividu différents
	 * @throws Exception
	 */
	
	public static ArrayList<Solution> CreerPopulation(Instance instance, int nbIndividu ) throws Exception {
		ArrayList<Solution> population = new ArrayList<Solution>(nbIndividu);
		m_instance = instance; 	 	  		  		    		 	
		Solution m_solution = new Solution(m_instance); 	 	  		  		    		 	
		
		//solution la plus simple, toutes les villes dans l'ordre
		for (int i=0; i < m_instance.getNbCities(); i++)   { 	 	  		  		    		 	
			m_solution.setCityPosition(i, i);     	 	  		  		    		 	
			}  	 	  		  		    		 	
		m_solution.setCityPosition(0, m_instance.getNbCities()); 	 	  		  		    		 	
		
		// on va creer nbIndividu differents 
		for(int j=0; j<nbIndividu; j++) { 
			Solution indiv=m_solution.copy();
			for(int m=0; m<300; m++) {  		  		    		 	
				int pos_a_echanger1 = (int)(Math.random()*(m_instance.getNbCities()-1));
				int pos_a_echanger2 = (int)(Math.random()*m_instance.getNbCities()); 	 	  		  		    		 	
				int index_city1 = indiv.getCity(pos_a_echanger1); 	 	  		  		    		 	
				int index_city2 = indiv.getCity(pos_a_echanger1+1);

				if(pos_a_echanger1!=0 && pos_a_echanger2!=0) {
					indiv.setCityPosition(index_city1, pos_a_echanger1+1);  	 	  		  		    		 	
					indiv.setCityPosition(index_city2, pos_a_echanger1);	
				}
			}  	  		  		    		 	
			population.add(j, indiv.copy());
		}
		return population;
	}
	
	
	/**éliminer les moins bons individus et garder uniquement les meilleurs en fonction de leur évaluation 
	 * par rapport au temps de parcours total
	 * étape de selection : on calcule la distance moyenne et on selectionne tous les individus en-dessous de cette moyenne
	 * @param population
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Solution> Selectionner_Meilleur_Pop(ArrayList<Solution> population) throws Exception { 	 	  		  		    		 	
		double moyenne=0;
		double evaluation =0;
		//on calul la distance moyenne de parcours 
		
		for(Solution individu : population) {
			evaluation = individu.evaluate();
			moyenne = moyenne + evaluation;
		}
		int nbIndividu= population.size();
		moyenne = moyenne/nbIndividu;
		
		//on selectionne les individus qui se situe en dessous de cette moyenne
		ArrayList<Solution> selection = new ArrayList<Solution>();
		for(Solution individu : population) {
			if(individu.evaluate()<=moyenne) {
				selection.add(individu);
			}	 
		}
		return selection;
	}
	
	/**garder uniquement les moins bon individus en fonction de leur évaluation 
	 * par rapport au temps de parcours total
	 * étape de selection : on calcule la distance moyenne et on selectionne tous les individus au-dessus de cette moyenne
	 * @param population
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Solution> Selectionner_Mauvaise_Pop(ArrayList<Solution> population) throws Exception { 	 	  		  		    		 	
		double moyenne=0;
		double evaluation =0;
		//on calul la distance moyenne de parcours 
		
		for(Solution individu : population) {
			evaluation = individu.evaluate();
			moyenne = moyenne + evaluation;
		}
		int nbIndividu= population.size();
		moyenne = moyenne/nbIndividu;
		
		//on selectionne les individus qui se situe en dessous de cette moyenne
		ArrayList<Solution> selection = new ArrayList<Solution>();
		for(Solution individu : population) {
			if(individu.evaluate()>moyenne) {
				selection.add(individu);
			}	 
		}
		return selection;
	}
	
	
	
	
	/**
	 * Etape de croisement des individus précédemment sélectionnés pour obtenir une nouvelle population. 
	 * Deux parents sont donc choisis pour appliquer un opérateur 
	 * de croisement afin d’obtenir deux descendants (nouvel individu). technique de « crossover en un point ». 
	 * On recopie une partie du parent 1 et une partie du parent 2 pour obtenir 
	 * un nouvel individu. Le point de séparation des parents est appelé point de croisement (il est choisi arbitrairement ici 50). 
	 * Il faut cependant faire attention à ne pas visiter plusieurs fois la même ville (on ne recopie 
	 * pas les villes déjà visitées), 
	 * et à ne pas oublier de ville (on rajoute à la fin les villes non prises en compte)
	 * @param selection
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Solution> CroisementPop (ArrayList<Solution> selection, Instance m_instance ) throws Exception {
		ArrayList<Solution> pop_croisee = new ArrayList<Solution>(nbIndividu);
		int nbVilles = m_instance.getNbCities();
		Solution nouvel_individu1 = new Solution(m_instance);	 	  		  		    		 	
		Solution nouvel_individu2 = new Solution(m_instance);
		
  	 	for(int i=0; i<selection.size()-1; i=i+2) { 
  	 		Solution parent1 = selection.get(i).copy();
  	 		Solution parent2 = selection.get(i+1).copy();
  	 		nouvel_individu1=parent1.copy();
  	 		nouvel_individu2=parent2.copy();
  	 	
  	 		/// on recopie toutes les villes non visitées par nouvel_individu avant la ville en position 300 de parents2 dans nouvel_individu
	 		int index_position_non_occupee_nouvel_individu1 = (int)(m_instance.getNbCities()/2); 
	 		int index_position_non_occupee_nouvel_individu2 = (int)(m_instance.getNbCities()/2);

	 		//System.out.println("le nouvel undividu issue de parent1 est "+ nouvel_individu. evaluate() + toString(nouvel_individu));
  	 		for(int k=0; k<nbVilles; k++) {
  	 			int indexCityParent2 = parent2.getCity(k);
  	 			int indexCityParent1 = parent1.getCity(k);

  	 			if(contains(nouvel_individu1,indexCityParent2,index_position_non_occupee_nouvel_individu1)==false) {
  	 				nouvel_individu1.setCityPosition( indexCityParent2, index_position_non_occupee_nouvel_individu1);
  	 				index_position_non_occupee_nouvel_individu1++;
  	 			}
  	 			
  	 			if(contains(nouvel_individu2,indexCityParent1,index_position_non_occupee_nouvel_individu2)==false) {
  	 				nouvel_individu2.setCityPosition( indexCityParent1, index_position_non_occupee_nouvel_individu2);
  	 				index_position_non_occupee_nouvel_individu2++;
  	 			}
  	 		}
  	 		nouvel_individu1.setCityPosition(nouvel_individu1.getCity(0), nbVilles); // on doit avoir 1er ville = dernière ville
  	 		nouvel_individu2.setCityPosition(nouvel_individu2.getCity(0), nbVilles); 
  	 		pop_croisee.add(nouvel_individu1); //on ajoute ce descendant à notre solution
  	 		pop_croisee.add(nouvel_individu2);
  	 	}
  	 	return pop_croisee;  
	}
	
	/**
	 * Rassembler deux populations en une seule
	 * @param population1
	 * @param population2
	 * @return une population issu du rasemblement de population1 et population2
	 * @throws Exception
	 */
	
	public static ArrayList<Solution> Rassemblement_Pop (ArrayList<Solution> population1, ArrayList<Solution> population2 ) throws Exception {
		ArrayList<Solution> nouvelle_pop= new ArrayList<>();
		for(Solution i : population1) {
			nouvelle_pop.add(i.copy());
		}
		for(Solution s : population2) {
			nouvelle_pop.add(s.copy());
		}
		return nouvelle_pop;
	}

	
	/**
	 * 
	 * @param individu de type Solution
	 * @return un individu qui a subit un nombre de mutations n aléatoire avec n appartenant à [1;6], qui correspondent à 
	 * des swap de deux villes entre elles
	 * @throws Exception
	 */
	
	public static Solution Mutation (Solution individu, Instance m_instance) throws Exception {
		int nb_mutation = (int)(Math.random()*50)+1;
		for(int i=0; i<nb_mutation; i++) {
			int pos_a_echanger1 = (int)(Math.random()*m_instance.getNbCities()-1);
			int index_city1 = individu.getCity(pos_a_echanger1); 	 	  		  		    		 	
			int index_city2 = individu.getCity(pos_a_echanger1+1);
			individu.setCityPosition(index_city1, pos_a_echanger1+1);  	 	  		  		    		 	
			individu.setCityPosition(index_city2, pos_a_echanger1); 
		}
		return individu;
	}
	
	/**
	 * @param population a la laquelle on va faire intervenir une mutation aléatoire sur la 
	 * moitié de ses individu les moins bons.
	 * @return population dont les individus ont subit des mutations
	 * @throws Exception
	 */
	
	public static ArrayList<Solution> MutationPop (ArrayList<Solution> population, Instance m_instance ) throws Exception {
		ArrayList<Solution> mauvaise_pop= new ArrayList<>();
		ArrayList<Solution> meilleur_pop= new ArrayList<>();
		ArrayList<Solution> mauvaise_pop2= new ArrayList<>();

		mauvaise_pop = Selectionner_Mauvaise_Pop(population);
		meilleur_pop = Selectionner_Meilleur_Pop(population);
		Solution individu_mute;
		for(Solution s : mauvaise_pop) {
			individu_mute=Mutation(s,m_instance);
			mauvaise_pop2.add(individu_mute);
		}
		population = Rassemblement_Pop(mauvaise_pop2, meilleur_pop);
		return population;

	}


	/**
	 * @param une population de type ArrayList
	 * @return le meilleur indidivu de cette population
	 * @throws Exception
	 */
	
	public static Solution Meilleur_Individu (ArrayList<Solution> population) throws Exception {
		Solution meilleur_ind = population.get(0).copy();
		double plus_courte_distance = meilleur_ind.evaluate();
		for(Solution s: population) {
			if(s.evaluate()<plus_courte_distance) {
				meilleur_ind=s;
				plus_courte_distance=meilleur_ind.evaluate();
			}
		}
		return meilleur_ind;
				
	}
	
	/**
	 *  Completer une population1 donnée pour quelle ait la meme taille que le population2 initiale
	 *  Si la population1 est plus petite que la population 2 on la complete avec des individu issue de mutation de population2
	 *  Si la population est plus grande on retire les plus mauvais individu de population1
	 *  Si elle a la même taille on ne fait rien
	 */
	public static ArrayList<Solution> Completer_Pop(ArrayList<Solution> population1, ArrayList<Solution> population2, Instance m_instance) throws Exception{
		int taille_pop2=population2.size();
		int taille_pop1=population1.size();
		int nbIndividu_manquant = taille_pop2-taille_pop1;
		ArrayList<Solution> pop = new ArrayList<Solution>();
		pop = population1;

		//si la population est plus petite que la taille initiale on la complete avec des individus issue de mutation de la pop initiale
		if(nbIndividu_manquant>0) {
			for(int j=0; j<nbIndividu_manquant; j++) {
				Solution indi = Mutation(population2.get(j),m_instance);
				pop.add(indi);
			}
		}
		
		//si elle est plus grande on ne selectionne que les meilleurs et on supprime les moins bons
		if(nbIndividu_manquant<0) {
			int inv = -nbIndividu_manquant;
			for(int k=0; k<inv; k++) {
				Solution moins_bon_ind=pop.get(0);
				double plus_longue_distance = moins_bon_ind.evaluate();
				for(Solution s : pop) {
					if(s.evaluate()>plus_longue_distance) {
						moins_bon_ind=s;
						plus_longue_distance = moins_bon_ind.evaluate();
					}
				}
				pop.remove(moins_bon_ind);
			}
		}
		return pop;
	}
	
	/**
	 * Supprimer tous les doublons d'une population et les remplacer par des individus mutés
	 * @param population
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Solution> Modifier_doublon(ArrayList<Solution> population, Instance m_instance) throws Exception{
		ArrayList<Double> distances = new ArrayList<Double>();
		ArrayList<Solution> nouvel_pop = new ArrayList<Solution>();
		double distance_totale=0;
		for(Solution s : population) {
			distance_totale=s.evaluate();
			if(distances.contains(distance_totale)) {
				Solution indiv_mutation=Mutation(s, m_instance);
				nouvel_pop.add(indiv_mutation);
			}
			else {
				distances.add(s.evaluate());
				nouvel_pop.add(s);
			}
		}
		return nouvel_pop;

	}

	
	
	/**
	 * Si l'individu créé contient déjà la ville d'indice i avant la postion j return true, 
	 * sinon return false
	 * @throws Exception 
	 */
	public static boolean contains(Solution individu, int i, int position) throws Exception {
		boolean res = false;
		for(int j=0; j < position; j++) {
			if(individu.getCity(j)==i) {
				res = true;
			}
		}
		return res;
	}
	
	/**
	 * Permet l'affichage des villes d'un individu
	 * @param individu
	 * @return un String composé de l'ensemble des villes parcourus par l'individu
	 */
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
		
		/*ArrayList<Solution> population = new ArrayList<Solution>();
		ArrayList<Solution> selection = new ArrayList<Solution>(); 
		ArrayList<Solution> pop_croisee = new ArrayList<Solution>(); 
		ArrayList<Solution> pop_rassemblee = new ArrayList<Solution>(); 
		ArrayList<Solution> pop_complete = new ArrayList<Solution>(); 
		ArrayList<Solution> pop_mute = new ArrayList<Solution>(); 
		ArrayList<Solution> pop_sans_doublons = new ArrayList<Solution>(); 

		// génétique
		Instance inst = new Instance("instances/d657.tsp", 0);
		population = CreerPopulation(inst, (long)0.1, 10);
		System.out.println("la population :");
		for(Solution indi : population) {
			System.out.println(indi.evaluate());
		}
		
		
		for(int i=0; i<10; i++) {

			selection = Selectionner_Meilleur_Pop(population);
			System.out.println("la selection des meilleures individus");
			for(Solution indi : selection) {
				System.out.println(indi.evaluate());
			}
			System.out.println("le meilleur indi de la selection est : "+ Meilleur_Individu(selection).evaluate());

		
			pop_croisee = CroisementPop(selection);
			System.out.println("le croisement des meilleurs individus");
			for(Solution indi : pop_croisee) {
				System.out.println(indi.evaluate());
			}
			System.out.println("le meilleur indi du croisement est : "+ Meilleur_Individu(pop_croisee).evaluate());
		
			pop_rassemblee=Rassemblement_Pop(selection, pop_croisee);
			System.out.println("le rassemblement des meilleurs individus et du croisement");
			for(Solution indi : pop_rassemblee) {
				System.out.println(indi.evaluate());
			}
			Solution solution = Meilleur_Individu(pop_rassemblee);
			System.out.println("la meilleur solution de la pop rassemblee");
			System.out.println(solution.evaluate() + toString(solution));
			
			pop_complete = Completer_Pop(pop_rassemblee, population);
			
			System.out.println("la population complete:");
			for(Solution indi : pop_complete) {
				System.out.println(indi.evaluate());
			}
			
			pop_mute=MutationPop(pop_complete);
			System.out.println("la population après mutation:");
			for(Solution indi : pop_mute) {
				System.out.println(indi.evaluate());
			}
			
			pop_sans_doublons=Modifier_doublon(pop_mute);
			population = pop_sans_doublons;
			
			System.out.println("la population après modification des doublons:");
			for(Solution indi : pop_sans_doublons) {
				System.out.println(indi.evaluate());
			}
			System.out.println(" ");


			
			
		}
		Solution solutionfinale = Meilleur_Individu(population);
		System.out.println("la solution finale est l'individu :");
		System.out.println(solutionfinale.evaluate() + toString(solutionfinale));
		*/
		
		//local search
		/*population = Miracle(inst, 1000);
		Solution solution = Meilleur_Individu(population);
		System.out.println(solution.evaluate() + toString(solution));*/
		//Instance inst = new Instance("instances/d657.tsp", 0);
		//System.out.println("la ville d'indice 624 est dans le carre :" + Numero_carre(647, inst) );
		//tableau_ville(inst);
	}
}

	
