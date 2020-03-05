package ru.devpav.registerconfresource.core.loader;

public interface Loader<T, E> {

    T load(E obj) throws Exception;

}
