package com.krzyzanek.scriptconsole.controller;

import com.krzyzanek.scriptconsole.service.ScriptExecutor;
import com.krzyzanek.scriptconsole.service.ScriptService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Controller for scripting console page
 *
 * @author Libor Krzyzanek
 */
@Named
@SessionScoped
public class ScriptConsoleController implements Serializable {

	private static final Logger log = Logger.getLogger(ScriptConsoleController.class.getName());

	@Inject
	private ScriptService scriptService;

	private String language = "JavaScript";

	private String script = "";

	private boolean separateThread = false;

	private ScriptExecutor executor;

	private boolean running = false;

	public String execute() {
		running = true;

		executor = scriptService.execute(script, language, separateThread);
		running = false;
		return "index.jsf?faces-redirect=true";
	}

	public List<String> getLanguages() {
		return scriptService.getLanguages();
	}

	public boolean isRunning() {
		if (separateThread && executor != null) {
			return executor.isRunning();
		}
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public String getResult() {
		if (script == "") {
			return "Insert script content, choose scripting language and then press the Execute button";
		}
		if (isRunning() && !separateThread) {
			return "";
		}
		return executor.getResult();
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public boolean isSeparateThread() {
		return separateThread;
	}

	public void setSeparateThread(boolean separateThread) {
		this.separateThread = separateThread;
	}

	public Date getStartDate() {
		if (executor != null) {
			return new Date(executor.getStart());
		}
		return null;
	}

	public Date getFinishDate() {
		if (executor != null && !executor.isRunning()) {
			return new Date(executor.getStop());
		}
		return null;
	}

	public Long getExecutionTimeInSec() {
		if (executor != null && !executor.isRunning()) {
			return executor.getExecutionTime() / 1000;
		}
		return null;
	}
}
