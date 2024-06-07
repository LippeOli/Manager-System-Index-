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

    public boolean isbnExiste(int isbn) {
        try (BufferedReader indexReader = new BufferedReader(new FileReader(INDEX_PRIMARIO))) {
            String linha;
            while ((linha = indexReader.readLine()) != null) {
                String[] parts = linha.split(",");
                if (Integer.parseInt(parts[0]) == isbn) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addManga(int isbn, String titulo, int anoIni, int anoFim, String autor, String genero, String revista, String editora, int anoEdi, int quantVolAdq) {

        if (isbnExiste(isbn)) {
            System.out.println("ISBN já existe. Mangá não adicionado.");
            return;
        }

        Manga manga = new Manga(isbn, titulo, anoIni, anoFim, autor, genero, revista, editora, anoEdi, quantVolAdq);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
             BufferedWriter indexPrimWriter = new BufferedWriter(new FileWriter(INDEX_PRIMARIO, true));
             BufferedWriter indexSecWriter = new BufferedWriter(new FileWriter(INDEX_SECUNDARIO, true))) {

            long offset = new File(FILE_NAME).length();
            int RRN = (int) (offset / 100); // Supondo que cada linha tem até 100 caracteres

            writer.write(manga.toString());
            writer.newLine();

            indexPrimWriter.write(isbn + "," + RRN);
            indexPrimWriter.newLine();

            indexSecWriter.write(titulo + "," + isbn);
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
                    int isbn = Integer.parseInt(parts[1]);
                    return buscarPorISBN(isbn);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Manga buscarPorISBN(int isbn) {
        try (BufferedReader indexReader = new BufferedReader(new FileReader(INDEX_PRIMARIO))) {
            String linha;
            while ((linha = indexReader.readLine()) != null) {
                String[] parts = linha.split(",");
                if (Integer.parseInt(parts[0]) == isbn) {
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

    public void alterarMangas(int isbnAtualizar, String novoTitulo, int novoAnoIni, int novoAnoFim, String novoAutor, String novoGenero, String novaRevista, String novaEditora, int novoAnoEdi, int novaQuantVolAdq) {
        List<Manga> mangas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Manga manga = Manga.fromString(linha);
                if (manga.getIsbn() == isbnAtualizar) {
                    manga = new Manga(isbnAtualizar, novoTitulo, novoAnoIni, novoAnoFim, novoAutor, novoGenero, novaRevista, novaEditora, novoAnoEdi, novaQuantVolAdq);
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
                indexPrimWriter.write(manga.getIsbn() + "," + RRN);
                indexPrimWriter.newLine();

                indexSecWriter.write(manga.getTitulo() + "," + manga.getIsbn());
                indexSecWriter.newLine();

                offset = raf.getFilePointer();
            }

            raf.setLength(offset);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void excluirManga(int isbn) {
        List<Manga> mangas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Manga manga = Manga.fromString(linha);
                if (manga.getIsbn() != isbn) {
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
                indexPrimWriter.write(manga.getIsbn() + "," + RRN);
                indexPrimWriter.newLine();

                indexSecWriter.write(manga.getTitulo() + "," + manga.getIsbn());
                indexSecWriter.newLine();

                offset = raf.getFilePointer();
            }

            raf.setLength(offset);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}