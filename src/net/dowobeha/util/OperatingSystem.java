package net.dowobeha.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;



public class OperatingSystem {

	private static final boolean MAC_OS_X = 
		(System.getProperty("os.name").toLowerCase().startsWith("mac os x"));

	public static boolean isMac() {
		return MAC_OS_X;
	}

	private static final RunnableInvocationHandler MAC_OS_X_INVOCATION_HANDLER = new RunnableInvocationHandler();
	private static final Method MAC_OS_X_HANDLE_APPLICATION_EVENT;
	private static final Object MAC_OS_X_APP;

	static {

		Method handleApplicationEventMethod = null;
		Object macApp = null;

		if (OperatingSystem.isMac()){

			try {
				Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
				Class<?> applicationListenerClass = Class.forName("com.apple.eawt.ApplicationListener");

				macApp = applicationClass.newInstance();

				Object proxy = 
					Proxy.newProxyInstance(
							RunnableInvocationHandler.class.getClassLoader(), 
							new Class[] { applicationListenerClass }, MAC_OS_X_INVOCATION_HANDLER);


				Method addListenerMethod = 
					applicationClass.getDeclaredMethod(
							"addApplicationListener", 
							new Class[] { applicationListenerClass });

				addListenerMethod.invoke(macApp, new Object[] { proxy });

				Class<?> applicationEventClass = Class.forName("com.apple.eawt.ApplicationEvent");

				handleApplicationEventMethod =
					applicationEventClass.getDeclaredMethod("setHandled", boolean.class);

			} catch (Exception e) { 
				// This space intentionally left blank
			}
		}

		MAC_OS_X_HANDLE_APPLICATION_EVENT = handleApplicationEventMethod;
		MAC_OS_X_APP = macApp;
	}

	public static void defineMacAboutMenu(final Runnable action) {
		if (OperatingSystem.isMac()) {
			try {
				// Using reflection, get the method 
				// com.apple.eawt.Application#setEnabledAboutMenu(boolean)
				Method enableAboutMenuMethod = 
					MAC_OS_X_APP.getClass().getDeclaredMethod(
							"setEnabledAboutMenu",
							new Class[] { boolean.class });

				// Invoke the method, 
				// passing true as a parameter
				enableAboutMenuMethod.invoke(
						MAC_OS_X_APP, new Object[] { true });

				// Register the ability to handle 
				// the about menu item 
				// with the reflection invocation handler
				MAC_OS_X_INVOCATION_HANDLER.put("handleAbout", action);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void defineMacQuitMenu(final Runnable action) {
		if (OperatingSystem.isMac()) {
			MAC_OS_X_INVOCATION_HANDLER.put("handleQuit", action);
		}
	}

	public static void defineMacPreferencesMenu(final Runnable action) {
		if (OperatingSystem.isMac()) {
			try {
				
				// Using reflection, get the method 
				// com.apple.eawt.Application#setEnabledPreferencesMenu(boolean)
				Method enablePreferencesMenuMethod = 
					MAC_OS_X_APP.getClass().getDeclaredMethod(
							"setEnabledPreferencesMenu",
							new Class[] { boolean.class });

				// Invoke the method, 
				// passing true as a parameter
				enablePreferencesMenuMethod.invoke(
						MAC_OS_X_APP, new Object[] { true });

				// Register the ability to handle 
				// the preferences menu item 
				// with the reflection invocation handler
				MAC_OS_X_INVOCATION_HANDLER.put("handlePreferences", action);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private static final boolean handleMacEvent(Method method, Object[] args) throws Throwable {

		MAC_OS_X_HANDLE_APPLICATION_EVENT.invoke(args[0], true);

		return true;
	}

	@SuppressWarnings("serial")
	private static final class RunnableInvocationHandler extends HashMap<String,Runnable> implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {

			String methodName = method.getName();

			if (containsKey(methodName) && handleMacEvent(method,args)) {
				Runnable action = get(methodName);
				action.run();
			}

			return null;
		}

	}

}

