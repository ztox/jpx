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
package jpx;

import static java.lang.String.format;

import java.util.Random;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 */
@Test
public class EmailTest extends XMLStreamTestBase<Email> {

	@Override
	protected Params<Email> params(final Random random) {
		return new Params<>(
			() -> nextEmail(random),
			Email.reader(),
			Email::write
		);
	}

	public static Email nextEmail(final Random random) {
		return Email.of(
			format("id_%s", random.nextInt(100)),
			format("domain_%s", random.nextInt(100))
		);
	}

}
