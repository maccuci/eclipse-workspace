package br.com.houldmc.rankup.manager.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.houldmc.rankup.player.group.list.GroupList;

/**
 * Command Framework - Command <br>
 * The command annotation used to designate methods as commands. All methods
 * should have a single CommandArgs argument
 * 
 * @author minnymin3
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	/**
	 * The name of the command. If it is a sub command then its values would be
	 * separated by periods. ie. a command that would be a subcommand of test
	 * would be 'test.subcommandname'
	 * 
	 * @return
	 */
	public String name();

	/**
	 * Gets the required permission of the command
	 * 
	 * @return
	 */
	public GroupList groupToUse() default GroupList.NORMAL;

	/**
	 * The message sent to the player when they do not have permission to
	 * execute it
	 * 
	 * @return
	 */
	public String noPerm() default "�cVoc� n�o pode executar este comando.";

	/**
	 * A list of alternate names that the command is executed under. See
	 * name() for details on how names work
	 * 
	 * @return
	 */
	public String[] aliases() default {};

	/**
	 * The description that will appear in /help of the command
	 * 
	 * @return
	 */
	public String description() default "";

	/**
	 * The usage that will appear in /help (commandname)
	 * 
	 * @return
	 */
	public String usage() default "";
	
	/**
	 * Whether or not the command is available to players only
	 * 
	 * @return
	 */
	public boolean inGameOnly() default false;
}
