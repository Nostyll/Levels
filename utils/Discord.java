package me.nostyll.Kingdoms.levels.utils;

import java.awt.Color;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.kingdoms.constants.group.Kingdom;

import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;


public class Discord {
	
	public static MessageEmbed discordLevelupEmbed(Kingdom kingdom){
		// Create the EmbedBuilder instance
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Kingdom Levelup", null);
		eb.setColor(new Color(163, 114, 28));
		
		
		return eb.build();
		
	}

	public static EmbedBuilder BuildLevelMessage(){
    	EmbedBuilder eb = new EmbedBuilder();
	    eb.setTitle("Level Up!", null);
	    eb.setColor(new Color(43, 212, 17));
	    eb.setDescription("Your Kingdom is now level up");
	    eb.addField("Title of field", "test of field", false);
	    eb.addBlankField(false);
	    eb.setAuthor("Level Up Bot", null, "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
	    eb.setFooter("Text", "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
	    eb.setThumbnail("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");
		return eb;
    }
	
	public static MessageEmbed discordVoiceChannelEmbed(){
		// Create the EmbedBuilder instance
		EmbedBuilder eb = new EmbedBuilder();
		String Time = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
		//Title of Embed
		eb.setTitle("Voice Channel up!", null);
		eb.setAuthor("CitadelCraft Bot", null, "https://i7.pngguru.com/preview/3/108/41/blue-microphones-computer-icons-amazon-echo-microphone-thumbnail.jpg");
		//Color thats shows on Embed
		eb.setColor(new Color(163, 114, 28));
		//Images 
		eb.setImage("https://i7.pngguru.com/preview/3/108/41/blue-microphones-computer-icons-amazon-echo-microphone-thumbnail.jpg");
		eb.setDescription("TEST TEST TEST TEST");
		eb.setFooter("CitadelCraft Bot | " + Time , "https://i7.pngguru.com/preview/3/108/41/blue-microphones-computer-icons-amazon-echo-microphone-thumbnail.jpg");
		
		return eb.build();
		
	}

}
