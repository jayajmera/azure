package springboot.storeApp.repository;

import org.springframework.data.repository.CrudRepository;

import springboot.storeApp.model.AppUser;



public interface AppUserRepository extends CrudRepository<AppUser, Integer>{

}


