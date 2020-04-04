package net.obmc.OBMetaProducer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class OBMetaTask {

    private final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
	private PrintWriter pw = null;
	private String playerWorld = "overworld";
	
	public void run(String metafile, Boolean obsfucate, Integer range) {
		
		// open the meta file for writing
		File mfile = new File(metafile);
		FileWriter writer;
		try {
			writer = new FileWriter(mfile, false);
			pw = new PrintWriter(writer);
		} catch(IOException e) {
			e.printStackTrace();
		}
    	
		//List<EntityPlayerMP> PlayersOnline = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		//for(int i = 0; i < PlayersOnline.size(); i++)
		PlayerList scm = server.getPlayerList();
		if (scm.getPlayerList().size() != 0) {
			pw.print("["); int i=0;
			for (EntityPlayerMP player : scm.getPlayerList()) {
				i++;

				//get player location
				int x = (int) player.posX; int y = (int) player.posY; int z = (int) player.posZ;

				// add any required player position obsfucation
				if (obsfucate) {
					Random rand = new Random();
					int xob = rand.nextInt(range); if (xob%2 == 0) {x+=xob;}else{x-=xob;} 
					int zob = rand.nextInt(range); if (zob%2 == 0) {z+=zob;}else{z-=zob;}
					int yob = rand.nextInt(40); if(yob%2 == 0) {y+=yob;}else{y-=yob;} if (y > 256) y=255; if (y<0) y=0; 
				}

				// cater for forge returning world and dimension as the same
				playerWorld = player.worldObj.getWorldInfo().getWorldName();
				if (player.worldObj.getWorldInfo().getWorldName().equals("DIM"+player.dimension)) {
					playerWorld = "overworld";
				}
				//pw.println(player.getDisplayName()+ ":" + player.worldObj.getWorldInfo().getWorldName() + ":" + player.dimension + ":" + player.posX + ":" + player.posY + ":" + player.posZ);
				pw.print(	"{\"msg\":\""+player.getDisplayNameString()+"\","+
						"\"world\":\""+playerWorld+"\","+
						"\"dimension\":\""+player.dimension+"\","+
						"\"x\":\""+x+"\","+
						"\"y\":\""+y+"\","+
						"\"z\":\""+z+"\"}");
				if (i < scm.getPlayerList().size()-1 ) { pw.print(",");}				
				pw.print("]");
			}
		} else {
			pw.print("[]");
		}
	
   		pw.close();

	}
}