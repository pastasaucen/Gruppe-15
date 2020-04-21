package persistence;

import domain.Cast;
import domain.IPersistenceCast;
import domain.Production;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CastMembersHandler implements IPersistenceCast {

    @Override
    public List<Cast> getCastMembers(String searchString) {
        return null;
    }

    @Override
    public void saveCastMembers(List<Cast> castMembers) {

    }
}
