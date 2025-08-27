package dev.shoxruhjon.services;

import dev.shoxruhjon.models.Train;

import java.util.List;
import java.util.Optional;

public interface ITrainService {

    void seedTestTrains();

    List<Train> getTrains();

    Optional<Train> findById(String id);

    void printUpcomingTop5();

    List<Train> printAll(boolean selectMode);
}
