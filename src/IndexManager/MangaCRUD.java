package IndexManager;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MangaCRUD {
    private static final String FILE_NAME = getFilePath("mangas.txt");
    private static final String INDEX_PRIMARIO = getFilePath("indicePrimario.txt");
    private static final String INDEX_SECUNDARIO = getFilePath("indiceSecundario.txt");

    private static String getFilePath(String fileName) {
        URL resource = MangaCRUD.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            // File does not exist, create it in the resources folder path
            String resourcePath = "src/resources/" + fileName;
            try {
                File file = new File(resourcePath);
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resourcePath;
        } else {
            return Paths.get(resource.getPath()).toString();
        }
    }

        public void addManga(int id, String titulo, int anoIni, String genero ) {
        Manga manga = new Manga(id, titulo, anoIni, genero);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
             BufferedWriter indexPrimWriter = new BufferedWriter(new FileWriter(INDEX_PRIMARIO, true));
             BufferedWriter indexSecWriter = new BufferedWriter(new FileWriter(INDEX_SECUNDARIO, true))) {


            long offset = new File(FILE_NAME).length();
            int RRN = (int) (offset / manga.toString().length()); // Simplificação para RRN

            writer.write(manga.toString());
            writer.newLine();

            indexPrimWriter.write(id + "," + RRN);
            indexPrimWriter.newLine();

            indexSecWriter.write(titulo + "," + id);
            indexSecWriter.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Manga buscarPorTitulo(String titulo) {
        try (BufferedReader indexReader = new BufferedReader(new FileReader(INDEX_SECUNDARIO))) {
            String linha;
            while ((linha = indexReader.readLine()) != null) {
                String[] parts = linha.split(",");
                if (parts[0].equalsIgnoreCase(titulo)) {
                    int numeroSerie = Integer.parseInt(parts[1]);
                    return buscarPorNumeroSerie(numeroSerie);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Manga buscarPorNumeroSerie(int numeroSerie) {
        try (BufferedReader indexReader = new BufferedReader(new FileReader(INDEX_PRIMARIO))) {
            String linha;
            while ((linha = indexReader.readLine()) != null) {
                String[] parts = linha.split(",");
                if (Integer.parseInt(parts[0]) == numeroSerie) {
                    int RRN = Integer.parseInt(parts[1]);
                    return buscarPorRRN(RRN);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Manga buscarPorRRN(int RRN) {
        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "r")) {
            raf.seek(RRN * 100); // Supondo que cada linha tem até 100 caracteres
            String linha = raf.readLine();
            if (linha != null) {
                return Manga.fromString(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void listarMangas() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Manga manga = Manga.fromString(linha);
                System.out.println(manga);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void alterarMangas(int id, String novoTitulo, int novoAnoIni, String novoGenero) {
        List<Manga> mangas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Manga manga = Manga.fromString(linha);
                if (manga.getId() == id) {
                    manga = new Manga(id, novoTitulo, novoAnoIni, novoGenero);
                }
                mangas.add(manga);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw");
             BufferedWriter indexPrimWriter = new BufferedWriter(new FileWriter(INDEX_PRIMARIO));
             BufferedWriter indexSecWriter = new BufferedWriter(new FileWriter(INDEX_SECUNDARIO))) {

            long offset = 0;
            for (Manga manga : mangas) {
                String mangaStr = manga.toString() + System.lineSeparator();
                raf.seek(offset);
                raf.writeBytes(mangaStr);

                int RRN = (int) (offset / manga.toString().length());
                indexPrimWriter.write(manga.getId() + "," + RRN);
                indexPrimWriter.newLine();

                indexSecWriter.write(manga.getTitulo() + "," + manga.getId());
                indexSecWriter.newLine();

                offset = raf.getFilePointer();
            }

            raf.setLength(offset);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void excluirManga(int id) {
        List<Manga> mangas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Manga manga = Manga.fromString(linha);
                if (manga.getId() != id) {
                    mangas.add(manga);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "rw");
             BufferedWriter indexPrimWriter = new BufferedWriter(new FileWriter(INDEX_PRIMARIO));
             BufferedWriter indexSecWriter = new BufferedWriter(new FileWriter(INDEX_SECUNDARIO))) {

            long offset = 0;
            for (Manga manga : mangas) {
                String mangaStr = manga.toString() + System.lineSeparator();
                raf.seek(offset);
                raf.writeBytes(mangaStr);

                int RRN = (int) (offset / manga.toString().length());
                indexPrimWriter.write(manga.getId() + "," + RRN);
                indexPrimWriter.newLine();

                indexSecWriter.write(manga.getTitulo() + "," + manga.getId());
                indexSecWriter.newLine();

                offset = raf.getFilePointer();
            }

            raf.setLength(offset);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}