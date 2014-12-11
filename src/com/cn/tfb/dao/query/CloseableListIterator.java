package com.cn.tfb.dao.query;

import java.io.Closeable;
import java.util.ListIterator;

public interface CloseableListIterator<T> extends ListIterator<T>, Closeable
{

}