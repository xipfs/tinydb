package net.xipfs.tinynosql.core.sstable.filters;

import java.nio.ByteBuffer;

import net.xipfs.tinynosql.core.interfaces.StringHasher;
import net.xipfs.tinynosql.core.interfaces.WritableFilter;

public class BloomFilter implements WritableFilter {
	private BitSet bitset;
	private StringHasher hasher;

	public BloomFilter(int numBits, StringHasher hasher) {
		this.bitset = new BitSet(numBits);
		this.hasher = hasher;
	}

	public BloomFilter(long[] words, StringHasher hasher) {
		this.bitset = new BitSet(words);
		this.hasher = hasher;
	}

	public boolean isPresent(String key) {
		for (int off : bitOffsets(key)) {
			if (!bitset.get(off))
				return false;
		}
		return true;
	}

	public void add(String key) {
		for (int off : bitOffsets(key)) {
			bitset.set(off);
		}
	}

	@SuppressWarnings("unused")
	private int bitOffset(String key) {
		long hash = hasher.hash(key);
		return ((int) Long.remainderUnsigned(hash, bitset.numLongs() * Long.SIZE));
	}

	private int[] bitOffsets(String key) {
		int[] offsets = new int[1];
		long hash = hasher.hash(key);
		for (int i = 0; i < 1; i++) {
			offsets[i] = bitOffset(hash);
			ByteBuffer bf = ByteBuffer.allocate(Long.BYTES);
			bf.putLong(hash);
			hash = hasher.hash(new String(bf.array()));
		}
		return offsets;
	}

	private int bitOffset(long hash) {
		return ((int) Long.remainderUnsigned(hash, bitset.numLongs() * Long.SIZE));
	}

	public long[] toLongs() {
		return bitset.toLongs();
	}
}
