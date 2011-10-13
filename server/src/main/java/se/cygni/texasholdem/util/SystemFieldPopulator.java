package se.cygni.texasholdem.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemFieldPopulator {

    private static Logger log = LoggerFactory
            .getLogger(SystemFieldPopulator.class);

    private final Object target;
    private final String propertyPrefix;

    public SystemFieldPopulator(final Object target, final String propertyPrefix) {

        this.target = target;
        this.propertyPrefix = propertyPrefix;
    }

    public void populateValuesFromSystemProperties() {

        final Field[] fields = target.getClass().getDeclaredFields();
        for (final Field f : fields) {

            // Don't try to set final variables
            if (Modifier.isFinal(f.getModifiers()))
                continue;

            final Object valueFromSystem = getPropertyFromSystem(
                    f.getName(), f.getType());

            if (valueFromSystem != null) {
                f.setAccessible(true);
                try {
                    f.set(target, valueFromSystem);

                    log.info("Overriding property {} to {} for "
                            + target.getClass().getSimpleName(), f.getName(),
                            valueFromSystem);

                } catch (final Exception e) {
                    log.warn("Failed to override property {} to {} for "
                            + target.getClass().getSimpleName(), f.getName(),
                            valueFromSystem);
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private Object getPropertyFromSystem(final String property, final Class type) {

        if (type == Double.class || type == double.class) {
            return getDoublePropertyFromSystem(property);
        }

        if (type == Float.class || type == float.class) {
            return getFloatPropertyFromSystem(property);
        }

        if (type == Long.class || type == long.class) {
            return getLongPropertyFromSystem(property);
        }

        if (type == Integer.class || type == int.class) {
            return getIntegerPropertyFromSystem(property);
        }

        if (type == String.class) {
            return getStringPropertyFromSystem(property);
        }

        if (type == Boolean.class || type == boolean.class) {
            return getBooleanPropertyFromSystem(property);
        }

        return null;
    }

    private Float getFloatPropertyFromSystem(final String property) {

        final String val = getStringPropertyFromSystem(property);
        if (val == null)
            return null;

        Float floatVal = null;

        try {
            floatVal = Float.valueOf(val);
        } catch (final Exception e) {
        }
        return floatVal;
    }

    private Double getDoublePropertyFromSystem(final String property) {

        final String val = getStringPropertyFromSystem(property);
        if (val == null)
            return null;

        Double doubleVal = null;

        try {
            doubleVal = Double.valueOf(val);
        } catch (final Exception e) {
        }
        return doubleVal;
    }

    private Long getLongPropertyFromSystem(final String property) {

        return Long.getLong(propertyPrefix + property);
    }

    private Integer getIntegerPropertyFromSystem(final String property) {

        return Integer.getInteger(propertyPrefix + property);
    }

    private String getStringPropertyFromSystem(final String property) {

        return System.getProperty(propertyPrefix + property);
    }

    private Boolean getBooleanPropertyFromSystem(final String property) {

        final String sysVal = System.getProperty(propertyPrefix + property);
        if (sysVal == null)
            return null;

        return Boolean.valueOf(sysVal.toLowerCase());
    }
}
