Scripting Console For Java EE 6
===============================

This simple Java EE 6 web app demonstrates easy way how to execute script written in supported language by JVM.

Simple scripts needs to have one method `run` as follows:

		importPackage(java.lang);

		/* Run the script. helper is ScriptServiceHelper, out is PrintWriter */
		function run(helper, out) {
		  out.append("\nDONE");
		}

Arguments are:

 * helper - helper class for easy getting CDI bean via helper.getBeanByName("beanName");
 * out - result of script


Features
--------

 * Execution in same thread or separate thread
 * Helper to get CDI bean based on its name
 * Print result of script to print writer

Examples
--------

1. Call method of CDI bean


		function run(helper, out) {
		  service = helper.getBeanByName("scriptService");
		  out.append("Available Languages: " + service.getLanguages());
		  out.append("\nDONE");
		}


2. Sleep 1 sec 3 times to demonstrate benefit by execution in separate thread


		importPackage(java.lang);

		function run(helper, out) {
		  Thread.sleep(1000);
		  out.append("\n1");
		  Thread.sleep(1000);
		  out.append("\n2");
		  Thread.sleep(1000);
		  out.append("\n3");
		  Thread.sleep(1000);
		  out.append("\nDONE");
		}


