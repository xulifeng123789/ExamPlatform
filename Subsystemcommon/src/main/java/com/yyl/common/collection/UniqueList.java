/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yyl.common.collection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author 
 * @author yangyl
 */
public class UniqueList<E> extends ArrayList<E> {

	public UniqueList() {
		super();
	}

	public UniqueList(Collection<E> c) {
		super(c.size());

		addAll(c);
	}

	public UniqueList(int initialCapacity) {
		super(initialCapacity);
	}

	@Override
	public boolean add(E e) {
		if (!contains(e)) {
			return super.add(e);
		}
		else {
			return false;
		}
	}

	@Override
	public void add(int index, E e) {
		if (!contains(e)) {
			super.add(index, e);
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean modified = false;

		for (E e : c) {
			if (!contains(e)) {
				super.add(e);

				modified = true;
			}
		}

		return modified;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		boolean modified = false;

		for (E e : c) {
			if (!contains(e)) {
				super.add(index++, e);

				modified = true;
			}
		}

		return modified;
	}

	@Override
	public E set(int index, E e) {
		Thread currentThread = Thread.currentThread();

		StackTraceElement[] stackTraceElements = currentThread.getStackTrace();

		if (stackTraceElements.length >= 4) {
			StackTraceElement stackTraceElement = stackTraceElements[3];

			String stackTraceElementString = stackTraceElement.toString();

			if (stackTraceElementString.contains(_STACK_TRACE_COLLECTIONS)) {
				return super.set(index, e);
			}
		}

		if (!contains(e)) {
			return super.set(index, e);
		}
		else {
			return e;
		}
	}

	private static final String _STACK_TRACE_COLLECTIONS =
		"java.util.Collections.sort(Collections.java";

}