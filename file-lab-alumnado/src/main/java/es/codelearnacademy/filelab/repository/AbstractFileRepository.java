package es.codelearnacademy.filelab.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractFileRepository<T, ID> implements IRepository<T, ID> {

    @Override
    public List<T> findAll() {
        try {
            return readAll();
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        List<T> list = findAll();
        for (T element : list) {
            if (getId(element).equals(id)) {
                return Optional.of(element);
            }
        }
        return Optional.empty();
    }
    @Override
    public boolean create(T entity) {
        List<T> list = new ArrayList<>();
        try {
            list = readAll();
        } catch (IOException e) {
            return false;
        }
        for (T element : list) {
            if (getId(element).equals(getId(entity))) {
                return false;
            }
        }
        list.add(entity);
        try {
            writeAll(list);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    @Override
    public boolean update(T entity) {
        List<T> list = new ArrayList<>();
        try {
            list = readAll();
        } catch (IOException e) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (getId(list.get(i)).equals(getId(entity))) {
                list.set(i, entity);
                try {
                    writeAll(list);
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean delete(ID id) {
        List<T> list = new ArrayList<>();
        try {
            list = readAll();
        } catch (IOException e) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (getId(list.get(i)).equals(id)) {
                list.remove(i);
                try {
                    writeAll(list);
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return false;
    }
    protected abstract ID getId(T entity);

    protected abstract List<T> readAll() throws IOException;

    protected abstract void writeAll(List<T> entities) throws IOException;
}