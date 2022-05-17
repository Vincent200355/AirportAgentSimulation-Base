package dhbw.sose2022.softwareengineering.airportagentsim.simulation.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurableAttribute;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurationFormatException;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurationParseException;

final class ObjectRegistryEntry extends RegistryEntry {
	
	final ConfigurableAttribute[] parameters;
	
	private final Constructor<?> c;
	
	final HashMap<String, Integer> parametersByName;
	
	ObjectRegistryEntry(Class<?> target, ConfigurableAttribute[] parameters) throws ConfigurationFormatException {
		super(target);
		
		this.parameters = parameters;
		
		this.c = findConstructor();
		this.parametersByName = getParameterMap();
		
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof ObjectRegistryEntry
				&& this.target.equals(((ObjectRegistryEntry) other).target)
				&& Arrays.equals(this.parameters, ((ObjectRegistryEntry) other).parameters);
	}
	
	@Override
	public int hashCode() {
		return this.target.hashCode() + Arrays.hashCode(this.parameters);
	}
	
	
	Object parse(Object[] parameters) throws ConfigurationFormatException, ConfigurationParseException {
		try {
			return this.c.newInstance(parameters);
		} catch(InstantiationException e) {
		} catch(IllegalAccessException e) {
		} catch(InvocationTargetException e) {
			throw new ConfigurationParseException("Parsing a configuration object of type " + this.target.getSimpleName() + " failed due to an exception thrown by its constructor.", e);
		}
		throw new ConfigurationFormatException("Invalid configuration format for " + this.target.getSimpleName() + ". The class is abstact, not accessible, not public or declares no public constructor using the given parameters.");
	}
	
	
	private Constructor<?> findConstructor() throws ConfigurationFormatException {
		if(Modifier.isAbstract(this.target.getModifiers()))
			throw new ConfigurationFormatException("Invalid configuration format for " + this.target.getSimpleName() + ". The class is abstact.");
		if(!Modifier.isPublic(this.target.getModifiers()))
			throw new ConfigurationFormatException("Invalid configuration format for " + this.target.getSimpleName() + ". The class is not public.");
		Class<?>[] parameters = new Class<?>[this.parameters.length];
		for(int i = 0; i < parameters.length; i++)
			parameters[i] = this.parameters[i].getType();
		try {
			return this.target.getConstructor(parameters);
		} catch(NoSuchMethodException e) {
			throw new ConfigurationFormatException("Invalid configuration format for " + this.target.getSimpleName() + ". The class does not define a public constructor with parameter types " + Arrays.asList(parameters).stream().map((type) -> type.getSimpleName()).collect(Collectors.joining(", ")) + ".");
		}
	}
	
	private HashMap<String, Integer> getParameterMap() throws ConfigurationFormatException {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(int i = 0; i < this.parameters.length; i++)
			if(map.put(this.parameters[i].getConfigurationKey(), i) != null)
				throw new ConfigurationFormatException("Invalid configuration format for " + this.target.getSimpleName() + ". Multiple parameters use the same key.");
		return map;
	}
	
}
