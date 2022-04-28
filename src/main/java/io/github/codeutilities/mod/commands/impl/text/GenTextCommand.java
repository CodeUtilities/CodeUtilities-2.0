package io.github.codeutilities.mod.commands.impl.text;

import static io.github.codeutilities.mod.commands.arguments.ArgBuilder.argument;

import java.util.HashMap;
import java.util.Map;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.codeutilities.mod.commands.Command;
import io.github.codeutilities.mod.commands.arguments.ArgBuilder;
import io.github.codeutilities.sys.util.StringUtil;
import io.github.codeutilities.sys.util.TextUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

public class GenTextCommand extends Command {
	
    @Override
    public void register(MinecraftClient mc, CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(ArgBuilder.literal("gentext")
                .then(ArgBuilder.literal("smallcaps")
                		.then(argument("text", StringArgumentType.greedyString())
                				.executes(ctx -> {
                    	
                					// small caps map intakes lower case only, so this one gets a special .toLowerCase
                					convertAndSendMessage(ctx.getArgument("text", String.class).toLowerCase(), smallCaps, mc.player);
                				    
                					return 1;
                				})
                			)
                	)
                .then(ArgBuilder.literal("latin")
                		.then(argument("text", StringArgumentType.greedyString())
                				.executes(ctx -> {
                    	
                					convertAndSendMessage(ctx.getArgument("text", String.class), latin, mc.player);
                				    
                					return 1;
                				})
                			)
                	)
                .then(ArgBuilder.literal("superscript")
                		.then(argument("text", StringArgumentType.greedyString())
                				.executes(ctx -> {
                    	
                					convertAndSendMessage(ctx.getArgument("text", String.class), superScript, mc.player);
                				    
                					return 1;
                				})
                			)
                	)
                .then(ArgBuilder.literal("subscript")
                		.then(argument("text", StringArgumentType.greedyString())
                				.executes(ctx -> {
                    	
                					convertAndSendMessage(ctx.getArgument("text", String.class), subScript, mc.player);
                				    
                					return 1;
                				})
                			)
                	)
            );
    }

    @Override
    public String getDescription() {
        return "[blue]/gentext <type> <text>\n[reset]"
                + "\n"
                + "Converts the provided text into a given format, then copies it to your clipboard.\n"
                + "Unconvertable characters will be left as they were.\n"
                + "[yellow]<type> = smallcaps | latin | superscript | subscript";
    }

    @Override
    public String getName() {
        return "/gentext";
    }
    
    public Text convertAndSendMessage(String originalMessage, Map<Character, Character> charSet, ClientPlayerEntity player) {
    	
    	StringBuilder stringMessage = new StringBuilder(originalMessage);
        
        for (int i = 0; i < stringMessage.length(); i++) {
        	Character c = stringMessage.charAt(i);
        	if (!charSet.containsKey(c))  {
        		//skip any color codes, we don't want to convert them
        		// increasing i by 1 will effectively skip the next char
        		if (c == '&') i++;
        		continue;
        	}
        	
        	stringMessage.setCharAt(i, charSet.get(c));
        }
        
        String finalText = stringMessage.toString();
        
        Text result = TextUtil.colorCodesToTextComponent(
        		"§a» §7Copied the following text to the clipboard: §r" + finalText.replace("&", "§")
				);
        
        player.sendMessage(result, false);
        
        // send the converted string with color codes still intact
        StringUtil.copyToClipboard(finalText);
        
        return result;
    	
    }
    
    public static Map<Character, Character> smallCaps = new HashMap<Character, Character>();

	static {
	    smallCaps.put('a', '\u1d00');
	    smallCaps.put('b', '\u0299');
	    smallCaps.put('c', '\u1d04');
	    smallCaps.put('d', '\u1d05');
	    smallCaps.put('e', '\u1d07');
	    smallCaps.put('f', '\uA730');
	    smallCaps.put('g', '\u0262');
	    smallCaps.put('h', '\u029c');
	    smallCaps.put('i', '\u026a');
	    smallCaps.put('j', '\u1d0a');
	    smallCaps.put('k', '\u1d0b');
	    smallCaps.put('l', '\u029f');
	    smallCaps.put('m', '\u1d0d');
	    smallCaps.put('n', '\u0274');
	    smallCaps.put('o', '\u1d0f');
	    smallCaps.put('p', '\u1d18');
	    smallCaps.put('q', '\u01eb');
	    smallCaps.put('r', '\u0280');
	    // yes, s & x are just normal.
	    smallCaps.put('s', 's');                 
	    smallCaps.put('t', '\u1d1b');
	    smallCaps.put('u', '\u1d1c');
	    smallCaps.put('v', '\u1d20');
	    smallCaps.put('w', '\u1d21');
	    smallCaps.put('x', 'x');                 
	    smallCaps.put('y', '\u028f');
	    smallCaps.put('z', '\u1d22');
	    
	    // these are probably unnecessary but why not i guess
	    smallCaps.put('ф', '\u0239');
	    smallCaps.put('ł', '\u1d0c');
	    smallCaps.put('ɔ', '\u1d10');
	    smallCaps.put('ɯ', '\ua7fa');
	    smallCaps.put('æ', '\u1d01');
	    smallCaps.put('œ', '\u0276');
	    smallCaps.put('ð', '\u1d06');
	    smallCaps.put('γ', '\u1d26');
	    smallCaps.put('λ', '\u1d27');
	    smallCaps.put('π', '\u1d28');
	    smallCaps.put('ρ', '\u1d18');
	    smallCaps.put('ʒ', '\u1d23');
	    smallCaps.put('ǝ', '\u2c7b');
	    smallCaps.put('ψ', '\u1d2a');

	}
	
	
	private static Map<Character, Character> superScript = new HashMap<Character, Character>();

	static {
        superScript.put('0', '\u2070');
        superScript.put('1', '\u00b9');
        superScript.put('2', '\u00b2');
        superScript.put('3', '\u00b3');
        superScript.put('4', '\u2074');
        superScript.put('5', '\u2075');
        superScript.put('6', '\u2076');
        superScript.put('7', '\u2077');
        superScript.put('8', '\u2078');
        superScript.put('9', '\u2079');
        superScript.put('+', '\u207a');
        superScript.put('-', '\u207b');
        superScript.put('=', '\u207c');
        superScript.put('(', '\u207d');
        superScript.put(')', '\u207e');
	}
	
	private static Map<Character, Character> subScript = new HashMap<Character, Character>();
	
	static {
        subScript.put('0', '\u2080');
        subScript.put('1', '\u2081');
        subScript.put('2', '\u2082');
        subScript.put('3', '\u2083');
        subScript.put('4', '\u2084');
        subScript.put('5', '\u2085');
        subScript.put('6', '\u2086');
        subScript.put('7', '\u2087');
        subScript.put('8', '\u2088');
        subScript.put('9', '\u2089');
        subScript.put('+', '\u208a');
        subScript.put('-', '\u208b');
        subScript.put('=', '\u208c');
        subScript.put('(', '\u208d');
        subScript.put(')', '\u208e');
	}
	
	private static Map<Character, Character> latin = new HashMap<Character, Character>();
	
	static {
		latin.put('A', '\uFF21');
		latin.put('B', '\uFF22');
		latin.put('C', '\uFF23');
		latin.put('D', '\uFF24');
		latin.put('E', '\uFF25');
		latin.put('F', '\uFF26');
		latin.put('G', '\uFF27');
		latin.put('H', '\uFF28');
		latin.put('I', '\uFF29');
		latin.put('J', '\uFF2A');
		latin.put('K', '\uFF2B');
		latin.put('L', '\uFF2C');
		latin.put('M', '\uFF2D');
		latin.put('N', '\uFF2E');
		latin.put('O', '\uFF2F');
		latin.put('P', '\uFF30');
		latin.put('Q', '\uFF31');
		latin.put('R', '\uFF32');
		latin.put('S', '\uFF33');
		latin.put('T', '\uFF34');
		latin.put('U', '\uFF35');
		latin.put('V', '\uFF36');
		latin.put('W', '\uFF37');
		latin.put('X', '\uFF38');
		latin.put('Y', '\uFF39');
		latin.put('Z', '\uFF3A');




		latin.put('a', '\uFF41');
		latin.put('b', '\uFF42');
		latin.put('c', '\uFF43');
		latin.put('d', '\uFF44');
		latin.put('e', '\uFF45');
		latin.put('f', '\uFF46');
		latin.put('g', '\uFF47');
		latin.put('h', '\uFF48');
		latin.put('i', '\uFF49');
		latin.put('j', '\uFF4A');
		latin.put('k', '\uFF4B');
		latin.put('l', '\uFF4C');
		latin.put('m', '\uFF4D');
		latin.put('n', '\uFF4E');
		latin.put('o', '\uFF4F');
		latin.put('p', '\uFF50');
		latin.put('q', '\uFF51');
		latin.put('r', '\uFF52');
		latin.put('s', '\uFF53');
		latin.put('t', '\uFF54');
		latin.put('u', '\uFF55');
		latin.put('v', '\uFF56');
		latin.put('w', '\uFF57');
		latin.put('x', '\uFF58');
		latin.put('y', '\uFF59');
		latin.put('z', '\uFF5A');
	}

}
