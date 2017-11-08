/*
 * Licensed to Laurent Broudoux (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.github.microcks.jenkins.plugin.model;

import hudson.model.TaskListener;

/**
 * @author laurent
 */
public interface ITimedMicrocksPlugin extends IMicrocksPlugin {

   enum TimeoutUnit {
      MILLISECONDS("milli",1),
      SECONDS("sec",1000),
      MINUTES("min",1000*60);

      public final String name;
      public final long multiplier;

      TimeoutUnit(String name, long multiplier) {
         this.name = name;
         this.multiplier = multiplier;
      }

      @Override
      public String toString() {
         return name;
      }

      public boolean matches(String value) {
         if (value == null || value.trim().isEmpty()) {
            return false;
         }
         return name.equalsIgnoreCase(value);
      }

      public static TimeoutUnit getByName( String unit ) {
         if (unit == null || unit.trim().isEmpty()) {
            // If units are not specified, fallback to millis for backwards compatibility.
            unit = "milli";
         }

         unit = unit.trim();

         for ( TimeoutUnit tu : TimeoutUnit.values() ) {
            if ( tu.name.equalsIgnoreCase( unit ) ) {
               return tu;
            }
         }

         throw new IllegalArgumentException("Unexpected time unit name: " + unit);
      }

      public static String normalize(String value) {
         if (value == null || value.trim().isEmpty()) {
            // Default to milliseconds for backwards compatibility
            return MILLISECONDS.toString();
         } else {
            return value.trim().toString();
         }
      }

      public long toMilliseconds(String value, long def) {
         if ( value == null || value.trim().isEmpty() ) {
            return def;
         }
         long l = Long.parseLong(value);
         l *= multiplier;
         return l;
      }

   }

   public String getWaitTime();

   public String getWaitUnit();

   long getGlobalTimeoutConfiguration();

   default long getTimeout(TaskListener listener, boolean chatty) {
      long global = getGlobalTimeoutConfiguration();

      TimeoutUnit unit = TimeoutUnit.getByName( getWaitUnit() );
      long chosen = unit.toMilliseconds(getWaitTime(), global);

      if (chatty) {
         listener.getLogger().println( "Found global job type timeout configuration: "  + global + " milliseconds" );
         if (getWaitTime() == null || getWaitTime().trim().isEmpty()) {
            listener.getLogger().println( "No local timeout configured for this step" );
         } else {
            listener.getLogger().println( "Local step timeout configuration: " + getWaitTime() + " " + unit );
         }
      }

      listener.getLogger().println( "Operation will timeout after " + chosen + " milliseconds" );
      return chosen;
   }
}
