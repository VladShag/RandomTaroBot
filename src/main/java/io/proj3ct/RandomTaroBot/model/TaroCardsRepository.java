package io.proj3ct.RandomTaroBot.model;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaroCardsRepository extends CrudRepository<TaroCard, Integer> {

}
