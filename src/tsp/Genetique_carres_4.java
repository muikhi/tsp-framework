package tsp;
import java.util.ArrayList;
	
	/**Dans cette classe genetique_carré_4 on découpe géographique en carrés l'espace où se situent les villes. Toutes les villes 
	 * appartiennent donc à un carré de 1 à 4 
	 * On ne fait des permuations de villes que entre deux villes d'un même carré pour limiter les grandes distances en passant d'une 
	 * ville éloignée à une autre.
	 * Deux configurations de l'espace sont testés par la suite (voir la classe Genetique_carres_16) et il apparait que le découpage en trop petits carré (16) n'est pas optimal.
	 * La pluspart des fonctions sont donc en double en fonction de si on découpe l'espace en 4 ou en 16 carrés. 
	 * Certaines fonctions sont communes.
	 * 
	 * Les fonctions spécifiques au découpage en 4 :
	 * int Numero_carre_4 (int index_ville, Instance instance)
	 * int[] tableau_ville_4(Instance instance)
	 * ArrayList<Solution> CreerPopulation_Carre_4
	 * int get_point_de_croisement_4 (Instance instance)
	 * Solution Mutation_4 (Solution individu) 
	 * ArrayList<Solution> Completer_Pop_4(ArrayList<Solution> population1, ArrayList<Solution> population2)
	 * ArrayList<Solution> Modifier_doublon_4(ArrayList<Solution> population)
	
	 * 
	 * Les fonctions communes aux deux découpages :
	 * ArrayList<Solution> Selectionner_Meilleur_Pop(ArrayList<Solution> population)
	 * ArrayList<Solution> Selectionner_Mauvaise_Pop(ArrayList<Solution> population)
	 * ArrayList<Solution> CroisementPop (ArrayList<Solution> selection, int point_de_croisement )
	 * ArrayList<Solution> Rassemblement_Pop (ArrayList<Solution> population1, ArrayList<Solution> population2 )
	 * Solution Meilleur_Individu (ArrayList<Solution> population)
	 * boolean contains(Solution individu, int i, int position)
	 * String toString(Solution individu)
	 * 
	 * @author Séverine De Christen
	 *
	 */

	public class Genetique_carres_4 {

		private static Instance m_instance;
		private static int nbIndividu;

		public Genetique_carres_4 (Instance m_instance, int nbIndividu) {
			this.m_instance =m_instance;
			this.nbIndividu=nbIndividu;
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
		 * en respectant l'ordre 1,2,3,4
		 * @param instance de type Instance
		 * @param nbIndividu de la population de type int
		 * @return une population composée d'individu 
		 * @throws Exception
		 */

		
		public static ArrayList<Solution> CreerPopulation_Carre_4(Instance instance, int nbIndividu ) throws Exception {
			ArrayList<Solution> population = new ArrayList<Solution>(nbIndividu);
			m_instance = instance; 	 	  		  		    		 	
			Solution m_solution = new Solution(m_instance); 	 	  		  		    		 	
			int[] tableau_ville = tableau_ville_4(m_instance);
			//On crée individu de départ qui respecte bien la classification par carré
			m_solution.setCityPosition(0,0);     	 	  		  		    		 	
			int position =1;
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
		
			// on va creer nbIndividu differents 
			for(int j=0; j<nbIndividu; j++) { 
				Solution indiv=m_solution.copy();
				for(int m=0; m<300; m++) {
				 	 	  		  		    		 	
					int pos_a_echanger1 = (int)(Math.random()*(m_instance.getNbCities()-1));
					int pos_a_echanger2 = (int)(Math.random()*m_instance.getNbCities()); 	 	  		  		    		 	
					int index_city1 = indiv.getCity(pos_a_echanger1); 	 	  		  		    		 	
					int index_city2 = indiv.getCity(pos_a_echanger1+1);
					//double distance_optim = m_solution.evaluate();

					if(pos_a_echanger1!=0 && pos_a_echanger2!=0 && Numero_carre_4(index_city1, instance)==Numero_carre_4(index_city2, instance)) {
						indiv.setCityPosition(index_city1, pos_a_echanger1+1);  	 	  		  		    		 	
						indiv.setCityPosition(index_city2, pos_a_echanger1);
						
						//on veut faire des changement uniquement si ce changement améliore la solution initiale
						/**if (indiv.evaluate() >= distance_optim) { //l'ancienne solution est meilleure ou égale, on y revient	 	  		  		    		 	
							indiv.setCityPosition(index_city1, pos_a_echanger1+1);  	 	  		  		    		 	
							indiv.setCityPosition(index_city2, pos_a_echanger1); 	 	 	  		  		    		 	
						} 
							else { 	//la nouvelle solution est meilleure, on la conserve 
								distance_optim=indiv.evaluate(); 	 	  		  		    		 	
								int index_ville_depart = indiv.getCity(0); 	 	  		  		    		 	
								indiv.setCityPosition(index_ville_depart, m_instance.getNbCities());
								 	  		  		    		 	
							} 
						*/	 	  		  		    		 		 	  		  		    		  	 	  		  		    		 	 	  		  		    		 	  		
					} 
						  		  		    		 		 	  		  		    		  	 	  		  		    		 	 	  		  		    		 	
				}  	  		  		    		 	
				//on ajoute notre indidu à la population
				population.add(j, indiv.copy());
			}
			return population;
		}
		
		
		/**Eliminer les moins bons individus et garder uniquement les meilleurs en fonction de leur évaluation 
		 * par rapport au temps de parcours total
		 * étape de selection : on calcule la distance moyenne et on selectionne tous les individus au-dessus de cette moyenne
		 * @param prends en paramètre une population composé d'individus de type Solution
		 * @return une population composé de tous les individus au dessus de la moyenne des individu de la population donnée initialement
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
			//System.out.println("la distance moyenne est :" + moyenne);
			
			//on selectionne les individus qui se situe en dessous de cette moyenne
			ArrayList<Solution> selection = new ArrayList<Solution>();
			//System.out.println("il y a:" + population.size() + " individus dans la population");
			for(Solution individu : population) {
				if(individu.evaluate()<=moyenne) {
					selection.add(individu);
					//System.out.println("on selection cet individu : "+toString(individu));
				}	 
			}
		
			return selection;
		}
		/**Eliminer les meilleurs individus et garder uniquement les moins bon en fonction de leur évaluation 
		 * par rapport au temps de parcours total
		 * étape de selection : on calcule la distance moyenne et on selectionne tous les individus en-dessous de cette moyenne
		 * @param prends en paramètre une population composé d'individus de type Solution
		 * @return une population composé de tous les individus en dessous de la moyenne des individu de la population donnée initialement
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
	
		
		/**trouver un point de séparation optimal, on souhaite que le point de séparation soit situé à la fin 
		 * d'un carré donc on compte le nombre de villes qu'il y a dans les 2 premiers carrés si découpage géographique en 4
		 * @param instance
		 * @return retourne un point de séparation 
		 * @throws Exception
		 */
		
		public static int get_point_de_croisement_4 (Instance instance) throws Exception{
			int [] tableau = tableau_ville_4(instance);
			int compteur = 0;
			for(int i=0; i<instance.getNbCities(); i++) {
				if(tableau[i]==1 || tableau[i]==2) {
					compteur++;
				}
			}
			return compteur;
		}
		
		/**
		 * Etape de croisement des individus précédemment sélectionnés pour obtenir une nouvelle population. 
		 * Deux parents sont donc choisis pour appliquer un opérateur 
		 * de croisement afin d’obtenir deux descendants (nouvel individu). technique de « crossover en un point ». 
		 * On recopie une partie du parent 1 et une partie du parent 2 pour obtenir 
		 * un nouvel individu. Le point de séparation des parents est appelé point de croisement. 
		 * Il faut cependant faire attention à ne pas visiter plusieurs fois la même ville (on ne recopie 
		 * pas les villes déjà visitées), 
		 * et à ne pas oublier de ville (on rajoute à la fin les villes non prises en compte)
		 * @param population nommé selection car elle est composée des meilleurs individus selectionnés precedement
		 * @return une population composées des enfants issues d'un mélange génétique
		 * @throws Exception
		 */
		public static ArrayList<Solution> CroisementPop (ArrayList<Solution> selection, int point_de_croisement ) throws Exception {
			ArrayList<Solution> pop_croisee = new ArrayList<Solution>(nbIndividu);
			int nbVilles = m_instance.getNbCities();
			Solution nouvel_individu1 = new Solution(m_instance);	 	  		  		    		 	
			Solution nouvel_individu2 = new Solution(m_instance);	 	  		  		    		 	

	  	 	for(int i=0; i<selection.size()-1; i=i+2) { 
	  	 		Solution parent1 = selection.get(i).copy();
	  	 		Solution parent2 = selection.get(i+1).copy();
	  	 		
	  	 		
	  	 		nouvel_individu1=parent1.copy();
	  	 		nouvel_individu2=parent2.copy();
	  
		 		int index_position_non_occupee_nouvel_individu1 = point_de_croisement;
		 		int index_position_non_occupee_nouvel_individu2 = point_de_croisement;
	  	 		
		 		/// on recopie toutes les villes non visitées par nouvel_individu avant la ville en position "point_de_croisement" de parents2 dans nouvel_individu
	  	 		for(int k=0; k<nbVilles; k++) {
	  	 			int indexCityParent2 = parent2.getCity(k);
	  	 			int indexCityParent1 = parent1.getCity(k);

	  	 			if(contains(nouvel_individu1,indexCityParent2,index_position_non_occupee_nouvel_individu1)==false) {
	  	 				nouvel_individu1.setCityPosition( indexCityParent2, index_position_non_occupee_nouvel_individu1);
	  	 				index_position_non_occupee_nouvel_individu1++;
	  	 				//on incremente à chaque fois la position non occupee
	  	 				
	  	 			}
	  	 			
	  	 			if(contains(nouvel_individu2,indexCityParent1,index_position_non_occupee_nouvel_individu2)==false) {
	  	 				nouvel_individu2.setCityPosition( indexCityParent1, index_position_non_occupee_nouvel_individu2);
	  	 				index_position_non_occupee_nouvel_individu2++;
	  	 			}
		 				
	  	 		}
	  	 		nouvel_individu1.setCityPosition(nouvel_individu1.getCity(0), nbVilles); // on doit avoir 1er ville = dernière ville
	  	 		nouvel_individu2.setCityPosition(nouvel_individu2.getCity(0), nbVilles); 

	  	 		//on ajoute ce descendant à notre solution
	  	 		pop_croisee.add(nouvel_individu1); 
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
		 * Introduire une mutation aléatoire qui correspond à un échange entre deux villes cote à cote uniquement si elles 
		 * font partie du même carré, il faut différencier carré à 4 et à 16
		 * @param individu de type Solution
		 * @return un individu qui a subit un nombre de mutations n aléatoire avec n appartenant à [1;6], qui correspondent 
		 * à des swap de deux villes entre elles
		 * @throws Exception
		 */
		
		public static Solution Mutation_4 (Solution individu) throws Exception {
			int nb_mutation = (int)(Math.random()*50)+1;
			for(int i=0; i<nb_mutation; i++) {
				int pos_a_echanger1 = (int)(Math.random()*m_instance.getNbCities()-1);
				int index_city1 = individu.getCity(pos_a_echanger1); 	 	  		  		    		 	
				int index_city2 = individu.getCity(pos_a_echanger1+1);
				if(Numero_carre_4(index_city1, m_instance)==Numero_carre_4(index_city2, m_instance)) {
					individu.setCityPosition(index_city1, pos_a_echanger1+1);  	 	  		  		    		 	
					individu.setCityPosition(index_city2, pos_a_echanger1); 
				}
			}
			return individu;
		}
		
		
		
		/**
		 * @param population a la laquelle on va faire intervenir une mutation aléatoire sur la moitié de ses individu les meilleurs.
		 * on sépare en deux la population 
		 * on distingue carre a 4 et 16
		 * @return une population dont les individus ont subit des mutations plus les individus les meilleurs de la population d'origine
		 * @throws Exception
		 */
		
		public static ArrayList<Solution> MutationPop_4 (ArrayList<Solution> population ) throws Exception {
			ArrayList<Solution> meilleur_pop= new ArrayList<>();
			ArrayList<Solution> meilleur_pop2= new ArrayList<>();
			ArrayList<Solution> mauvaise_pop= new ArrayList<>();


			meilleur_pop = Selectionner_Meilleur_Pop(population);
			mauvaise_pop = Selectionner_Mauvaise_Pop(population);
			Solution individu_mute;
			for(Solution s : mauvaise_pop) {
				individu_mute=Mutation_4(s);
				meilleur_pop2.add(individu_mute);
			}
			population = Rassemblement_Pop(meilleur_pop2, meilleur_pop);
			return population;
		}
		

		/**
		 * @param une population de type ArrayList
		 * @return le meilleur indidivu de cette population selon le critère de distance maximale parcourue
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
		 *  on Sépare cas de carré 4 ou 16
		 *  Si elle a la même taille on ne fait rien
		 * @param population1 de taille : taille_pop1
		 * @param population2 de taille : taille_pop2
		 * @return une population de taille : taille_pop2
		 * @throws Exception
		 */
		
		public static ArrayList<Solution> Completer_Pop_4(ArrayList<Solution> population1, ArrayList<Solution> population2) throws Exception{
			int taille_pop2=population2.size();
			int taille_pop1=population1.size();
			int nbIndividu_manquant = taille_pop2-taille_pop1;
			ArrayList<Solution> pop = new ArrayList<Solution>();
			pop = population1;

			//si la population est plus petite que la taille initiale on la complete avec des individus issue de mutation de la pop initiale
			if(nbIndividu_manquant>0) {
				for(int j=0; j<nbIndividu_manquant; j++) {
					Solution indi = Mutation_4(population2.get(j));
					pop.add(indi);
				}
			}
			
			//si elle est plus grande on ne selectionne que les meilleurs, i.e. on supprime les moins bons individus
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
		 * Supprimer les doublons au sein d'une population et les remplacer par des individus mutés
		 * @param population composée d'individu de type Solution
		 * @return une population sans doublon parmis les individu et de même taille qu'initialement
		 * @throws Exception
		 */
		
		public static ArrayList<Solution> Modifier_doublon_4(ArrayList<Solution> population) throws Exception{
			ArrayList<Double> distances = new ArrayList<Double>();
			ArrayList<Solution> nouvel_pop = new ArrayList<Solution>();
			double distance_totale=0;
			for(Solution s : population) {
				distance_totale=s.evaluate();
				if(distances.contains(distance_totale)) {
					Solution indiv_mutation=Mutation_4(s);
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
		 * 
		 * @param individu
		 * @return la liste des villes parcourues par un individu. Par exemple si il visite la ville 3 puis 4 puis 7 on a "(-3-4-7- ...)"
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

}
