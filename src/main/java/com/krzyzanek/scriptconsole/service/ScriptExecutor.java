package com.krzyzanek.scriptconsole.service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Script executor
 *
 * @author Libor Krzyzanek
 */
public class ScriptExecutor extends Thread {

	private static final Logger log = Logger.getLogger(ScriptExecutor.class.getName());

	private boolean running = false;

	private long start = 0;

	private long stop = 0;

	private String script;

	private StringWriter result = new StringWriter();

	private ScriptEngine engine;

	private ScriptServiceHelper scriptServiceHelper;

	public ScriptExecutor(String script, ScriptEngine engine, ScriptServiceHelper scriptServiceHelper) {
		super("ScriptingConsole-Executor");
		this.script = script;
		this.engine = engine;
		this.scriptServiceHelper = scriptServiceHelper;
	}

	@Override
	public void run() {
		doRun();
	}

	public void doRun() {
		start = System.currentTimeMillis();
		stop = 0;
		running = true;
		PrintWriter r = new PrintWriter(result, true);

		try {
			engine.eval(script);
			Invocable invocableEngine = (Invocable) engine;
			Object output = invocableEngine.invokeFunction("run", scriptServiceHelper, r);
			if (output != null) {
				r.println("Output from script:");
				r.print(output.toString());
			}
		} catch (ScriptException e) {
			e.printStackTrace(r);
			log.log(Level.SEVERE, "Exception occurred during script execution.", e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace(r);
			log.log(Level.SEVERE, "Exception occurred during script execution.", e);
		}

		r.close();

		running = false;
		stop = System.currentTimeMillis();
	}

	public boolean isRunning() {
		return running;
	}

	/**
	 * Get current value of result
	 *
	 * @return
	 */
	public String getResult() {
		return result.toString();
	}

	public long getStart() {
		return start;
	}

	public long getStop() {
		return stop;
	}

	public long getExecutionTime() {
		if (isRunning()) {
			return 0;
		}
		return stop - start;
	}
}
