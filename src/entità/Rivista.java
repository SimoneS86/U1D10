package entità;

import java.util.UUID;

public class Rivista extends OggettiLeggibili {
	public enum Periodicita {
		SETTIMANALE, MENSILE, SEMESTRALE
	}

	private Periodicita periodicita;

	public Rivista(UUID isbn, String titolo, int annoPubblicazione, int numeroPagine, Periodicita periodicita) {
		super(isbn, titolo, annoPubblicazione, numeroPagine);
		this.periodicita = periodicita;
	}

	public Periodicita getPeriodicita() {
		return periodicita;
	}

	@Override
	public String toString() {
		return super.toString() + " - Periodicità: " + periodicita;
	}

}