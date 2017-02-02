package com.nfl.graphql.mediator.glitr.type;


import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Copied directly from Glitr.  Any changes made there should be copied into here.
 *
 * See: https://github.com/nfl/glitr/blob/master/src/main/java/com/nfl/glitr/registry/type/Scalars.java
 */
public class GlitrScalars {

    private static final String UTC = "UTC";
    private static final String SHIELD_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    private static final String SHIELD_LOCAL_DATE_PATTERN = "yyyy-MM-dd";


    /**
     * {@code GraphQLDateTime} represents a date time as `2014-08-20T18:00:00.000Z`
     * ISO-8601 compliant and in UTC
     */
    public static final GraphQLScalarType GraphQLDateTime = new GraphQLScalarType("DateTime", "DateTime is represented in UTC with the following template: 2014-08-20T18:00:00.000Z", new Coercing() {

        @Override
        public Object serialize(Object input) {
            if (input == null) {
                return null;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SHIELD_DATE_TIME_PATTERN).withZone(ZoneId.of(UTC));

            if (input instanceof ZonedDateTime) {
                ZonedDateTime time = (ZonedDateTime) input;
                return time.format(formatter);
            }

            if (input instanceof Instant) {
                Instant time = (Instant) input;
                return formatter.format(time);
            }

            throw new IllegalArgumentException("Can't serialize type " + input.getClass() + " with value " + input.toString());
        }

        @Override
        public Object parseValue(Object input) {
            return serialize(input);
        }

        /**
         * Always parse to {@code Instant}.
         *
         * @param input date time string representation
         * @return date as an {@code Instant}
         */
        @Override
        public Object parseLiteral(Object input) {
            if (!(input instanceof StringValue)) return null;
            String encodedDateTime = ((StringValue) input).getValue();

            return Instant.parse(encodedDateTime);
        }
    });

    /**
     * {@code GraphQLDate} represents a date as `2014-08-20` without any timezone information.
     * This template can be used only for description of date but can't be represented as an instant on the time-line.
     * A birth date is the perfect use case.
     */
    public static final GraphQLScalarType GraphQLDate = new GraphQLScalarType("Date", "Date represents a local date without any timezone " +
            "information therefore should only be used for displaying purposes. The template is 2014-08-20", new Coercing() {

        @Override
        public Object serialize(Object input) {
            if (input == null) {
                return null;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SHIELD_LOCAL_DATE_PATTERN).withZone(ZoneId.of(UTC));

            if (input instanceof LocalDate) {
                LocalDate time = (LocalDate) input;
                return time.format(formatter);
            }

            if (input instanceof ZonedDateTime) {
                ZonedDateTime time = (ZonedDateTime) input;
                return time.format(formatter);
            }

            if (input instanceof Instant) {
                Instant time = (Instant) input;
                return formatter.format(time);
            }

            throw new IllegalArgumentException("Can't serialize type " + input.getClass() + " with value " + input.toString());
        }

        @Override
        public Object parseValue(Object input) {
            return serialize(input);
        }

        /**
         * Always parse to {@code LocalDate}.
         *
         * @param input date time string representation
         * @return date as an {@code LocalDate}
         */
        @Override
        public Object parseLiteral(Object input) {
            if (!(input instanceof StringValue)) return null;
            String encodedDateTime = ((StringValue) input).getValue();

            return LocalDate.parse(encodedDateTime);
        }
    });
}
