package classi;

public class Amministratore 
{
	//credenziali assegnate da noi programmatori di default (USERNAME: "username" e PASSWORD: "password")
	private String username = "username";
	private String password = "password";
	
	//proprietà che restituiscono l'username e la password una volta chiamate
	public String getUserName() 
	{
		return this.username;
	}
	
	public String getPassword() 
	{
		return this.password;
	}
}