package exam.service;

import exam.model.entities.Laptop;

import java.io.IOException;
import java.util.List;


public interface LaptopService {
    boolean areImported();

    String readLaptopsFileContent() throws IOException;

    String importLaptops() throws IOException;

    String exportBestLaptops();

    Laptop findByMacAddress(String macAddress);

    List<Laptop> findLaptopsWithShopAndTown();
}
