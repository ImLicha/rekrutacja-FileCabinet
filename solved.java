interface Cabinet {
    // zwraca dowolny element o podanej nazwie
    Optional<Folder> findFolderByName(String name);
    

    // zwraca wszystkie foldery podanego rozmiaru SMALL/MEDIUM/LARGE
    List<Folder> findFoldersBySize(String size);

    //zwraca liczbę wszystkich obiektów tworzących strukturę
    int count();
}

public class FileCabinet implements Cabinet {
    private List<Folder> folders;

    // funkcja pomocnicza do pobrania wszystkich folderów w jednowymiarową liste
    private List<Folder> getAllFolders(List<Folder> folders) {
        List<Folder> allFolders = new ArrayList<>();
        if(folders == null) return allFolders;
        for (Folder folder : folders) {
            allFolders.add(folder);
            if (folder instanceof MultiFolder) {
                allFolders.addAll(getAllFolders(((MultiFolder) folder).getFolders()));
            }
        }
        return allFolders;
    }

    @Override
    public Optional<Folder> findFolderByName(String name) {
        if(name == null) return Optional.empty();

        List<Folder> allFolders = getAllFolders(folders);
        for (Folder folder : allFolders) {
            if (folder.getName().equals(name)) {
                return Optional.of(folder);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Folder> findFoldersBySize(String size) {
        if (size == null || (!size.equals("SMALL") && !size.equals("MEDIUM") && !size.equals("LARGE"))) {
            throw new IllegalArgumentException("Entered invalid size: " + size);
        }
        List<Folder> result = new ArrayList<>();        
        List<Folder> allFolders = getAllFolders(folders);
        for (Folder folder : allFolders) {
            if (folder.getSize().equals(size)) {
                result.add(folder);
            }
        }
        return result;
    }

    @Override
    public int count() {
        return getAllFolders(folders).size();
    }
}

interface Folder {
    String getName();
    String getSize();
}

interface MultiFolder extends Folder {
    List<Folder> getFolders();
}