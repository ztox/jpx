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
package io.jenetics.jpx.jdbc.internal.querily;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import io.jenetics.jpx.jdbc.internal.querily.Param.Value;

/**
 * This class represents a <em>deconstructor</em> for a given (record) class. It
 * allows to extract the fields, inclusively names, from a given record.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public final class Dctor<T>
	implements SqlFunction3<T, String, Connection, Value>
{

	/**
	 * Deconstructed field from a record class of type {@code T}.
	 *
	 * @param <T> the record type this field belongs to
	 */
	public static final class Field<T, R>
		implements SqlFunction2<T, Connection, R>
	{
		private final String _name;
		private final SqlFunction2<? super T, Connection, ? extends R> _value;

		private Field(
			final String name,
			final SqlFunction2<? super T, Connection, ? extends R> value
		) {
			_name = requireNonNull(name);
			_value = requireNonNull(value);
		}

		/**
		 * Return the name of the record field.
		 *
		 * @return the field name
		 */
		public String name() {
			return _name;
		}

		/**
		 * Return the field value from the given {@code record} instance.
		 *
		 * @param record the record from where to fetch the field value
		 * @return the record field value
		 */
		@Override
		public R apply(final T record, final Connection conn)
			throws SQLException
		{
			return _value.apply(record, conn);
		}

		/**
		 * Create a new record field with the given {@code name} and field
		 * {@code accessor}.
		 *
		 * @param name the field name
		 * @param value the field accessor
		 * @param <T> the record type
		 * @param <R> the field type
		 * @return a new record field
		 */
		public static <T, R> Field<T, R> of(
			final String name,
			final SqlFunction2<? super T, Connection, ? extends R> value
		) {
			return new Field<>(name, value);
		}

		public static <A, T, R> Field<A, R> of(
			final String name,
			final Function<? super A, ? extends T> mapper,
			final SqlFunction2<? super T, Connection, ? extends R> value
		) {
			return new Field<>(name, (r, c) -> value.apply(mapper.apply(r), c));
		}

		public static <T, R> Field<T, R> of(
			final String name,
			final SqlFunction<? super T, ? extends R> value
		) {
			return new Field<>(name, (record, conn) -> value.apply(record));
		}

		public static <T, R> Field<T, R> ofValue(
			final String name,
			final R value
		) {
			return new Field<>(name, (record, conn) -> value);
		}
	}

	private final List<Field<T, ?>> _fields;

	private Dctor(final List<Field<T, ?>> fields) {
		_fields = unmodifiableList(fields);
	}

	public List<Field<T, ?>> fields() {
		return _fields;
	}

	@Override
	public Value apply(final T record, final String name, final Connection conn)
		throws SQLException
	{
		for (Field<T, ?> field : _fields) {
			if (Objects.equals(name, field.name())) {
				return Value.of(field.apply(record, conn));
			}
		}

		return null;
	}

	@SafeVarargs
	public static <T> Dctor<T> of(final Field<T, ?>... fields) {
		return new Dctor<>(asList(fields));
	}

	public static <T> Dctor<T> of(final List<Field<T, ?>> fields) {
		return new Dctor<>(new ArrayList<>(fields));
	}

}