package rusketh.com.github.spigot.parts;

import org.bukkit.entity.Player;

import rusketh.com.github.spigot.Rusketh;

public class Econemy extends Part {
	
	public double startMoney;
	public String unitName;
	public String unitPlural;
	public String subUnitName;
	public String subUnitPlural;
	public String unitSymbol;
	
	public Econemy(Rusketh plugin) {
		super(plugin);
		startMoney = plugin.getConfig().getDouble("Econemy.Default_Money", 0);
		unitName = plugin.getConfig().getString("Econemy.Unit.Name", "Pound");
		unitPlural = plugin.getConfig().getString("Econemy.Unit.Ploral", "Pounds");
		subUnitName = plugin.getConfig().getString("Econemy.SubUnit.Name", "Penny");
		subUnitPlural = plugin.getConfig().getString("Econemy.SubUnit.Plural", "Pence");
		unitSymbol = plugin.getConfig().getString("Econemy.Unit.Symbol", "£");
	}
	
	public void setMoney(String ply, double money) {
		if (money < 0) money = 0;
		Rusketh.plyMgr.getPlayerData(ply).set("Econemy.Money", money);
		Rusketh.plyMgr.savePlayerData(ply);
	}

	public double getMoney(String ply) {
		return Rusketh.plyMgr.getPlayerData(ply).getDouble("Econemy.Money", startMoney);
	}
	
	public double addMoney(String ply, double amount) {
		double money = getMoney(ply) + amount;
		setMoney(ply, money);
		return money;
	}
	
	public boolean hasMoney(String ply, double amount) {
		return getMoney(ply) <= amount;
	}
	
	public boolean useMoney(String ply, double amount) {
		if (amount <= 0) return false;
		if (getMoney(ply) > amount) return false;
		addMoney(ply, -amount);
		return true;
	}
	
	public boolean transferMoney(String ply, String targ, double amount) {
		if (!useMoney(ply, amount)) return false;
		addMoney(targ, amount);
		return true;
	}
	
	public void setBankBalance(String ply, double money) {
		if (money < 0) money = 0;
		Rusketh.plyMgr.getPlayerData(ply).set("Econemy.Bank.Balance", money);
		Rusketh.plyMgr.savePlayerData(ply);
	}

	public double getBankBalance(String ply) {
		return Rusketh.plyMgr.getPlayerData(ply).getDouble("Econemy.Bank.Balance", 0);
	}
	
	public double addBankBalance(String ply, double amount) {
		double money = getBankBalance(ply) + amount;
		setBankBalance(ply, money);
		return money;
	}
	
	public boolean hasBankBalance(String ply, double amount) {
		return getBankBalance(ply) <= amount;
	}
	
	public boolean useBankBalance(String ply, double amount) {
		if (amount <= 0) return false;
		if (getBankBalance(ply) > amount) return false;
		addBankBalance(ply, -amount);
		return true;
	}
	
	public boolean transferBankBalance(String ply, String targ, double amount) {
		if (!useBankBalance(ply, amount)) return false;
		addBankBalance(targ, amount);
		return true;
	}
	
	public boolean withdrawBankBalance(String ply, double amount) {
		if (!useBankBalance(ply, amount)) return false;
		addMoney(ply, amount);
		return true;
	}
	
	public boolean depositBankBalance(String ply, double amount) {
		if (!useMoney(ply, amount)) return false;
		addBankBalance(ply, amount);
		return true;
	}
	
	public void setMoney(Player ply, double money) { setMoney(ply.getName(), money); }
	public double getMoney(Player ply) { return getMoney(ply.getName()); }
	public double addMoney(Player ply, double amount) { return addMoney(ply.getName(), amount); }
	public boolean hasMoney(Player ply, double amount) { return hasMoney(ply.getName(), amount); }
	public boolean useMoney(Player ply, double amount) { return useMoney(ply.getName(), amount); }
	public boolean transferMoney(Player ply, String targ, double amount) { return transferMoney(ply.getName(), targ, amount); }
	public boolean transferMoney(String ply, Player targ, double amount) { return transferMoney(ply, targ.getName(), amount); }
	public boolean transferMoney(Player ply, Player targ, double amount) { return transferMoney(ply.getName(), targ.getName(), amount); }
	public void setBankBalance(Player ply, double money) { setBankBalance(ply.getName(), money); }
	public double getBankBalance(Player ply) { return getBankBalance(ply.getName()); }
	public double addBankBalance(Player ply, double amount) { return addBankBalance(ply.getName(), amount); }
	public boolean hasBankBalance(Player ply, double amount) { return hasBankBalance(ply.getName(), amount); }
	public boolean useBankBalance(Player ply, double amount) { return useBankBalance(ply.getName(), amount); }
	public boolean transferBankBalance(Player ply, String targ, double amount) { return transferBankBalance(ply.getName(), targ, amount); }
	public boolean transferBankBalance(String ply, Player targ, double amount) { return transferBankBalance(ply, targ.getName(), amount); }
	public boolean transferBankBalance(Player ply, Player targ, double amount) { return transferBankBalance(ply.getName(), targ.getName(), amount); }
	public boolean withdrawBankBalance(Player ply, double amount) { return withdrawBankBalance(ply.getName(), amount); }
	public boolean depositBankBalance(Player ply, double amount) { return depositBankBalance(ply.getName(), amount); }
	
	
}
