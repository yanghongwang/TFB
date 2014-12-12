package com.cn.tfb.json.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import com.cn.tfb.json.JSON;
import com.cn.tfb.json.util.IOUtils;

public class DateSerializer implements ObjectSerializer
{

	public final static DateSerializer instance = new DateSerializer();

	@SuppressLint("SimpleDateFormat")
	public void write(JSONSerializer serializer, Object object,
			Object fieldName, Type fieldType) throws IOException
	{
		SerializeWriter out = serializer.getWriter();

		if (object == null)
		{
			out.writeNull();
			return;
		}

		if (out.isEnabled(SerializerFeature.WriteClassName))
		{
			if (object.getClass() != fieldType)
			{
				if (object.getClass() == java.util.Date.class)
				{
					out.write("new Date(");
					out.writeLongAndChar(((Date) object).getTime(), ')');
				}
				else
				{
					out.write('{');
					out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
					serializer.write(object.getClass().getName());
					out.writeFieldValue(',', "val", ((Date) object).getTime());
					out.write('}');
				}
				return;
			}
		}

		Date date = (Date) object;

		if (out.isEnabled(SerializerFeature.WriteDateUseDateFormat))
		{
			DateFormat format = serializer.getDateFormat();
			if (format == null)
			{
				format = new SimpleDateFormat(JSON.DEFFAULT_DATE_FORMAT);
			}
			String text = format.format(date);
			out.writeString(text);
			return;
		}

		long time = date.getTime();
		if (serializer.isEnabled(SerializerFeature.UseISO8601DateFormat))
		{
			if (serializer.isEnabled(SerializerFeature.UseSingleQuotes))
			{
				out.append('\'');
			}
			else
			{
				out.append('\"');
			}

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(time);

			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);
			int millis = calendar.get(Calendar.MILLISECOND);

			char[] buf;
			if (millis != 0)
			{
				buf = "0000-00-00T00:00:00.000".toCharArray();
				IOUtils.getChars(millis, 23, buf);
				IOUtils.getChars(second, 19, buf);
				IOUtils.getChars(minute, 16, buf);
				IOUtils.getChars(hour, 13, buf);
				IOUtils.getChars(day, 10, buf);
				IOUtils.getChars(month, 7, buf);
				IOUtils.getChars(year, 4, buf);

			}
			else
			{
				if (second == 0 && minute == 0 && hour == 0)
				{
					buf = "0000-00-00".toCharArray();
					IOUtils.getChars(day, 10, buf);
					IOUtils.getChars(month, 7, buf);
					IOUtils.getChars(year, 4, buf);
				}
				else
				{
					buf = "0000-00-00T00:00:00".toCharArray();
					IOUtils.getChars(second, 19, buf);
					IOUtils.getChars(minute, 16, buf);
					IOUtils.getChars(hour, 13, buf);
					IOUtils.getChars(day, 10, buf);
					IOUtils.getChars(month, 7, buf);
					IOUtils.getChars(year, 4, buf);
				}
			}

			out.write(buf);

			if (serializer.isEnabled(SerializerFeature.UseSingleQuotes))
			{
				out.append('\'');
			}
			else
			{
				out.append('\"');
			}
		}
		else
		{
			out.writeLong(time);
		}
	}
}
