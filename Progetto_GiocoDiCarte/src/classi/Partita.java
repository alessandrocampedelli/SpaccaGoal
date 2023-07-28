package classi;

public class Partita extends Gara{
	
	public Partita(String alias1, String alias2, String codice) {
		String[] alias = {alias1, alias2};
		aggiungiGiocatori(alias, codice);
	}
	public Partita(String alias1, String alias2, String alias3, String codice) {
		String[] alias = {alias1, alias2, alias3};
		aggiungiGiocatori(alias, codice);
	}
	public Partita(String alias1, String alias2,String alias3, String alias4, String codice) {
		String[] alias = {alias1, alias2, alias3, alias4};
		aggiungiGiocatori(alias, codice);
	}
	public Partita(String alias1, String alias2,String alias3, String alias4, String alias5, String codice) {
		String[] alias = {alias1, alias2, alias3, alias4, alias5};
		aggiungiGiocatori(alias, codice);
	}
}
