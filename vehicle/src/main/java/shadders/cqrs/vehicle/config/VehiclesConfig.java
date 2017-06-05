/*
 * Copyright (c) 2010-2016. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package shadders.cqrs.vehicle.config;

import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import shadders.cqrs.vehicle.aggregate.Vehicle;

@Configuration
public class VehiclesConfig {

    @Autowired
    private EventStore eventStore;

    @Bean
    @Scope("prototype")
    public Vehicle vehicle() {
        return new Vehicle();
    }

    @Bean
    EventSourcingRepository<Vehicle> vehicleRepository() {
        EventSourcingRepository<Vehicle> repo
                = new EventSourcingRepository<Vehicle>(Vehicle.class, eventStore);

        return repo;
    }



}