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
	// �����ɾ��ͼƬ��ť
	private Drawable imgAble;

	// ����ɾ��ͼƬ��ʾ��־
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
	 * ��ʼ��ɾ������ͼƬ
	 */
	private void init()
	{
		// ��ʼ��ɾ��ͼƬ
		imgAble = AppApplication.getInstance().getResources()
				.getDrawable(R.drawable.delete);
		// ����������
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
				// �������������־����ɾ��ͼƬ,�����õ�����
				if (length() >= 1 && flag == 1)
				{
					// ���������ұ����ͼƬ
					setCompoundDrawablesWithIntrinsicBounds(null, null,
							imgAble, null);
				}
				else
				{
					// ����ͼƬ
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
					// ͼƬ��ʾ��flagΪ1
					flag = 1;
					// �������������֣���ʾɾ��ͼƬ
					if (length() >= 1)
					{
						// ���������ұ����ͼƬ
						setCompoundDrawablesWithIntrinsicBounds(null, null,
								imgAble, null);
					}
				}
				else
				{
					// ����ͼƬ
					setCompoundDrawablesWithIntrinsicBounds(null, null, null,
							null);
					// ͼƬ���أ�flagΪ0
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
	 * ɾ���¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// �ж�ͼƬ�Ƿ����,ͼƬ�Ƿ���ʾ,���������¼�
		if (imgAble != null && flag == 1
				&& event.getAction() == MotionEvent.ACTION_DOWN)
		{ // �����ť��λ�õ�X����
			int eventX = (int) event.getRawX();
			// �����ť��λ�õ�Y����
			int eventY = (int) event.getRawY();

			// ����һ������
			Rect rect = new Rect();
			// �õ��ؼ��ľ���λ��
			getGlobalVisibleRect(rect);
			// ���¶�����εĿ��
			rect.left = rect.right - 50;
			// �жϵ��λ���Ƿ���ɾ��ͼƬλ��
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
