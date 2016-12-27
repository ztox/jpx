/*
 * Java GPX Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics.jpx;

import java.util.Random;
import java.util.function.Supplier;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version 1.0
 * @since 1.0
 */
@Test
public class BoundsTest extends XMLStreamTestBase<Bounds> {

	@Override
	public Supplier<Bounds> factory(Random random) {
		return () -> nextBounds(random);
	}

	@Override
	protected Params<Bounds> params(final Random random) {
		return new Params<>(
			() -> nextBounds(random),
			Bounds.reader(),
			Bounds::write
		);
	}

	public static Bounds nextBounds(final Random random) {
		return Bounds.of(
			Latitude.ofDegrees(random.nextInt(90)),
			Longitude.ofDegrees(random.nextInt(90)),
			Latitude.ofDegrees(random.nextInt(90)),
			Longitude.ofDegrees(random.nextInt(90))
		);
	}

}