package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.config;

import org.apache.commons.lang3.Validate;

public final class ConfigurableAttribute {
	
	private final String configKey;
	private final boolean required;
	private final Class<?> type;
	private final Object defaultValue;
	
	/**
	 * Constructs a new required configurable attribute. It will use the given
	 * key and be of the given type.<br><br>
	 * 
	 * @param configKey the configuration key
	 * @param type the value type
	 */
	public ConfigurableAttribute(String configKey, Class<?> type) {
		Validate.notNull(configKey);
		Validate.notNull(type);
		this.configKey = configKey;
		this.required = true;
		this.type = type;
		this.defaultValue = null;
	}
	
	/**
	 * Constructs a new optional configurable attribute. It will use the given
	 * key and be of the given type. If the value is omitted from the
	 * configuration, it will assume the given default value.<br><br>
	 * 
	 * <b>The given default value must be immutable, i.e. its internal state
	 * must not change at any point, because the provided instance will be
	 * shared between all occurrences of this attribute in and between
	 * configurations.</b>
	 * 
	 * @param configKey the configuration key
	 * @param type the value type
	 * @param defaultValue the immutable default value
	 */
	public <T> ConfigurableAttribute(String configKey, Class<T> type, T defaultValue) {
		Validate.notNull(configKey);
		Validate.notNull(type);
		Validate.notNull(defaultValue);
		Validate.isTrue(type.isInstance(defaultValue), "default value must be assignable to the parameter type");
		this.configKey = configKey;
		this.required = false;
		this.type = type;
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Returns the non-{@code null} key used to identify this attribute in a
	 * configuration file.<br><br>
	 * 
	 * @return the configuration key
	 */
	public String getConfigurationKey() {
		return this.configKey;
	}
	
	/**
	 * Returns {@code true} if, and only if, this value is required. If a
	 * required value is omitted in the configuration, parsing will fail.
	 * <br><br>
	 * 
	 * If this method returns {@code false}, {@link #getDefaultValue()} can be
	 * used to obtain the default value for this attribute.<br><br>
	 * 
	 * @return {@code true} if this value is required, {@code false} otherwise
	 */
	public boolean isRequired() {
		return this.required;
	}
	
	/**
	 * Returns the value type for this attribute. When parsing the
	 * configuration, the value defined for this attribute's key will be assumed
	 * to be of this type.<br><br>
	 * 
	 * @return the value type
	 */
	public Class<?> getType() {
		return this.type;
	}
	
	/**
	 * Returns a default value for this attribute. The default attribute is
	 * immutable.<br><br>
	 * 
	 * A default value is present, it will never be {@code null}. A default
	 * value is present if, and only if, {@link #isRequired()} returns
	 * {@code false}.<br><br>
	 * 
	 * @return the immutable default value
	 */
	public Object getDefaultValue() {
		return this.defaultValue;
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof ConfigurableAttribute
				&& this.configKey.equals(((ConfigurableAttribute) other).configKey)
				&& this.required == ((ConfigurableAttribute) other).required
				&& this.type == ((ConfigurableAttribute) other).type
				&& (this.defaultValue == null ? ((ConfigurableAttribute) other).defaultValue == null : this.defaultValue.equals(((ConfigurableAttribute) other).defaultValue));
	}
	
	@Override
	public int hashCode() {
		return this.type.hashCode();
	}
	
}
