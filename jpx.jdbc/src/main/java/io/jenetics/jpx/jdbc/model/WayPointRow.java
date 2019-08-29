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
package io.jenetics.jpx.jdbc.model;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import io.jenetics.jpx.jdbc.internal.db.BatchQuery;
import io.jenetics.jpx.jdbc.internal.db.Param;
import io.jenetics.jpx.jdbc.internal.db.RowParser;
import io.jenetics.jpx.jdbc.internal.db.SQLQuery;
import io.jenetics.jpx.jdbc.internal.db.Stored;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@Accessors(fluent = true)
public class WayPointRow {
	private final double latitude;
	private final double longitude;
	private final Double elevation;
	private final Double speed;
	private final ZonedDateTime time;
	private final Double magneticVariation;
	private final Double geoidHeight;
	private final String name;
	private final String comment;
	private final String description;
	private final String source;
	private final String symbol;
	private final String type;
	private final String fix;
	private final Integer sat;
	private final Double hdop;
	private final Double vdop;
	private final Double pdop;
	private final Integer ageOfGPSData;
	private final Integer dgpsID;
	private final Double course;
	private final String extensions;

	private static final RowParser<Stored<WayPointRow>> ROW_PARSER = rs -> Stored.of(
		rs.getLong("id"),
		WayPointRow.builder()
			.latitude(rs.getDouble("lat"))
			.longitude(rs.getDouble("lon"))
			.elevation(rs.getDouble("ele"))
			.speed(rs.getDouble("speed"))
			.time(rs.getZonedDateTime("time"))
			.magneticVariation(rs.getDouble("magvar"))
			.geoidHeight(rs.getDouble("geoidheight"))
			.name(rs.getString("name"))
			.comment(rs.getString("cmt"))
			.description(rs.getString("desc"))
			.source(rs.getString("src"))
			.symbol(rs.getString("sym"))
			.type(rs.getString("type"))
			.fix(rs.getString("fix"))
			.sat(rs.getInteger("sat"))
			.hdop(rs.getDouble("hdop"))
			.vdop(rs.getDouble("vdop"))
			.pdop(rs.getDouble("pdop"))
			.ageOfGPSData(rs.getInteger("ageofdgpsdata"))
			.dgpsID(rs.getInteger("dgpsid"))
			.course(rs.getDouble("course"))
			.extensions(rs.getString("extensions"))
			.build()
	);

	private static final String INSERT_SQL =
		"INSERT INTO way_point(" +
			"lat, " +
			"lon, " +
			"ele, " +
			"speed, " +
			"time, " +
			"magvar, " +
			"geoidheight, " +
			"name, " +
			"cmt, " +
			"desc, " +
			"src," +
			"sym, " +
			"type, " +
			"fix, " +
			"sat, " +
			"hdop, " +
			"vdop, " +
			"pdop, " +
			"ageofdgpsdata, " +
			"dgpsid, " +
			"course, " +
			"extensions" +
		")" +
		"VALUES(" +
			"{lat}, " +
			"{lon}, " +
			"{ele}, " +
			"{speed}, " +
			"{time}, " +
			"{magvar}, " +
			"{geoidheight}, " +
			"{name}, " +
			"{cmt}, " +
			"{desc}, " +
			"{src}," +
			"{sym}, " +
			"{type}, " +
			"{fix}, " +
			"{sat}, " +
			"{hdop}, " +
			"{vdop}, " +
			"{pdop}, " +
			"{ageofdgpsdata}, " +
			"{dgpsid}" +
			"{course}, " +
			"{extensions} " +
		")";

		private static final String SELECT_ALL_SQL =
			"SELECT id, " +
				"lat, " +
				"lon, " +
				"ele, " +
				"speed, " +
				"time, " +
				"magvar, " +
				"geoidheight, " +
				"name, " +
				"cmt, " +
				"desc, " +
				"src," +
				"sym, " +
				"type, " +
				"fix, " +
				"sat, " +
				"hdop, " +
				"vdop, " +
				"pdop, " +
				"ageofdgpsdata, " +
				"dgpsid " +
			"FROM way_point " +
			"ORDER BY id";


	public Stored<WayPointRow> insert(final Connection connection)
		throws SQLException
	{
		return insert(singletonList(this), connection).get(0);
	}

	public List<Stored<WayPointRow>> insert(
		final Collection<WayPointRow> points,
		final Connection connection
	)
		throws SQLException
	{
		final BatchQuery query = new BatchQuery(connection, INSERT_SQL);

		return query.insert(
			points,
			point -> asList(
				Param.value("lat", point.latitude()),
				Param.value("lon", point.longitude()),
				Param.value("ele", point.elevation()),
				Param.value("speed", point.speed()),
				Param.value("time", point.time()),
				Param.value("magvar", point.magneticVariation()),
				Param.value("geoidheight", point.geoidHeight()),
				Param.value("name", point.name()),
				Param.value("cmt", point.comment()),
				Param.value("desc", point.description()),
				Param.value("src", point.source()),
				Param.value("sym", point.symbol()),
				Param.value("type", point.type()),
				Param.value("fix", point.fix()),
				Param.value("sat", point.sat()),
				Param.value("hdop", point.hdop()),
				Param.value("vdop", point.vdop()),
				Param.value("pdop", point.pdop()),
				Param.value("ageofdgpsdata", point.ageOfGPSData()),
				Param.value("dgpsid", point.dgpsID()),
				Param.value("course", point.course()),
				Param.value("extensions", point.elevation())
			)
		);
	}

	public static List<Stored<WayPointRow>> selectAll(final Connection connection)
		throws SQLException
	{
		return new SQLQuery(connection, SELECT_ALL_SQL).as(ROW_PARSER.list());
	}

	public static void main(final String[] args) {
		WayPointRow row = WayPointRow.builder()
			.build();
	}
}