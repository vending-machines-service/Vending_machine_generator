
package vms.vmsmachineemulator.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import vms.vmsmachineemulator.entity.MachineJPA;

public interface MachinesSqlRepository extends JpaRepository<MachineJPA, Integer> {

}