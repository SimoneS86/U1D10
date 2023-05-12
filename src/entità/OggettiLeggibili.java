package entità;

import java.util.UUID;

public abstract class OggettiLeggibili {
	protected UUID isbn;
	private String titolo;
	private int annoPubblicazione;
	private int numeroPagine;

	public OggettiLeggibili(UUID isbn, String titolo, int annoPubblicazione, int numeroPagine) {
		this.titolo = titolo;
		this.annoPubblicazione = annoPubblicazione;
		this.numeroPagine = numeroPagine;
		this.isbn = isbn;
	}

	public String getTitolo() {
		return titolo;
	}

	public int getAnnoPubblicazione() {
		return annoPubblicazione;
	}

	public int getNumeroPagine() {
		return numeroPagine;
	}

	public UUID getIsbn() {
		return isbn;
	}

	@Override
	public String toString() {
		return "ISBN: " + isbn + " - Titolo: " + titolo + " - Anno Pubblicazione: " + annoPubblicazione
				+ " - Numero Pagine: " + numeroPagine;
	}

}
