package core.neural_network.mlp;

import java.util.Iterator;

public class utils {
	public static Iterable<Integer> range(final int max){
		return new Iterable<Integer>() {
			
			@Override
			public Iterator<Integer> iterator() {
				return new Iterator<Integer>() {
					private int nMax = max; 
					private int actual = 0;
					@Override
					public boolean hasNext() {
						return actual >= nMax;
					}

					@Override
					public Integer next() {
						return ++actual;
					}

					@Override
					public void remove() {
						//do not use
					}
				};
			}
		};
	}
}
