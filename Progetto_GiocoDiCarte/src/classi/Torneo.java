package classi;

public class Torneo extends Gara
{	
	public Torneo(String alias1, String alias2, String alias3, String alias4, String codice) 
	{
		String[] alias = {alias1, alias2, alias3, alias4};
		aggiungiGiocatori(alias, codice);
	}
	
	public Torneo(String alias1, String alias2, String alias3, String alias4, String alias5, String alias6, String alias7, String alias8, String codice) {
		String[] alias = {alias1, alias2, alias3, alias4, alias5, alias6, alias7, alias8};
		aggiungiGiocatori(alias, codice);
	}
}
