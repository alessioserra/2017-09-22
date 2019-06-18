package it.polito.tdp.formulaone.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.omg.CORBA.PUBLIC_MEMBER;

import com.mchange.v2.lang.ThreadGroupUtils;
import com.sun.prism.shader.AlphaOne_Color_AlphaTest_Loader;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {
	
	private List<Season> stagioni;
	private FormulaOneDAO dao;
	private Graph<Race, DefaultWeightedEdge> grafo;
	
	public List<Season> getStagioni(){
		return this.stagioni;
	}
	
	public Model(){	
		dao = new FormulaOneDAO();
		stagioni = dao.getAllSeasons();	
	}
	
	public void creaGrafo(Season year) {
		
		//Creo grafo
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Aggiungo vertici
		List<Race> corse = new ArrayList<>();
		corse = dao.getRaces(year);
		Graphs.addAllVertices(this.grafo, corse);
		
		//Agginungo gli archi (Metodo doppio for)
		for (Race r1 : corse) {
			for (Race r2 : corse) {
				
				if (!r1.equals(r2) && !this.grafo.containsEdge(r1, r2)) {
					int peso1 = dao.getWight(r1.getRaceId());
					int peso2 = dao.getWight(r2.getRaceId());
					double peso = peso1+peso2;
					Graphs.addEdge(this.grafo, r1, r2, peso);	
				}
			}
		}
		System.out.println("GRAFO CREATO");
		System.out.println("#NODI: "+this.grafo.vertexSet().size());
		System.out.println("#ARCHI: "+this.grafo.edgeSet().size()+"\n");
	}
	
	public List<Accoppiamenti> getBest(){
		
		List<Accoppiamenti> list = new ArrayList<>();
		
		List<DefaultWeightedEdge> edgeList = new ArrayList<>();
		double best = 0.0;
		
		for (DefaultWeightedEdge eee : this.grafo.edgeSet()) {
			
			//Se lo supera:
			if (this.grafo.getEdgeWeight(eee)>best) {
		        //Aggiorno nuovo best
				best = this.grafo.getEdgeWeight(eee);
				//Pulisco la lista
				list.clear();
				//Aggiungo il nuovo elemento
				list.add( new Accoppiamenti(this.grafo.getEdgeSource(eee) , this.grafo.getEdgeTarget(eee), best));			
			}
			//Se lo eguaglia:
			if (this.grafo.getEdgeWeight(eee)==best) {
				//Lo aggiungo a quello esistente
				list.add( new Accoppiamenti(this.grafo.getEdgeSource(eee) , this.grafo.getEdgeTarget(eee), best));
			}
		}
		
		//Alla fine restituisco il risultato
		return list;
	}


}
