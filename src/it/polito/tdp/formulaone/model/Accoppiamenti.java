package it.polito.tdp.formulaone.model;

import javafx.print.PaperSource;

public class Accoppiamenti {
	
	Race r1;
	Race r2;
	double peso;
	
	public Accoppiamenti(Race r1, Race r2, double peso) {
		this.r1 = r1;
		this.r2 = r2;
		this.peso=peso;
	}

	public Race getR1() {
		return r1;
	}

	public Race getR2() {
		return r2;
	}

	public double getPeso() {
		return peso;
	}
	
}
