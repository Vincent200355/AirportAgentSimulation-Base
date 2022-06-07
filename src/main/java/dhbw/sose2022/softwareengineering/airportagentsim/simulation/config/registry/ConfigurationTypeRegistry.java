package dhbw.sose2022.softwareengineering.airportagentsim.simulation.config.registry;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.Validate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurableAttribute;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurationFormatException;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config.ConfigurationParseException;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.LoadedPlugin;

public final class ConfigurationTypeRegistry {
	
	private final HashMap<Class<?>, RegistryEntry> entries = new HashMap<Class<?>, RegistryEntry>();
	
	private final HashMap<String, RegisteredEntity> entitiesByID = new HashMap<String, RegisteredEntity>();
	private final HashMap<Class<?>, String> entityIDByClass = new HashMap<Class<?>, String>();
	
	private final Class<Entity> entityClass;
	private final Field entityClassPluginField;
	
	
	public ConfigurationTypeRegistry() {
		
		registerSimpleEntry(Boolean.class);
		registerSimpleEntry(Byte.class);
		registerSimpleEntry(Short.class);
		registerSimpleEntry(Integer.class);
		registerSimpleEntry(Long.class);
		registerSimpleEntry(Float.class);
		registerSimpleEntry(Double.class);
		registerSimpleEntry(String.class);
		
		this.entityClass = Entity.class;
		try {
			this.entityClassPluginField = this.entityClass.getDeclaredField("plugin");
			this.entityClassPluginField.setAccessible(true);
		} catch(NoSuchFieldException e) {
			throw new Error(e);
		} catch(SecurityException e) {
			throw new Error(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T parseJSONObject(Class<T> type, JsonObject object) throws ConfigurationFormatException, ConfigurationParseException {
		
		if(type.isArray())
			throw new ConfigurationParseException("Cannot parse JSON object to array");
		
		RegistryEntry registryEntry = this.entries.get(type);
		if(registryEntry == null || !(registryEntry instanceof ObjectRegistryEntry))
			throw new ConfigurationFormatException("No configuration format defined for " + type.getSimpleName());
		
		Object o = parseObject((ObjectRegistryEntry) registryEntry, object);
		if(!type.isInstance(o))
			throw new ConfigurationFormatException("Invalid configuration format for " + type.getSimpleName());
		return (T) o;
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T parseJSONArray(Class<T> type, JsonArray array) throws ConfigurationFormatException, ConfigurationParseException {
		
		if(!type.isArray())
			throw new ConfigurationParseException("Cannot parse JSON array to non-array object");
		
		Object o = parseArray(type, array);
		if(!type.isInstance(o))
			throw new ConfigurationFormatException("Invalid configuration format for " + type.getSimpleName());
		return (T) o;
		
	}
	
	public void registerConfigurationType(Class<?> type, ConfigurableAttribute[] parameters) throws ConfigurationFormatException {
		
		Validate.notNull(type);
		Validate.noNullElements(parameters);
		Validate.isTrue(!type.isArray(), "Cannot define array configuration types explicitly");
		
		if(parameters.length > 0) {
			HashSet<String> configKeys = new HashSet<String>();
			for(int i = 0; i < parameters.length; i++)
				if(!configKeys.add(parameters[i].getConfigurationKey()))
					throw new ConfigurationFormatException("Cannot define a configuration type which defines multiple parameters using the same configuration key");
		}
		
		ObjectRegistryEntry ore = new ObjectRegistryEntry(type, parameters);
		if(this.entries.containsKey(type)) {
			if(ore.equals(this.entries.get(type)))
				return;
			throw new ConfigurationFormatException("Cannot redefine configuration type " + type.getSimpleName());
		}
		this.entries.put(type, ore);
		
	}
	
	public boolean isEntityIDRegistered(String entityTypeID) {
		return this.entitiesByID.containsKey(entityTypeID);
	}
	
	public LoadedPlugin getPluginByEntityID(String entityTypeID) {
		RegisteredEntity registeredEntity = this.entitiesByID.get(entityTypeID);
		if(registeredEntity == null)
			return null;
		return registeredEntity.getPlugin();
	}
	
	public Entity parseEntity(String entityID, JsonObject object) throws ConfigurationFormatException, ConfigurationParseException {
		
		RegisteredEntity registeredEntity = this.entitiesByID.get(entityID);
		
		if(registeredEntity == null)
			throw new ConfigurationFormatException("Unknown entity ID \"" + entityID + "\"");
		
		return parseJSONObject(registeredEntity.getEntityType(), object);
		
	}
	
	public String getEntityID(Class<?> type) {
		return this.entityIDByClass.get(type);
	}
	
	public void registerEntityID(LoadedPlugin loadedPlugin, String entityID, Class<? extends Entity> type) throws IllegalArgumentException {
		if(this.entitiesByID.containsKey(entityID))
			throw new IllegalArgumentException("Duplicate entity id: " + entityID);
		if(this.entityIDByClass.containsKey(type))
			throw new IllegalArgumentException("Duplicate entity registration: " + type.getSimpleName());
		this.entitiesByID.put(entityID, new RegisteredEntity(loadedPlugin, type));
		this.entityIDByClass.put(type, entityID);
	}
	
	
	private void registerSimpleEntry(Class<?> type) {
		this.entries.put(type, new SimpleRegistryEntry(type));
	}
	
	private Object parseObject(ObjectRegistryEntry registryEntry, JsonObject object) throws ConfigurationFormatException, ConfigurationParseException {
		
		Object[] parameters = new Object[registryEntry.parameters.length];
		
		HashSet<Integer> undefinedIndecies = new HashSet<Integer>();
		for(int i = 0; i < parameters.length; i++)
			undefinedIndecies.add(i);
		
		for(String key : object.keySet()) {
			
			Integer parameterIndex = registryEntry.parametersByName.get(key);
			if(parameterIndex == null)
				throw new ConfigurationParseException("Attempting to parse an object of type " + registryEntry.target.getSimpleName() + ", but found illegal configuration key \"" + key + "\"");
			
			ConfigurableAttribute childAttribute = registryEntry.parameters[parameterIndex];
			try {
				parameters[parameterIndex] = parseJSONElement(childAttribute.getType(), object.get(key));
			} catch(ConfigurationParseException e) {
				throw new ConfigurationParseException("Failed to parse an object of type " + registryEntry.target.getSimpleName() + ": " + e.getMessage());
			}
			
			undefinedIndecies.remove(parameterIndex);
			
		}
		
		for(Integer undefinedIndex : undefinedIndecies) {
			if(registryEntry.parameters[undefinedIndex].isRequired())
				throw new ConfigurationParseException("Attempting to parse an object of type " + registryEntry.target.getSimpleName() + ", but required configuration key \"" + registryEntry.parameters[undefinedIndex].getConfigurationKey() + "\" is missing");
			parameters[undefinedIndex] = registryEntry.parameters[undefinedIndex].getDefaultValue();
		}
		
		return registryEntry.parse(parameters);
		
	}
	
	private Object parseArray(Class<?> targetType, JsonArray array) throws ConfigurationFormatException, ConfigurationParseException {
		
		Class<?> componentType = targetType.getComponentType();
		
		int length = array.size();
		Object resultArray = Array.newInstance(componentType, length);
		
		for(int i = 0; i < length; i++) {
			try {
				Array.set(resultArray, i, parseJSONElement(componentType, array.get(i)));
			} catch(ConfigurationParseException e) {
				throw new ConfigurationParseException("Failed to parse an array of type " + targetType.getSimpleName() + ": Error at index " + i + ": " + e.getMessage());
			}
		}
		
		return resultArray;
		
	}
	
	private Object parseJSONElement(Class<?> targetType, Object element) throws ConfigurationFormatException, ConfigurationParseException {
		
		if(targetType.isArray()) {
			if(!(element instanceof JsonArray))
				throw new ConfigurationParseException("Failed to parse " + targetType.getSimpleName() + ". Expected " + JsonArray.class.getSimpleName() + ", got " + element.getClass().getSimpleName());
			return parseArray(targetType, (JsonArray) element);
		}
		
		RegistryEntry registryEntry = this.entries.get(targetType);
		
		if(registryEntry instanceof SimpleRegistryEntry) {
			
			SimpleRegistryEntry sre = (SimpleRegistryEntry) registryEntry;
			
			if(Number.class.isAssignableFrom(sre.target)) {
				
				if(element instanceof JsonPrimitive) {
					element = ((JsonPrimitive) element).getAsNumber();
				}
				
				if(!(element instanceof Number))
					throw new ConfigurationParseException("Expected " + targetType.getSimpleName() + ", got " + element.getClass().getSimpleName());
				
				Number converted = parseNumber((Number) element, sre.target);
				if(converted != null)
					return converted;
				
			} else if(Boolean.class == sre.target) {
				
				if(element instanceof JsonPrimitive) {
					element = ((JsonPrimitive) element).getAsBoolean();
				}
				
			} else if(String.class == sre.target) {
				
				if(element instanceof JsonPrimitive) {
					element = ((JsonPrimitive) element).getAsString();
				}
				
			}
			
			if(!sre.target.isInstance(element))
				throw new ConfigurationParseException("Expected " + targetType.getSimpleName() + ", got " + element.getClass().getSimpleName());
			
			return element;
			
		} else if(registryEntry instanceof ObjectRegistryEntry) {
			
			if(!(element instanceof JsonObject))
				throw new ConfigurationParseException("Failed to parse " + targetType.getSimpleName() + ". Expected " + JsonObject.class.getSimpleName() + ", got " + element.getClass().getSimpleName());
			
			return parseObject((ObjectRegistryEntry) registryEntry, (JsonObject) element);
			
		} else {
			throw new ConfigurationFormatException("No configuration type definition for type " + targetType.getSimpleName());
		}
		
	}
	
	private Number parseNumber(Number original, Class<?> target) throws ConfigurationFormatException, ConfigurationParseException {
		
		if(original.getClass() == target)
			return original;
		
		if(target == Double.class)
			return Double.valueOf(original.doubleValue());
		if(target == Float.class)
			return Float.valueOf(original.floatValue());
		
		// GSON does not produce a Number type provided by Java. Therefore, the
		// original number cannot be type checked. Instead, check whether the
		// interpretations of the number as long and double differ. This will
		// not detect errors if the number is sufficiently large
		double doubleValue = original.doubleValue();
		if(!Double.isFinite(doubleValue) || ((double) original.longValue()) != doubleValue)
			throw new ConfigurationParseException("Attempting to parse an object of type " + target.getSimpleName() + ", but configuration value is not an integer");
		
		Number converted;
		if(target == Byte.class) {
			converted = Byte.valueOf(original.byteValue());
		} else if(target == Short.class) {
			converted = Short.valueOf(original.shortValue());
		} else if(target == Integer.class) {
			converted = Integer.valueOf(original.intValue());
		} else if(target == Long.class) {
			converted = Long.valueOf(original.longValue());
		} else {
			return null;
		}
		
		if(converted.longValue() != original.longValue())
			throw new ConfigurationParseException("Attempting to parse an object of type " + target.getSimpleName() + ", but configuration value is out of range");
		
		return converted;
		
	}
	
}
