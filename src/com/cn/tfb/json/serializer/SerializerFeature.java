package com.cn.tfb.json.serializer;

public enum SerializerFeature
{
	QuoteFieldNames,
	/**
     * 
     */
	UseSingleQuotes,
	/**
     * 
     */
	WriteMapNullValue,
	/**
     * 
     */
	WriteEnumUsingToString,
	/**
     * 
     */
	UseISO8601DateFormat,
	/**
	 * @since 1.1
	 */
	WriteNullListAsEmpty,
	/**
	 * @since 1.1
	 */
	WriteNullStringAsEmpty,
	/**
	 * @since 1.1
	 */
	WriteNullNumberAsZero,
	/**
	 * @since 1.1
	 */
	WriteNullBooleanAsFalse,
	/**
	 * @since 1.1
	 */
	SkipTransientField,
	/**
	 * @since 1.1
	 */
	SortField,
	/**
	 * @since 1.1.1
	 */
	@Deprecated
	WriteTabAsSpecial,
	/**
	 * @since 1.1.2
	 */
	PrettyFormat,
	/**
	 * @since 1.1.2
	 */
	WriteClassName,

	/**
	 * @since 1.1.6
	 */
	DisableCircularReferenceDetect,

	/**
	 * @since 1.1.9
	 */
	WriteSlashAsSpecial,

	/**
	 * @since 1.1.10
	 */
	BrowserCompatible,

	/**
	 * @since 1.1.14
	 */
	WriteDateUseDateFormat,

	/**
	 * @since 1.1.15
	 */
	NotWriteRootClassName,

	/**
	 * @since 1.1.19
	 */
	DisableCheckSpecialChar,

	/**
	 * @since 1.1.35
	 */
	BeanToArray,

	/**
	 * @since 1.1.37
	 */
	WriteNonStringKeyAsString,

	/**
	 * @since 1.1.42
	 */
	NotWriteDefaultValue;

	private SerializerFeature()
	{
		mask = (1 << ordinal());
	}

	private final int mask;

	public final int getMask()
	{
		return mask;
	}

	public static boolean isEnabled(int features, SerializerFeature feature)
	{
		return (features & feature.getMask()) != 0;
	}

	public static int config(int features, SerializerFeature feature,
			boolean state)
	{
		if (state)
		{
			features |= feature.getMask();
		}
		else
		{
			features &= ~feature.getMask();
		}

		return features;
	}

	public static int of(SerializerFeature[] features)
	{
		if (features == null) { return 0; }

		int value = 0;

		for (SerializerFeature feature : features)
		{
			value |= feature.getMask();
		}

		return value;
	}
}
