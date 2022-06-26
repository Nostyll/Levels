package me.nostyll.Kingdoms.levels.handler;

import java.util.HashMap;
import java.util.UUID;

public class LoadKingdomData {

    private HashMap<UUID, LoadKingdoms> LoadKingdomsData = new HashMap<UUID, LoadKingdoms>();
    
    public LoadKingdoms getKingdomData(UUID kingdomuuid) {
	    return getAllKingdomdata().get(kingdomuuid);
	}
 
	public void createKingdomData(UUID kingdomuuid) {
		if (!this.hasKingdomData(kingdomuuid)){
			getAllKingdomdata().put(kingdomuuid, new LoadKingdoms(kingdomuuid));	
		}
	}

	public boolean hasKingdomData(UUID kingdomuuid) {
		 return getAllKingdomdata().containsKey(kingdomuuid);
	}
	
	public void removeKingdomData(UUID kingdomuuid){
		getAllKingdomdata().entrySet().removeIf(LoadKingdomsData -> LoadKingdomsData.getKey().equals(kingdomuuid));
	}
	
	public HashMap<UUID, LoadKingdoms> getAllKingdomdata(){
		return LoadKingdomsData;
	}

	public static LoadKingdomData getLoadKingdomData() {
		return new LoadKingdomData();
	}
    
}
