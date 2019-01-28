package net.xipfs.tinynosql.utils;

import java.util.Objects;

public class Timed<T> implements Comparable<Timed<T>> {
	private T val;
	private long timestamp;

	public Timed(T val) {
		this(val, System.currentTimeMillis());
	}

	public Timed(T val, long timestamp) {
		this.val = val;
		this.timestamp = timestamp;
	}

	public static <T> Timed<T> now(T t) {
		return new Timed<T>(t);
	}

	public T get() {
		return this.val;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Timed) {
			final Timed<T> that = (Timed<T>) o;
			return Objects.equals(this.val, that.val) && this.timestamp == that.timestamp;
		}
		return false;
	}

	public int compareTo(Timed<T> other) {
		return (int) (this.timestamp - other.getTimestamp());
	}
}
