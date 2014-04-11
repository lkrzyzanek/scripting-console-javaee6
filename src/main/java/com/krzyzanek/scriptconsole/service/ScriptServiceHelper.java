package com.krzyzanek.scriptconsole.service;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper bean for script execution or accessing CDI bean by its name
 *
 * @author Libor Krzyzanek
 */
@Named
@ApplicationScoped
@Singleton
public class ScriptServiceHelper {

	private static final Logger log = Logger.getLogger(ScriptServiceHelper.class.getName());

	private List<String> languages;

	private ScriptEngineManager scriptEngineMgr;

	@Inject
	private BeanManager beanManager;

	@PostConstruct
	public void init() {
		this.scriptEngineMgr = new ScriptEngineManager();

		this.languages = new ArrayList<>();

		List<ScriptEngineFactory> factories = scriptEngineMgr.getEngineFactories();

		for (int i = 0; i < factories.size(); i++) {
			ScriptEngineFactory factory = factories.get(i);
			languages.addAll(factory.getNames());
		}

	}

	public Object getBeanByName(String name) {
		Iterator<Bean<?>> iter = beanManager.getBeans(name).iterator();
		if (!iter.hasNext()) {
			log.log(Level.FINEST, "Cannot find bean. name: {0}", name);
			return null;
		}
		Bean bean = iter.next();
		CreationalContext ctx = beanManager.createCreationalContext(bean);
		Type type = (Type) bean.getTypes().iterator().next();
		return beanManager.getReference(bean, type, ctx);
	}

	public List<String> getLanguages() {
		return languages;
	}

	public ScriptEngineManager getScriptEngineMgr() {
		return scriptEngineMgr;
	}
}
