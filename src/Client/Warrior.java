package Client;


public class Warrior extends Charater {

	protected final int BaseCritalPercent = 5;
	protected int CritalPercent=0;
	protected static final String JOBNAME = "Warrior";
	
	public Warrior(){
		super();
		this.Job = JOBNAME;
		this.CritalPercent = BaseCritalPercent +this.Level;
	}
	
	private boolean checkCrital(int critalpercent){
		Double check = Math.random()*100+1;
		
		if(critalpercent > check){
			return true;
		}else{
			return false;
		}
		
		
	}
	
	@Override
	protected int attack(int damage){		
		if(checkCrital(CritalPercent)){
			
			return damage*2;
		}else{
			
			return damage;
		}
		
	}
		
	
}
