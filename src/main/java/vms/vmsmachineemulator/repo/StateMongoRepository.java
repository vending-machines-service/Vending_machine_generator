package vms.vmsmachineemulator.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import vms.vmsmachineemulator.dto.MachineDTO;

@Repository
public interface StateMongoRepository extends MongoRepository<MachineDTO, Integer> {

}
