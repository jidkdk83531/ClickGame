package Client;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Charater implements Serializable {

	protected int Level=0;
	protected int Damage=0;
	protected String Job =null;
	protected int Experience;
	
	public Charater(){
		this.Level=1;
		this.Damage=1;
		this.Experience=1;
	}	
	
	
	protected int attack(int damage){
		this.Experience+=damage;
		return damage;
	}
	
	
	
	
	protected void CheckLeveUp(int level, int experience){
		if(level <11){
			if(level *10 < experience){
				this.Experience = 0;
				this.Damage +=level;
				this.Level += 1;
			}
		}else if (level < 21){
			if(level *20 >= experience){
				this.Experience = 0;
				this.Damage +=level;
				this.Level += 1;
			}
		}else if (level < 31){
			if(level *30 >= experience){
				this.Experience = 0;
				this.Damage +=level;
				this.Level += 1;
			}
		}else if (level < 41){
			if(level *40 >= experience){
				this.Experience = 0;
				this.Damage +=level;
				this.Level += 1;
			}
		}else if (level < 51){
			if(level *50 >= experience){
				this.Experience = 0;
				this.Damage +=level;
				this.Level += 1;
			}
		}			
	}
	
	
	
	
	
	
	//getter setter
	public int getLevel() {
		return Level;
	}

	public void setLevel(int level) {
		this.Level = level;
	}

	public int getDamage() {
		return Damage;
	}

	public void setDamage(int damage) {
		this.Damage = damage;
	}

	public String getJob() {
		return Job;
	}

	public void setJob(String job) {
		this.Job = job;
	}
	
	public int getExperience() {
		return Experience;
	}

	public void setExperience(int experience) {
		this.Experience = experience;
	}


	@Override
	public String toString() {
		return "Level=" + Level + " , Damage=" + Damage + " , Job=" + Job + ", Experience=" +Experience;
	}
	
	
	
}
	
