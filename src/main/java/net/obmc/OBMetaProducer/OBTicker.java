package net.obmc.OBMetaProducer;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class OBTicker {

	Integer tickcount = 0;
	
	String metafile;
	Boolean obsfucate;
	Integer range;

	OBMetaTask task = new OBMetaTask();
	
	 public OBTicker(String mfile, Boolean obsfe, Integer rng) {
		metafile = mfile;
		obsfucate = obsfe;
		range = rng;
	}

	//Called when the server ticks. Usually 20 ticks a second. 
	 @SubscribeEvent
	 public void onServerTick(TickEvent.ServerTickEvent event) {
		 
		 if (event.phase == TickEvent.Phase.END) {
			tickcount++;
		 	if (tickcount == 100 ) {
		 		//FMLLog.log(Level.INFO,"[OBMetaProducer] Writing meta data");
		    
		 		task.run(metafile, obsfucate, range);
		    
		 		tickcount = 0;
		 	}
	 	}
	 }

}