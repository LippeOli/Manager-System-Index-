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
            long RRN = isbn * offset;

            writer.write(manga.toString());
            writer.newLine();

            indexPrimWriter.write(isbn + "," + RRN);
            indexPrimWriter.newLine();

            indexSecWriter.write(titulo + "," + isbn);
            indexSecWriter.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        atualizarIndices();
        System.out.println("Manga adicionado com sucesso!");
    }

    private void atualizarIndices() {
        List<String> indexPrimario = new ArrayList<>();
        List<String> indexSecundario = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            long offset = 0;
            while ((linha = reader.readLine()) != null) {
                Manga manga = Manga.fromString(linha);
                int RRN = (int) (offset / 100); // Supondo que cada linha tem até 100 caracteres
                indexPrimario.add(manga.getIsbn() + "," + RRN);
                indexSecundario.add(manga.getTitulo() + "," + manga.getIsbn());
                offset += 100; // Incrementa o offset para a próxima linha
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        indexPrimario.sort(String::compareTo);
        indexSecundario.sort(String::compareTo);

        try (BufferedWriter indexPrimWriter = new BufferedWriter(new FileWriter(INDEX_PRIMARIO));
             BufferedWriter indexSecWriter = new BufferedWriter(new FileWriter(INDEX_SECUNDARIO))) {
            for (String linha : indexPrimario) {
                indexPrimWriter.write(linha);
                indexPrimWriter.newLine();
            }
            for (String linha : indexSecundario) {
                indexSecWriter.write(linha);
                indexSecWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Manga buscarPorTitulo(String titulo) {
        try (BufferedReader indexReader = new BufferedReader(new FileReader(INDEX_SECUNDARIO))) {
            List<String> indices = new ArrayList<>();
            String linha;
            while ((linha = indexReader.readLine()) != null) {
                indices.add(linha);
            }

            int index = buscaBinariaSecundario(indices, titulo);
            if (index >= 0) {
                String[] parts = indices.get(index).split(",");
                int isbn = Integer.parseInt(parts[1]);
                return buscarPorISBN(isbn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int buscaBinariaSecundario(List<String> indices, String titulo) {
        int esquerda = 0;
        int direita = indices.size() - 1;

        while (esquerda <= direita) {
            int meio = (esquerda + direita) / 2;
            String[] parts = indices.get(meio).split(",");
            int comparacao = parts[0].compareToIgnoreCase(titulo);

            if (comparacao == 0) {
                return meio;
            } else if (comparacao < 0) {
                esquerda = meio + 1;
            } else {
                direita = meio - 1;
            }
        }
        return -1;
    }
    public Manga buscarPorISBN(int isbn) {
        try (BufferedReader indexReader = new BufferedReader(new FileReader(INDEX_PRIMARIO))) {
            List<String> indices = new ArrayList<>();
            String linha;
            while ((linha = indexReader.readLine()) != null) {
                indices.add(linha);
            }

            int index = buscaBinariaPrimario(indices, isbn);
            if (index >= 0) {
                String[] parts = indices.get(index).split(",");
                long RRN = Integer.parseInt(parts[1]);
                return buscarPorRRN(RRN);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int buscaBinariaPrimario(List<String> indices, int isbn) {
        int esquerda = 0;
        int direita = indices.size() - 1;

        while (esquerda <= direita) {
            int meio = (esquerda + direita) / 2;
            String[] parts = indices.get(meio).split(",");
            int isbnAtual = Integer.parseInt(parts[0]);

            if (isbnAtual == isbn) {
                return meio;
            } else if (isbnAtual < isbn) {
                esquerda = meio + 1;
            } else {
                direita = meio - 1;
            }
        }
        return -1;
    }


    private Manga buscarPorRRN(long RRN) {
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

                long RRN = manga.getIsbn() * new File(FILE_NAME).length();
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
        System.out.println("Mangá alterado com sucesso!");
    }

    public void excluirManga(int isbn) {
        // Lê todos os registros, exceto o que deve ser excluído
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

        // Chama a função para compactar o arquivo e atualizar os índices
        compactarArquivo(mangas);
    }


    private void compactarArquivo(List<Manga> mangas) {
        // Escreve os registros não excluídos em um arquivo temporário
        File arquivoTemporario = new File(FILE_NAME + ".tmp");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemporario));
             BufferedWriter indexPrimWriter = new BufferedWriter(new FileWriter(INDEX_PRIMARIO));
             BufferedWriter indexSecWriter = new BufferedWriter(new FileWriter(INDEX_SECUNDARIO))) {

            long offset = 0;
            for (Manga manga : mangas) {
                String mangaStr = manga.toString();
                writer.write(mangaStr);
                writer.newLine();

                long RRN = offset;
                indexPrimWriter.write(manga.getIsbn() + "," + RRN);
                indexPrimWriter.newLine();

                indexSecWriter.write(manga.getTitulo() + "," + manga.getIsbn());
                indexSecWriter.newLine();

                offset += mangaStr.length() + System.lineSeparator().length();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Substitui o arquivo original pelo arquivo temporário
        File arquivoOriginal = new File(FILE_NAME);
        if (arquivoOriginal.delete()) {
            if (arquivoTemporario.renameTo(arquivoOriginal)) {
                System.out.println("Arquivo compactado com sucesso!"); // Sucesso na compactação do arquivo
            } else {
                System.out.println("Falha ao renomear o arquivo temporário.");
            }
        } else {
            System.out.println("Falha ao excluir o arquivo original.");
        }
    }

}