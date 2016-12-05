package server.persistence;

/**
 * Persistence Provider Singleton
 */
public class Persistence {

    private static IPersistenceProvider persistenceProvider;

    public static IPersistenceProvider getInstance(){
        return persistenceProvider;
    }

    public static void setPersistence(IPersistenceProvider persistenceProvider) {
        Persistence.persistenceProvider = persistenceProvider;
    }
}
