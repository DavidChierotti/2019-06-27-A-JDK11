package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private SimpleWeightedGraph<String,DefaultWeightedEdge> grafo;
	private List<String> migliore;
	private double pesoTot;
	
	public Model() {
		this.dao=new EventsDao();
	}
	
	public void creaGrafo(int anno,String categoria) {
		this.grafo=new SimpleWeightedGraph<String,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo,dao.vertici(anno, categoria));
		
		for(Adiacenza a:dao.getArchi(anno, categoria)) {
			Graphs.addEdge(grafo,a.getT1(),a.getT2(),a.getPeso());
		}
		
		
	}
	
	public List<String> cercaCammino(Adiacenza a){
		
	 this.migliore=new ArrayList<>();
	 List<String> parziale=new ArrayList<>();
	 this.pesoTot=0;
	 double min=100000000;
	 String p=a.getT1();
	 String arr=a.getT2();
	 parziale.add(p);
	 this.cerca(parziale,arr,min);
	 
	 pesoTot=this.calcolaPeso(migliore);
	 
	 return migliore;
		
		
	}
	
	

	private void cerca(List<String> parziale,String arr,double min) {
		if(parziale.size()==grafo.vertexSet().size()) {
			if(this.calcolaPeso(parziale)<min) {
				migliore=new ArrayList<>(parziale);
				min=this.calcolaPeso(migliore);
			}
		}
		else {
			for(String s:this.possibili(parziale, arr)) {
				parziale.add(s);
				this.cerca(parziale, arr,min);
				parziale.remove(parziale.size()-1);
			}
		}
		
		
	}
	
	
	private double calcolaPeso(List<String> parziale) {
		double peso=0;
		if(parziale.size()>1) {
		for(int i=1;i<parziale.size()-1;i++) {
			DefaultWeightedEdge e=grafo.getEdge(parziale.get(i-1), parziale.get(i));
			peso+=grafo.getEdgeWeight(e);
		}
		}
		return peso;
	}
	
	private List<String> possibili(List<String> parziale,String arr){
		List<String> possibili=new ArrayList<>();
		String p=parziale.get(parziale.size()-1);
		if(parziale.size()<grafo.vertexSet().size()-1) {
		for(String s:Graphs.neighborListOf(grafo,p)) {
			
			if(!parziale.contains(s)&&!s.equals(arr))
				possibili.add(s);
		     }
		}
			else
				possibili.add(arr);
		return possibili;
	}

	

	public List<Adiacenza> massimi(){
		double max=0;
		List<Adiacenza> massimi=new ArrayList<>();
		for(DefaultWeightedEdge e:grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)>max)
				max=grafo.getEdgeWeight(e);
		}
		for(DefaultWeightedEdge e:grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)==max) {
				Adiacenza a=new Adiacenza(grafo.getEdgeSource(e),grafo.getEdgeTarget(e),grafo.getEdgeWeight(e));
				massimi.add(a);
			}	
		}
		return massimi;
	}
	
	
	
	public int nVert() {
		return grafo.vertexSet().size();
	}
	public int nArch() {
		return grafo.edgeSet().size();
	}

	public double getPesoTot() {
		return pesoTot;
	}
	
}
