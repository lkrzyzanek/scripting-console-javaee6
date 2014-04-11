package com.krzyzanek.scriptconsole.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.script.ScriptEngine;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

/**
 * Main logic for scripting console
 *
 * @author Libor Krzyzanek
 */
@Named
@Stateless
public class ScriptService implements Serializable {

	private static final Logger log = Logger.getLogger(ScriptService.class.getName());

	@Inject
	private ScriptServiceHelper scriptServiceHelper;

	public ScriptExecutor execute(String script, String language, boolean separateThread) {
		ScriptEngine engine = scriptServiceHelper.getScriptEngineMgr().getEngineByName(language);
		ScriptExecutor executor = new ScriptExecutor(script, engine, scriptServiceHelper);
		if (separateThread) {
			executor.start();
		} else {
			executor.doRun();
		}
		return executor;
	}

	public List<String> getLanguages() {
		return scriptServiceHelper.getLanguages();
	}

}
