/*
 * Copyright 2014 JBoss Inc
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
 */
package org.hibernate.bugs;

import org.hibernate.Session;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class DynamicMapWithCompositeId extends BaseCoreFunctionalTestCase {

    @Override
    protected String[] getMappings() {
        return new String[] {"CompId.hbm.xml"};
    }

    @Override
    protected void configure(Configuration configuration) {
        super.configure( configuration );

        configuration.setProperty( AvailableSettings.SHOW_SQL, Boolean.TRUE.toString() );
        configuration.setProperty( AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString() );
    }

    @Test
    public void testImplicitCompositeIdInDynamicMapMode() {
        Session session = openSession();
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                connection.createStatement().execute("insert into CompId values ('1', '2')");
                connection.commit();
            }
        });
        HashMap<Object, Object> id = new HashMap<>();
        id.put("id1", "1");
        id.put("id2", "2");
        session.get("CompId", id);
    }
}