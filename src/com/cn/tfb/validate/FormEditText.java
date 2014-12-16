package com.cn.tfb.validate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.cn.tfb.AppApplication;
import com.cn.tfb.R;

public class FormEditText extends EditText
{
	// 输入框删除图片按钮
	private Drawable imgAble;

	// 设置删除图片显示标志
	private int flag = 0;

	public FormEditText(Context context)
	{
		super(context);
		init();
		throw new RuntimeException("Not supported");
	}

	public FormEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
		editTextValidator = new DefaultEditTextValidator(this, attrs, context);
	}

	public FormEditText(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
		editTextValidator = new DefaultEditTextValidator(this, attrs, context);
	}

	/**
	 * 初始化删除文字图片
	 */
	private void init()
	{
		// 初始化删除图片
		imgAble = AppApplication.getInstance().getResources()
				.getDrawable(R.drawable.delete);
		// 输入框监听器
		addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
			}

			@Override
			public void afterTextChanged(Editable s)
			{
				// 如果输入框有文字就添加删除图片,输入框得到焦点
				if (length() >= 1 && flag == 1)
				{
					// 在输入框的右边添加图片
					setCompoundDrawablesWithIntrinsicBounds(null, null,
							imgAble, null);
				}
				else
				{
					// 隐藏图片
					setCompoundDrawablesWithIntrinsicBounds(null, null, null,
							null);
				}
			}
		});
		setOnFocusChangeListener(new OnFocusChangeListener()
		{

			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (hasFocus)
				{
					// 图片显示，flag为1
					flag = 1;
					// 如果输入框有文字，显示删除图片
					if (length() >= 1)
					{
						// 在输入框的右边添加图片
						setCompoundDrawablesWithIntrinsicBounds(null, null,
								imgAble, null);
					}
				}
				else
				{
					// 隐藏图片
					setCompoundDrawablesWithIntrinsicBounds(null, null, null,
							null);
					// 图片隐藏，flag为0
					flag = 0;
				}

			}
		});
	}

	public void addValidator(Validator theValidator)
			throws IllegalArgumentException
	{
		editTextValidator.addValidator(theValidator);
	}

	/**
	 * 删除事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// 判断图片是否加载,图片是否显示,监听按下事件
		if (imgAble != null && flag == 1
				&& event.getAction() == MotionEvent.ACTION_DOWN)
		{ // 点击按钮的位置的X坐标
			int eventX = (int) event.getRawX();
			// 点击按钮的位置的Y坐标
			int eventY = (int) event.getRawY();

			// 定义一个矩形
			Rect rect = new Rect();
			// 得到控件的矩形位置
			getGlobalVisibleRect(rect);
			// 重新定义矩形的宽度
			rect.left = rect.right - 50;
			// 判断点击位置是否在删除图片位置
			if (rect.contains(eventX, eventY))
			{
				setText("");
			}

		}
		return super.onTouchEvent(event);
	}

	public EditTextValidator getEditTextValidator()
	{
		return editTextValidator;
	}

	public void setEditTextValidator(EditTextValidator editTextValidator)
	{
		this.editTextValidator = editTextValidator;
	}

	public boolean validity()
	{
		return editTextValidator.validity();
	}

	private EditTextValidator editTextValidator;

	private Drawable lastErrorIcon = null;

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event)
	{
		if (TextUtils.isEmpty(getText().toString())
				&& keyCode == KeyEvent.KEYCODE_DEL) return true;
		else return super.onKeyPreIme(keyCode, event);
	}

	@Override
	public void setError(CharSequence error, Drawable icon)
	{
		super.setError(error, icon);
		lastErrorIcon = icon;

		if (error != null /* !isFocused() && */)
		{
			showErrorIconHax(icon);
		}
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect)
	{
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		showErrorIconHax(lastErrorIcon);
	}

	private void showErrorIconHax(Drawable icon)
	{
		if (icon == null) return;

		try
		{
			Class<?> textview = Class.forName("android.widget.TextView");
			Field tEditor = textview.getDeclaredField("mEditor");
			tEditor.setAccessible(true);
			Class<?> editor = Class.forName("android.widget.Editor");
			Method privateShowError = editor.getDeclaredMethod("setErrorIcon",
					Drawable.class);
			privateShowError.setAccessible(true);
			privateShowError.invoke(tEditor.get(this), icon);
		}
		catch (Exception e)
		{
		}
	}

}
